package com.abn.amro.assignments.recipes.services;

import org.springframework.data.domain.Page;

import com.abn.amro.assignments.recipes.beans.RecipeBean;
import com.abn.amro.assignments.recipes.beans.RecipeSearchBean;
import com.abn.amro.assignments.recipes.entities.RecipeEntity;

public interface RecipeService {

	RecipeBean createRecipe(RecipeBean recipe);
	
	RecipeBean updateRecipe(Long id, RecipeBean recipe);
	
	void deleteRecipe(Long id);
	
	Page<RecipeEntity> search(RecipeSearchBean bean, int pageNum, int pageSize);
	
}
