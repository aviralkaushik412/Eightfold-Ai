package com.aviral.confidence;

import com.aviral.model.Candidate;
import com.aviral.model.Provenance;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConfidenceCalculator {
    private static final double CSV_CONFIDENCE = 0.8;
    private static final double RESUME_CONFIDENCE = 0.9;
    private static final double GITHUB_CONFIDENCE = 0.95;

    public double calculateOverallConfidence(Candidate candidate) {
        if (candidate == null) {
            return 0.0;
        }

        List<String> availableFields = collectAvailableFields(candidate);
        if (availableFields.isEmpty()) {
            candidate.setOverallConfidence(0.0);
            return 0.0;
        }

        double totalScore = 0.0;
        int countedFields = 0;

        for (String fieldName : availableFields) {
            Optional<Double> fieldConfidence = calculateFieldConfidence(candidate, fieldName);
            if (fieldConfidence.isPresent()) {
                totalScore += fieldConfidence.get();
                countedFields++;
            }
        }

        double average = countedFields == 0 ? 0.0 : totalScore / countedFields;
        candidate.setOverallConfidence(average);
        return average;
    }

    private List<String> collectAvailableFields(Candidate candidate) {
        List<String> fields = new ArrayList<>();
        if (candidate.getFullName() != null && !candidate.getFullName().isBlank()) {
            fields.add("fullName");
        }
        if (candidate.getEmails() != null && !candidate.getEmails().isEmpty()) {
            fields.add("emails");
        }
        if (candidate.getPhones() != null && !candidate.getPhones().isEmpty()) {
            fields.add("phones");
        }
        if (candidate.getLocation() != null) {
            fields.add("location");
        }
        if (candidate.getLinks() != null) {
            fields.add("links");
        }
        if (candidate.getHeadline() != null && !candidate.getHeadline().isBlank()) {
            fields.add("headline");
        }
        if (candidate.getYearsExperience() != null) {
            fields.add("yearsExperience");
        }
        if (candidate.getSkills() != null && !candidate.getSkills().isEmpty()) {
            fields.add("skills");
        }
        if (candidate.getExperience() != null && !candidate.getExperience().isEmpty()) {
            fields.add("experience");
        }
        if (candidate.getEducation() != null && !candidate.getEducation().isEmpty()) {
            fields.add("education");
        }
        return fields;
    }

    /**
     * Returns the highest confidence score across all provenance entries that
     * match the given field (including nested fields like "experience.company"
     * matching "experience"). Returns Optional.empty() if no matching
     * provenance exists, so the caller can skip the field entirely.
     */
    private Optional<Double> calculateFieldConfidence(Candidate candidate, String fieldName) {
        List<Provenance> provenances = candidate.getProvenance();
        if (provenances == null || provenances.isEmpty()) {
            return Optional.empty();
        }

        Double highestScore = null;
        for (Provenance provenance : provenances) {
            if (matchesField(provenance.getField(), fieldName) && provenance.getSource() != null) {
                double score = scoreForSource(provenance.getSource().trim());
                if (highestScore == null || score > highestScore) {
                    highestScore = score;
                }
            }
        }

        return Optional.ofNullable(highestScore);
    }

    /**
     * Matches a provenance field against a candidate field name, supporting
     * nested fields (e.g. "experience.company" matches "experience").
     */
    private boolean matchesField(String provenanceField, String fieldName) {
        if (provenanceField == null) {
            return false;
        }
        return provenanceField.equals(fieldName) || provenanceField.startsWith(fieldName + ".");
    }

    private double scoreForSource(String source) {
        String normalized = source.toLowerCase();
        if (normalized.contains("github")) {
            return GITHUB_CONFIDENCE;
        }
        if (normalized.contains("resume")) {
            return RESUME_CONFIDENCE;
        }
        return CSV_CONFIDENCE;
    }
}