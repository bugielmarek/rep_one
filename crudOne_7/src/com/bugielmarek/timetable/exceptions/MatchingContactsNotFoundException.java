package com.bugielmarek.timetable.exceptions;

public class MatchingContactsNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String name;

	public MatchingContactsNotFoundException(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
}
