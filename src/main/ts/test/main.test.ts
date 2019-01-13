import { expect } from "chai";

describe("LCSK", () => {
  const bacteria = [...Array(7).keys()].map(i => `bacteria${i + 1}.txt`);
  const synthetic = ["1e2", "1e4", "1e6"].map(x =>
    [...Array(4).keys()].map(i => `input${i + 1}-${x}.txt`)
  );
  const files = { bacteria, demo: ["sequence.fasta"], synthetic };
  console.log(files);

  it("test1", () => {
    expect(true);
  });
});
