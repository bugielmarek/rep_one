package com.bugielmarek.crudone.tests.controllers;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasProperty;
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

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceView;

import com.bugielmarek.crudone.controllers.ContactController;
import com.bugielmarek.crudone.models.Contact;
import com.bugielmarek.crudone.models.FormClass;
import com.bugielmarek.crudone.services.ContactService;

@RunWith(MockitoJUnitRunner.class)
public class ContactControllerTest {

	private MockMvc mockMvc;
	private ContactController controller;

	@Mock
	ContactService mockService;

	private static final String CONTACT = "contact";
	private static final String CONTACTS = "contacts";
	private static final String PAGE = "page";
	private static final String CREATE_CONTACT = "createcontact";

	@Before
	public void setUp() {
		controller = new ContactController(mockService);
	}

	@Test
	public void testContactsPage_DefaultPage() throws Exception {
		
		mockMvc = standaloneSetup(controller)
				.setSingleView(new InternalResourceView(""))
				.build();
		Page<Contact> page = createContactsPage();
		when(mockService.getPage(1)).thenReturn(page);

		mockMvc.perform(get("/contacts"))
				.andExpect(status().isOk())
				.andExpect(view().name(CONTACTS))
				.andExpect(model().attributeExists(PAGE))
				.andExpect(model().attribute(PAGE, page))
				.andExpect(model().attribute(PAGE, hasItems(page.getContent().toArray())))
				.andExpect(model().attribute(PAGE,
						hasItem(allOf(hasProperty("id", is(1L)), hasProperty("name", is("nameOne"))))))
				.andExpect(model().attribute(PAGE,
						hasItem(allOf(hasProperty("id", is(2L)), hasProperty("name", is("nameTwo"))))))
				;
		verify(mockService, times(1)).getPage(1);
		verifyNoMoreInteractions(mockService);
		assertEquals("List size is 2", 2, page.getContent().size());
	}

	@Test
	public void testContactsPageWhe_PageNumberIsNotDefault() throws Exception {
		mockMvc = standaloneSetup(controller).setSingleView(new InternalResourceView("")).build();

		Page<Contact> page = createContactsPage();
		when(mockService.getPage(2)).thenReturn(page);

		mockMvc.perform(get("/contacts?p=2"))
				.andExpect(status().isOk())
				.andExpect(view().name(CONTACTS))
				.andExpect(model().attributeExists(PAGE))
				.andExpect(model().attribute(PAGE, page))
				.andExpect(model().attribute(PAGE, hasItems(page.getContent().toArray())))
				.andExpect(model().attribute(PAGE,
						hasItem(allOf(hasProperty("id", is(1L)), hasProperty("name", is("nameOne"))))))
				.andExpect(model().attribute(PAGE,
						hasItem(allOf(hasProperty("id", is(2L)), hasProperty("name", is("nameTwo"))))))
				;
		verify(mockService, times(1)).getPage(2);
		verifyNoMoreInteractions(mockService);
		assertEquals("List size is 2", 2, page.getContent().size());
	}

	@Test
	public void testCreateContact_GET() throws Exception {
		mockMvc = standaloneSetup(controller).setSingleView(new InternalResourceView("")).build();

		mockMvc.perform(get("/contacts/createcontact"))
				.andExpect(status().isOk())
				.andExpect(view().name(CREATE_CONTACT))
				.andExpect(model().attributeExists(CONTACT))
				.andExpect(model().attribute(CONTACT, hasProperty("id", nullValue())))
				.andExpect(model().attribute(CONTACT, hasProperty("name", nullValue())))
				.andExpect(model().attribute(CONTACT, hasProperty("firstName", nullValue())))
				.andExpect(model().attribute(CONTACT, hasProperty("lastName", nullValue())))
				.andExpect(model().attribute(CONTACT, hasProperty("email", nullValue())))
				.andExpect(model().attribute(CONTACT, hasProperty("phoneNumber", nullValue())))
				.andExpect(model().attribute(CONTACT, hasProperty("text", nullValue())))
				;
		verifyZeroInteractions(mockService);
	}

