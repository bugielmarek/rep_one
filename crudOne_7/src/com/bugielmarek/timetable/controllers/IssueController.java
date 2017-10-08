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

import com.bugielmarek.timetable.model.FormClass;
import com.bugielmarek.timetable.model.Issue;
import com.bugielmarek.timetable.services.IssueService;
import com.bugielmarek.timetable.services.UserService;

@Controller
@RequestMapping(value = "/issues")
public class IssueController {

	private static final String ISSUES = "issues";
	private static final String CREATE_ISSUE = "createissue";
	private static final String PAGE = "page";
	private static final String ISSUE = "issue";
	private static final String HOME = "home";

	private IssueService issueService;

	private UserService userService;
	
	@Autowired
	public IssueController(IssueService issueService, UserService userService) {
		this.issueService = issueService;
		this.userService = userService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String allIssues(Model model, Issue issue, @RequestParam(value = "p", defaultValue = "1") int pageNumber) {

		if (!model.containsAttribute(PAGE)) {
			model.addAttribute(PAGE, issueService.getPage(pageNumber));
		}
		return ISSUES;
	}

	@RequestMapping(value = "/myissues", method = RequestMethod.GET)
	public String myIssues(Model model, Issue issue, @RequestParam(value = "p", defaultValue = "1") int pageNumber) {
		model.addAttribute(PAGE, issueService.findPageMyIssues(userService.findUser(), pageNumber));
		return ISSUES;
	}

	@RequestMapping(value = "/createissue", method = RequestMethod.GET)
	public String createIssue(Model model) {

		model.addAttribute(ISSUE, new Issue());
		return CREATE_ISSUE;
	}

	@RequestMapping(value = "/createissue", method = RequestMethod.POST)
	public String createIssue(@Valid Issue issue, BindingResult result, RedirectAttributes redModel) {

		if (result.hasErrors()) {
			return CREATE_ISSUE;
		}
		Issue saved = issueService.save(issue, userService.findUser());
		redModel.addAttribute("id", saved.getId());
		redModel.addFlashAttribute(saved);
		return "redirect:/issues/issue/{id}";
	}

	@RequestMapping(value = "/issue/{id}", method = RequestMethod.GET)
	public String issue(Model model, @PathVariable long id) {

		if (!model.containsAttribute(ISSUE)) {
			model.addAttribute(issueService.findOne(id));
		}
		return ISSUE;
	}

	@RequestMapping(value = "/editissue/{id}", method = RequestMethod.GET)
	public String editIssue(Model model, @PathVariable long id) {

		model.addAttribute(issueService.findOne(id));
		return CREATE_ISSUE;
	}

	@RequestMapping(value = "/deleteissue/{id}", method = RequestMethod.GET)
	public String deleteIssue(Model model, @PathVariable long id) {

		issueService.delete(id);
		return "redirect:/issues";
	}

	@RequestMapping(value = "/findissue", method = RequestMethod.POST)
	public String searchForIssue(RedirectAttributes model, @ModelAttribute("formClass") FormClass formClass,
			BindingResult rs, @RequestParam(name = "p", defaultValue = "1") int pageNumber) {

		if (!issueService.searchInputHasResult(formClass, pageNumber)) {
			rs.rejectValue("issue.name", "search.issue.notFound");
			return HOME;
		}
		model.addFlashAttribute(PAGE, issueService.getPageResultFromSearchInput(formClass, pageNumber));
		return "redirect:/issues";
	}
}