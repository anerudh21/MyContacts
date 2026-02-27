package com.user.model;

/**
 * PremiumUser
 * Represents a paid user with extended access in the application.
 * @author Developer
 * @version 1.0
 */
//child class of User -> Premium
public class PremiumUser extends User {
	
	private final boolean premium;

 public PremiumUser(String email,String password,String firstName,String lastName) {
     super(email,password, firstName, lastName);
     this.premium = true;
 }

 public boolean isPremium() {
     return premium;
 }

 @Override
 public UserType getUserType() {
     return UserType.premium;
 }
}