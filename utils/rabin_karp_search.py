
def basic_hash(h):
    num=1
    for i in "ACTG":
        h[i]=num
        num+=1

def hash_init(seq1,k,h,hash_map):
    start=0
    for i in range(0,len(seq1)):
        value=0
        subseq=seq1[i:k+i]
        #print (subseq)
        br=0
        if len(subseq)==k:
            for i in subseq:
                value+=h[i]*(3**br)
                #print (value)
                br+=1
        hash_map[start]=value
        start+=1

def hash_prvi_2_init(seq2,k,h):
    subseq=seq2[0:k]
    br=0
    value=0
    for i in subseq:
        value+=h[i]*(3**br)
        #print (value)
        br+=1
    return value


def find_match_pairs(seq2,hash_map,k,hash_2,pair_indices,h):
    for string_ind  in hash_map:
        if hash_2 == hash_map[string_ind]:
            #print("Its match!")
            pair_indices.append((string_ind,0,'start'))
    for i in range(1, len(seq2)):
        subseq=seq2[i:k+i]
        ##print(i ," ", subseq)
        if len(subseq)==k:
            x=hash_2-h[seq2[i-1]]
            x=x/3
            #print (k-1)
            x=x+h[subseq[k-1]]*(3**2)
            hash_2 = x
            for string_ind  in hash_map:
               # #print(x,":",string_ind," ",hash_map[string_ind])
                if x == hash_map[string_ind]:
                    #print("Its match! ",x," ",hash_map[string_ind])
                    pair_indices.append((string_ind,i,'start'))


def match_pairs(seq1,seq2,k):
    h={}
    hash_map={}
    pair_indices=[]
    pair_ends=[]
    basic_hash(h)
    hash_init(seq1,k,h,hash_map)
    hash_2=hash_prvi_2_init(seq2,k,h)
    find_match_pairs(seq2,hash_map,k,hash_2,pair_indices,h)
    for i in pair_indices:
        pair_ends.append((i[0]+k,i[1]+k,'end'))
        ##print (pair_ends)
    pair_indices.extend(pair_ends)
    ##print(pair_indices)
    return pair_indices
