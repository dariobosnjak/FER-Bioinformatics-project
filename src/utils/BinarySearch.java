package utils;

import java.util.ArrayList;
import java.util.Comparator;

public class BinarySearch<T> {

    private final Comparator<T> comparator;

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
    public int binarySearch(ArrayList<T> arr, T x) {
        int l = 0;
        int r = arr.size() - 1;

        while (l <= r) {
            int mid = l + (r - l) / 2;

            // hit
            if (comparator.compare(arr.get(mid), x) == 0)
                return mid;

            // if element is smaller than element in the middle -> search left subarray
            if (comparator.compare(arr.get(mid), x) > 0) {
                r = mid - 1;
            }
            // if element is larger than element in the middle -> search right subarray
            else {
                l = mid + 1;
            }
        }
        // element does not exist
        return -1;
    }


}