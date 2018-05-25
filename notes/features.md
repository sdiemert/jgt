
## Features

### CLI

+ Make new graphs
+ Make new rules and systems
+ Apply rules to graphs
+ Read/Write graphs and systems to files.
+ Merge multiple systems together
+ Invoke various analysis (e.g., critical pairs, etc.)
+ Command memory. 

### UI

+ Implements the CLI
+ Provides a visual representation of the graphs as they are built by the CLI.

### Code Generation

+ Given a System specification, generates a library of rules that can be included directly in a project
+ Given a Graph specification, generates a routine to generate the corresponding graph object.

It seems a good approach might be to generate a library of classes or a jar. This library would contain:

- Access to the GTSystem object with the spec'd rules.

A JAR here is ideal as it makes it difficult for the user to edit the rules and then loose backwards compatibility with the original specification.

### Interfaces

+ Interfacing with Groove to facilitate model checking.
    - Read Groove systems
    - Write to Grove systems
    - Call groove directly to do model checking?

### Analysis

+ Critical pair analysis

It would be nice to have sort of termination analysis - but it is unclear what that would be? Need to do more reading.

### Documentation

- Writeup about GT basics (wiki?)
- Tutorials
- GIFs of CLI and UI together
- Pick a reasonable license
- Basic README
- Write a paper

### Devops

- Maven integration (?)
- Setup some sort of continuous integration (Travis CI?)

### Tests

- Performance Tests