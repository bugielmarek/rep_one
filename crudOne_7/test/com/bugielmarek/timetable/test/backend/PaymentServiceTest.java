package com.bugielmarek.timetable.test.backend;

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

import com.bugielmarek.timetable.model.CaseType;
import com.bugielmarek.timetable.model.FormClass;
import com.bugielmarek.timetable.model.Payment;
import com.bugielmarek.timetable.services.PaymentService;
import com.bugielmarek.timetable.services.UserService;

@ActiveProfiles("apacheCommonsDbcp")
@ContextConfiguration(locations = { "classpath:com/bugielmarek/timetable/config/testConfig.xml",
		"classpath:com/bugielmarek/timetable/config/rootAppContext.xml",
		"classpath:com/bugielmarek/timetable/config/security-context.xml" })
@RunWith(SpringRunner.class)
public class PaymentServiceTest {

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
			assertEquals("There should be no payments in DB", 0, paymentService.getPage(1).getContent().size());
				paymentService.save(payment1, userService.findUser());
				paymentService.save(payment2, userService.findUser());
			assertEquals("There should be two payments in DB", 2, paymentService.getPage(1).getContent().size());
	}
	
	@Test
	@WithMockUser
	public void testFindPageByName_ResultsFound() {
		
				Payment payment1 = Payment.builder()
						.name("desiredName")
						.caseType(CaseType.KM)
						.sygNumber("1967/16")
						.date("2017-08-14")
						.build();
				Payment payment2 = Payment.builder()
						.name("xDESIREDNAMEx")
						.caseType(CaseType.KM)
						.sygNumber("1967/16")
						.date("2017-08-14")
						.build();
				Payment payment3 = Payment.builder()
						.name("someOtherName")
						.caseType(CaseType.KM)
						.sygNumber("1967/16")
						.date("2017-08-14")
						.build();
				String searchInput = "sir";
				paymentService.save(payment1, userService.findUser());
				paymentService.save(payment2, userService.findUser());
				paymentService.save(payment3, userService.findUser());
			assertEquals("There should be three payments in DB", 3, paymentService.getPage(1).getContent().size());
				List<Payment> matchingPayments = Arrays.asList(payment1, payment2);
				List<Payment> sortableList = paymentService.findPageByName(searchInput, 1).getContent();
			assertEquals("There should be only two results matching", 2, paymentService.findPageByName(searchInput, 1).getContent().size());
			assertEquals("Custom matching list should be equal to retrived one", matchingPayments, sortableList);
	}
	
	@Test
	@WithMockUser
	public void testFindPageByName_NoResults() {
		
				Payment payment1 = Payment.builder()
						.name("someName")
						.caseType(CaseType.KM)
						.sygNumber("1967/16")
						.date("2017-08-14")
						.build();
				Payment payment2 = Payment.builder()
						.name("someOtherName")
						.caseType(CaseType.KM)
						.sygNumber("1967/16")
						.date("2017-08-14")
						.build();
				Payment payment3 = Payment.builder()
						.name("yetAnotherName")
						.caseType(CaseType.KM)
						.sygNumber("1967/16")
						.date("2017-08-14")
						.build();
				String searchInput = "sir";
				paymentService.save(payment1, userService.findUser());
				paymentService.save(payment2, userService.findUser());
				paymentService.save(payment3, userService.findUser());
			assertEquals("There should be three payments in DB", 3, paymentService.getPage(1).getContent().size());
			assertEquals("There should be no matching result", 0, paymentService.findPageByName(searchInput, 1).getContent().size());
	}
	
	@Test
	@WithMockUser
	public void testFindPageByCaseTypeSygNumber_ResultsFound() {
		
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
				Payment payment6 = Payment.builder()
						.name("someName")
						.caseType(CaseType.KM)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				String sygNumber = "1111/11";
				CaseType caseType = CaseType.KM;
				paymentService.save(payment1, userService.findUser());
				paymentService.save(payment2, userService.findUser());
				paymentService.save(payment3, userService.findUser());
				paymentService.save(payment4, userService.findUser());
				paymentService.save(payment5, userService.findUser());
				paymentService.save(payment6, userService.findUser());
			assertEquals(6, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				List<Payment> matchingList = Arrays.asList(payment1, payment6);
				List<Payment> retrivedPayments = paymentService.findPageByCaseTypeSygNumber(caseType, sygNumber, 1).getContent();
			assertEquals("There should be two matching result", 2, paymentService.findPageByCaseTypeSygNumber(caseType, sygNumber, 1).getContent().size());
			assertEquals("Custom made matching list should be equal to retrived one", matchingList, retrivedPayments);
	}
	
	@Test
	@WithMockUser
	public void testFindPageByCaseTypeSygNumber_NoResults() {
		
				Payment payment1 = Payment.builder()
						.name("someName")
						.caseType(CaseType.KM)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment2 = Payment.builder()
						.name("someOtherName")
						.caseType(CaseType.KMS)
						.sygNumber("2222/22")
						.date("2017-08-14")
						.build();
				Payment payment3 = Payment.builder()
						.name("yetAnotherName")
						.caseType(CaseType.KMP)
						.sygNumber("2222/22")
						.date("2017-08-14")
						.build();
				Payment payment4 = Payment.builder()
						.name("someName")
						.caseType(CaseType.KM)
						.sygNumber("2222/12")
						.date("2017-08-14")
						.build();
				String sygNumber = "2222/22";
				CaseType caseType = CaseType.KM;
				paymentService.save(payment1, userService.findUser());
				paymentService.save(payment2, userService.findUser());
				paymentService.save(payment3, userService.findUser());
				paymentService.save(payment4, userService.findUser());
			assertEquals(4, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
			assertEquals("There should be no matching result", 0, paymentService.findPageByCaseTypeSygNumber(caseType, sygNumber, 1).getContent().size());
	}
	
	@Test
	@WithMockUser
	public void testFindPageByNameCaseTypeSygNumber_ResultsFound() {
		
				Payment payment1 = Payment.builder()
						.name("desiredName")
						.caseType(CaseType.KM)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment2 = Payment.builder()
						.name("xDESIREDNAMEx")
						.caseType(CaseType.KM)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment3 = Payment.builder()
						.name("notWantedName")
						.caseType(CaseType.KM)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment4 = Payment.builder()
						.name("desiredName")
						.caseType(CaseType.KMS)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment5 = Payment.builder()
						.name("xDESIREDNAMEx")
						.caseType(CaseType.KMS)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment6 = Payment.builder()
						.name("notWantedName")
						.caseType(CaseType.KMS)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment7 = Payment.builder()
						.name("desiredName")
						.caseType(CaseType.KMP)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment8 = Payment.builder()
						.name("xDESIREDNAMEx")
						.caseType(CaseType.KMP)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment9 = Payment.builder()
						.name("notWantedName")
						.caseType(CaseType.KMP)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment10 = Payment.builder()
						.name("desiredName")
						.caseType(CaseType.KM)
						.sygNumber("2222/22")
						.date("2017-08-14")
						.build();
				String sygNumber = "1111/11";
				CaseType caseType = CaseType.KM;
				String name = "sir";
				paymentService.save(payment1, userService.findUser());
				paymentService.save(payment2, userService.findUser());
				paymentService.save(payment3, userService.findUser());
				paymentService.save(payment4, userService.findUser());
				paymentService.save(payment5, userService.findUser());
				paymentService.save(payment6, userService.findUser());
				paymentService.save(payment7, userService.findUser());
				paymentService.save(payment8, userService.findUser());
				paymentService.save(payment9, userService.findUser());
				paymentService.save(payment10, userService.findUser());
			assertEquals(10, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				List<Payment> matchingList = Arrays.asList(payment1, payment2);
				List<Payment> sortableList = paymentService.findPageByNameCaseTypeSygNumber(name, caseType, sygNumber, 1).getContent();
			assertEquals("There should be two matching result", 2, paymentService.findPageByNameCaseTypeSygNumber(name, caseType, sygNumber, 1).getContent().size());
			assertEquals("Custom made matchig list should be equal to retrived one", matchingList, sortableList);
	}
	@Test
	@WithMockUser
	public void testFindPageByNameCaseTypeSygNumber_NoResults() {
		
				Payment payment1 = Payment.builder()
						.name("desiredName")
						.caseType(CaseType.KM)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment2 = Payment.builder()
						.name("xDESIREDNAMEx")
						.caseType(CaseType.KM)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment3 = Payment.builder()
						.name("notWantedName")
						.caseType(CaseType.KM)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment4 = Payment.builder()
						.name("desiredName")
						.caseType(CaseType.KMS)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment5 = Payment.builder()
						.name("xDESIREDNAMEx")
						.caseType(CaseType.KMS)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment6 = Payment.builder()
						.name("notWantedName")
						.caseType(CaseType.KMS)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment7 = Payment.builder()
						.name("desiredName")
						.caseType(CaseType.KMP)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment8 = Payment.builder()
						.name("xDESIREDNAMEx")
						.caseType(CaseType.KMP)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment9 = Payment.builder()
						.name("notWantedName")
						.caseType(CaseType.KMP)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment10 = Payment.builder()
						.name("desiredName")
						.caseType(CaseType.KM)
						.sygNumber("2222/22")
						.date("2017-08-14")
						.build();
				String sygNumber = "1111/11";
				CaseType caseType = CaseType.KM;
				String name = "dir";
				paymentService.save(payment1, userService.findUser());
				paymentService.save(payment2, userService.findUser());
				paymentService.save(payment3, userService.findUser());
				paymentService.save(payment4, userService.findUser());
				paymentService.save(payment5, userService.findUser());
				paymentService.save(payment6, userService.findUser());
				paymentService.save(payment7, userService.findUser());
				paymentService.save(payment8, userService.findUser());
				paymentService.save(payment9, userService.findUser());
				paymentService.save(payment10, userService.findUser());
			assertEquals(10, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
			assertEquals("There should be no matching result", 0, paymentService.findPageByNameCaseTypeSygNumber(name, caseType, sygNumber, 1).getContent().size());
	}
	
	
	
	@Test
	@WithMockUser
	public void testFindByCaseTypeSygNumber_ResultsFound() {
			
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
			paymentService.save(payment1, userService.findUser());
			paymentService.save(payment2, userService.findUser());
			paymentService.save(payment3, userService.findUser());
			paymentService.save(payment4, userService.findUser());
			paymentService.save(payment5, userService.findUser());
			assertEquals(5, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
			assertEquals("Payments should match", payment1, paymentService.findByCaseTypeSygNumber(caseType, sygNumber));
	}
	
	@Test
	@WithMockUser
	public void testSavePayment() {

				Payment payment = Payment.builder()
						.name("someName")
						.caseType(CaseType.KM)
						.sygNumber("1967/16")
						.date("2017-08-14")
						.build();
			assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbc, "payments", "name = '" + payment.getName() + "'"));
				paymentService.save(payment, userService.findUser());
			assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbc, "payments", "name = '" + payment.getName() + "'"));
	}
	
	@Test
	@WithMockUser
	public void testDeletePayment() {

				Payment payment = Payment.builder()
						.name("someName")
						.caseType(CaseType.KM)
						.sygNumber("1967/16")
						.date("2017-08-14")
						.build();
			assertEquals(0, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				paymentService.save(payment, userService.findUser());
			assertEquals(1, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				paymentService.delete(payment.getId());
			assertEquals(0, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
	}
	
	@Test
	@WithMockUser
	public void testExistsPayment_CaseType_SygNumber_Id_ReturnsTrue() {

				Payment payment1 = Payment.builder()
						.name("someName")
						.caseType(CaseType.KM)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment2 = Payment.builder()
						.name("someName")
						.caseType(CaseType.KM)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
			assertEquals(0, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				paymentService.save(payment1, userService.findUser());
				paymentService.save(payment2, userService.findUser());
			assertEquals(2, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
			assertEquals("Payment should exist in DB", true, paymentService.exists(payment1.getCaseType(), payment1.getSygNumber(), payment1.getId()));
	}
	
	@Test
	@WithMockUser
	public void testExistsPayment_CaseType_SygNumber_Id_ReturnsFalse() {

				Payment payment1 = Payment.builder()
						.name("someName")
						.caseType(CaseType.KM)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment2 = Payment.builder()
						.name("someName")
						.caseType(CaseType.KM)
						.sygNumber("2222/11")
						.date("2017-08-14")
						.build();
				Payment payment3 = Payment.builder()
						.name("someName")
						.caseType(CaseType.KMS)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment4 = Payment.builder()
						.name("someName")
						.caseType(CaseType.KMP)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
			assertEquals(0, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				paymentService.save(payment1, userService.findUser());
				paymentService.save(payment2, userService.findUser());
				paymentService.save(payment3, userService.findUser());
				paymentService.save(payment4, userService.findUser());
			assertEquals(4, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
			assertEquals("Payment should not exist in DB", false, paymentService.exists(payment1.getCaseType(), payment1.getSygNumber(), payment1.getId()));
	}
	
	@Test
	@WithMockUser
	public void testExistsPayment_CaseType_SygNumber_ReturnsTrue() {

				Payment payment1 = Payment.builder()
						.name("someName")
						.caseType(CaseType.KM)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment2 = Payment.builder()
						.name("someName")
						.caseType(CaseType.KM)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
			assertEquals(0, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				paymentService.save(payment1, userService.findUser());
				paymentService.save(payment2, userService.findUser());
			assertEquals(2, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
			assertEquals("Payment should exist in DB", true, paymentService.exists(payment1.getCaseType(), payment1.getSygNumber()));
	}
	
	@Test
	@WithMockUser
	public void testExistsPayment_CaseType_SygNumber_ReturnsFalse() {

				Payment payment1 = Payment.builder()
						.name("someName")
						.caseType(CaseType.KMS)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment2 = Payment.builder()
						.name("someName")
						.caseType(CaseType.KMP)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment3 = Payment.builder()
						.name("someName")
						.caseType(CaseType.KM)
						.sygNumber("2111/11")
						.date("2017-08-14")
						.build();
				String sygNumber = "1111/11";
				CaseType caseType = CaseType.KM;
			
			assertEquals(0, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				paymentService.save(payment1, userService.findUser());
				paymentService.save(payment2, userService.findUser());
				paymentService.save(payment3, userService.findUser());
			assertEquals(3, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
			assertEquals("Payment should not exist in DB", false, paymentService.exists(caseType, sygNumber));
	}
	
	@Test
	@WithMockUser
	public void testFindOne_PaymentFound() {

				Payment payment = Payment.builder()
						.name("someName")
						.caseType(CaseType.KMS)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
			
			assertEquals(0, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				paymentService.save(payment, userService.findUser());
			assertEquals(1, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
			assertEquals("Payment should exist in DB", payment, paymentService.findOne(payment.getId()));
	}
	
	@Test
	@WithMockUser
	public void testFindOne_NotFound() {

				Payment payment = Payment.builder()
						.name("someName")
						.caseType(CaseType.KMS)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
			
			assertEquals(0, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				paymentService.save(payment, userService.findUser());
			assertEquals(1, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
			assertEquals("Payment should exist in DB", null, paymentService.findOne(2L));
	}
	
	
	@Test
	public void testSearchInputHasResult_ReturnsTrue_Name_SygNumber_BothFieldsEmpty() {

				Payment payment = Payment.builder()
						.name("")
						.caseType(CaseType.KMS)
						.sygNumber("")
						.date("2017-08-14")
						.build();
				FormClass formClass = new FormClass(payment);
			assertEquals("Fields 'name' and 'sygNumber' are empty", true, paymentService.searchInputHasResult(formClass, 1));
	}
	
	@Test
	@WithMockUser
	public void testSearchInputHasResult_ReturnsTrue_SygNumberAndCaseTypeEntered() {

				Payment payment = Payment.builder()
						.name("")
						.caseType(CaseType.KMS)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				FormClass formClass = new FormClass(payment);
				Payment payment1 = Payment.builder()
						.name("someName")
						.caseType(CaseType.KMS)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment2 = Payment.builder()
						.name("someName")
						.caseType(CaseType.KMS)
						.sygNumber("2111/11")
						.date("2017-08-14")
						.build();
				Payment payment3 = Payment.builder()
						.name("someName")
						.caseType(CaseType.KM)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment4 = Payment.builder()
						.name("someName")
						.caseType(CaseType.KMP)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
			assertEquals(0, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				paymentService.save(payment1, userService.findUser());
				paymentService.save(payment2, userService.findUser());
				paymentService.save(payment3, userService.findUser());
				paymentService.save(payment4, userService.findUser());
			assertEquals(4, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
			assertEquals("There should be matching payment in DB", true, paymentService.searchInputHasResult(formClass, 1));
	}
	
	@Test
	@WithMockUser
	public void testSearchInputHasResult_ReturnsTrue_NameEntered() {

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
						.caseType(CaseType.KMS)
						.sygNumber("2111/11")
						.date("2017-08-14")
						.build();
				Payment payment3 = Payment.builder()
						.name("someName")
						.caseType(CaseType.KM)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
				Payment payment4 = Payment.builder()
						.name("someOtherName")
						.caseType(CaseType.KMP)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
			assertEquals(0, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				paymentService.save(payment1, userService.findUser());
				paymentService.save(payment2, userService.findUser());
				paymentService.save(payment3, userService.findUser());
				paymentService.save(payment4, userService.findUser());
			assertEquals(4, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
			assertEquals("There should be matching payment in DB", true, paymentService.searchInputHasResult(formClass, 1));
	}
	
	@Test
	@WithMockUser
	public void testSearchInputHasResult_ReturnsTrue_AllFieldsEntered() {

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
						.caseType(CaseType.KMS)
						.sygNumber("2111/11")
						.date("2017-08-14")
						.build();
				Payment payment3 = Payment.builder()
						.name("someName")
						.caseType(CaseType.KM)
						.sygNumber("1111/11")
						.date("2017-08-14")
						.build();
			assertEquals(0, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				paymentService.save(payment1, userService.findUser());
				paymentService.save(payment2, userService.findUser());
				paymentService.save(payment3, userService.findUser());
			assertEquals(3, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
			assertEquals("There should be matching payment in DB", true, paymentService.searchInputHasResult(formClass, 1));
	}

	
	@Test
	@WithMockUser
	public void testGetPageResultFromSearchInput_BothFieldsEmpty() {

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
			assertEquals(0, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				paymentService.save(payment1, userService.findUser());
				paymentService.save(payment2, userService.findUser());
				paymentService.save(payment3, userService.findUser());
			assertEquals(3, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				List<Payment> matchingPayments = Arrays.asList(payment1, payment2, payment3);
				List<Payment> retrivedPayments = paymentService.getPageResultFromSearchInput(formClass, 1).getContent();
			assertEquals("There should be three payments matching", matchingPayments.size(), retrivedPayments.size());
			assertEquals("Custom made matching list should be equal to retrived one", matchingPayments, retrivedPayments);
	}
	
	@Test
	@WithMockUser
	public void testGetPageResultFromSearchInput_NameEntered() {

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
			assertEquals(0, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				paymentService.save(payment1, userService.findUser());
				paymentService.save(payment2, userService.findUser());
				paymentService.save(payment3, userService.findUser());
			assertEquals(3, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				List<Payment> matchingPayments = Arrays.asList(payment1, payment2);
				List<Payment> retrivedPayments = paymentService.getPageResultFromSearchInput(formClass, 1).getContent();
			assertEquals("There should be two payments matching", matchingPayments.size(), retrivedPayments.size());
			assertEquals("Custom made matching list should be equal to retrived one", matchingPayments, retrivedPayments);
	}
	
	@Test
	@WithMockUser
	public void testGetPageResultFromSearchInput_SygNumberCaseTypeEntered() {

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
			assertEquals(0, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				paymentService.save(payment1, userService.findUser());
				paymentService.save(payment2, userService.findUser());
				paymentService.save(payment3, userService.findUser());
				paymentService.save(payment4, userService.findUser());
			assertEquals(4, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				List<Payment> matchingPayments = Arrays.asList(payment1);
				List<Payment> retrivedPayments = paymentService.getPageResultFromSearchInput(formClass, 1).getContent();
			assertEquals("There should be two payments matching", matchingPayments.size(), retrivedPayments.size());
			assertEquals("Custom made matching list should be equal to retrived one", matchingPayments, retrivedPayments);
	}
	
	@Test
	@WithMockUser
	public void testGetPageResultFromSearchInput_AllFieldsEntered() {

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
			assertEquals(0, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				paymentService.save(payment1, userService.findUser());
				paymentService.save(payment2, userService.findUser());
				paymentService.save(payment3, userService.findUser());
				paymentService.save(payment4, userService.findUser());
			assertEquals(4, JdbcTestUtils.countRowsInTable(jdbc, PAYMENTS));
				List<Payment> matchingPayments = Arrays.asList(payment1);
				List<Payment> retrivedPayments = paymentService.getPageResultFromSearchInput(formClass, 1).getContent();
			assertEquals("There should be two payments matching", matchingPayments.size(), retrivedPayments.size());
			assertEquals("Custom made matching list should be equal to retrived one", matchingPayments, retrivedPayments);
	}
	
	
}
