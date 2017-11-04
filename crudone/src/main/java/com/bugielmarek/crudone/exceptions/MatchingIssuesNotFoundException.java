package com.bugielmarek.crudone.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MatchingIssuesNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String name;
}
