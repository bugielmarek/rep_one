package com.bugielmarek.crudone.controllers;

import javax.validation.Valid;

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

import com.bugielmarek.crudone.models.FormClass;
import com.bugielmarek.crudone.models.ReceivedCaseFile;
import com.bugielmarek.crudone.services.ReceivedCaseFileService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/receivedcasefiles")
public class ReceivedCaseFileController {

	private static final String RECEIVEDCASEFILE = "receivedcasefile";
	private static final String RECEIVEDCASEFILES = "receivedcasefiles";
	private static final String PAGE = "page";
	private static final String CREATE_RECEIVEDCASEFILE = "createreceivedcasefile";
	private static final String HOME = "home";

	private ReceivedCaseFileService convService;

	@Autowired
	public ReceivedCaseFileController(ReceivedCaseFileService convService) {
		this.convService = convService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String allReceivedcasefiles(Model model, ReceivedCaseFile receivedCaseFile,
			@RequestParam(value = "p", defaultValue = "1") int pageNumber) {

		ReceivedCaseFileController.log.info("GET request/redirect for '/receivedcasefiles' made");
		if (!model.containsAttribute(PAGE)) {
			ReceivedCaseFileController.log.info("No flashAttribute detected");
			model.addAttribute(PAGE, convService.getPage(pageNumber));
		}
		return RECEIVEDCASEFILES;
	}

	@RequestMapping(value = "/createreceivedcasefile", method = RequestMethod.GET)
	public String createReceivedcasefile(Model model) {
		ReceivedCaseFileController.log.info("GET request for '/createreceivedcasefile' made");
		model.addAttribute(new ReceivedCaseFile());
		return CREATE_RECEIVEDCASEFILE;
	}

	@RequestMapping(value = "/createreceivedcasefile", method = RequestMethod.POST)
	public String createReceivedcasefile(RedirectAttributes model, @Valid ReceivedCaseFile receivedCaseFile, BindingResult result) {
		ReceivedCaseFileController.log.info("POST request for '/createreceivedcasefile' made");
		if (result.hasErrors()) {
			ReceivedCaseFileController.log.info("Invalid Receivedcasefile obj passed, returning form to the user");
			return CREATE_RECEIVEDCASEFILE;
		}
		ReceivedCaseFile saved = convService.save(receivedCaseFile);
		model.addAttribute("id", saved.getId());
		model.addFlashAttribute(saved);
		return "redirect:/receivedcasefiles/receivedcasefile/{id}";
	}

	@RequestMapping(value = "/receivedcasefile/{id}", method = RequestMethod.GET)
	public String receivedcasefile(Model model, @PathVariable long id) {
		ReceivedCaseFileController.log.info("GET request for '/receivedcasefile/{id}' made");
		if (!model.containsAttribute(RECEIVEDCASEFILE)) {
			model.addAttribute(convService.findOne(id));
		}
		return RECEIVEDCASEFILE;
	}

	@RequestMapping(value = "/editreceivedcasefile/{id}", method = RequestMethod.GET)
	public String editReceivedcasefile(Model model, @PathVariable long id) {
		ReceivedCaseFileController.log.info("GET request for '/editreceivedcasefile/{id}' made");
		model.addAttribute(convService.findOne(id));
		return CREATE_RECEIVEDCASEFILE;
	}

	@RequestMapping(value = "/deletereceivedcasefile/{id}", method = RequestMethod.GET)
	public String deleteReceivedcasefile(@PathVariable long id) {
		ReceivedCaseFileController.log.info("GET request for '/deletereceivedcasefile/{id}' made");
		convService.delete(id);
		return "redirect:/receivedcasefiles";
	}

	@RequestMapping(value = "/findreceivedcasefile", method = RequestMethod.POST)
	public String findReceivedcasefile(RedirectAttributes model, @ModelAttribute("formClass") FormClass formClass,
			BindingResult rs, @RequestParam(name = "p", defaultValue = "1") int pageNumber) {

		ReceivedCaseFileController.log.info("POST request for '/findreceivedcasefile' made");
		Page<ReceivedCaseFile> pageResult = convService.getPageResultFromSearchInput(formClass, pageNumber);
		if (!pageResult.hasContent()) {
			ReceivedCaseFileController.log.info("No matching results found in DB for given input");
			rs.rejectValue("receivedcasefile.name", "search.receivedcasefile.notFound");
			return HOME;
		}
		ReceivedCaseFileController.log.info("Matching results found in DB. Redirecting to '/receivedcasefiles'");
		model.addFlashAttribute(PAGE, pageResult);
		return "redirect:/receivedcasefiles";
	}
}