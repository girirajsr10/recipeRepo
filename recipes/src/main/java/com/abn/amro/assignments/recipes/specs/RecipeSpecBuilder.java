package com.abn.amro.assignments.recipes.specs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.abn.amro.assignments.recipes.beans.SearchCriteria;
import com.abn.amro.assignments.recipes.entities.RecipeEntity;
import com.abn.amro.assignments.recipes.enums.SearchOperation;
import com.abn.amro.assignments.recipes.repositories.RecipeSpec;

public class RecipeSpecBuilder {
	
	private final List<SearchCriteria> params;
	
	public RecipeSpecBuilder(){
        this.params = new ArrayList<>();
    }

    public final RecipeSpecBuilder with(String key, 
                          String operation, Object value){
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public final RecipeSpecBuilder with(SearchCriteria
                    searchCriteria){
        params.add(searchCriteria);
        return this;
    }

    public Specification<RecipeEntity> build(){
        if(params.size() == 0){
            return null;
        }

        Specification<RecipeEntity> result = 
                   new RecipeSpec(params.get(0));
        for (int idx = 1; idx < params.size(); idx++){
            SearchCriteria criteria = params.get(idx);
            
            	result =  SearchOperation.getDataOption(criteria
            			.getDataOption()) == SearchOperation.ALL
            			? Specification.where(result).and(new    
            					RecipeSpec(criteria))
            					: Specification.where(result).or(
            							new RecipeSpec(criteria));
            	
        }
        return result;
    }
}
