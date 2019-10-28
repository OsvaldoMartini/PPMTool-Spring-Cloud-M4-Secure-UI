package com.shifthunter.ppmtool;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
@EnableOAuth2Sso
public class ReportController extends WebSecurityConfigurerAdapter {

	@Autowired
    private OAuth2ClientContext clientContext;
	
	@Autowired
    private OAuth2RestTemplate oauth2RestTemplate;	
	
	@RequestMapping("/")
	public String loadHome(){
		return "home";
	}
	

	
	@RequestMapping("/reports")
	public String loadReports(Model model){
		
		OAuth2AccessToken t = clientContext.getAccessToken();
		System.out.println("Token: " + t.getValue());
		
		//I Call secure Services, get back to the results and add them to our model
		
		// We are calling "oauth2RestTemplate" 
		// We Have the Token Automatically Injected in the header
		// this s a Bean that's makes normal call to a Rest Get
		
		ResponseEntity<ArrayList<TollUsage>> tolls = oauth2RestTemplate.exchange("http://localhost:9001/services/tolldata", HttpMethod.GET, null, new ParameterizedTypeReference<ArrayList<TollUsage>>(){});
		
		// Get Back an Array of "tollsUsage"
		model.addAttribute("tolls", tolls.getBody());
		
		return "reports";
	}
	
	// This is For What will be come back from the Web Service, from Json
	// Just to Serialize inn this Object -> I am being Lazy
	public static class TollUsage {
		
		public String Id;
		public String stationId;
		public String licensePlate;
		public String timestamp;
		
		public TollUsage() {}
		
		public TollUsage(String id, String stationid, String licenseplate, String timestamp){
			this.Id = id;
			this.stationId = stationid;
			this.licensePlate = licenseplate;
			this.timestamp = timestamp;
		}
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

}
