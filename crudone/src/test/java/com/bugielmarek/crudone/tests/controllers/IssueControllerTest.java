package com.bugielmarek.crudone.tests.controllers;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
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

import com.bugielmarek.crudone.controllers.IssueController;
import com.bugielmarek.crudone.models.CaseType;
import com.bugielmarek.crudone.models.FormClass;
import com.bugielmarek.crudone.models.Issue;
import com.bugielmarek.crudone.models.User;
import com.bugielmarek.crudone.services.IssueService;
import com.bugielmarek.crudone.services.UserService;
import com.bugielmarek.crudone.tests.utils.TestUtils;

@RunWith(MockitoJUnitRunner.class)
public class IssueControllerTest {

	private MockMvc mockMvc;
	private IssueController controller;
	
	@Mock
	IssueService mockService;
	
	@Mock
	UserService mockUserService;
	
	private static final String ISSUES = "issues";
	private static final String CREATE_ISSUE = "createissue";
	private static final String PAGE = "page";
	private static final String ISSUE = "issue";

	@Before
	public void setUp(){
		controller = new IssueController(mockService, mockUserService);
	}
	
	@Test
	public void testIssuesPage_PageNumberHasDefaultValue() throws Exception{
		mockMvc = standaloneSetup(controller)
				.setSingleView(new InternalResourceView(""))
				.build();
			Page<Issue> page = createIssuesPage(10);
		when(mockService.getPage(1)).thenReturn(page);
		
		mockMvc.perform(get("/issues"))
			.andExpect(status().isOk())
			.andExpect(view().name(ISSUES))
			.andExpect(model().attributeExists(PAGE))
			.andExpect(model().attribute(PAGE, page))
			.andExpect(model().attribute(PAGE, hasItems(page.getContent().toArray())))
			.andExpect(model().attribute(PAGE, hasItem(
					allOf(hasProperty("id", is(1L)), hasProperty("name", is("name1"))))))
			.andExpect(model().attribute(PAGE, hasItem(
					allOf(hasProperty("id", is(10L)), hasProperty("name", is("name10"))))))
			;
		verify(mockService, times(1)).getPage(1);
		verifyNoMoreInteractions(mockService);
		assertEquals("List size is 10", 10, page.getContent().size());
	}
	
	@Test
	public void testIssuesPage_PageNumberIsNotDefault() throws Exception{
		mockMvc = standaloneSetup(controller)
				.setSingleView(new InternalResourceView(""))
				.build();
				Page<Issue> page = createIssuesPage(10);
		when(mockService.getPage(2)).thenReturn(page);
		
		mockMvc.perform(get("/issues?p=2"))
			.andExpect(status().isOk())
			.andExpect(view().name(ISSUES))
			.andExpect(model().attributeExists(PAGE))
			.andExpect(model().attribute(PAGE, page))
			.andExpect(model().attribute(PAGE, hasItems(page.getContent().toArray())))
			;
		verify(mockService, times(1)).getPage(2);
		verifyNoMoreInteractions(mockService);
		assertEquals("List size is 10", 10, page.getContent().size());
	}
	
	@Test
	public void testCreateIssueGET() throws Exception{
		mockMvc = standaloneSetup(controller)
				.setSingleView(new InternalResourceView(""))
				.build();
		mockMvc.perform(get("/issues/createissue"))
			.andExpect(status().isOk())
			.andExpect(view().name(CREATE_ISSUE))
			.andExpect(model().attributeExists(ISSUE))
			.andExpect(model().attribute(ISSUE, hasProperty("id", nullValue())))
			.andExpect(model().attribute(ISSUE, hasProperty("name", nullValue())))
			.andExpect(model().attribute(ISSUE, hasProperty("caseType", nullValue())))
			.andExpect(model().attribute(ISSUE, hasProperty("sygNumber", nullValue())))
			.andExpect(model().attribute(ISSUE, hasProperty("deadline", nullValue())))
			.andExpect(model().attribute(ISSUE, hasProperty("date", nullValue())))
			.andExpect(model().attribute(ISSUE, hasProperty("user", nullValue())))
			.andExpect(model().attribute(ISSUE, hasProperty("text", nullValue())))
			;
		verifyZeroInteractions(mockService);
	}
	
