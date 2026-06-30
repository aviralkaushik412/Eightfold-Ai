package com.aviral.model;

import java.util.ArrayList;
import java.util.List;

public class Candidate {
    private String candidateId;
    private String fullName;
    private List<String> emails = new ArrayList<>();
    private List<String> phones = new ArrayList<>();
    private Location location;
    private Links links;
    private String headline;
    private Double yearsExperience;
    private List<Skill> skills = new ArrayList<>();
    private List<Experience> experience = new ArrayList<>();
    private List<Education> education = new ArrayList<>();
    private List<Provenance> provenance = new ArrayList<>();
    private Double overallConfidence;

    public Candidate() {
    }

    public Candidate(String candidateId, String fullName, List<String> emails, List<String> phones,
                     Location location, Links links, String headline, Double yearsExperience,
                     List<Skill> skills, List<Experience> experience, List<Education> education,
                     List<Provenance> provenance, Double overallConfidence) {
        this.candidateId = candidateId;
        this.fullName = fullName;
        this.emails = emails == null ? new ArrayList<>() : new ArrayList<>(emails);
        this.phones = phones == null ? new ArrayList<>() : new ArrayList<>(phones);
        this.location = location;
        this.links = links;
        this.headline = headline;
        this.yearsExperience = yearsExperience;
        this.skills = skills == null ? new ArrayList<>() : new ArrayList<>(skills);
        this.experience = experience == null ? new ArrayList<>() : new ArrayList<>(experience);
        this.education = education == null ? new ArrayList<>() : new ArrayList<>(education);
        this.provenance = provenance == null ? new ArrayList<>() : new ArrayList<>(provenance);
        this.overallConfidence = overallConfidence;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails == null ? new ArrayList<>() : new ArrayList<>(emails);
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones == null ? new ArrayList<>() : new ArrayList<>(phones);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public Double getYearsExperience() {
        return yearsExperience;
    }

    public void setYearsExperience(Double yearsExperience) {
        this.yearsExperience = yearsExperience;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills == null ? new ArrayList<>() : new ArrayList<>(skills);
    }

    public List<Experience> getExperience() {
        return experience;
    }

    public void setExperience(List<Experience> experience) {
        this.experience = experience == null ? new ArrayList<>() : new ArrayList<>(experience);
    }

    public List<Education> getEducation() {
        return education;
    }

    public void setEducation(List<Education> education) {
        this.education = education == null ? new ArrayList<>() : new ArrayList<>(education);
    }

    public List<Provenance> getProvenance() {
        return provenance;
    }

    public void setProvenance(List<Provenance> provenance) {
        this.provenance = provenance == null ? new ArrayList<>() : new ArrayList<>(provenance);
    }

    public Double getOverallConfidence() {
        return overallConfidence;
    }

    public void setOverallConfidence(Double overallConfidence) {
        this.overallConfidence = overallConfidence;
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "candidateId='" + candidateId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", emails=" + emails +
                ", phones=" + phones +
                ", location=" + location +
                ", links=" + links +
                ", headline='" + headline + '\'' +
                ", yearsExperience=" + yearsExperience +
                ", skills=" + skills +
                ", experience=" + experience +
                ", education=" + education +
                ", provenance=" + provenance +
                ", overallConfidence=" + overallConfidence +
                '}';
    }
}
