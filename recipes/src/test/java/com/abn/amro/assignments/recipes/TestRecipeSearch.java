package com.abn.amro.assignments.recipes;

import static org.assertj.core.api.Assertions.assertThat;

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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.abn.amro.assignments.recipes.beans.RecipeBean;
import com.abn.amro.assignments.recipes.beans.RecipeSearchBean;
import com.abn.amro.assignments.recipes.beans.SearchCriteria;
import com.abn.amro.assignments.recipes.beans.UserBean;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestRecipeSearch {

	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate  restTemplate;
	
	private String token;
	
	HttpHeaders headers;
	
	
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
		headers = new HttpHeaders();
		headers.set("Authorization", token);
		
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
		System.out.println("id --> " + createRecipeResponse.getBody().getId());
		
		recipeBean.setNumOfServings(3);
		recipeBean.setRecipeName("Recipe2");
		recipeBean.setType("Vegan");
		requestEntity = new HttpEntity<>(recipeBean, headers);
		createRecipeResponse = this.restTemplate.postForEntity("http://localhost:"+port+"/recipe", requestEntity, RecipeBean.class, "");
		System.out.println("id --> " + createRecipeResponse.getBody().getId());
		
		
		recipeBean.setNumOfServings(1);
		recipeBean.setRecipeName("Recipe3");
		steps = new ArrayList<>();
		steps.add("NewData");
		steps.add("NewData taxi");
		steps.add("NewData gas");
		steps.add("NewData oven");
		recipeBean.setSteps(steps);
		ingredientsList = new ArrayList<>();
		ingredientsList.add("Almond");
		ingredientsList.add("Water");
		ingredientsList.add("mew");
		recipeBean.setIngridients(ingredientsList);
		recipeBean.setType("non Veg");
		requestEntity = new HttpEntity<>(recipeBean, headers);
		createRecipeResponse = this.restTemplate.postForEntity("http://localhost:"+port+"/recipe", requestEntity, RecipeBean.class, "");
		System.out.println("id --> " + createRecipeResponse.getBody().getId());
		
		
		ingredientsList = new ArrayList<>();
		ingredientsList.add("Manog");
		ingredientsList.add("milk");
		ingredientsList.add("random");
		recipeBean.setNumOfServings(4);
		recipeBean.setRecipeName("Recipe4");
		recipeBean.setIngridients(ingredientsList);
		recipeBean.setType("Paleo");
		requestEntity = new HttpEntity<>(recipeBean, headers);
		createRecipeResponse = this.restTemplate.postForEntity("http://localhost:"+port+"/recipe", requestEntity, RecipeBean.class, "");
		System.out.println("id --> " + createRecipeResponse.getBody().getId());
	}
	
	@Test
	public void searchRecipeType() {
		List<SearchCriteria> searchCriteriaList = new ArrayList<>();
		SearchCriteria criteria = new SearchCriteria("type", "eq", "veg");
		
		searchCriteriaList.add(criteria);
		RecipeSearchBean searchBean = new RecipeSearchBean();
		searchBean.setDataOption("all");
		searchBean.setSearchList(searchCriteriaList);
		HttpEntity<RecipeSearchBean> searchRequestEntity = new HttpEntity<>(searchBean, headers);
 
		ResponseEntity<Object> recipesList = this.restTemplate.postForEntity("http://localhost:"+port+"/recipe/search",
				 searchRequestEntity, Object.class
				);
		
		Map<String, Object> content = (Map)recipesList.getBody();
		List<Map<String, String>> contentList= (List)content.get("content");
		
		assertThat(contentList.size()).isEqualTo(1);
		assertThat(contentList.get(0).get("recipeName")).isEqualTo("Recipe1");
		assertThat(contentList.get(0).get("type")).isEqualTo("Veg");
		
		
		criteria = new SearchCriteria("type", "cn", "vegan");
		searchCriteriaList = new ArrayList<>();
		searchCriteriaList.add(criteria);
		searchBean = new RecipeSearchBean();
		searchBean.setDataOption("all");
		searchBean.setSearchList(searchCriteriaList);
		searchRequestEntity = new HttpEntity<>(searchBean, headers);
 
		recipesList = this.restTemplate.postForEntity("http://localhost:"+port+"/recipe/search",
				 searchRequestEntity, Object.class
				);
		
		content = (Map)recipesList.getBody();
		contentList= (List)content.get("content");
		
		assertThat(contentList.size()).isEqualTo(1);
		assertThat(contentList.get(0).get("recipeName")).isEqualTo("Recipe2");
		assertThat(contentList.get(0).get("type")).isEqualTo("Vegan");
		
	}
	
	
	@Test
	public void searchRecipeServings() {
		List<SearchCriteria> searchCriteriaList = new ArrayList<>();
		SearchCriteria criteria = new SearchCriteria("numOfServings", "gt", 2);
		
		searchCriteriaList.add(criteria);
		RecipeSearchBean searchBean = new RecipeSearchBean();
		searchBean.setDataOption("all");
		searchBean.setSearchList(searchCriteriaList);
		HttpEntity<RecipeSearchBean> searchRequestEntity = new HttpEntity<>(searchBean, headers);
 
		ResponseEntity<Object> recipesList = this.restTemplate.postForEntity("http://localhost:"+port+"/recipe/search",
				 searchRequestEntity, Object.class
				);
		System.out.println("recipe list --> "+ recipesList);
		Map<String, Object> content = (Map)recipesList.getBody();
		List<Map<String, Object>> contentList= (List)content.get("content");
		
		assertThat(contentList.size()).isEqualTo(2);
		assertThat((Integer)contentList.get(0).get("numOfServings")).isGreaterThan(2);
		
		
		criteria = new SearchCriteria("numOfServings", "ge", 2);
		searchCriteriaList = new ArrayList<>();
		searchCriteriaList.add(criteria);
		searchBean = new RecipeSearchBean();
		searchBean.setDataOption("all");
		searchBean.setSearchList(searchCriteriaList);
		searchRequestEntity = new HttpEntity<>(searchBean, headers);
 
		recipesList = this.restTemplate.postForEntity("http://localhost:"+port+"/recipe/search",
				 searchRequestEntity, Object.class
				);
		
		content = (Map)recipesList.getBody();
		contentList= (List)content.get("content");
		
		assertThat(contentList.size()).isEqualTo(3);
		assertThat((Integer)contentList.get(0).get("numOfServings")).isGreaterThanOrEqualTo(2);
		
		
		criteria = new SearchCriteria("numOfServings", "eq", 2);
		searchCriteriaList = new ArrayList<>();
		searchCriteriaList.add(criteria);
		searchBean = new RecipeSearchBean();
		searchBean.setDataOption("all");
		searchBean.setSearchList(searchCriteriaList);
		searchRequestEntity = new HttpEntity<>(searchBean, headers);
 
		recipesList = this.restTemplate.postForEntity("http://localhost:"+port+"/recipe/search",
				 searchRequestEntity, Object.class
				);
		
		content = (Map)recipesList.getBody();
		contentList= (List)content.get("content");
		
		assertThat(contentList.size()).isEqualTo(1);
		assertThat((Integer)contentList.get(0).get("numOfServings")).isEqualTo(2);
		assertThat(contentList.get(0).get("recipeName")).isEqualTo("Recipe1");
		
	}
	
	@Test
	public void searchRecipeSteps() {
		List<SearchCriteria> searchCriteriaList = new ArrayList<>();
		SearchCriteria criteria = new SearchCriteria("instructions", "cn", "oven");
		
		searchCriteriaList.add(criteria);
		RecipeSearchBean searchBean = new RecipeSearchBean();
		searchBean.setDataOption("all");
		searchBean.setSearchList(searchCriteriaList);
		HttpEntity<RecipeSearchBean> searchRequestEntity = new HttpEntity<>(searchBean, headers);
 
		ResponseEntity<Object> recipesList = this.restTemplate.postForEntity("http://localhost:"+port+"/recipe/search",
				 searchRequestEntity, Object.class
				);
		System.out.println("recipe list --> "+ recipesList);
		Map<String, Object> content = (Map)recipesList.getBody();
		List<Map<String, Object>> contentList= (List)content.get("content");
		
		assertThat(contentList.size()).isEqualTo(2);
		assertThat(contentList.get(0).get("recipeName")).isEqualTo("Recipe3");
		assertThat(contentList.get(1).get("recipeName")).isEqualTo("Recipe4");
		
		
//		criteria = new SearchCriteria("numOfServings", "ge", 2);
//		searchCriteriaList = new ArrayList<>();
//		searchCriteriaList.add(criteria);
//		searchBean = new RecipeSearchBean();
//		searchBean.setDataOption("all");
//		searchBean.setSearchList(searchCriteriaList);
//		searchRequestEntity = new HttpEntity<>(searchBean, headers);
// 
//		recipesList = this.restTemplate.postForEntity("http://localhost:"+port+"/recipe/search",
//				 searchRequestEntity, Object.class
//				);
//		
//		content = (Map)recipesList.getBody();
//		contentList= (List)content.get("content");
//		
//		assertThat(contentList.size()).isEqualTo(3);
//		assertThat((Integer)contentList.get(0).get("numOfServings")).isGreaterThanOrEqualTo(2);
//		
//		
//		criteria = new SearchCriteria("numOfServings", "eq", 2);
//		searchCriteriaList = new ArrayList<>();
//		searchCriteriaList.add(criteria);
//		searchBean = new RecipeSearchBean();
//		searchBean.setDataOption("all");
//		searchBean.setSearchList(searchCriteriaList);
//		searchRequestEntity = new HttpEntity<>(searchBean, headers);
// 
//		recipesList = this.restTemplate.postForEntity("http://localhost:"+port+"/recipe/search",
//				 searchRequestEntity, Object.class
//				);
//		
//		content = (Map)recipesList.getBody();
//		contentList= (List)content.get("content");
//		
//		assertThat(contentList.size()).isEqualTo(1);
//		assertThat((Integer)contentList.get(0).get("numOfServings")).isEqualTo(2);
//		assertThat(contentList.get(0).get("recipeName")).isEqualTo("Recipe1");
		
	}
	
	@Test
	public void searchRecipeIngredients() {
		List<SearchCriteria> searchCriteriaList = new ArrayList<>();
		SearchCriteria criteria = new SearchCriteria("ingredients", "cn", "Almond");
		
		searchCriteriaList.add(criteria);
		RecipeSearchBean searchBean = new RecipeSearchBean();
		searchBean.setDataOption("all");
		searchBean.setSearchList(searchCriteriaList);
		HttpEntity<RecipeSearchBean> searchRequestEntity = new HttpEntity<>(searchBean, headers);
 
		ResponseEntity<Object> recipesList = this.restTemplate.postForEntity("http://localhost:"+port+"/recipe/search",
				 searchRequestEntity, Object.class
				);
		System.out.println("recipe list --> "+ recipesList);
		Map<String, Object> content = (Map)recipesList.getBody();
		List<Map<String, Object>> contentList= (List)content.get("content");
		
		assertThat(contentList.size()).isEqualTo(1);
		assertThat(contentList.get(0).get("recipeName")).isEqualTo("Recipe3");
		
		
		criteria = new SearchCriteria("ingredients", "cn", "carrot");
		searchCriteriaList = new ArrayList<>();
		searchCriteriaList.add(criteria);
		searchBean = new RecipeSearchBean();
		searchBean.setDataOption("all");
		searchBean.setSearchList(searchCriteriaList);
		searchRequestEntity = new HttpEntity<>(searchBean, headers);
 
		recipesList = this.restTemplate.postForEntity("http://localhost:"+port+"/recipe/search",
				 searchRequestEntity, Object.class
				);
		
		content = (Map)recipesList.getBody();
		contentList= (List)content.get("content");
		
		assertThat(contentList.size()).isEqualTo(2);
//		
//		
//		criteria = new SearchCriteria("numOfServings", "eq", 2);
//		searchCriteriaList = new ArrayList<>();
//		searchCriteriaList.add(criteria);
//		searchBean = new RecipeSearchBean();
//		searchBean.setDataOption("all");
//		searchBean.setSearchList(searchCriteriaList);
//		searchRequestEntity = new HttpEntity<>(searchBean, headers);
// 
//		recipesList = this.restTemplate.postForEntity("http://localhost:"+port+"/recipe/search",
//				 searchRequestEntity, Object.class
//				);
//		
//		content = (Map)recipesList.getBody();
//		contentList= (List)content.get("content");
//		
//		assertThat(contentList.size()).isEqualTo(1);
//		assertThat((Integer)contentList.get(0).get("numOfServings")).isEqualTo(2);
//		assertThat(contentList.get(0).get("recipeName")).isEqualTo("Recipe1");
		
	}
	
	@Test
	public void searchRecipeIngredientsAndNumberOfServings() {
		List<SearchCriteria> searchCriteriaList = new ArrayList<>();
		SearchCriteria criteria = new SearchCriteria("ingredients", "cn", "carrot");
		SearchCriteria criteria2 = new SearchCriteria("numOfServings", "lt", 3);
		
		searchCriteriaList.add(criteria);
		searchCriteriaList.add(criteria2);
		RecipeSearchBean searchBean = new RecipeSearchBean();
		searchBean.setDataOption("all");
		searchBean.setSearchList(searchCriteriaList);
		HttpEntity<RecipeSearchBean> searchRequestEntity = new HttpEntity<>(searchBean, headers);
 
		ResponseEntity<Object> recipesList = this.restTemplate.postForEntity("http://localhost:"+port+"/recipe/search",
				 searchRequestEntity, Object.class
				);
		System.out.println("recipe list --> "+ recipesList);
		Map<String, Object> content = (Map)recipesList.getBody();
		List<Map<String, Object>> contentList= (List)content.get("content");
		
		assertThat(contentList.size()).isEqualTo(1);
		assertThat(contentList.get(0).get("recipeName")).isEqualTo("Recipe1");
		
		
		criteria = new SearchCriteria("ingredients", "cn", "Mango");
		criteria2 = new SearchCriteria("numOfServings", "le", 3);
		searchCriteriaList = new ArrayList<>();
		searchCriteriaList.add(criteria);
		searchCriteriaList.add(criteria2);
		searchBean = new RecipeSearchBean();
		searchBean.setDataOption("all");
		searchBean.setSearchList(searchCriteriaList);
		searchRequestEntity = new HttpEntity<>(searchBean, headers);
 
		recipesList = this.restTemplate.postForEntity("http://localhost:"+port+"/recipe/search",
				 searchRequestEntity, Object.class
				);
		
		content = (Map)recipesList.getBody();
		contentList= (List)content.get("content");
		
		assertThat(contentList.size()).isEqualTo(0);
//		
//		
//		criteria = new SearchCriteria("numOfServings", "eq", 2);
//		searchCriteriaList = new ArrayList<>();
//		searchCriteriaList.add(criteria);
//		searchBean = new RecipeSearchBean();
//		searchBean.setDataOption("all");
//		searchBean.setSearchList(searchCriteriaList);
//		searchRequestEntity = new HttpEntity<>(searchBean, headers);
// 
//		recipesList = this.restTemplate.postForEntity("http://localhost:"+port+"/recipe/search",
//				 searchRequestEntity, Object.class
//				);
//		
//		content = (Map)recipesList.getBody();
//		contentList= (List)content.get("content");
//		
//		assertThat(contentList.size()).isEqualTo(1);
//		assertThat((Integer)contentList.get(0).get("numOfServings")).isEqualTo(2);
//		assertThat(contentList.get(0).get("recipeName")).isEqualTo("Recipe1");
		
	}
}
