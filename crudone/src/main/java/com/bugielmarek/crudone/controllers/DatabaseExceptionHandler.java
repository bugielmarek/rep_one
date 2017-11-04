package com.bugielmarek.crudone.controllers;

import org.springframework.dao.DataAccessException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class DatabaseExceptionHandler {

	@ExceptionHandler(DataAccessException.class)
	public String exceptionHandler(DataAccessException ex, Model model) {
		DatabaseExceptionHandler.log.error("DataAccessException thrown, {}", ex.toString());
		model.addAttribute("error", ex.toString());
		return "error";
	}
}
