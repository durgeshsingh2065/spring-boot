package com.org.security.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.security.response.ExceptionResponse;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class JwtFilterAdapter extends OncePerRequestFilter {
	
	@Autowired
	MyUserDetailsService myUserDetailsService;
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		final String authorizationHeader = request.getHeader("Authorization");
		String userName = null;
		String jwtToekn = null;
		
		try {
			if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
				jwtToekn = authorizationHeader.substring(7);
				userName = jwtTokenUtil.getUsernameFromToken(jwtToekn);
			}
			
			if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
				UserDetails userDetails = myUserDetailsService.loadUserByUsername(userName);
				String payload = jwtTokenUtil.getJwtUserPayloadFromToken(jwtToekn);
				ObjectMapper mapper = new ObjectMapper();
				JwtUserPayload pay = mapper.readValue(payload, JwtUserPayload.class);
				if(jwtTokenUtil.validateToken(jwtToekn, userDetails)){
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
			chain.doFilter(request, response);
		} catch(Exception exception) {
			ExceptionResponse exceptionResponse = new ExceptionResponse();
			if(exception instanceof SignatureException || exception instanceof MalformedJwtException || exception instanceof UnsupportedJwtException ||
					exception instanceof IllegalArgumentException){
				exceptionResponse.setMessage("INVALID_TOKEN");
			}else{
				exceptionResponse.setMessage(exception.getMessage());
			}
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");  
			exceptionResponse.setDate(formatter.format(new Date()));
			
			ObjectMapper mapper = new ObjectMapper();
			response.getWriter().write(mapper.writeValueAsString(exceptionResponse));
		}
		
				
		
	}


}
