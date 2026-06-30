package com.aviral.normalizer;

import java.util.Arrays;
import java.util.stream.Collectors;

public class NameNormalizer {
    public String normalize(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }

        return Arrays.stream(name.trim().split("\\s+"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }
}
