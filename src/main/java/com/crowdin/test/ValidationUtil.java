package com.crowdin.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationUtil {

    public static boolean isArgumentsValid(String[] arguments) {
        if (arguments.length != 3) {
            log.error("Wrong arguments length. {} is not equals to 3", arguments.length);
            return false;
        }
        String regexp = arguments[2];
        if (!regexp.matches(".*\\..*")) {
            log.error("Wrong RegExp: {}", arguments[2]);
            return false;
        }
        return true;
    }
}
