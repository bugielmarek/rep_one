package com.bugielmarek.crudone.services.impls;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bugielmarek.crudone.daos.PaymentsDao;
import com.bugielmarek.crudone.models.CaseType;
import com.bugielmarek.crudone.models.FormClass;
import com.bugielmarek.crudone.models.Payment;
import com.bugielmarek.crudone.models.User;
import com.bugielmarek.crudone.services.PaymentService;

import lombok.extern.slf4j.Slf4j;

/**
 * Provides the base model implementation for Payment service. Allows creating,
 * reading, updating, deleting, searching payments.
 * @author Bugiel Marek
 */
@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

	private PaymentsDao dao;

	@Autowired
	public PaymentServiceImpl(PaymentsDao dao) {
		this.dao = dao;
	}

	private static final int PAGESIZE = 10;

	@Override
	public Page<Payment> getPage(int pageNumber) {
		PaymentServiceImpl.log.info("getPage() invoked, pageNumber={}", pageNumber);
		return dao.findAll(new PageRequest(pageNumber - 1, PAGESIZE, Sort.Direction.ASC, "deadline"));
	}

	@Override
	public Payment findByCaseTypeSygNumber(CaseType caseType, String sygNumber) {
		PaymentServiceImpl.log.info("findByCaseTypeSygNumber() invoked, caseType={}, sygNumber={}", caseType,
				sygNumber);
		return dao.findByCaseTypeAndSygNumber(caseType, sygNumber);
	}

	@Override
	public Payment save(Payment payment, User user) {
		LocalDate date = LocalDate.parse(payment.getDate());
		payment.setDeadline(date);
		payment.setUser(user);
		Payment saved = dao.save(payment);
		PaymentServiceImpl.log.info("save() invoked, saved Payment({})", saved);
		return saved;
	}

	@Override
	public void delete(Long id) {
		PaymentServiceImpl.log.info("delete() invoked, deleting Payment with id={}", id);
		dao.delete(id);
	}

	@Override
	public Payment findOne(long id) {
		PaymentServiceImpl.log.info("findOne() invoked, id={}", id);
		Payment payment = dao.findOne(id);
		if (payment == null) {
			PaymentServiceImpl.log.info("Returning NULL");
			return null;
		}
		payment.setDate(payment.getDeadline().toString());
		PaymentServiceImpl.log.info("Returning Payment({})", payment);
		return payment;
	}

	@Override
	public boolean exists(Payment payment) {
		PaymentServiceImpl.log.info("exists() invoked, Payment({})", payment);
		if (payment.getId() != null && dao.existsByCaseTypeAndSygNumberAndIdNot(payment.getCaseType(),
				payment.getSygNumber(), payment.getId())) {
			PaymentServiceImpl.log.info("Returning TRUE, for id!=null");
			return true;
		}
		if (payment.getId() == null
				&& dao.existsByCaseTypeAndSygNumber(payment.getCaseType(), payment.getSygNumber())) {
			PaymentServiceImpl.log.info("Returning TRUE, for id==null");
			return true;
		}
		return false;
	}

	@Override
	public Page<Payment> getPageResultFromSearchInput(FormClass formClass, int pageNumber) {

		Payment payment = formClass.getPayment();
		String name = payment.getName();
		String sygNumber = payment.getSygNumber();
		CaseType caseType = payment.getCaseType();
		PaymentServiceImpl.log.info("getPageResultFromSearchInput() invoked, Payment({})", payment);
		boolean isNameEmpty = name.isEmpty();
		boolean isSygNumberEmpty = sygNumber.isEmpty();

		if (isNameEmpty && !isSygNumberEmpty) {
			PaymentServiceImpl.log.info("Fetching results for 'sygNumber' and 'caseType'");
			return dao.findByCaseTypeAndSygNumber(caseType, sygNumber, new PageRequest(pageNumber - 1, PAGESIZE));
		}
		if (!isNameEmpty && isSygNumberEmpty) {
			PaymentServiceImpl.log.info("Fetching results for 'name'");
			return dao.findByNameContaining(name,
					new PageRequest(pageNumber - 1, PAGESIZE, Sort.Direction.ASC, "name"));
		}
		if (!isNameEmpty && !isSygNumberEmpty) {
			PaymentServiceImpl.log.info("Fetching results for 'sygNumber' and 'caseType' and 'name'");
			return dao.findByNameContainingAndCaseTypeAndSygNumber(name, caseType, sygNumber,
					new PageRequest(pageNumber - 1, PAGESIZE));
		}
		PaymentServiceImpl.log.info("Empty input, returning all records from DB");
		return getPage(pageNumber);
	}
}
