package com.alvie.authentification.service;

import javax.annotation.Resource;
import javax.mail.MessagingException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.StringUtils;


import com.alvie.authentification.exception.InvalidTokenException;
import com.alvie.authentification.exception.UserAlreadyExistException;
import com.alvie.authentification.jpa.data.AlvieUser;
import com.alvie.authentification.jpa.data.SecureToken;
import com.alvie.authentification.jpa.repository.SecureTokenRepository;
import com.alvie.authentification.jpa.repository.UserRepository;
import com.alvie.authentification.web.data.AlvieUserDTO;

import java.util.Objects;


@Service("userService")
public class UserServiceAlvie implements UserService {
	
	@Resource
    private UserRepository userRepository;
	
	@Resource
    private PasswordEncoder passwordEncoder;
	
	@Resource
    private EmailService emailService;
	
	@Resource
    private SecureTokenService secureTokenService;
	
	@Resource
    SecureTokenRepository secureTokenRepository;
	
	@Value("${site.base.url.https}")
    private String baseURL;
	
	@Override
    public void registerUser(AlvieUserDTO newUser) throws UserAlreadyExistException {
		if ( checkIfUserExists(newUser.getEmail()) ) {
			throw new UserAlreadyExistException("User with this email already exists");
		}
		AlvieUser newAlvieUser = new AlvieUser();
		BeanUtils.copyProperties(newUser, newAlvieUser);
		encodePassword(newUser, newAlvieUser);
		userRepository.save(newAlvieUser);
		sendRegistrationConfirmationEmail(newAlvieUser);
	}
	
	@Override
	public boolean checkIfUserExists(String email) {
		return userRepository.findByEmail(email)!=null ? true : false;
	}
	
	@Override
	public void sendRegistrationConfirmationEmail(AlvieUser user) {
		SecureToken secureToken= secureTokenService.createSecureToken();
        secureToken.setUser(user);
        secureTokenRepository.save(secureToken);
        AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
        emailContext.init(user);
        emailContext.setToken(secureToken.getToken());
        emailContext.buildVerificationUrl(baseURL, secureToken.getToken());
        try {
            emailService.sendMail(emailContext);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
	}
	
	private void encodePassword(AlvieUserDTO source, AlvieUser target){
        target.setPassword(passwordEncoder.encode(source.getPassword()));
    }

	@Override
	public boolean verifyUser(String token) throws InvalidTokenException {
		SecureToken secureToken = secureTokenService.findByToken(token);
	    if (Objects.isNull(secureToken) || !StringUtils.equals(token, secureToken.getToken()) || secureToken.isExpired()) {
	        throw new InvalidTokenException("Token is not valid");
	    }
	    AlvieUser user = userRepository.getOne(secureToken.getUser().getId());
	    if (Objects.isNull(user)) {
	        return false;
	    }
	    user.setAccountVerified(true);
	    userRepository.save(user); // let’s same user details

	    // we don’t need invalid password now
	    secureTokenService.removeToken(secureToken);
	    return true;
	}
	
}
