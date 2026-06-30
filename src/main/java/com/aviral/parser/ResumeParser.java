package com.aviral.parser;

import com.aviral.model.Candidate;

public interface ResumeParser {
    Candidate parse(String filePath) throws Exception;
}
