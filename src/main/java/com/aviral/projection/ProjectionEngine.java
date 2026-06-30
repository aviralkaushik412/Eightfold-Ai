package com.aviral.projection;

import com.aviral.model.Candidate;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProjectionEngine {
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Projects a candidate into a field-selected map using the config file ordering.
     *
     * @param candidate the candidate to project
     * @param configFilePath path to the projection config JSON file
     * @return a LinkedHashMap containing the requested fields in config order
     * @throws IOException if the config file cannot be read or parsed
     */
    public Map<String, Object> project(Candidate candidate, String configFilePath) throws IOException {
        if (candidate == null) {
            return new LinkedHashMap<>();
        }

        ProjectionConfig config = objectMapper.readValue(Files.readString(Path.of(configFilePath)), ProjectionConfig.class);
        List<String> fields = config.getFields();
        Map<String, Object> projected = new LinkedHashMap<>();
        if (fields == null || fields.isEmpty()) {
            return projected;
        }

        for (String field : fields) {
            putField(projected, candidate, field);
        }

        return projected;
    }

    private void putField(Map<String, Object> projected, Candidate candidate, String field) {
        switch (field) {
            case "candidateId":
                projected.put("candidateId", candidate.getCandidateId());
                break;
            case "fullName":
                projected.put("fullName", candidate.getFullName());
                break;
            case "emails":
                projected.put("emails", candidate.getEmails());
                break;
            case "phones":
                projected.put("phones", candidate.getPhones());
                break;
            case "location":
                projected.put("location", candidate.getLocation());
                break;
            case "links":
                projected.put("links", candidate.getLinks());
                break;
            case "headline":
                projected.put("headline", candidate.getHeadline());
                break;
            case "yearsExperience":
                projected.put("yearsExperience", candidate.getYearsExperience());
                break;
            case "skills":
                projected.put("skills", candidate.getSkills());
                break;
            case "experience":
                projected.put("experience", candidate.getExperience());
                break;
            case "education":
                projected.put("education", candidate.getEducation());
                break;
            case "provenance":
                projected.put("provenance", candidate.getProvenance());
                break;
            case "overallConfidence":
                projected.put("overallConfidence", candidate.getOverallConfidence());
                break;
            default:
                break;
        }
    }
}
