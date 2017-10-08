package com.bugielmarek.timetable.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bugielmarek.timetable.daos.PaymentsDao;
import com.bugielmarek.timetable.model.CaseType;
import com.bugielmarek.timetable.model.FormClass;
import com.bugielmarek.timetable.model.Payment;
import com.bugielmarek.timetable.model.User;

@Service
public class PaymentServiceImpl implements PaymentService {

	private PaymentsDao dao;

	@Autowired
	public PaymentServiceImpl(PaymentsDao dao){
		this.dao = dao;
	}

	private static final int PAGESIZE = 10;

	public Page<Payment> getPage(int pageNumber) {
		return dao.findAll(new PageRequest(pageNumber - 1, PAGESIZE, Sort.Direction.ASC, "deadline"));
	}

	public Page<Payment> findPageByName(String name, int pageNumber) {
		return dao.findByNameContaining(name, new PageRequest(pageNumber - 1, PAGESIZE, Sort.Direction.ASC, "name"));
	}

	public Page<Payment> findPageByCaseTypeSygNumber(CaseType caseType, String sygNumber, int pageNumber) {
		return dao.findByCaseTypeAndSygNumber(caseType, sygNumber, new PageRequest(pageNumber - 1, PAGESIZE));
	}

	public Page<Payment> findPageByNameCaseTypeSygNumber(String name, CaseType caseType, String sygNumber, int pageNumber) {
		return dao.findByNameContainingAndCaseTypeAndSygNumber(name, caseType, sygNumber,
				new PageRequest(pageNumber - 1, PAGESIZE));
	}

	public Payment findByCaseTypeSygNumber(CaseType caseType, String sygNumber) {
		return dao.findByCaseTypeAndSygNumber(caseType, sygNumber);
	}

	public Payment save(Payment payment, User user) {
		LocalDate date = LocalDate.parse(payment.getDate());
		payment.setDeadline(date);
		payment.setUser(user);
		return dao.save(payment);
	}

	public void delete(Long id) {
		dao.delete(id);
	}

	public boolean exists(CaseType caseType, String sygNumber, Long id) {
		return dao.existsByCaseTypeAndSygNumberAndIdNot(caseType, sygNumber, id);
	}

	public boolean exists(CaseType caseType, String sygNumber) {
		return dao.existsByCaseTypeAndSygNumber(caseType, sygNumber);
	}

	public Payment findOne(long paymentId) {
		Payment payment = dao.findOne(paymentId);
		payment.setDate(payment.getDeadline().toString());
		return payment;
	}

	public boolean isNotValid(Payment payment) {

		if (payment.getId() != null && exists(payment.getCaseType(), payment.getSygNumber(), payment.getId())) {
			return true;
		}
		if (payment.getId() == null && exists(payment.getCaseType(), payment.getSygNumber())) {
			return true;
		}
		return false;
	}

	public boolean searchInputHasResult(FormClass formClass, int pageNumber) {
		Payment payment = formClass.getPayment();
		String name = payment.getName();
		String sygNumber = payment.getSygNumber();
		CaseType caseType = payment.getCaseType();
		boolean isNameEmpty = name.isEmpty();
		boolean isSygNumberEmpty = sygNumber.isEmpty();

		// if both fields are empty user is not going back to searchForm so false
		if (isNameEmpty && isSygNumberEmpty) {
			return false;
		}
		
		// sygNumber was provided, check if it matches records in DB
		if (isNameEmpty && !isSygNumberEmpty) {
			Page<Payment> page = findPageByCaseTypeSygNumber(caseType, sygNumber, pageNumber);
			return !page.hasContent();
		}
		// name was provided, check if it matches records in DB
		if (!isNameEmpty && isSygNumberEmpty) {
			Page<Payment> page = findPageByName(name, pageNumber);
			return !page.hasContent();
		}
		// both fields were provided, check if they match records in DB
		Page<Payment> page = findPageByNameCaseTypeSygNumber(name, caseType, sygNumber, pageNumber);
		return page.hasContent();
	}

	public Page<Payment> getPageResultFromSearchInput(FormClass formClass, int pageNumber) {

		Payment payment = formClass.getPayment();

		String name = payment.getName();
		String sygNumber = payment.getSygNumber();
		CaseType caseType = payment.getCaseType();

		boolean isNameEmpty = name.isEmpty();
		boolean isSygNumberEmpty = sygNumber.isEmpty();

		// return records from DB for matching sygNumber
		if (isNameEmpty && !isSygNumberEmpty) {
			return findPageByCaseTypeSygNumber(caseType, sygNumber, pageNumber);
		}
		// return records from DB matching name
		if (!isNameEmpty && isSygNumberEmpty) {
			return findPageByName(name, pageNumber);
		}
		// return records from DB matching both fields
		if (!isNameEmpty && !isSygNumberEmpty)
			return findPageByNameCaseTypeSygNumber(name, caseType, sygNumber, pageNumber);
		// nothing was provided, return all records from DB
		return getPage(pageNumber);
	}
}
