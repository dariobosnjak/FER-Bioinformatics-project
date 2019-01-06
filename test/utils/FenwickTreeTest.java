package utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FenwickTreeTest {
    private FenwickTree ft;

    @BeforeEach
    void setUp() {
        ft = new FenwickTree(10);
        ft.update(3, 7);
        ft.update(2, 3);
        ft.update(8, 6);
    }

    @Test
    void getMax() {
        assertEquals(ft.getMax(2), 0);
        assertEquals(ft.getMax(9), 7);
    }

    @Test
    void getElement() {
        assertEquals(ft.getElement(3), 7);
        assertEquals(ft.getElement(2), 3);
        assertEquals(ft.getElement(8), 6);
    }
}