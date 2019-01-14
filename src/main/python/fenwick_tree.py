class Fenwick_tree(object):
    def __init__(self,size):
        self.f_tree=[(0,(-1,-1,'start'))]*(size+1)
        self.length=size+1
    #LSB calculating method
    def LSB(self,i):
      return (i&-i)
    #method that returns the value that will be add to k to get ssubequence length
    def get(self,i):
        result=0
        i+=1
        parent=(-1,-1,'start')
        while(i>0):
            value,p=self.f_tree[i]
            if value>result: 
                result=value
                parent=p
            i-=(self.LSB(i))
        return result,parent
    #set the element on index i 
    def update(self,i,value_update,p):
        i+=1
        while(i<self.length):
            val,_=self.f_tree[i]
            if val<=val_update: 
                self.f_tree[i]=(val_update,p)
            i+=(self.LSB(i))
