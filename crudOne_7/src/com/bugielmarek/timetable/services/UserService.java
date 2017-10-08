package com.bugielmarek.timetable.services;

import org.springframework.data.domain.Page;

import com.bugielmarek.timetable.model.User;

public interface UserService {

	Page<User> getPage(int pageNumber);
	
	User create(User user);
	
	boolean exists(String username);
	
	void delete(Long id);
	
	User findOne(Long id);
	
	User findUser();
}
