package com.bugielmarek.crudone.services;

import org.springframework.data.domain.Page;

import com.bugielmarek.crudone.models.User;

/**
 * Provides the base model interface for User service. Allows creating, reading,
 * updating, deleting users.
 * @author Bugiel Marek
 *
 */
public interface UserService {

	/**
	 * Returns page matching pageNumber
	 * @param pageNumber indicates which page should be returned
	 * @return matching page
	 */
	Page<User> getPage(int pageNumber);

	/**
	 * Saves user
	 * @param user created by submitting the form
	 * @return saved user
	 */
	User save(User user);

	/**
	 * Deletes user
	 * @param id the primary key of user
	 */
	void delete(Long id);

	/**
	 * Returns user matching id, the primary key
	 * @param id the primary key of user
	 * @return matching user
	 */
	User findOne(Long id);

	/**
	 * Fetches currently logged in user
	 * @return logged in user
	 */
	User findUser();

	/**
	 * Returns <code>true</code> if there already is user in DB with exactly the
	 * same 'username' as @param user has. 
	 * @param user sent by submitting the form
	 * @return <code>true</code> if 'username' already is present in DB,
	 *  <code>false</code> otherwise
	 */
	boolean doesUsernameExists(User user);
}
