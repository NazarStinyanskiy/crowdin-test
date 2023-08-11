package com.crowdin.test.model;

import lombok.Getter;

@Getter
public class Arguments {
    private final long projectId;
    private final String token;
    private final String filePattern;

    public Arguments(String[] arguments) {
        this.projectId = Long.parseLong(arguments[0]);
        this.token = arguments[1];
        this.filePattern = adaptFilePattern(arguments[2]);
    }

    private String adaptFilePattern(String filePattern) {
        return filePattern.replaceAll("\\.", "\\\\\\.").replaceAll("\\*", ".*");
    }
}
