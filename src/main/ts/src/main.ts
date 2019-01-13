import { lcskPlusPlus } from "./LCSKPlusPlus";
import { timed, readFastaFile } from "./util";

// const seqA = <"GTCGAACGGGAATCAGTTAGGCGACCACGTTCCAGCCGTGGAATCAGTCGTCGACAAGTTCATATGGATTTTCCTCCTTTACTTGAGTAAGGCCAATGAT";
// const seqB = "CAGAACCGCCGGACAGGCGTCTTCCTGAATAATCCGTCATTGCGTCGAATAGCATGCGGGTAGTACAAAAACGCAGAGATCGACCTTCGGTAGCATGTAG";

// const out = lcskPlusPlus(seqA, seqB, 2);
const [seqA, seqB] = readFastaFile("../../../data/bacteria/bacteria1.txt");
const out = timed(() => lcskPlusPlus(seqA, seqB, 15));
console.log(out);
