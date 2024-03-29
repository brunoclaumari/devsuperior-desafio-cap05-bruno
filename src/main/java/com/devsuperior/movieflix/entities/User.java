package com.devsuperior.movieflix.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.JoinColumn;

@Entity
@Table(name = "tb_user")
public class User implements  UserDetails, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;	
	
	private String name;	
	
	private String email;
	
	private String password;
	
	/*
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<OrderDetail> orderDetail = new HashSet();
	 * */
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "user")
	Set<Review> reviews = new HashSet<>();	
	
	@ManyToMany(fetch = FetchType.EAGER) // metodo EAGER já traz os Roles respectivos do User
	@JoinTable(
			name = "tb_user_role", 
			joinColumns = @JoinColumn(name = "user_id"), 
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	
	public User() {
		
	}

	public User(Long id, String name, String email, String password) {		
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Review> getReviews() {
		return reviews;
	}

	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(email, other.email) && Objects.equals(id, other.id);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}

	@Override
	public boolean isEnabled() {
		
		return true;
	}
	
	
	public boolean hasRole(String roleName) {
		/*
		 userLogged.getRoles().stream()
				.filter(x->x.getAuthority().equals("ROLE_MEMBER"))
				.collect(Collectors.toList())
		 * */
		return this.roles.stream()
				.filter(x->x.getAuthority().equals(roleName))
				.collect(Collectors.toList()).size() > 0;
	}
	
	
	

}
