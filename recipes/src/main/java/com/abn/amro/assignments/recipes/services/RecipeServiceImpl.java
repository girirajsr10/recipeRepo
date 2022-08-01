package com.abn.amro.assignments.recipes.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.abn.amro.assignments.recipes.beans.RecipeBean;
import com.abn.amro.assignments.recipes.beans.RecipeSearchBean;
import com.abn.amro.assignments.recipes.beans.SearchCriteria;
import com.abn.amro.assignments.recipes.entities.IngredientsEntity;
import com.abn.amro.assignments.recipes.entities.RecipeEntity;
import com.abn.amro.assignments.recipes.entities.RecipeStepEntity;
import com.abn.amro.assignments.recipes.repositories.IngredientsRepo;
import com.abn.amro.assignments.recipes.repositories.RecipeRepo;
import com.abn.amro.assignments.recipes.repositories.RecipeStepsRepo;
import com.abn.amro.assignments.recipes.repositories.UserRepo;
import com.abn.amro.assignments.recipes.specs.RecipeSpecBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService{
	
	private RecipeRepo recipeRepo;
	private IngredientsRepo ingredientsRepo;
	private UserRepo userRepo;
	
	@Autowired
	public RecipeServiceImpl(RecipeRepo recipeRepo, IngredientsRepo ingredientsRepo, 
			UserRepo userRepo) {
		this.recipeRepo = recipeRepo;
		this.ingredientsRepo = ingredientsRepo;
		this.userRepo = userRepo;
	}
	

	@Override
	@Transactional
	public RecipeBean createRecipe(RecipeBean recipe) {
		log.info("Service to create Recipe");
		List<IngredientsEntity> ingredientList = findCreateAllIngredients(recipe.getIngridients());
		log.info("ingredients listed");
		RecipeEntity entity = setRecipeValues(recipe, ingredientList);
		RecipeEntity finalEntity = recipeRepo.save(entity);
		recipe.setId(finalEntity.getId());
		return recipe;
	}

	@Override
	public RecipeBean updateRecipe(Long id, RecipeBean recipe) {
		Optional<RecipeEntity> recipeEntity = recipeRepo.findById(id);
		if(recipeEntity.isPresent()) {
			RecipeEntity entity = assignAllNonNullValues(recipeEntity.get(), recipe);
			recipeRepo.save(entity);
		}
		return recipe;
	}

	@Override
	public void deleteRecipe(Long id) {
		
		recipeRepo.deleteById(id);
	}
	private RecipeEntity assignAllNonNullValues(RecipeEntity entity, RecipeBean recipe) {
		List<IngredientsEntity> newIngredients = null;
			entity.setRecipeName(recipe.getRecipeName());
			entity.setNumOfServings(recipe.getNumOfServings());
			newIngredients = findCreateAllIngredients(recipe.getIngridients());
			entity.setIngridients(newIngredients);
			entity.setType(recipe.getType());
			entity.getSteps().clear();
			entity.getSteps().addAll(assignRecipeStepsToRecipe(recipe, entity));
		return entity;
	}
	
	private List<IngredientsEntity> findCreateAllIngredients(List<String> ingredientNames){
		List<IngredientsEntity> entityList = ingredientsRepo.findByNameIn(ingredientNames);
		
		if(entityList.size() < ingredientNames.size()) {//This means there are new ingrediants to be added to DB.
			List<String> ingredientsFound = new ArrayList<>();
			List<IngredientsEntity>newEntities = new ArrayList<>();
			entityList.stream()
			.forEach(entity -> ingredientsFound.add(entity.getName()));
			
			List<String> newIngredients = ingredientNames.stream()
					.filter(ingredient -> !ingredientsFound.contains(ingredient))
					.collect(Collectors.toList());
			
			log.info("New ingredients --> " + newIngredients);
//			log.info("Entity List --> " + entityList);
			
			newIngredients.stream().forEach(ingredient -> {
				IngredientsEntity entity = new IngredientsEntity();
				entity.setName(ingredient);
//				newEntities.add(entity);
				entityList.add(entity);
			});
			
//			ingredientsRepo.saveAll(newEntities);
//			entityList.addAll(newEntities);
		}
		return entityList;
	}

	private RecipeEntity setRecipeValues(RecipeBean recipe, List<IngredientsEntity> ingredientList) {
		RecipeEntity entity = new RecipeEntity();
		BeanUtils.copyProperties(recipe, entity);
		
		ObjectMapper mapper = new ObjectMapper();
		
//		ingredientList.forEach(ingredientEntity -> ingredientEntity.se);
		entity.setIngridients(ingredientList);
		entity.setSteps(assignRecipeStepsToRecipe(recipe, entity));
//		log.info("Final generated Entity --> " + entity);
		
		return entity;
	}


	private List<RecipeStepEntity> assignRecipeStepsToRecipe(RecipeBean recipe, RecipeEntity entity) {
		List<RecipeStepEntity> steps = new ArrayList<>();
		
		recipe.getSteps().forEach(instruction ->{
			RecipeStepEntity stepEntity = new RecipeStepEntity();
			stepEntity.setStepNum(steps.size()+1);
			stepEntity.setInstruction(instruction);
			stepEntity.setRecipe(entity);
			steps.add(stepEntity);
		});
		return steps;
	}


	@Override
	public Page<RecipeEntity> search(RecipeSearchBean bean, int pageNum, int pageSize) {
		
		RecipeSpecBuilder specBuilder = new RecipeSpecBuilder();
		List<SearchCriteria> criteriaList = bean.getSearchList();
		if(criteriaList != null) {
			criteriaList.forEach(criteria -> {
				criteria.setDataOption(bean.getDataOption());
				specBuilder.with(criteria);
			});
		}
		
		Pageable page = PageRequest.of(pageNum, pageSize);
		Page<RecipeEntity> recipes =  recipeRepo.findAll(specBuilder.build(), page);
		log.info("data received --> ", recipes);
		return recipes;
	}
}
