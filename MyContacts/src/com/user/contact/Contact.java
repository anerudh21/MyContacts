package com.user.contact;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

// contact class for blueprints of a contact
public abstract class Contact {

    private final UUID id;
    private final LocalDateTime createdAt;
    private String name;
    private final List<PhoneNumber> phoneNumbers;
    private final List<EmailAddress> emailAddresses;

    public Contact(String name) {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.name = name;
        this.phoneNumbers = new ArrayList<>();
        this.emailAddresses = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public List<EmailAddress> getEmailAddresses() {
        return emailAddresses;
    }

    public void addPhoneNumber(PhoneNumber phoneNumber) {
        phoneNumbers.add(phoneNumber);
    }

    public void addEmailAddress(EmailAddress emailAddress) {
        emailAddresses.add(emailAddress);
    }

    public abstract String getContactType();

    @Override
    public String toString() {
        return """
                ==============================
                Contact Details
                ==============================
                Type: %s
                Name: %s
                Phone Numbers: %s
                Email Addresses: %s
                Created At: %s
                ==============================
                """.formatted(
                getContactType(),
                name,
                phoneNumbers,
                emailAddresses,
                createdAt
        );
    }
}