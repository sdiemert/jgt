#!/bin/bash

mvn install:install-file  -Dfile=./lib/com.microsoft.z3.jar -DgroupId=com.microsoft -DartifactId=z3 -Dversion=4.8.3 -Dpackaging=jar -DgeneratePom=true

mvn install:install-file  -Dfile=./lib/jgraphx-3.9.3.jar -DgroupId=com.mxgraph -DartifactId=jgraphx -Dversion=4.5.1 -Dpackaging=jar -DgeneratePom=true
