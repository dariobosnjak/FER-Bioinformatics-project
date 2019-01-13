CXX=g++
CXXFLAGS=-Wall
SOURCES=main.cpp lckpp.cpp

main: $(SOURCES)
	$(CXX) -O3 $(SOURCES) -o main 

clean:
	@rm -f *.o main
