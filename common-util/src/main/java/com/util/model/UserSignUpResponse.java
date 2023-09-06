package com.util.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class UserSignUpResponse {
    private Long userId;
    private List<String> roles;
    private List<Permission> permissions;
}
