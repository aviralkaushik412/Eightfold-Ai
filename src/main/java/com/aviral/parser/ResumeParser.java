package com.aviral.parser;

import com.aviral.model.Candidate;
import com.aviral.model.Experience;
import com.aviral.model.Provenance;
import com.aviral.model.Skill;
import com.aviral.util.SkillDictionary;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResumeParser implements Parser {
    @Override
    public Candidate parse(String filePath) throws Exception {
        try (PDDocument document = PDDocument.load(Path.of(filePath).toFile())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String extractedText = stripper.getText(document);

            Candidate candidate = new Candidate();
            candidate.setFullName(extractName(extractedText));
            addProvenance(candidate, "fullName", "Resume", "pdfbox-first-non-empty-line");

            candidate.setEmails(extractEmails(extractedText));
            addProvenance(candidate, "emails", "Resume", "regex-email");

            candidate.setPhones(extractPhones(extractedText));
            addProvenance(candidate, "phones", "Resume", "regex-phone");

            candidate.setSkills(extractSkills(extractedText));
            addProvenance(candidate, "skills", "Resume", "dictionary-skill-match");

            candidate.setExperience(extractExperience(extractedText));
            addProvenance(candidate, "experience", "Resume", "heuristic-experience-section");
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
        if (extractedText == null || extractedText.isBlank()) {
            return new ArrayList<>();
        }

        LinkedHashSet<String> uniqueEmails = new LinkedHashSet<>();
        Pattern emailPattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");
        Matcher matcher = emailPattern.matcher(extractedText);
        while (matcher.find()) {
            uniqueEmails.add(matcher.group());
        }
        return new ArrayList<>(uniqueEmails);
    }

    private List<String> extractPhones(String extractedText) {
        if (extractedText == null || extractedText.isBlank()) {
            return new ArrayList<>();
        }

        LinkedHashSet<String> uniquePhones = new LinkedHashSet<>();
        Pattern phonePattern = Pattern.compile("(?:\\+\\d{1,3}[-.\\s]?)?(?:\\(?\\d{2,4}\\)?[-.\\s]?)\\d{3}[-.\\s]?\\d{4}");
        Matcher matcher = phonePattern.matcher(extractedText);
        while (matcher.find()) {
            uniquePhones.add(matcher.group());
        }
        return new ArrayList<>(uniquePhones);
    }

    private List<Skill> extractSkills(String extractedText) {
        List<Skill> skills = new ArrayList<>();
        if (extractedText == null || extractedText.isBlank()) {
            return skills;
        }

        String normalizedResumeText = extractedText.toLowerCase(Locale.ROOT);
        for (String skillName : SkillDictionary.SKILLS) {
            if (normalizedResumeText.contains(skillName.toLowerCase(Locale.ROOT))) {
                skills.add(createSkill(skillName));
            }
        }

        return skills;
    }

    private List<Experience> extractExperience(String extractedText) {
        if (extractedText == null || extractedText.isBlank()) {
            return new ArrayList<>();
        }

        List<String> lines = Arrays.stream(extractedText.split("\\R"))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .toList();

        int sectionStart = -1;
        for (int index = 0; index < lines.size(); index++) {
            String line = lines.get(index).toLowerCase(Locale.ROOT);
            if (line.equals("experience") || line.equals("work experience") || line.equals("professional experience")) {
                sectionStart = index + 1;
                break;
            }
        }

        if (sectionStart < 0 || sectionStart >= lines.size()) {
            return new ArrayList<>();
        }

        List<Experience> experiences = new ArrayList<>();
        for (int index = sectionStart; index < lines.size(); index++) {
            String line = lines.get(index);
            if (isSectionHeading(line)) {
                break;
            }

            if (line.isBlank()) {
                continue;
            }

            String lowerLine = line.toLowerCase(Locale.ROOT);
            if (lowerLine.startsWith("experience") || lowerLine.startsWith("work experience") || lowerLine.startsWith("professional experience")) {
                continue;
            }

            Experience experience = new Experience();
            experience.setCompany(extractCompany(line));
            experience.setTitle(extractTitle(line));
            experience.setStartDate(extractDate(line, true));
            experience.setEndDate(extractDate(line, false));
            experience.setSummary(buildSummary(lines, index));

            if (experience.getCompany() != null || experience.getTitle() != null) {
                experiences.add(experience);
            }
        }

        return experiences;
    }

    private boolean isSectionHeading(String line) {
        String normalized = line.toLowerCase(Locale.ROOT).trim();
        return normalized.equals("education")
                || normalized.equals("skills")
                || normalized.equals("projects")
                || normalized.equals("certifications")
                || normalized.equals("contact")
                || normalized.equals("summary")
                || normalized.equals("objective");
    }

    private String extractCompany(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }

        String[] parts = line.split("\\s{2,}");
        if (parts.length > 1) {
            return parts[0];
        }

        return null;
    }

    private String extractTitle(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }

        String[] parts = line.split("\\s{2,}");
        return parts.length > 1 ? parts[1] : null;
    }

    private String extractDate(String line, boolean isStartDate) {
        if (line == null || line.isBlank()) {
            return null;
        }

        Matcher matcher = Pattern.compile("\\b(\\d{4}|\\d{2}/\\d{4}|\\d{4}-\\d{2}|\\w+\\s+\\d{4})\\b").matcher(line);
        List<String> dates = new ArrayList<>();
        while (matcher.find()) {
            dates.add(matcher.group());
        }

        if (dates.isEmpty()) {
            return null;
        }

        return isStartDate ? dates.get(0) : dates.get(dates.size() - 1);
    }

    private String buildSummary(List<String> lines, int startIndex) {
        StringBuilder summary = new StringBuilder();
        for (int index = startIndex + 1; index < lines.size() && index < startIndex + 3; index++) {
            String candidateLine = lines.get(index).trim();
            if (candidateLine.isBlank() || isSectionHeading(candidateLine)) {
                break;
            }
            if (summary.length() > 0) {
                summary.append(" ");
            }
            summary.append(candidateLine);
        }
        return summary.isEmpty() ? null : summary.toString();
    }

    private Skill createSkill(String skillName) {
        Skill skill = new Skill();
        skill.setName(skillName);
        skill.setConfidence(0.9);
        skill.setSources(new ArrayList<>(List.of("Resume")));
        return skill;
    }

    private void addProvenance(Candidate candidate, String field, String source, String method) {
        if (candidate.getProvenance() == null) {
            candidate.setProvenance(new ArrayList<>());
        }

        Provenance provenance = new Provenance();
        provenance.setField(field);
        provenance.setSource(source);
        provenance.setMethod(method);
        candidate.getProvenance().add(provenance);
    }
}
