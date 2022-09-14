package com.alvie.authentification.service;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.alvie.authentification.exception.UserAlreadyExistException;
import com.alvie.authentification.jpa.data.AlvieUser;
import com.alvie.authentification.jpa.repository.UserRepository;
import com.alvie.authentification.web.data.AlvieUserDTO;

@Service("userService")
public class UserServiceAlvie implements UserService {
	
	@Resource
    private UserRepository userRepository;
	
	@Override
    public void registerUser(AlvieUserDTO newUser) throws UserAlreadyExistException {
		if ( checkIfUserExists(newUser.getEmail()) ) {
			throw new UserAlreadyExistException("User with this email already exists");
		}
		AlvieUser newAlvieUser = new AlvieUser();
		BeanUtils.copyProperties(newUser, newAlvieUser);
		//encodePassword(newUser, newAlvieUser);
		userRepository.save(newAlvieUser);
	}
	
	@Override
	public boolean checkIfUserExists(String email) {
		return userRepository.findByEmail(email)!=null ? true : false;
	}
	
}
