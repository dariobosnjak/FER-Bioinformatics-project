import { lcskPlusPlus } from "./LCSKPlusPlus";
import { timed, readFastaFile, humanizeMemory } from "./util";

const [seqA, seqB] = readFastaFile(
  "../../../data/synthetic/1e6/input1-1e6.txt"
);
const out = timed(() => lcskPlusPlus(seqA, seqB, 10));
const memoryUsage = process.memoryUsage();
console.log(humanizeMemory(memoryUsage.heapUsed));
console.log(out);
