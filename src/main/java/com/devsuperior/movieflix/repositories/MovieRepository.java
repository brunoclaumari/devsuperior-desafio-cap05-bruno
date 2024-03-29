package com.devsuperior.movieflix.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
	
	/*
	 * 	@Query("SELECT DISTINCT mov FROM Movie mov "
			+ "INNER JOIN mov.genres cats WHERE "
			+ "(COALESCE(:categories) IS NULL OR cats IN :genres) AND "
			+ "(LOWER(obj.name) LIKE LOWER(CONCAT('%',:name,'%')) )")
	 * */
	
	
	@Query("SELECT DISTINCT mov FROM Movie mov "
			+ "INNER JOIN mov.genre gen WHERE "
			+ "(COALESCE(:genres) IS NULL OR gen IN :genres)")
	Page<Movie> findWithFilterGenreId(List<Genre> genres, Pageable pageable);

}
