package com.user.contact;

public class Person extends Contact {

    public Person(String name) {
        super(name);
    }

    @Override
    public String getContactType() {
        return "Person";
    }
}