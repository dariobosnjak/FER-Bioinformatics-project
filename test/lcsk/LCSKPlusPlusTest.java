package lcsk;
import utils.Pair;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class LCSKPlusPlusTest {
    private int k;
    private String x;
    private String y;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        k = 2;
        x = "ABCGGAB";
        y = "ABCBA";

        System.out.println("K = " + k);
        printString(x, "X");
        printString(y, "Y");
    }

    private void printString(String s, String stringName) {
        System.out.print(stringName + " = ");
        for (int i = 0; i < s.length(); i++) {
            System.out.print(s.charAt(i) + " ");
        }
        System.out.println();
    }


    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void main() {
    }

    @org.junit.jupiter.api.Test
    void findAllKMatchPairs() {

        ArrayList<LCSKPlusPlus.MatchPair> matchPairs = LCSKPlusPlus.findAllKMatchPairs(x, y, k);

        for (LCSKPlusPlus.MatchPair m : matchPairs) {
            Pair<Integer, Integer> mStart = m.getStart();
            Pair<Integer, Integer> mEnd = m.getEnd();

            String xSubstring = x.substring(mStart.getFirstElement(), mEnd.getFirstElement());
            String ySubstring = y.substring(mStart.getSecondElement(), mEnd.getSecondElement());

            System.out.println(m + " substringX=" + xSubstring + " substringY=" + ySubstring);

            assertEquals(xSubstring, ySubstring);
        }
    }
}