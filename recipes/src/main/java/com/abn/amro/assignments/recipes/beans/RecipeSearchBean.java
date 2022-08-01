package com.abn.amro.assignments.recipes.beans;

import java.util.List;

import lombok.Data;

@Data
public class RecipeSearchBean {

	private List<SearchCriteria> searchList;
	private String dataOption;
}
