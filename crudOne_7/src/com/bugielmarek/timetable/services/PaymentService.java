package com.bugielmarek.timetable.services;

import org.springframework.data.domain.Page;

import com.bugielmarek.timetable.model.CaseType;
import com.bugielmarek.timetable.model.FormClass;
import com.bugielmarek.timetable.model.Payment;
import com.bugielmarek.timetable.model.User;

public interface PaymentService {

	Page<Payment> getPage(int pageNumber);

	Page<Payment> findPageByName(String name, int pageNumber);

	Page<Payment> findPageByCaseTypeSygNumber(CaseType caseType, String sygNumber, int pageNumber);

	Page<Payment> findPageByNameCaseTypeSygNumber(String name, CaseType caseType, String sygNumber, int pageNumber);

	Page<Payment> getPageResultFromSearchInput(FormClass formClass, int pageNumber);
	
	Payment findByCaseTypeSygNumber(CaseType caseType, String sygNumber);

	Payment save(Payment payment, User user);

	void delete(Long id);

	boolean exists(CaseType caseType, String sygNumber, Long id);

	boolean exists(CaseType caseType, String sygNumber);

	Payment findOne(long paymentId);

	boolean isNotValid(Payment payment);

	boolean searchInputHasResult(FormClass formClass, int pageNumber);
}
