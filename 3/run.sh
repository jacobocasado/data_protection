#!/bin/bash
rm -rf buil-jar > /dev/null 2>&1
rm *.class > /dev/null 2>&1
javac *.java
jar cfm SimpleSec.jar Manifest.txt *.class
rm *.class
chmod +x SimpleSec.jar
