package com.alvie.authentification.service;

import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriBuilder;

import com.alvie.authentification.jpa.data.AlvieUser;

public class AccountVerificationEmailContext extends AbstractEmailContext {
	
	private String token;
	
	@Override
    public <T> void init(T context){
        //we can do any common configuration setup here
        // like setting up some base URL and context
        AlvieUser customer = (AlvieUser) context; // we pass the customer informati
        put("firstName", customer.getFirstName());
        setTemplateLocation("emails/email-verification");
        setSubject("Complete your registration");
        setFrom("no-reply@javadevjournal.com");
        setTo(customer.getEmail());
    }
	
	public void setToken(String token) {
        this.token = token;
        put("token", token);
    }

    public void buildVerificationUrl(final String baseURL, final String token){
        final String url= UriComponentsBuilder.fromHttpUrl(baseURL)
                .path("/user/verify").queryParam("token", token).toUriString();
        put("verificationURL", url);
    }
	
}
