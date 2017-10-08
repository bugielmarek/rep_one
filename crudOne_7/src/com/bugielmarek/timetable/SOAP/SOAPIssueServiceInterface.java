package com.bugielmarek.timetable.SOAP;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.bugielmarek.timetable.model.Issue;

@WebService
public interface SOAPIssueServiceInterface {

	@WebMethod(operationName="findOne")
	Issue findOne(Long id);

	@WebMethod(operationName="save")
	Issue save(Issue issue, String username);

	@WebMethod(operationName="delete")
	void delete(Long id);
}