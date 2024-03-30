package com.devsuperior.movieflix.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.devsuperior.movieflix.entities.Review;
import com.fasterxml.jackson.annotation.JsonIgnore;



public class ReviewDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;	
	
	@NotBlank(message = "Não pode deixar este campo em branco")
	private String text;	

	private Long movieId;	
	
	private UserDTO user;	

	@JsonIgnore
	private MovieDTO movie;
	
	
	public ReviewDTO() {
		
	}

	public ReviewDTO(Long id, String text) {		
		this.id = id;
		this.text = text;
	}
	
	public ReviewDTO(Review entity) {
		this.id = entity.getId();
		this.text = entity.getText();
		this.movieId = entity.getMovie().getId();
		this.user = new UserDTO(entity.getUser());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}	
	

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public MovieDTO getMovie() {
		return movie;
	}

	public void setMovie(MovieDTO movie) {
		this.movie = movie;
	}

	public Long getMovieId() {
		return movieId;
	}

	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}

}
