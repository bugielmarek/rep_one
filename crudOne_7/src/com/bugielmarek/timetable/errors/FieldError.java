package com.bugielmarek.timetable.errors;

public class FieldError {

	private String path;
	private String message;
	
	public FieldError(String path, String message) {
		this.path = path;
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public String getMessage() {
		return message;
	}
	
}
