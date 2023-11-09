package com.tracker_application.tracker.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class JwtRequest {

	private String userId;
	private String password;

	public JwtRequest() {
	}

	public JwtRequest(String userId, String password) {
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
