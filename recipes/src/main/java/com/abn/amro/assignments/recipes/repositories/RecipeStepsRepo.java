package com.abn.amro.assignments.recipes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abn.amro.assignments.recipes.entities.RecipeStepEntity;

public interface RecipeStepsRepo extends JpaRepository<RecipeStepEntity, Long>{

}
