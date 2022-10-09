package com.waverchat.api.v1.util;


import com.waverchat.api.v1.user.UserConstants;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {

    public static boolean isValidEmail(String email) {
        return new EmailValidator().isValid(email, null);
    }

    public static boolean isValidPassword(String password) {
        // checking if username is the right size
        boolean correctSize = password.length() >= UserConstants.MIN_PASSWORD_LENGTH && password.length() <= UserConstants.MIN_PASSWORD_LENGTH;
        if (!correctSize) return false;

        // checking if the password contains one of these special characters
        String specialCharacters = "~`!@#$%^&*()_-+={[}]|\\:;\"'<,>.?/";
        for (int i = 0; i <= specialCharacters.length(); i++) {
            if (i == specialCharacters.length()) return false;
            if (password.contains(specialCharacters.substring(i, i+1))) break;
        }

        // checking if the password contains at least one number
        char[] pChars = password.toCharArray();
        boolean containsDigit = false;
        for (char c : pChars) {
            if (Character.isDigit(c)) {
                containsDigit = true;
                break;
            }
        }

        return containsDigit;
    }

    public static boolean isValidUsername(String username) {
        if (username.contains(" ")) return false;
        Pattern p = Pattern.compile("^[A-Za-z0-9_]*$");
        Matcher m = p.matcher(username);
        return m.matches();
    }

}
