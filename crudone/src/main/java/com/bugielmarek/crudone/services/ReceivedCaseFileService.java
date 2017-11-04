package com.bugielmarek.crudone.services;

import org.springframework.data.domain.Page;

import com.bugielmarek.crudone.models.ReceivedCaseFile;
import com.bugielmarek.crudone.models.FormClass;

/**
 * Provides the base model interface for ReceivedCaseFile service. Allows
 * creating, reading, updating, deleting, searching receivedCaseFiles.
 * @author Bugiel Marek
 *
 */
public interface ReceivedCaseFileService {

	/**
	  * Returns page matching pageNumber
	 * @param pageNumber indicates which page should be returned
	 * @return matching page
	 */
	Page<ReceivedCaseFile> getPage(int pageNumber);

	/**
	 * Returns page matching receivedCaseFile held in formClass and pageNumber
	 * @param formClass holds receivedCaseFile in it. ReceivedCaseFile was created by submitting the form
	 * @param pageNumber indicates which matching page should be returned
	 * @return matching page
	 */
	Page<ReceivedCaseFile> getPageResultFromSearchInput(FormClass formClass, int pageNumber);

	/**
	 * Returns receivedCaseFile matching id, the primary key
	 * @param id the primary key of receivedCaseFile 
	 * @return matching receivedCaseFile
	 */
	ReceivedCaseFile findOne(Long id);

	/**
	 * Saves receivedCaseFile
	 * @param receivedCaseFile created by submitting the form
	 * @return saved receivedCaseFile
	 */
	ReceivedCaseFile save(ReceivedCaseFile receivedCaseFile);

	/**
	 * Deletes receivedCaseFile
	 * @param id the primary key of receivedCaseFile 
	 */
	void delete(Long id);
}
