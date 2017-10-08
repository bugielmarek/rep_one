package com.bugielmarek.timetable.daos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bugielmarek.timetable.model.Convergence;

public interface ConvergencesDao extends PagingAndSortingRepository<Convergence, Long>{
	
	Page<Convergence> findByNameContaining(String name, Pageable pageable);
}
