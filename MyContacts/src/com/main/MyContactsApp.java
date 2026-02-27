package com.main;
/**
 * MyContactsApp UC1 – User Registration
 * This main class coordinates the user registration process, taking user input,
 * validating it, and creating the appropriate user objects.
 * @author Developer
 * @version 3.0
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

public class MyContactsApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        UserRepository repository = new UserRepository();
        Authentication auth = new BasicAuth(repository);
        SessionManager session = new SessionManager();

        while (true) {

            if (!session.isLoggedIn()) {

                System.out.println("\n1. Register User");
                System.out.println("2. Login ");
                System.out.println("3. Exit the Application");
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
                System.out.println("4. Logout");
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