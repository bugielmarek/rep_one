package com.bugielmarek.crudone.services.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bugielmarek.crudone.daos.UsersDao;
import com.bugielmarek.crudone.models.User;
import com.bugielmarek.crudone.services.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * Provides the base model implementation for User service. Allows creating, reading,
 * updating, deleting users.
 * 
 * @author Bugiel Marek
 *
 */
@Slf4j
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
	@Override
	public Page<User> getPage(int pageNumber) {
		UserServiceImpl.log.info("getPage() invoked, pageNumber={}", pageNumber);
		return dao.findByAuthority("ROLE_USER", new PageRequest(pageNumber-1, PAGESIZE, Sort.Direction.ASC, "username"));
	}
	
	@Secured("ROLE_ADMIN")
	@Override
	public User save(User user){
		UserServiceImpl.log.info("save() invoked");
		user.setAuthority("ROLE_USER");
		user.setEnabled(true);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return dao.save(user);
	}

	@Secured("ROLE_ADMIN")
	@Override
	public void delete(Long id) {
		UserServiceImpl.log.info("delete() invoked, id={}", id);
		dao.delete(id);
	}
	
	@Secured("ROLE_ADMIN")
	@Override
	public User findOne(Long id) {
		UserServiceImpl.log.info("findOne() invoked, id={}", id);
		return dao.findOne(id);
	}
	
	@Override
	public User findUser(){
		UserServiceImpl.log.info("findUser() invoked");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return dao.findByUsername(auth.getName());
	}

	@Override
	public boolean doesUsernameExists(User user) {
		UserServiceImpl.log.info("doesUsernameExists() invoked, id={}", user.getId());
		if(user.getId() == null){
			return dao.existsByUsername(user.getUsername());
		}
		return dao.existsByUsernameAndIdNot(user.getUsername(), user.getId());
	}
}
