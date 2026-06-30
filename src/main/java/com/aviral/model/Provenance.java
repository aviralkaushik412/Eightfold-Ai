package com.aviral.model;

public class Provenance {
    private String field;
    private String source;
    private String method;

    public Provenance() {
    }

    public Provenance(String field, String source, String method) {
        this.field = field;
        this.source = source;
        this.method = method;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "Provenance{" +
                "field='" + field + '\'' +
                ", source='" + source + '\'' +
                ", method='" + method + '\'' +
                '}';
    }
}
