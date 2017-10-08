package com.bugielmarek.timetable.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.bugielmarek.timetable.RESTClientModel.Quote;
import com.bugielmarek.timetable.RESTClientModel.QuoteResponse;

@Controller
@RequestMapping(value = "/quote")
public class RESTClientController {

	@RequestMapping(method = RequestMethod.GET)
	public String showQuote(Model model) {
		
		RestTemplate rest = new RestTemplate();
		QuoteResponse quoteResponse = rest.getForObject("http://quotes.rest/qod.json", QuoteResponse.class);
		Quote quote = quoteResponse.getContents().getQuotes().get(0);
		model.addAttribute(quote);
		return "quote";
	}
}
