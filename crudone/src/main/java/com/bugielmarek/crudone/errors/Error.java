package com.bugielmarek.crudone.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Error {

	private int code;
	private String message;
}
