export default class FenwickTree {
  private tree: Array<number>;
  private size: number;

  public constructor(size: number) {
    this.size = size;
    this.tree = new Array(size + 1).fill(0);
  }

  public increase(position: number, value: number): FenwickTree {
    this.assertOutOfRange(position);
    for (let i = position; i <= this.size; i += i & -i) {
      this.tree[i] += value;
    }

    return this;
  }

  public query(position: number): number {
    this.assertOutOfRange(position);

    let sum = 0;
    for (let i = position; i > 0; i -= i & -i) {
      sum += this.tree[i];
    }

    return sum;
  }

  public queryRange(leftIdx: number, rightIdx: number): number {
    if (leftIdx > rightIdx) {
      throw new Error("Left index can not be greater than right.");
    }

    if (leftIdx === 1) {
      return this.query(rightIdx);
    }

    return this.query(rightIdx) - this.query(leftIdx - 1);
  }

  public get(idx: number): number {
    return this.tree[idx];
  }

  private assertOutOfRange(position: number) {
    if (position < 1 || position > this.size) {
      throw new Error("Position out of range.");
    }
  }
}
