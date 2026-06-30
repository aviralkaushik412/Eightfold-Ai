package com.aviral.merger;

import com.aviral.model.Candidate;
import com.aviral.model.Education;
import com.aviral.model.Experience;
import com.aviral.model.Provenance;
import com.aviral.model.Skill;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class CandidateMerger {
    public Candidate merge(Candidate existingCandidate, Candidate resumeCandidate) {
        if (existingCandidate == null) {
            return copyCandidate(resumeCandidate);
        }
        if (resumeCandidate == null) {
            return copyCandidate(existingCandidate);
        }

        Candidate merged = new Candidate();
        merged.setCandidateId(existingCandidate.getCandidateId() != null ? existingCandidate.getCandidateId() : resumeCandidate.getCandidateId());
        merged.setFullName(resumeCandidate.getFullName() != null ? resumeCandidate.getFullName() : existingCandidate.getFullName());
        merged.setEmails(mergeStrings(existingCandidate.getEmails(), resumeCandidate.getEmails()));
        merged.setPhones(mergeStrings(existingCandidate.getPhones(), resumeCandidate.getPhones()));
        merged.setLocation(existingCandidate.getLocation() != null ? existingCandidate.getLocation() : resumeCandidate.getLocation());
        merged.setLinks(existingCandidate.getLinks() != null ? existingCandidate.getLinks() : resumeCandidate.getLinks());
        merged.setHeadline(existingCandidate.getHeadline() != null ? existingCandidate.getHeadline() : resumeCandidate.getHeadline());
        merged.setYearsExperience(existingCandidate.getYearsExperience() != null ? existingCandidate.getYearsExperience() : resumeCandidate.getYearsExperience());
        merged.setSkills(mergeSkills(existingCandidate.getSkills(), resumeCandidate.getSkills()));
        merged.setExperience(mergeExperiences(existingCandidate.getExperience(), resumeCandidate.getExperience()));
        merged.setEducation(mergeEducations(existingCandidate.getEducation(), resumeCandidate.getEducation()));
        merged.setProvenance(mergeProvenance(existingCandidate.getProvenance(), resumeCandidate.getProvenance()));
        merged.setOverallConfidence(null);
        return merged;
    }

    private Candidate copyCandidate(Candidate candidate) {
        if (candidate == null) {
            return null;
        }

        Candidate copy = new Candidate();
        copy.setCandidateId(candidate.getCandidateId());
        copy.setFullName(candidate.getFullName());
        copy.setEmails(copyStrings(candidate.getEmails()));
        copy.setPhones(copyStrings(candidate.getPhones()));
        copy.setLocation(candidate.getLocation());
        copy.setLinks(candidate.getLinks());
        copy.setHeadline(candidate.getHeadline());
        copy.setYearsExperience(candidate.getYearsExperience());
        copy.setSkills(copySkills(candidate.getSkills()));
        copy.setExperience(copyExperiences(candidate.getExperience()));
        copy.setEducation(copyEducations(candidate.getEducation()));
        copy.setProvenance(copyProvenance(candidate.getProvenance()));
        copy.setOverallConfidence(candidate.getOverallConfidence());
        return copy;
    }

    private List<String> mergeStrings(List<String> first, List<String> second) {
        Set<String> merged = new LinkedHashSet<>();
        addUniqueValues(merged, first);
        addUniqueValues(merged, second);
        return new ArrayList<>(merged);
    }

    private List<Skill> mergeSkills(List<Skill> first, List<Skill> second) {
        Map<String, Skill> mergedByKey = new LinkedHashMap<>();
        addSkills(mergedByKey, first);
        addSkills(mergedByKey, second);
        return new ArrayList<>(mergedByKey.values());
    }

    private List<Experience> mergeExperiences(List<Experience> first, List<Experience> second) {
        Map<String, Experience> mergedByKey = new LinkedHashMap<>();
        addExperiences(mergedByKey, first);
        addExperiences(mergedByKey, second);
        return new ArrayList<>(mergedByKey.values());
    }

    private List<Education> mergeEducations(List<Education> first, List<Education> second) {
        Map<String, Education> mergedByKey = new LinkedHashMap<>();
        addEducations(mergedByKey, first);
        addEducations(mergedByKey, second);
        return new ArrayList<>(mergedByKey.values());
    }

    private List<Provenance> mergeProvenance(List<Provenance> first, List<Provenance> second) {
        Map<String, Provenance> mergedByKey = new LinkedHashMap<>();
        addProvenances(mergedByKey, first);
        addProvenances(mergedByKey, second);
        return new ArrayList<>(mergedByKey.values());
    }

    private void addUniqueValues(Set<String> target, List<String> values) {
        if (values == null) {
            return;
        }
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                target.add(value);
            }
        }
    }

    private void addSkills(Map<String, Skill> target, List<Skill> skills) {
        if (skills == null) {
            return;
        }
        for (Skill skill : skills) {
            if (skill == null || skill.getName() == null || skill.getName().isBlank()) {
                continue;
            }

            String key = buildSkillKey(skill);
            Skill existing = target.get(key);
            if (existing == null) {
                target.put(key, copySkill(skill));
            } else {
                existing.setConfidence(Math.max(
                        existing.getConfidence() == null ? 0.0 : existing.getConfidence(),
                        skill.getConfidence() == null ? 0.0 : skill.getConfidence()));
                mergeSourceLists(existing, skill);
            }
        }
    }

    private void addExperiences(Map<String, Experience> target, List<Experience> experiences) {
        if (experiences == null) {
            return;
        }
        for (Experience experience : experiences) {
            if (experience == null) {
                continue;
            }
            String key = buildExperienceKey(experience);
            if (!target.containsKey(key)) {
                target.put(key, copyExperience(experience));
            }
        }
    }

    private void addEducations(Map<String, Education> target, List<Education> educations) {
        if (educations == null) {
            return;
        }
        for (Education education : educations) {
            if (education == null) {
                continue;
            }
            String key = buildEducationKey(education);
            if (!target.containsKey(key)) {
                target.put(key, copyEducation(education));
            }
        }
    }

    private void addProvenances(Map<String, Provenance> target, List<Provenance> provenances) {
        if (provenances == null) {
            return;
        }
        for (Provenance provenance : provenances) {
            if (provenance == null) {
                continue;
            }
            String key = buildProvenanceKey(provenance);
            if (!target.containsKey(key)) {
                target.put(key, copyProvenance(provenance));
            }
        }
    }

    private void mergeSourceLists(Skill target, Skill source) {
        Set<String> mergedSources = new LinkedHashSet<>();
        if (target.getSources() != null) {
            for (String sourceValue : target.getSources()) {
                if (sourceValue != null && !sourceValue.isBlank()) {
                    mergedSources.add(sourceValue);
                }
            }
        }
        if (source.getSources() != null) {
            for (String sourceValue : source.getSources()) {
                if (sourceValue != null && !sourceValue.isBlank()) {
                    mergedSources.add(sourceValue);
                }
            }
        }
        target.setSources(new ArrayList<>(mergedSources));
    }

    private String buildSkillKey(Skill skill) {
        return skill.getName().trim().toLowerCase(Locale.ROOT);
    }

    private String buildExperienceKey(Experience experience) {
        return (experience.getCompany() == null ? "" : experience.getCompany().trim().toLowerCase(Locale.ROOT))
                + "|"
                + (experience.getTitle() == null ? "" : experience.getTitle().trim().toLowerCase(Locale.ROOT));
    }

    private String buildEducationKey(Education education) {
        return (education.getInstitution() == null ? "" : education.getInstitution().trim().toLowerCase(Locale.ROOT))
                + "|"
                + (education.getDegree() == null ? "" : education.getDegree().trim().toLowerCase(Locale.ROOT));
    }

    private String buildProvenanceKey(Provenance provenance) {
        return (provenance.getField() == null ? "" : provenance.getField().trim().toLowerCase(Locale.ROOT))
                + "|"
                + (provenance.getSource() == null ? "" : provenance.getSource().trim().toLowerCase(Locale.ROOT))
                + "|"
                + (provenance.getMethod() == null ? "" : provenance.getMethod().trim().toLowerCase(Locale.ROOT));
    }

    private List<String> copyStrings(List<String> values) {
        return values == null ? new ArrayList<>() : new ArrayList<>(values);
    }

    private List<Skill> copySkills(List<Skill> skills) {
        if (skills == null) {
            return new ArrayList<>();
        }
        List<Skill> copied = new ArrayList<>();
        for (Skill skill : skills) {
            copied.add(copySkill(skill));
        }
        return copied;
    }

    private Skill copySkill(Skill skill) {
        if (skill == null) {
            return null;
        }
        Skill copy = new Skill();
        copy.setName(skill.getName());
        copy.setConfidence(skill.getConfidence());
        copy.setSources(skill.getSources() == null ? new ArrayList<>() : new ArrayList<>(skill.getSources()));
        return copy;
    }

    private List<Experience> copyExperiences(List<Experience> experiences) {
        if (experiences == null) {
            return new ArrayList<>();
        }
        List<Experience> copied = new ArrayList<>();
        for (Experience experience : experiences) {
            copied.add(copyExperience(experience));
        }
        return copied;
    }

    private Experience copyExperience(Experience experience) {
        if (experience == null) {
            return null;
        }
        Experience copy = new Experience();
        copy.setCompany(experience.getCompany());
        copy.setTitle(experience.getTitle());
        copy.setStartDate(experience.getStartDate());
        copy.setEndDate(experience.getEndDate());
        copy.setSummary(experience.getSummary());
        return copy;
    }

    private List<Education> copyEducations(List<Education> educations) {
        if (educations == null) {
            return new ArrayList<>();
        }
        List<Education> copied = new ArrayList<>();
        for (Education education : educations) {
            copied.add(copyEducation(education));
        }
        return copied;
    }

    private Education copyEducation(Education education) {
        if (education == null) {
            return null;
        }
        Education copy = new Education();
        copy.setInstitution(education.getInstitution());
        copy.setDegree(education.getDegree());
        copy.setField(education.getField());
        copy.setEndYear(education.getEndYear());
        return copy;
    }

    private List<Provenance> copyProvenance(List<Provenance> provenances) {
        if (provenances == null) {
            return new ArrayList<>();
        }
        List<Provenance> copied = new ArrayList<>();
        for (Provenance provenance : provenances) {
            copied.add(copyProvenance(provenance));
        }
        return copied;
    }

    private Provenance copyProvenance(Provenance provenance) {
        if (provenance == null) {
            return null;
        }
        Provenance copy = new Provenance();
        copy.setField(provenance.getField());
        copy.setSource(provenance.getSource());
        copy.setMethod(provenance.getMethod());
        return copy;
    }
}
