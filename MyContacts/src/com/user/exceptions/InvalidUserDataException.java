package com.user.exceptions;



//Custom Checked Exception
@SuppressWarnings("serial")
public class InvalidUserDataException extends Exception {
	public InvalidUserDataException(String msg) {
		super(msg);
	}
}