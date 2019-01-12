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
		std::cerr << "Error! Correct function call:LSCkppC   value of k  file name" << std::endl;
		return -1;
	}
	
	bool firstString = true;
	bool firstLine = true;
	int k = atoi(argv[1]);
	int solution;
	
	std::cout << "k=" << k << std::endl;
	
	//Reading sekuences s1 and s2 from file
	std::string s1 = "", s2 = "";
	std::ifstream input(argv[2]);

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

	//Initialising class lckpp and calling run function
	LCKPP lckpp;
	solution = lckpp.run(k, s1, s2);
	std::cout << "max Dp(P)= " << solution << std::endl;

	return 0;
}