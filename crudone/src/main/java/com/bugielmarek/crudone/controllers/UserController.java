package com.bugielmarek.crudone.controllers;

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

import com.bugielmarek.crudone.models.User;
import com.bugielmarek.crudone.services.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/users")
public class UserController {

	private static final String USERS = "users";
	private static final String USER = "user";
	private static final String PAGE = "page";
	private static final String CREATE_USER = "createuser";

	private UserService userService;

	@Autowired
	public UserController(UserService userServiceImpl) {
		this.userService = userServiceImpl;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String allUsers(Model model, User user, @RequestParam(value = "p", defaultValue = "1") int pageNumber) {
		UserController.log.info("GET request for '/users' made");
		model.addAttribute(PAGE, userService.getPage(pageNumber));
		return USERS;
	}

	@RequestMapping(value = "/createuser", method=RequestMethod.GET)
	public String createUser(Model model) {
		UserController.log.info("GET request for '/createuser' made");
		model.addAttribute(new User());
		return CREATE_USER;
	}

	@RequestMapping(value = "/createuser", method = RequestMethod.POST)
	public String createUser(@Valid User user, BindingResult result, RedirectAttributes model) {
		UserController.log.info("POST request for '/createuser' made");
		if (result.hasErrors()) {
			UserController.log.info("Invalid User obj passed, returning form to the user");
			return CREATE_USER;
		}
		
		if(!doPasswordsMatch(user)){
			UserController.log.info("Invalid User obj passed, passwords don't match");
			result.rejectValue("formPassword", "PasswordsDontMatch.user.password");
			return CREATE_USER;
		}
		
		if (userService.doesUsernameExists(user)) {
			UserController.log.info("User with given username already exists in DB");
			result.rejectValue("username", "DuplicateKey.user.username");
			return CREATE_USER;
		}
		User saved = userService.save(user);
		model.addAttribute("id", saved.getId());
		model.addFlashAttribute(saved);
		return "redirect:/users/user/{id}";
	}
	
	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	public String user(Model model, @PathVariable long id) {
		UserController.log.info("GET request/redirect for '/user/{id}' made");
		if(!model.containsAttribute(USER)){
			model.addAttribute(userService.findOne(id));
		}
		return USER;
	}
	
	@RequestMapping(value = "/edituser/{id}", method = RequestMethod.GET)
	public String editUser(Model model, @PathVariable long id) {
		UserController.log.info("GET request for '/edituser/{id}' made");
		model.addAttribute(userService.findOne(id));
		return CREATE_USER;
	}
	
	@RequestMapping(value = "/deleteuser/{id}", method = RequestMethod.GET)
	public String deleteUser(@PathVariable long id) {
		UserController.log.info("GET request for '/deleteuser/{id}' made");
		userService.delete(id);
		return "redirect:/users";
	}
	
	/**
	 * Checks weather passwords entered by user match
	 * @param user sent by submitting the form
	 * @return <code>true</code> if they match, <code>false</code> otherwise
	 */
	private boolean doPasswordsMatch(User user) {
		return user.getPassword().equals(user.getFormPassword());
	}
}
