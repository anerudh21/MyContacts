package com.user.exceptions;



//Custom Checked Exception
public class InvalidUserDataException extends Exception {
	public InvalidUserDataException(String msg) {
		super(msg);
	}
}