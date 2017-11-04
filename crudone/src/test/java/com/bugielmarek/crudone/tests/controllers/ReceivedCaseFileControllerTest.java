package com.bugielmarek.crudone.tests.controllers;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceView;

import com.bugielmarek.crudone.controllers.ReceivedCaseFileController;
import com.bugielmarek.crudone.models.CaseType;
import com.bugielmarek.crudone.models.ReceivedCaseFile;
import com.bugielmarek.crudone.models.FormClass;
import com.bugielmarek.crudone.services.ReceivedCaseFileService;
import com.bugielmarek.crudone.tests.utils.TestUtils;

@RunWith(MockitoJUnitRunner.class)
public class ReceivedCaseFileControllerTest {

	private MockMvc mockMvc;
	private ReceivedCaseFileController controller;
	
	@Mock
	ReceivedCaseFileService mockService;
	
	@Before
	public void setUp(){
		controller = new ReceivedCaseFileController(mockService);
	}
	
	private static final String RECEIVEDCASEFILE_ATTRIBUTE = "receivedCaseFile";
	private static final String RECEIVEDCASEFILE_VIEW = "receivedcasefile";
	private static final String RECEIVEDCASEFILES = "receivedcasefiles";
	private static final String PAGE = "page";
	private static final String CREATE_RECEIVEDCASEFILE = "createreceivedcasefile";
	
	@Test
	public void testReceivedCaseFilesPage_DefaultPage() throws Exception{
		mockMvc = standaloneSetup(controller)
				.setSingleView(new InternalResourceView(""))
				.build();
		Page<ReceivedCaseFile> page = createReceivedCaseFilePage(15);
		when(mockService.getPage(1)).thenReturn(page);
		
		mockMvc.perform(get("/receivedcasefiles"))
			.andExpect(status().isOk())
			.andExpect(view().name(RECEIVEDCASEFILES))
			.andExpect(model().attributeExists(PAGE))
			.andExpect(model().attribute(PAGE, page))
			.andExpect(model().attribute(PAGE, hasItems(page.getContent().toArray())))
			.andExpect(model().attribute(PAGE, hasItem(hasProperty("name", is("name1")))))
			;
		verify(mockService, times(1)).getPage(1);
		verifyNoMoreInteractions(mockService);
		assertEquals("size is 15", 15, page.getContent().size());
	}
	
	@Test
	public void testReceivedCaseFilesPage_PageNumberIsNotDefault() throws Exception{
		mockMvc = standaloneSetup(controller)
				.setSingleView(new InternalResourceView(""))
				.build();
		Page<ReceivedCaseFile> page = createReceivedCaseFilePage(10);
		
		when(mockService.getPage(2)).thenReturn(page);
		
		mockMvc.perform(get("/receivedcasefiles?p=2"))
			.andExpect(status().isOk())
			.andExpect(view().name(RECEIVEDCASEFILES))
			.andExpect(model().attributeExists(PAGE))
			.andExpect(model().attribute(PAGE, page))
			.andExpect(model().attribute(PAGE, hasItems(page.getContent().toArray())))
			.andExpect(model().attribute(PAGE, hasItem(hasProperty("name", is("name1")))))
			;
		verify(mockService, times(1)).getPage(2);
		verifyNoMoreInteractions(mockService);
		assertEquals("size is 10", 10, page.getContent().size());
	}
	
	@Test
	public void testCreateReceivedCaseFile_GET() throws Exception{
		mockMvc = standaloneSetup(controller)
				.setSingleView(new InternalResourceView(""))
				.build();
		ReceivedCaseFile receivedCaseFile = new ReceivedCaseFile();
		
		mockMvc.perform(get("/receivedcasefiles/createreceivedcasefile"))
			.andExpect(status().isOk())
			.andExpect(view().name(CREATE_RECEIVEDCASEFILE))
			.andExpect(model().attributeExists(RECEIVEDCASEFILE_ATTRIBUTE))
			.andExpect(model().attribute(RECEIVEDCASEFILE_ATTRIBUTE, receivedCaseFile))
			.andExpect(model().attribute(RECEIVEDCASEFILE_ATTRIBUTE, hasProperty("name", nullValue())))
			.andExpect(model().attribute(RECEIVEDCASEFILE_ATTRIBUTE, hasProperty("sygNumber", nullValue())))
			.andExpect(model().attribute(RECEIVEDCASEFILE_ATTRIBUTE, hasProperty("caseType", nullValue())))
			.andExpect(model().attribute(RECEIVEDCASEFILE_ATTRIBUTE, hasProperty("office", nullValue())))
			.andExpect(model().attribute(RECEIVEDCASEFILE_ATTRIBUTE, hasProperty("arrivedAt", nullValue())))
			.andExpect(model().attribute(RECEIVEDCASEFILE_ATTRIBUTE, hasProperty("date", nullValue())))
			;
		verifyZeroInteractions(mockService);
	}

