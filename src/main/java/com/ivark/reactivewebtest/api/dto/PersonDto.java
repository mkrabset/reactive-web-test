package com.ivark.reactivewebtest.api.dto;

import org.springframework.hateoas.RepresentationModel;

public class PersonDto extends RepresentationModel<PersonDto> {
    public String id;
    public String firstName;
    public String lastName;
    public String city;
}
