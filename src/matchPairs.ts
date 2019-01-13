import { MatchPair } from "./interfaces";
import { Pair } from "./interfaces";

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
    map.get(sub).push({ i, j: 0 });
  }

  for (let j = 0; j < seqB.length - (k - 1); ++j) {
    const sub = seqB.substr(j, k);
    if (map.has(sub)) {
      map.get(sub).forEach(pair => {
        if (seqA.substring(pair.i, pair.i + k) === sub) {
          const start: Pair = { i: pair.i, j };
          const end: Pair = { i: start.i + k, j: start.j + k };
          matchPairs.push({
            start,
            end
          });
        }
      });
    }
  }
  return matchPairs;
}
