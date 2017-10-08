package com.bugielmarek.timetable.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bugielmarek.timetable.daos.ContactsDao;
import com.bugielmarek.timetable.model.Contact;
import com.bugielmarek.timetable.model.FormClass;

@Service
public class ContactServiceImpl implements ContactService {

	private ContactsDao dao;
	
	@Autowired
	public ContactServiceImpl(ContactsDao dao) {
		this.dao = dao;
	}

	private static final int PAGESIZE = 10;

	public Page<Contact> getPage(int pageNumber) {
		PageRequest request = new PageRequest(pageNumber - 1, PAGESIZE, Sort.Direction.ASC, "name");
		return dao.findAll(request);
	}

	public Page<Contact> findPageByName(String name, int pageNumber) {
		return dao.findByName(name, new PageRequest(pageNumber - 1, PAGESIZE, Sort.Direction.ASC, "name"));
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

	public boolean searchInputHasResult(FormClass formClass, int pageNumber) {
		String name = formClass.getContact().getName();
		return (findPageByName(name, pageNumber).hasContent());
	}

	public Page<Contact> getPageResultFromSearchInput(FormClass formClass, int pageNumber) {
		String name = formClass.getContact().getName();
		return findPageByName(name, pageNumber);
	}

	public List<Contact> findListAllContacts() {
		List<Contact> retrived = dao.findAllByOrderByNameAsc();
		if (retrived.isEmpty()) {
			return retrived;
		}
		return retrived;
	}

	public List<Contact> findListFromSearchInput(String name) {
		return dao.findByName(name);
	}
}