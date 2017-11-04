package com.bugielmarek.crudone.services.impls;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bugielmarek.crudone.daos.IssuesDao;
import com.bugielmarek.crudone.models.CaseType;
import com.bugielmarek.crudone.models.FormClass;
import com.bugielmarek.crudone.models.Issue;
import com.bugielmarek.crudone.models.User;
import com.bugielmarek.crudone.services.IssueService;

import lombok.extern.slf4j.Slf4j;

/**
 * Provides the base model implementation for Issue service. Allows creating,
 * reading, updating, deleting, searching issues.
 * 
 * @author Bugiel Marek
 *
 */
@Slf4j
@Service
public class IssueServiceImpl implements IssueService {

	private IssuesDao dao;
	
	private static final String DEADLINE = "deadline";

	@Autowired
	public IssueServiceImpl(IssuesDao dao) {
		this.dao = dao;
	}

	private static final int PAGESIZE = 7;

	public Page<Issue> getPage(int pageNumber) {
		return dao.findAll(new PageRequest(pageNumber - 1, PAGESIZE, Sort.Direction.ASC, DEADLINE));
	}

	public Page<Issue> findPageMyIssues(User user, int pageNumber) {
		return dao.findByUser(user, new PageRequest(pageNumber - 1, PAGESIZE, Sort.Direction.ASC, DEADLINE));
	}

	public Issue findOne(Long id) {
		Issue issue = dao.findOne(id);
		if (issue == null) {
			return null;
		}
		issue.setDate(issue.getDeadline().toString());
		return issue;
	}

	public Issue save(Issue issue, User user) {
		LocalDate date = LocalDate.parse(issue.getDate());
		issue.setUser(user);
		issue.setDeadline(date);
		return dao.save(issue);
	}

	public void delete(Long id) {
		dao.delete(id);
	}

	public Page<Issue> getPageResultFromSearchInput(FormClass formClass, int pageNumber) {
		Issue issue = formClass.getIssue();
		String name = issue.getName();
		String sygNumber = issue.getSygNumber();
		CaseType caseType = issue.getCaseType();
		boolean isNameEmpty = name.isEmpty();
		boolean isSygNumberEmpty = sygNumber.isEmpty();

		IssueServiceImpl.log.info("getPageResultFromSearchInput() invoked, Issue(name={}, sygNumber={}, caseType={})",
				name, sygNumber, caseType);
		
		if (isNameEmpty && !isSygNumberEmpty) {
			IssueServiceImpl.log.info("Fetching results for 'sygNumber' and 'caseType'");
			return dao.findByCaseTypeAndSygNumber(caseType, sygNumber, new PageRequest(pageNumber - 1, PAGESIZE, Sort.Direction.ASC, DEADLINE));
		}
		if (!isNameEmpty && isSygNumberEmpty) {
			IssueServiceImpl.log.info("Fetching results for 'name'");
			return dao.findByNameContaining(name,
					new PageRequest(pageNumber - 1, PAGESIZE, Sort.Direction.ASC, DEADLINE));
		}
		if (!isNameEmpty && !isSygNumberEmpty){
			IssueServiceImpl.log.info("Fetching results for 'sygNumber' and 'caseType' and 'name'");
			return dao.findByNameContainingAndCaseTypeAndSygNumber(name, caseType, sygNumber, new PageRequest(pageNumber - 1, PAGESIZE, Sort.Direction.ASC, DEADLINE));
		}
	
		IssueServiceImpl.log.info("Empty input, returning all records from DB");
		return getPage(pageNumber);
	}

	public List<Issue> findListAllIssues() {
		List<Issue> retrived = dao.findAllByOrderByDeadlineAsc();
		if (retrived.isEmpty()) {
			return retrived;
		}
		return retrived;
	}

	public Issue save(Issue issue) {
		LocalDate date = LocalDate.parse(issue.getDate());
		issue.setDeadline(date);
		return dao.save(issue);
	}

	public List<Issue> findListFromSearchInput(String name) {
		return dao.findByNameContaining(name);
	}
}
