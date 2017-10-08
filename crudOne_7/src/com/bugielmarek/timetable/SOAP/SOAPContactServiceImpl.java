package com.bugielmarek.timetable.SOAP;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;

import com.bugielmarek.timetable.daos.ContactsDao;
import com.bugielmarek.timetable.model.Contact;

@WebService(endpointInterface = "com.bugielmarek.timetable.SOAP.SOAPContactServiceInterface", serviceName = "SOAPContact")
public class SOAPContactServiceImpl implements SOAPContactServiceInterface {

	private ContactsDao dao;
	
	@Autowired
	public SOAPContactServiceImpl(ContactsDao dao) {
		this.dao = dao;
	}
	public Contact findOne(Long id) {
		return dao.findOne(id);
	}

	public Contact save(Contact contact) {
		return dao.save(contact);
	}

	public void delete(Long id) {
		dao.delete(id);
	}

}