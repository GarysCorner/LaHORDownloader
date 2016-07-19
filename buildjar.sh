#!/bin/bash

echo "Cleaning up jar file"
rm LaHORDownloader.jar 

cd net/garyscorner/lahordownloader/

echo "Cleaning class files"
rm *.class

echo "Compiling"
javac *.java

cd ../../../

jar cvfm LaHORDownloader.jar MANIFEST.MF  net/garyscorner/lahordownloader/* LICENSE.md README.md

echo "Removing class files"
rm net/garyscorner/lahordownloader/*.class

echo " "
echo "Done!"
