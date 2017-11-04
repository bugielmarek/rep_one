package com.bugielmarek.crudone.services.impls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bugielmarek.crudone.daos.ContactsDao;
import com.bugielmarek.crudone.models.Contact;
import com.bugielmarek.crudone.models.FormClass;
import com.bugielmarek.crudone.services.ContactService;

import lombok.extern.slf4j.Slf4j;

/**
 * Provides the base model interface for Contact service. Allows creating,
 * reading, updating, deleting, searching contacts.
 * 
 * @author Bugiel Marek
 */
@Slf4j
@Service
public class ContactServiceImpl implements ContactService {

	private ContactsDao dao;
	
	@Autowired
	public ContactServiceImpl(ContactsDao dao) {
		this.dao = dao;
	}

	private static final int PAGESIZE = 10;

	public Page<Contact> getPage(int pageNumber) {
		ContactServiceImpl.log.info("getPage() invoked, pageNumber={}", pageNumber);
		PageRequest request = new PageRequest(pageNumber - 1, PAGESIZE, Sort.Direction.ASC, "name");
		return dao.findAll(request);
	}

	public Contact findOne(Long id) {
		ContactServiceImpl.log.info("findOne() invoked, id={}", id);
		Contact contact = dao.findOne(id);
		if(contact == null){
			ContactServiceImpl.log.info("Returning NULL");
			return null;
		}
		ContactServiceImpl.log.info("Returning Contact({})", contact);
		return contact;
	}

	public Contact save(Contact contact) {
		Contact saved = dao.save(contact);
		ContactServiceImpl.log.info("save() invoked, saved Contact({})", saved);
		return saved;
	}

	public void delete(Long id) {
		ContactServiceImpl.log.info("delete() invoked, deleting Contact with id={}", id);
		dao.delete(id);
	}

	public Page<Contact> getPageResultFromSearchInput(FormClass formClass, int pageNumber) {
		String name = formClass.getContact().getName();
		ContactServiceImpl.log.info("getPageResultFromSearchInput() invoked, returning all matching results for given input: {}", name);
		return dao.findByName(name, new PageRequest(pageNumber - 1, PAGESIZE, Sort.Direction.ASC, "name"));
	}

	public List<Contact> findListAllContacts() {
		ContactServiceImpl.log.info("findListAllContacts() invoked");
		return dao.findAllByOrderByNameAsc();
	}

	public List<Contact> findListFromSearchInput(String name) {
		ContactServiceImpl.log.info("findListFromSearchInput() invoked, Contact(name={})", name);
		return dao.findByName(name);
	}
}