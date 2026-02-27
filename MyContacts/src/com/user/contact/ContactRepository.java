package com.user.contact;

import java.util.*;
public class ContactRepository {

    private final Map<UUID, Contact> contactStore = new HashMap<>();

    public void save(Contact contact) {
        contactStore.put(contact.getId(), contact);
    }

    public List<Contact> findAll() {
        return new ArrayList<>(contactStore.values());
    }

    public Optional<Contact> findById(UUID id) {
        return Optional.ofNullable(contactStore.get(id));
    }
    
    public boolean isEmpty() {
        return contactStore.isEmpty();
    }
    
    public void update(Contact contact) {
    	contactStore.put(contact.getId(), contact);
    }
}