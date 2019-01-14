def find_all_k_length_substrings(string,k,map):
    for i in range(len(string)):
        substring=string[i:i+k]
        if len(substring)==k:
            map[i]=substring 


#svaka od mapa sastoji se od indeks, podniz vrijednosti
def  match_pair(string_1,string_2,k):
	indices_list=[]
	map1={}
	map2={}
	find_all_k_length_substrings(string_1,k,map1)
	find_all_k_length_substrings(string_2,k,map2)

	for i in map1:
		for j in map2:
			if map1[i]==map2[j]:
				indices_list.append((i,j,'start'))
				indices_list.append((i+k,j+k,'end'))
	indices_list.sort()
	return indices_list



