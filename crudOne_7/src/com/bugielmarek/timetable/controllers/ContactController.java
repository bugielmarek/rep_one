package com.bugielmarek.timetable.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bugielmarek.timetable.SOAP.SOAPContactServiceInterface;
import com.bugielmarek.timetable.model.Contact;
import com.bugielmarek.timetable.model.FormClass;
import com.bugielmarek.timetable.services.ContactService;

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
	private SOAPContactServiceInterface SOAPservice;
	
	@RequestMapping(value="/SOAPsingle", method = RequestMethod.GET)
	public String getSOAPsingle(Model model) {

		model.addAttribute(SOAPservice.findOne(13L));
		return "weather";
	}
	
	@Autowired
	public ContactController(ContactService contactService) {
		this.contactService = contactService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String allContacts(Model model, Contact contact,
			@RequestParam(value = "p", defaultValue = "1") int pageNumber) {

		if (!model.containsAttribute(PAGE)) {
			model.addAttribute(PAGE, contactService.getPage(pageNumber));
		}
		return CONTACTS;
	}

	@RequestMapping(value = "/createcontact", method = RequestMethod.GET)
	public String createContact(Model model) {

		model.addAttribute(new Contact());
		return CREATE_CONTACT;
	}

	@RequestMapping(value = "/createcontact", method = RequestMethod.POST)
	public String createContact(RedirectAttributes model, @ModelAttribute Contact contact) {

		Contact saved = contactService.save(contact);
		model.addAttribute("id", saved.getId());
		model.addFlashAttribute(saved);
		return "redirect:/contacts/contact/{id}";
	}

	@RequestMapping(value = "/contact/{id}", method = RequestMethod.GET)
	public String contact(Model model, @PathVariable long id) {

		if (!model.containsAttribute(CONTACT)) {
			model.addAttribute(contactService.findOne(id));
		}
		return CONTACT;
	}

	@RequestMapping(value = "/editcontact/{id}", method = RequestMethod.GET)
	public String editContact(Model model, @PathVariable long id) {

		model.addAttribute(contactService.findOne(id));
		return CREATE_CONTACT;
	}

	@RequestMapping(value = "/deletecontact/{id}", method = RequestMethod.GET)
	public String deleteContact(@PathVariable long id) {

		contactService.delete(id);
		return "redirect:/contacts";
	}

	@RequestMapping(value = "/findcontact", method=RequestMethod.POST)
	public String findContact(@ModelAttribute("formClass") FormClass formClass, BindingResult rs,
			@RequestParam(name = "p", defaultValue = "1") int pageNumber, RedirectAttributes model) {

		if (!contactService.searchInputHasResult(formClass, pageNumber)) {
			rs.rejectValue("contact.name", "search.contactByName.notFound");
			return HOME;
		}
		model.addFlashAttribute(PAGE, contactService.getPageResultFromSearchInput(formClass, pageNumber));
		return "redirect:/contacts";
	}
}
