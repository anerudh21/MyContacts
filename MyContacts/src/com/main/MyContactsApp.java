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
 * @version 7.0
 */
import java.util.Optional;
import java.util.Scanner;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ArrayList;
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
        	// when user is not logged in
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

            // when user is logged in
            } else {

                System.out.println("\n1. Update Name");
                System.out.println("2. Change Password");
                System.out.println("3. Change Preferences");
                System.out.println("4. Add Contact");
                System.out.println("5. View Contact Details");
                System.out.println("6. Edit Contact");
                System.out.println("7. Delete Contact");
                System.out.println("8. Logout");
                System.out.print("Choose option: ");
                int choice = Integer.parseInt(sc.nextLine());

                try {

                    // Update Name option
                    if (choice == 1) {

                        System.out.print("Enter new first name: ");
                        String firstName = sc.nextLine();

                        System.out.print("Enter new last name: ");
                        String lastName = sc.nextLine();

                        session.getCurrentUser().updateName(firstName, lastName);
                        System.out.println("Name updated successfully!");

                    // Change Password option
                    } else if (choice == 2) {

                        System.out.print("Enter old password: ");
                        String oldPassword = sc.nextLine();

                        System.out.print("Enter new password: ");
                        String newPassword = sc.nextLine();

                        session.getCurrentUser().changePassword(oldPassword, newPassword);
                        System.out.println("Password changed successfully!");

                    // Change Preferences option
                    } else if (choice == 3) {

                        System.out.print("Enable Dark Mode? (true/false): ");
                        boolean darkMode = Boolean.parseBoolean(sc.nextLine());

                        System.out.print("Enable Email Notifications? (true/false): ");
                        boolean emailNotifications = Boolean.parseBoolean(sc.nextLine());

                        session.getCurrentUser()
                               .getPreferences()
                               .updatePreferences(darkMode, emailNotifications);

                        System.out.println("Preferences updated successfully!");

                    // Add Contact option
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
                               .save(contact);

                        System.out.println("Contact added successfully!");

                    // View Contact Details option
                    } else if (choice == 5) {

                        List<Contact> contacts =
                                session.getCurrentUser()
                                       .getContactRepository()
                                       .findAll();

                        if (contacts.isEmpty()) {
                            System.out.println("No contacts available.");
                            continue;
                        }

                        System.out.println("\n--- Your Contacts ---");

                        for (int i = 0; i < contacts.size(); i++) {
                            System.out.println((i + 1) + ". "
                                    + contacts.get(i).getName()
                                    + " (" + contacts.get(i).getContactType() + ")");
                        }

                        System.out.print("Select contact number: ");
                        int selection = Integer.parseInt(sc.nextLine());

                        if (selection < 1 || selection > contacts.size()) {
                            System.out.println("Invalid selection.");
                            continue;
                        }

                        Contact c = contacts.get(selection - 1);

                        List<String> phones = c.getPhoneNumbers()
                                .stream()
                                .map(PhoneNumber::getNumber)
                                .toList();

                        List<String> emails = c.getEmailAddresses()
                                .stream()
                                .map(EmailAddress::getEmail)
                                .toList();

                        ContactView view = new ContactView(
                                c.getId(),
                                c.getContactType(),
                                c.getName(),
                                c.getCreatedAt(),
                                phones,
                                emails
                        );

                        System.out.println("\nContact Details:");
                        System.out.println(view);
                    
                    // edit contact option
                    } else if (choice == 6) {

                        ContactRepository repo = session.getCurrentUser().getContactRepository();
                        List<Contact> contacts = repo.findAll();

                        if (contacts.isEmpty()) {
                            System.out.println("No contacts available to edit.");
                            return;
                        }

                        System.out.println("\n--- Your Contacts ---");
                        for (int i = 0; i < contacts.size(); i++) {
                            Contact c = contacts.get(i);
                            System.out.println((i + 1) + ". " + c.getName() + " (" + c.getContactType() + ")");
                        }

                        System.out.print("Select contact number to edit: ");
                        int index = Integer.parseInt(sc.nextLine());

                        if (index < 1 || index > contacts.size()) {
                            System.out.println("Invalid selection.");
                            return;
                        }

                        Contact existing = contacts.get(index - 1);
                        Contact edited;

                        // Deep copy using copy constructor
                        if (existing instanceof Person p) {
                            edited = new Person(p);
                        } else if (existing instanceof Organization o) {
                            edited = new Organization(o);
                        } else {
                            System.out.println("Unknown contact type.");
                            return;
                        }

                        System.out.println("\nWhat would you like to edit?");
                        System.out.println("1. Name");
                        System.out.println("2. Phone Numbers");
                        System.out.println("3. Email Addresses");

                        int editChoice = Integer.parseInt(sc.nextLine());

                        switch (editChoice) {

                            case 1 -> {
                                System.out.print("Enter new name: ");
                                String newName = sc.nextLine();
                                edited.setName(newName);
                            }

                            case 2 -> {
                                System.out.print("How many phone numbers? ");
                                int phoneCount = Integer.parseInt(sc.nextLine());

                                List<PhoneNumber> newPhones = new ArrayList<>();

                                for (int i = 0; i < phoneCount; i++) {
                                    System.out.print("Enter phone number: ");
                                    newPhones.add(new PhoneNumber(sc.nextLine()));
                                }

                                edited.setPhoneNumbers(newPhones);
                            }

                            case 3 -> {
                                System.out.print("How many email addresses? ");
                                int emailCount = Integer.parseInt(sc.nextLine());

                                List<EmailAddress> newEmails = new ArrayList<>();

                                for (int i = 0; i < emailCount; i++) {
                                    System.out.print("Enter email address: ");
                                    newEmails.add(new EmailAddress(sc.nextLine()));
                                }

                                edited.setEmailAddresses(newEmails);
                            }

                            default -> {
                                System.out.println("Invalid option.");
                                return;
                            }
                        }

                        repo.update(edited);
                        System.out.println("Contact updated successfully!");
                    
                    // delete contact option
                    } else if (choice == 7) {
                    	ContactRepository repo = session.getCurrentUser().getContactRepository();
                        List<Contact> contacts = repo.findAll();

                        if (contacts.isEmpty()) {
                            System.out.println("No contacts available to delete.");
                            return;
                        }

                        System.out.println("\n--- Your Contacts ---");
                        for (int i = 0; i < contacts.size(); i++) {
                            Contact c = contacts.get(i);
                            System.out.println((i + 1) + ". " + c.getName() + " (" + c.getContactType() + ")");
                        }

                        System.out.print("Select contact number to delete: ");
                        int index = Integer.parseInt(sc.nextLine());

                        if (index < 1 || index > contacts.size()) {
                            System.out.println("Invalid selection.");
                            return;
                        }

                        Contact selected = contacts.get(index - 1);

                        System.out.print("Are you sure you want to delete '" 
                                + selected.getName() + "'? (yes/no): ");

                        String confirmation = sc.nextLine();

                        if (confirmation.equalsIgnoreCase("yes")) {
                            try {
                                repo.delete(selected.getId());
                                System.out.println("Contact deleted successfully!");
                            } catch (NoSuchElementException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Deletion cancelled.");
                        }
                        
                    // logout option
                    } else if (choice == 8) {

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