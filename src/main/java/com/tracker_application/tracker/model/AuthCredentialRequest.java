package com.tracker_application.tracker.model;

import lombok.Getter;
import lombok.Setter;


public class AuthCredentialRequest {
  
    private String userId;
	private String password;
	public AuthCredentialRequest() {
	}
	public AuthCredentialRequest(String userId, String password) {
		this.userId = userId;
		this.password = password;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}


	




    
    

}
