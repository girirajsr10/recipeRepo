package com.abn.amro.assignments.recipes.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@IdClass(RecipesIngridientsId.class)
@Table(name="recipes_ingridients")
public class RecipesIngridients {

	@Id
	@ManyToOne
	@JoinColumn(name = "recipe_id", insertable = false, updatable = false)
	private RecipeEntity recipeId;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "ingredient_id", insertable = false, updatable = false)
	private IngredientsEntity ingredientId;
}
