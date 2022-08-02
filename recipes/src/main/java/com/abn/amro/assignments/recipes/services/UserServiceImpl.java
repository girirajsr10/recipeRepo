package com.abn.amro.assignments.recipes.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.abn.amro.assignments.recipes.beans.UserBean;
import com.abn.amro.assignments.recipes.entities.UserEntity;
import com.abn.amro.assignments.recipes.repositories.UserRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

	private UserRepo userRepo;
	private PasswordEncoder passEncoder;
	@Autowired
	public UserServiceImpl(UserRepo userRepo, PasswordEncoder passEncoder) {
		this.userRepo = userRepo;
		this.passEncoder = passEncoder;
	}
	
	@Override
	public List<UserBean> getUsers() {
		List<UserEntity>userList = this.userRepo.findAll();
		List<UserBean> userBeanList = new ArrayList<>();
		userList.stream().forEach(user -> {
			UserBean bean = new UserBean();
			user.setPassword("");
			BeanUtils.copyProperties(user, bean);
			userBeanList.add(bean);
		});
		return userBeanList;
	}

	@Override
	public UserBean createUsers(UserBean bean) {
		log.info("Creating User Entity!!!");
		UserEntity userEntity = new UserEntity();
		
		BeanUtils.copyProperties(bean, userEntity);
		userEntity.setPassword(passEncoder.encode(bean.getPassword()));
		log.info("User Entity --> "+ userEntity);
		userEntity = userRepo.save(userEntity);
		bean.setId(userEntity.getId());
		log.info("User creation successful!");
		return bean;
	}
	
	
}
