package com.aviral.parser;

import com.aviral.model.Candidate;
import com.aviral.model.Experience;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvParser implements Parser {
    @Override
    public Candidate parse(String filePath) throws Exception {
        Candidate candidate = new Candidate();

        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             CSVParser csvParser = CSVFormat.DEFAULT.builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .build()
                     .parse(reader)) {

            if (csvParser.iterator().hasNext()) {
                CSVRecord record = csvParser.iterator().next();

                candidate.setFullName(getValue(record, "name"));
                candidate.setEmails(toList(getValue(record, "email")));
                candidate.setPhones(toList(getValue(record, "phone")));

                Experience experience = new Experience();
                experience.setCompany(getValue(record, "current_company"));
                experience.setTitle(getValue(record, "title"));
                candidate.getExperience().add(experience);
            }
        }

        return candidate;
    }

    private String getValue(CSVRecord record, String headerName) {
        String value = record.get(headerName);
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private List<String> toList(String value) {
        List<String> values = new ArrayList<>();
        if (value != null) {
            values.add(value);
        }
        return values;
    }
}
