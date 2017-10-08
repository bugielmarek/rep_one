package com.bugielmarek.timetable.tests.RESTcontrollers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.bugielmarek.timetable.RESTcontrollers.IssueRESTController;
import com.bugielmarek.timetable.model.CaseType;
import com.bugielmarek.timetable.model.Issue;
import com.bugielmarek.timetable.model.User;
import com.bugielmarek.timetable.services.IssueService;
import com.bugielmarek.timetable.services.UserService;
import com.bugielmarek.timetable.tests.utils.TestUtils;

@RunWith(MockitoJUnitRunner.class)
public class IssueRESTControllerTest {
	
	private MockMvc mockMvc;
	
	@Mock
	private IssueService mockIssueService;
	
	@Mock
	private UserService mockUserService;
	
	private static final String LOCATION = "/api/issues/issue/";
	
	@Before
	public void setUp(){
		mockMvc = standaloneSetup(new IssueRESTController(mockIssueService, mockUserService)).build();
	}
	
	@Test
	public void testAllIssues_ListNotEmpty() throws Exception{
		when(mockIssueService.findListAllIssues()).thenReturn(createIssueList());
		
		mockMvc.perform(get("/api/issues/allissues"))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(jsonPath("$[0].id", is(1)))
					.andExpect(jsonPath("$[0].name", is("nameOne")))
					.andExpect(jsonPath("$[1].id", is(2)))
					.andExpect(jsonPath("$[1].name", is("nameTwo")))
					;
		verify(mockIssueService, times(1)).findListAllIssues();
		verifyNoMoreInteractions(mockIssueService);
	}
	
	@Test
	public void testAllIssues_ListEmpty() throws Exception{
		when(mockIssueService.findListAllIssues()).thenReturn(Collections.emptyList());
		
		mockMvc.perform(get("/api/issues/allissues"))
					.andExpect(status().isNotFound())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(jsonPath("$.code", is(4)))
					.andExpect(jsonPath("$.message", is("No Issues whatsoever found on the server")))
					;
		verify(mockIssueService, times(1)).findListAllIssues();
		verifyNoMoreInteractions(mockIssueService);
	}
	
	@Test
	public void testIssue_Found() throws Exception{
		Issue retrived = new Issue.IssueBuilder()
				.id(1L)
				.name("someName")
				.build();
		when(mockIssueService.findOne(retrived.getId())).thenReturn(retrived);
		
		mockMvc.perform(get("/api/issues/issue/{id}", retrived.getId()))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(jsonPath("$.id", is(1)))
					.andExpect(jsonPath("$.name", is(retrived.getName())))
					;
		verify(mockIssueService, times(1)).findOne(retrived.getId());
		verifyNoMoreInteractions(mockIssueService);
	}
	
	@Test
	public void testIssue_NotFound() throws Exception{
		when(mockIssueService.findOne(1L)).thenReturn(null);
		
		mockMvc.perform(get("/api/issues/issue/{id}", 1L))
					.andExpect(status().isNotFound())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(jsonPath("$.code", is(4)))
					.andExpect(jsonPath("$.message", is("Issue id:" + 1L + " not found")))
					;
		verify(mockIssueService, times(1)).findOne(1L);
		verifyNoMoreInteractions(mockIssueService);
	}
	
	@Test
	public void testMyIssues_UserFound_ListNotEmpty() throws Exception{
		String username = "someName";
		User user = new User();
		when(mockUserService.findUser()).thenReturn(user);
		when(mockIssueService.findListMyIssues(user)).thenReturn(createIssueList());
		
		mockMvc.perform(get("/api/issues/myissues/{username}", username))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(jsonPath("$[0].id", is(1)))
					.andExpect(jsonPath("$[0].name", is("nameOne")))
					.andExpect(jsonPath("$[1].id", is(2)))
					.andExpect(jsonPath("$[1].name", is("nameTwo")))
					;
		verify(mockUserService, times(2)).findUser();
		verify(mockIssueService, times(1)).findListMyIssues(user);
		verifyNoMoreInteractions(mockIssueService);
	}
	
