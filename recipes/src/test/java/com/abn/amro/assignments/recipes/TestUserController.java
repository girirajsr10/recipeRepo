package com.abn.amro.assignments.recipes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.abn.amro.assignments.recipes.beans.UserBean;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TestUserController {
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate  restTemplate;
	
	@Test
	public void testUserCreation() throws Exception {
		UserBean bean = new UserBean();
		bean.setDispname("Ron");
		bean.setUsername("Ron");
		bean.setEnabled(true);
		bean.setPassword("1234");
		bean.setRole("user");
		
		ResponseEntity<UserBean> responseEntity = this.restTemplate.postForEntity("http://localhost:"+port+"/users/registration", bean, UserBean.class, "");
		
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertThat(responseEntity.getBody().getId()).isNotNull();
		assertThat(responseEntity.getBody().getDispname()).isEqualTo("Ron");
	}
	
	@Test
	public void userInvalidCreds() throws Exception{
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("username", "Ron");
		requestBody.put("password", "123");
		ResponseEntity<UserBean> responseEntity = this.restTemplate.postForEntity("http://localhost:"+port+"/authenticate", requestBody, UserBean.class, "");
		System.out.println("test -->  " + responseEntity.getBody());
		assertThat(responseEntity.getBody()).isNull();
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}
	
	@Test
	public void userTokenCreation() throws Exception{
		
		UserBean bean = new UserBean();
		bean.setDispname("Ron");
		bean.setUsername("Ron");
		bean.setEnabled(true);
		bean.setPassword("1234");
		bean.setRole("user");
		
		ResponseEntity<UserBean> creationResponseEntity = this.restTemplate.postForEntity("http://localhost:"+port+"/users/registration", bean, UserBean.class, "");
		
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("username", "Ron");
		requestBody.put("password", "1234");
		ResponseEntity<Object> responseEntity = this.restTemplate.postForEntity("http://localhost:"+port+"/authenticate", requestBody, Object.class, "");
		System.out.println("test -->  " + responseEntity.getBody());
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
}
