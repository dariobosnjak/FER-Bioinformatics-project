import { expect } from "chai";
import { lcskPlusPlus } from "../src/LCSKPlusPlus";
import { readFastaFile } from "../src/util";
import { matchPairs } from "../src/matchPairs";
import FenwickTree from "../src/fenwickTree";

describe("LCSK", () => {
  it("basic", () => {
    const out = lcskPlusPlus("ABCDE", "ABCDE", 3);
    expect(5).eq(out);
  });
  it("basic2", () => {
    const out = lcskPlusPlus("ABCFG", "ABCDE", 3);
    expect(3).eq(out);
  });

  it("bacteria1,k=15", () => {
    const [seqA, seqB] = readFastaFile("../../../data/bacteria/bacteria1.txt");
    const out = lcskPlusPlus(seqA, seqB, 15);
    expect(1657).eq(out);
  });
});

describe("matchPairs", () => {
  it("matchPairs", () => {
    const seqA = "ABCGGAB";
    const seqB = "ABCBA";
    const k = 2;

    const mPairs = matchPairs(seqA, seqB, k);

    mPairs.forEach(matchPair => {
      const substr1 = seqA.substring(matchPair.start.i, matchPair.end.i);
      const substr2 = seqB.substring(matchPair.start.j, matchPair.end.j);
      expect(substr1).eq(substr2);
    });
  });
});

describe("fenwick", () => {
  let fenwick: FenwickTree;
  fenwick = new FenwickTree(10);
  fenwick.update(3, 7);
  fenwick.update(2, 3);
  fenwick.update(8, 6);

  it("max", () => {
    expect(0).eq(fenwick.query(2));
    expect(7).eq(fenwick.query(9));
  });
});

describe("parser", () => {
  const seqA =
    "ATATCCCGCCGTAGCTATTGACTTCAAACCCCGACACTGGCGTAGTTACTACGCCCATTGGAAGTTTGACCTAGTAACCGACTCATTTGGCTAGAGCTTT";
  const seqB =
    "TAGCTAGTACGTGATGCGCCTGCTCGAGTGCGCAAGGGATCCTTGGTAGACTTACCGAGGCTGCTCAAATATCTAACTTCGAAGCTTTTCTAGAGCGCCA";

  it("synthetic1", () => {
    const [seqA2, seqB2] = readFastaFile(
      "../../../data/synthetic/1e2/input1-1e2.txt"
    );
    expect(seqA).eq(seqA2);
    expect(seqB).eq(seqB2);
  });
});
