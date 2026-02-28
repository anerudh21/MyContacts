package com.user.contact;

public class Organization extends Contact {

    public Organization(String name) {
        super(name);
    }
    
    public Organization(Organization other) {
    	super(other);
    }

    @Override
    public String getContactType() {
        return "Organization";
    }
}