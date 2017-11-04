package com.bugielmarek.crudone.tests.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.bugielmarek.crudone.controllers.HomeController;

public class HomeControllerTest {

	@Test
	public void testHomeController() throws Exception {
		HomeController controller = new HomeController();
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		mockMvc.perform(get("/"))
		.andExpect(status().isOk())
		.andExpect(view().name("home"));
	}
}
