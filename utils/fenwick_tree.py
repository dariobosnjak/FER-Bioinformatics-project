class Fenwick_tree(object):
    #Fenwick tree for range max query implementation

    def __init__(self,size):
        # we intialize the size to +1 since the 0 node is not
        # a part of the Binary Index Tree structure (BIT)
        #size+=1
        self.f_tree=[(0,(-1,-1,'start'))]*(size+1)
        self.length=size+1

    def LSB(self,i):
      return (i&-i)

    def get(self,i):
        #the get method calculates ths maximum in the [0,i] range
        #this is done in O(log n) time complexity
        result=0
        i+=1
        p_r=(-1,-1,'start')
        while(i>0):
            value,p=self.f_tree[i]
            #if value>result: #if value>result take this index
                #result=value
                #p_r=p
            result+=value-result
            p_r=p
            i-=(self.LSB(i))
            #we calculate the next index by removing the least significant one
        return result,p_r

    def update(self,i,value_update,p):
        # the update method updates the BIT to allow for max range query calculation
        #this is also done in O(log n) time complexity
        i+=1
        while(i<self.length):
            value,_=self.f_tree[i]
            if value<=value_update: #if value to update is greater then update with the new value and parent
                self.f_tree[i]=(value_update,p)
            i+=(self.LSB(i))
            #we calculate the next index by adding the least significant one

#test:
