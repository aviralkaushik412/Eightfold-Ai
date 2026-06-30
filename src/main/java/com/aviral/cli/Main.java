package com.aviral.cli;

import com.aviral.parser.CsvParser;
import com.aviral.model.Candidate;

public class Main {
    public static void main(String[] args) throws Exception {
        CsvParser parser = new CsvParser();
        Candidate candidate = parser.parse("samples/candidate.csv");
        System.out.println(candidate);
    }
}