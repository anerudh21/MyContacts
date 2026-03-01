package com.user.contact;

import java.time.LocalDateTime;
import java.util.*;
import com.user.tag.Tag;

/**
 * Abstract Contact class representing the blueprint for all contact types.
 * Implements UC-10 (Frequency), UC-11 (Tag relationship), and UC-12 (Tag management).
 */
public abstract class Contact {

    private final UUID id;
    private final LocalDateTime createdAt;
    private String name;
    private final List<PhoneNumber> phoneNumbers;
    private final List<EmailAddress> emailAddresses;
    private final Set<Tag> tags; 
    private int requestCount = 0; 

    public Contact(String name) {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.name = name;
        this.phoneNumbers = new ArrayList<>();
        this.emailAddresses = new ArrayList<>();
        this.tags = new HashSet<>();
    }
    
    protected Contact(Contact other) {
        this.id = other.id;
        this.createdAt = other.createdAt;
        this.name = other.name;
        this.phoneNumbers = new ArrayList<>(other.phoneNumbers);
        this.emailAddresses = new ArrayList<>(other.emailAddresses);
        this.tags = new HashSet<>(other.tags); 
        this.requestCount = other.requestCount;
    }

    // --- ADDED METHODS FOR COMPATIBILITY WITH MAIN ---

    /**
     * Adds a single PhoneNumber object to the list.
     * Used in Choice 4 (Add Contact) of the Main App.
     */
    public void addPhoneNumber(PhoneNumber phoneNumber) {
        if (phoneNumber != null) {
            this.phoneNumbers.add(phoneNumber);
        }
    }

    /**
     * Adds a single EmailAddress object to the list.
     * Used in Choice 4 (Add Contact) of the Main App.
     */
    public void addEmailAddress(EmailAddress emailAddress) {
        if (emailAddress != null) {
            this.emailAddresses.add(emailAddress);
        }
    }

    // --- GETTERS ---
    public UUID getId() { return id; }
    public String getName() { return name; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public int getRequestCount() { return requestCount; }

    public List<PhoneNumber> getPhoneNumbers() {
        return new ArrayList<>(phoneNumbers);
    }

    public List<EmailAddress> getEmailAddresses() {
        return new ArrayList<>(emailAddresses);
    }

    public Set<Tag> getTags() {
        return new HashSet<>(tags);
    }

    // UC-10: Frequency Tracking
    public void incrementRequestCount() {
        this.requestCount++;
    }

    // UC-11 & UC-12: Tag Relationship Management
    
    public void addTag(Tag tag) {
        if (tag != null) {
            tags.add(tag);
        }
    }
    
    public void removeTag(String tagName) {
        if (tagName != null) {
            tags.remove(new Tag(tagName));
        }
    }

    public void clearAllTags() {
        this.tags.clear();
    }

    // --- SETTERS ---
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
    }
    
    public void setPhoneNumbers(List<PhoneNumber> phones) {
        this.phoneNumbers.clear();
        this.phoneNumbers.addAll(phones);
    }

    public void setEmailAddresses(List<EmailAddress> emails) {
        this.emailAddresses.clear();
        this.emailAddresses.addAll(emails);
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
                Tags: %s
                Frequency: %d
                Created At: %s
                ==============================
                """.formatted(
                getContactType(),
                name,
                phoneNumbers,
                emailAddresses,
                tags,
                requestCount,
                createdAt
        );
    }
}