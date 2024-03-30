package com.devsuperior.movieflix.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.dto.UserDTO;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.repositories.UserRepository;
import com.devsuperior.movieflix.services.exceptions.DatabaseException;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;


@Service
public class ReviewService{
	
	private static Logger logger = LoggerFactory.getLogger(ReviewService.class);

	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;


	@Transactional
	public ReviewDTO insert(ReviewDTO dto) {
		Review entity = new Review();
		copyDtoToEntity(dto, entity);
		entity = reviewRepository.save(entity);

		return new ReviewDTO(entity);
	}

//	@Transactional
//	public MovieDTO update(Long id, MovieDTO dto) {
//		try {
//			Movie entity = reviewRepository.getOne(id);
//			copyDtoToEntity(dto, entity);
//			entity = repository.save(entity);
//			return new MovieDTO(entity);
//
//		} catch (EntityNotFoundException e) {
//			throw new ResourceNotFoundException("Id not found: " + id);
//		}
//
//	}

	// Único sem Transactional, pois tem que capturar uma exceção e o transactional
	// não deixaria
	public void delete(Long id) {
		String message = "";
		try {
			reviewRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			message = "Id not found: " + id;
			logger.error(message);
			throw new ResourceNotFoundException(message);
		} catch (DataIntegrityViolationException e) {
			message = "Integrity database Violation!";
			throw new DatabaseException(message);
		}

	}

	private void copyDtoToEntity(ReviewDTO dto, Review entity) {

		UserDTO userDto = userService.getLoggedUser();
		//dto.setUser(userDto);
		
		entity.setId(dto.getId());
		entity.setText(dto.getText());
		var movie = movieRepository.findById(dto.getMovieId());
		if(movie != null && movie.get() != null) {			
			entity.setMovie(movie.get());
		}
		
		var user = userRepository.findById(userDto.getId()).get();
		if(user != null)
			entity.setUser(user);
		//entity.setUser(new User(userDto.getId(), userDto.getName(), userDto.getEmail(), null));


	}
	
//	private Genre copyDtoToGenre(GenreDTO dto) {
//		
//		return new Genre(dto.getId(), dto.getName());
//	}


	

}
