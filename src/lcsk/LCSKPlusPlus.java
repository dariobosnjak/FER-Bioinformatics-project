package lcsk;

import utils.BinarySearch;
import utils.Pair;

import java.io.*;
import java.util.*;

public class LCSKPlusPlus {

    public static void main(String[] args) throws IOException {

        // TODO load arguments
        int k = 5;

        File file = new File("input");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String x = br.readLine();//"ABCDEFGH"; // "ATTATG"
        String y = br.readLine();//"ABCDEFGH"; // "CTATAGAGTA"

        // sparse dynamic programming table, key is location in the table
        HashMap<Pair<Integer, Integer>, Integer> dp = new HashMap<>();
        int[] maxColDp = new int[y.length()];

        // get all k-match pairs between X and Y
        ArrayList<MatchPair> matchPairs = findAllKMatchPairs(x, y, k);

        // all starts and ends of match pairs, sorted in row-major order
        ArrayList<Event> events = extractEvents(matchPairs);
        events.sort(new RowMajorEventComparator());

        for (Event event : events) {
            if (event.getType() == Event.EventType.START) {
                Pair<Integer, Integer> p = event.getPair();
                int max = findMaxInArray(maxColDp, 0, p.getSecondElement());
                //System.out.println(max);
                dp.put(p, k + max);
            } else {
                // event.getPair() is an END (indexes should be subtracted by one because they are exclusive)
                // find starting location of this event
                Pair<Integer, Integer> pStartLocation = new Pair<>(event.getPair().getFirstElement() - k, event.getPair().getSecondElement() - k);
                // find GStart such that PStart continues GStart -> ip - jp == ig - jg && ip - ig = 1
                Pair<Integer, Integer> gStartLocation = findG(pStartLocation, events);

                // if such GStart exists
                if (gStartLocation != null) {
                    // if dp(GStart) + 1 > dp(PStart)
                    if (dp.get(gStartLocation) + 1 > dp.get(pStartLocation)) {
                        // dp(PStart) = dp(GStart) + 1
                        dp.put(pStartLocation, dp.get(gStartLocation) + 1);
                    }
                }
                // if dp(PStart) > maxColDp(PEnd.j)
                if (dp.get(pStartLocation) > maxColDp[event.getPair().getSecondElement() - 1]) {
                    // maxColDp(PEnd.j) = dp(PStart)
                    maxColDp[event.getPair().getSecondElement() - 1] = dp.get(pStartLocation);
                }
            }
        }

        int result = 0;
        if (!dp.values().isEmpty()) {
            result = Collections.max(dp.values());
        }
        System.out.println(result);
    }