	@Test
	public void testMyIssues_UserFound_ListEmpty() throws Exception{
		String username = "someName";
		User user = new User();
		when(mockUserService.findUser()).thenReturn(user);
		when(mockIssueService.findListMyIssues(user)).thenReturn(Collections.emptyList());
		
		mockMvc.perform(get("/api/issues/myissues/{username}", username))
					.andExpect(status().isNotFound())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(jsonPath("$.code", is(4)))
					.andExpect(jsonPath("$.message", is("No Issues found on the Server for username: " + username)))
					;
		verify(mockUserService, times(2)).findUser();
		verify(mockIssueService, times(1)).findListMyIssues(user);
		verifyNoMoreInteractions(mockIssueService);
	}
	
	@Test
	public void testMyIssues_UserNotFound() throws Exception{
		String username = "someName";
		when(mockUserService.findUser()).thenReturn(null);
		
		mockMvc.perform(get("/api/issues/myissues/{username}", username))
					.andExpect(status().isNotFound())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(jsonPath("$.code", is(4)))
					.andExpect(jsonPath("$.message", is("User with username: " + username + " not found")))
					;
		verify(mockUserService, times(1)).findUser();
		verifyNoMoreInteractions(mockIssueService);
	}
	
	@Test
	public void testFindIssues_ListNotEmpty() throws Exception{
		String name = "someName";
		when(mockIssueService.findListFromSearchInput(name)).thenReturn(createIssueList());
		
		mockMvc.perform(get("/api/issues/find/{name}", name))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(jsonPath("$[0].id", is(1)))
					.andExpect(jsonPath("$[0].name", is("nameOne")))
					.andExpect(jsonPath("$[1].id", is(2)))
					.andExpect(jsonPath("$[1].name", is("nameTwo")))
					;
		verify(mockIssueService, times(1)).findListFromSearchInput(name);
		verifyNoMoreInteractions(mockIssueService);
	}
	
	@Test
	public void testFindIssues_ListEmpty() throws Exception{
		String name = "someName";
		when(mockIssueService.findListFromSearchInput(name)).thenReturn(Collections.emptyList());
		
		mockMvc.perform(get("/api/issues/find/{name}", name))
					.andExpect(status().isNotFound())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(jsonPath("$.code", is(4)))
					.andExpect(jsonPath("$.message", is("No issues matching: " + name + " found")))
					;
		verify(mockIssueService, times(1)).findListFromSearchInput(name);
		verifyNoMoreInteractions(mockIssueService);
	}
	
	@Test
	public void testCreate_ValidIssuePassed() throws Exception{
		Issue passed = new Issue.IssueBuilder()
				.name("someName")
				.caseType(CaseType.KM)
				.sygNumber("1111/11")
				.build();
		Issue saved = new Issue.IssueBuilder()
				.id(1L)
				.name("someName")
				.caseType(CaseType.KM)
				.sygNumber("1111/11")
				.build();
		when(mockIssueService.save(passed)).thenReturn(saved);
		
		mockMvc.perform(post("/api/issues/create")
					.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
					.content(TestUtils.asJsonString(passed)))
						.andExpect(status().isCreated())
						.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
						.andExpect(jsonPath("$.id", is(1)))
						.andExpect(jsonPath("$.name", is(saved.getName())))
						.andExpect(jsonPath("$.caseType", is(saved.getCaseType().name())))
						.andExpect(jsonPath("$.sygNumber", is(saved.getSygNumber())))
						.andExpect(header().string("location", containsString(LOCATION + saved.getId().toString())))
						;
		verify(mockIssueService, times(1)).save(passed);
		verifyNoMoreInteractions(mockIssueService);
	}
	
	@Test
	public void testCreate_InvalidIssuePassed_ErrorFields_Text_Date_SygNumber_CaseType() throws Exception{
		Issue passed = new Issue.IssueBuilder()
				.name("someName")
				.sygNumber("1111/1")
				.text(TestUtils.createStringWithLength(501))
				.date("201-09-05")
				.build();
		mockMvc.perform(post("/api/issues/create")
					.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
					.content(TestUtils.asJsonString(passed)))
						.andExpect(status().isBadRequest())
						.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
						.andExpect(jsonPath("$.fieldErrors", hasSize(4)))
						.andExpect(jsonPath("$.fieldErrors[*].path", containsInAnyOrder("text", "date", "sygNumber", "caseType")))
						;
		verifyZeroInteractions(mockIssueService);
	}
	
