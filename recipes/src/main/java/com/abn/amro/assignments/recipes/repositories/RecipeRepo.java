package com.abn.amro.assignments.recipes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.abn.amro.assignments.recipes.entities.RecipeEntity;

public interface RecipeRepo extends JpaRepository<RecipeEntity, Long>,
	JpaSpecificationExecutor<RecipeEntity>{

}
