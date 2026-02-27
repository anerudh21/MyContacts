package com.user.validation;

// validator class for validating all registration fields
import java.util.regex.Pattern;
import com.user.exceptions.InvalidUserDataException;

public class Validator {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    public static void validate(String email,String password,String firstName,String lastName) throws InvalidUserDataException {

        if (email == null || !pattern.matcher(email).matches()) throw new InvalidUserDataException("Invalid email format.");
        
        if (password == null || password.length() < 6) throw new InvalidUserDataException("Password must be at least 6 characters.");
        
        if (firstName == null || firstName.isBlank()) throw new InvalidUserDataException("First name cannot be empty.");
        
        if (lastName == null || lastName.isBlank()) throw new InvalidUserDataException("Last name cannot be empty.");
    }
}