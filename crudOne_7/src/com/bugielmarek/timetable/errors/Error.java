package com.bugielmarek.timetable.errors;

public class Error {

	private int code;
	private String message;

	public Error(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public int getCode(){
		return code;
	}
}
