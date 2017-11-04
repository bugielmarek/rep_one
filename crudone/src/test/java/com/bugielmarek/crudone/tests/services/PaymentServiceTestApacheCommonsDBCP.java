package com.bugielmarek.crudone.tests.services;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.bugielmarek.crudone.models.CaseType;
import com.bugielmarek.crudone.models.FormClass;
import com.bugielmarek.crudone.models.Payment;
import com.bugielmarek.crudone.services.PaymentService;
import com.bugielmarek.crudone.services.UserService;

@ActiveProfiles("apacheCommonsDbcp")
@ContextConfiguration(locations = { "classpath:testsContext.xml" })
@RunWith(SpringRunner.class)
public class PaymentServiceTestApacheCommonsDBCP {

	private JdbcTemplate jdbc;

	@Autowired
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return this.jdbc = new JdbcTemplate(dataSource);
	}

	private static final String DELETE_PAYMENTS = "delete from payments";
	private static final String PAYMENTS = "payments";

	@Before
	public void setUp() {
		jdbc.execute(DELETE_PAYMENTS);
	}

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private UserService userService;

	
	@Test
	@WithMockUser
	public void testGetPage() {
	// GIVEN	
				Payment payment1 = Payment.builder()
						.name("someName")
						.caseType(CaseType.KM)
						.sygNumber("1967/16")
						.date("2017-08-14")
						.build();
				Payment payment2 = Payment.builder()
						.name("someName")
						.caseType(CaseType.KM)
						.sygNumber("1967/16")
						.date("2017-08-14")
						.build();
	// WHEN
			assertEquals("There should be no payments in DB", 0, paymentService.getPage(1).getContent().size());
				paymentService.save(payment1, userService.findUser());
				paymentService.save(payment2, userService.findUser());
	// THEN			
			assertEquals("There should be two payments in DB", 2, paymentService.getPage(1).getContent().size());
	}
	
	@Test
	@WithMockUser
	public void testFindByCaseTypeSygNumber_ResultsFound() {
	// GIVEN		
				Payment payment1 = Payment.builder()
						.name("someName")
						.caseType(CaseType.KM)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment2 = Payment.builder()
						.name("someOtherName")
						.caseType(CaseType.KMS)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment3 = Payment.builder()
						.name("yetAnotherName")
						.caseType(CaseType.KMP)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment4 = Payment.builder()
						.name("someName")
						.caseType(CaseType.KM)
						.sygNumber("1111/12")
						.date("2017-08-14")
						.build();
				Payment payment5 = Payment.builder()
						.name("someOtherName")
						.caseType(CaseType.KMS)
						.sygNumber("1111/22")
						.date("2017-08-14")
						.build();
				String sygNumber = "1111/11";
				CaseType caseType = CaseType.KM;
	// WHEN
			paymentService.save(payment1, userService.findUser());
			paymentService.save(payment2, userService.findUser());
			paymentService.save(payment3, userService.findUser());
			paymentService.save(payment4, userService.findUser());
			paymentService.save(payment5, userService.findUser());
			assertEquals(5, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
	// THEN
			assertEquals("Payments should match", payment1, paymentService.findByCaseTypeSygNumber(caseType, sygNumber));
	}
	
	@Test
	@WithMockUser
	public void testSavePayment() {
	// GIVEN
				Payment payment = Payment.builder()
						.name("someName")
						.caseType(CaseType.KM)
						.sygNumber("1967/16")
						.date("2017-08-14")
						.build();
	// WHEN
			assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbc, "payments", "name = '" + payment.getName() + "'"));
				paymentService.save(payment, userService.findUser());
	// THEN			
			assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbc, "payments", "name = '" + payment.getName() + "'"));
	}
	
	@Test
	@WithMockUser
	public void testDeletePayment() {
	// GIVEN
				Payment payment = Payment.builder()
						.name("someName")
						.caseType(CaseType.KM)
						.sygNumber("1967/16")
						.date("2017-08-14")
						.build();
	// WHEN
			assertEquals(0, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				paymentService.save(payment, userService.findUser());
			assertEquals(1, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
	// THEN		
				paymentService.delete(payment.getId());
			assertEquals(0, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
	}
	
	@Test
	@WithMockUser
	public void testFindOne_PaymentFound() {
	// GIVEN
				Payment payment = Payment.builder()
						.name("someName")
						.caseType(CaseType.KMS)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
	// WHEN	
			assertEquals(0, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				paymentService.save(payment, userService.findUser());
			assertEquals(1, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
	// THEN		
			assertEquals("Payment should exist in DB", payment, paymentService.findOne(payment.getId()));
	}
	
	@Test
	@WithMockUser
	public void testFindOne_NotFound() {
	// GIVEN 
				Payment payment = Payment.builder()
						.name("someName")
						.caseType(CaseType.KMS)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
	// WHEN	
			assertEquals(0, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				paymentService.save(payment, userService.findUser());
			assertEquals(1, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
	// THEN		
			assertEquals("Payment should exist in DB", null, paymentService.findOne(2L));
	}
	
	@Test
	@WithMockUser
	public void testGetPageResultFromSearchInput_BothFieldsEmpty() {
	// GIVEN
				Payment payment = Payment.builder()
						.name("")
						.caseType(CaseType.KMS)
						.sygNumber("")
						.date("2017-08-14")
						.build();
				FormClass formClass = new FormClass(payment);
				Payment payment1 = Payment.builder()
						.name("name")
						.caseType(CaseType.KMS)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment2 = Payment.builder()
						.name("someName")
						.caseType(CaseType.KMP)
						.sygNumber("6666/66")
						.date("2017-08-14")
						.build();
				Payment payment3 = Payment.builder()
						.name("someOtherName")
						.caseType(CaseType.KM)
						.sygNumber("9999/15")
						.date("2017-08-14")
						.build();
	// WHEN
			assertEquals(0, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				paymentService.save(payment1, userService.findUser());
				paymentService.save(payment2, userService.findUser());
				paymentService.save(payment3, userService.findUser());
			assertEquals(3, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				List<Payment> matchingPayments = Arrays.asList(payment1, payment2, payment3);
				List<Payment> retrivedPayments = paymentService.getPageResultFromSearchInput(formClass, 1).getContent();
	// THEN			
			assertEquals("There should be three payments matching", matchingPayments.size(), retrivedPayments.size());
			assertEquals("Custom made matching list should be equal to retrived one", matchingPayments, retrivedPayments);
	}
	
	@Test
	@WithMockUser
	public void testGetPageResultFromSearchInput_NameEntered() {
	// GIVEN
				Payment payment = Payment.builder()
						.name("sir")
						.caseType(CaseType.KMS)
						.sygNumber("")
						.date("2017-08-14")
						.build();
				FormClass formClass = new FormClass(payment);
				Payment payment1 = Payment.builder()
						.name("desiredName")
						.caseType(CaseType.KMS)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment2 = Payment.builder()
						.name("xDESIREDNAMEx")
						.caseType(CaseType.KMP)
						.sygNumber("6666/66")
						.date("2017-08-14")
						.build();
				Payment payment3 = Payment.builder()
						.name("someOtherName")
						.caseType(CaseType.KM)
						.sygNumber("9999/15")
						.date("2017-08-14")
						.build();
	// WHEN
			assertEquals(0, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				paymentService.save(payment1, userService.findUser());
				paymentService.save(payment2, userService.findUser());
				paymentService.save(payment3, userService.findUser());
			assertEquals(3, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				List<Payment> matchingPayments = Arrays.asList(payment1, payment2);
				List<Payment> retrivedPayments = paymentService.getPageResultFromSearchInput(formClass, 1).getContent();
	// THEN
			assertEquals("There should be two payments matching", matchingPayments.size(), retrivedPayments.size());
			assertEquals("Custom made matching list should be equal to retrived one", matchingPayments, retrivedPayments);
	}
	
	@Test
	@WithMockUser
	public void testGetPageResultFromSearchInput_SygNumberCaseTypeEntered() {
	// GIVEN
				Payment payment = Payment.builder()
						.name("")
						.caseType(CaseType.KMS)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				FormClass formClass = new FormClass(payment);
				Payment payment1 = Payment.builder()
						.name("desiredName")
						.caseType(CaseType.KMS)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment2 = Payment.builder()
						.name("xDESIREDNAMEx")
						.caseType(CaseType.KMP)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment3 = Payment.builder()
						.name("someOtherName")
						.caseType(CaseType.KM)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment4 = Payment.builder()
						.name("desiredName")
						.caseType(CaseType.KMS)
						.sygNumber("2222/22")
						.date("2017-08-14")
						.build();
	// WHEN
			assertEquals(0, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				paymentService.save(payment1, userService.findUser());
				paymentService.save(payment2, userService.findUser());
				paymentService.save(payment3, userService.findUser());
				paymentService.save(payment4, userService.findUser());
			assertEquals(4, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				List<Payment> matchingPayments = Arrays.asList(payment1);
				List<Payment> retrivedPayments = paymentService.getPageResultFromSearchInput(formClass, 1).getContent();
	// THEN
			assertEquals("There should be two payments matching", matchingPayments.size(), retrivedPayments.size());
			assertEquals("Custom made matching list should be equal to retrived one", matchingPayments, retrivedPayments);
	}
	
	@Test
	@WithMockUser
	public void testGetPageResultFromSearchInput_AllFieldsEntered() {
	// GIVEN
				Payment payment = Payment.builder()
						.name("sir")
						.caseType(CaseType.KMS)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				FormClass formClass = new FormClass(payment);
				Payment payment1 = Payment.builder()
						.name("desiredName")
						.caseType(CaseType.KMS)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment2 = Payment.builder()
						.name("xDESIREDNAMEx")
						.caseType(CaseType.KMP)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment3 = Payment.builder()
						.name("DESIREDNAME")
						.caseType(CaseType.KM)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment4 = Payment.builder()
						.name("desiredName")
						.caseType(CaseType.KMS)
						.sygNumber("2222/22")
						.date("2017-08-14")
						.build();
	// WHEN			
			assertEquals(0, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				paymentService.save(payment1, userService.findUser());
				paymentService.save(payment2, userService.findUser());
				paymentService.save(payment3, userService.findUser());
				paymentService.save(payment4, userService.findUser());
			assertEquals(4, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				List<Payment> matchingPayments = Arrays.asList(payment1);
				List<Payment> retrivedPayments = paymentService.getPageResultFromSearchInput(formClass, 1).getContent();
	// THEN			
			assertEquals("There should be two payments matching", matchingPayments.size(), retrivedPayments.size());
			assertEquals("Custom made matching list should be equal to retrived one", matchingPayments, retrivedPayments);
	}
}
