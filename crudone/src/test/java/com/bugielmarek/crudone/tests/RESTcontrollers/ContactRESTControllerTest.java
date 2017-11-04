package com.bugielmarek.crudone.tests.RESTcontrollers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.bugielmarek.crudone.RESTcontrollers.ContactRESTController;
import com.bugielmarek.crudone.models.Contact;
import com.bugielmarek.crudone.services.ContactService;
import com.bugielmarek.crudone.tests.utils.TestUtils;

@RunWith(MockitoJUnitRunner.class)
public class ContactRESTControllerTest {

	private MockMvc mockMvc;
	
	private static final String LOCATION = "/api/contacts/contact/";
	
	@Mock
	private ContactService mockService;

	@Before
	public void setUp(){
		mockMvc = standaloneSetup(new ContactRESTController(mockService)).build();
	}
	
	@Test
	public void testAllContacts_ListNotEmpty() throws Exception{
		
		when(mockService.findListAllContacts()).thenReturn(createContactsList());
		
		mockMvc.perform(get("/api/contacts/allcontacts"))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(jsonPath("$", hasSize(2)))
		            .andExpect(jsonPath("$[0].id", is(1)))
		            .andExpect(jsonPath("$[0].name", is("nameOne")))
		            .andExpect(jsonPath("$[1].id", is(2)))
		            .andExpect(jsonPath("$[1].name", is("nameTwo")))
		            ;	
		verify(mockService, times(1)).findListAllContacts();
		verifyNoMoreInteractions(mockService);
	}
	
	@Test
	public void testAllContacts_ListEmpty() throws Exception{
		
		when(mockService.findListAllContacts()).thenReturn(Collections.emptyList());
		
		mockMvc.perform(get("/api/contacts/allcontacts"))
					.andExpect(status().isNotFound())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		            .andExpect(jsonPath("$.code", is(4)))
		            .andExpect(jsonPath("$.message", is("No contacts whatsoever found on the server")))
		            ;
		verify(mockService, times(1)).findListAllContacts();
		verifyNoMoreInteractions(mockService);
	}
	
	@Test
	public void testContact_Found() throws Exception{
		
		Contact contact = Contact.builder()
				.id(1L)
				.name("someName")
				.build();
		
		Long id = 1L;
		when(mockService.findOne(id)).thenReturn(contact);
		
		mockMvc.perform(get("/api/contacts/contact/{id}", id))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		            .andExpect(jsonPath("$.id", is(1)))
		            .andExpect(jsonPath("$.name", is(contact.getName())))
		            ;
		verify(mockService, times(1)).findOne(id);
		verifyNoMoreInteractions(mockService);
	}
	
	@Test
	public void testContact_NotFound() throws Exception{
		
		Long id = 1L;
		when(mockService.findOne(id)).thenReturn(null);
		
		mockMvc.perform(get("/api/contacts/contact/{id}", id))
					.andExpect(status().isNotFound())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		            .andExpect(jsonPath("$.code", is(4)))
		            .andExpect(jsonPath("$.message", is("Contact id: " + id + " not found.")))
		            ;
		verify(mockService, times(1)).findOne(id);
		verifyNoMoreInteractions(mockService);
	}
	
	@Test
	public void testDeleteContact_Found() throws Exception{

		Contact contact = Contact.builder()
				.id(1L)
				.name("someName")
				.build();
		when(mockService.findOne(contact.getId())).thenReturn(contact);
		doNothing().when(mockService).delete(contact.getId());
		
		mockMvc.perform(delete("/api/contacts/contact/{id}", contact.getId()))
					.andExpect(status().isNoContent())
		            ;
		verify(mockService, times(1)).findOne(contact.getId());
		verify(mockService, times(1)).delete(contact.getId());
		verifyNoMoreInteractions(mockService);
	}
	
	@Test
	public void testDeleteContact_NotFound() throws Exception{
		
		Long id = 1L;
		when(mockService.findOne(id)).thenReturn(null);
		
		mockMvc.perform(delete("/api/contacts/contact/{id}", id))
					.andExpect(status().isNotFound())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		            .andExpect(jsonPath("$.code", is(4)))
		            .andExpect(jsonPath("$.message", is("Contact id: " + id + " not found.")))
		            ;
		verify(mockService, times(1)).findOne(id);
		verifyNoMoreInteractions(mockService);
	}
	
