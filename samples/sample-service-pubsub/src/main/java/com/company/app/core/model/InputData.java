package com.company.app.core.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class InputData {

    @NotEmpty(message = "Please enter a username")
    private String username;

    @NotEmpty(message = "Please enter a password")
    private String password;

}
