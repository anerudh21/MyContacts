package com.user.contact;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Contact {

    private final UUID id;
    private final LocalDateTime createdAt;

    private String name;
    private final List<PhoneNumber> phoneNumbers = new ArrayList<>();
    private final List<EmailAddress> emailAddresses = new ArrayList<>();

    protected Contact(String name) {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getName() {
        return name;
    }

    public void addPhoneNumber(PhoneNumber phone) {
        phoneNumbers.add(phone);
    }

    public void addEmailAddress(EmailAddress email) {
        emailAddresses.add(email);
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public List<EmailAddress> getEmailAddresses() {
        return emailAddresses;
    }

    public abstract String getContactType();
}