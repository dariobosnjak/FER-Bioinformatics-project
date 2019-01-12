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

int LCKPP::run(int k, std::string s1, std::string s2) {
	int solution = 0;

	initFenwick(s2.size());

	std::unordered_map<std::pair<int, int>, int, pair_hash> dp;  //da li ovo mora da bude hash?
	
	std::vector<int> maxColDp;
	for (int i = 0; i <= s2.size(); ++i) {
		maxColDp.push_back(0);
	}
	std::cout << "Creating matches..." << std::endl;
	std::unordered_map<std::string, std::vector<int>> kStrPositions;
	for (int i = 0; i <= s1.size() - k; ++i) { //mozda je  <=
		std::string kStr = s1.substr(i, k); // od pozicije i uzimamo iducih k slova
		kStrPositions[kStr].push_back(i); // stavljamo u mapu pod kljuèem kStr na kraj 
	}

	std::vector<std::pair<int, int>> kMatchesStart, kMatchesEnd;
	for (int i = 0; i <= s2.size() - k; ++i) { //mozda je  <=
		std::string kStr = s2.substr(i, k);
		for (int position : kStrPositions[kStr]) {
			kMatchesStart.push_back({ position, i });     //nalazimo par u s2 i stavljamo u par
			kMatchesEnd.push_back({ position + k, i + k });
			//std::cout << "Novi k match: (" << position << ", " << i << ")" << std::endl;
		}
	}
	std::cout << "Matches created." << std::endl;

	std::cout << "Sorting matches.." << std::endl;

	// SORTIRANJE
	// {1,2} {0, 10} {5, 3}  ===> {0, 10}, {1, 2}, {5, 3}
	std::sort(kMatchesStart.begin(), kMatchesStart.end());
	//{3,13}, {4,5}, {8,6}
	std::sort(kMatchesEnd.begin(), kMatchesEnd.end());

	std::cout << "Sorting done." << std::endl;

	
	std::cout << "Event search started..." << std::endl;

	int p1 = 0, p2 = 0;
	while (p1 < kMatchesStart.size() && p2 < kMatchesEnd.size()) {
		bool isStartEvent;
		std::pair<int, int> event;

		if (p2 == kMatchesEnd.size()) {  // obisli smo sve iz prvog niza
			isStartEvent = true;
			event = kMatchesStart[p1];
			p1++;
		}
		else if (p1 == kMatchesStart.size()) {  // obisli smo sve iz drugog niza
			isStartEvent = false;
			event = kMatchesEnd[p2];
			p2++;
		}
		else if (kMatchesStart[p1] < kMatchesEnd[p2]) {
			isStartEvent = true;
			event = kMatchesStart[p1];
			p1++;
		}
		else {
			isStartEvent = false;
			event = kMatchesEnd[p2];
			p2++;
		}

		if (isStartEvent) {
			// dp(P)   k + maxx20:::jP MaxColDp(x)
			int ip = event.first;
			int jp = event.second;
			int maks = queryMaxFenwick(jp);
			/*
			int m1 = -1;
			for (int i = 0; i <= jp; ++i) {               // jel lik zabrijo u PDFu? meni radi s <=!
				m1 = std::max(m1, maxColDp[i]);
			}
			
			assert(m1 == maks);*/
			dp[{ip, jp}] = maks + k;
			solution = std::max(solution, dp[{ip, jp}]);
		}
		else {
			/*
			if 9G s.t. P continues G then
				dp(P)   maxfdp(P); dp(G) + 1g
			end if
			MaxColDp(jP + k)   max fMaxColDp(jP + k); dp(P)g
			*/

			int ip = event.first - k;
			int jp = event.second - k;

			int ig = ip - 1;
			int jg = jp - 1;

			std::vector<std::pair<int, int>> v;

			auto it = std::lower_bound(kMatchesStart.begin(), kMatchesStart.end(), std::make_pair(ig, jg));
			if (it != kMatchesStart.end() && (*it).first == ig && (*it).second == jg) {  // postoji G koji nastavlja P!
				dp[{ip, jp}] = std::max(dp[{ip, jp}], dp[{ig, jg}] + 1);
				solution = std::max(solution, dp[{ip, jp}]);
			}


			//updateSumFenwick(jp + k, std::max(maxColDp[jp + k], dp[{ip, jp}]), s2.size());
			updateFenwick(jp + k, std::max(queryExactElement(jp + k), dp[{ip, jp}]), s2.size());
			//maxColDp[jp + k] = std::max(maxColDp[jp + k], dp[{ip, jp}]);
		}
	}
	std::cout << "Processing done." << std::endl;
	return solution;
}