	@Test
	public void testIssue() throws Exception {
		mockMvc = standaloneSetup(controller)
				.build();
			Issue retrived = Issue.builder()
					.id(1L)
					.name("someName")
					.build();
		when(mockService.findOne(retrived.getId())).thenReturn(retrived);
			
		mockMvc.perform(get("/issues/issue/{id}", retrived.getId()))
				.andExpect(status().isOk())
				.andExpect(view().name(ISSUE))
				.andExpect(model().attributeExists(ISSUE))
				.andExpect(model().attribute(ISSUE, retrived))
				.andExpect(model().attribute(ISSUE, hasProperty("id", is(retrived.getId()))))
				.andExpect(model().attribute(ISSUE, hasProperty("name", is(retrived.getName()))))
				;
		verify(mockService, times(1)).findOne(retrived.getId());
		verifyNoMoreInteractions(mockService);
	}
	
	@Test
	public void testEditIssue() throws Exception {
		mockMvc = standaloneSetup(controller)
				.build();
			Issue retrived = Issue.builder()
				.id(1L)
				.name("someName")
				.build();
		when(mockService.findOne(retrived.getId())).thenReturn(retrived);
			
		mockMvc.perform(get("/issues/editissue/{id}", retrived.getId()))
				.andExpect(status().isOk())
				.andExpect(view().name(CREATE_ISSUE))
				.andExpect(model().attributeExists(ISSUE))
				.andExpect(model().attribute(ISSUE, retrived))
				.andExpect(model().attribute(ISSUE, hasProperty("id", is(retrived.getId()))))
				.andExpect(model().attribute(ISSUE, hasProperty("name", is(retrived.getName()))))
				;
		verify(mockService, times(1)).findOne(retrived.getId());
		verifyNoMoreInteractions(mockService);
	}
	
	@Test
	public void testDeleteIssue() throws Exception {
		mockMvc = standaloneSetup(controller)
				.build();
		doNothing().when(mockService).delete(1L);
			
		mockMvc.perform(get("/issues/deleteissue/1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/issues"))
				;
		verify(mockService, times(1)).delete(1L);
		verifyNoMoreInteractions(mockService);
	}
	
