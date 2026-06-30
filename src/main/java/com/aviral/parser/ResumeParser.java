package com.aviral.parser;

import com.aviral.model.Candidate;
import com.aviral.model.Skill;
import com.aviral.util.SkillDictionary;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResumeParser implements Parser {
    @Override
    public Candidate parse(String filePath) throws Exception {
        try (PDDocument document = PDDocument.load(Path.of(filePath).toFile())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String extractedText = stripper.getText(document);
            System.out.println(extractedText);

            Candidate candidate = new Candidate();
            candidate.setFullName(extractName(extractedText));
            candidate.setEmails(extractEmails(extractedText));
            candidate.setPhones(extractPhones(extractedText));
            candidate.setSkills(extractSkills(extractedText));
            return candidate;
        }
    }

    private String extractName(String extractedText) {
        if (extractedText == null || extractedText.isBlank()) {
            return null;
        }

        return Arrays.stream(extractedText.split("\\R"))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .findFirst()
                .orElse(null);
    }

    private List<String> extractEmails(String extractedText) {
        List<String> emails = new ArrayList<>();
        if (extractedText == null || extractedText.isBlank()) {
            return emails;
        }

        Pattern emailPattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");
        Matcher matcher = emailPattern.matcher(extractedText);
        while (matcher.find()) {
            emails.add(matcher.group());
        }
        return emails;
    }

    private List<String> extractPhones(String extractedText) {
        List<String> phones = new ArrayList<>();
        if (extractedText == null || extractedText.isBlank()) {
            return phones;
        }

        Pattern phonePattern = Pattern.compile("(?:\\+\\d{1,3}[-.\\s]?)?(?:\\(?\\d{2,4}\\)?[-.\\s]?)\\d{3}[-.\\s]?\\d{4}");
        Matcher matcher = phonePattern.matcher(extractedText);
        while (matcher.find()) {
            phones.add(matcher.group());
        }
        return phones;
    }

    private List<Skill> extractSkills(String extractedText) {
        List<Skill> skills = new ArrayList<>();
        if (extractedText == null || extractedText.isBlank()) {
            return skills;
        }

        for (String skillName : SkillDictionary.SKILLS) {
            if (extractedText.contains(skillName)) {
                Skill skill = new Skill();
                skill.setName(skillName);
                skill.setConfidence(1.0);
                skills.add(skill);
            }
        }

        return skills;
    }
}
