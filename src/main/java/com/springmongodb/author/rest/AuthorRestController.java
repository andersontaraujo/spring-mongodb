package com.springmongodb.author.rest;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.springmongodb.author.Author;
import com.springmongodb.author.AuthorRepository;

@RestController
@RequestMapping("/authors")
public class AuthorRestController {
	
	@Autowired
	private AuthorRepository repository;
	
	@Autowired
	private ModelMapper mapper;
	
	@PostMapping
	public ResponseEntity<AuthorResource> create(@RequestBody AuthorResource resource) {
		Author entity = repository.save(mapper.map(resource, Author.class));
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{_id}").buildAndExpand(entity.get_id()).toUri();
		return ResponseEntity.created(uri).body(mapper.map(entity, AuthorResource.class));
	}
	
	@GetMapping
	public ResponseEntity<?> findAll() {
		List<Author> entities = repository.findAll();
		List<AuthorResource> resources = entities.stream().map(source -> mapper.map(source, AuthorResource.class)).collect(Collectors.toList());
		return ResponseEntity.ok().body(resources);
	}	
	
	@GetMapping("/{_id}")
	public ResponseEntity<AuthorResource> findById(@PathVariable String _id) {
		Optional<Author> author = repository.findById(new ObjectId(_id));
		if (!author.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().body(mapper.map(author.get(), AuthorResource.class));
	}
	
	@PutMapping("/{_id}")
	public ResponseEntity<AuthorResource> update(@PathVariable String _id, @RequestBody AuthorResource resource) {
		if (!_id.equals(resource.get_id())) {
			return ResponseEntity.badRequest().build();
		}
		Optional<Author> author = repository.findById(new ObjectId(_id));
		if (!author.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Author entity = repository.save(mapper.map(resource, Author.class));
		return ResponseEntity.ok().body(mapper.map(entity, AuthorResource.class));
	}
	
	@DeleteMapping("/{_id}")
	public ResponseEntity<?> delete(@PathVariable String _id) {
		Optional<Author> author = repository.findById(new ObjectId(_id));
		if (!author.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		repository.deleteById(new ObjectId(_id));
		return ResponseEntity.noContent().build();
	}

}
