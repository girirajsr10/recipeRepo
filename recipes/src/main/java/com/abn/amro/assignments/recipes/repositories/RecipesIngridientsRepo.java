package com.abn.amro.assignments.recipes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abn.amro.assignments.recipes.entities.RecipesIngridients;
import com.abn.amro.assignments.recipes.entities.RecipesIngridientsId;


public interface RecipesIngridientsRepo extends JpaRepository<RecipesIngridients, RecipesIngridientsId> {

}
