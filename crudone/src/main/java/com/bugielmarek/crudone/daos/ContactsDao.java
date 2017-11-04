package com.bugielmarek.crudone.daos;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.bugielmarek.crudone.models.Contact;

public interface ContactsDao extends PagingAndSortingRepository<Contact, Long> {

	@Query("select c from Contact c where lower(c.name) like lower(concat('%', :name,'%')) or lower(c.firstName) like lower(concat('%', :name,'%')) or lower(c.lastName) like lower(concat('%', :name,'%'))")
	Page<Contact> findByName(@Param("name") String name, Pageable pageable);

	List<Contact> findAllByOrderByNameAsc();

	@Query("select c from Contact c where lower(c.name) like lower(concat('%', :name,'%')) or lower(c.firstName) like lower(concat('%', :name,'%')) or lower(c.lastName) like lower(concat('%', :name,'%'))")
	List<Contact> findByName(@Param("name") String name);
}
