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
  if (a === b) {
    return 0;
  }
  const [i1, j1, i2, j2] = [a.pair.i, a.pair.j, b.pair.i, b.pair.j];

  if (i1 < i2) {
    return -1;
  }

  if (i1 === i2 && j1 < j2) {
    return -1;
  }

  if (
    i1 === i2 &&
    j1 === j2 &&
    a.type === EventType.End &&
    b.type === EventType.Start
  ) {
    return -1;
  }

  return 1;
}

export function timed(f: () => any) {
  const from = new Date();
  const out = f();
  const to = new Date();
  const diff = to.getTime() - from.getTime();
  console.log(`${diff} ms`);
  return out;
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