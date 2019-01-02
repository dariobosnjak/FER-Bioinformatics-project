package lcsk;

import utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class LCSKPlusPlus {

    public static void main(String[] args) {

        // TODO load arguments
        int k = 3;
        String x = "ABCDEFGCBAKFOWCBAFDACBA";
        String y = "ABCBA";

        ArrayList<MatchPair> matchPairs = findAllKMatchPairs(x, y, k);
    }

    /**
     * Finds all k-match pairs between x and y sequence.
     *
     * @param x string
     * @param y string
     * @param k match length
     * @return all k-match pairs between x and y
     */
    static ArrayList<MatchPair> findAllKMatchPairs(String x, String y, int k) {
        ArrayList<MatchPair> matchPairs = new ArrayList<>();

        // contains all k-grams from string sequence
        ArrayList<String> kGramsX = new ArrayList<>();
        ArrayList<String> kGramsY = new ArrayList<>();

        // keys are k-grams from string sequence, values are utils.Pair objects which represent the beginning and the end
        // (indexes)of the k-gram
        HashMap<String, ArrayList<Pair<Integer, Integer>>> xMap = initializeKGramMap(x, k);
        HashMap<String, ArrayList<Pair<Integer, Integer>>> yMap = initializeKGramMap(y, k);

        // intersection (keep only items which appear in both maps as keys (k-grams))
        Set<String> keysIntersection = new TreeSet<>(xMap.keySet());
        keysIntersection.retainAll(yMap.keySet());

        // construct result
        for (String key : keysIntersection) {
            for (Pair<Integer, Integer> xValue : xMap.get(key)) {
                for (Pair<Integer, Integer> yValue : yMap.get(key)) {
                    Pair<Integer, Integer> start = new Pair<>(xValue.getFirstElement(), yValue.getFirstElement());
                    Pair<Integer, Integer> end = new Pair<>(xValue.getSecondElement(), yValue.getSecondElement());
                    matchPairs.add(new MatchPair(start, end));
                }
            }
        }

        return matchPairs;
    }

    /**
     * Creates new map where keys are k-grams from the given string, and values are utils.Pair objects. utils.Pair objects represent
     * beginning and ending indexes of k-gram in the string. Each k-gram can appear multiple times in the string.
     *
     * @param s string
     * @param k defines the length of k-grams
     */
    private static HashMap<String, ArrayList<Pair<Integer, Integer>>> initializeKGramMap(String s, int k) {
        int sLength = s.length();
        HashMap<String, ArrayList<Pair<Integer, Integer>>> kGramMap = new HashMap<String, ArrayList<Pair<Integer, Integer>>>();

        for (int start = 0; start < sLength - (k - 1); start++) {
            int end = start + k;

            Pair<Integer, Integer> p = new Pair<>(start, end);
            String kGram = s.substring(start, end);

            ArrayList<Pair<Integer, Integer>> value;
            value = kGramMap.getOrDefault(kGram, new ArrayList<>());
            value.add(p);

            kGramMap.put(kGram, value);
        }

        return kGramMap;
    }

    /**
     * Represents one match pair.
     */
    public static class MatchPair {
        private int length;
        private Pair<Integer, Integer> start;
        private Pair<Integer, Integer> end;

        /**
         * Constructs new MatchPair object.
         *
         * @param start represents a pair which defines start of this match pair. First element of the pair defines
         *              beginning index of the first string sequence, and the second element defines beginning index of
         *              the second string sequence. Indexes are inclusive.
         * @param end   represents a pair which defines end of this match pair. First element of the pair defines
         *              ending index of the first string sequence, and the second element defines ending index of
         *              the second string sequence. Indexes are exclusive.
         *              Example: X="abcDEFkj", Y="DEFghi". One match pair of length 3 would be start=(3,0), end=(6,3).
         */
        public MatchPair(Pair<Integer, Integer> start, Pair<Integer, Integer> end) {
            this.start = start;
            this.end = end;
            this.length = end.getFirstElement() - start.getFirstElement();
        }

        public int getLength() {
            return length;
        }

        public Pair<Integer, Integer> getStart() {
            return start;
        }

        public Pair<Integer, Integer> getEnd() {
            return end;
        }

        @Override
        public String toString() {
            return "MatchPair{" +
                    "length=" + length +
                    ", start=" + start +
                    ", end=" + end +
                    '}';
        }
    }

}
