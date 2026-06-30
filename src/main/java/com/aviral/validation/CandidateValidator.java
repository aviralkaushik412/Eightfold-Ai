package com.aviral.validation;

import com.aviral.model.Candidate;

public class CandidateValidator {
    /**
     * Validates that a candidate exists and carries at least one identity field.
     *
     * @param candidate the candidate to validate
     * @throws IllegalArgumentException if the candidate is null or lacks a full name, email, or phone
     */
    public void validate(Candidate candidate) {
        if (candidate == null) {
            throw new IllegalArgumentException("Candidate cannot be null.");
        }

        boolean hasIdentity = hasValue(candidate.getFullName())
                || hasAnyValue(candidate.getEmails())
                || hasAnyValue(candidate.getPhones());

        if (!hasIdentity) {
            throw new IllegalArgumentException("Candidate must have at least one of: fullName, email, phone.");
        }
    }

    private boolean hasValue(String value) {
        return value != null && !value.isBlank();
    }

    private boolean hasAnyValue(java.util.List<String> values) {
        return values != null && !values.isEmpty();
    }
}
