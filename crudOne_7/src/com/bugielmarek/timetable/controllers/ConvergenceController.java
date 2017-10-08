package com.bugielmarek.timetable.controllers;

import javax.validation.Valid;

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

import com.bugielmarek.timetable.model.Convergence;
import com.bugielmarek.timetable.model.FormClass;
import com.bugielmarek.timetable.services.ConvergenceService;
import com.bugielmarek.timetable.services.UserService;

@Controller
@RequestMapping(value = "/convergences")
public class ConvergenceController {

	private static final String CONVERGENCE = "convergence";
	private static final String CONVERGENCES = "convergences";
	private static final String PAGE = "page";
	private static final String CREATE_CONVERGENCE = "createconvergence";
	private static final String HOME = "home";

	private ConvergenceService convService;

	private UserService userService;
	
	@Autowired
	public ConvergenceController(ConvergenceService convService, UserService userService) {
		this.convService = convService;
		this.userService = userService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String allConvergences(Model model, Convergence convergence,
			@RequestParam(value = "p", defaultValue = "1") int pageNumber) {

		if (!model.containsAttribute(PAGE)) {
			model.addAttribute(PAGE, convService.getPage(pageNumber));
		}
		return CONVERGENCES;
	}

	@RequestMapping(value = "/createconvergence", method = RequestMethod.GET)
	public String createConvergence(Model model) {
		model.addAttribute(new Convergence());
		return CREATE_CONVERGENCE;
	}

	@RequestMapping(value = "/createconvergence", method = RequestMethod.POST)
	public String createConvergence(RedirectAttributes model, @Valid Convergence convergence, BindingResult result) {

		if (result.hasErrors()) {
			return CREATE_CONVERGENCE;
		}
		Convergence saved = convService.save(convergence, userService.findUser());
		model.addAttribute("id", saved.getId());
		model.addFlashAttribute(saved);
		return "redirect:/convergences/convergence/{id}";
	}

	@RequestMapping(value = "/convergence/{id}", method = RequestMethod.GET)
	public String convergence(Model model, @PathVariable long id) {

		if (!model.containsAttribute(CONVERGENCE)) {
			model.addAttribute(convService.findOne(id));
		}
		return CONVERGENCE;
	}

	@RequestMapping(value = "/editconvergence/{id}", method = RequestMethod.GET)
	public String editconvergence(Model model, @PathVariable long id) {

		model.addAttribute(convService.findOne(id));
		return CREATE_CONVERGENCE;
	}

	@RequestMapping(value = "/deleteconvergence/{id}", method = RequestMethod.GET)
	public String deleteConvergence(@PathVariable long id) {

		convService.delete(id);
		return "redirect:/convergences";
	}

	@RequestMapping(value = "/findconvergence", method = RequestMethod.POST)
	public String searchForConvergence(RedirectAttributes model, @ModelAttribute("formClass") FormClass formClass,
			BindingResult rs, @RequestParam(name = "p", defaultValue = "1") int pageNumber) {

		if (!convService.searchInputHasResult(formClass, pageNumber)) {
			rs.rejectValue("convergence.name", "search.convergence.notFound");
			return HOME;
		}
		model.addFlashAttribute(PAGE, convService.getPageResultFromSearchInput(formClass, pageNumber));
		return "redirect:/convergences";
	}
}