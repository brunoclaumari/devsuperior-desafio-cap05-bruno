package com.devsuperior.movieflix.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.devsuperior.movieflix.controller.exceptions.FieldMessage;
import com.devsuperior.movieflix.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;


public class UserDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;	

	private Long id;	
	
	private String name;	
	
	private String email;
	

	@JsonIgnore
	Set<ReviewDTO> reviews = new HashSet<>();		

	
	private Set<RoleDTO> roles = new HashSet<>();
	
	public List<FieldMessage> errorList = new ArrayList<>();
	
	public UserDTO() {
		
	}

	public UserDTO(Long id, String name, String email) {		
		this.id = id;
		this.name = name;
		this.email = email;		
	}
	
	public UserDTO(User user) {		
		this.id = user.getId();
		this.name = user.getName();
		this.email = user.getEmail();		
		user.getRoles().forEach(role->this.roles.add(new RoleDTO(role)));
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<ReviewDTO> getReviews() {
		return reviews;
	}

	public void setReviews(Set<ReviewDTO> reviews) {
		this.reviews = reviews;
	}

	public Set<RoleDTO> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleDTO> roles) {
		this.roles = roles;
	}
	

}
