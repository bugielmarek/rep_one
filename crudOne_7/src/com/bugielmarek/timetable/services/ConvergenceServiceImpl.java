package com.bugielmarek.timetable.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bugielmarek.timetable.daos.ConvergencesDao;
import com.bugielmarek.timetable.model.Convergence;
import com.bugielmarek.timetable.model.FormClass;
import com.bugielmarek.timetable.model.User;

@Service
public class ConvergenceServiceImpl implements ConvergenceService{

	private ConvergencesDao dao;
	
	@Autowired
	public ConvergenceServiceImpl(ConvergencesDao dao) {
		this.dao = dao;
	}

	private static final int PAGESIZE = 10;

	public Page<Convergence> getPage(int pageNumber){
		PageRequest request = new PageRequest(pageNumber -1, PAGESIZE, Sort.Direction.ASC, "arrivedAt");
		return dao.findAll(request);
	}
	
	public Page<Convergence> findPageByName(String name, int pageNumber) {
		return dao.findByNameContaining(name, new PageRequest(pageNumber -1, PAGESIZE, Sort.Direction.ASC, "arrivedAt"));
	}
	
	public Convergence findOne(Long id) {
		Convergence convergence = dao.findOne(id);
		convergence.setDate(convergence.getArrivedAt().toString());
		return convergence;
	}
	
	public Convergence save(Convergence convergence, User user) {
		LocalDate date = LocalDate.parse(convergence.getDate());
		convergence.setArrivedAt(date);
		convergence.setUser(user);
		return dao.save(convergence);
	}

	public void delete(Long id) {
		dao.delete(id);
	}

	public boolean searchInputHasResult(FormClass formClass, int pageNumber) {
		String name = formClass.getConvergence().getName();
		Page<Convergence> page = findPageByName(name, pageNumber);
		return page.hasContent();
	}

	public Page<Convergence> getPageResultFromSearchInput(FormClass formClass, int pageNumber) {
		String name = formClass.getConvergence().getName();
		return findPageByName(name, pageNumber);
	}
}
