package com.company.app.core.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


@Getter
@Setter
public class InputData {

    @NotNull(message = "username is required")
    @NotEmpty(message = "username is required")
    private String username;

    @NotNull(message = "password is required")
    @NotEmpty(message = "password is required")
    private String password;

}
