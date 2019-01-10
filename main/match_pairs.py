

#find all substrings in first string
#map_of_substrings is pair of specific substring and start index of that substring
def find_all_k_length_substrings(string_1,k,map_of_substrings):
    for i in range(len(string_1)):
        substring=string_1[i:i+k]
        if len(substring)==k:
            map_of_substrings[i]=substring # i je pocetak niza 

#find the start position of substring in secound substring and return it

def find_substring_start_in_string_2(substring, string_2):
    index = 0
    sub_index = 0
    position = -1
    for ch_i,ch_f in enumerate(string_2) :
        if ch_f != substring[sub_index]:
            position = -1
            sub_index = 0
        if ch_f == substring[sub_index]: 
            if sub_index == 0 :
                position = ch_i

            if (len(substring) - 1) <= sub_index :
                break
            else:
                sub_index += 1
    return (position) #pocetna pozicija drugog niza koji se preklapa 

#find the start position of substring in secound substring
#append the indces_list with start index position of secound string
def find_substring_start_in_string_2_test(substring, string_2,indices_list):
    index = 0
    while index < len(string_2):
        index = string_2.find(substring, index)
        if index == -1:
            break
        indices_list.append(index)
        index += 1 



def create_start_end_indices_map(map_of_substrings,string_1,string_2,substring_start_indices,substring_end_indices,k,number_of_pairs,all_indices):
    for substring in map_of_substrings:
        indices_list=[]
        #check if there is a specific substring in string_2
        position=find_substring_start_in_string_2_test(map_of_substrings[substring],string_2,indices_list)
        for position in indices_list:
            if  position!=-1:
                number_of_pairs+=1
                substring_start_indices[(substring,position,'start')]=map_of_substrings[substring]
            #print (substring_start_indices, number_of_pairs)
            #end: (index+k,index+k)

    for substring in substring_start_indices:
        #print (substring[0])
        #if there is substring that is < len(k) do not append it to list
        if substring[0]+k <= len(string_1) and substring[1]+k<=len(string_2):
            all_indices.append((substring[0],substring[1],"start"))
            substring_end_indices.append((substring[0]+k,substring[1]+k,"end"))
    print ("Start indecies of substring:",all_indices)
    print ("End indecies of substring:",substring_end_indices)
    print ("Number of pairs:",number_of_pairs )

def find_matches(string_1,string_2,k):
    map_of_substrings={}
    substring_start_indices={}
    all_indices=[]
    substring_end_indices=[]
    number_of_pairs=0
    find_all_k_length_substrings(string_1,k,map_of_substrings)
    create_start_end_indices_map(map_of_substrings,string_1,string_2,substring_start_indices,substring_end_indices,k,number_of_pairs,all_indices)
    all_indices.extend(substring_end_indices)
    return all_indices
