package com.bugielmarek.crudone.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FieldError {

	private String path;
	private String message;
}
