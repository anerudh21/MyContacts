package com.user.contact;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class ContactSearchService {

    // 1. Search by Name (Case-insensitive comparison)
    public Predicate<Contact> getNamePredicate(String nameQuery) {
        return contact -> contact.getName().toLowerCase().contains(nameQuery.toLowerCase());
    }

    // 2. Search by Phone (Checks all numbers in the list)
    public Predicate<Contact> getPhonePredicate(String phoneQuery) {
        return contact -> contact.getPhoneNumbers().stream()
                .anyMatch(p -> p.getNumber().contains(phoneQuery));
    }

    // 3. Search by Email (Regex pattern matching)
    public Predicate<Contact> getEmailRegexPredicate(String regex) {
        try {
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            return contact -> contact.getEmailAddresses().stream()
                    .anyMatch(e -> pattern.matcher(e.getEmail()).find());
        } catch (Exception e) {
            // If regex is invalid, return a predicate that matches nothing
            return contact -> false;
        }
    }

    // 4. Search by Tags
    public Predicate<Contact> getTagPredicate(String tagQuery) {
        return contact -> contact.getTags().contains(tagQuery.toLowerCase().trim());
    }
}