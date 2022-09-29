#!/bin/bash
rm -rf buil-jar
rm *.class
javac *.java
jar cfm SecureSec.jar Manifest.txt *.class
rm *.class
chmod +x SecureSec.jar
