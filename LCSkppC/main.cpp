/*
Implementation of LCSk++ algorithm
Author: Dorian Ljubenko
FER, Bioinformatics project
*/

#include <string>
#include <iostream>
#include <fstream>
#include "lckpp.hpp"

int main(int argc, char *argv[]) {
	
	if (argc!=3 ) {
		std::cerr << "Error! Number of arguments has to be 3!" << std::endl;
		return -1;
	}
	
	bool firstString = true;
	bool firstLine = true;
	std::string s1 = "", s2 = "";
	std::ifstream input(argv[2]);
	int k = atoi(argv[1]);
	std::cout << "k=" << k << std::endl;


	//Reading sekuences s1 and s2 from file
	for (std::string line; getline(input, line); ) {
		if (firstLine) {
			firstLine = false;
			continue;
		}
		if (line[0] == '>') {
			firstString = false;
			continue;
		}
		else if (firstString) {
			s1 += line;
		}
		else {
			s2 += line;
		}
	}

	LCKPP lckpp;
	std::cout << "Final solution max Dp(P)=: " << lckpp.run(k,s1, s2) << std::endl;

	return 0;
}