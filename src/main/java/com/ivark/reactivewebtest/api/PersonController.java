package com.ivark.reactivewebtest.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ivark.reactivewebtest.api.dto.PersonDto;
import com.ivark.reactivewebtest.api.dto.PersonMapper;
import com.ivark.reactivewebtest.domain.Person;
import com.ivark.reactivewebtest.repository.PersonRepository;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RestController()
@RequestMapping(path = "/persons")
public class PersonController {

    private final PersonRepository personRepository;
    private final ObjectMapper objectMapper;

    private final WebClient NATIONALIZE_CLIENT = WebClient.create("https://api.nationalize.io");


    public PersonController(PersonRepository personRepository, ObjectMapper objectMapper) {
        this.personRepository = personRepository;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<PersonDto>> getPerson(@PathVariable("id") String id) {
        return personRepository.findById(id)
                .map(PersonMapper::toDto)
                .map(p -> p.add(personLink(id)))
                .map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                .switchIfEmpty(Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

    @GetMapping()
    public Flux<PersonDto> getPerson() {
        return personRepository.findAll()
                .map(PersonMapper::toDto)
                .map(p -> p.add(personLink(p.id)));
    }

    @PostMapping()
    public Mono<ResponseEntity<PersonDto>> addPerson(@RequestBody PersonDto newPerson, UriComponentsBuilder uriBuilder) {
        Mono<Person> saved = personRepository.save(new Person(newPerson.firstName, newPerson.lastName, newPerson.city));
        return saved
                .map(PersonMapper::toDto)
                .map(p -> created(uriBuilder.path("/persons/{id}").build(p.id)).build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePerson(@PathVariable("id") String id) {
        return personRepository.deleteById(id)
                .map(v -> ok().build());
    }


    @GetMapping("/{id}/info")
    public Mono<ResponseEntity<JsonNode>> getWebInfo(@PathVariable("id") String id) {
        return personRepository.findById(id)
                .flatMap(person -> NATIONALIZE_CLIENT.get()
                        .uri("/?name={name}", person.firstName)
                        .retrieve()
                        .bodyToMono(JsonNode.class)
                        .map(body -> {
                            //if (1 == 1) throw new RuntimeException("Went south!");
                            ObjectNode root = objectMapper.createObjectNode();
                            root.set("probable_nationalities", body);
                            return (JsonNode) root;
                        })
                        .map(body -> new ResponseEntity<>(body, HttpStatus.OK)))
                .switchIfEmpty(Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

    private Link personLink(String id) {
        return linkTo(PersonController.class).slash(id).withSelfRel();
    }

}
