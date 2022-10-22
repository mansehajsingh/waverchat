package com.waverchat.api.v1.applicationresource.usercreationconfirmation;


import com.waverchat.api.v1.applicationresource.user.UserConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserCreationConfirmationValidationUtil {

    public static boolean isValidEmail(String email) {
        if (email.isEmpty()) return false;
        Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isValidPassword(String password) {
        // checking if username is the right size
        boolean correctSize = password.length() >= UserConstants.MIN_PASSWORD_LENGTH && password.length() <= UserConstants.MAX_PASSWORD_LENGTH;
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
        if (username.isEmpty()) return false;
        if (username.length() < UserConstants.MIN_USERNAME_LENGTH || username.length() > UserConstants.MAX_USERNAME_LENGTH)
            return false;
        if (username.contains(" ")) return false;
        Pattern p = Pattern.compile("^[A-Za-z0-9_]*$");
        Matcher m = p.matcher(username);
        return m.matches();
    }

    public static boolean isValidFirstName(String firstName) {
        if (firstName == null) return false;
        if (firstName.length() == 0) return false;
        return true;
    }

    public static boolean isValidLastName(String lastName) {
        return !(lastName == null);
    }

}
