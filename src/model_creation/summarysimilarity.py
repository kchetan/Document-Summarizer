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
#print model.most_similar(positive=[sys.argv[2]]);
f=open(sys.argv[2],"r")
a=[]
words=0
val=0.0
for i in f:
	i=i.split()
	for j in i:
		words+=1
		try:
			b=model.most_similar(positive=[j]);
			count=0
			avg=0.0
			for k in b:
				count+=1
				avg+=k[1]
			avg=avg/count
			val=val+avg
		except:
			None

val=val/words
print val
