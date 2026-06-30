package com.aviral.model;

public class Education {
    private String institution;
    private String degree;
    private String field;
    private Integer endYear;

    public Education() {
    }

    public Education(String institution, String degree, String field, Integer endYear) {
        this.institution = institution;
        this.degree = degree;
        this.field = field;
        this.endYear = endYear;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    @Override
    public String toString() {
        return "Education{" +
                "institution='" + institution + '\'' +
                ", degree='" + degree + '\'' +
                ", field='" + field + '\'' +
                ", endYear=" + endYear +
                '}';
    }
}
