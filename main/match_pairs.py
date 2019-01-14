
#mapping all possible k lenght subsequences,key is the start index
def find_all_k_length_subsequences(sequence_1,k,map_of_subsequences):
    for i in range(len(sequence_1) - k+1):
        subsequence=sequence_1[i:i+k]
        map_of_subsequences[i]=subsequence


#finding if mapped subsequences exist in sequence_2
def find_subsequence_start_in_sequence_2_test(subsequence, sequence_2,indecies_list):
    index = 0
    while index < len(sequence_2):
        index = sequence_2.find(subsequence, index)
        if index == -1:
            break
        #if there is a match, return the start position of subsequence in sequence_2
        indecies_list.append(index)
        index += 1

#createing the end_indecies list for all start_indices and sort the final list
def create_start_end_indices_map(map_of_subsequences,k,sequence_1,sequence_2):
    indices_list_final = []
    for start_index_1 in map_of_subsequences:
        indecies_list=[]
        find_subsequence_start_in_sequence_2_test(map_of_subsequences[start_index_1],sequence_2,indecies_list)
        for start_index_2 in indecies_list:
            if  start_index_2!=-1:
                indices_list_final.append((start_index_1,start_index_2, "start"))
                indices_list_final.append((start_index_1+k, start_index_2+k, "end"))

    indices_list_final.sort()
    return indices_list_final

def find_matches(sequence_1,sequence_2,k):
	map_of_subsequences={}
	find_all_k_length_subsequences(sequence_1,k,map_of_subsequences)
	indices_list_final = create_start_end_indices_map(map_of_subsequences,k,sequence_1,sequence_2)
	return indices_list_final