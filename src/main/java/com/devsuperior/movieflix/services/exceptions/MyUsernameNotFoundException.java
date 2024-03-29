package com.devsuperior.movieflix.services.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MyUsernameNotFoundException extends UsernameNotFoundException {

	private static final long serialVersionUID = 1L;
	
	public MyUsernameNotFoundException(String msg) {
		super(msg);
	}

}
