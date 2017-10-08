package com.bugielmarek.timetable.services;

import org.springframework.data.domain.Page;

import com.bugielmarek.timetable.model.Convergence;
import com.bugielmarek.timetable.model.FormClass;
import com.bugielmarek.timetable.model.User;

public interface ConvergenceService {

	Page<Convergence> getPage(int pageNumber);
	
	Page<Convergence> findPageByName(String name, int pageNumber);
	
	Page<Convergence> getPageResultFromSearchInput(FormClass formClass, int pageNumber);
	
	Convergence findOne(Long id);
	
	Convergence save(Convergence convergence, User user);
	
	void delete(Long id);

	boolean searchInputHasResult(FormClass formClass, int pageNumber);

}
