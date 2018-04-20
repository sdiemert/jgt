package com.sdiemert.jgt.fs;

import com.sdiemert.jgt.core.GTSystem;
import com.sdiemert.jgt.core.Graph;
import com.sdiemert.jgt.core.GraphException;
import com.sdiemert.jgt.core.RuleException;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Builds up a GTSystem and/or Graphs from the contents of a file.
 */
public abstract class FileParser {

    public abstract Graph loadGraph(String filePath) throws IOException, JSONException, GraphException;

    public abstract GTSystem loadSystem(String filePath) throws IOException, JSONException, GraphException, RuleException;

    JSONObject fromFile(String path) throws IOException {
        File f = new File(path);
        String c = FileUtils.readFileToString(f, "utf-8");
        return new JSONObject(c);
    }
}
