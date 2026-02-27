package com.main;
/*
 * This module implements user registration for MyContactsApp.
 * User provides: email, password, first name, last name, and user type (FREE or PREMIUM).
 * Input is validated using regex (email format) and basic rules (minimum password length, non-empty fields).
 * Invalid input throws a custom InvalidUserDataException.
 * An abstract User class defines common attributes and behavior.
 * FreeUser and PremiumUser extend User using inheritance.
 * PremiumUser includes a premium flag to distinguish premium accounts. 
 * The Main class handles console input, validation, object creation, and displays the registration result.
 * 
 * @author Developer
 * @version 4.0
 */
import java.util.Optional;
import java.util.Scanner;
import com.user.auth.Authentication;
import com.user.auth.BasicAuth;
import com.user.encryption.PasswordHashing;
import com.user.exceptions.InvalidUserDataException;
import com.user.model.FreeUser;
import com.user.model.PremiumUser;
import com.user.model.User;
import com.user.repository.UserRepository;
import com.user.session.SessionManager;
import com.user.validation.Validator;
import com.user.contact.*;

public class MyContactsApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        UserRepository repository = new UserRepository();
        Authentication auth = new BasicAuth(repository);
        SessionManager session = new SessionManager();

        while (true) {

            if (!session.isLoggedIn()) {

                System.out.println("\n1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Choose option: ");
                int choice = Integer.parseInt(sc.nextLine());

                try {

                    if (choice == 1) {

                        System.out.print("Enter first name: ");
                        String firstName = sc.nextLine();

                        System.out.print("Enter last name: ");
                        String lastName = sc.nextLine();

                        System.out.print("Enter email: ");
                        String email = sc.nextLine();

                        System.out.print("Enter password: ");
                        String password = sc.nextLine();

                        System.out.print("Enter user type (free/premium): ");
                        String typeInput = sc.nextLine().toLowerCase();

                        Validator.validate(email, password, firstName, lastName);

                        String hashedPassword = PasswordHashing.hashPassword(password);

                        User user;

                        if (typeInput.equals("free")) {
                            user = new FreeUser(email, hashedPassword, firstName, lastName);
                        } else if (typeInput.equals("premium")) {
                            user = new PremiumUser(email, hashedPassword, firstName, lastName);
                        } else {
                            throw new InvalidUserDataException("Invalid user type.");
                        }

                        repository.save(user);
                        System.out.println("Registration Successful!");

                    } else if (choice == 2) {

                        System.out.print("Enter email: ");
                        String email = sc.nextLine();

                        System.out.print("Enter password: ");
                        String password = sc.nextLine();

                        Optional<User> loggedInUser = auth.login(email, password);

                        if (loggedInUser.isPresent()) {
                            session.login(loggedInUser.get());
                            System.out.println("Login Successful!");
                            System.out.println("Welcome " + session.getCurrentUser().getFirstName());
                        } else {
                            System.out.println("Invalid credentials.");
                        }

                    } else if (choice == 3) {
                        System.out.println("Exiting...");
                        break;
                    }

                } catch (InvalidUserDataException e) {
                    System.out.println("Error: " + e.getMessage());
                }

            } else {

                System.out.println("\n1. Update Name");
                System.out.println("2. Change Password");
                System.out.println("3. Change Preferences");
                System.out.println("4. Add Contact");
                System.out.println("5. Logout");
                System.out.print("Choose option: ");
                int choice = Integer.parseInt(sc.nextLine());

                try {

                    if (choice == 1) {

                        System.out.print("Enter new first name: ");
                        String firstName = sc.nextLine();

                        System.out.print("Enter new last name: ");
                        String lastName = sc.nextLine();

                        session.getCurrentUser().updateName(firstName, lastName);
                        System.out.println("Name updated successfully!");

                    } else if (choice == 2) {

                        System.out.print("Enter old password: ");
                        String oldPassword = sc.nextLine();

                        System.out.print("Enter new password: ");
                        String newPassword = sc.nextLine();

                        session.getCurrentUser().changePassword(oldPassword, newPassword);
                        System.out.println("Password changed successfully!");

                    } else if (choice == 3) {

                        System.out.print("Enable Dark Mode? (true/false): ");
                        boolean darkMode = Boolean.parseBoolean(sc.nextLine());

                        System.out.print("Enable Email Notifications? (true/false): ");
                        boolean emailNotifications = Boolean.parseBoolean(sc.nextLine());

                        session.getCurrentUser()
                               .getPreferences()
                               .updatePreferences(darkMode, emailNotifications);

                        System.out.println("Preferences updated successfully!");
                    
                	} else if (choice == 4) {

                	    System.out.print("Enter contact type (person/organization): ");
                	    String type = sc.nextLine().toLowerCase();

                	    System.out.print("Enter contact name: ");
                	    String name = sc.nextLine();

                	    Contact contact;

                	    if (type.equals("person")) {
                	        contact = new Person(name);
                	    } else if (type.equals("organization")) {
                	        contact = new Organization(name);
                	    } else {
                	        System.out.println("Invalid contact type.");
                	        continue;
                	    }

                	    System.out.print("How many phone numbers? ");
                	    int phoneCount = Integer.parseInt(sc.nextLine());

                	    for (int i = 0; i < phoneCount; i++) {
                	        System.out.print("Enter phone number: ");
                	        contact.addPhoneNumber(new PhoneNumber(sc.nextLine()));
                	    }

                	    System.out.print("How many email addresses? ");
                	    int emailCount = Integer.parseInt(sc.nextLine());

                	    for (int i = 0; i < emailCount; i++) {
                	        System.out.print("Enter email address: ");
                	        contact.addEmailAddress(new EmailAddress(sc.nextLine()));
                	    }

                	    session.getCurrentUser()
                	           .getContactRepository()
                	           .addContact(contact);

                	    System.out.println("Contact added successfully!");
                    
                    } else if (choice == 5) {

                        session.logout();
                        System.out.println("Logged out successfully.");
                    }

                } catch (InvalidUserDataException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }
}