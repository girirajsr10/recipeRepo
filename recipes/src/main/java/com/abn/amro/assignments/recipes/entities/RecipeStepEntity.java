package com.abn.amro.assignments.recipes.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Entity
@Table(name="recipe_steps")
public class RecipeStepEntity implements Serializable{

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
	
	private int stepNum;
	
	private String instruction;
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recipe_id")
	private RecipeEntity recipe;
	
	
}
