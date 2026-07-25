package com.kanbanboard.backend.service;

import org.springframework.stereotype.Service;

@Service
public class Auth {
    
    // SignUp Process

    // 1. Does pass match with confirmPass?
    public boolean passwordsMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    // 2. Is password strong enough?
    public boolean isPassGood(String password) {
        if (password.length() < 8) {
            return false;
        }

        // Check for special characters

        boolean lower = false;
        boolean upper = false;
        boolean digit = false;
        boolean special = false;

        for (int i = 0; i<password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) {
                upper = true;
            }
            else if (Character.isLowerCase(c)) {
                lower = true;
            }
            else if (Character.isDigit(c)) {
                digit = true;
            }
            else if ("!@#$%^&*()-_=".indexOf(c) != -1) {
                special = true;
            }
        }

        return lower && upper && digit && special;
    }

    // // 3. Repeat username?
    // public boolean userExist(String username) {
    //     return true;
    // }

    // 4. 
}
