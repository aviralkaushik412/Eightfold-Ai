package com.aviral.model;

import java.util.List;

public class Skill {
    private String name;
    private Double confidence;
    private List<String> sources;

    public Skill() {
    }

    public Skill(String name, Double confidence, List<String> sources) {
        this.name = name;
        this.confidence = confidence;
        this.sources = sources;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "name='" + name + '\'' +
                ", confidence=" + confidence +
                ", sources=" + sources +
                '}';
    }
}
