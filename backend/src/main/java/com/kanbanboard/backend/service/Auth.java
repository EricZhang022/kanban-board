package com.kanbanboard.backend.service;

import org.springframework.stereotype.Service;

@Service
public class Auth {
    
    // SignUp Process

    // 0. Is First & Last name valid
    public boolean isNameValid(String firstName, String lastName) {
        return firstName.matches("[A-Za-z]+") && lastName.matches("[A-Za-z]+");
    }

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

    // 3. Is Username valid?
    public boolean isUsernameValid(String username) {
        return username.matches("[A-Za-z0-9]+");
    }

    // // 4. Repeat username?
    // public boolean userExist(String username) {
    //     return true;
    // }

    // 5. Valid Email regex
    public boolean isEmailValid(String email) {
       return email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"); 
    }

    // // 6. Is Email Unique
    // public boolean isEmailExist(String email) {

    // }


}
