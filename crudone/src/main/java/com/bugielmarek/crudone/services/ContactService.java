package com.bugielmarek.crudone.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.bugielmarek.crudone.models.Contact;
import com.bugielmarek.crudone.models.FormClass;

/**
 * Provides the base model interface for Contact service. Allows creating,
 * reading, updating, deleting, searching contacts.
 * @author Bugiel Marek
 *
 */
public interface ContactService {

	/**
	 * Returns page matching pageNumber
	 * @param pageNumber indicates which page should be returned
	 * @return matching page
	 */
	Page<Contact> getPage(int pageNumber);

	/**
	 * Returns page matching contact held in formClass and pageNumber
	 * @param formClass holds contact in it. Contact was created by submitting the form
	 * @param pageNumber indicates which matching page should be returned
	 * @return matching page
	 */
	Page<Contact> getPageResultFromSearchInput(FormClass formClass, int pageNumber);

	/**
	 * Returns contact matching id, the primary key
	 * @param id the primary key of contact
	 * @return matching contact
	 */
	Contact findOne(Long id);

	/**
	 * Saves contact
	 * @param contact created by submitting the form
	 * @return saved contact
	 */
	Contact save(Contact contact);

	/**
	 * Deletes contact
	 * @param id the primary key of contact
	 */
	void delete(Long id);

	/**
	 * Returns list
	 * @return list of contacts
	 */
	List<Contact> findListAllContacts();

	/**
	 * Returns list matching name
	 * @param name contact's name
	 * @return list matching contacts
	 */
	List<Contact> findListFromSearchInput(String name);
}