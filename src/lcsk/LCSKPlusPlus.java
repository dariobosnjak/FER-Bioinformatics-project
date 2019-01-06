package lcsk;

import utils.BinarySearch;
import utils.FenwickTree;
import utils.Pair;

import java.io.*;
import java.util.*;

class LCSKPlusPlus {

    public static void main(String[] args) {

        ///////////////// Arguments parsing /////////////////
        int k = 0;
        String pathname = null;

        try {
            pathname = args[0];
            k = Integer.parseInt(args[1]);
        } catch (NumberFormatException kNotANumber) {
            System.err.println("K must be an integer.");
            System.exit(-1);
        } catch (ArrayIndexOutOfBoundsException missingArguments) {
            System.err.println("Usage: <input file OR input directory> <k>");
            System.exit(-1);
        }

        ///////////////// Load input file /////////////////
        File filePath = new File(pathname);
        BufferedReader br;

        String x, y;

        File[] directoryListing = null;

        // if given pathname is a directory, extract all files in that directory
        if (filePath.isDirectory()) {
            directoryListing = filePath.listFiles();
            // if given pathname is a file, store that file
        } else if (filePath.isFile()) {
            directoryListing = new File[]{filePath};
        } else {
            // given pathname does not exist - terminate
            System.err.println("Given path " + filePath + " does not exist.");
            System.exit(-1);
        }
        try {
            for (File childFile : directoryListing) {
                if (!childFile.isFile()) {
                    continue;
                }
                br = new BufferedReader(new FileReader(childFile));

                x = br.readLine().trim();//"ABCDEFGH"; // "ATTATG"
                y = br.readLine().trim();//"ABCDEFGH"; // "CTATAGAGTA"

                ///////////////// LCSk++ /////////////////
                int result = lcskPlusPlus(x, y, k);
                System.out.printf("Input file=%s, k=%d, result=%d\n", childFile.getName(), k, result);

                ///////////////// Write results /////////////////
                // remove file extension
                String childName = childFile.getName().split("\\.")[0];
                writeResults(result, childName, k);

                br.close();
            }

        } catch (FileNotFoundException e) {
            // this can happen if path to the single file is passed and it does not exist
            System.err.println("File " + filePath + " does not exist.");
            System.exit(-1);
        } catch (IOException e) {
            System.err.println("Error while reading " + pathname + " file.");
            System.exit(-1);
        }
    }

