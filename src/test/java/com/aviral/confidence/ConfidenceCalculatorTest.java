package com.aviral.confidence;

import com.aviral.model.Candidate;
import com.aviral.model.Provenance;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfidenceCalculatorTest {
    @Test
    void calculatesAverageConfidenceAcrossAvailableFields() {
        Candidate candidate = new Candidate();
        candidate.setFullName("Ada Lovelace");
        candidate.setEmails(List.of("ada@example.com"));
        candidate.setPhones(List.of("+1-555-0100"));
        candidate.setSkills(List.of());

        candidate.setProvenance(List.of(
                new Provenance("fullName", "Resume", "regex"),
                new Provenance("emails", "CSV", "regex"),
                new Provenance("phones", "CSV", "regex"),
                new Provenance("skills", "GitHub", "api"),
                new Provenance("skills", "Resume", "api")
        ));

        ConfidenceCalculator calculator = new ConfidenceCalculator();
        double overallConfidence = calculator.calculateOverallConfidence(candidate);

        assertEquals(0.8625, overallConfidence, 1e-9);
        assertEquals(overallConfidence, candidate.getOverallConfidence(), 1e-9);
    }

    @Test
    void returnsZeroWhenNoProvenanceIsAvailable() {
        Candidate candidate = new Candidate();
        candidate.setFullName("Ada Lovelace");

        ConfidenceCalculator calculator = new ConfidenceCalculator();
        double overallConfidence = calculator.calculateOverallConfidence(candidate);

        assertEquals(0.0, overallConfidence, 1e-9);
        assertEquals(0.0, candidate.getOverallConfidence(), 1e-9);
    }
}
