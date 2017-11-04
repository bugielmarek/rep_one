package com.bugielmarek.crudone.tests.services;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.bugielmarek.crudone.models.Contact;
import com.bugielmarek.crudone.models.FormClass;
import com.bugielmarek.crudone.services.ContactService;

@ActiveProfiles("embeddedH2")
@ContextConfiguration(locations = { "classpath:testsContext.xml" })
@RunWith(SpringRunner.class)
public class ContactServiceTestH2Embedded {

	private JdbcTemplate jdbc;

	@Autowired
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return this.jdbc = new JdbcTemplate(dataSource);
	}

	private static final String CREATE = "createContactsTable.sql";
	private static final String DROP = "dropContactsTable.sql";

	@Before
	public void before() throws ScriptException, SQLException {
		ScriptUtils.executeSqlScript(jdbc.getDataSource().getConnection(), new ClassPathResource(CREATE));
	}

	@After
	public void after() throws ScriptException, SQLException {
		ScriptUtils.executeSqlScript(jdbc.getDataSource().getConnection(), new ClassPathResource(DROP));
	}

	@Autowired
	ContactService contactService;

	@Test
	public void testGetPage() {
		// GIVEN
		Contact contact1 = Contact.builder().name("kontakt1").build();
		Contact contact2 = Contact.builder().name("kontakt2").build();
		// WHEN
		assertEquals("No contacts in DB", 0, contactService.getPage(1).getContent().size());
		contactService.save(contact1);
		contactService.save(contact2);
		// THEN
		assertEquals("Shoud be 2 contacts in DB", 2, contactService.getPage(1).getContent().size());
	}

	@Test
	public void testSave() {
		// GIVEN
		Contact contact = Contact.builder().name("kontakt").build();
		// WHEN
		assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbc, "contacts", "name = '" + contact.getName() + "'"));
		contactService.save(contact);
		// THEN
		assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbc, "contacts", "name = '" + contact.getName() + "'"));
	}

	@Test
	public void testFindOne_Found() {
		// GIVEN
		Contact contact = Contact.builder().name("kontakt").build();
		// WHEN
		assertEquals("Not yet saved contact should have id=null", null, contact.getId());
		contactService.save(contact);
		Contact retrived = contactService.findOne(1L);
		// THEN
		assertEquals("Saved contact should have id=1", new Long(1L), retrived.getId());
		assertEquals("Contact objs should be the same", contact, retrived);
	}

	@Test
	public void testFindOne_NotFound() {
		assertEquals(0, JdbcTestUtils.countRowsInTable(jdbc, "contacts"));
		assertEquals("Contact objs should be the same", null, contactService.findOne(1L));
	}

	@Test
	public void testDelete() {
		// GIVEN
		Contact contact = Contact.builder().name("kontakt").build();
		contactService.save(contact);
		// WHEN
		assertEquals("Should be only one contact obj in DB", 1, contactService.getPage(1).getContent().size());
		contactService.delete(1L);
		// THEN
		assertEquals("After delete there should be no contacts in DB", 0,
				contactService.getPage(1).getContent().size());
	}

	@Test
	public void testGetPageResultFromSearchInput_ResultsFound_EmptyInputPassed() {
		// GIVEN
		Contact contact1 = Contact.builder().name("contactWithDesiredName").build();
		Contact contact2 = Contact.builder().firstName("CONTACTWITHDESIREDNAME").build();
		Contact contact3 = Contact.builder().lastName("mSiRm").build();
		Contact contact4 = Contact.builder().name("aaa").build();
		Contact contact5 = Contact.builder().firstName("bbb").build();
		Contact contact6 = Contact.builder().lastName("ccc").build();
		Contact formContact = Contact.builder().name("").build();
		FormClass formClass = new FormClass(formContact);
		// WHEN
		assertEquals("No contacts in DB", 0, contactService.getPage(1).getContent().size());
		contactService.save(contact1);
		contactService.save(contact2);
		contactService.save(contact3);
		contactService.save(contact4);
		contactService.save(contact5);
		contactService.save(contact6);
		assertEquals("Should be 6 contacts in DB", 6, contactService.getPage(1).getContent().size());
		List<Contact> matchingContacts = Arrays.asList(contact1, contact2, contact3, contact4, contact5, contact6);
		List<Contact> sortableResult = new ArrayList<>(
				contactService.getPageResultFromSearchInput(formClass, 1).getContent());
		sortableResult.sort(Comparator.comparing(Contact::getId));
		// THEN
		assertEquals("All contact objs in DB should be retrived", 6, sortableResult.size());
		assertEquals("Search result should be equal to matching made List", matchingContacts, sortableResult);
	}

	@Test
	public void testGetPageResultFromSearchInput_ResultsFound_NonEmptyInputPassed() {
		// GIVEN
		Contact contact1 = Contact.builder().name("contactWithDesiredName").build();
		Contact contact2 = Contact.builder().firstName("CONTACTWITHDESIREDNAME").build();
		Contact contact3 = Contact.builder().lastName("mSiRm").build();
		Contact contact4 = Contact.builder().name("aaa").build();
		Contact contact5 = Contact.builder().firstName("bbb").build();
		Contact contact6 = Contact.builder().lastName("ccc").build();
		Contact formContact = Contact.builder().name("SIR").build();
		FormClass formClass = new FormClass(formContact);
		// WHEN
		assertEquals("No contacts in DB", 0, contactService.getPage(1).getContent().size());
		contactService.save(contact1);
		contactService.save(contact2);
		contactService.save(contact3);
		contactService.save(contact4);
		contactService.save(contact5);
		contactService.save(contact6);
		assertEquals("Should be 6 contacts in DB", 6, contactService.getPage(1).getContent().size());
		List<Contact> matchingContacts = Arrays.asList(contact1, contact2, contact3);
		List<Contact> sortableResult = new ArrayList<>(
				contactService.getPageResultFromSearchInput(formClass, 1).getContent());
		sortableResult.sort(Comparator.comparing(Contact::getId));
		// THEN
		assertEquals("All contact objs in DB should be retrived", 3, sortableResult.size());
		assertEquals("Search result should be equal to matching made List", matchingContacts, sortableResult);
	}

	@Test
	public void testGetPageResultFromSearchInput_NoResultsFound() {
		// GIVEN
		Contact contact1 = Contact.builder().name("contactWithDesiredName").build();
		Contact contact2 = Contact.builder().firstName("CONTACTWITHDESIREDNAME").build();
		Contact contact3 = Contact.builder().lastName("mSiRm").build();
		Contact contact4 = Contact.builder().name("aaa").build();
		Contact contact5 = Contact.builder().firstName("bbb").build();
		Contact contact6 = Contact.builder().lastName("ccc").build();
		Contact formContact = Contact.builder().name("x").build();
		FormClass formClass = new FormClass(formContact);
		// WHEN
		assertEquals("No contacts in DB", 0, contactService.getPage(1).getContent().size());
		contactService.save(contact1);
		contactService.save(contact2);
		contactService.save(contact3);
		contactService.save(contact4);
		contactService.save(contact5);
		contactService.save(contact6);
		assertEquals("Should be 6 contacts in DB", 6, contactService.getPage(1).getContent().size());
		Page<Contact> resultPage = contactService.getPageResultFromSearchInput(formClass, 1);
		// THEN
		assertFalse(resultPage.hasContent());
	}

}