	@Test
	public void testUpdate_IssueFound_ValidIssuePassed_UpdatedFields_Name_CaseType_SygNumber_Text() throws Exception{
		Issue retrived = new Issue.IssueBuilder()
				.id(1L)
				.name("someName")
				.caseType(CaseType.KMS)
				.sygNumber("1111/11")
				.text("someText")
				.build();
		Issue passed = new Issue.IssueBuilder()
				.id(1L)
				.name("someNewName")
				.caseType(CaseType.KMP)
				.sygNumber("2222/22")
				.date("2017-09-05")
				.text("someNewText")
				.build();
		Issue saved = new Issue.IssueBuilder()
				.id(1L)
				.name("someNewName")
				.caseType(CaseType.KMP)
				.sygNumber("2222/22")
				.text("someNewText")
				.build();
		when(mockIssueService.findOne(passed.getId())).thenReturn(retrived);
		when(mockIssueService.save(passed)).thenReturn(saved);
		
		mockMvc.perform(put("/api/issues/update/{id}", passed.getId())
					.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
					.content(TestUtils.asJsonString(passed)))
						.andExpect(status().isOk())
						.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
						.andExpect(jsonPath("$.caseType", is(saved.getCaseType().name())))
						.andExpect(jsonPath("$.sygNumber", is(saved.getSygNumber())))
						.andExpect(jsonPath("$.text", is(saved.getText())))
						.andExpect(jsonPath("$.name", is(saved.getName())))
						.andExpect(header().string("location", containsString(LOCATION + saved.getId())))
						;
		verify(mockIssueService, times(1)).findOne(passed.getId());
		verify(mockIssueService, times(1)).save(passed);
		assertEquals("Date should be null, as it's not stored in DB", null, saved.getDate());
		verifyNoMoreInteractions(mockIssueService);
	}
	
	@Test
	public void testUpdate_IssueNotFound() throws Exception{
		Issue passed = new Issue.IssueBuilder()
				.id(1L)
				.name("someNewName")
				.caseType(CaseType.KMP)
				.sygNumber("2222/22")
				.date("2017-09-05")
				.text("someNewText")
				.build();
		when(mockIssueService.findOne(passed.getId())).thenReturn(null);
		
		mockMvc.perform(put("/api/issues/update/{id}", passed.getId())
					.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
					.content(TestUtils.asJsonString(passed)))
						.andExpect(status().isNotFound())
						.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
						.andExpect(jsonPath("$.code", is(4)))
						.andExpect(jsonPath("$.message", is("Issue id:" + 1L + " not found")))
						;
		verify(mockIssueService, times(1)).findOne(passed.getId());
		verifyNoMoreInteractions(mockIssueService);
	}
	
	@Test
	public void testUpdate_IssueFound_InvalidIssuePassed_ErrorFields_Text_Date_SygNumber() throws Exception{
		Issue retrived = new Issue.IssueBuilder()
				.id(1L)
				.name("someName")
				.caseType(CaseType.KMS)
				.sygNumber("1111/11")
				.text("someText")
				.build();
		Issue passed = new Issue.IssueBuilder()
				.id(1L)
				.name("someName")
				.caseType(CaseType.KMS)
				.sygNumber("1111/1")
				.text(TestUtils.createStringWithLength(501))
				.date("201-09-05")
				.build();
		when(mockIssueService.findOne(passed.getId())).thenReturn(retrived);
		
		mockMvc.perform(put("/api/issues/update/{id}", passed.getId())
					.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
					.content(TestUtils.asJsonString(passed)))
						.andExpect(status().isBadRequest())
						.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
						.andExpect(jsonPath("$.fieldErrors", hasSize(3)))
						.andExpect(jsonPath("$.fieldErrors[*].path", containsInAnyOrder("text", "date", "sygNumber")))
						;
		verify(mockIssueService, times(1)).findOne(passed.getId());
		verifyNoMoreInteractions(mockIssueService);
	}
	
	private static List<Issue> createIssueList(){
		List<Issue> list = Arrays.asList(
				new Issue.IssueBuilder()
					.id(1L)
					.name("nameOne")
					.build(),
				new Issue.IssueBuilder()
					.id(2L)
					.name("nameTwo")
					.build());
		return list;
	}
}
