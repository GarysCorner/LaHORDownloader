#!/bin/bash

echo "Cleaning build..."
rm LaHORDownloader.jar
#rm net/garyscorner/lahordownloader/*.class

echo "Compiling *.class files
javac net/garyscorner/lahordownloader/AboutDialog.java
javac net/garyscorner/lahordownloader/DownloadThread.java
javac net/garyscorner/lahordownloader/DownloadProgress.java
javac net/garyscorner/lahordownloader/HelpDialog.java
javac net/garyscorner/lahordownloader/MainDialog.java
javac net/garyscorner/lahordownloader/LaHORDownloader.java






echo "Creating JAR with source, license, and readme included
jar -cf LaHORDownloader.jar net.garyscorner.lahordownloader.LaHORDownloader net/garyscorner/lahordownloader/* LICENSE.md README.md


#rm net/garyscorner/lahordownloader/*.class

