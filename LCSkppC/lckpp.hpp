#include <string>
#include <vector>
#include <algorithm>

class LCKPP {
public:
	int run(int k, std::string s1, std::string s2);

	// stackoverflow
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

	void initFenwick(int n) {
		n++;
		for (int i = 0; i <= n; ++i) {
			F.push_back(0);
			G.push_back(0);
		}
	}

	int lobit(int x) { return x & -x; }

	int querySumFenwick(int x) {
		x++;
		int sum = 0;
		for (; x > 0; x -= lobit(x)) sum += F[x];
		return sum;
	}

	void updateSumFenwick(int x, int add, int n) {
		x++; n++;
		for (; x <= n; x += lobit(x)) F[x] += add;
	}

	int queryExactElement(int x) {
		return querySumFenwick(x) - querySumFenwick(x - 1);
	}

	int queryMaxFenwick(int x) {
		x++;
		int ret = -1;
		for (; x > 0; x -= lobit(x)) ret = std::max(ret, G[x]);
		return ret;
	}

	void updateMaxFenwick(int x, int value, int n) {
		x++; n++;
		for (; x <= n; x += lobit(x)) F[x] = std::max(G[x], value);
	}

	std::vector<int> F;
	std::vector<int> G;

};