	@Test
	public void testCreateContact_POST() throws Exception {
		mockMvc = standaloneSetup(controller).build();
		
		Contact unsaved = Contact.builder()
				.name("someName")
				.build();
		Contact saved = Contact.builder()
				.id(1L)
				.name("someName")
				.build();
		when(mockService.save(unsaved)).thenReturn(saved);

		mockMvc.perform(post("/contacts/createcontact")
				.param("name", "someName"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/contacts/contact/" + saved.getId()))
				.andExpect(model().attributeExists("id"))
				.andExpect(flash().attributeExists(CONTACT))
				.andExpect(flash().attribute(CONTACT, saved))
				.andExpect(flash().attributeCount(1))
				;
		verify(mockService, times(1)).save(unsaved);
		verifyNoMoreInteractions(mockService);
	}
	
	@Test
	public void testContact() throws Exception {
		mockMvc = standaloneSetup(controller).build();

		Contact contact = Contact.builder()
				.id(1L)
				.name("someName")
				.build();
		when(mockService.findOne(1L)).thenReturn(contact);

		mockMvc.perform(get("/contacts/contact/1"))
				.andExpect(status().isOk())
				.andExpect(view().name(CONTACT))
				.andExpect(model().attributeExists(CONTACT))
				.andExpect(model().attribute(CONTACT, contact))
				.andExpect(model().attribute(CONTACT, hasProperty("id", is(1L))))
				.andExpect(model().attribute(CONTACT, hasProperty("name", is(contact.getName()))))
				;
		verify(mockService, times(1)).findOne(1L);
		verifyNoMoreInteractions(mockService);
	}

	@Test
	public void testEditContact() throws Exception {
		mockMvc = standaloneSetup(controller).build();

		Contact contact = Contact.builder()
				.id(1L)
				.name("someName")
				.build();
		when(mockService.findOne(1L)).thenReturn(contact);

		mockMvc.perform(get("/contacts/editcontact/1"))
				.andExpect(status().isOk())
				.andExpect(view().name(CREATE_CONTACT))
				.andExpect(model().attributeExists(CONTACT))
				.andExpect(model().attribute(CONTACT, contact))
				.andExpect(model().attribute(CONTACT, hasProperty("id", is(1L))))
				.andExpect(model().attribute(CONTACT, hasProperty("name", is(contact.getName()))))
				;
		verify(mockService, times(1)).findOne(1L);
		verifyNoMoreInteractions(mockService);
	}

	@Test
	public void testDeleteContact() throws Exception {
		mockMvc = standaloneSetup(controller).build();

		doNothing().when(mockService).delete(1L);

		mockMvc.perform(get("/contacts/deletecontact/1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/contacts"))
				;
		verify(mockService, times(1)).delete(1L);
		verifyNoMoreInteractions(mockService);
	}

	@Test
	public void testFindContact_MatchingResults_DefaultPage() throws Exception {
		mockMvc = standaloneSetup(controller).build();
		Contact formContact = Contact.builder()
				.name("nameMatchingResultsInDB")
				.build();
		FormClass formClass = new FormClass(formContact);
		Page<Contact> page = createContactsPage();

		when(mockService.getPageResultFromSearchInput(formClass, 1)).thenReturn(page);

		mockMvc.perform(post("/contacts/findcontact")
				.param("contact.name", formContact.getName()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/contacts"))
				.andExpect(flash().attributeExists(PAGE))
				.andExpect(flash().attribute(PAGE, page))
				.andExpect(flash().attribute(PAGE, hasItems(page.getContent().toArray())))
				.andExpect(flash().attributeCount(1))
				;
		verify(mockService, times(1)).getPageResultFromSearchInput(formClass, 1);
		verifyNoMoreInteractions(mockService);
	}

	@Test
	public void testFindContact_MatchingResults_PageIsNotDefault() throws Exception {
		mockMvc = standaloneSetup(controller).build();
		Contact formContact = Contact.builder()
				.name("nameMatchingResultsInDB")
				.build();
		FormClass formClass = new FormClass(formContact);

		Page<Contact> page = createContactsPage();

		when(mockService.getPageResultFromSearchInput(formClass, 2)).thenReturn(page);

		mockMvc.perform(post("/contacts/findcontact?p=2")
				.param("contact.name", formContact.getName()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/contacts"))
				.andExpect(flash().attributeExists(PAGE))
				.andExpect(flash().attribute(PAGE, page))
				.andExpect(flash().attribute(PAGE, hasItems(page.getContent().toArray())))
				.andExpect(flash().attributeCount(1))
				;
		verify(mockService, times(1)).getPageResultFromSearchInput(formClass, 2);
		verifyNoMoreInteractions(mockService);
	}

	private Page<Contact> createContactsPage() {
		List<Contact> list = Arrays.asList(
				Contact.builder()
				.id(1L)
				.name("nameOne")
				.build(),
				Contact.builder()
				.id(2L)
				.name("nameTwo")
				.build());
		Page<Contact> page = new PageImpl<Contact>(list);
		return page;
	}
}
