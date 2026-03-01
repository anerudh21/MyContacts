package com.main;

/*
 * MyContactsApp - Integrated Module
* FEATURES OVERVIEW:
 *  Authentication     : Secure Login/Registration & Session Hashing
 *  Contact Engine      : Full CRUD for Persons and Organizations
 *  Search Engine (UC09): Multi-parameter Predicate Filtering
 *  Analytics (UC10)    : Frequency tracking and Temporal Sorting
 *  Tagging (UC11/12)   : Set-based unique tagging & relationship mapping
 *  
 *  @author Developer
 *  @version 12.0
 */

import java.util.*;
import java.util.function.Predicate;

import com.user.auth.Authentication;
import com.user.auth.BasicAuth;
import com.user.encryption.PasswordHashing;
import com.user.model.FreeUser;
import com.user.model.PremiumUser;
import com.user.model.User;
import com.user.repository.UserRepository;
import com.user.session.SessionManager;
import com.user.validation.Validator;
import com.user.contact.*;
import com.user.tag.Tag;

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
                
                String input = sc.nextLine();
                if (input.isBlank()) continue;
                int choice = Integer.parseInt(input);

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

                        User user = typeInput.equals("premium") ? 
                                    new PremiumUser(email, hashedPassword, firstName, lastName) : 
                                    new FreeUser(email, hashedPassword, firstName, lastName);

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
                            System.out.println("Login Successful! Welcome " + session.getCurrentUser().getFirstName());
                        } else {
                            System.out.println("Invalid credentials.");
                        }
                    } else if (choice == 3) {
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else {
                // LOGGED IN MENU
                System.out.println("\n--- MyContacts Menu ---");
                System.out.println("1. Update Name          2. Change Password      3. Change Preferences");
                System.out.println("4. Add Contact          5. View Contact Details 6. Edit Contact");
                System.out.println("7. Delete Contact       8. Bulk Operations      9. Search Contacts");
                System.out.println("10. Basic Filtering     11. Manage Tags         12. Logout");
                System.out.print("Choose option: ");
                
                String input = sc.nextLine();
                if (input.isBlank()) continue;
                int choice = Integer.parseInt(input);

                try {
                    ContactRepository repo = session.getCurrentUser().getContactRepository();

                    if (choice == 1) {
                        System.out.print("New first name: ");
                        String fn = sc.nextLine();
                        System.out.print("New last name: ");
                        String ln = sc.nextLine();
                        session.getCurrentUser().updateName(fn, ln);
                        System.out.println("Name updated.");

                    } else if (choice == 2) {
                        System.out.print("Old password: ");
                        String oldP = sc.nextLine();
                        System.out.print("New password: ");
                        String newP = sc.nextLine();
                        session.getCurrentUser().changePassword(oldP, newP);
                        System.out.println("Password changed.");

                    } else if (choice == 3) {
                        System.out.print("Dark Mode (true/false): ");
                        boolean dm = Boolean.parseBoolean(sc.nextLine());
                        System.out.print("Email Notifications (true/false): ");
                        boolean en = Boolean.parseBoolean(sc.nextLine());
                        session.getCurrentUser().getPreferences().updatePreferences(dm, en);
                        System.out.println("Preferences saved.");

                    } else if (choice == 4) {
                        System.out.print("Type (person/organization): ");
                        String type = sc.nextLine().toLowerCase();
                        System.out.print("Name: ");
                        String name = sc.nextLine();
                        
                        Contact contact = type.equals("organization") ? new Organization(name) : new Person(name);
                        
                        System.out.print("Phone number: ");
                        contact.addPhoneNumber(new PhoneNumber(sc.nextLine()));
                        System.out.print("Email: ");
                        contact.addEmailAddress(new EmailAddress(sc.nextLine()));
                        
                        repo.save(contact);
                        System.out.println("Contact added! Use 'Manage Tags' to categorize.");

                    } else if (choice == 5) {
                        List<Contact> contacts = repo.findAll();
                        if (contacts.isEmpty()) { System.out.println("No contacts."); continue; }
                        
                        for (int i = 0; i < contacts.size(); i++) 
                            System.out.println((i+1) + ". " + contacts.get(i).getName());
                        
                        System.out.print("Select number: ");
                        int sel = Integer.parseInt(sc.nextLine()) - 1;
                        Contact c = contacts.get(sel);
                        c.incrementRequestCount(); // UC-10 Tracking
                        
                        List<String> phones = c.getPhoneNumbers().stream().map(PhoneNumber::getNumber).toList();
                        List<String> emails = c.getEmailAddresses().stream().map(EmailAddress::getEmail).toList();
                        System.out.println(new ContactView(c.getId(), c.getContactType(), c.getName(), c.getCreatedAt(), phones, emails));
                        System.out.println("Tags: " + c.getTags());

                    } else if (choice == 6) {
                        List<Contact> contacts = repo.findAll();
                        for (int i = 0; i < contacts.size(); i++) System.out.println((i+1) + ". " + contacts.get(i).getName());
                        System.out.print("Select index to edit: ");
                        int idx = Integer.parseInt(sc.nextLine()) - 1;
                        Contact existing = contacts.get(idx);
                        Contact edited = (existing instanceof Person p) ? new Person(p) : new Organization((Organization)existing);

                        System.out.println("1. Name 2. Phones 3. Emails");
                        int ec = Integer.parseInt(sc.nextLine());
                        if(ec == 1) { System.out.print("New name: "); edited.setName(sc.nextLine()); }
                        else if(ec == 2) { 
                            List<PhoneNumber> np = new ArrayList<>();
                            System.out.print("Phone: "); np.add(new PhoneNumber(sc.nextLine()));
                            edited.setPhoneNumbers(np);
                        }
                        repo.update(edited);
                        System.out.println("Updated.");

                    } else if (choice == 7) {
                        List<Contact> contacts = repo.findAll();
                        for (int i = 0; i < contacts.size(); i++) System.out.println((i+1) + ". " + contacts.get(i).getName());
                        System.out.print("Delete selection: ");
                        int sel = Integer.parseInt(sc.nextLine()) - 1;
                        repo.delete(contacts.get(sel).getId());
                        System.out.println("Deleted.");

                    } else if (choice == 8) {
                        List<Contact> contacts = repo.findAll();
                        System.out.println("1. Multi-Delete 2. Clear Persons 3. Clear Orgs");
                        int bc = Integer.parseInt(sc.nextLine());
                        if(bc == 1) {
                            System.out.print("Indices (e.g. 1,2): ");
                            String[] ids = sc.nextLine().split(",");
                            List<UUID> toDel = Arrays.stream(ids).map(s -> contacts.get(Integer.parseInt(s.trim())-1).getId()).toList();
                            repo.deleteAll(toDel);
                        } else if(bc == 2) {
                            repo.deleteAll(repo.filter(c -> c.getContactType().equals("Person")).stream().map(Contact::getId).toList());
                        }
                        System.out.println("Bulk operation complete.");

                    } else if (choice == 9) {
                        ContactSearchService ss = new ContactSearchService();
                        System.out.println("1. Name 2. Phone 3. Email (Regex) 4. Tag");
                        int st = Integer.parseInt(sc.nextLine());
                        System.out.print("Term: ");
                        String term = sc.nextLine();
                        Predicate<Contact> p = switch(st) {
                            case 1 -> ss.getNamePredicate(term);
                            case 2 -> ss.getPhonePredicate(term);
                            case 3 -> ss.getEmailRegexPredicate(term);
                            case 4 -> ss.getTagPredicate(term);
                            default -> (Contact c) -> false;
                        };
                        List<Contact> results = repo.filter(p);
                        if (results.isEmpty()) System.out.println("No results found.");
                        else results.forEach(c -> System.out.println("- " + c.getName() + " " + c.getTags()));

                    } else if (choice == 10) {
                        ContactFilterService fs = new ContactFilterService();
                        List<Contact> all = repo.findAll();
                        System.out.println("1. By Tag 2. By Date 3. By Frequency");
                        int fc = Integer.parseInt(sc.nextLine());
                        List<Contact> res = switch(fc) {
                            case 1 -> { System.out.print("Tag: "); yield fs.filterByTag(all, sc.nextLine()); }
                            case 2 -> fs.sortByDateAdded(all);
                            case 3 -> fs.sortByFrequency(all);
                            default -> all;
                        };
                        res.forEach(c -> System.out.println(c.getName() + " [Visited: " + c.getRequestCount() + "]"));

                    } else if (choice == 11) {
                        // --- UC-12: APPLY TAGS TO CONTACTS ---
                        List<Contact> contacts = repo.findAll();
                        if (contacts.isEmpty()) { System.out.println("Add contacts first."); continue; }
                        
                        for (int i = 0; i < contacts.size(); i++) 
                            System.out.println((i+1) + ". " + contacts.get(i).getName() + " (Tags: " + contacts.get(i).getTags() + ")");
                        
                        System.out.print("Select contact number: ");
                        Contact c = contacts.get(Integer.parseInt(sc.nextLine()) - 1);
                        
                        System.out.println("\n1. Add Multiple Tags (comma separated)");
                        System.out.println("2. Remove a Tag");
                        System.out.println("3. Clear All Tags");
                        System.out.print("Action: ");
                        int tagAction = Integer.parseInt(sc.nextLine());

                        switch (tagAction) {
                            case 1 -> {
                                System.out.print("Enter tags (e.g. Work, Family, Urgent): ");
                                String inputTags = sc.nextLine();
                                String[] tagArray = inputTags.split(",");
                                for (String t : tagArray) {
                                    c.addTag(new Tag(t.trim()));
                                }
                                System.out.println("Tags applied successfully!");
                            }
                            case 2 -> {
                                System.out.print("Tag name to remove: ");
                                c.removeTag(sc.nextLine());
                                System.out.println("Tag removed.");
                            }
                            case 3 -> {
                                c.clearAllTags();
                                System.out.println("Tags cleared.");
                            }
                            default -> System.out.println("Invalid selection.");
                        }

                    } else if (choice == 12) {
                        session.logout();
                        System.out.println("Logged out successfully.");
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }
}