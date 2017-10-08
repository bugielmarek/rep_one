package com.bugielmarek.timetable.daos;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bugielmarek.timetable.model.CaseType;
import com.bugielmarek.timetable.model.Issue;
import com.bugielmarek.timetable.model.User;

public interface IssuesDao extends PagingAndSortingRepository<Issue, Long> {

	Page<Issue> findByNameContaining(String name, Pageable pageable);
	
	Page<Issue> findByCaseTypeAndSygNumber(CaseType caseType, String sygNumber, Pageable pageable);

	Page<Issue> findByNameContainingAndCaseTypeAndSygNumber(String name, CaseType caseType, String sygNumber, Pageable pageable);

	Page<Issue> findByUser(User user, Pageable pageable);

	List<Issue> findAllByOrderByDeadlineAsc();
	
	List<Issue> findByUserOrderByDeadlineAsc(User user);
	
	List<Issue> findByNameContaining(String name);
}

