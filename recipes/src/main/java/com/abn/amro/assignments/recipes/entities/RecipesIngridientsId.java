package com.abn.amro.assignments.recipes.entities;

import java.io.Serializable;

import lombok.Data;

@Data
public class RecipesIngridientsId implements Serializable{

	private Long recipeId;
	private Long ingredientId;
}
