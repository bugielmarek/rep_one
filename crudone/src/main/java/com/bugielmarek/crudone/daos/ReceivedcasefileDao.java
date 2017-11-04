package com.bugielmarek.crudone.daos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bugielmarek.crudone.models.ReceivedCaseFile;

public interface ReceivedcasefileDao extends PagingAndSortingRepository<ReceivedCaseFile, Long>{
	
	Page<ReceivedCaseFile> findByNameContaining(String name, Pageable pageable);
}
