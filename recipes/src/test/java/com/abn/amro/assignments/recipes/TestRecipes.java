package com.abn.amro.assignments.recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.abn.amro.assignments.recipes.beans.JwtResponse;
import com.abn.amro.assignments.recipes.beans.RecipeBean;
import com.abn.amro.assignments.recipes.beans.RecipeSearchBean;
import com.abn.amro.assignments.recipes.beans.SearchCriteria;
import com.abn.amro.assignments.recipes.beans.UserBean;
import com.abn.amro.assignments.recipes.entities.RecipeEntity;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.io.entity.HttpEntities;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestRecipes {

	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate  restTemplate;
	
	private String token;
	
	@BeforeAll
	public void init(){
		
		//Create User
		UserBean bean = new UserBean();
		bean.setDispname("Harry");
		bean.setUsername("Harry");
		bean.setEnabled(true);
		bean.setPassword("1234");
		bean.setRole("user");
		
		ResponseEntity<UserBean> createUserResponse = this.restTemplate.postForEntity("http://localhost:"+port+"/users/registration", bean, UserBean.class, "");
		
		//Login in user 
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("username", "Harry");
		requestBody.put("password", "1234");
		ResponseEntity<Object> responseEntity = this.restTemplate.postForEntity("http://localhost:"+port+"/authenticate", requestBody, Object.class, "");
		
		token = "Bearer "+((Map)responseEntity.getBody()).get("jwttoken");
	}
	
	@Test
	public void createRecipe() {
		RecipeBean recipeBean = new RecipeBean();
		List<String> ingredientsList = new ArrayList<>();
		ingredientsList.add("Carrot");
		ingredientsList.add("Onion");
		ingredientsList.add("Roots");
		recipeBean.setIngridients(ingredientsList);
		recipeBean.setNumOfServings(2);
		recipeBean.setRecipeName("Recipe1");
		recipeBean.setType("Veg");
		List<String> steps = new ArrayList<>();
		steps.add("Step1");
		steps.add("Step2");
		steps.add("Step3");
		recipeBean.setSteps(steps);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", token);
		HttpEntity<RecipeBean> requestEntity = new HttpEntity<>(recipeBean, headers);
 
		
		ResponseEntity<RecipeBean> createRecipeResponse = this.restTemplate.postForEntity("http://localhost:"+port+"/recipe", requestEntity, RecipeBean.class, "");
		
		assertThat(createRecipeResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		
		assertThat(createRecipeResponse.getBody().getId()).isNotNull();
		
		
	}
	
	@Test
	public void updateRecipe() {
		RecipeBean recipeBean = new RecipeBean();
		List<String> ingredientsList = new ArrayList<>();
		ingredientsList.add("Carrot");
		ingredientsList.add("Onion");
		ingredientsList.add("Roots");
		recipeBean.setIngridients(ingredientsList);
		recipeBean.setNumOfServings(2);
		recipeBean.setRecipeName("Recipe1");
		recipeBean.setType("Veg");
		List<String> steps = new ArrayList<>();
		steps.add("Step1");
		steps.add("Step2");
		steps.add("Step3");
		recipeBean.setSteps(steps);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", token);
		
		HttpEntity<RecipeBean> requestEntity = new HttpEntity<>(recipeBean, headers);

		ResponseEntity<RecipeBean> createRecipeResponse = this.restTemplate.postForEntity("http://localhost:"+port+"/recipe", requestEntity, RecipeBean.class, "");
		
		long id = createRecipeResponse.getBody().getId();
		List<SearchCriteria> searchCriteriaList = new ArrayList<>();
		SearchCriteria idCriteria = new SearchCriteria("id", "eq", id);
		
		searchCriteriaList.add(idCriteria);
		RecipeSearchBean searchBean = new RecipeSearchBean();
		searchBean.setDataOption("all");
		searchBean.setSearchList(searchCriteriaList);
		HttpEntity<RecipeSearchBean> searchRequestEntity = new HttpEntity<>(searchBean, headers);
 
		ResponseEntity<Object> recipesList = this.restTemplate.postForEntity("http://localhost:"+port+"/recipe/search",
				 searchRequestEntity, Object.class
				);
		Map<String, Object> content = (Map)recipesList.getBody();
		List<Map<String, String>> contentList= (List)content.get("content");
		
		assertThat(contentList.size()).isGreaterThan(0);
		assertThat(contentList.get(0).get("recipeName")).isEqualTo("Recipe1");
		
		
		recipeBean.setRecipeName("NewRecipe");
		
		this.restTemplate.put("http://localhost:"+port+"/recipe/"+id, requestEntity);
		
		
		recipesList = this.restTemplate.postForEntity("http://localhost:"+port+"/recipe/search",
				searchRequestEntity, Object.class
				);
		assertThat(recipesList.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		content = (Map)recipesList.getBody();
		contentList= (List)content.get("content");
		assertThat(recipesList.getBody()).isNotNull();
		assertThat(contentList.size()).isGreaterThan(0);
		assertThat(contentList.get(0).get("recipeName")).isEqualTo("NewRecipe");
		
		
	}
	
	@Test
	public void deleteRecipeTest() {
		RecipeBean recipeBean = new RecipeBean();
		List<String> ingredientsList = new ArrayList<>();
		ingredientsList.add("Carrot");
		ingredientsList.add("Onion");
		ingredientsList.add("Roots");
		recipeBean.setIngridients(ingredientsList);
		recipeBean.setNumOfServings(2);
		recipeBean.setRecipeName("Recipe1");
		recipeBean.setType("Veg");
		List<String> steps = new ArrayList<>();
		steps.add("Step1");
		steps.add("Step2");
		steps.add("Step3");
		recipeBean.setSteps(steps);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", token);
		
		HttpEntity<RecipeBean> requestEntity = new HttpEntity<>(recipeBean, headers);

		ResponseEntity<RecipeBean> createRecipeResponse = this.restTemplate.postForEntity("http://localhost:"+port+"/recipe", requestEntity, RecipeBean.class, "");
		
		long id = createRecipeResponse.getBody().getId();
		List<SearchCriteria> searchCriteriaList = new ArrayList<>();
		SearchCriteria idCriteria = new SearchCriteria("id", "eq", id);
		
		searchCriteriaList.add(idCriteria);
		RecipeSearchBean searchBean = new RecipeSearchBean();
		searchBean.setDataOption("all");
		searchBean.setSearchList(searchCriteriaList);
		HttpEntity<RecipeSearchBean> searchRequestEntity = new HttpEntity<>(searchBean, headers);
 
		ResponseEntity<Object> recipesList = this.restTemplate.postForEntity("http://localhost:"+port+"/recipe/search",
				 searchRequestEntity, Object.class
				);
		
		Map<String, Object> content = (Map)recipesList.getBody();
		List<Map<String, String>> contentList= (List)content.get("content");
		assertThat(contentList.size()).isGreaterThan(0);
		this.restTemplate.exchange("http://localhost:"+port+"/recipe/"+id, HttpMethod.DELETE, requestEntity, String.class);
		
		recipesList = this.restTemplate.postForEntity("http://localhost:"+port+"/recipe/search",
				 searchRequestEntity, Object.class
				);
		content = (Map)recipesList.getBody();
		contentList= (List)content.get("content");
		assertThat(contentList.size()).isEqualTo(0);
	}
}

