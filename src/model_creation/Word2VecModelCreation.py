import logging
import sys
#Commandline Arguements:
#1.File Path Containing Extracted Text Content 
#2.Path to Save binary Word2Vec model
for arg in sys.argv:
    print arg
from gensim.models import word2vec;
#sentences=word2vec.Text8Corpus('/home/User/Word2VectInputFile');
sentences=word2vec.Text8Corpus(sys.argv[1]);
model = word2vec.Word2Vec(sentences);
#model.save('/home/User/Word2VecModelComputerScienceModel');
model.save(sys.argv[2]);
