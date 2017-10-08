package com.bugielmarek.timetable.exceptions;

public class ContactNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private long id;

	public ContactNotFoundException(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}
	
}
