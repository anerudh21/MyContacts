package com.user.contact;

import java.util.ArrayList;
import java.util.List;

public class ContactRepository {

    private final List<Contact> contacts = new ArrayList<>();

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public List<Contact> getAllContacts() {
        return contacts;
    }
}