#include <string>
#include <iostream>

#include "lckpp.hpp"

int main(void) {
	// ucitavanje i ispis 
	// pozivanje lck++

	//std::string s1 = "CCCGACCAGCTTCCCGCCATGCTCTGGCGATTAAGGGGGTTAAATGCCACGCTAAGCGACCAGTATGGGG";
	//std::string s2 = "AAGCTGGTCGCGTAGTGTGGCATTTAACCCCCTTAATCGCCAGGCCATGGAGAAGCTGGTCGCGTAGCGT";

	//std::cout << s1 + s2 << std::endl;
	
	//std::string s3 = s1 + s2;
	//std::cout << s3.size() << std::endl;

	LCKPP lckpp;
	std::cout << "RJESENJE: " << lckpp.run(2, "ABCDEFGH", "ABCDEFGH") << std::endl;

	return 0;
}