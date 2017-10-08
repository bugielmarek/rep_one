package com.bugielmarek.timetable.daos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bugielmarek.timetable.model.CaseType;
import com.bugielmarek.timetable.model.Payment;

public interface PaymentsDao extends PagingAndSortingRepository<Payment, Long> {

	Page<Payment> findByNameContaining(String name, Pageable pageable);
	
	Page<Payment> findByCaseTypeAndSygNumber(CaseType caseType, String sygNumber, Pageable pageable);
	
	Page<Payment> findByNameContainingAndCaseTypeAndSygNumber(String name, CaseType caseType, String sygNumber, Pageable pageable);
	
	Payment findByCaseTypeAndSygNumber(CaseType caseType, String sygNumber);
	
	boolean existsByCaseTypeAndSygNumber(CaseType caseType, String sygNumber);
	
	boolean existsByCaseTypeAndSygNumberAndIdNot(CaseType caseType, String sygNumber, Long id);
}
