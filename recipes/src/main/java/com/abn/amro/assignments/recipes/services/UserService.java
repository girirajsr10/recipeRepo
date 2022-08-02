package com.abn.amro.assignments.recipes.services;

import java.util.List;

import com.abn.amro.assignments.recipes.beans.UserBean;

public interface UserService {

	List<UserBean> getUsers();
	
	UserBean createUsers(UserBean bean);
	
}
