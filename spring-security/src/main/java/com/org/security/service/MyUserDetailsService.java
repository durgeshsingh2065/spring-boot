package com.org.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.org.security.repo.User;
import com.org.security.repo.UserRepository;
import com.org.security.request.UserRequest;
import com.org.security.secure.MyUserDetails;

@Service
public class MyUserDetailsService implements UserDetailsService{

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		
		Optional<User> user = userRepository.findByUserName(userName);
		user.orElseThrow(() -> new UsernameNotFoundException("User Not Found -> "+userName));
		return user.map(MyUserDetails::new).get();
	}
	
	public void userRegistration(UserRequest request) throws Exception{
		Optional<User> tempUser = userRepository.findByUserName(request.getUserName());
		if(tempUser.isPresent()){
			throw new Exception("DUPLICATE_USER");
		}
		User user = new User();
		user.setUserName(request.getUserName());
		user.setActive(request.isActive());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRoles(request.getRoles());
		userRepository.save(user);
	}

}
