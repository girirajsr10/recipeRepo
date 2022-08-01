package com.abn.amro.assignments.recipes.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abn.amro.assignments.recipes.beans.RecipeBean;
import com.abn.amro.assignments.recipes.beans.RecipeSearchBean;
import com.abn.amro.assignments.recipes.entities.RecipeEntity;
import com.abn.amro.assignments.recipes.services.RecipeService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("recipe")
public class RecipeController {
	
	private RecipeService recipeService;
	
	@Autowired
	public RecipeController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

//	@GetMapping(path = "/search")
	@PostMapping(path = "/search")
	public Page<RecipeEntity> getAllRecipes(
			@RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
			@RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
			@RequestBody RecipeSearchBean searchBean) {
		
		log.info("Search bean -->" + searchBean);
		
		return recipeService.search(searchBean, pageNum, pageSize);
	}
	
	@PostMapping
	public ResponseEntity<RecipeBean> createRecipe(@RequestBody @Valid  RecipeBean recipe) {
		log.info("Creating new recipe --> " + recipe);
		
		RecipeBean createdEntity = recipeService.createRecipe(recipe);
		return new ResponseEntity<>(createdEntity, HttpStatus.CREATED);
	}
	
	@PutMapping(path = "/{id}")
	public ResponseEntity<Object> updateRecipe(@PathVariable Long id, @RequestBody @Valid RecipeBean recipe) {
		
		RecipeBean bean = recipeService.updateRecipe(id, recipe);
		return new ResponseEntity<>(bean, HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Object> deleteRecipe(@PathVariable Long id) {
		recipeService.deleteRecipe(id);
		return new ResponseEntity<>( HttpStatus.NO_CONTENT);
	}
}
