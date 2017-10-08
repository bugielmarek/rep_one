package com.bugielmarek.timetable.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.bugielmarek.timetable.model.CaseType;
import com.bugielmarek.timetable.model.FormClass;
import com.bugielmarek.timetable.model.Issue;
import com.bugielmarek.timetable.model.User;

public interface IssueService {

	Page<Issue> getPage(int pageNumber);

	Page<Issue> findPageMyIssues(User user, int pageNumber);

	Page<Issue> findPageByName(String name, int pageNumber);

	Page<Issue> findPageByCaseTypeSygNumber(CaseType caseType, String sygNumber, int pageNumber);

	Page<Issue> findPageByNameCaseTypeSygNumber(String name, CaseType caseType, String sygNumber, int pageNumber);

	Page<Issue> getPageResultFromSearchInput(FormClass formClass, int pageNumber);
	
	List<Issue> findListAllIssues();
	
	Issue findOne(Long id);

	Issue save(Issue issue, User user);

	void delete(Long id);

	//User findByUsername(String username);

	boolean searchInputHasResult(FormClass formClass, int pageNumber);

	List<Issue> findListMyIssues(User user);

	Issue save(Issue issue);

	List<Issue> findListFromSearchInput(String name);
	
}