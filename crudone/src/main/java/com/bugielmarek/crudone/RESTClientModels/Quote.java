package com.bugielmarek.crudone.RESTClientModels;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Quote {

	private String quote;
	
	private String length;
	
	private String author;
	
	private String category;
	
	private String date;
	
	private List<String> tags;
}
