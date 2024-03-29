package com.devsuperior.movieflix.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.GenreDTO;
import com.devsuperior.movieflix.dto.MovieDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.repositories.GenreRepository;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.DatabaseException;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;


@Service
public class MovieService{
	
	private static Logger logger = LoggerFactory.getLogger(MovieService.class);
	
	@Autowired
	private MovieRepository repository;	
	
	@Autowired
	private GenreRepository genreRepository;	
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	/*
	 * @Transactional(readOnly = true)
	public Page<MovieDTO> findAllPaged(Long GenreId, String name, PageRequest pageRequest) {
		List<Genre> categories = (GenreId == 0) ? null : Arrays.asList(GenreRepository.getOne(GenreId));

		Page<Movie> list = repository.find(categories, name, pageRequest);
		// Usando 'expressão lambda' para transferir Movie para MovieDTO
		return list.map(x -> new MovieDTO(x));
	}*/
	
	@Transactional(readOnly = true)
	public Page<MovieDTO> findAll(Pageable pageable) {
		Page<Movie> page = repository.findAll(pageable);
		return page.map(x -> new MovieDTO(x));
	} 
	
	@Transactional(readOnly = true)
	public Page<MovieDTO> findAllPaged(Long genreId, PageRequest pageRequest) {
		List<Genre> genres = (genreId == 0) ? null : Arrays.asList(genreRepository.getOne(genreId));

		Page<Movie> list = repository.findWithFilterGenreId(genres, pageRequest);
		// Usando 'expressão lambda' para transferir Movie para MovieDTO
		return list.map(x -> new MovieDTO(x));
	}
	
	//Movie reviews
	///movies/1/reviews
	public List<MovieDTO> findMovieReviews(Long movieId){
		
		
		return null;
	}

	@Transactional(readOnly = true)
	public MovieDTO findById(Long id) {
		/*
		 * No lugar de um try/catch é usado o método 'orElseThrow' que lança a exceção
		 * personalizada criada caso o 'obj' não traga valores na requisição.
		 */
		Optional<Movie> obj = repository.findById(id);

		Movie entity = obj.orElseThrow(() -> new ResourceNotFoundException(String.format("Movie not found. Id: %s",id)));

		return new MovieDTO(entity, entity.getReviews());
	}
	
	@Transactional
	public MovieDTO insert(MovieDTO dto) {
		Movie entity = new Movie();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);

		return new MovieDTO(entity);
	}

	@Transactional
	public MovieDTO update(Long id, MovieDTO dto) {
		try {
			Movie entity = repository.getOne(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new MovieDTO(entity);

		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found: " + id);
		}

	}

	// Único sem Transactional, pois tem que capturar uma exceção e o transactional
	// não deixaria
	public void delete(Long id) {
		String message = "";
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			message = "Id not found: " + id;
			logger.error(message);
			throw new ResourceNotFoundException(message);
		} catch (DataIntegrityViolationException e) {
			message = "Integrity database Violation!";
			throw new DatabaseException(message);
		}

	}

	private void copyDtoToEntity(MovieDTO dto, Movie entity) {

		entity.setTitle(dto.getTitle());
		entity.setSubTitle(dto.getSubTitle());
		entity.setYear(dto.getYear());
		entity.setImgUrl(dto.getImgUrl());
		entity.setSynopsis(dto.getSynopsis());
		entity.setGenre(copyDtoToGenre(dto.getGenreDTO()));

		entity.getReviews().clear();

		dto.getReviews().forEach(reviewDto -> {
			// Category cat=categoryRepository.getOne(catDto.getId());
			entity.getReviews().add(reviewRepository.getOne(reviewDto.getId()));
		});

	}
	
	private Genre copyDtoToGenre(GenreDTO dto) {
		
		return new Genre(dto.getId(), dto.getName());
	}


	

}
