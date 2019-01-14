from lcskpp import LCSkpp
from lcskpp import read_sequence
import sys
import time 
import os 
import psutil


#reading sequences from file and printing out the similarity
#print out the time of code execution and memory usage
def main():
    k=int(sys.argv[1])
    file=sys.argv[2]
    sequence1, sequence2 = read_sequence(file)
    start_time=time.time()
    similarity=LCSkpp(sequence1,sequence2,k)
    new_file_name=file.replace(".txt","")
    process=psutil.Process(os.getpid())
    file_output=open("%s-k=%s.txt" %(new_file_name, k),'w')
    file_output.write("Similarity: %s\n" %(similarity))
    file_output.write("Duration: %s seconds\n" % (time.time() - start_time))
    file_output.write("Memory usage: %s\n" %(process.memory_info().rss))


if __name__ == '__main__':
    main()
