import { lcskPlusPlus } from "./LCSKPlusPlus";
import { timed, readFastaFile, humanizeMemory, writeResults } from "./util";
import * as path from "path";

const args = process.argv;
const filePath = path.resolve(args[2]);
const filename = path.parse(filePath).name;
const resultsDir = path.resolve("results");

const k = Number(args.length > 3 ? args[3] : 10);

console.log({
  filePath,
  k
});

const [seqA, seqB] = readFastaFile(
  args.length > 2 ? filePath : "../../../data/synthetic/1e6/input1-1e6.txt"
);
const { result, duration } = timed(() => lcskPlusPlus(seqA, seqB, k));
const memoryUsage = humanizeMemory(process.memoryUsage().heapUsed);
console.log(memoryUsage);
console.log(result);

writeResults(
  {
    result,
    memoryUsage,
    duration: `${duration} ms`
  },
  path.resolve(resultsDir, `${filename}_${k}.txt`)
);
