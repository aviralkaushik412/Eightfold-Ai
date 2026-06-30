package com.aviral.parser;

import com.aviral.model.Candidate;

public interface Parser {
    Candidate parse(String filePath) throws Exception;
}
