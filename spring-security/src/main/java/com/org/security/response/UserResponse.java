package com.org.security.response;

public class UserResponse {

	private final String jwtTooken;
	
	public UserResponse(String jwtTooken) {
		this.jwtTooken = jwtTooken;
	}

	public String getJwtTooken() {
		return jwtTooken;
	}


	
}
