package com.anthony.mediadatabase.model;

import org.springframework.data.repository.CrudRepository;

public interface TVShowRepository extends CrudRepository<TVShow, Long>{
	TVShow findById(long id);
}
