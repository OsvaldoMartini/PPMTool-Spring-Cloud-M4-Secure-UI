package com.shifthunter.ppmtool;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;


import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
@EnableOAuth2Sso
public class ReportController extends WebSecurityConfigurerAdapter {

	@Autowired
    private OAuth2ClientContext clientContext;
	
	@RequestMapping("/")
	public String loadHome(){
		return "home";
	}
	
	// I am Authorazing the Home Page do not be redirect here to Auth2
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
			.antMatchers("/", "/login**") //Permite any trafic for this
			.permitAll()
		.anyRequest()
			.authenticated();
	}
	
	@RequestMapping("/reports")
	public String loadReports(Model model){
		
		OAuth2AccessToken t = clientContext.getAccessToken();
		System.out.println("Token: " + t.getValue());
		
		return "reports";
	}
	

}
