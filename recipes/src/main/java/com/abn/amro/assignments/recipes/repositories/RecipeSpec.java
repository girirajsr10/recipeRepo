package com.abn.amro.assignments.recipes.repositories;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;

import com.abn.amro.assignments.recipes.beans.SearchCriteria;
import com.abn.amro.assignments.recipes.entities.IngredientsEntity;
import com.abn.amro.assignments.recipes.entities.RecipeEntity;
import com.abn.amro.assignments.recipes.entities.RecipeStepEntity;
import com.abn.amro.assignments.recipes.entities.RecipesIngridients;
import com.abn.amro.assignments.recipes.enums.SearchOperation;

public class RecipeSpec implements Specification<RecipeEntity> {

	private final SearchCriteria searchCriteria;

	public RecipeSpec(SearchCriteria searchCriteria) {
		super();
		this.searchCriteria = searchCriteria;
	}

	@Override
	public Predicate toPredicate(Root<RecipeEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		// TODO Auto-generated method stub
		String strToSearch = searchCriteria.getValue().toString().toLowerCase();
		switch (SearchOperation.getSimpleOperation(searchCriteria.getOperation())) {
		case CONTAINS:
			if("ingredients".equals(searchCriteria.getFilterKey())) {
				return criteriaBuilder.like(criteriaBuilder.lower(ingredientsJoin(root).
	                       <String>get("name")),
	                       "%" + strToSearch + "%");
			}else if("instructions".equals(searchCriteria.getFilterKey())) {
				return criteriaBuilder.like(criteriaBuilder.lower(recipeStepJoin(root).
	                       <String>get("instruction")),
	                       "%" + strToSearch + "%");
			}
			return criteriaBuilder.like(criteriaBuilder.lower(root.get(searchCriteria.getFilterKey())),
					"%" + strToSearch + "%");
		case DOES_NOT_CONTAIN:
//			if("ingredients".equals(searchCriteria.getFilterKey())) {
//				Subquery<IngredientsEntity> ingredientsQuery = query.subquery(IngredientsEntity.class);
//				Root<IngredientsEntity> ingredientsQueryFrom = ingredientsQuery.from(IngredientsEntity.class);
////				ingredientsQuery.select(ingredientsQuery.from(clazz).get("id"));
//				
//				return criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.lower(ingredientsJoin(root).
//	                       <String>get("name")),
//	                       "%" + strToSearch + "%"), 
//						criteriaBuilder.equal(root.join("categories", JoinType.LEFT), ingredientsQueryFrom),
//						criteriaBuilder.like(criteriaBuilder.lower(recipeIngredientsJoin(root).
//			                       <String>get("ingredientsId")),
//			                       "%" + strToSearch + "%"));
////				return 
//			}else if("instructions".equals(searchCriteria.getFilterKey())) {
//				return criteriaBuilder.like(criteriaBuilder.lower(recipeStepJoin(root).
//	                       <String>get("instruction")),
//	                       "%" + strToSearch + "%");
//			}
			return criteriaBuilder.notLike(criteriaBuilder.lower(root.get(searchCriteria.getFilterKey())),
					"%" + strToSearch + "%");
		case EQUAL:
			return criteriaBuilder.equal(criteriaBuilder.lower(root.get(searchCriteria.getFilterKey())),
					searchCriteria.getValue());
		case NOT_EQUAL:
			return criteriaBuilder.notEqual(criteriaBuilder.lower(root.get(searchCriteria.getFilterKey())),
					searchCriteria.getValue());

		case GREATER_THAN:
			return criteriaBuilder.greaterThan(root.<String>get(searchCriteria.getFilterKey()),
					searchCriteria.getValue().toString());

		case GREATER_THAN_EQUAL:
			return criteriaBuilder.greaterThanOrEqualTo(root.<String>get(searchCriteria.getFilterKey()),
					searchCriteria.getValue().toString());

		case LESS_THAN:
			return criteriaBuilder.lessThan(root.<String>get(searchCriteria.getFilterKey()), searchCriteria.getValue().toString());

		case LESS_THAN_EQUAL:
			return criteriaBuilder.lessThanOrEqualTo(root.<String>get(searchCriteria.getFilterKey()),
					searchCriteria.getValue().toString());
		default:
			break;
		}
		return null;
	}
	private Join<RecipeEntity, IngredientsEntity> ingredientsJoin(Root<RecipeEntity> root){
		return root.join("ingridients"); //This is the key that is used in mapped by in entity.
	}
	
	private Join<RecipeEntity, RecipesIngridients> recipeIngredientsJoin(Root<RecipeEntity> root){
		return root.join("recipe"); //This is the key that is used in mapped by in entity.
	}
	
	private Join<RecipeEntity, RecipeStepEntity> recipeStepJoin(Root<RecipeEntity> root){
		return root.join("steps");
	}
}
