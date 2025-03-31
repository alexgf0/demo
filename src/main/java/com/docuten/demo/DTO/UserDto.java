package com.docuten.demo.DTO;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class UserDto {
    private UUID id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "First surname is required")
    private String firstSurname;

    // optional
    private String secondSurname;

    public UUID getId() {
        return id;
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
