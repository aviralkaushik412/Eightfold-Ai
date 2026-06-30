# Candidate Data Transformer

Candidate Data Transformer is a Java Maven project that transforms candidate data from recruiter CSV files and resume PDFs into a structured, normalized, validated, and projected JSON payload.

## Project Overview

This project demonstrates a modular data pipeline for candidate processing. It ingests candidate information from multiple sources, merges it into a unified candidate model, computes confidence scores, validates identity fields, and projects only the requested attributes before serializing the result to JSON.

## Features

- Parse recruiter data from CSV
- Parse candidate details from PDF resumes
- Merge multiple candidate records into a single candidate object
- Track provenance for extracted values
- Normalize and deduplicate fields such as emails, phones, and skills
- Calculate overall confidence from source reliability and provenance
- Validate candidate identity information
- Project fields based on a JSON configuration file
- Serialize the projected output to JSON

## Architecture

The application follows a simple pipeline design:

1. Parse input sources
   - CSV parser for recruiter-supplied data
   - PDF parser for resume extraction
2. Merge candidate data
   - Combine recruiter and resume views into one candidate model
3. Score confidence
   - Use source-specific confidence rules and provenance metadata
4. Validate candidate identity
   - Ensure the merged candidate includes at least one identity signal
5. Project output
   - Select only the fields requested in a JSON config file
6. Serialize output
   - Write the projected candidate view to JSON

## Folder Structure

```text
src/
  main/
    java/
      com/aviral/
        cli/              # Entry point
        confidence/       # Confidence scoring
        merger/           # Candidate merge logic
        model/            # Domain model classes
        parser/           # CSV and PDF parsers
        projection/       # Field projection logic
        validation/       # Validation rules and exceptions
    resources/
  test/
    java/
      com/aviral/
        parser/
        projection/
        merger/
        validation/
        confidence/

samples/
  candidate.csv
  resume.pdf

config.json
output/
```

## Tech Stack

- Java 17
- Maven
- Apache Commons CSV
- Apache PDFBox
- Jackson Databind
- JUnit 5

## Design Decisions

- Modularity: each stage of the pipeline has a focused responsibility.
- Provenance-first processing: extracted values keep metadata about their source and method.
- Config-driven projection: output fields are selected through JSON configuration.
- Clear validation rules: missing identity signals fail fast with meaningful errors.
- Testability: core behaviors are covered with JUnit 5 tests.

## Assumptions

- Recruiter CSV files contain standard headers such as name, email, phone, current_company, and title.
- Resume PDFs contain plain text content with recognizable contact details and skill terms.
- The output projection config is provided in JSON format.
- Confidence scoring is heuristic and intended for demonstration and extensibility.

## How to Run

1. Clone the repository.
2. Ensure Java 17 and Maven are installed.
3. Build the project:

```bash
mvn clean package
```

4. Run the main entry point from your IDE or from the command line.

Example:

```bash
mvn -q -DskipTests compile
```

Put candidate.csv in samples/
Put resume.pdf in samples/
Configure config.json
Run Main.java
output/output.json is generated

Then run the application entry point through your preferred Java launcher.

## Sample Input

### Recruiter CSV

```csv
name,email,phone,current_company,title
Aviral Kaushik,aviral@gmail.com,9876543210,Google,SDE Intern
```

### Resume PDF

A PDF containing candidate contact information, experience summary, and technical skills such as Java, Python, and SQL.

## Sample Output

```json
{
  "fullName": "Aviral Kaushik",
  "emails": ["aviral@gmail.com"],
  "phones": ["9876543210"],
  "skills": [
    {
      "name": "Java",
      "confidence": 0.95,
      "sources": ["Resume"]
    }
  ],
  "overallConfidence": 0.9
}
```

## Future Improvements

- Add more robust resume parsing with NLP-based extraction
- Support additional input formats such as LinkedIn or JSON
- Improve normalization for names, locations, and skills
- Add richer validation and error reporting
- Introduce a web UI or REST API for easier interaction

## Author

Aviral Kaushik

