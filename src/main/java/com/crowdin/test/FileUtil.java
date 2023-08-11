package com.crowdin.test;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class FileUtil {

    public static Set<File> getFilesByPattern(String pattern) {
        return Stream.of(Objects.requireNonNull(new File(".").listFiles()))
                .filter(file -> file.getName().matches(pattern))
                .filter(File::isFile)
                .collect(Collectors.toSet());
    }

    public static InputStream getInputStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            log.error("File was not found: ", e);
        }
        return null;
    }

    public static void closeStream(InputStream inputStream) {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            log.error("Cannot close stream");
        }
    }
}
