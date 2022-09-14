package com.alvie.authentification;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alvie.authentification.exception.UserAlreadyExistException;
import com.alvie.authentification.service.UserService;
import com.alvie.authentification.web.data.AlvieUserDTO;

@ResponseBody
@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@GetMapping("/login")
	public String index() {
		return "Greetings from Spring Boot 3";
	}
	@PostMapping(value = "/signup")
	public String registerUser( @RequestBody @Valid AlvieUserDTO alvieUserDTO ) {
		try {
			userService.registerUser( alvieUserDTO );
			return "Created user with email " + alvieUserDTO.getEmail() + " and password " + alvieUserDTO.getPassword();
		} catch (UserAlreadyExistException e) {
			return e.getMessage();
		}
	}
}