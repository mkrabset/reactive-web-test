package com.ivark.reactivewebtest.repository;

import com.ivark.reactivewebtest.domain.Person;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends ReactiveMongoRepository<Person, String> {
    //public Person findByFirstName(String firstName);
    //public List<Person> findByLastName(String lastName);
}
