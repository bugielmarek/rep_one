package com.bugielmarek.timetable.RESTcontrollers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.bugielmarek.timetable.errors.Error;
import com.bugielmarek.timetable.exceptions.ContactNotFoundException;
import com.bugielmarek.timetable.exceptions.ContactsNotFoundException;
import com.bugielmarek.timetable.exceptions.MatchingContactsNotFoundException;
import com.bugielmarek.timetable.model.Contact;
import com.bugielmarek.timetable.services.ContactService;

@RestController
@RequestMapping(value = "/api/contacts")
public class ContactRESTController {

	private ContactService contactService;

	@Autowired
	public ContactRESTController(ContactService contactService) {
		this.contactService = contactService;
	}

	@RequestMapping(value = "/allcontacts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Contact> allContact() {
		List<Contact> retrived = contactService.findListAllContacts();
		if (retrived.isEmpty()) {
			throw new ContactsNotFoundException();
		}
		return retrived;
	}

	@RequestMapping(value = "/contact/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Contact contactById(@PathVariable long id) {

		Contact retrived = contactService.findOne(id);
		if (retrived == null) {
			throw new ContactNotFoundException(id);
		}
		return retrived;
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/contact/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void deleteContact(@PathVariable long id) {

		Contact retrived = contactService.findOne(id);
		if (retrived == null) {
			throw new ContactNotFoundException(id);
		}
		contactService.delete(id);
	}

	@RequestMapping(value = "/find/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Contact> findContacts(@PathVariable String name) {

		List<Contact> retrived = contactService.findListFromSearchInput(name);
		if (retrived.isEmpty()) {
			throw new MatchingContactsNotFoundException(name);
		}
		return retrived;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Contact> createContact(@RequestBody Contact contact, UriComponentsBuilder ucb) {

		Contact saved = contactService.save(contact);
		HttpHeaders headers = new HttpHeaders();
		URI locationUri = ucb.path("/api/contacts/contact/").path(String.valueOf(saved.getId())).build().toUri();
		headers.setLocation(locationUri);
		return new ResponseEntity<Contact>(saved, headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Contact> updateContact(@PathVariable long id, @RequestBody Contact contact,
			UriComponentsBuilder ucb) {

		if (contactService.findOne(id) == null) {
			throw new ContactNotFoundException(id);
		}

		Contact saved = contactService.save(contact);
		HttpHeaders headers = new HttpHeaders();
		URI locationUri = ucb.path("/api/contacts/contact/").path(String.valueOf(saved.getId())).build().toUri();
		headers.setLocation(locationUri);
		return new ResponseEntity<Contact>(saved, headers, HttpStatus.OK);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ContactNotFoundException.class)
	public Error contactNotFound(ContactNotFoundException e) {

		long id = e.getId();
		return new Error(4, "Contact id: " + id + " not found.");
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ContactsNotFoundException.class)
	public Error contactsNotFound(ContactsNotFoundException e) {

		return new Error(4, "No contacts whatsoever found on the server");
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(MatchingContactsNotFoundException.class)
	public Error matchingContactsNotFound(MatchingContactsNotFoundException e) {
		String name = e.getName();
		return new Error(4, "No contacts matching: " + name + " found");
	}

}
