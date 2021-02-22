package com.org.security.controller;

import java.security.SecureRandom;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.security.request.UserRequest;
import com.org.security.response.UserResponse;
import com.org.security.service.JwtTokenUtil;
import com.org.security.service.JwtUserPayload;
import com.org.security.service.MyUserDetailsService;

@RestController
public class ResourceController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	MyUserDetailsService myUserDetailsService;
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@GetMapping("/user")
	public String user() {
		return "<h1> Welcome to User Page... </h1>";
	}
	
	@GetMapping("/admin")
	public String admin() {
		return "<h1> Welcome to Admin Page... </h1>";
	}

	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody UserRequest userRequest) throws Exception{
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(userRequest.getUserName(), userRequest.getPassword()));
		} catch (BadCredentialsException exception) {
			throw exception;
		}
		final UserDetails userDetail = myUserDetailsService.loadUserByUsername(userRequest.getUserName());
		JwtUserPayload jwtPayload = new JwtUserPayload(userRequest.getUserName(), getUniqueId(), new Date());
		ObjectMapper mapper = new ObjectMapper();
		final String jwtTooken = jwtTokenUtil.generateToken(userDetail, mapper.writeValueAsString(jwtPayload));
		return ResponseEntity.ok(new UserResponse(jwtTooken));		
	}
	
	@PostMapping("/userRegistration")
	public ResponseEntity<?> userResgistration(@RequestBody UserRequest userRequest) throws Exception{
		myUserDetailsService.userRegistration(userRequest);
		return ResponseEntity.ok("Registration success...");		
	}
	
	private String getUniqueId(){
		String result = new SecureRandom().ints(0,36)
	            .mapToObj(i -> Integer.toString(i, 36))
	            .map(String::toUpperCase).distinct().limit(16).collect(Collectors.joining())
	            .replaceAll("([A-Z0-9]{4})", "$1").substring(0,15);
		return result;
	}

}
