import { matchPairs } from "./matchPairs";

export function lcskPlusPlus(seqA: string, seqB: string, k: number): number {
  const mPairs = matchPairs(seqA, seqB, k);
  console.log(mPairs);
  return 42;
}
