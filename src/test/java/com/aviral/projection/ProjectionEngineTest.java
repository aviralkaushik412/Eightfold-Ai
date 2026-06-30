package com.aviral.projection;

import com.aviral.model.Candidate;
import com.aviral.model.Education;
import com.aviral.model.Experience;
import com.aviral.model.Links;
import com.aviral.model.Location;
import com.aviral.model.Provenance;
import com.aviral.model.Skill;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProjectionEngineTest {
    @TempDir
    Path tempDir;

    @Test
    void projectsOnlyRequestedFields() throws IOException {
        ProjectionEngine engine = new ProjectionEngine();
        Path configFile = writeConfig("{\"fields\":[\"fullName\",\"emails\"]}");
        Candidate candidate = new Candidate();
        candidate.setFullName("Ada Lovelace");
        candidate.setEmails(List.of("ada@example.com"));
        candidate.setPhones(List.of("+1-555-0100"));

        Map<String, Object> projected = engine.project(candidate, configFile.toString());

        assertEquals(2, projected.size());
        assertTrue(projected.containsKey("fullName"));
        assertTrue(projected.containsKey("emails"));
        assertFalse(projected.containsKey("phones"));
    }

    @Test
    void ignoresUnknownFields() throws IOException {
        ProjectionEngine engine = new ProjectionEngine();
        Path configFile = writeConfig("{\"fields\":[\"fullName\",\"unknown\",\"skills\"]}");
        Candidate candidate = new Candidate();
        candidate.setFullName("Ada Lovelace");
        candidate.setSkills(List.of(new Skill("Java", 0.95, List.of("resume"))));

        Map<String, Object> projected = engine.project(candidate, configFile.toString());

        assertEquals(2, projected.size());
        assertTrue(projected.containsKey("fullName"));
        assertTrue(projected.containsKey("skills"));
        assertFalse(projected.containsKey("unknown"));
    }

    @Test
    void returnsEmptyMapForEmptyConfig() throws IOException {
        ProjectionEngine engine = new ProjectionEngine();
        Path configFile = writeConfig("{\"fields\":[]}");
        Candidate candidate = new Candidate();
        candidate.setFullName("Ada Lovelace");

        Map<String, Object> projected = engine.project(candidate, configFile.toString());

        assertTrue(projected.isEmpty());
    }

    @Test
    void returnsEmptyMapForNullCandidate() throws IOException {
        ProjectionEngine engine = new ProjectionEngine();
        Path configFile = writeConfig("{\"fields\":[\"fullName\"]}");

        Map<String, Object> projected = engine.project(null, configFile.toString());

        assertTrue(projected.isEmpty());
        assertInstanceOf(LinkedHashMap.class, projected);
    }

    @Test
    void returnsEmptyMapWhenConfigHasNoFieldsProperty() throws IOException {
        ProjectionEngine engine = new ProjectionEngine();
        Path configFile = writeConfig("{}");
        Candidate candidate = new Candidate();
        candidate.setFullName("Ada Lovelace");

        Map<String, Object> projected = engine.project(candidate, configFile.toString());

        assertTrue(projected.isEmpty());
    }

    @Test
    void preservesConfigOrder() throws IOException {
        ProjectionEngine engine = new ProjectionEngine();
        Path configFile = writeConfig("{\"fields\":[\"headline\",\"fullName\",\"emails\"]}");
        Candidate candidate = new Candidate();
        candidate.setHeadline("Engineer");
        candidate.setFullName("Ada Lovelace");
        candidate.setEmails(List.of("ada@example.com"));

        Map<String, Object> projected = engine.project(candidate, configFile.toString());

        assertEquals(List.of("headline", "fullName", "emails"), new ArrayList<>(projected.keySet()));
        assertEquals("Engineer", projected.get("headline"));
        assertEquals("Ada Lovelace", projected.get("fullName"));
    }

    @Test
    void supportsAllCandidateFields() throws IOException {
        ProjectionEngine engine = new ProjectionEngine();
        Path configFile = writeConfig("{\"fields\":[\"candidateId\",\"fullName\",\"emails\",\"phones\",\"location\",\"links\",\"headline\",\"yearsExperience\",\"skills\",\"experience\",\"education\",\"provenance\",\"overallConfidence\"]}");
        Candidate candidate = new Candidate(
                "123",
                "Ada Lovelace",
                List.of("ada@example.com"),
                List.of("+1-555-0100"),
                new Location("London", "ENG", "UK"),
                new Links("https://linkedin.com/in/ada", "https://github.com/ada", "https://ada.dev", List.of("https://x.com/ada")),
                "Engineer",
                5.0,
                List.of(new Skill("Java", 0.95, List.of("resume"))),
                List.of(new Experience("Acme", "Engineer", "2020-01", "2023-01", "Built software")),
                List.of(new Education("MIT", "BSc", "CS", 2020)),
                List.of(new Provenance("fullName", "resume", "regex")),
                0.91
        );

        Map<String, Object> projected = engine.project(candidate, configFile.toString());

        assertEquals("123", projected.get("candidateId"));
        assertEquals("Ada Lovelace", projected.get("fullName"));
        assertEquals(List.of("ada@example.com"), projected.get("emails"));
        assertEquals(List.of("+1-555-0100"), projected.get("phones"));
        assertEquals(candidate.getLocation(), projected.get("location"));
        assertEquals(candidate.getLinks(), projected.get("links"));
        assertEquals("Engineer", projected.get("headline"));
        assertEquals(5.0, projected.get("yearsExperience"));
        assertEquals(candidate.getSkills(), projected.get("skills"));
        assertEquals(candidate.getExperience(), projected.get("experience"));
        assertEquals(candidate.getEducation(), projected.get("education"));
        assertEquals(candidate.getProvenance(), projected.get("provenance"));
        assertEquals(0.91, projected.get("overallConfidence"));
    }

    private Path writeConfig(String content) throws IOException {
        Path configFile = tempDir.resolve("config.json");
        Files.writeString(configFile, content);
        return configFile;
    }
}
