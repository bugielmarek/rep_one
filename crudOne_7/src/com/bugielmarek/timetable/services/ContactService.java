package com.bugielmarek.timetable.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.bugielmarek.timetable.model.Contact;
import com.bugielmarek.timetable.model.FormClass;

public interface ContactService {

	Page<Contact> getPage(int pageNumber);

	Page<Contact> findPageByName(String name, int pageNumber);

	Page<Contact> getPageResultFromSearchInput(FormClass formClass, int pageNumber);
	
	Contact findOne(Long id);

	Contact save(Contact contact);

	void delete(Long id);

	boolean searchInputHasResult(FormClass formClass, int pageNumber);

	List<Contact> findListAllContacts();

	List<Contact> findListFromSearchInput(String name);

}