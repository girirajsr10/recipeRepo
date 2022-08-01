package com.abn.amro.assignments.recipes.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.abn.amro.assignments.recipes.beans.UserBean;
import com.abn.amro.assignments.recipes.entities.UserEntity;
import com.abn.amro.assignments.recipes.repositories.UserRepo;

@Service
public class JwtUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<UserEntity> optUser = userRepo.findByUsername(username);
		if (optUser.isPresent()) {
			UserEntity user = optUser.get();
			return new User(user.getUsername(), user.getPassword(),new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found ");
		}
	}

	public UserEntity save(UserBean user) {
		UserEntity newUser = new UserEntity();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		return userRepo.save(newUser);
	}
}
