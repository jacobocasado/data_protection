#!/bin/bash
rm -rf buil-jar
rm *.class
javac *.java
jar cfm SimpleSec.jar Manifest.txt *.class
rm *.class
chmod +x SimpleSec.jar
