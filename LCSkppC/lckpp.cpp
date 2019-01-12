/*
Implementation of LCSk++ algorithm
Author: Dorian Ljubenko
FER, Bioinformatics project
*/

#include "lckpp.hpp"
#include <unordered_map>
#include <vector>
#include <iostream>
#include <algorithm>
#include <cassert>

//Class LCKPP implements LCSk++ algorithm

int LCKPP::run(int k, std::string s1, std::string s2) {
	int solution = 0;

	//Initialising Fenwick tree
	initFenwick(s2.size());

	//Hash map of Dp values 
	std::unordered_map<std::pair<int, int>, int, pair_hash> dp; 
	
	//Initialising MaxColDp and filling with zeros
	std::vector<int> maxColDp;
	for (int i = 0; i <= s2.size(); ++i) {
		maxColDp.push_back(0);
	}

	/*
	Creating matches 
	Strings of k lenght are extracted from s1 , positions stored in kStrPositions
	Than all positions are searched and maching strings are found in s2 thus creatind Match pairs
	*/
	std::cout << "Creating matches..." << std::endl;
	std::unordered_map<std::string, std::vector<int>> kStrPositions;
	std::vector<std::pair<int, int>> kMatchesStart, kMatchesEnd;

	for (int i = 0; i <= s1.size() - k; ++i) { 
		std::string kStr = s1.substr(i, k); 
		kStrPositions[kStr].push_back(i); 
	}

	for (int i = 0; i <= s2.size() - k; ++i) { 
		std::string kStr = s2.substr(i, k);
		//Searching all positions for matching pairs
		for (int position : kStrPositions[kStr]) {
			kMatchesStart.push_back({ position, i });     
			kMatchesEnd.push_back({ position + k, i + k });
		}
	}
	std::cout << "Matches created." << std::endl;


	//Sorting Matches, separately starts and ends
	std::cout << "Sorting matches..." << std::endl;
	std::sort(kMatchesStart.begin(), kMatchesStart.end());
	std::sort(kMatchesEnd.begin(), kMatchesEnd.end());
	std::cout << "Sorting done." << std::endl;

	

	//Searching events and applying dynamic programing algorithm
	int p1 = 0, p2 = 0;

	std::cout << "Event search started..." << std::endl;
	while (p1 < kMatchesStart.size() && p2 < kMatchesEnd.size()) {
		bool isStartEvent;
		std::pair<int, int> event;

		if (p2 == kMatchesEnd.size()) {  
			//p2 reached the end of the second(end) array
			isStartEvent = true;
			event = kMatchesStart[p1];
			p1++;
		}
		else if (p1 == kMatchesStart.size()) { 
			//p1 reached the end of the first(start) array
			isStartEvent = false;
			event = kMatchesEnd[p2];
			p2++;
		}
		else if (kMatchesStart[p1] < kMatchesEnd[p2]) {
			//p1 is before p2
			isStartEvent = true;
			event = kMatchesStart[p1];
			p1++;
		}
		else {
			//p2 is before p1
			isStartEvent = false;
			event = kMatchesEnd[p2];
			p2++;
		}

		//If event is start of Match
		if (isStartEvent) {
			//P=(ip,jp)
			int ip = event.first;
			int jp = event.second;
			int maks = queryMaxFenwick(jp);

			// dp(P) = k + max x=[0,jP]MaxColDp(x)
			dp[{ip, jp}] = maks + k;
			solution = std::max(solution, dp[{ip, jp}]);
		}
		//If event is end of Match
		else {
			//P=(ip,jp)
			int ip = event.first - k;
			int jp = event.second - k;
			
			//G=(ig,jg)
			int ig = ip - 1;
			int jg = jp - 1;

			//it= iterator to the first event(start) not less than G
			auto it = std::lower_bound(kMatchesStart.begin(), kMatchesStart.end(), std::make_pair(ig, jg));
		
			//If it points to a Match than P continues G
			if (it != kMatchesStart.end() && (*it).first == ig && (*it).second == jg) { 
				//dp(P)=max{dp(P),dp(G) + 1}
				dp[{ip, jp}] = std::max(dp[{ip, jp}], dp[{ig, jg}] + 1);
				solution = std::max(solution, dp[{ip, jp}]);
			}

			//MaxColDp(jP + k)= max{MaxColDp(jP + k),dp(P)}
			updateFenwick(jp + k, std::max(queryExactElement(jp + k), dp[{ip, jp}]), s2.size());
		}
	}
	std::cout << "Processing done." << std::endl;
	return solution;
}