package com.abn.amro.assignments.recipes.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="users")
public class UserEntity {

	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
	
	@Column(name="password", length=4000, nullable=false, unique=false)
	private String password;
	
	@Column(name="username", length=100, nullable=false, unique=false)
	private String username;
	
	@Column(name="dispname", length=100, nullable=false, unique=false)
	private String dispname;
	
	@Column(name="role", length=45, nullable=false, unique=false)
	private String role;
	
	@Column(name="enabled")
	private boolean enabled;
}
