package com.moninfotech.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PhoneValidator implements Validator {
    private static final Pattern VALID_PHONE_ADDRESS_REGEX =
            Pattern.compile("\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_PHONE_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    @Override
    public boolean isValid(String str) {
        return validate(str);
    }
}
