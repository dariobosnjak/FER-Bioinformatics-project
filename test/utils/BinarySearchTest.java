package utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BinarySearchTest {

    BinarySearch<Integer> bs;
    Integer arr[];

    @BeforeEach
    void setUp() {
        bs = new BinarySearch<>(Comparator.<Integer>naturalOrder());
        arr = new Integer[]{-5, 0, 1, 3, 4, 10, 18};
    }

    @Test
    void binarySearch() {
        int i = 0;
        for (Integer x : Arrays.asList(arr)) {
            int idx = bs.binarySearch(arr, x);
            System.out.println(idx);
            assertEquals(i, idx);
            i++;
        }
    }
}