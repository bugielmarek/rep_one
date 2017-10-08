package com.bugielmarek.timetable.SOAP;

import java.time.LocalDate;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;

import com.bugielmarek.timetable.daos.IssuesDao;
import com.bugielmarek.timetable.daos.UsersDao;
import com.bugielmarek.timetable.model.Issue;

@WebService(endpointInterface = "com.bugielmarek.timetable.SOAP.SOAPIssueServiceInterface", serviceName = "SOAPIssue")
public class SOAPIssueServiceImpl implements SOAPIssueServiceInterface {

	private IssuesDao dao;

	private UsersDao usersdao;

	@Autowired
	public SOAPIssueServiceImpl(IssuesDao dao, UsersDao usersdao) {
		this.dao = dao;
		this.usersdao = usersdao;
	}

	public Issue findOne(Long id) {
		Issue issue = dao.findOne(id);
		if (issue == null) {
			return null;
		}
		return issue;
	}

	public Issue save(Issue issue, String username) {
		LocalDate date = LocalDate.parse(issue.getDate());
		issue.setUser(usersdao.findByUsername(username));
		issue.setDeadline(date);
		return dao.save(issue);
	}

	public void delete(Long id) {
		dao.delete(id);
	}
}
