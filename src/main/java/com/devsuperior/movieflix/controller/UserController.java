package com.devsuperior.movieflix.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.movieflix.dto.UserDTO;
import com.devsuperior.movieflix.dto.UserInsertDTO;
import com.devsuperior.movieflix.dto.UserUpdateDTO;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.services.AuthService;
import com.devsuperior.movieflix.services.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserController {
	
	@Autowired
	private UserService service;

	@Autowired
	private AuthService authService;
	
	@GetMapping
	public ResponseEntity<Page<UserDTO>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction,
			@RequestParam(value = "orderBy", defaultValue = "name") String orderBy// vai ordenar pelo atributo 'name' da
																					// classe User
	) {
		// Esses 4 annotations RequestParams são os parametros de paginação da aplicação

		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);

		Page<UserDTO> list = service.findAllPaged(pageRequest);
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<UserDTO> findById(@PathVariable Long id){		
		
		UserDTO user = service.findById(id);		
		
		return ResponseEntity.ok().body(user);
	}
	
	@GetMapping(value = "/profile")
	public ResponseEntity<UserDTO> getLoggedUser(){
		
		User user = new User();
		user = authService.authenticated();
		if(user != null) {
			return ResponseEntity.ok().body(new UserDTO(user));
		}		
		
		return ResponseEntity.notFound().build();
	}


	@PostMapping
	public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO dto) {		
		
		UserDTO newDto = service.insert(dto);
		if(newDto.errorList != null && newDto.errorList.size() > 0) {
			return ResponseEntity.unprocessableEntity().body(newDto);
		}
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(newDto.getId()).toUri();

		return ResponseEntity.created(uri).body(newDto);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<UserDTO> update(@PathVariable Long id,@Valid @RequestBody UserUpdateDTO dto) {
		UserDTO newDto = service.update(id, dto);
		if(newDto.errorList != null && newDto.errorList.size() > 0) {
			return ResponseEntity.unprocessableEntity().body(newDto);
		}

		return ResponseEntity.ok().body(newDto);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<UserDTO> delete(@PathVariable Long id) {
		service.delete(id);

		// vai retornar 204 que é que deu certo e o corpo da
		// resposta está vazio.
		return ResponseEntity.noContent().build();
	}

}
