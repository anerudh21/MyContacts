package com.user.model;

/**
 * FreeUser
 * Represents a standard user with limited access in the application.
 * @author Developer
 * @version 1.0
 */
//Child class of User -> Free UserType
public class FreeUser extends User {
	
	public FreeUser(String email, String password,String firstName,String lastName) {
		super(email,password,firstName,lastName);
	}
	
	@Override
 public UserType getUserType() {
     return UserType.free;
 }
}