package com.bugielmarek.crudone.RESTcontrollers;

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

import com.bugielmarek.crudone.errors.Error;
import com.bugielmarek.crudone.errors.ValidationError;
import com.bugielmarek.crudone.exceptions.InvalidIssueProvidedException;
import com.bugielmarek.crudone.exceptions.IssueNotFoundException;
import com.bugielmarek.crudone.exceptions.IssuesNotFoundException;
import com.bugielmarek.crudone.exceptions.MatchingIssuesNotFoundException;
import com.bugielmarek.crudone.models.Issue;
import com.bugielmarek.crudone.services.IssueService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/issues")
public class IssueRESTController {

	private IssueService issueService;
	
	@Autowired
	public IssueRESTController(IssueService issueService) {
		this.issueService = issueService;
	}

	@RequestMapping(value = "/allissues", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Issue> allIssues() {
		IssueRESTController.log.info("GET request for '/allissues' made");
		List<Issue> retrived = issueService.findListAllIssues();
		if (retrived.isEmpty()) {
			IssueRESTController.log.info("Retrieved List from DB is empty, throwing IssuesNotFoundException");
			throw new IssuesNotFoundException();
		}
		return retrived;
	}

	@RequestMapping(value = "/issue/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Issue issueById(@PathVariable long id) {
		IssueRESTController.log.info("GET request for '/issue/{id}' made");
		Issue retrived = issueService.findOne(id);
		if (retrived == null) {
			IssueRESTController.log.info("No Issue found for given id={}, throwing IssueNotFoundException", id);
			throw new IssueNotFoundException(id);
		}
		return retrived;
	}

	@RequestMapping(value = "/find/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Issue> findIssues(@PathVariable String name) {
		IssueRESTController.log.info("GET request for '/find/{name}' made");
		List<Issue> retrived = issueService.findListFromSearchInput(name);
		if (retrived.isEmpty()) {
			IssueRESTController.log.info("No Issues found for given name={}, throwing MatchingIssuesNotFoundException", name);
			throw new MatchingIssuesNotFoundException(name);
		}
		return retrived;
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/issue/{id}", method = RequestMethod.DELETE)
	public @ResponseBody void deleteIssue(@PathVariable long id) {
		IssueRESTController.log.info("DELETE request for '/issue/{id}' made");
		Issue retrived = issueService.findOne(id);
		if (retrived == null) {
			IssueRESTController.log.info("No Issue found for given id={}, throwing IssueNotFoundException", id);
			throw new IssueNotFoundException(id);
		}
		issueService.delete(id);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Issue> createIssue(@Valid @RequestBody Issue issue, BindingResult result,
			UriComponentsBuilder ucb) {
		IssueRESTController.log.info("POST request for '/create' made");
		if (result.hasErrors()) {
			IssueRESTController.log.info("Invalid Issue passed, throwing InvalidIssueProvidedException");
			throw new InvalidIssueProvidedException(result);
		}
		Issue saved = issueService.save(issue);
		HttpHeaders headers = new HttpHeaders();
		URI locationUri = ucb.path("/api/issues/issue/").path((String.valueOf(saved.getId()))).build().toUri();
		headers.setLocation(locationUri);
		return new ResponseEntity<>(saved, headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Issue> updateIssue(@PathVariable long id, @Valid @RequestBody Issue issue, BindingResult result,
			UriComponentsBuilder ucb) {
		IssueRESTController.log.info("PUT request for '/update/{id}' made");
		Issue retrived = issueService.findOne(id);
		if (retrived == null) {
			IssueRESTController.log.info("No Issue found for given id={}, throwing IssueNotFoundException", id);
			throw new IssueNotFoundException(id);
		}

		if (result.hasErrors()) {
			IssueRESTController.log.info("Invalid Issue passed, throwing InvalidIssueProvidedException");
			throw new InvalidIssueProvidedException(result);
		}
		Issue saved = issueService.save(issue);
		HttpHeaders headers = new HttpHeaders();
		URI locationUri = ucb.path("/api/issues/issue/").path((String.valueOf(saved.getId()))).build().toUri();
		headers.setLocation(locationUri);
		return new ResponseEntity<>(saved, headers, HttpStatus.OK);
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
		IssueRESTController.log.info("IssuesNotFoundException thrown");
		return new Error(4, "No Issues whatsoever found on the server");
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(MatchingIssuesNotFoundException.class)
	public Error matchingIssuesNotFound(MatchingIssuesNotFoundException e) {
		IssueRESTController.log.info("MatchingIssuesNotFoundException thrown");
		String name = e.getName();
		return new Error(4, "No issues matching: " + name + " found");
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidIssueProvidedException.class)
	public ValidationError issueNotFound(InvalidIssueProvidedException e) {
		IssueRESTController.log.info("InvalidIssueProvidedException thrown");
		BindingResult result = e.getResult();
		List<FieldError> fieldErrors = result.getFieldErrors();
		return processFieldErrors(fieldErrors);
	}
	
	private ValidationError processFieldErrors(List<FieldError> fieldErrors) {
		ValidationError validationError = new ValidationError();
		IssueRESTController.log.info("processFieldErrors() invoked");
		for(FieldError fieldError : fieldErrors){
			validationError.addErrors(fieldError.getField(), fieldError.getDefaultMessage());
		}
		return validationError;
	}
}
