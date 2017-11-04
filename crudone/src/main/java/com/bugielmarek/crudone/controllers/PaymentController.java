package com.bugielmarek.crudone.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bugielmarek.crudone.models.FormClass;
import com.bugielmarek.crudone.models.Payment;
import com.bugielmarek.crudone.services.PaymentService;
import com.bugielmarek.crudone.services.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/payments")
public class PaymentController {

	private static final String PAYMENT = "payment";
	private static final String PAYMENTS = "payments";
	private static final String CREATE_PAYMENT = "createpayment";
	private static final String PAYMENT_EXISTS = "paymentExists";
	private static final String HOME = "home";
	private static final String PAGE = "page";

	private PaymentService paymentService;

	private UserService userService;
	
	@Autowired
	public PaymentController(PaymentService paymentService, UserService userService) {
		this.paymentService = paymentService;
		this.userService = userService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String allPayments(Model model, Payment payment,
			@RequestParam(value = "p", defaultValue = "1") int pageNumber) {

		PaymentController.log.info("GET request/redirect for '/payments' made");
		if (!model.containsAttribute(PAGE)) {
			PaymentController.log.info("No flashAttribute detected");
			model.addAttribute(PAGE, paymentService.getPage(pageNumber));
		}
		return PAYMENTS;
	}

	@RequestMapping(value = "/createpayment", method = RequestMethod.GET)
	public String createPayment(Model model) {
		PaymentController.log.info("GET request for '/createpayment' made");
		model.addAttribute(new Payment());
		return CREATE_PAYMENT;
	}

	@RequestMapping(value = "/createpayment", method = RequestMethod.POST)
	public String createPayment(@Valid Payment payment, BindingResult result, Model model,
			RedirectAttributes redModel) {
		PaymentController.log.info("POST request for '/createpayment' made");
		if (result.hasErrors()) {
			PaymentController.log.info("Invalid Payment obj passed, returning form to the user");
			return CREATE_PAYMENT;
		}
		if (paymentService.exists(payment)) {
			model.addAttribute(PAYMENT_EXISTS,
					paymentService.findByCaseTypeSygNumber(payment.getCaseType(), payment.getSygNumber()));
			PaymentController.log.info("Payment with given sygNumber and caseType already exists in DB");
			return CREATE_PAYMENT;
		}
		Payment saved = paymentService.save(payment, userService.findUser());
		redModel.addAttribute("id", saved.getId());
		redModel.addFlashAttribute(saved);
		return "redirect:/payments/payment/{id}";
	}

	@RequestMapping(value = "/payment/{id}", method = RequestMethod.GET)
	public String payment(Model model, @PathVariable long id) {
		PaymentController.log.info("GET request for '/payment/{id}' made");
		if (!model.containsAttribute(PAYMENT)) {
			PaymentController.log.info("No flashAttribute detected");
			model.addAttribute(paymentService.findOne(id));
		}
		return PAYMENT;
	}

	@RequestMapping(value = "/editpayment/{id}", method = RequestMethod.GET)
	public String editPayment(Model model, @PathVariable long id) {
		PaymentController.log.info("GET request for '/editpayment/{id}' made");
		model.addAttribute(paymentService.findOne(id));
		return CREATE_PAYMENT;
	}

	@RequestMapping(value = "/deletepayment/{id}", method = RequestMethod.GET)
	public String deletePayment(@PathVariable long id) {
		PaymentController.log.info("GET request for '/deletepayment/{id}' made");
		paymentService.delete(id);
		return "redirect:/payments";
	}

	@RequestMapping(value = "/findpayment", method = RequestMethod.POST)
	public String findPayment(RedirectAttributes model, @ModelAttribute("formClass") FormClass formClass,
			BindingResult rs, @RequestParam(value = "p", defaultValue = "1") int pageNumber) {
		
		PaymentController.log.info("POST request for '/findpayment' made");
		Page<Payment> resultPage = paymentService.getPageResultFromSearchInput(formClass, pageNumber);
		if (!resultPage.hasContent()) {
			PaymentController.log.info("No matching results found in DB for given input");
			rs.rejectValue("payment.name", "search.payment.notFound");
			return HOME;
		}
		PaymentController.log.info("Matching results found in DB. Redirecting to '/payments'");
		model.addFlashAttribute(PAGE, resultPage);
		return "redirect:/payments";
	}
}