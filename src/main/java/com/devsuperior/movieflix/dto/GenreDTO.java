package com.devsuperior.movieflix.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;

import com.devsuperior.movieflix.entities.Genre;
import com.fasterxml.jackson.annotation.JsonIgnore;


public class GenreDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;	
	
	private Long id;	
	
	@NotBlank(message = "Campo obrigatório!")
	private String name;	
	
	@JsonIgnore
	Set<MovieDTO> movies = new HashSet<>();	
	
	public GenreDTO() {
		
	}

	public GenreDTO(Long id, String name) {		
		this.id = id;
		this.name = name;
	}
	
	public GenreDTO(Genre genre) {		
		this.id = genre.getId();
		this.name = genre.getName();
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	public Set<MovieDTO> getMovies() {
		return movies;
	}

	public void setMovies(Set<MovieDTO> movies) {
		this.movies = movies;
	}	

}
