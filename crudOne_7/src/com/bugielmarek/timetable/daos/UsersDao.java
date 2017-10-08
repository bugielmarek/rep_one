package com.bugielmarek.timetable.daos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.bugielmarek.timetable.model.User;

@Repository
public interface UsersDao extends PagingAndSortingRepository<User, Long>{

	Page<User> findByAuthority(String authority, Pageable pageable);
	
	User findByUsername(String username);
	
	boolean existsByUsername(String username);
}
