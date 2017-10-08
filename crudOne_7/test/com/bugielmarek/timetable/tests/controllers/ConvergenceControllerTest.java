package com.bugielmarek.timetable.tests.controllers;

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

import com.bugielmarek.timetable.controllers.ConvergenceController;
import com.bugielmarek.timetable.model.CaseType;
import com.bugielmarek.timetable.model.Convergence;
import com.bugielmarek.timetable.model.FormClass;
import com.bugielmarek.timetable.model.User;
import com.bugielmarek.timetable.services.ConvergenceService;
import com.bugielmarek.timetable.services.UserService;
import com.bugielmarek.timetable.tests.utils.TestUtils;

@RunWith(MockitoJUnitRunner.class)
public class ConvergenceControllerTest {

	private MockMvc mockMvc;
	private ConvergenceController controller;
	
	@Mock
	ConvergenceService mockService;
	
	@Mock
	UserService mockUserService;
	
	@Before
	public void setUp(){
		controller = new ConvergenceController(mockService, mockUserService);
	}
	
	private static final String CONVERGENCE = "convergence";
	private static final String CONVERGENCES = "convergences";
	private static final String PAGE = "page";
	private static final String CREATE_CONVERGENCE = "createconvergence";
	private static final String HOME = "home";
	
	@Test
	public void testConvergencesPage_DefaultPage() throws Exception{
		mockMvc = standaloneSetup(controller)
				.setSingleView(new InternalResourceView(""))
				.build();
		Page<Convergence> page = createConvergencesPage(15);
		when(mockService.getPage(1)).thenReturn(page);
		
		mockMvc.perform(get("/convergences"))
			.andExpect(status().isOk())
			.andExpect(view().name(CONVERGENCES))
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
	public void testConvergencesPage_PageNumberIsNotDefault() throws Exception{
		mockMvc = standaloneSetup(controller)
				.setSingleView(new InternalResourceView(""))
				.build();
		Page<Convergence> page = createConvergencesPage(10);
		
		when(mockService.getPage(2)).thenReturn(page);
		
		mockMvc.perform(get("/convergences?p=2"))
			.andExpect(status().isOk())
			.andExpect(view().name(CONVERGENCES))
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
	public void testCreateConvergence_GET() throws Exception{
		mockMvc = standaloneSetup(controller)
				.setSingleView(new InternalResourceView(""))
				.build();
		Convergence convergence = new Convergence();
		
		mockMvc.perform(get("/convergences/createconvergence"))
			.andExpect(status().isOk())
			.andExpect(view().name(CREATE_CONVERGENCE))
			.andExpect(model().attributeExists(CONVERGENCE))
			.andExpect(model().attribute(CONVERGENCE, convergence))
			.andExpect(model().attribute(CONVERGENCE, hasProperty("name", nullValue())))
			.andExpect(model().attribute(CONVERGENCE, hasProperty("sygNumber", nullValue())))
			.andExpect(model().attribute(CONVERGENCE, hasProperty("caseType", nullValue())))
			.andExpect(model().attribute(CONVERGENCE, hasProperty("office", nullValue())))
			.andExpect(model().attribute(CONVERGENCE, hasProperty("arrivedAt", nullValue())))
			.andExpect(model().attribute(CONVERGENCE, hasProperty("date", nullValue())))
			;
		verifyZeroInteractions(mockService);
	}

	@Test
	public void testCreateConvergence_POST_ValidConvergencePassed() throws Exception{
		mockMvc = standaloneSetup(controller)
				.build();
		Convergence unsaved = new Convergence.ConvergenceBuilder()
						.name("someName")
						.caseType(CaseType.KMP)
						.sygNumber("1111/11")
						.date("2017-09-06")
						.office("KS Szostek")
						.build();
		User user = new User.UserBuilder()
				.id(1L)
				.username("BugMar")
				.build();
		Convergence saved = new Convergence.ConvergenceBuilder()
				.id(1L)
				.name("someName")
				.caseType(CaseType.KMP)
				.sygNumber("1111/11")
				.arrivedAt(LocalDate.now())
				.office("KS Szostek")
				.user(user)
				.build();
		when(mockUserService.findUser()).thenReturn(user);
		when(mockService.save(unsaved, mockUserService.findUser())).thenReturn(saved);
		
		mockMvc.perform(post("/convergences/createconvergence")
				.param("name", unsaved.getName())
				.param("caseType", unsaved.getCaseType().name())
				.param("sygNumber", unsaved.getSygNumber())
				.param("date", unsaved.getDate())
				.param("office", unsaved.getOffice()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/convergences/convergence/1"))
				.andExpect(model().hasNoErrors())
				.andExpect(model().attributeExists("id"))
				.andExpect(model().attribute("id", saved.getId().toString()))
				.andExpect(flash().attributeExists(CONVERGENCE))
				.andExpect(flash().attribute(CONVERGENCE, hasProperty("id", is(saved.getId()))))
				.andExpect(flash().attribute(CONVERGENCE, hasProperty("name", is(saved.getName()))))
				.andExpect(flash().attribute(CONVERGENCE, hasProperty("caseType", is(saved.getCaseType()))))
				.andExpect(flash().attribute(CONVERGENCE, hasProperty("sygNumber", is(saved.getSygNumber()))))
				.andExpect(flash().attribute(CONVERGENCE, hasProperty("arrivedAt", is(saved.getArrivedAt()))))
				.andExpect(flash().attribute(CONVERGENCE, hasProperty("user", is(saved.getUser()))))
				;
		verify(mockUserService, times(2)).findUser();
		verify(mockService, times(1)).save(unsaved, user);
		verifyNoMoreInteractions(mockService);
	}
	
	@Test
	public void testCreateConvergence_POST_FieldErrors_Name_CaseType_Date_SygNumber_Office() throws Exception{
		mockMvc = standaloneSetup(controller)
				.setSingleView(new InternalResourceView(""))
				.build();
		Convergence unsaved = new Convergence.ConvergenceBuilder()
						.name(TestUtils.createStringWithLength(81))
						.sygNumber("1111/")
						.date("201-09-06")
						.office(TestUtils.createStringWithLength(81))
						.build();
		mockMvc.perform(post("/convergences/createconvergence")
				.param("name", unsaved.getName())
				.param("sygNumber", unsaved.getSygNumber())
				.param("date", unsaved.getDate())
				.param("office", unsaved.getOffice()))
				.andExpect(status().isOk())
				.andExpect(view().name(CREATE_CONVERGENCE))
				.andExpect(model().attributeHasErrors(CONVERGENCE))
				.andExpect(model().errorCount(5))
				.andExpect(model().attributeHasFieldErrors(CONVERGENCE, "name"))
				.andExpect(model().attributeHasFieldErrors(CONVERGENCE, "caseType"))
				.andExpect(model().attributeHasFieldErrors(CONVERGENCE, "sygNumber"))
				.andExpect(model().attributeHasFieldErrors(CONVERGENCE, "office"))
				;
		verifyZeroInteractions(mockService);
	}
	
	@Test
	public void testConvergence() throws Exception{
		mockMvc = standaloneSetup(controller)
				.build();
			Convergence convergence = new Convergence.ConvergenceBuilder()
							.id(1L)
							.name("someName")
							.build();
			when(mockService.findOne(1L)).thenReturn(convergence);
			
		mockMvc.perform(get("/convergences/convergence/{id}", convergence.getId()))
			.andExpect(status().isOk())
			.andExpect(view().name(CONVERGENCE))
			.andExpect(model().attributeExists(CONVERGENCE))
			.andExpect(model().attribute(CONVERGENCE, convergence))
			.andExpect(model().attribute(CONVERGENCE, hasProperty("name", is(convergence.getName()))))
			;
		verify(mockService, times(1)).findOne(1L);
		verifyNoMoreInteractions(mockService);
	}
	

	@Test
	public void testEditConvergence() throws Exception{
		mockMvc = standaloneSetup(controller)
				.build();
		Convergence convergence = new Convergence.ConvergenceBuilder()
						.id(1L)
						.name("someName")
						.build();
		when(mockService.findOne(1L)).thenReturn(convergence);
		
		mockMvc.perform(get("/convergences/editconvergence/{id}", convergence.getId()))
			.andExpect(status().isOk())
			.andExpect(view().name(CREATE_CONVERGENCE))
			.andExpect(model().attributeExists(CONVERGENCE))
			.andExpect(model().attribute(CONVERGENCE, convergence))
			.andExpect(model().attribute(CONVERGENCE, hasProperty("name", is(convergence.getName()))))
			;
		verify(mockService, times(1)).findOne(1L);
		verifyNoMoreInteractions(mockService);
	}
	 
	@Test
	public void testDeleteConvergence() throws Exception{
		mockMvc = standaloneSetup(controller)
				.build();
			doNothing().when(mockService).delete(1L);
			
			mockMvc.perform(get("/convergences/deleteconvergence/1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/convergences"))
				;
		verify(mockService, times(1)).delete(1L);
		verifyNoMoreInteractions(mockService);
	}
	
	
	@Test
	public void testFindConvergence_MatchingResults_DefaultPage() throws Exception{
		mockMvc = standaloneSetup(controller)
				.build();
			Page<Convergence> page = createConvergencesPage(20);
			Convergence convergence = new Convergence.ConvergenceBuilder()
							.name("nameMatchinfSomeResultInDB")
							.build();
			FormClass formClass = new FormClass(convergence);
		when(mockService.searchInputHasResult(formClass, 1)).thenReturn(true);
		when(mockService.getPageResultFromSearchInput(formClass, 1)).thenReturn(page);
		
		mockMvc.perform(post("/convergences/findconvergence")
				.param("convergence.name", convergence.getName()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/convergences"))
			.andExpect(flash().attributeExists(PAGE))
			.andExpect(flash().attribute(PAGE, page))
			.andExpect(flash().attribute(PAGE, hasItems(page.getContent().toArray())))
			.andExpect(flash().attribute(PAGE, hasItem(hasProperty("name", is("name19")))))
			;
		verify(mockService, times(1)).searchInputHasResult(formClass, 1);
		verify(mockService, times(1)).getPageResultFromSearchInput(formClass, 1);
		verifyNoMoreInteractions(mockService);
		assertEquals("size is 20", 20, page.getContent().size());
	}
	
	@Test
	public void testFindConvergence_NoMatchingResults() throws Exception{
		mockMvc = standaloneSetup(controller)
				.build();
			Convergence convergence = new Convergence.ConvergenceBuilder()
							.name("nameMatchingNoResultsInDB")
							.build();
			FormClass formClass = new FormClass(convergence);
		when(mockService.searchInputHasResult(formClass, 1)).thenReturn(false);
		
		mockMvc.perform(post("/convergences/findconvergence")
				.param("convergence.name", convergence.getName()))
			.andExpect(status().isOk())
			.andExpect(view().name(HOME))
			.andExpect(model().attributeHasErrors("formClass"))
			.andExpect(model().attributeHasFieldErrors("formClass", "convergence.name"))
			;
		verify(mockService, times(1)).searchInputHasResult(formClass, 1);
		verifyNoMoreInteractions(mockService);
	}
	
	private Page<Convergence> createConvergencesPage(int count) {
		
		List<Convergence> list = new ArrayList<>();
		for(int i=0; i<count; i++){
			list.add(new Convergence.ConvergenceBuilder()
							.name("name" + i)
							.build());
					}
		Page<Convergence> page = new PageImpl<>(list);
		return page;
	}
}
