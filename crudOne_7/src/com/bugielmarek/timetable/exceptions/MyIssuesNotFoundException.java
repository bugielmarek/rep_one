package com.bugielmarek.timetable.exceptions;

public class MyIssuesNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String name;

	public MyIssuesNotFoundException(String name) {
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	
}