	@Test
	public void testCreateIssuePOST_NoValidationErrors() throws Exception{
		mockMvc = standaloneSetup(controller)
				.build();
			Issue unsaved = Issue.builder()
					.name("someName")
					.caseType(CaseType.KMS)
					.sygNumber("1111/17")
					.text("someText")
					.date("2017-09-05")
					.build();
			User user = User.builder()
				.id(1L)
				.username("BugMar")
				.build();
			Issue saved = Issue.builder()
					.id(1L)
					.name("someName")
					.caseType(CaseType.KMS)
					.sygNumber("1111/17")
					.text("someText")
					.deadline(LocalDate.now())
					.user(user)
					.build();
		when(mockUserService.findUser()).thenReturn(user);	
		when(mockService.save(unsaved, mockUserService.findUser())).thenReturn(saved);
		
		mockMvc.perform(post("/issues/createissue")
			.param("name", unsaved.getName())
			.param("caseType", unsaved.getCaseType().name())
			.param("sygNumber", unsaved.getSygNumber())
			.param("text", unsaved.getText())
			.param("date", unsaved.getDate()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/issues/issue/1"))
			.andExpect(model().attributeExists("id"))
			.andExpect(model().attribute("id", saved.getId().toString()))
			.andExpect(flash().attributeExists(ISSUE))
			.andExpect(flash().attribute(ISSUE, saved))
			.andExpect(flash().attribute(ISSUE, hasProperty("id", is(saved.getId()))))
			.andExpect(flash().attribute(ISSUE, hasProperty("name", is(saved.getName()))))
			.andExpect(flash().attribute(ISSUE, hasProperty("caseType", is(saved.getCaseType()))))
			.andExpect(flash().attribute(ISSUE, hasProperty("sygNumber", is(saved.getSygNumber()))))
			.andExpect(flash().attribute(ISSUE, hasProperty("text", is(saved.getText()))))
			.andExpect(flash().attribute(ISSUE, hasProperty("deadline", is(saved.getDeadline()))))
			.andExpect(flash().attribute(ISSUE, hasProperty("user", is(saved.getUser()))))
			.andExpect(flash().attributeCount(1))
			;
		verify(mockService, times(1)).save(unsaved, user);
		verifyNoMoreInteractions(mockService);
	}
	
	@Test
	public void testCreateIssuePOST_ValidationErrors_Fields_Text_Date_SygNumber() throws Exception{
		mockMvc = standaloneSetup(controller)
				.setSingleView(new InternalResourceView(""))
				.build();
			Issue unsaved = Issue.builder()
				.name("someName")
				.caseType(CaseType.KMS)
				.sygNumber("1111/1")
				.text(TestUtils.createStringWithLength(501))
				.date("201-09-05")
				.build();
			mockMvc.perform(post("/issues/createissue")
				.param("name", unsaved.getName())
				.param("caseType", unsaved.getCaseType().name())
				.param("sygNumber", unsaved.getSygNumber())
				.param("text", unsaved.getText())
				.param("date", unsaved.getDate()))
				.andExpect(status().isOk())
				.andExpect(view().name(CREATE_ISSUE))
				.andExpect(model().attributeExists(ISSUE))
				.andExpect(model().attribute(ISSUE, unsaved))
				.andExpect(model().attributeHasErrors(ISSUE))
				.andExpect(model().attributeHasFieldErrors(ISSUE, "text"))
				.andExpect(model().attributeHasFieldErrors(ISSUE, "date"))
				.andExpect(model().attributeHasFieldErrors(ISSUE, "sygNumber"))
				.andExpect(model().hasErrors())
				.andExpect(model().errorCount(3))
				;
		verifyZeroInteractions(mockService);
	}
	
	@Test
	public void testFindIssue_NameOnly_MatchingResultsDefaultPage() throws Exception{
		mockMvc = standaloneSetup(controller)
				.build();
			Issue formIssue = Issue.builder()
					.name("nameMatchingResultsInDB")
					.build();
			FormClass formClass = new FormClass(formIssue);
			Page<Issue> page = createIssuesPage(14);
			
		when(mockService.getPageResultFromSearchInput(formClass, 1)).thenReturn(page);
			
		mockMvc.perform(post("/issues/findissue")
				.param("issue.name", formIssue.getName()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/issues"))
				.andExpect(flash().attributeExists(PAGE))
				.andExpect(flash().attribute(PAGE, page))
				.andExpect(flash().attribute(PAGE, hasItems(page.getContent().toArray())))
				.andExpect(flash().attributeCount(1))
				;
		verify(mockService, times(1)).getPageResultFromSearchInput(formClass, 1);
		verifyNoMoreInteractions(mockService);
	}
	
	@Test
	public void testFindIssue_NameOnly_MatchingResultsPageIsNotDefault() throws Exception{
		mockMvc = standaloneSetup(controller)
				.build();
			Issue formIssue = Issue.builder()
					.name("nameMatchingResultsInDB")
					.build();
			FormClass formClass = new FormClass(formIssue);
			Page<Issue> page = createIssuesPage(9);
			
		when(mockService.getPageResultFromSearchInput(formClass, 2)).thenReturn(page);
		
		mockMvc.perform(post("/issues/findissue?p=2")
				.param("issue.name", formIssue.getName()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/issues"))
				.andExpect(flash().attributeExists(PAGE))
				.andExpect(flash().attribute(PAGE, page))
				.andExpect(flash().attribute(PAGE, hasItems(page.getContent().toArray())))
				.andExpect(flash().attributeCount(1))
				;
		verify(mockService, times(1)).getPageResultFromSearchInput(formClass, 2);
		verifyNoMoreInteractions(mockService);
	}
	
	private Page<Issue> createIssuesPage(int count){
		
		List<Issue> list = new ArrayList<>();
		for(int i=0; i<count; i++){
			list.add(Issue.builder()
							.id(1L + i)
							.name("name" + (1 + i))
							.build());
				}
		Page<Issue> page = new PageImpl<Issue>(list);
		return page;
	}
}