    private static void writeResults(int result, String childFileName, int k) {
        // create directories on the path to the output file
        File directory = new File("data/results/java");
        directory.mkdirs();

        String outputFile = "data/results/java/input__" + childFileName + "-k__" + k + ".txt";
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(outputFile, "UTF-8");
            writer.println(result);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            System.err.println("Error while writing to " + outputFile + " file. Check if directory exists.");
            writer.close();
            System.exit(-1);
        } finally {
            writer.close();
        }
    }

    // LCSk ++ algorithm
    private static int lcskPlusPlus(String x, String y, int k) {
        // sparse dynamic programming table, key is location in the table
        HashMap<Pair<Integer, Integer>, Integer> dp = new HashMap<>();
        FenwickTree maxColDp = new FenwickTree(y.length());

        // get all k-match pairs between X and Y
        ArrayList<MatchPair> matchPairs = findAllKMatchPairsSimpleHashSearch(x, y, k);

        // all starts and ends of match pairs, sorted in row-major order
        ArrayList<Event> events = extractEvents(matchPairs);
        events.sort(new RowMajorEventComparator());

        for (Event event : events) {
            if (event.getType() == Event.EventType.START) {
                Pair<Integer, Integer> p = event.getPair();
                //int max = findMaxInArray(maxColDp, 0, p.getSecondElement());
                // getMax takes exclusive ending index
                int max = maxColDp.getMax(p.getSecondElement());
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
                if (dp.get(pStartLocation) > maxColDp.getElement(event.getPair().getSecondElement() - 1)) {
                    // maxColDp(PEnd.j) = dp(PStart)
                    maxColDp.update(event.getPair().getSecondElement() - 1, dp.get(pStartLocation));
                }
            }
        }

        int result = 0;
        if (!dp.values().isEmpty()) {
            result = Collections.max(dp.values());
        }
        return result;
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
        Event g = new Event(new Pair<>(ig, jg), Event.EventType.START);

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
    static ArrayList<MatchPair> findAllKMatchPairsNaiveSearch(String x, String y, int k) {
        ArrayList<MatchPair> matchPairs = new ArrayList<>();

        // keys are k-grams from string sequence, values are utils.Pair objects which represent the beginning and the end
        // (indexes)of the k-gram
        HashMap<String, ArrayList<Pair<Integer, Integer>>> xMap = initializeKGramMap(x, k);

        // intersection (keep only items which appear in both maps as keys (k-grams))
        for (int yStart = 0; yStart < y.length() - (k - 1); yStart++) {
            // check if xMap contains this kGram
            if (xMap.containsKey(y.substring(yStart, yStart + k))) {
                String key = y.substring(yStart, yStart + k);
                // add all matches to result
                for (Pair<Integer, Integer> xStartEnd : xMap.get(key)) {
                    matchPairs.add(new MatchPair(new Pair<>(xStartEnd.getFirstElement(), yStart), new Pair<>(xStartEnd.getSecondElement(), yStart + k)));
                }
            }
        }

        return matchPairs;
    }

    /**
     * Finds all k-match pairs between x and y sequence.
     *
     * @param x string
     * @param y string
     * @param k match length
     * @return all k-match pairs between x and y
     */
    static ArrayList<MatchPair> findAllKMatchPairsSimpleHashSearch(String x, String y, int k) {
        ArrayList<MatchPair> matchPairs = new ArrayList<>();
        HashMap<Long, ArrayList<Pair<Integer, Integer>>> kGramMapX = new HashMap<>();

        for (int startX = 0; startX < x.length() - (k - 1); startX++) {
            long hash = (long) x.substring(startX, startX + k).hashCode();
            kGramMapX.computeIfAbsent(hash, k1 -> new ArrayList<>());
            // start of the substring in Y is unknown for now
            kGramMapX.get(hash).add(new Pair<>(startX, null));
        }

        for (int startY = 0; startY < y.length() - (k - 1); startY++) {
            long hash = (long) y.substring(startY, startY + k).hashCode();
            if (kGramMapX.get(hash) != null) {
                // if hashes are same -> substrings COULD be equal
                for (Pair<Integer, Integer> p : kGramMapX.get(hash)) {
                    if (x.substring(p.getFirstElement(), p.getFirstElement() + k).equals(y.substring(startY, startY + k))) {
                        Pair<Integer, Integer> pCopy = new Pair<>(p.getFirstElement(), startY);
                        Pair<Integer, Integer> end = new Pair<>(pCopy.getFirstElement() + k, pCopy.getSecondElement() + k);
                        matchPairs.add(new MatchPair(pCopy, end));
                    }
                }
            }
        }
        return matchPairs;
    }

    /**
     * Finds all k-match pairs between x and y sequence.
     *
     * @param x string
     * @param y string
     * @param k match length
     * @return all k-match pairs between x and y
     */
    // TODO for bigger k values -> overflow
    private static ArrayList<MatchPair> findAllKMatchPairsRabinKarpSearch(String x, String y, int k) {
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
        kGramMapX.get(hash).add(new Pair<>(0, null));

        // calculate hash for other kGrams
        for (int startX = 1; startX < x.length() - (k - 1); startX++) {
            hash = ((hash - x.charAt(startX - 1) * (long) Math.pow(alphabetSize, k - 1)) * alphabetSize + x.charAt(startX + k - 1)) % prime;
            hash = hash < 0 ? hash + prime : hash;
            //hash = (hash + prime - ((long) Math.pow(alphabetSize, k - 1) * x.charAt(startX - 1) % prime)) % prime;
            //hash = (hash * alphabetSize + x.charAt(startX + k - 1)) % prime;

            if ("JGABCDEFGJFDLKHFDJSH".equals(x.substring(startX, startX + k))) {
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

            if ("JGABCDEFGJFDLKHFDJSH".equals(y.substring(startY, startY + k))) {
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
                // if key does not exists -> create new array list and append pair
                kGramMap.put(kGram, new ArrayList<>());
                kGramMap.get(kGram).add(new Pair<>(start, end));
            }
        }

        return kGramMap;
    }

    /**
     * Represents one match pair.
     */
    static class MatchPair {
        private final int length;
        private final Pair<Integer, Integer> start;
        private final Pair<Integer, Integer> end;

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
        MatchPair(Pair<Integer, Integer> start, Pair<Integer, Integer> end) {
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
