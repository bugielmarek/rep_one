package com.bugielmarek.crudone.tests.controllers;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.View;

import com.bugielmarek.crudone.controllers.UserController;
import com.bugielmarek.crudone.models.User;
import com.bugielmarek.crudone.services.UserService;
import com.bugielmarek.crudone.tests.utils.TestUtils;

public class UserControllerTest {

	@Mock
	UserService mockService;
	
	@Mock
	View view;

	private UserController controller;
	private MockMvc mockMvc;

	private static final String USERS = "users";
	private static final String USER = "user";
	private static final String PAGE = "page";
	private static final String CREATE_USER = "createuser";

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		controller = new UserController(mockService);
	}

	@Test
	public void testAllUsers() throws Exception {
		mockMvc = standaloneSetup(controller)
				.setSingleView(view)
				.build();
		Page<User> page = createUserPage(10);
		when(mockService.getPage(1)).thenReturn(page);
		
		mockMvc.perform(get("/users"))
				.andExpect(status().isOk())
				.andExpect(view().name(USERS))
				.andExpect(model().attributeExists(PAGE))
				.andExpect(model().attribute(PAGE, hasItems(page.getContent().toArray())))
				;
		assertEquals("List size is 10", 10, page.getContent().size());
		verify(mockService, times(1)).getPage(1);
		verifyNoMoreInteractions(mockService);
	}

	@Test
	public void testCreateUser_GET() throws Exception {
		mockMvc = standaloneSetup(controller)
				.setSingleView(view)
				.build();
		
		mockMvc.perform(get("/users/createuser"))
				.andExpect(status().isOk())
				.andExpect(view().name(CREATE_USER))
				.andExpect(model().attributeExists(USER))
				.andExpect(model().attribute(USER, hasProperty("id", nullValue())))
				.andExpect(model().attribute(USER, hasProperty("username", nullValue())))
				.andExpect(model().attribute(USER, hasProperty("password", nullValue())))
				;
		verifyZeroInteractions(mockService);
	}

	@Test
	public void testCreateUser_POST__ValidUserPassed_NonExistingInDB() throws Exception {
		
		mockMvc = standaloneSetup(controller)
				.build();
		User unsaved =  User.builder()
				.username("nameee")
				.password("somePassword")
				.formPassword("somePassword")
				.build();
		User saved = User.builder()
			.id(1L)
			.username("namee")
			.password("someEncryptedPassword")
			.build();
		when(mockService.doesUsernameExists(unsaved)).thenReturn(false);
		when(mockService.save(unsaved)).thenReturn(saved);

		mockMvc.perform(post("/users/createuser")
				.param("username", unsaved.getUsername())
				.param("password", unsaved.getPassword())
				.param("formPassword", unsaved.getFormPassword()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/users/user/1"))
				.andExpect(model().attributeExists("id"))
				.andExpect(model().attribute("id", saved.getId().toString()))
				.andExpect(flash().attributeExists(USER))
				.andExpect(flash().attribute(USER, hasProperty("id", is(saved.getId()))))
				.andExpect(flash().attribute(USER, hasProperty("username", is(saved.getUsername()))))
				.andExpect(flash().attribute(USER, hasProperty("password", is(saved.getPassword()))))
				;
		verify(mockService, times(1)).doesUsernameExists(unsaved);
		verify(mockService, times(1)).save(unsaved);
		verifyNoMoreInteractions(mockService);
	}

	@Test
	public void testCreateUserPOST_ExistingInDB() throws Exception {
		
		mockMvc = standaloneSetup(controller)
				.setSingleView(view)
				.build();
		User unsaved = User.builder()
				.username("nameee")
				.password("somepassword")
				.formPassword("somepassword")
				.build();
		when(mockService.doesUsernameExists(unsaved)).thenReturn(true);

		mockMvc.perform(
				post("/users/createuser")
				.param("username", unsaved.getUsername())
				.param("password", unsaved.getPassword())
				.param("formPassword", unsaved.getFormPassword()))
				.andExpect(status().isOk())
				.andExpect(view().name(CREATE_USER))
				.andExpect(model().attributeExists(USER))
				.andExpect(model().attribute(USER, hasProperty("id", is(nullValue()))))
				.andExpect(model().attributeHasFieldErrors(USER, "username"))
				.andExpect(model().errorCount(1))
				;
		verify(mockService, times(1)).doesUsernameExists(unsaved);
		verifyNoMoreInteractions(mockService);
	}

	@Test
	public void testCreateUserPOST_FieldErrors_Username_FormPassword() throws Exception {
		
		mockMvc = standaloneSetup(controller)
				.setSingleView(view)
				.build();
		User unsaved = User.builder()
				.username(TestUtils.createStringWithLength(7))
				.password("validPassword")
				.formPassword(TestUtils.createStringWithLength(16) + " ")
				.build();
		
		mockMvc.perform(post("/users/createuser")
				.param("username", unsaved.getUsername())
				.param("password", unsaved.getPassword())
				.param("formPassword", unsaved.getFormPassword()))
				.andExpect(status().isOk())
				.andExpect(view().name(CREATE_USER))
				.andExpect(model().attributeExists(USER))
				.andExpect(model().attributeHasFieldErrors(USER, "username"))
				.andExpect(model().attributeHasFieldErrors(USER, "formPassword"))
				.andExpect(model().errorCount(3))
				// violating both @Size and @Pattern constraints for 'formPassword' thus 3 errors
				;
		verifyZeroInteractions(mockService);
	}
	
	@Test
	public void testUser() throws Exception {
		
		mockMvc = standaloneSetup(controller)
				.build();
		User retrived = User.builder()
				.id(1L)
				.username("someName")
				.password("someEncryptedPassword")
				.enabled(true)
				.authority("ROLE_USER")
				.build();
		when(mockService.findOne(retrived.getId())).thenReturn(retrived);

		mockMvc.perform(get("/users/user/{id}", retrived.getId()))
				.andExpect(status().isOk())
				.andExpect(view().name(USER))
				.andExpect(model().attributeExists(USER))
				.andExpect(model().attribute(USER, retrived))
				.andExpect(model().attribute(USER, hasProperty("id", is(retrived.getId()))))
				.andExpect(model().attribute(USER, hasProperty("username", is(retrived.getUsername()))))
				.andExpect(model().attribute(USER, hasProperty("password", is(retrived.getPassword()))))
				.andExpect(model().attribute(USER, hasProperty("enabled", is(retrived.isEnabled()))))
				.andExpect(model().attribute(USER, hasProperty("authority", is(retrived.getAuthority()))))
				;
		verify(mockService, times(1)).findOne(retrived.getId());
		verifyNoMoreInteractions(mockService);
	}

	@Test
	public void testEditUser() throws Exception {
		mockMvc = standaloneSetup(controller)
				.build();
		User retrived = User.builder()
				.id(1L)
				.username("someName")
				.password("someEncryptedPassword")
				.enabled(true)
				.authority("ROLE_USER")
				.build();
		when(mockService.findOne(retrived.getId())).thenReturn(retrived);

		mockMvc.perform(get("/users/edituser/{id}", retrived.getId()))
				.andExpect(status().isOk())
				.andExpect(view().name(CREATE_USER))
				.andExpect(model().attributeExists(USER))
				.andExpect(model().attribute(USER, retrived))
				.andExpect(model().attribute(USER, hasProperty("id", is(retrived.getId()))))
				.andExpect(model().attribute(USER, hasProperty("username", is(retrived.getUsername()))))
				.andExpect(model().attribute(USER, hasProperty("password", is(retrived.getPassword()))))
				.andExpect(model().attribute(USER, hasProperty("enabled", is(retrived.isEnabled()))))
				.andExpect(model().attribute(USER, hasProperty("authority", is(retrived.getAuthority()))))
				;
		verify(mockService, times(1)).findOne(retrived.getId());
		verifyNoMoreInteractions(mockService);
	}

	@Test
	public void testDeleteUser() throws Exception {
		
		mockMvc = standaloneSetup(controller)
				.build();
		doNothing().when(mockService).delete(1L);
		
		mockMvc.perform(get("/users/deleteuser/{id}", 1L))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/users"))
				;
		verify(mockService, times(1)).delete(1L);
		verifyNoMoreInteractions(mockService);
	}


	private Page<User> createUserPage(int count) {
		List<User> userList = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			userList.add(User.builder().id(1L + i).username("name" + (1 + i)).build());
		}
		Page<User> userPage = new PageImpl<>(userList);
		return userPage;
	}
}
