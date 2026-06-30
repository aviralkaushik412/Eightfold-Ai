package com.aviral.normalizer;

import java.util.LinkedHashMap;
import java.util.Map;

public class SkillNormalizer {
    private static final Map<String, String> ALIASES = new LinkedHashMap<>();

    static {
        ALIASES.put("js", "JavaScript");
        ALIASES.put("javascript", "JavaScript");
        ALIASES.put("java script", "JavaScript");
        ALIASES.put("node", "Node.js");
        ALIASES.put("nodejs", "Node.js");
        ALIASES.put("node.js", "Node.js");
        ALIASES.put("reactjs", "React");
        ALIASES.put("react", "React");
        ALIASES.put("springboot", "Spring Boot");
        ALIASES.put("spring boot", "Spring Boot");
        ALIASES.put("restapi", "REST API");
        ALIASES.put("rest api", "REST API");
    }

    public String normalize(String skill) {
        if (skill == null || skill.isBlank()) {
            return null;
        }

        String normalized = skill.trim().toLowerCase();
        return ALIASES.getOrDefault(normalized, skill.trim());
    }
}