	@Test
	public void testFindContacts_ListNotEmpty() throws Exception{
		
		List<Contact> list = createContactsList();
		String name = "someString";
		
		when(mockService.findListFromSearchInput(name)).thenReturn(list);
		
		mockMvc.perform(get("/api/contacts/find/{name}", name))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(jsonPath("$", hasSize(2)))
					.andExpect(jsonPath("$[0].id", is(1)))
					.andExpect(jsonPath("$[0].name", is("nameOne")))
					.andExpect(jsonPath("$[1].id", is(2)))
					.andExpect(jsonPath("$[1].name", is("nameTwo")))
					;
		
		verify(mockService, times(1)).findListFromSearchInput(name);
		verifyNoMoreInteractions(mockService);
	}
	
	@Test
	public void testFindContacts_ListEmpty() throws Exception{
		
		String name = "someString";
		
		when(mockService.findListFromSearchInput(name)).thenReturn(Collections.emptyList());
		
		mockMvc.perform(get("/api/contacts/find/{name}", name))
					.andExpect(status().isNotFound())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(jsonPath("$.code", is(4)))
					.andExpect(jsonPath("$.message", is("No contacts matching: " + name + " found")))
					;
		verify(mockService, times(1)).findListFromSearchInput(name);
		verifyNoMoreInteractions(mockService);
	}
	
	@Test
	public void testCreateContact() throws Exception{
		
		Contact passed = Contact.builder()
				.name("someName")
				.build();
		Contact saved = Contact.builder()
				.id(1L)
				.name("someName")
				.build();
		
		when(mockService.save(passed)).thenReturn(saved);
		
		mockMvc.perform(post("/api/contacts/create")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(TestUtils.asJsonString(passed)))
					.andExpect(status().isCreated())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(jsonPath("$.name", is("someName")))
					.andExpect(jsonPath("$.id", is(1)))
					.andExpect(header().string("location", containsString(LOCATION + saved.getId().toString())))
					;
		verify(mockService, times(1)).save(passed);
		verifyNoMoreInteractions(mockService);
	}
	
	@Test
	public void testUpdateContact_Found() throws Exception{
		
		Contact passed = Contact.builder()
				.id(1L)
				.name("someChangedName")
				.build();
		Contact retrived = Contact.builder()
				.id(1L)
				.name("someName")
				.build();
		Contact saved = Contact.builder()
				.id(1L)
				.name("someChangedName")
				.build();
		
		when(mockService.findOne(passed.getId())).thenReturn(retrived);
		when(mockService.save(passed)).thenReturn(saved);
		
		mockMvc.perform(put("/api/contacts/update/{id}", passed.getId())
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(TestUtils.asJsonString(passed)))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(jsonPath("$.id", is(1)))
					.andExpect(jsonPath("$.name", is("someChangedName")))
					.andExpect(header().string("location", containsString(LOCATION + saved.getId().toString())))
					;
		verify(mockService, times(1)).findOne(passed.getId());
		verify(mockService, times(1)).save(passed);
		verifyNoMoreInteractions(mockService);
	}

	@Test
	public void testUpdateContact_NotFound() throws Exception{
		
		Contact passed = Contact.builder()
				.id(1L)
				.name("someChangedName")
				.build();
		when(mockService.findOne(passed.getId())).thenReturn(null);
		
		mockMvc.perform(put("/api/contacts/update/{id}", passed.getId())
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(TestUtils.asJsonString(passed)))
					.andExpect(status().isNotFound())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(jsonPath("$.code", is(4)))
					.andExpect(jsonPath("$.message", is("Contact id: " + passed.getId() + " not found.")))
					;
		verify(mockService, times(1)).findOne(passed.getId());
		verifyNoMoreInteractions(mockService);
	}

	private List<Contact> createContactsList() {

		List<Contact> list = Arrays.asList(
				Contact.builder()
				.id(1L)
				.name("nameOne")
				.build(),
				Contact.builder()
				.id(2L)
				.name("nameTwo")
				.build());
		return list;
	}
}
