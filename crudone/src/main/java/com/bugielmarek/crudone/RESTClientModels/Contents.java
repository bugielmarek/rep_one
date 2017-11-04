package com.bugielmarek.crudone.RESTClientModels;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Contents {

	private List<Quote> quotes;
}
