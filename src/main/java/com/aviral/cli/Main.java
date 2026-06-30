package com.aviral.cli;

import com.aviral.confidence.ConfidenceCalculator;
import com.aviral.merger.CandidateMerger;
import com.aviral.model.Candidate;
import com.aviral.parser.CsvParser;
import com.aviral.parser.Parser;
import com.aviral.parser.ResumeParser;
import com.aviral.projection.ProjectionEngine;
import com.aviral.validation.CandidateValidator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        Parser csvParser = new CsvParser();
        Candidate recruiterCandidate = csvParser.parse("samples/candidate.csv");

        Parser resumeParser = new ResumeParser();
        Candidate resumeCandidate = resumeParser.parse("samples/resume.pdf");

        CandidateMerger merger = new CandidateMerger();
        Candidate mergedCandidate = merger.merge(recruiterCandidate, resumeCandidate);

        ConfidenceCalculator confidenceCalculator = new ConfidenceCalculator();
        confidenceCalculator.calculateOverallConfidence(mergedCandidate);

        CandidateValidator validator = new CandidateValidator();
        validator.validate(mergedCandidate);

        ProjectionEngine projectionEngine = new ProjectionEngine();
        Map<String, Object> projected = projectionEngine.project(mergedCandidate, "config.json");

        Files.createDirectories(Path.of("output"));
        Path outputFile = Path.of("output/output.json");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile.toFile(), projected);

        System.out.println("Pipeline completed successfully. Output written to " + outputFile.toAbsolutePath());
    }
}