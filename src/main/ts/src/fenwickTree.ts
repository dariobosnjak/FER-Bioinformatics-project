export default class FenwickTree {
  private tree: Array<number>;
  private size: number;

  public constructor(size: number) {
    this.size = size + 1;
    this.tree = new Array(size + 1).fill(0);
  }

  public update(position: number, value: number): FenwickTree {
    let i = position + 1;
    while (i <= this.size) {
      if (value >= this.tree[i]) {
        this.tree[i] = value;
      }
      i += i & -i;
    }

    return this;
  }

  public query(position: number): number {
    let max = 0;
    let i = position + 1;
    while (i > 0) {
      max = Math.max(max, this.tree[i]);
      i -= i & -i;
    }

    return max;
  }

  public get(idx: number): number {
    return this.tree[idx];
  }
}
