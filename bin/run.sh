#!/bin/sh
#javac -cp ../lib/tika-app-1.7.jar:../lib/log4j-core-2.2.jar:../lib/log4j-api-2.2.jar ../src/TextExtraction.java
#java -cp ../src:../lib/tika-app-1.7.jar:../lib/log4j-core-2.2.jar:../lib/log4j-api-2.2.jar  TextExtraction $1
#python ../src/summarizer_2.py ../src/text.txt > ../src/output_2.txt
#python ../src/summarizer_3.py ../src/text.txt > ../src/output_3.txt
#sudo python ../src/summarizer_4.py ../src/text.txt > ../src/output_4.txt
java -jar DocumentSummarizer.jar $1 $2

