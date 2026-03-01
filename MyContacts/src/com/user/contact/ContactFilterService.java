package com.user.contact;

import java.util.*;

public class ContactFilterService {

    // 1. Filter by Tag (Loop with condition)
    public List<Contact> filterByTag(List<Contact> contacts, String tag) {
        List<Contact> filtered = new ArrayList<>();
        String searchTag = tag.toLowerCase().trim();
        
        for (Contact c : contacts) {
            if (c.getTags().contains(searchTag)) {
                filtered.add(c);
            }
        }
        return filtered;
    }

    // 2. Sort by Date Added (Newest First)
    public List<Contact> sortByDateAdded(List<Contact> contacts) {
        List<Contact> sortedList = new ArrayList<>(contacts);
        
        // Using Comparator to compare LocalDateTime
        Collections.sort(sortedList, new Comparator<Contact>() {
            @Override
            public int compare(Contact c1, Contact c2) {
                return c2.getCreatedAt().compareTo(c1.getCreatedAt());
            }
        });
        
        return sortedList;
    }

    // 3. Sort by Frequently Contacted (Highest Count First)
    public List<Contact> sortByFrequency(List<Contact> contacts) {
        List<Contact> sortedList = new ArrayList<>(contacts);
        
        Collections.sort(sortedList, new Comparator<Contact>() {
            @Override
            public int compare(Contact c1, Contact c2) {
                return Integer.compare(c2.getRequestCount(), c1.getRequestCount());
            }
        });
        
        return sortedList;
    }
}