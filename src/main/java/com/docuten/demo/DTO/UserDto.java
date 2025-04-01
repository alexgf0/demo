package com.docuten.demo.DTO;

import com.docuten.demo.exceptions.ArgumentRequiredException;

import java.util.UUID;

public class UserDto {
    private UUID id;

    private String name;

    private String firstSurname;

    // optional
    private String secondSurname;

    public UUID getId() {
        return id;
    }

    public void checkRequiredFields() throws ArgumentRequiredException {
        if (name == null) {
            throw new ArgumentRequiredException("name is a required field");
        }

        if (firstSurname == null) {
            throw new ArgumentRequiredException("firstSurname is a required field");
        }
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstSurname() {
        return firstSurname;
    }

    public void setFirstSurname(String firstSurname) {
        this.firstSurname = firstSurname;
    }

    public String getSecondSurname() {
        return secondSurname;
    }

    public void setSecondSurname(String secondSurname) {
        this.secondSurname = secondSurname;
    }
}
