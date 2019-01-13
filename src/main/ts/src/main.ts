import { lcskPlusPlus } from "./LCSKPlusPlus";
import { timed, readFastaFile } from "./util";

// const seqA = <"GTCGAACGGGAATCAGTTAGGCGACCACGTTCCAGCCGTGGAATCAGTCGTCGACAAGTTCATATGGATTTTCCTCCTTTACTTGAGTAAGGCCAATGAT";
// const seqB = "CAGAACCGCCGGACAGGCGTCTTCCTGAATAATCCGTCATTGCGTCGAATAGCATGCGGGTAGTACAAAAACGCAGAGATCGACCTTCGGTAGCATGTAG";

// const out = lcskPlusPlus(seqA, seqB, 2);
const [seqA, seqB] = readFastaFile(
  "../../../data/synthetic/1e2/input1-1e2.txt"
);
const out = timed(() => lcskPlusPlus(seqA, seqB, 2));
console.log(out);
