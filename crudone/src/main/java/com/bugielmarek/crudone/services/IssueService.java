package com.bugielmarek.crudone.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.bugielmarek.crudone.models.FormClass;
import com.bugielmarek.crudone.models.Issue;
import com.bugielmarek.crudone.models.User;

/**
 * Provides the base model interface for Issue service. Allows creating,
 * reading, updating, deleting, searching issues.
 * @author Bugiel Marek
 *
 */
public interface IssueService {

	/**
	 * Returns page matching pageNumber
	 * @param pageNumber indicates which page should be returned
	 * @return matching page
	 */
	Page<Issue> getPage(int pageNumber);

	/**
	 * Returns page matching user and pageNumber
	 * @param user issue's user
	 * @param pageNumber indicates which matching page should be returned
	 * @return matching page
	 */
	Page<Issue> findPageMyIssues(User user, int pageNumber);

	/**
	 * Returns page matching issue held in formClass and pageNumber
	 * @param formClass holds issue in it. Issue was created by submitting the form
	 * @param pageNumber indicates which matching page should be returned
	 * @return matching page
	 */
	Page<Issue> getPageResultFromSearchInput(FormClass formClass, int pageNumber);

	/**
	 * Returns issue matching id, the primary key
	 * @param id the primary key of issue
	 * @return matching issue
	 */
	Issue findOne(Long id);

	/**
	 * Saves issue
	 * @param issue created by submitting the form
	 * @param user one that submitted the form
	 * @return saved user
	 */
	Issue save(Issue issue, User user);

	/**
	 * Deletes issue
	 * @param id the primary key of issue
	 */
	void delete(Long id);

	/**
	 * Saves issue sent by REST client
	 * @param issue sent by REST client
	 * @return saved issue
	 */
	Issue save(Issue issue);

	/**Returns list
	 * @return list of issues
	 */
	List<Issue> findListAllIssues();
	
	/**
	 * Returns list matching name
	 * @param name issue's name
	 * @return list of matching issues
	 */
	List<Issue> findListFromSearchInput(String name);
}