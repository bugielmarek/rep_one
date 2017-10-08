package com.bugielmarek.timetable.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bugielmarek.timetable.model.FormClass;
import com.bugielmarek.timetable.model.Payment;
import com.bugielmarek.timetable.services.PaymentService;
import com.bugielmarek.timetable.services.UserService;

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

		if (!model.containsAttribute(PAGE)) {
			model.addAttribute(PAGE, paymentService.getPage(pageNumber));
		}
		return PAYMENTS;
	}

	@RequestMapping(value = "/createpayment", method = RequestMethod.GET)
	public String createPayment(Model model) {

		model.addAttribute(new Payment());
		return CREATE_PAYMENT;
	}

	@RequestMapping(value = "/createpayment", method = RequestMethod.POST)
	public String createPayment(@ModelAttribute @Valid Payment payment, BindingResult result, Model model,
			RedirectAttributes redModel) {

		if (result.hasErrors()) {
			return CREATE_PAYMENT;
		}
		if (paymentService.isNotValid(payment)) {
			model.addAttribute(PAYMENT_EXISTS,
					paymentService.findByCaseTypeSygNumber(payment.getCaseType(), payment.getSygNumber()));
			return CREATE_PAYMENT;
		}
		Payment saved = paymentService.save(payment, userService.findUser());
		redModel.addAttribute("id", saved.getId());
		redModel.addFlashAttribute(saved);
		return "redirect:/payments/payment/{id}";
	}

	@RequestMapping(value = "/payment/{id}", method = RequestMethod.GET)
	public String payment(Model model, @PathVariable long id) {

		if (!model.containsAttribute(PAYMENT)) {
			model.addAttribute(paymentService.findOne(id));
		}
		return PAYMENT;
	}

	@RequestMapping(value = "/editpayment/{id}", method = RequestMethod.GET)
	public String editPayment(Model model, @PathVariable long id) {

		model.addAttribute(paymentService.findOne(id));
		return CREATE_PAYMENT;
	}

	@RequestMapping(value = "/deletepayment/{id}", method = RequestMethod.GET)
	public String deletePayment(@PathVariable long id) {

		paymentService.delete(id);
		return "redirect:/payments";
	}

	@RequestMapping(value = "/findpayment", method = RequestMethod.POST)
	public String findPayment(RedirectAttributes model, @ModelAttribute("formClass") FormClass formClass,
			BindingResult rs, @RequestParam(value = "p", defaultValue = "1") int pageNumber) {

		if (!paymentService.searchInputHasResult(formClass, pageNumber)) {
			rs.rejectValue("payment.name", "search.payment.notFound");
			return HOME;
		}
		model.addFlashAttribute(PAGE, paymentService.getPageResultFromSearchInput(formClass, pageNumber));
		return "redirect:/payments";
	}
}