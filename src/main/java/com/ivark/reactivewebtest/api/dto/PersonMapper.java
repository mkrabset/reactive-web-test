package com.ivark.reactivewebtest.api.dto;

import com.ivark.reactivewebtest.domain.Person;

public class PersonMapper {
    public static PersonDto toDto(Person person) {
        PersonDto dto = new PersonDto();
        dto.id=person.id;
        dto.firstName=person.firstName;
        dto.lastName=person.lastName;
        dto.city=person.city;
        return dto;
    }
}
