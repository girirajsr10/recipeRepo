package com.abn.amro.assignments.recipes.beans;

import lombok.Data;

@Data
public class SearchCriteria {

	private String filterKey;
	private Object value;
	private String operation;
	private String dataOption;

	public SearchCriteria(String filterKey, String operation, Object value) {
		super();
		this.filterKey = filterKey;
		this.operation = operation;
		this.value = value;
	}
}
