package com.bugielmarek.crudone.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bugielmarek.crudone.models.Contact;
import com.bugielmarek.crudone.models.FormClass;
import com.bugielmarek.crudone.services.ContactService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/contacts")
public class ContactController {

	private static final String CONTACT = "contact";
	private static final String CONTACTS = "contacts";
	private static final String PAGE = "page";
	private static final String CREATE_CONTACT = "createcontact";
	private static final String HOME = "home";

	private ContactService contactService;

	@Autowired
	public ContactController(ContactService contactService) {
		this.contactService = contactService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String allContacts(Model model, Contact contact,
			@RequestParam(value = "p", defaultValue = "1") int pageNumber) {

		ContactController.log.info("GET request/redirect for '/contacts' made");
		if (!model.containsAttribute(PAGE)) {
			ContactController.log.info("No flashAttribute detected");
			model.addAttribute(PAGE, contactService.getPage(pageNumber));
		}
		return CONTACTS;
	}

	@RequestMapping(value = "/createcontact", method = RequestMethod.GET)
	public String createContact(Model model) {

		ContactController.log.info("GET request for '/createcontact' made");
		model.addAttribute(new Contact());
		return CREATE_CONTACT;
	}

	@RequestMapping(value = "/createcontact", method = RequestMethod.POST)
	public String createContact(RedirectAttributes model, Contact contact) {

		ContactController.log.info("POST request for '/createcontact' made");
		Contact saved = contactService.save(contact);
		model.addAttribute("id", saved.getId());
		model.addFlashAttribute(saved);
		ContactController.log.debug("Contact saved into DB: {}", saved);
		return "redirect:/contacts/contact/{id}";
	}

	@RequestMapping(value = "/contact/{id}", method = RequestMethod.GET)
	public String contact(Model model, @PathVariable long id) {

		ContactController.log.info("GET request for '/contact/{id}' made");
		if (!model.containsAttribute(CONTACT)) {
			model.addAttribute(contactService.findOne(id));
			ContactController.log.debug("No flashAttribute detected");
		}
		return CONTACT;
	}

	@RequestMapping(value = "/editcontact/{id}", method = RequestMethod.GET)
	public String editContact(Model model, @PathVariable long id) {

		ContactController.log.info("GET request for '/editcontact/{id}' made");
		model.addAttribute(contactService.findOne(id));
		return CREATE_CONTACT;
	}

	@RequestMapping(value = "/deletecontact/{id}", method = RequestMethod.GET)
	public String deleteContact(@PathVariable long id) {

		ContactController.log.info("GET request for '/deletecontact/{id}' made, id: {}", id);
		contactService.delete(id);
		return "redirect:/contacts";
	}

	@RequestMapping(value = "/findcontact", method = RequestMethod.POST)
	public String findContact(@ModelAttribute("formClass") FormClass formClass, BindingResult rs,
			@RequestParam(name = "p", defaultValue = "1") int pageNumber, RedirectAttributes model) {

		ContactController.log.info("POST request for '/findcontact' made");
		Page<Contact> resultPage = contactService.getPageResultFromSearchInput(formClass, pageNumber);
		if (!resultPage.hasContent()) {
			ContactController.log.info("No matching results found in DB for given input");
			rs.rejectValue("contact.name", "search.contact.notFound");
			return HOME;
		}
		ContactController.log.info("Matching results found in DB. Redirecting to '/contacts'");
		model.addFlashAttribute(PAGE, resultPage);
		return "redirect:/contacts";
	}
}
