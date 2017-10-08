package com.bugielmarek.timetable.RESTClientModel;

import java.util.ArrayList;
import java.util.List;

public class Contents {

	private List<Quote> quotes = new ArrayList<>(0);

	public List<Quote> getQuotes() {
		return quotes;
	}

	public void setQuotes(List<Quote> quotes) {
		this.quotes = quotes;
	}

	@Override
	public String toString() {
		return "Contents [quotes=" + quotes + "]";
	}
	
	
}
