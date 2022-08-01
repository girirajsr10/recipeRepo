package com.abn.amro.assignments.recipes.beans;

import lombok.Data;

@Data
public class UserBean {

	private Long id;
	private String username;
	private String password;
	private String dispname;
	private String role;
	private boolean enabled;
	private String token;
}
