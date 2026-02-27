package com.user.contact;

public class Organization extends Contact {

    public Organization(String name) {
        super(name);
    }

    @Override
    public String getContactType() {
        return "Organization";
    }
}