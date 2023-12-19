package com.stixis.ems.exceptions;

public class DuplicateEmailException extends RuntimeException{
	public DuplicateEmailException(String message) {
		super(message);
	}
}
