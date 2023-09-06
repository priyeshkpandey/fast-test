package com.util.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserSignUpRequest {
    private Long id;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
}
