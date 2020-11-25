package com.anthony.mediadatabase.model;

import org.springframework.data.repository.CrudRepository;

public interface SeasonRepository extends CrudRepository<Season, Long>{
	Season findById(long id);
}
