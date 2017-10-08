package com.bugielmarek.timetable.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bugielmarek.timetable.daos.IssuesDao;
import com.bugielmarek.timetable.model.CaseType;
import com.bugielmarek.timetable.model.FormClass;
import com.bugielmarek.timetable.model.Issue;
import com.bugielmarek.timetable.model.User;

@Service
public class IssueServiceImpl implements IssueService {

	private IssuesDao dao;

	@Autowired
	public IssueServiceImpl(IssuesDao dao) {
		this.dao = dao;
	}

	private static final int PAGESIZE = 7;

	public Page<Issue> getPage(int pageNumber) {
		return dao.findAll(new PageRequest(pageNumber - 1, PAGESIZE, Sort.Direction.ASC, "deadline"));
	}

	public Page<Issue> findPageMyIssues(User user, int pageNumber) {
		return dao.findByUser(user, new PageRequest(pageNumber - 1, PAGESIZE, Sort.Direction.ASC, "deadline"));
	}

	public Page<Issue> findPageByName(String name, int pageNumber) {
		return dao.findByNameContaining(name,
				new PageRequest(pageNumber - 1, PAGESIZE, Sort.Direction.ASC, "deadline"));
	}

	public Page<Issue> findPageByCaseTypeSygNumber(CaseType caseType, String sygNumber, int pageNumber) {
		return dao.findByCaseTypeAndSygNumber(caseType, sygNumber,
				new PageRequest(pageNumber - 1, PAGESIZE, Sort.Direction.ASC, "deadline"));
	}

	public Page<Issue> findPageByNameCaseTypeSygNumber(String name, CaseType caseType, String sygNumber, int pageNumber) {
		return dao.findByNameContainingAndCaseTypeAndSygNumber(name, caseType, sygNumber,
				new PageRequest(pageNumber - 1, PAGESIZE, Sort.Direction.ASC, "deadline"));
	}

	public Issue findOne(Long id) {
		Issue issue = dao.findOne(id);
		// czy to sprawdzanie nulla rzeczywiscie jest potrzebne? Skoro issue == null to jaki jest sens
		// zwracania nulla w innej postaci?
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

	/*public User findByUsername(String username) {
		return usersdao.findByUsername(username);
	}
*/
	public boolean searchInputHasResult(FormClass formClass, int pageNumber) {
		Issue issue = formClass.getIssue();
		String name = issue.getName();
		String sygNumber = issue.getSygNumber();
		CaseType caseType = issue.getCaseType();
		boolean isNameEmpty = name.isEmpty();
		boolean isSygNumberEmpty = sygNumber.isEmpty();
		// if both fields are empty user is not going back to searchForm so
		// false
		if (isNameEmpty && isSygNumberEmpty) {
			return false;
		}
		// sygNumber was provided, check if it matches records in DB
		if (isNameEmpty && !isSygNumberEmpty) {
			Page<Issue> page = findPageByCaseTypeSygNumber(caseType, sygNumber, pageNumber);
			return !page.hasContent();
		}
		// name was provided, check if it matches records in DB
		if (!isNameEmpty && isSygNumberEmpty) {
			Page<Issue> page = findPageByName(name, pageNumber);
			return !page.hasContent();
		}
		// both fields were provided, check if they match records in DB
		Page<Issue> page = findPageByNameCaseTypeSygNumber(name, caseType, sygNumber, pageNumber);
		return page.hasContent();
	}

	public Page<Issue> getPageResultFromSearchInput(FormClass formClass, int pageNumber) {
		Issue issue = formClass.getIssue();
		String name = issue.getName();
		String sygNumber = issue.getSygNumber();
		CaseType caseType = issue.getCaseType();
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

	public List<Issue> findListAllIssues() {
		List<Issue> retrived = dao.findAllByOrderByDeadlineAsc();
		if (retrived.isEmpty()) {
			return retrived;
		}
		return retrived;
	}

	public List<Issue> findListMyIssues(User user) {
		return dao.findByUserOrderByDeadlineAsc(user);
	}

	// czy jest w ogóle odpalona?
	public Issue save(Issue issue) {
		LocalDate date = LocalDate.parse(issue.getDate());
		issue.setDeadline(date);
		return dao.save(issue);
	}

	public List<Issue> findListFromSearchInput(String name) {
		return dao.findByNameContaining(name);
	}
}
