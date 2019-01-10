from match_pairs import find_matches
from fenwick_tree import Fenwick_tree


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
	for i in dp:
		print (i,dp[i])

LCSkpp("ABCBCBCADBC","BCBBCBCBCABCB",3)







