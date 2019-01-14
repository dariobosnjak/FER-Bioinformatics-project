from lcskpp import LCSkpp
from lcskpp import read_sequence
import sys
import time 
import os 
import psutil



def main():
    k=int(sys.argv[1])
    file=sys.argv[2]
    sequence1, sequence2 = read_sequence(file)
    start_time=time.time()
    similarity=LCSkpp(sequence1,sequence2,k)
    print (similarity)
    #time.sleep(5)
    print("--- %s seconds ---" % (time.time() - start_time))
    process=psutil.Process(os.getpid())
    print ("Memory usage:",process.memory_info().rss)

if __name__ == '__main__':
    main()