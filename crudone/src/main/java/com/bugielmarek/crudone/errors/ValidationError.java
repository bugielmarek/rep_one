package com.bugielmarek.crudone.errors;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class ValidationError {

	private List<FieldError> fieldErrors = new ArrayList<>();
	
	public void addErrors(String path, String message){
		fieldErrors.add(new FieldError(path, message));
	}
}
