import logging
import sys
#CammandLine arguements:
#1.Word for which similar words to be identified
for arg in sys.argv:
    print arg
from gensim.models import word2vec;
from gensim.models import Word2Vec
#loading Existing model
#model=word2vec.Word2Vec.load('/home/User/Word2VecModelComputerScienceModel');
model=word2vec.Word2Vec.load(sys.argv[1]);
#model = Word2Vec.load_word2vec_format(sys.argv[1], binary=True)
print model.most_similar(positive=[sys.argv[2]]);
'''f=open("src/output_3.txt","r")
a=[]
for i in f:
	i=i.split()
	a=a+i
for i in a:
	print "-"*100 , i
	try:
		print model.most_similar(positive=i);
	except:
		print "No ,",i	
'''
