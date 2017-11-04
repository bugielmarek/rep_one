package com.bugielmarek.crudone.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContactNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private long id;
}
