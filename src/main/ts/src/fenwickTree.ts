export default class FenwickTree {
  private tree: Array<number>;
  private size: number;

  public constructor(size: number) {
    this.size = size;
    this.tree = new Array(size + 1).fill(0);
  }

  public update(position: number, value: number): FenwickTree {
    for (let i = position; i < this.size; i |= i + 1) {
      if (value > this.tree[i]) {
        this.tree[i] += value;
      }
    }

    return this;
  }

  public query(position: number): number {
    let res = 0;
    for (let i = position; i >= 0; i |= (i & (i + 1)) - 1) {
      res += this.tree[i];
    }

    return res;
  }

  public get(idx: number): number {
    return this.tree[idx];
  }
}
