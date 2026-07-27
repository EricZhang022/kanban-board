package com.kanbanboard.backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kanbanboard.backend.dto.Response;
import com.kanbanboard.backend.dto.SignUpRequest;
import com.kanbanboard.backend.entity.User;
import com.kanbanboard.backend.repo.UserRepository;

@Service
public class Auth {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    public Auth(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepo = userRepository;
        this.encoder = encoder;
    }
    
    // SignUp Process ============================================================================

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

    // 4. Repeat username?
    public boolean isUsernameExist(String username) {
        return userRepo.existsByUsername(username);
    }

    // 5. Valid Email regex
    public boolean isEmailValid(String email) {
       return email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"); 
    }

    // 6. Is Email Unique
    public boolean isEmailExist(String email) {
        return userRepo.existsByEmail(email);
    }

    public Response<String> signup(SignUpRequest request) {
        Response<String> res;
        String firstName = request.getFirstName();
        String lastName = request.getLastName();
        String username = request.getUsername();
        String password = request.getPassword();
        String confirmPass = request.getConfirmPassword();
        String email = request.getEmail();

        // 0. Valid name?
        if (!isNameValid(firstName, lastName)) {
            res = new Response<>(400, "First name and Last name must only consist alphabets");
            return res;
        }

        // 1. Valid username?
        if (!isUsernameValid(username)) {
            res = new Response<>(400, "Username can only consist alphabets or digits");
            return res;
        }

        // 2. Username already taken?
        if (isUsernameExist(username)) {
            res = new Response<>(400, "Username is already taken");
            return res;
        }

        // 2. Pass = ConfirmPass check
        if (!passwordsMatch(password, confirmPass)) {
            res = new Response<>(400, "Confirm Password does not match your password");
            return res;
        }
        if (!isPassGood(password)) {
            res = new Response<>(400, "Password needs to be at least 8 characters, including at least a lower case, an upper case, a digit, and a special character.");
            return res;
        }

        // 3. Valid Email Format
        if (!isEmailValid(email)) {
            res = new Response<>(400, "Email format does not follow the standard.");
            return res;
        }

        if (isEmailExist(email)) {
            res = new Response<>(400, "Email exists already");
            return res;
        }

        // Else start prepare to add user into db
        String hashedPass = encoder.encode(password);
        userRepo.save(new User(firstName, lastName, username, email, hashedPass));
        res = new Response<>(200, "User has been successfully created");
        return res;
    }

}
