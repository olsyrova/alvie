package com.alvie.authentification.service;


import com.alvie.authentification.exception.InvalidTokenException;
import com.alvie.authentification.exception.UserAlreadyExistException;
import com.alvie.authentification.jpa.data.AlvieUser;
import com.alvie.authentification.web.data.AlvieUserDTO;

public interface UserService {
	public void registerUser(final AlvieUserDTO user) throws UserAlreadyExistException;
	boolean checkIfUserExists(String email);
	void sendRegistrationConfirmationEmail(AlvieUser user);
	public boolean verifyUser(String token) throws InvalidTokenException;
}