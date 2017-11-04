package com.bugielmarek.crudone.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.bugielmarek.crudone.RESTClientModels.Quote;
import com.bugielmarek.crudone.RESTClientModels.QuoteResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/quote")
public class RESTClientController {

	@RequestMapping(method = RequestMethod.GET)
	public String showQuote(Model model) {
		RESTClientController.log.info("GET request for '/quote' made");
		RestTemplate rest = new RestTemplate();
		QuoteResponse quoteResponse = rest.getForObject("http://quotes.rest/qod.json", QuoteResponse.class);
		Quote quote = quoteResponse.getContents().getQuotes().get(0);
		model.addAttribute(quote);
		return "quote";
	}
}
