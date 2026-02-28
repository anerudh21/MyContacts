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
    
    public void delete(UUID id) {
    	if (!contactStore.containsKey(id)) {
    		throw new NoSuchElementException("Contact Not Found!");
    	}
    	contactStore.remove(id);
    }
    
    public void deleteAll(List<UUID> ids) {
    	ids.forEach(contactStore::remove);
    }
    
    public List<Contact> filter(java.util.function.Predicate<Contact> predicate) {
    	return contactStore.values().stream().filter(predicate).toList();
    }
}