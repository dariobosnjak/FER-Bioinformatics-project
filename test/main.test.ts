import { expect } from "chai";
import { lcskPlusPlus } from "../src/LCSKPlusPlus";

describe("LCSK", () => {
  let x = "ABCGGAB";
  let y = "ABCBA";
  let k = 2;

  it("test1", () => {
    let out = lcskPlusPlus(x, y, k);
    expect(out).eql(42);
  });
});
