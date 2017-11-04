package com.bugielmarek.crudone.services.impls;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bugielmarek.crudone.daos.ReceivedcasefileDao;
import com.bugielmarek.crudone.models.ReceivedCaseFile;
import com.bugielmarek.crudone.services.ReceivedCaseFileService;
import com.bugielmarek.crudone.models.FormClass;

import lombok.extern.slf4j.Slf4j;

/**
 * Provides the base model implementation for ReceivedCaseFile service. Allows
 * creating, reading, updating, deleting, searching receivedCaseFiles.
 * @author Bugiel Marek
 *
 */
@Slf4j
@Service
public class ReceivedCaseFileServiceImpl implements ReceivedCaseFileService {

	private ReceivedcasefileDao dao;

	@Autowired
	public ReceivedCaseFileServiceImpl(ReceivedcasefileDao dao) {
		this.dao = dao;
	}

	private static final int PAGESIZE = 10;

	public Page<ReceivedCaseFile> getPage(int pageNumber) {
		ReceivedCaseFileServiceImpl.log.info("getPage() invoked, pageNumber={}", pageNumber);
		return dao.findAll(new PageRequest(pageNumber - 1, PAGESIZE, Sort.Direction.ASC, "arrivedAt"));
	}

	public ReceivedCaseFile findOne(Long id) {
		ReceivedCaseFileServiceImpl.log.info("findOne() invoked for id={}", id);
		ReceivedCaseFile receivedCaseFile = dao.findOne(id);
		if (receivedCaseFile == null) {
			ReceivedCaseFileServiceImpl.log.info("Returning NULL");
			return null;
		}
		ReceivedCaseFileServiceImpl.log.info("Returning ReceivedCaseFile({})", receivedCaseFile);
		receivedCaseFile.setDate(receivedCaseFile.getArrivedAt().toString());
		return receivedCaseFile;
	}

	public ReceivedCaseFile save(ReceivedCaseFile receivedCaseFile) {
		LocalDate date = LocalDate.parse(receivedCaseFile.getDate());
		receivedCaseFile.setArrivedAt(date);
		ReceivedCaseFile saved = dao.save(receivedCaseFile);
		ReceivedCaseFileServiceImpl.log.info("save() invoked, saved ReceivedCaseFile({})", saved);
		return saved;
	}

	public void delete(Long id) {
		ReceivedCaseFileServiceImpl.log.info("delete() invoked, deleting ReceivedCaseFile with id={}", id);
		dao.delete(id);
	}

	public Page<ReceivedCaseFile> getPageResultFromSearchInput(FormClass formClass, int pageNumber) {
		String name = formClass.getReceivedCaseFile().getName();
		ReceivedCaseFileServiceImpl.log.info("getPageResultFromSearchInput() invoked, ReceivedCaseFile(name={})", name);
		return dao.findByNameContaining(name,
				new PageRequest(pageNumber - 1, PAGESIZE, Sort.Direction.ASC, "arrivedAt"));
	}
}
