package com.abn.amro.assignments.recipes.beans;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class RecipeBean {

	private Long id;
	
	@Size(min=2, message = "Recipe name cant be null")
	private String recipeName;
	
	@Size(min = 1, message = "Recipe should have at least one ingredient")
	private List<String> ingridients;
	
	@Min(value = 1, message = "Recipe should serve at least one person")
	private int numOfServings;
	
	
	private String type;
	
	@Size(min = 1, message = "Recipe should have steps")
	private List<String> steps;
}
