import { Event, EventType } from "./interfaces";
import fs = require("fs");

export function getCharacters(...seqs: Array<string>): Map<string, number> {
  let characters: Map<string, number> = new Map();
  let i = 0;
  seqs.forEach(seq =>
    seq.split("").forEach(char => {
      if (!characters.get(char)) {
        characters.set(char, i++);
      }
    })
  );
  return characters;
}

export function eventComparator(a: Event, b: Event) {
  const [i1, j1, i2, j2] = [a.pair.i, a.pair.j, b.pair.i, b.pair.j];

  if (i1 === i2 && j1 === j2 && a.type === b.type) {
    return 0;
  }

  if (
    i1 < i2 ||
    (i1 === i2 && j1 < j2) ||
    (i1 === i2 &&
      j1 === j2 &&
      a.type === EventType.End &&
      b.type === EventType.Start)
  ) {
    return -1;
  }

  return 1;
}

export function timed<O>(f: () => O) {
  const from = process.hrtime();
  const out = f();
  const to = process.hrtime();
  let sec = to[0] - from[0];
  let ms = (to[1] - from[1]) / 1000000;
  const diff = sec * 1000 + ms;
  return { result: out, duration: diff };
}

export function readFastaFile(path: string): [string, string] {
  let file: string = fs.readFileSync(path).toString();
  let seqs = file
    .split(">")
    .splice(1)
    .map(s =>
      s
        .split("\n")
        .splice(1)
        .join("")
    );
  return [seqs[0], seqs[1]];
}

export function writeResults(out: any, path: string) {
  fs.writeFileSync(path, JSON.stringify(out, null, 4));
}

export function humanizeMemory(bytes: number): string {
  let res = bytes;
  if (res < 1024) {
    return `${round(res, 2)} B`;
  }
  res /= 1024;
  // if (res < 1024) {
  return `${round(res, 2)} kB`;
  // }
  // res /= 1024;
  // if (res < 1024) {
  //   return `${round(res, 2)} MB`;
  // }
  // res /= 1024;
  // return `${round(res, 2)} GB`;
}

export function round(n: number, digits: number): number {
  const x = 10 ** digits;
  return Math.round(n * x) / x;
}
