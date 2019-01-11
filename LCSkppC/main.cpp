#include <string>
#include <iostream>
#include <fstream>

#include "lckpp.hpp"

int main(void) {
	// ucitavanje i ispis 
	// pozivanje lck++

	//std::string s1 = "CCCGACCAGCTTCCCGCCATGCTCTGGCGATTAAGGGGGTTAAATGCCACGCTAAGCGACCAGTATGGGG";
	//std::string s2 = "AAGCTGGTCGCGTAGTGTGGCATTTAACCCCCTTAATCGCCAGGCCATGGAGAAGCTGGTCGCGTAGCGT";

	//std::cout << s1 + s2 << std::endl;
	
	//std::string s3 = s1 + s2;
	//std::cout << s3.size() << std::endl;

	bool firstString = true;
	bool firstLine = true;
	std::string s1 = "", s2 = "";
	std::ifstream input("file1.txt");
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

	std::cout << s1 << " " << s2 << std::endl;

	LCKPP lckpp;
	std::cout << "RJESENJE: " << lckpp.run(10,s1, s2) << std::endl;

	return 0;
}