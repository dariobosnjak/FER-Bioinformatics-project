import { lcskPlusPlus } from "./LCSKPlusPlus";
import { timed, readFastaFile, humanizeMemory, writeResults } from "./util";
import * as path from "path";

const args = process.argv;
let filePath;
let filename;
let resultsDir;
if (args.length > 2) {
  filePath = path.resolve(args[2]);
  filename = path.parse(filePath).name;
  resultsDir = path.resolve("results");
}

const k = Number(args.length > 3 ? args[3] : 3);

let seqA, seqB;

if (filePath) {
  console.log({
    filePath,
    k
  });

  [seqA, seqB] = readFastaFile(
    filePath || "../../../data/synthetic/1e6/input1-1e6.txt"
  );
} else {
  [seqA, seqB] = ["ABCDE", "ABCDE"];
}

const { result, duration } = timed(() => lcskPlusPlus(seqA, seqB, k));
const memoryUsage = humanizeMemory(process.memoryUsage().heapUsed);

const out = {
  result,
  memoryUsage,
  duration: `${duration} ms`
};

console.log(out);

if (filename && resultsDir) {
  writeResults(out, path.resolve(resultsDir, `${filename}_${k}.txt`));
}
