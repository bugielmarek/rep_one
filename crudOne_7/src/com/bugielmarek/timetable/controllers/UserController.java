package com.bugielmarek.timetable.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bugielmarek.timetable.model.User;
import com.bugielmarek.timetable.services.UserService;

@Controller
@RequestMapping(value = "/users")
public class UserController {

	private static final String USERS = "users";
	private static final String USER = "user";
	private static final String PAGE = "page";
	private static final String CREATE_USER = "createuser";

	private UserService userServiceImpl;

	@Autowired
	public UserController(UserService userServiceImpl) {
		this.userServiceImpl = userServiceImpl;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String allUsers(Model model, User user, @RequestParam(value = "p", defaultValue = "1") int pageNumber) {

		model.addAttribute(PAGE, userServiceImpl.getPage(pageNumber));
		return USERS;
	}

	@RequestMapping(value = "/createuser", method=RequestMethod.GET)
	public String createUser(Model model) {

		model.addAttribute(USER, new User());
		return CREATE_USER;
	}

	@RequestMapping(value = "/createuser", method = RequestMethod.POST)
	public String createUser(@Valid User user, BindingResult result, RedirectAttributes model) {

		if (result.hasErrors()) {
			return CREATE_USER;
		}
		if (user.getId() == null && userServiceImpl.exists(user.getUsername())  ) {
			result.rejectValue("username", "DuplicateKey.user.username");
			return CREATE_USER;
		}
		User saved = userServiceImpl.create(user);
		model.addAttribute("id", saved.getId());
		model.addFlashAttribute(saved);
		return "redirect:/users/user/{id}";
	}
	
	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	public String user(Model model, @PathVariable long id) {

		if(!model.containsAttribute(USER)){
			model.addAttribute(userServiceImpl.findOne(id));
		}
		return USER;
	}
	
	@RequestMapping(value = "/edituser/{id}", method = RequestMethod.GET)
	public String editUser(Model model, @PathVariable long id) {
		
		model.addAttribute(userServiceImpl.findOne(id));
		return CREATE_USER;
	}
	
	@RequestMapping(value = "/deleteuser/{id}", method = RequestMethod.GET)
	public String deleteUser(@PathVariable long id) {
		
		userServiceImpl.delete(id);
		return "redirect:/users";
	}
}
