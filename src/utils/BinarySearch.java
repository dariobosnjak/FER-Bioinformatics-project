package utils;

import java.util.Comparator;

public class BinarySearch<T> {

    private Comparator<T> comparator;

    public BinarySearch(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    /**
     * Performs binary search.
     *
     * @param arr array to search
     * @param x   element
     * @return if the element exists, index of that element in the array, or -1 if it does not exist.
     */
    public int binarySearch(T arr[], T x) {
        int l = 0;
        int r = arr.length - 1;

        return binarySearch(arr, l, r, x);
    }

    /**
     * Performs binary search.
     *
     * @param arr array to search
     * @param l   lower bound index
     * @param r   upper bound index
     * @param x   element
     * @return if the element exists, index of that element in the array, or -1 if it does not exist.
     */
    private int binarySearch(T arr[], int l, int r, T x) {
        if (r >= l) {
            int mid = l + (r - l) / 2;

            // hit
            if (comparator.compare(arr[mid], x) == 0)
                return mid;

            // if element is smaller than element in the middle -> search left subarray
            if (comparator.compare(arr[mid], x) > 0)
                return binarySearch(arr, l, mid - 1, x);

            // if element is larger than element in the middle -> search right subarray
            return binarySearch(arr, mid + 1, r, x);
        }

        // element does not exist
        return -1;
    }
}