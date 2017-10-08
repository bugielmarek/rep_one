package com.bugielmarek.timetable.tests.controllers;

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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.View;

import com.bugielmarek.timetable.controllers.PaymentController;
import com.bugielmarek.timetable.model.CaseType;
import com.bugielmarek.timetable.model.FormClass;
import com.bugielmarek.timetable.model.Payment;
import com.bugielmarek.timetable.model.User;
import com.bugielmarek.timetable.services.PaymentService;
import com.bugielmarek.timetable.services.UserService;
import com.bugielmarek.timetable.tests.utils.TestUtils;

public class PaymentControllerTest {

	@Mock
	PaymentService mockService;
	
	@Mock
	UserService mockUserService;
	
	@Mock
	View view;

	@Captor
	ArgumentCaptor<Integer> intCaptor;

	private PaymentController controller;
	private MockMvc mockMvc;

	private static final String PAYMENT = "payment";
	private static final String PAYMENTS = "payments";
	private static final String CREATE_PAYMENT = "createpayment";
	private static final String PAYMENT_EXISTS = "paymentExists";
	private static final String HOME = "home";
	private static final String PAGE = "page";

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		controller = new PaymentController(mockService, mockUserService);
	}

	@Test
	public void testPaymentsPage_PageNumberHasDefaultValue() throws Exception {
		mockMvc = standaloneSetup(controller)
				.setSingleView(view)
				.build();
		Page<Payment> page = createPaymentPage(10);
		when(mockService.getPage(1)).thenReturn(page);
		
		mockMvc.perform(get("/payments"))
				.andExpect(status().isOk())
				.andExpect(view().name(PAYMENTS))
				.andExpect(model().attributeExists(PAGE))
				.andExpect(model().attribute(PAGE, hasItems(page.getContent().toArray())))
				.andExpect(model().attribute(PAGE,
						hasItem(allOf(hasProperty("id", is(1L)), hasProperty("name", is("name1"))))))
				.andExpect(model().attribute(PAGE,
						hasItem(allOf(hasProperty("id", is(10L)), hasProperty("name", is("name10"))))))
				;
		verify(mockService).getPage(intCaptor.capture());
		assertEquals("pageNumber is 1", 1, (int) intCaptor.getValue());
		assertEquals("List size is 10", 10, page.getContent().size());
		verify(mockService, times(1)).getPage(1);
		verifyNoMoreInteractions(mockService);
	}

	@Test
	public void testPaymentsPage_PageNumberIsNotDefault() throws Exception {
		mockMvc = standaloneSetup(controller)
				.setSingleView(view)
				.build();
		Page<Payment> page = createPaymentPage(10);
		when(mockService.getPage(2)).thenReturn(page);
		
		mockMvc.perform(get("/payments?p=2"))
				.andExpect(status().isOk())
				.andExpect(view().name(PAYMENTS))
				.andExpect(model().attributeExists(PAGE))
				.andExpect(model().attribute(PAGE, hasItems(page.getContent().toArray())))
				;
		verify(mockService, times(1)).getPage(2);
		verifyNoMoreInteractions(mockService);
	}

	@Test
	public void testCreatePayment_GET() throws Exception {
		mockMvc = standaloneSetup(controller)
				.setSingleView(view)
				.build();
		
		mockMvc.perform(get("/payments/createpayment"))
				.andExpect(status().isOk())
				.andExpect(view().name(CREATE_PAYMENT))
				.andExpect(model().attributeExists(PAYMENT))
				.andExpect(model().attribute(PAYMENT, hasProperty("id", nullValue())))
				.andExpect(model().attribute(PAYMENT, hasProperty("name", nullValue())))
				.andExpect(model().attribute(PAYMENT, hasProperty("caseType", nullValue())))
				.andExpect(model().attribute(PAYMENT, hasProperty("sygNumber", nullValue())))
				.andExpect(model().attribute(PAYMENT, hasProperty("date", nullValue())))
				.andExpect(model().attribute(PAYMENT, hasProperty("deadline", nullValue())))
				.andExpect(model().attribute(PAYMENT, hasProperty("user", nullValue())))
				;
		verifyZeroInteractions(mockService);
	}

	@Test
	public void testCreatePayment_POST__ValidPaymentPassed_NonExistingInDB() throws Exception {
		
		mockMvc = standaloneSetup(controller)
				.build();
		Payment unsaved = new Payment.PaymentBuilder()
				.name("someName")
				.caseType(CaseType.KM)
				.sygNumber("2017/17")
				.date("2017-09-04")
				.build();
		User user = new User.UserBuilder()
			.id(1L)
			.username("BugMar")
			.build();
		Payment saved = new Payment.PaymentBuilder()
				.id(1L)
				.name("someName")
				.caseType(CaseType.KM)
				.sygNumber("2017/17")
				.deadline(LocalDate.now())
				.user(user)
				.build();
		when(mockService.isNotValid(unsaved)).thenReturn(false);
		when(mockUserService.findUser()).thenReturn(user);
		when(mockService.save(unsaved, mockUserService.findUser())).thenReturn(saved);

		mockMvc.perform(post("/payments/createpayment")
				.param("name", unsaved.getName())
				.param("date", unsaved.getDate())
				.param("caseType", unsaved.getCaseType().name())
				.param("sygNumber", unsaved.getSygNumber()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/payments/payment/1"))
				.andExpect(model().attributeExists("id"))
				.andExpect(model().attribute("id", saved.getId().toString()))
				.andExpect(flash().attributeExists(PAYMENT))
				.andExpect(flash().attribute(PAYMENT, hasProperty("id", is(saved.getId()))))
				.andExpect(flash().attribute(PAYMENT, hasProperty("name", is(saved.getName()))))
				.andExpect(flash().attribute(PAYMENT, hasProperty("caseType", is(saved.getCaseType()))))
				.andExpect(flash().attribute(PAYMENT, hasProperty("sygNumber", is(saved.getSygNumber()))))
				.andExpect(flash().attribute(PAYMENT, hasProperty("user", is(saved.getUser()))))
				;
		verify(mockService, times(1)).isNotValid(unsaved);
		verify(mockService, times(1)).save(unsaved, user);
		verifyNoMoreInteractions(mockService);
	}

	@Test
	public void testCreatePaymentPOST_ExistingInDB() throws Exception {
		
		mockMvc = standaloneSetup(controller)
				.setSingleView(view)
				.build();
		Payment unsaved = new Payment.PaymentBuilder()
				.name("someName")
				.caseType(CaseType.KM)
				.sygNumber("2017/17")
				.date("2017-09-04")
				.build();
		Payment existing = new Payment.PaymentBuilder()
				.id(1L).name("someOtherName")
				.caseType(CaseType.KM)
				.sygNumber("2017/17")
				.deadline(LocalDate.now())
				.user(new User.UserBuilder()
						.id(1L).username("BugMar")
						.build())
				.build();
		when(mockService.isNotValid(unsaved)).thenReturn(true);
		when(mockService.findByCaseTypeSygNumber(unsaved.getCaseType(), unsaved.getSygNumber())).thenReturn(existing);

		mockMvc.perform(
				post("/payments/createpayment")
				.param("name", unsaved.getName())
				.param("date", unsaved.getDate())
				.param("caseType", unsaved.getCaseType().name())
				.param("sygNumber", unsaved.getSygNumber()))
				.andExpect(view().name(CREATE_PAYMENT))
				.andExpect(model().attributeExists(PAYMENT))
				.andExpect(model().attribute(PAYMENT, hasProperty("id", is(nullValue()))))
				.andExpect(model().attribute(PAYMENT, hasProperty("name", is(unsaved.getName()))))
				.andExpect(model().attribute(PAYMENT, hasProperty("caseType", is(unsaved.getCaseType()))))
				.andExpect(model().attribute(PAYMENT, hasProperty("sygNumber", is(unsaved.getSygNumber()))))
				.andExpect(model().attribute(PAYMENT, hasProperty("date", is(unsaved.getDate()))))
				.andExpect(model().attributeExists(PAYMENT_EXISTS))
				.andExpect(model().attribute(PAYMENT_EXISTS, hasProperty("id", is(existing.getId()))))
				.andExpect(model().attribute(PAYMENT_EXISTS, hasProperty("name", is(existing.getName()))))
				.andExpect(model().attribute(PAYMENT_EXISTS, hasProperty("caseType", is(existing.getCaseType()))))
				.andExpect(model().attribute(PAYMENT_EXISTS, hasProperty("sygNumber", is(existing.getSygNumber()))))
				.andExpect(model().attribute(PAYMENT_EXISTS, hasProperty("deadline", is(existing.getDeadline()))))
				.andExpect(model().attribute(PAYMENT_EXISTS, hasProperty("user", is(existing.getUser()))))
				;
		verify(mockService, times(1)).isNotValid(unsaved);
		verify(mockService, times(1)).findByCaseTypeSygNumber(unsaved.getCaseType(), unsaved.getSygNumber());
		verifyNoMoreInteractions(mockService);
	}

	@Test
	public void testCreatePaymentPOST_FieldErrors_Name_Date_SygNumber_CaseType() throws Exception {
		
		mockMvc = standaloneSetup(controller)
				.setSingleView(view)
				.build();
		Payment unsaved = new Payment.PaymentBuilder()
				.name(TestUtils.createStringWithLength(81))
				.sygNumber("2017/")
				.date("201-09-04")
				.build();
		mockMvc.perform(post("/payments/createpayment")
				.param("name", unsaved.getName())
				.param("date", unsaved.getDate())
				.param("sygNumber", unsaved.getSygNumber()))
				.andExpect(status().isOk())
				.andExpect(view().name(CREATE_PAYMENT))
				.andExpect(model().attributeExists(PAYMENT))
				.andExpect(model().attributeHasFieldErrors(PAYMENT, "name"))
				.andExpect(model().attributeHasFieldErrors(PAYMENT, "date"))
				.andExpect(model().attributeHasFieldErrors(PAYMENT, "sygNumber"))
				.andExpect(model().attributeHasFieldErrors(PAYMENT, "caseType"))
				.andExpect(model().errorCount(4))
				;
		verifyZeroInteractions(mockService);
	}

	@Test
	public void testPayment() throws Exception {
		
		mockMvc = standaloneSetup(controller)
				.build();
		Payment retrived = new Payment.PaymentBuilder()
				.id(1L).name("someName")
				.caseType(CaseType.KM)
				.sygNumber("2017/17")
				.deadline(LocalDate.now())
				.user(new User.UserBuilder()
						.id(1L).username("BugMar")
						.build())
				.build();
		when(mockService.findOne(retrived.getId())).thenReturn(retrived);

		mockMvc.perform(get("/payments/payment/{id}", retrived.getId()))
				.andExpect(status().isOk())
				.andExpect(view().name(PAYMENT))
				.andExpect(model().attributeExists(PAYMENT))
				.andExpect(model().attribute(PAYMENT, retrived))
				.andExpect(model().attribute(PAYMENT, hasProperty("name", is(retrived.getName()))))
				.andExpect(model().attribute(PAYMENT, hasProperty("date", is(nullValue()))))
				.andExpect(model().attribute(PAYMENT, hasProperty("caseType", is(retrived.getCaseType()))))
				.andExpect(model().attribute(PAYMENT, hasProperty("sygNumber", is(retrived.getSygNumber()))))
				.andExpect(model().attribute(PAYMENT, hasProperty("deadline", is(retrived.getDeadline()))))
				.andExpect(model().attribute(PAYMENT, hasProperty("user", is(retrived.getUser()))))
				;
		verify(mockService, times(1)).findOne(retrived.getId());
		verifyNoMoreInteractions(mockService);
	}

	@Test
	public void testEditPayment() throws Exception {
		mockMvc = standaloneSetup(controller)
				.build();
		Payment retrived = new Payment.PaymentBuilder()
				.id(1L)
				.name("someName")
				.caseType(CaseType.KM)
				.sygNumber("2017/17")
				.deadline(LocalDate.now())
				.user(new User.UserBuilder()
						.id(1L)
						.username("BugMar")
						.build())
				.build();
		when(mockService.findOne(retrived.getId())).thenReturn(retrived);

		mockMvc.perform(get("/payments/editpayment/{id}", retrived.getId()))
				.andExpect(status().isOk())
				.andExpect(view().name(CREATE_PAYMENT))
				.andExpect(model().attributeExists(PAYMENT))
				.andExpect(model().attribute(PAYMENT, retrived))
				.andExpect(model().attribute(PAYMENT, hasProperty("name", is(retrived.getName()))))
				.andExpect(model().attribute(PAYMENT, hasProperty("date", is(nullValue()))))
				.andExpect(model().attribute(PAYMENT, hasProperty("caseType", is(retrived.getCaseType()))))
				.andExpect(model().attribute(PAYMENT, hasProperty("sygNumber", is(retrived.getSygNumber()))))
				.andExpect(model().attribute(PAYMENT, hasProperty("deadline", is(retrived.getDeadline()))))
				.andExpect(model().attribute(PAYMENT, hasProperty("user", is(retrived.getUser()))))
				;
		verify(mockService, times(1)).findOne(retrived.getId());
		verifyNoMoreInteractions(mockService);
	}

	@Test
	public void testDeletePayment() throws Exception {
		
		mockMvc = standaloneSetup(controller)
				.build();
		doNothing().when(mockService).delete(1L);
		
		mockMvc.perform(get("/payments/deletepayment/{id}", 1L))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/payments"))
				;
		verify(mockService, times(1)).delete(1L);
		verifyNoMoreInteractions(mockService);
	}

	@Test
	public void testfindPayment_FieldNameOnly_MatchingResultsDefaultPage() throws Exception {
		
		mockMvc = standaloneSetup(controller)
				.build();
		Payment formPayment = new Payment.PaymentBuilder()
				.name("nameMarchingResultsInDB")
				.build();
		FormClass formClass = new FormClass(formPayment);
		Page<Payment> page = createPaymentPage(10);

		when(mockService.searchInputHasResult(formClass, 1)).thenReturn(true);
		when(mockService.getPageResultFromSearchInput(formClass, 1)).thenReturn(page);

		mockMvc.perform(post("/payments/findpayment")
				.param("payment.name", formPayment.getName()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/payments"))
				.andExpect(flash().attributeExists(PAGE))
				.andExpect(flash().attribute(PAGE, page))
				.andExpect(flash().attribute(PAGE, hasItems(page.getContent().toArray())))
				.andExpect(flash().attributeCount(1))
				;
		verify(mockService, times(1)).searchInputHasResult(formClass, 1);
		verify(mockService, times(1)).getPageResultFromSearchInput(formClass, 1);
	}

	@Test
	public void testFindPayment_FieldNameOnly_NoMatchingResults() throws Exception {
		
		mockMvc = standaloneSetup(controller)
				.build();
		Payment formPayment = new Payment.PaymentBuilder()
				.name("nameMarchingNoResultsInDB")
				.build();
		FormClass formClass = new FormClass(formPayment);
		when(mockService.searchInputHasResult(formClass, 1)).thenReturn(false);

		mockMvc.perform(post("/payments/findpayment")
				.param("payment.name", formPayment.getName()))
				.andExpect(status().isOk())
				.andExpect(view().name(HOME))
				.andExpect(model().attributeExists("formClass"))
				.andExpect(model().attributeHasErrors("formClass"))
				.andExpect(model().attributeHasFieldErrors("formClass", "payment.name"))
				;
		verify(mockService, times(1)).searchInputHasResult(formClass, 1);
	}

	private Page<Payment> createPaymentPage(int count) {
		List<Payment> paymentList = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			paymentList.add(new Payment.PaymentBuilder().id(1L + i).name("name" + (1 + i)).build());
		}
		Page<Payment> paymentPage = new PageImpl<>(paymentList);
		return paymentPage;
	}
}
