package com.bugielmarek.timetable.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bugielmarek.timetable.daos.UsersDao;
import com.bugielmarek.timetable.model.User;

@Service
public class UserServiceImpl implements UserService{

	private UsersDao dao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserServiceImpl(UsersDao dao) {
		this.dao = dao;
	}
	
	private static final int PAGESIZE = 10;
	
	@Secured("ROLE_ADMIN")
	public Page<User> getPage(int pageNumber) {
		PageRequest request = new PageRequest(pageNumber-1, PAGESIZE, Sort.Direction.ASC, "username");
		return dao.findByAuthority("ROLE_USER", request);
	}
	
	@Secured("ROLE_ADMIN")
	public User create(User user){
		user.setAuthority("ROLE_USER");
		user.setEnabled(true);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return dao.save(user);
	}

	@Secured("ROLE_ADMIN")
	public boolean exists(String username) {
		return dao.existsByUsername(username);
	}
	
	@Secured("ROLE_ADMIN")
	public void delete(Long id) {
		dao.delete(id);
	}
	
	@Secured("ROLE_ADMIN")
	public User findOne(Long id) {
		return dao.findOne(id);
	}
	
	public User findUser(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return dao.findByUsername(auth.getName());
	}
}
