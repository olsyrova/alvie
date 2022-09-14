package com.alvie.authentification.service;


import com.alvie.authentification.exception.UserAlreadyExistException;
import com.alvie.authentification.web.data.AlvieUserDTO;

public interface UserService {
	public void registerUser(final AlvieUserDTO user) throws UserAlreadyExistException;
	boolean checkIfUserExists(String email);
}