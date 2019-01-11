import { Pair, MatchPair } from "./interfaces/interfaces";

export function matchPairs(
  seqA: string,
  seqB: string,
  k: number
): Array<MatchPair> {
  const matchPairs = new Array<MatchPair>();

  const map = new Map<string, Array<Pair>>();
  for (let i = 0; i < seqA.length - (k - 1); ++i) {
    const sub = seqA.substr(i, k);
    if (!map.has(sub)) {
      map.set(sub, []);
    }
    map.get(sub).push({ i, j: null });
  }

  for (let j = 0; j < seqB.length - (k - 1); ++j) {
    const sub = seqB.substr(j, k);
    if (map.has(sub)) {
      map.get(sub).forEach(pair => {
        matchPairs.push({
          start: { i: pair.i, j },
          end: { i: pair.i + k, j: j + k }
        });
      });
    }
  }

  return matchPairs;
}
