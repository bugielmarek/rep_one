package com.bugielmarek.timetable.test.backend;

import static org.junit.Assert.assertEquals;

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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.bugielmarek.timetable.model.Contact;
import com.bugielmarek.timetable.model.FormClass;
import com.bugielmarek.timetable.services.ContactService;

@ActiveProfiles("embeddedH2")
@ContextConfiguration(locations = { "classpath:com/bugielmarek/timetable/config/testConfig.xml",
		"classpath:com/bugielmarek/timetable/config/rootAppContext.xml",
		"classpath:com/bugielmarek/timetable/config/security-context.xml" })
@RunWith(SpringRunner.class)
public class ContactServiceTest {

	private JdbcTemplate jdbc;

	@Autowired
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return this.jdbc = new JdbcTemplate(dataSource);
	}

	private static final String CREATE = "com/bugielmarek/timetable/scripts/createContactsTable.sql";
	private static final String DROP = "com/bugielmarek/timetable/scripts/dropContactsTable.sql";

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
			Contact contact1 = new Contact.ContactBuilder()
					.name("kontakt1")
					.build();
			Contact contact2 = new Contact.ContactBuilder()
					.name("kontakt2")
					.build();
		assertEquals("No contacts in DB", 0, contactService.getPage(1).getContent().size());
			contactService.save(contact1);
			contactService.save(contact2);
		assertEquals("Shoud be 2 contacts in DB", 2, contactService.getPage(1).getContent().size());
	}

	@Test
	public void testSave() {
			Contact contact = new Contact.ContactBuilder()
					.name("kontakt")
					.build();
		assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbc, "contacts", "name = '" + contact.getName() + "'"));
			contactService.save(contact);
		assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbc, "contacts", "name = '" + contact.getName() + "'"));
	}
	
	@Test
	public void testFindOne_Found() {
			Contact contact = new Contact.ContactBuilder()
					.name("kontakt")
					.build();
		assertEquals("Not yet saved contact should have id=null", null, contact.getId());
			contactService.save(contact);
			Contact retrived = contactService.findOne(1L);
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
			Contact contact = new Contact.ContactBuilder()
					.name("kontakt")
					.build();
			contactService.save(contact);
		assertEquals("Should be only one contact obj in DB", 1, contactService.getPage(1).getContent().size());
			contactService.delete(1L);
		assertEquals("After delete there should be no contacts in DB", 0, contactService.getPage(1).getContent().size());
	}
	
	@Test
	public void testGetPageByName_ResultsFound() {
			Contact contact1 = new Contact.ContactBuilder()
					.name("contactWithDesiredName")
					.build();
			Contact contact2 = new Contact.ContactBuilder()
					.firstName("CONTACTWITHDESIREDNAME")
					.build();
			Contact contact3 = new Contact.ContactBuilder()
					.lastName("mSiRm")
					.build();
			Contact contact4 = new Contact.ContactBuilder()
					.name("aaa")
					.build();
			Contact contact5 = new Contact.ContactBuilder()
					.firstName("bbb")
					.build();
			Contact contact6 = new Contact.ContactBuilder()
					.lastName("ccc")
					.build();
			String searchInput = "sir";
		
		assertEquals("No contacts in DB", 0, contactService.getPage(1).getContent().size());
			contactService.save(contact1);
			contactService.save(contact2);
			contactService.save(contact3);
			contactService.save(contact4);
			contactService.save(contact5);
			contactService.save(contact6);
		assertEquals("Should be 6 contacts in DB", 6, contactService.getPage(1).getContent().size());
			List<Contact> matchingContacts = Arrays.asList(contact1, contact2, contact3);
			List<Contact> sortableResult = new ArrayList<>(contactService.findPageByName(searchInput, 1).getContent());
			sortableResult.sort(Comparator.comparing(Contact::getId));
		assertEquals("There should be only 3 contacts machting search input", 3, contactService.findPageByName(searchInput, 1).getContent().size());
		assertEquals("Custom matching list should be equal to retrived one", matchingContacts, sortableResult);
	}
	
	@Test
	public void testGetPageByName_NoResultsFound() {
			Contact contact1 = new Contact.ContactBuilder()
					.name("contactWithDesiredName")
					.build();
			Contact contact2 = new Contact.ContactBuilder()
					.firstName("CONTACTWITHDESIREDNAME")
					.build();
			Contact contact3 = new Contact.ContactBuilder()
					.lastName("mSiRm")
					.build();
			String searchInput = "xxx";
		
		assertEquals("No contacts in DB", 0, contactService.getPage(1).getContent().size());
			contactService.save(contact1);
			contactService.save(contact2);
			contactService.save(contact3);
		assertEquals("Should be 3 contacts in DB", 3, contactService.getPage(1).getContent().size());
		assertEquals("There should be no matching results - list.size() should be 0", 0, contactService.findPageByName(searchInput, 1).getContent().size());
	}
	
	@Test
	public void testSearchInputHasResult_ResultsFound() {
			Contact contact1 = new Contact.ContactBuilder()
					.name("contactWithDesiredName")
					.build();
			Contact contact2 = new Contact.ContactBuilder()
					.firstName("CONTACTWITHDESIREDNAME")
					.build();
			Contact contact3 = new Contact.ContactBuilder()
					.lastName("mSiRm")
					.build();
			Contact contact4 = new Contact.ContactBuilder()
					.name("aaa")
					.build();
			Contact contact5 = new Contact.ContactBuilder()
					.firstName("bbb")
					.build();
			Contact contact6 = new Contact.ContactBuilder()
					.lastName("ccc")
					.build();
			Contact formContact = new Contact.ContactBuilder()
					.name("sir")
					.build();
			FormClass formClass = new FormClass(formContact);
		
		assertEquals("No contacts in DB", 0, contactService.getPage(1).getContent().size());
			contactService.save(contact1);
			contactService.save(contact2);
			contactService.save(contact3);
			contactService.save(contact4);
			contactService.save(contact5);
			contactService.save(contact6);
		assertEquals("Should be 6 contacts in DB", 6, contactService.getPage(1).getContent().size());
		assertEquals("There are contact objs in DB matching form input", true, contactService.searchInputHasResult(formClass, 1));
	}
	
	@Test
	public void testSearchInputHasResult_NoResultsFound() {
			Contact contact1 = new Contact.ContactBuilder()
					.name("contactWithDesiredName")
					.build();
			Contact contact2 = new Contact.ContactBuilder()
					.firstName("CONTACTWITHDESIREDNAME")
					.build();
			Contact contact3 = new Contact.ContactBuilder()
					.lastName("mSiRm")
					.build();
			Contact contact4 = new Contact.ContactBuilder()
					.name("aaa")
					.build();
			Contact contact5 = new Contact.ContactBuilder()
					.firstName("bbb")
					.build();
			Contact contact6 = new Contact.ContactBuilder()
					.lastName("ccc")
					.build();
			Contact formContact = new Contact.ContactBuilder()
					.name("xxx")
					.build();
			FormClass formClass = new FormClass(formContact);
		
		assertEquals("No contacts in DB", 0, contactService.getPage(1).getContent().size());
			contactService.save(contact1);
			contactService.save(contact2);
			contactService.save(contact3);
			contactService.save(contact4);
			contactService.save(contact5);
			contactService.save(contact6);
		assertEquals("Should be 6 contacts in DB", 6, contactService.getPage(1).getContent().size());
		assertEquals("No contact objs in DB matching form input", false, contactService.searchInputHasResult(formClass, 1));
	}
	
	@Test
	public void testGetPageResultFromSearchInput_ResultsFound_EmptyInputPassed() {
			Contact contact1 = new Contact.ContactBuilder()
					.name("contactWithDesiredName")
					.build();
			Contact contact2 = new Contact.ContactBuilder()
					.firstName("CONTACTWITHDESIREDNAME")
					.build();
			Contact contact3 = new Contact.ContactBuilder()
					.lastName("mSiRm")
					.build();
			Contact contact4 = new Contact.ContactBuilder()
					.name("aaa")
					.build();
			Contact contact5 = new Contact.ContactBuilder()
					.firstName("bbb")
					.build();
			Contact contact6 = new Contact.ContactBuilder()
					.lastName("ccc")
					.build();
			Contact formContact = new Contact.ContactBuilder()
					.name("")
					.build();
			FormClass formClass = new FormClass(formContact);
		
		assertEquals("No contacts in DB", 0, contactService.getPage(1).getContent().size());
			contactService.save(contact1);
			contactService.save(contact2);
			contactService.save(contact3);
			contactService.save(contact4);
			contactService.save(contact5);
			contactService.save(contact6);
		assertEquals("Should be 6 contacts in DB", 6, contactService.getPage(1).getContent().size());
			List<Contact> matchingContacts = Arrays.asList(contact1, contact2, contact3, contact4, contact5, contact6);
			List<Contact> sortableResult = new ArrayList<>(contactService.getPageResultFromSearchInput(formClass, 1).getContent());
			sortableResult.sort(Comparator.comparing(Contact::getId));
		assertEquals("All contact objs in DB should be retrived", 6, sortableResult.size());
		assertEquals("Search result should be equal to matching made List", matchingContacts, sortableResult);
	}
	
	@Test
	public void testGetPageResultFromSearchInput_ResultsFound_NonEmptyInputPassed() {
			Contact contact1 = new Contact.ContactBuilder()
					.name("contactWithDesiredName")
					.build();
			Contact contact2 = new Contact.ContactBuilder()
					.firstName("CONTACTWITHDESIREDNAME")
					.build();
			Contact contact3 = new Contact.ContactBuilder()
					.lastName("mSiRm")
					.build();
			Contact contact4 = new Contact.ContactBuilder()
					.name("aaa")
					.build();
			Contact contact5 = new Contact.ContactBuilder()
					.firstName("bbb")
					.build();
			Contact contact6 = new Contact.ContactBuilder()
					.lastName("ccc")
					.build();
			Contact formContact = new Contact.ContactBuilder()
					.name("SIR")
					.build();
			FormClass formClass = new FormClass(formContact);
		
		assertEquals("No contacts in DB", 0, contactService.getPage(1).getContent().size());
			contactService.save(contact1);
			contactService.save(contact2);
			contactService.save(contact3);
			contactService.save(contact4);
			contactService.save(contact5);
			contactService.save(contact6);
		assertEquals("Should be 6 contacts in DB", 6, contactService.getPage(1).getContent().size());
			List<Contact> matchingContacts = Arrays.asList(contact1, contact2, contact3);
			List<Contact> sortableResult = new ArrayList<>(contactService.getPageResultFromSearchInput(formClass, 1).getContent());
			sortableResult.sort(Comparator.comparing(Contact::getId));
		assertEquals("All contact objs in DB should be retrived", 3, sortableResult.size());
		assertEquals("Search result should be equal to matching made List", matchingContacts, sortableResult);
	}
}
