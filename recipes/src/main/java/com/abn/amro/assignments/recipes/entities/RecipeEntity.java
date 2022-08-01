package com.abn.amro.assignments.recipes.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Data
@Entity
@Table(name="recipes")
public class RecipeEntity implements Serializable{
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String recipeName;
	
	@JsonManagedReference
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable( 
			  joinColumns = @JoinColumn(name = "recipe_id"), 
			  inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
	private List<IngredientsEntity> ingridients;
	
	private int numOfServings;
	
	private String type;
	
	@OneToOne
	private UserEntity user;
	
	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "recipe")
	private List<RecipeStepEntity> steps;
	
	
}
