#!/usr/bin/python
f=open("output1.txt","r")
f1=open("output.txt",'w')
m={}
for i in f:
	try:
		if len(i)>4:
			m[i]=1
	except:
		pass
for i in m:
	try:
		f1.write(i.encode('utf-8'))
	except:
		pass
f1.close()
