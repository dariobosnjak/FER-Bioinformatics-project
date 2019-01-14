import string
from match_pairs import find_matches
from fenwick_tree import Fenwick_tree


#method for reading sequences from files
def read_sequence(file):
    with open(file, "r") as f:
        data = f.read()

    data = data.split('>')
    data = data[1:]

    for i in range(len(data)):
        data[i] = data[i][data[i].find('\n'):]
        #removing all '\n' from sequence
        data[i] = data[i].replace('\n', '')

    return data[0], data[1]


def LCSkpp(string_1,string_2,k):
    n=len(string_2)
    #use of Fenwick tree to set elements and to get max of first i elements in list
    maxColDp=Fenwick_tree(n)
    #all start and end indices stored in list events
    events=find_matches(string_1,string_2,k)
    dp={}
    continue_map={}
    
    for event in events:
        if event[2]=="start":
            rest,parent=maxColDp.get(event[1])
            dp[event]=(rest+k,parent) 
            continue_map[(event[0]+1,event[1]+1,event[2])]=event 
        else:
            p=(event[0]-k,event[1]-k,'start')
            g=continue_map.get(p)
            if g!=None:
                if dp[g][0]+1>dp[p][0]:
                    dp[p]=(dp[g][0]+1,g)
            maxColDp.update(event[1],dp[p][0],p)
    maxi=0
    #return the maximum value that presents the longest common subequence
    for i in dp.values():
        maxi=max(i[0],maxi)
    return maxi


    
