#!/bin/bash

mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent package sonar:sonar \
    -Dsonar.host.url=https://sonarcloud.io \
    -Dsonar.organization=sdiemert-github\
    -Dsonar.login=8d4cca738baa10c795dff0edb324f0470a92db9c

