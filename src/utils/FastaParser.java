package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for fasta files.
 */
public class FastaParser {
    /**
     * Extracts sequences from fasta files.
     * @param fastaFile valid fasta file
     * @return list of sequences which are stored in given fasta file
     * @throws IOException if file does not exist or reading from it is not possible
     */
    public static List<String> parse(File fastaFile) throws IOException {
        List<String> sequences = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(fastaFile));

        String line;
        String sequence = "";
        boolean sequenceStart = false;

        // until EOF
        while ((line = br.readLine()) != null) {
            if (line.contains("TRECA")) {
                System.out.println();
            }
            if (line.startsWith(">")) {
                // save previous sequence
                saveSequence(sequences, sequence);
                // reset sequence
                sequence = "";
                // start new sequence
                sequenceStart = true;

            } else if (line.trim().isEmpty()) {
                // blank line -> end of the sequence
                sequenceStart = false;
            } else if (sequenceStart && !(line.startsWith(",") || line.startsWith(";"))) {
                // skip comments
                // add this line to the sequence
                sequence = sequence.concat(line.trim());
            }
        }
        // save last sequence
        saveSequence(sequences, sequence);
        br.close();
        return sequences;
    }

    /**
     * Helper method, used for saving sequence in the result list.
     */
    private static void saveSequence(List<String> sequences, String sequence) {
        if (!sequence.isEmpty()) {
            // sequence can end with "*" character - check it and remove it
            if (sequence.endsWith("*")) {
                sequence = sequence.substring(0, sequence.length() - 1);
            }

            // append previous sequence - create copy of object
            sequences.add(sequence);
        }
    }
}
