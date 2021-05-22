package com.ivark.reactivewebtest.domain;

import org.springframework.data.annotation.Id;

public class Person {
    @Id
    public String id;

    public String firstName;
    public String lastName;
    public String city;

    public Person() {}

    public Person(String firstName, String lastName, String city) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city=city;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
