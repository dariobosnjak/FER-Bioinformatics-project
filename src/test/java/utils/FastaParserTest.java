package utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FastaParserTest {
    private File temporaryFile;
    private List<String> results;
    private List<String> expectedResults;

    @BeforeEach
    void setUp() throws IOException {
            // Create temp file.
            temporaryFile = File.createTempFile("test", ".fasta");

            // Delete temp file when program exits.
            temporaryFile.deleteOnExit();

            // Write to temp file
            BufferedWriter out = new BufferedWriter(new FileWriter(temporaryFile));
            out.write(">SEQUENCE_1\n" +
                    "PRVA\n" +
                    "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\n" +
                    "AAAAAAAAAAAAAAAAAAAAAA\n" +
                    "AAA\n" +
                    ">SEQUENCE_2\n" +
                    "DRUGA\n" +
                    "BBBBBBBBBB\n" +
                    "BBBBBB\n" +
                    "BBBBBBBBBBBBBBBBBBBBBBBB\n" +
                    ">SEQUENCE_3\n" +
                    "TRECA\n" +
                    ",.jkjk\n" +
                    "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
            out.close();

        expectedResults = new ArrayList<>();
        expectedResults.add("PRVAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        expectedResults.add("DRUGABBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
        expectedResults.add("TRECACCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
    }

    @Test
    void parse() throws IOException {
        results = FastaParser.parse(temporaryFile);
        assertEquals(results.size(), expectedResults.size());

        for (int index = 0; index < results.size(); index++) {
            assertEquals(expectedResults.get(index), results.get(index));
        }
    }
}