    private static void printMatrix(int[][] m) {
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                System.out.print(m[i][j] + " ");
            }
            System.out.println();
        }
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

        //Event[] eventsArray = events.toArray(new Event[events.size()]);
        int gIndex = bs.binarySearch(events, g);

        if (gIndex != -1)
            return g.getPair();
        else
            return null;
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
        int max = 0;

        for (int i = startingIndex; i < endingIndex; i++) {
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

    // TODO for bigger k values -> overflow
    private ArrayList<MatchPair> findAllKMatchPairsRabinKarpSearch(String x, String y, int k){
        ArrayList<MatchPair> matchPairs2 = new ArrayList<>();

        // large prime number
        long prime = 7800097;
        // size of the alphabet
        String xDistinct = x.chars().distinct().toString();
        String yDistinct = y.chars().distinct().toString();
        long alphabetSize = xDistinct.concat(yDistinct).chars().distinct().count();

        // key is hashcode, value is starting index of substring in X and Y (Pair object)
        HashMap<Long, ArrayList<Pair<Integer, Integer>>> kGramMapX = new HashMap<>();

        // calculate hash for the first kGram in X (different method is used for calculation of others)
        long hash = 0;
        for (int i = 0; i < k; i++) {
            hash += (x.charAt(i) * Math.pow(alphabetSize, k - i - 1)) % prime;
        }
        // if key does not exist, create new map entry
        kGramMapX.computeIfAbsent(hash, k1 -> new ArrayList<>());
        // this kGram with hashcode "hash" starts at index 0 for string X (starting index for Y is unknown for now)
        kGramMapX.get(hash).add(new Pair<Integer, Integer>(0, null));

        // calculate hash for other kGrams
        for (int startX = 1; startX < x.length() - (k - 1); startX++) {
            hash = ((hash - x.charAt(startX - 1) * (long) Math.pow(alphabetSize, k - 1)) * alphabetSize + x.charAt(startX + k - 1)) % prime;
            hash = hash < 0 ? hash + prime : hash;
            //hash = (hash + prime - ((long) Math.pow(alphabetSize, k - 1) * x.charAt(startX - 1) % prime)) % prime;
            //hash = (hash * alphabetSize + x.charAt(startX + k - 1)) % prime;

            if("JGABCDEFGJFDLKHFDJSH".equals(x.substring(startX, startX+k))){
                System.out.println(hash);
            }

            // if key does not exist, create new map entry
            kGramMapX.computeIfAbsent(hash, k1 -> new ArrayList<>());
            kGramMapX.get(hash).add(new Pair<>(startX, null));
        }

        // calculate hash for the first kGram in Y (different method is used for calculation of others)
        hash = 0;
        for (int i = 0; i < k; i++) {
            hash += (y.charAt(i) * Math.pow(alphabetSize, k - i - 1)) % prime;
        }
        // if key exists -> intersection -> update result
        ArrayList<Pair<Integer, Integer>> startingLocationsInX = kGramMapX.get(hash);
        if (startingLocationsInX != null) {
            for (Pair<Integer, Integer> p : startingLocationsInX) {
                // if hashcode is the same, substrings can be equal - check if they are
                if (x.substring(p.getFirstElement(), p.getFirstElement() + k).equals(y.substring(0, k))) {
                    Pair<Integer, Integer> pCopy = new Pair<>(p.getFirstElement(), 0);
                    Pair<Integer, Integer> end = new Pair<>(pCopy.getFirstElement() + k, pCopy.getSecondElement() + k);
                    matchPairs2.add(new MatchPair(pCopy, end));
                }
            }
        }

        // calculate hash for other kGrams
        for (int startY = 1; startY < y.length() - (k - 1); startY++) {
            //hash = ((hash - y.charAt(startY - 1) * (long) Math.pow(alphabetSize, k - 1)) * alphabetSize + y.charAt(startY + k - 1)) % prime;
            //hash = hash < 0 ? hash + prime : hash;
            hash = (hash + prime - (long) Math.pow(alphabetSize, k - 1) * y.charAt(startY - 1) % prime) % prime;
            hash = (hash * alphabetSize + y.charAt(startY + k - 1)) % prime;

            if("JGABCDEFGJFDLKHFDJSH".equals(y.substring(startY, startY+k))){
                System.out.println(hash);
                System.out.println();
            }

            // if key exists -> intersection -> update result
            startingLocationsInX = kGramMapX.get(hash);
            if (startingLocationsInX != null) {
                for (Pair<Integer, Integer> p : startingLocationsInX) {
                    // if hashcode is the same, substrings can be equal - check if they are
                    //System.out.println(x.substring(p.getFirstElement(), p.getFirstElement() + k));
                    //System.out.println((y.substring(startY, startY + k)));
                    //System.out.println();
                    if (x.substring(p.getFirstElement(), p.getFirstElement() + k).equals(y.substring(startY, startY + k))) {
                        Pair<Integer, Integer> pCopy = new Pair<>(p.getFirstElement(), startY);
                        Pair<Integer, Integer> end = new Pair<>(pCopy.getFirstElement() + k, pCopy.getSecondElement() + k);
                        matchPairs2.add(new MatchPair(pCopy, end));
                    }
                }
            }

        }

        return matchPairs2;
    }

    /**
     * Creates new map where keys are k-grams from the given string, and values are utils.Pair objects. They represent
     * beginning (inclusive) and ending (exclusive) indexes of k-gram in the string.
     * Each k-gram can appear multiple times in the string.
     *
     * @param s string
     * @param k defines the length of k-grams
     */
    private static HashMap<String, ArrayList<Pair<Integer, Integer>>> initializeKGramMap(String s, int k) {
        int sLength = s.length();
        HashMap<String, ArrayList<Pair<Integer, Integer>>> kGramMap = new HashMap<>();

        for (int start = 0; start < sLength - (k - 1); start++) {
            // end is exclusive
            int end = start + k;

            String kGram = s.substring(start, end);
            // if key exists -> append new pair
            if (kGramMap.get(kGram) != null) {
                kGramMap.get(kGram).add(new Pair<>(start, end));
            } else {
                // if key does not exists -> create new arraylist and append pair
                kGramMap.put(kGram, new ArrayList<Pair<Integer, Integer>>());
                kGramMap.get(kGram).add(new Pair<>(start, end));
            }
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
