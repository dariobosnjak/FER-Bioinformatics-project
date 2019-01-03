package lcsk;

import utils.BinarySearch;
import utils.Pair;

import java.util.*;

public class LCSKPlusPlus {

    public static void main(String[] args) {

        // TODO load arguments
        int k = 3;
        String x = "ABCDE";
        String y = "ABCBA";

        int[][] dp = new int[x.length()][y.length()];
        int[] maxColDp = new int[x.length()];

        ArrayList<MatchPair> matchPairs = findAllKMatchPairs(x, y, k);

        ArrayList<Event> events = extractEvents(matchPairs);
        events.sort(new RowMajorEventComparator());

        for (Event event : events) {
            if (event.getType() == Event.EventType.START) {
                Pair<Integer, Integer> p = event.getPair();
                int max = findMaxInArray(maxColDp, 0, p.getSecondElement() + 1);
                dp[p.getFirstElement()][p.getSecondElement()] = k + max;
            } else {
                Pair<Integer, Integer> eventPair = event.getPair();
                Pair<Integer, Integer> p = new Pair<>(eventPair.getFirstElement() + k, eventPair.getSecondElement() + k);

                // find G such that P continues G -> ip - jp == ig - jg && ip - ig = 1
                Pair<Integer, Integer> g = findG(p, events);
                if (g != null) {
                    // if dp(G) + 1 > dp(P)
                    int dpOfG = dp[g.getFirstElement()][g.getSecondElement()];
                    int dpOfP = dp[p.getFirstElement()][p.getSecondElement()];
                    if(dpOfG + 1> dpOfP){
                        // dp(P) = dp(G) + 1
                        dp[p.getFirstElement()][p.getSecondElement()] = dpOfG + 1;
                    }

                    // if dp(P) > maxColDp(jp + k)
                    if(dp[p.getFirstElement()][p.getFirstElement()] > maxColDp[p.getSecondElement() + k]) {
                        // maxColDp(jp + k) = dp(p)
                        maxColDp[p.getSecondElement() + k] = dp[p.getFirstElement()][p.getFirstElement()];
                    }
                }
            }
        }
        int result = findMaxInMatrix(dp);
        System.out.println(result);
    }

    /**
     * Binary search over events, searches for G such that P continues G.
     *
     * @return G if it does exist, null otherwise.
     */
    private static Pair<Integer, Integer> findG(Pair<Integer, Integer> p, ArrayList<Event> events) {
        int ip = p.getFirstElement();
        int jp = p.getSecondElement();

        int ig = ip - 1;
        int jg = -(ip - jp - ig);
        Event g = new Event(new Pair<Integer, Integer>(ig, jg), Event.EventType.START);

        RowMajorEventComparator rmec = new RowMajorEventComparator();
        BinarySearch<Event> bs = new BinarySearch<>(rmec);

        Event[] eventsArray = events.toArray(new Event[events.size()]);
        int gIndex = bs.binarySearch(eventsArray, g);

        if (gIndex != -1)
            return g.getPair();
        else
            return null;
    }

    /**
     * Finds a maximum value in the matrix.
     */
    private static int findMaxInMatrix(int[][] matrix) {
        int max = Integer.MIN_VALUE;

        int numberOfColumns = matrix[0].length;
        int numberOfRows = matrix.length;

        for(int i = 0; i < numberOfColumns; i++) {
            for(int j = 0; j < numberOfColumns; j++) {
                if(matrix[i][j] > max) {
                    max = matrix[i][j];
                }
            }
        }
        return max;
    }

    /**
     * Finds a maximum value in the array defined as max(array[startingIndex], array[startingIndex+1], ..., array[endingIndex-1])
     *
     * @param arr           array to search
     * @param startingIndex inclusive
     * @param endingIndex   exclusive
     * @return maximum value from array slice defined with starting and ending indexes.
     */
    private static int findMaxInArray(int[] arr, int startingIndex, int endingIndex) {
        int max = Integer.MIN_VALUE;

        for (int i = startingIndex; i <= endingIndex; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

    /**
     * Extracts events (start and end pairs).
     */
    private static ArrayList<Event> extractEvents(ArrayList<MatchPair> matchPairs) {
        ArrayList<Event> events = new ArrayList<>();

        for (MatchPair matchPair : matchPairs) {
            Event startEvent = new Event(matchPair.start, Event.EventType.START);
            Event endEvent = new Event(matchPair.end, Event.EventType.END);
            events.add(startEvent);
            events.add(endEvent);
        }

        return events;
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
    static class MatchPair {
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
