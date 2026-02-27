package com.main;
/**
 * MyContactsApp UC1 – User Registration
 * This main class coordinates the user registration process, taking user input,
 * validating it, and creating the appropriate user objects.
 * @author Developer
 * @version 1.0
 */


import java.util.Scanner;
import com.user.exceptions.InvalidUserDataException;
import com.user.model.FreeUser;
import com.user.model.PremiumUser;
import com.user.model.User;
import com.user.validation.Validator;

// main class, start of execution
public class MyContactsApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {
        	// get registration details from user
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
            
            // validate all fields
            Validator.validate(email,password,firstName,lastName);
            User user;
            
            // check if user type is free or premium
            if (typeInput.equals("free")) {
                user = new FreeUser(email,password,firstName,lastName);
            } else if (typeInput.equals("premium")) {
                user = new PremiumUser(email,password,firstName,lastName);
            } else {
                throw new InvalidUserDataException("Invalid user type.");
                
            }
            
            // display registration details
            System.out.println("\nRegistration Successful!");
            System.out.println("Name: "+user.getFirstName()+" "+user.getLastName());
            System.out.println("Email: "+user.getEmail());
            System.out.println("User Type: "+user.getUserType());

        } catch (InvalidUserDataException e) {
            System.out.println("Registration Failed! "+e.getMessage());
        }
        sc.close();
    }
}