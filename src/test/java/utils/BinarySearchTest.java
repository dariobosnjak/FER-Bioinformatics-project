package utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BinarySearchTest {

    private BinarySearch<Integer> bs;
    private ArrayList<Integer> arr;

    @BeforeEach
    void setUp() {
        bs = new BinarySearch<>(Comparator.<Integer>naturalOrder());
        arr = new ArrayList<>();
        arr.add(-5);
        arr.add(0);
        arr.add(1);
        arr.add(3);
        arr.add(4);
        arr.add(10);
        arr.add(18);
    }

    @Test
    void binarySearch() {
        int i = 0;
        for (Integer x : arr) {
            int idx = bs.binarySearch(arr, x);
            System.out.println(idx);
            assertEquals(i, idx);
            i++;
        }
    }
}