	@Test
	public void testCreateReceivedCaseFile_POST_ValidReceivedCaseFilePassed() throws Exception{
		mockMvc = standaloneSetup(controller)
				.build();
		ReceivedCaseFile unsaved = ReceivedCaseFile.builder()
						.name("someName")
						.caseType(CaseType.KMP)
						.sygNumber("1111/11")
						.date("2017-09-06")
						.office("KS Szostek")
						.build();
		ReceivedCaseFile saved = ReceivedCaseFile.builder()
				.id(1L)
				.name("someName")
				.caseType(CaseType.KMP)
				.sygNumber("1111/11")
				.arrivedAt(LocalDate.now())
				.office("KS Szostek")
				.build();
		when(mockService.save(unsaved)).thenReturn(saved);
		
		mockMvc.perform(post("/receivedcasefiles/createreceivedcasefile")
				.param("name", unsaved.getName())
				.param("caseType", unsaved.getCaseType().name())
				.param("sygNumber", unsaved.getSygNumber())
				.param("date", unsaved.getDate())
				.param("office", unsaved.getOffice()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/receivedcasefiles/receivedcasefile/1"))
				.andExpect(model().hasNoErrors())
				.andExpect(model().attributeExists("id"))
				.andExpect(model().attribute("id", saved.getId().toString()))
				.andExpect(flash().attributeExists(RECEIVEDCASEFILE_ATTRIBUTE))
				.andExpect(flash().attribute(RECEIVEDCASEFILE_ATTRIBUTE, hasProperty("id", is(saved.getId()))))
				.andExpect(flash().attribute(RECEIVEDCASEFILE_ATTRIBUTE, hasProperty("name", is(saved.getName()))))
				.andExpect(flash().attribute(RECEIVEDCASEFILE_ATTRIBUTE, hasProperty("caseType", is(saved.getCaseType()))))
				.andExpect(flash().attribute(RECEIVEDCASEFILE_ATTRIBUTE, hasProperty("sygNumber", is(saved.getSygNumber()))))
				.andExpect(flash().attribute(RECEIVEDCASEFILE_ATTRIBUTE, hasProperty("arrivedAt", is(saved.getArrivedAt()))))
				;
		verify(mockService, times(1)).save(unsaved);
		verifyNoMoreInteractions(mockService);
	}
	
	@Test
	public void testCreateReceivedCaseFile_POST_FieldErrors_Name_CaseType_Date_SygNumber_Office() throws Exception{
		mockMvc = standaloneSetup(controller)
				.setSingleView(new InternalResourceView(""))
				.build();
		ReceivedCaseFile unsaved = ReceivedCaseFile.builder()
						.name(TestUtils.createStringWithLength(81))
						.sygNumber("1111/")
						.date("201-09-06")
						.office(TestUtils.createStringWithLength(81))
						.build();
		mockMvc.perform(post("/receivedcasefiles/createreceivedcasefile")
				.param("name", unsaved.getName())
				.param("sygNumber", unsaved.getSygNumber())
				.param("date", unsaved.getDate())
				.param("office", unsaved.getOffice()))
				.andExpect(status().isOk())
				.andExpect(view().name(CREATE_RECEIVEDCASEFILE))
				.andExpect(model().attributeHasErrors(RECEIVEDCASEFILE_ATTRIBUTE))
				.andExpect(model().errorCount(5))
				.andExpect(model().attributeHasFieldErrors(RECEIVEDCASEFILE_ATTRIBUTE, "name"))
				.andExpect(model().attributeHasFieldErrors(RECEIVEDCASEFILE_ATTRIBUTE, "caseType"))
				.andExpect(model().attributeHasFieldErrors(RECEIVEDCASEFILE_ATTRIBUTE, "sygNumber"))
				.andExpect(model().attributeHasFieldErrors(RECEIVEDCASEFILE_ATTRIBUTE, "office"))
				;
		verifyZeroInteractions(mockService);
	}
	
	@Test
	public void testReceivedCaseFile() throws Exception{
		mockMvc = standaloneSetup(controller)
				.build();
			ReceivedCaseFile receivedCaseFile = ReceivedCaseFile.builder()
							.id(1L)
							.name("someName")
							.build();
			when(mockService.findOne(1L)).thenReturn(receivedCaseFile);
			
		mockMvc.perform(get("/receivedcasefiles/receivedcasefile/{id}", receivedCaseFile.getId()))
			.andExpect(status().isOk())
			.andExpect(view().name(RECEIVEDCASEFILE_VIEW))
			.andExpect(model().attributeExists(RECEIVEDCASEFILE_ATTRIBUTE))
			.andExpect(model().attribute(RECEIVEDCASEFILE_ATTRIBUTE, receivedCaseFile))
			.andExpect(model().attribute(RECEIVEDCASEFILE_ATTRIBUTE, hasProperty("name", is(receivedCaseFile.getName()))))
			;
		verify(mockService, times(1)).findOne(1L);
		verifyNoMoreInteractions(mockService);
	}
	

	@Test
	public void testEditReceivedCaseFile() throws Exception{
		mockMvc = standaloneSetup(controller)
				.build();
		ReceivedCaseFile receivedCaseFile = ReceivedCaseFile.builder()
						.id(1L)
						.name("someName")
						.build();
		when(mockService.findOne(1L)).thenReturn(receivedCaseFile);
		
		mockMvc.perform(get("/receivedcasefiles/editreceivedcasefile/{id}", receivedCaseFile.getId()))
			.andExpect(status().isOk())
			.andExpect(view().name(CREATE_RECEIVEDCASEFILE))
			.andExpect(model().attributeExists(RECEIVEDCASEFILE_ATTRIBUTE))
			.andExpect(model().attribute(RECEIVEDCASEFILE_ATTRIBUTE, receivedCaseFile))
			.andExpect(model().attribute(RECEIVEDCASEFILE_ATTRIBUTE, hasProperty("name", is(receivedCaseFile.getName()))))
			;
		verify(mockService, times(1)).findOne(1L);
		verifyNoMoreInteractions(mockService);
	}
	 
	@Test
	public void testDeleteReceivedCaseFile() throws Exception{
		mockMvc = standaloneSetup(controller)
				.build();
			doNothing().when(mockService).delete(1L);
			
			mockMvc.perform(get("/receivedcasefiles/deletereceivedcasefile/1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/receivedcasefiles"))
				;
		verify(mockService, times(1)).delete(1L);
		verifyNoMoreInteractions(mockService);
	}
	
	
	@Test
	public void testFindReceivedCaseFile_MatchingResults_DefaultPage() throws Exception{
		mockMvc = standaloneSetup(controller)
				.build();
			Page<ReceivedCaseFile> page = createReceivedCaseFilePage(20);
			ReceivedCaseFile receivedCaseFile = ReceivedCaseFile.builder()
							.name("nameMatchinfSomeResultInDB")
							.build();
			FormClass formClass = new FormClass(receivedCaseFile);
		when(mockService.getPageResultFromSearchInput(formClass, 1)).thenReturn(page);
		
		mockMvc.perform(post("/receivedcasefiles/findreceivedcasefile")
				.param("receivedCaseFile.name", receivedCaseFile.getName()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/receivedcasefiles"))
			.andExpect(flash().attributeExists(PAGE))
			.andExpect(flash().attribute(PAGE, page))
			.andExpect(flash().attribute(PAGE, hasItems(page.getContent().toArray())))
			.andExpect(flash().attribute(PAGE, hasItem(hasProperty("name", is("name19")))))
			;
		verify(mockService, times(1)).getPageResultFromSearchInput(formClass, 1);
		verifyNoMoreInteractions(mockService);
		assertEquals("size is 20", 20, page.getContent().size());
	}
	
	private Page<ReceivedCaseFile> createReceivedCaseFilePage(int count) {
		
		List<ReceivedCaseFile> list = new ArrayList<>();
		for(int i=0; i<count; i++){
			list.add(ReceivedCaseFile.builder()
							.name("name" + i)
							.build());
					}
		Page<ReceivedCaseFile> page = new PageImpl<>(list);
		return page;
	}
}
