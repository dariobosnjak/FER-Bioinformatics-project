package utils;

/**
 * Fenwick tree data structure.
 */
public class FenwickTree {

    private final int[] tree;

    /**
     * Creates new FenwickTree object.
     * @param length number of elements
     */
    public FenwickTree(int length) {
        this.tree = new int[length + 1];
    }

    /**
     * Sets element at the given index to the given value.
     * @param index index of the element to set
     * @param value new value for that element
     */
    public void update(int index, int value) {
        index++;
        while (index <= tree.length - 1) {
            if(value >= tree[index]){
                tree[index] = value;
            }
            // get index of parent -> flip rightmost bit in the index
            index += index & (-index);
        }
    }

    /*
     * Returns maximum value from first indexEnd values (indexEnd is exclusive).
     * @param indexEnd
     * @return maximum value from subaray defined with given index.
     */
    public int getMax(int indexEnd) {
        int res = 0;//Integer.MIN_VALUE;
        while(indexEnd > 0){
            res = Math.max(res, this.tree[indexEnd]);
            indexEnd -= indexEnd & (-indexEnd);
        }


        return res;
    }

    /**
     * Returns element at given index.
     * @param index defines position of an element in the array.
     * @return element at the index.
     */
    public int getElement(int index) {
        index++;
        return tree[index];
    }
}