package com.bugielmarek.crudone.RESTClientModels;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QuoteResponse {

	private Success success;
	
	private Contents contents;
}
