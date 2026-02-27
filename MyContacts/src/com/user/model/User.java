package com.user.model;

/**
 * User
 * Abstract base class for all system users.
 * This class holds the common properties and defines the contract for user types.
 * @author Developer
 * @version 1.0
 */


//base abstract class
import com.user.encryption.PasswordHashing;
import com.user.exceptions.InvalidUserDataException;

public abstract class User {

 public enum UserType { free, premium }
 
 private Preferences preferences = new Preferences();
 
 private final String email;
 private String password;
 private String firstName;
 private String lastName;

 protected User(String email, String password, String firstName, String lastName) {
     this.email = email;
     this.password = password;
     this.firstName = firstName;
     this.lastName = lastName;
 }

 public String getEmail() {
     return email;
 }

 public String getPassword() {
     return password;
 }

 public String getFirstName() {
     return firstName;
 }

 public String getLastName() {
     return lastName;
 }
 
 public Preferences getPreferences() {
     return preferences;
 }

 public void updateName(String firstName, String lastName) throws InvalidUserDataException {
     if (firstName == null || firstName.isBlank())
         throw new InvalidUserDataException("First name cannot be empty.");
     if (lastName == null || lastName.isBlank())
         throw new InvalidUserDataException("Last name cannot be empty.");

     this.firstName = firstName;
     this.lastName = lastName;
 }

 public void changePassword(String oldPassword, String newPassword) throws InvalidUserDataException {

     String hashedOld = PasswordHashing.hashPassword(oldPassword);

     if (!this.password.equals(hashedOld))
         throw new InvalidUserDataException("Old password is incorrect.");

     if (newPassword == null || newPassword.length() < 6)
         throw new InvalidUserDataException("New password must be at least 6 characters.");

     this.password = PasswordHashing.hashPassword(newPassword);
 }

 public abstract UserType getUserType();
}