package com.anthony.mediadatabase.repository;

import org.springframework.data.repository.CrudRepository;

import com.anthony.mediadatabase.model.Season;

public interface SeasonRepository extends CrudRepository<Season, Long>{
	Season findById(long id);
}
