package com.bugielmarek.crudone.daos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bugielmarek.crudone.models.CaseType;
import com.bugielmarek.crudone.models.Payment;

public interface PaymentsDao extends PagingAndSortingRepository<Payment, Long> {

	Page<Payment> findByNameContaining(String name, Pageable pageable);
	
	Page<Payment> findByCaseTypeAndSygNumber(CaseType caseType, String sygNumber, Pageable pageable);
	
	Page<Payment> findByNameContainingAndCaseTypeAndSygNumber(String name, CaseType caseType, String sygNumber, Pageable pageable);
	
	Payment findByCaseTypeAndSygNumber(CaseType caseType, String sygNumber);
	
	boolean existsByCaseTypeAndSygNumber(CaseType caseType, String sygNumber);
	
	boolean existsByCaseTypeAndSygNumberAndIdNot(CaseType caseType, String sygNumber, Long id);
}
