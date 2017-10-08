package com.bugielmarek.timetable.tests.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceView;

import com.bugielmarek.timetable.controllers.LoginController;

public class LoginControllerTest {

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		LoginController controller = new LoginController();
		mockMvc = MockMvcBuilders.standaloneSetup(controller).setSingleView(new InternalResourceView("")).build();
	}

	@Test
	public void testLoginPage() throws Exception {
		mockMvc.perform(get("/login"))
		.andExpect(status().isOk())
		.andExpect(view().name("login"));
	}

	@Test
	public void testDeniedPage() throws Exception {
		mockMvc.perform(get("/denied"))
		.andExpect(status().isOk())
		.andExpect(view().name("denied"));
	}
}
