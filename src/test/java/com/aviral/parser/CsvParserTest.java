package com.aviral.parser;

import com.aviral.model.Candidate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CsvParserTest {
    private final CsvParser parser = new CsvParser();

    @Test
    void parsesValidCsvIntoCandidate() throws Exception {
        Candidate candidate = parser.parse("samples/candidate.csv");

        assertNotNull(candidate);
        assertEquals("Aviral Kaushik", candidate.getFullName());
        assertEquals(List.of("aviral@gmail.com"), candidate.getEmails());
        assertEquals(List.of("9876543210"), candidate.getPhones());
        assertEquals(1, candidate.getExperience().size());
    }

    @Test
    void extractsExpectedFieldsFromCsv() throws Exception {
        Candidate candidate = parser.parse("samples/candidate.csv");

        assertEquals("Aviral Kaushik", candidate.getFullName());
        assertEquals(List.of("aviral@gmail.com"), candidate.getEmails());
        assertEquals(List.of("9876543210"), candidate.getPhones());
        assertEquals("Google", candidate.getExperience().get(0).getCompany());
        assertEquals("SDE Intern", candidate.getExperience().get(0).getTitle());
    }

    @Test
    void parsesEmptyCsvIntoEmptyCandidate() throws Exception {
        Path emptyCsv = Files.createTempFile("empty-candidate", ".csv");
        Files.writeString(emptyCsv, "name,email,phone,current_company,title\n");

        Candidate candidate = parser.parse(emptyCsv.toString());

        assertNotNull(candidate);
        assertEquals(null, candidate.getFullName());
        assertTrue(candidate.getEmails().isEmpty());
        assertTrue(candidate.getPhones().isEmpty());
        assertTrue(candidate.getExperience().isEmpty());
    }

    @Test
    void invalidFilePathThrowsException() {
        assertThrows(Exception.class, () -> parser.parse("samples/does-not-exist.csv"));
    }
}
