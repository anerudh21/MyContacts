package com.user.contact;

public class Person extends Contact {

    public Person(String name) {
        super(name);
    }
    
    public Person(Person other) {
    	super(other);
    }

    @Override
    public String getContactType() {
        return "Person";
    }
}