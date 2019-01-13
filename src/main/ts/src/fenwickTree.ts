export default class FenwickTree {
  private tree: Array<number>;
  private size: number;

  public constructor(size: number) {
    this.size = size;
    this.tree = new Array(size + 1).fill(0);
  }

  public update(position: number, value: number): FenwickTree {
    for (let i = position; i <= this.size; i += i & -i) {
      if (value > this.tree[i]) {
        this.tree[i] = value;
      }
    }

    return this;
  }

  public query(position: number): number {
    let max = 0;
    for (let i = position; i > 0; i -= i & -i) {
      max = Math.max(this.tree[i], max);
    }

    return max;
  }

  public get(idx: number): number {
    return this.tree[idx];
  }
}
