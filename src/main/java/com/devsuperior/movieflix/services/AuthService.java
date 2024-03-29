package com.devsuperior.movieflix.services;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.UserDTO;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.UserRepository;
import com.devsuperior.movieflix.services.exceptions.ForbiddenException;
import com.devsuperior.movieflix.services.exceptions.UnauthorizedException;

@Service
public class AuthService {

	@Autowired
	UserRepository userRepository;
	
	@Transactional(readOnly = true)
	public User authenticated() {
		
		try {
			Authentication loggedUser = SecurityContextHolder.getContext().getAuthentication();
			if(!(loggedUser instanceof AnonymousAuthenticationToken)) {
				String email = loggedUser.getName();
				User user = userRepository.findByEmail(email);
				return user;
			}
			
		} catch (Exception e) {
			throw new UnauthorizedException("Invalid user");
		}
		
		return null;
	}
	
	public void validateSelfOrMember(Long userId) {
		
		User userLogged = authenticated();		
		
		if(!userLogged.hasRole("ROLE_MEMBER") && !userLogged.getId().equals(userId)) {
			throw new ForbiddenException("Access denied!!!");
		}
		
	}
	
}
