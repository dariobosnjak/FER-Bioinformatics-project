import string
import random
from match_pairs import find_matches
from fenwick_tree import Fenwick_tree

def sequence_1_generator(size, chars):
    return ''.join(random.choice(chars) for _ in range(size))
def read_sequence_2():
	sequence=open("sequence.txt",'r')
	return sequence

def LCSkpp(string_1,string_2,k):
	n=len(string_2)
	maxColDp=Fenwick_tree(n)
	events=find_matches(string_1,string_2,k)
	dp={} #parovi prethodnjaka

	for event in events:
		#print (event)
		if event[2]=="start":
			rest,parent=maxColDp.get(event[1])
			dp[event]=(rest+k,parent) 
		else:
			p=(event[0]-k,event[1]-k,'start')
			g=(event[0]-k-1,event[1]-k-1,'start')
			#print(g)
			#print (dp)
			if g in dp:
				if dp[g][0]+1>dp[p][0]:
					dp[p]=(dp[g][0]+1,g)
			maxColDp.update(event[1],dp[p][0],p)

	result=open("lcskpp_resault.txt", 'w')
	for i in dp:
		result.write(dp[i])





sequence_1=sequence_1_generator(10,"ACTG")
sequence_2=read_sequence_2()
LCSkpp(sequence_1,sequence_2,3)







