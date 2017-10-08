package com.bugielmarek.timetable.SOAP;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.bugielmarek.timetable.model.Contact;

@WebService
public interface SOAPContactServiceInterface {

	@WebMethod(operationName="znajdzOne")
	Contact findOne(Long id);

	@WebMethod(operationName="zapisz")
	Contact save(Contact contact);

	@WebMethod(operationName="usun")
	void delete(Long id);

}