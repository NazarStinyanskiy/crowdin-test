package com.crowdin.test;

import com.crowdin.test.model.Arguments;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

//d51921909c1fa72fbb2e101cc4533e29910f56178b9e734eafae4e72adc3a7755a94aeae146c2134

@Slf4j
public class Application {

    public static void main(String[] args) {
        log.info("Starting the program");
        if (!ValidationUtil.isArgumentsValid(args)) {
            System.out.println("Program failed: Arguments is ");
            return;
        }

        Arguments arguments = new Arguments(args);
        CrowdinManager crowdinManager = new CrowdinManager(arguments);
        FileUtil.getFilesByPattern(arguments.getFilePattern()).stream()
                .map(crowdinManager::addStorage)
                .filter(Objects::nonNull)
                .map(crowdinManager::addFile)
                .filter(Objects::nonNull)
                .forEach(fileInfo -> log.info("File with name `{}` was added to the project", fileInfo.getName()));
    }
}
