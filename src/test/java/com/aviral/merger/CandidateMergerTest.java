package com.aviral.merger;

import com.aviral.model.Candidate;
import com.aviral.model.Education;
import com.aviral.model.Experience;
import com.aviral.model.Provenance;
import com.aviral.model.Skill;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CandidateMergerTest {
    private final CandidateMerger merger = new CandidateMerger();

    @Test
    void resumeFullNameOverridesCsvFullName() {
        Candidate csvCandidate = new Candidate();
        csvCandidate.setFullName("CSV Name");

        Candidate resumeCandidate = new Candidate();
        resumeCandidate.setFullName("Resume Name");

        Candidate merged = merger.merge(csvCandidate, resumeCandidate);

        assertEquals("Resume Name", merged.getFullName());
    }

    @Test
    void mergesEmailsWithoutDuplicates() {
        Candidate csvCandidate = new Candidate();
        csvCandidate.setEmails(new ArrayList<>(List.of("a@example.com", "b@example.com")));

        Candidate resumeCandidate = new Candidate();
        resumeCandidate.setEmails(new ArrayList<>(List.of("b@example.com", "c@example.com")));

        Candidate merged = merger.merge(csvCandidate, resumeCandidate);

        assertEquals(List.of("a@example.com", "b@example.com", "c@example.com"), merged.getEmails());
    }

    @Test
    void mergesPhonesWithoutDuplicates() {
        Candidate csvCandidate = new Candidate();
        csvCandidate.setPhones(new ArrayList<>(List.of("111", "222")));

        Candidate resumeCandidate = new Candidate();
        resumeCandidate.setPhones(new ArrayList<>(List.of("222", "333")));

        Candidate merged = merger.merge(csvCandidate, resumeCandidate);

        assertEquals(List.of("111", "222", "333"), merged.getPhones());
    }

    @Test
    void mergesSkillsWithoutDuplicates() {
        Candidate csvCandidate = new Candidate();
        csvCandidate.setSkills(new ArrayList<>(List.of(skill("Java", 0.8, List.of("CSV")))));

        Candidate resumeCandidate = new Candidate();
        resumeCandidate.setSkills(new ArrayList<>(List.of(skill("Java", 0.95, List.of("Resume")), skill("Python", 0.9, List.of("Resume")))));

        Candidate merged = merger.merge(csvCandidate, resumeCandidate);

        assertEquals(2, merged.getSkills().size());
        assertTrue(merged.getSkills().stream().anyMatch(skill -> "Java".equals(skill.getName())));
        assertTrue(merged.getSkills().stream().anyMatch(skill -> "Python".equals(skill.getName())));
    }

    @Test
    void mergesExperienceLists() {
        Candidate csvCandidate = new Candidate();
        csvCandidate.setExperience(new ArrayList<>(List.of(experience("Acme", "Engineer"))));

        Candidate resumeCandidate = new Candidate();
        resumeCandidate.setExperience(new ArrayList<>(List.of(experience("Globex", "Intern"))));

        Candidate merged = merger.merge(csvCandidate, resumeCandidate);

        assertEquals(2, merged.getExperience().size());
    }

    @Test
    void mergesEducationLists() {
        Candidate csvCandidate = new Candidate();
        csvCandidate.setEducation(new ArrayList<>(List.of(education("MIT", "BS"))));

        Candidate resumeCandidate = new Candidate();
        resumeCandidate.setEducation(new ArrayList<>(List.of(education("Stanford", "MS"))));

        Candidate merged = merger.merge(csvCandidate, resumeCandidate);

        assertEquals(2, merged.getEducation().size());
    }

    @Test
    void mergesProvenanceLists() {
        Candidate csvCandidate = new Candidate();
        csvCandidate.setProvenance(new ArrayList<>(List.of(provenance("fullName", "CSV", "header"))));

        Candidate resumeCandidate = new Candidate();
        resumeCandidate.setProvenance(new ArrayList<>(List.of(provenance("emails", "Resume", "regex"))));

        Candidate merged = merger.merge(csvCandidate, resumeCandidate);

        assertEquals(2, merged.getProvenance().size());
    }

    @Test
    void leavesInputCandidatesUnchangedAfterMerge() {
        Candidate csvCandidate = new Candidate();
        csvCandidate.setFullName("CSV Name");
        csvCandidate.setEmails(new ArrayList<>(List.of("a@example.com")));
        csvCandidate.setPhones(new ArrayList<>(List.of("111")));
        csvCandidate.setSkills(new ArrayList<>(List.of(skill("Java", 0.8, List.of("CSV")))));
        csvCandidate.setExperience(new ArrayList<>(List.of(experience("Acme", "Engineer"))));
        csvCandidate.setEducation(new ArrayList<>(List.of(education("MIT", "BS"))));
        csvCandidate.setProvenance(new ArrayList<>(List.of(provenance("fullName", "CSV", "header"))));

        Candidate resumeCandidate = new Candidate();
        resumeCandidate.setFullName("Resume Name");
        resumeCandidate.setEmails(new ArrayList<>(List.of("b@example.com")));
        resumeCandidate.setPhones(new ArrayList<>(List.of("222")));
        resumeCandidate.setSkills(new ArrayList<>(List.of(skill("Java", 0.95, List.of("Resume")))));
        resumeCandidate.setExperience(new ArrayList<>(List.of(experience("Globex", "Intern"))));
        resumeCandidate.setEducation(new ArrayList<>(List.of(education("Stanford", "MS"))));
        resumeCandidate.setProvenance(new ArrayList<>(List.of(provenance("emails", "Resume", "regex"))));

        Candidate merged = merger.merge(csvCandidate, resumeCandidate);

        assertEquals("CSV Name", csvCandidate.getFullName());
        assertEquals(List.of("a@example.com"), csvCandidate.getEmails());
        assertEquals(List.of("111"), csvCandidate.getPhones());
        assertEquals(1, csvCandidate.getSkills().size());
        assertEquals(1, csvCandidate.getExperience().size());
        assertEquals(1, csvCandidate.getEducation().size());
        assertEquals(1, csvCandidate.getProvenance().size());

        assertEquals("Resume Name", resumeCandidate.getFullName());
        assertEquals(List.of("b@example.com"), resumeCandidate.getEmails());
        assertEquals(List.of("222"), resumeCandidate.getPhones());
        assertEquals(1, resumeCandidate.getSkills().size());
        assertEquals(1, resumeCandidate.getExperience().size());
        assertEquals(1, resumeCandidate.getEducation().size());
        assertEquals(1, resumeCandidate.getProvenance().size());

        assertNotSame(csvCandidate, merged);
        assertNotSame(resumeCandidate, merged);
    }

    private Skill skill(String name, double confidence, List<String> sources) {
        Skill skill = new Skill();
        skill.setName(name);
        skill.setConfidence(confidence);
        skill.setSources(new ArrayList<>(sources));
        return skill;
    }

    private Experience experience(String company, String title) {
        Experience experience = new Experience();
        experience.setCompany(company);
        experience.setTitle(title);
        return experience;
    }

    private Education education(String institution, String degree) {
        Education education = new Education();
        education.setInstitution(institution);
        education.setDegree(degree);
        return education;
    }

    private Provenance provenance(String field, String source, String method) {
        Provenance provenance = new Provenance();
        provenance.setField(field);
        provenance.setSource(source);
        provenance.setMethod(method);
        return provenance;
    }
}
