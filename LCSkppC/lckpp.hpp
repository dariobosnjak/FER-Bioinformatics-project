/*
Implementation of LCSk++ algorithm
Author: Dorian Ljubenko
FER, Bioinformatics project
*/

#include <string>
#include <vector>
#include <algorithm>

class LCKPP {
public:
	int run(int k, std::string s1, std::string s2);

	// Structure taken from Stack Overflow
	struct pair_hash {
		template <class T1, class T2>
		std::size_t operator () (const std::pair<T1, T2> &p) const {
			auto h1 = std::hash<T1>{}(p.first);
			auto h2 = std::hash<T2>{}(p.second);

			// Mainly for demonstration purposes, i.e. works but is overly simple
			// In the real world, use sth. like boost.hash_combine
			return h1 ^ h2;
		}
	};

	
	//*************************************
	//Implementation of Fenwick tree
	//*************************************
	
	// F and G elements
	std::vector<int> F;
	std::vector<int> G;

	//Function returns the last high bit of x (other bits are zeros)
	int lobit(int x) {
	return x & -x;
	}

	//Function to initialise tree
	void initFenwick(int n) {
		n++;
		for (int i = 0; i <= n; i++) {
			F.push_back(0);
			G.push_back(0);
		}
	}
	
	//Function to get exact element of tree
	int queryExactElement(int x) {
		return F[x + 1];
	}

	//Function to get maximum of given index
	int queryMaxFenwick(int x) {
		x++;
		int ret = -1;
		while (x > 0) {
			ret = std::max(ret, G[x]);
			x -= lobit(x);
		}
		return ret;
	}

	//Function to update tree with new value
	void updateFenwick(int x, int value, int n) {
		x++; n++;
		F[x] = value;
		while (x <= n) {
			G[x] = std::max(G[x], value);
			x += lobit(x);
		}
	}
	//**************************************

};