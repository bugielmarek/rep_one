package com.bugielmarek.crudone.services;

import org.springframework.data.domain.Page;

import com.bugielmarek.crudone.models.CaseType;
import com.bugielmarek.crudone.models.FormClass;
import com.bugielmarek.crudone.models.Payment;
import com.bugielmarek.crudone.models.User;

/**
 * Provides the base model interface for Payment service. Allows creating, reading, updating, deleting, searching
 * payments.
 * @author Bugiel Marek
 */
public interface PaymentService {

	/**
	 * Returns page matching pageNumber
	 * @param pageNumber indicates which page should be returned
	 * @return matching page
	 */
	Page<Payment> getPage(int pageNumber);

	/**
	 * Returns page matching payment held in formClass and pageNumber
	 * @param formClass holds payment in it. Payment was created by submitting the form
	 * @param pageNumber indicates which matching page should be returned
	 * @return matching page
	 */
	Page<Payment> getPageResultFromSearchInput(FormClass formClass, int pageNumber);
	
	/**
	 * Returns payment matching caseType and sygNumber.
	 * @param caseType payment's caseType
	 * @param sygNumber payment's sygNumber
	 * @return the matching payment
	 */
	Payment findByCaseTypeSygNumber(CaseType caseType, String sygNumber);

	/**
	 * Saves payment
	 * @param payment created by submitting the form
	 * @param user one that submitted the form
	 * @return saved payment
	 */
	Payment save(Payment payment, User user);

	/**
	 * Deletes payment
	 * @param id the primary key of payment
	 */
	void delete(Long id);

	/**
	 * Returns payment matching id, the primary key
	 * @param id the primary key of payment 
	 * @return matching payment
	 */
	Payment findOne(long id);

	/** Returns <code>true</code> if there already is payment with exactly the
	 *  same values for both caseType and sygNumber fields as @param payment has. 
	 * @param payment created by submitting the form
	 * @return <code>true</code> if payment already exists;
	 * 		   <code>false</code> otherwise
	 */
	boolean exists(Payment payment);

}
