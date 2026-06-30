package com.aviral.validation;

import com.aviral.model.Candidate;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CandidateValidatorTest {
    private final CandidateValidator validator = new CandidateValidator();

    @Test
    void validCandidatePassesValidation() {
        Candidate candidate = new Candidate();
        candidate.setFullName("Ada Lovelace");

        assertDoesNotThrow(() -> validator.validate(candidate));
    }

    @Test
    void nullCandidateThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validator.validate(null));
    }

    @Test
    void candidateWithoutNameEmailOrPhoneThrowsIllegalArgumentException() {
        Candidate candidate = new Candidate();

        assertThrows(IllegalArgumentException.class, () -> validator.validate(candidate));
    }

    @Test
    void candidateWithOnlyEmailPasses() {
        Candidate candidate = new Candidate();
        candidate.setEmails(List.of("ada@example.com"));

        assertDoesNotThrow(() -> validator.validate(candidate));
    }

    @Test
    void candidateWithOnlyPhonePasses() {
        Candidate candidate = new Candidate();
        candidate.setPhones(List.of("+1-555-0100"));

        assertDoesNotThrow(() -> validator.validate(candidate));
    }

    @Test
    void candidateWithOnlyFullNamePasses() {
        Candidate candidate = new Candidate();
        candidate.setFullName("Ada Lovelace");

        assertDoesNotThrow(() -> validator.validate(candidate));
    }
}
