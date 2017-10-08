package com.bugielmarek.timetable.exceptions;

public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String username;

	public UserNotFoundException(String username) {
		this.username = username;
	}
	public String getUsername() {
		return username;
	}

	
}
