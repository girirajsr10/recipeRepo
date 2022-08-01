package com.abn.amro.assignments.recipes.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abn.amro.assignments.recipes.entities.IngredientsEntity;

public interface IngredientsRepo extends JpaRepository<IngredientsEntity, Long>{

	List<IngredientsEntity> findByNameIn(List<String> ingredients);
}
