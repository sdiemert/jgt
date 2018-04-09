package com.sdiemert.jgt;


import java.util.*;

/**
 * A class for representing a system of graph transformation rules.
 *
 * Things this class should does:
 *  - Allow non-deterministic application of rules, creates record of application.
 *  - List all rules with applicable morphisms.
 */
public class GTSystem {

    ArrayList<Rule> rules;

    public GTSystem(){
         this.rules = new ArrayList<Rule>();
    }

    /**
     * Adds a new rule to the set of rules in this GT system.
     *
     * @param r the rule to add.
     */
    public void addRule(Rule r){
        assert(r != null);
        this.rules.add(r);
    }

    /**
     * Adds a variable (one or more) number of Rules to the GTSystem.
     *
     * @param rules a variable number of Rules to add.
     */
    public void addRules(Rule... rules){
        for(Rule r : rules){
            this.addRule(r);
        }
    }

    /**
     * Attempts to apply a Rule (non-deterministically chosen) from the list of Rules.
     *
     * @param host the graph to attempt to apply the rule too.
     * @return Either a RuleApplication (morphism and rule pair) or null if no rule was applied.
     * @throws GraphException if there is an error applying the rule.
     */
    public RuleApplication apply(Graph host) throws GraphException{

        HashMap<Rule, Morphism> candidates = this.candidates(host);

        ArrayList<Rule> cRules = new ArrayList<Rule>(candidates.keySet());

        if(cRules.size() <= 0){
            return null;
        }

        Random rand = new Random();
        Rule toApply = cRules.get(rand.nextInt(cRules.size()));
        Boolean ret = toApply.apply(host, candidates.get(toApply));

        if(!ret){
            return null;
        }else{
            return new RuleApplication(toApply, candidates.get(toApply));
        }
    }

    /**
     * Determines candidate Rules to apply to a given host Graph and returns a mapping containing
     * Morphisms.
     *
     * @param host the graph to search for matches on.
     * @return a mapping of rules and morphisms, only contains rule/morphism pairs that actually have match.
     */
    public HashMap<Rule, Morphism> candidates(Graph host){
        HashMap<Rule, Morphism> map = new HashMap<Rule, Morphism>();
        for(Rule r : this.rules){
            Morphism m = r.findMatch(host);
            if(m != null){
                map.put(r, m);
            }
        }
        return map;
    }



    public List<Rule> getRules(){
        return this.rules;
    }

}
