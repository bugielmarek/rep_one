package com.bugielmarek.timetable.RESTcontrollers;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.bugielmarek.timetable.errors.Error;
import com.bugielmarek.timetable.errors.ValidationError;
import com.bugielmarek.timetable.exceptions.InvalidIssueProvidedException;
import com.bugielmarek.timetable.exceptions.IssueNotFoundException;
import com.bugielmarek.timetable.exceptions.IssuesNotFoundException;
import com.bugielmarek.timetable.exceptions.MatchingIssuesNotFoundException;
import com.bugielmarek.timetable.exceptions.MyIssuesNotFoundException;
import com.bugielmarek.timetable.exceptions.UserNotFoundException;
import com.bugielmarek.timetable.model.Issue;
import com.bugielmarek.timetable.services.IssueService;
import com.bugielmarek.timetable.services.UserService;

@RestController
@RequestMapping("/api/issues")
public class IssueRESTController {

	private IssueService issueService;
	
	private UserService userService;
	
	@Autowired
	public IssueRESTController(IssueService issueService, UserService userService) {
		this.issueService = issueService;
		this.userService = userService;
	}
	
	

	@RequestMapping(value = "/issue/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Issue issueById(@PathVariable long id) {
		Issue retrived = issueService.findOne(id);
		if (retrived == null) {
			throw new IssueNotFoundException(id);
		}
		return retrived;
	}

	@RequestMapping(value = "/allissues", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Issue> allIssues() {
		List<Issue> retrived = issueService.findListAllIssues();
		if (retrived.isEmpty()) {
			throw new IssuesNotFoundException();
		}
		return retrived;
	}

	@RequestMapping(value = "/myissues/{username}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Issue> myIssues(@PathVariable String username) {

		if (userService.findUser() == null) {
			throw new UserNotFoundException(username);
		}

		List<Issue> retrived = issueService.findListMyIssues(userService.findUser());
		if (retrived.isEmpty()) {
			throw new MyIssuesNotFoundException(username);
		}
		return retrived;
	}

	@RequestMapping(value = "/find/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Issue> findIssues(@PathVariable String name) {

		List<Issue> retrived = issueService.findListFromSearchInput(name);
		if (retrived.isEmpty()) {
			throw new MatchingIssuesNotFoundException(name);
		}
		return retrived;
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/issue/{id}", method = RequestMethod.DELETE)
	public @ResponseBody void deleteIssue(@PathVariable long id) {

		Issue retrived = issueService.findOne(id);
		if (retrived == null) {
			throw new IssueNotFoundException(id);
		}
		issueService.delete(id);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Issue> createIssue(@Valid @RequestBody Issue issue, BindingResult result,
			UriComponentsBuilder ucb) {

		if (result.hasErrors()) {
			throw new InvalidIssueProvidedException(result);
		}
		Issue saved = issueService.save(issue);
		HttpHeaders headers = new HttpHeaders();
		URI locationUri = ucb.path("/api/issues/issue/").path((String.valueOf(saved.getId()))).build().toUri();
		headers.setLocation(locationUri);
		return new ResponseEntity<Issue>(saved, headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Issue> updateIssue(@PathVariable long id, @Valid @RequestBody Issue issue, BindingResult result,
			UriComponentsBuilder ucb) {

		Issue retrived = issueService.findOne(id);
		if (retrived == null) {
			throw new IssueNotFoundException(id);
		}

		if (result.hasErrors()) {
			throw new InvalidIssueProvidedException(result);
		}
		Issue saved = issueService.save(issue);
		HttpHeaders headers = new HttpHeaders();
		URI locationUri = ucb.path("/api/issues/issue/").path((String.valueOf(saved.getId()))).build().toUri();
		headers.setLocation(locationUri);
		return new ResponseEntity<Issue>(saved, headers, HttpStatus.OK);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(IssueNotFoundException.class)
	public Error issueNotFound(IssueNotFoundException e) {
		long issueID = e.getIssueId();
		return new Error(4, "Issue id:" + issueID + " not found");
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(IssuesNotFoundException.class)
	public Error issuesNotFound(IssuesNotFoundException e) {
		return new Error(4, "No Issues whatsoever found on the server");
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(MyIssuesNotFoundException.class)
	public Error myIssuesNotFound(MyIssuesNotFoundException e) {
		String username = e.getName();
		return new Error(4, "No Issues found on the Server for username: " + username);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(UserNotFoundException.class)
	public Error myIssuesNotFoundNoSuchUser(UserNotFoundException e) {
		String username = e.getUsername();
		return new Error(4, "User with username: " + username + " not found");
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(MatchingIssuesNotFoundException.class)
	public Error matchingIssuesNotFound(MatchingIssuesNotFoundException e) {
		String name = e.getName();
		return new Error(4, "No issues matching: " + name + " found");
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidIssueProvidedException.class)
	public ValidationError issueNotFound(InvalidIssueProvidedException e) {
		BindingResult result = e.getResult();
		List<FieldError> fieldErrors = result.getFieldErrors();
		return processFieldErrors(fieldErrors);
	}
	
	private ValidationError processFieldErrors(List<FieldError> fieldErrors) {
		ValidationError validationError = new ValidationError();
		for(FieldError fieldError : fieldErrors){
			validationError.addErrors(fieldError.getField(), fieldError.getDefaultMessage());
		}
		return validationError;
	}
}
