package com.abn.amro.assignments.recipes.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abn.amro.assignments.recipes.beans.UserBean;
import com.abn.amro.assignments.recipes.services.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("users")
public class UserController {

	private UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping
	public ResponseEntity<List<UserBean>> getUsers(){
		List<UserBean> beanList = this.userService.getUsers();
		if(beanList.size() > 0) {
			return new ResponseEntity<>(beanList, HttpStatus.OK);			
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	@PostMapping(path = "/registration")
	public ResponseEntity<UserBean> createUser(@RequestBody UserBean user) {
		log.info("Creating new User --> " + user);
		userService.createUsers(user);
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}
}
