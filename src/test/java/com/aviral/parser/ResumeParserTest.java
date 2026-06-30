package com.aviral.parser;

import com.aviral.model.Candidate;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResumeParserTest {
    private final ResumeParser parser = new ResumeParser();

    @Test
    void readsResumePdfSuccessfully() throws Exception {
        Candidate candidate = parser.parse("samples/resume.pdf");

        assertNotNull(candidate);
        assertFalse(candidate.getFullName() == null || candidate.getFullName().isBlank());
    }

    @Test
    void extractsExpectedResumeFields() throws Exception {
        Candidate candidate = parser.parse("samples/resume.pdf");

        assertEquals("Aviral Kaushik", candidate.getFullName());
        assertEquals(List.of("aviral@example.com"), candidate.getEmails());
        assertEquals(List.of("9876543210"), candidate.getPhones());
        assertFalse(candidate.getSkills().isEmpty());
        assertTrue(candidate.getSkills().stream().anyMatch(skill -> "Java".equals(skill.getName())));
    }

    @Test
    void emptyPdfReturnsEmptyCandidate() throws Exception {
        Path emptyPdf = Files.createTempFile("empty-resume", ".pdf");
        Candidate candidate = parser.parse(emptyPdf.toString());

        assertNotNull(candidate);
        assertTrue(candidate.getFullName() == null || candidate.getFullName().isBlank());
        assertTrue(candidate.getEmails().isEmpty());
        assertTrue(candidate.getPhones().isEmpty());
        assertTrue(candidate.getSkills().isEmpty());
    }

    @Test
    void invalidFilePathThrowsException() {
        assertThrows(Exception.class, () -> parser.parse("samples/does-not-exist.pdf"));
    }
}
