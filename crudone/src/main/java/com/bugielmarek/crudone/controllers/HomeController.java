package com.bugielmarek.crudone.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bugielmarek.crudone.models.FormClass;

@Controller
public class HomeController {

	@RequestMapping("/")
	public String showHome(@ModelAttribute("formClass") FormClass formClass){
		return "home";
	}
}
