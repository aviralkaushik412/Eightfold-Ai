package com.aviral.cli;

import com.aviral.model.Candidate;
import com.aviral.model.Skill;
import com.aviral.parser.Parser;
import com.aviral.parser.ResumeParser;

public class Main {
    public static void main(String[] args) throws Exception {
        Parser parser = new ResumeParser();
        Candidate candidate = parser.parse("samples/resume.pdf");

        StringBuilder output = new StringBuilder();
        output.append(candidate.getFullName() == null ? "" : candidate.getFullName());
        output.append("\n");
        output.append(candidate.getEmails() == null ? "" : candidate.getEmails());
        output.append("\n");
        output.append(candidate.getPhones() == null ? "" : candidate.getPhones());
        output.append("\n");
        output.append(candidate.getSkills() == null ? "" : candidate.getSkills().stream()
                .map(Skill::getName)
                .toList());

        System.out.println(output);
    }
}