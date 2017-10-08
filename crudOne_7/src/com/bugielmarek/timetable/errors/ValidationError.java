package com.bugielmarek.timetable.errors;

import java.util.ArrayList;
import java.util.List;

public class ValidationError {

	private List<FieldError> fieldErrors = new ArrayList<>();
	
	public void addErrors(String path, String message){
		fieldErrors.add(new FieldError(path, message));
	}

	public List<FieldError> getFieldErrors() {
		return fieldErrors;
	}
}
