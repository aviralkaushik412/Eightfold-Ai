package com.aviral.model;

import java.util.List;

public class Links {
    private String linkedin;
    private String github;
    private String portfolio;
    private List<String> other;

    public Links() {
    }

    public Links(String linkedin, String github, String portfolio, List<String> other) {
        this.linkedin = linkedin;
        this.github = github;
        this.portfolio = portfolio;
        this.other = other;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(String portfolio) {
        this.portfolio = portfolio;
    }

    public List<String> getOther() {
        return other;
    }

    public void setOther(List<String> other) {
        this.other = other;
    }

    @Override
    public String toString() {
        return "Links{" +
                "linkedin='" + linkedin + '\'' +
                ", github='" + github + '\'' +
                ", portfolio='" + portfolio + '\'' +
                ", other=" + other +
                '}';
    }
}
