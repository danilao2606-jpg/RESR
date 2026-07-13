package ru.kata.spring.boot_security.demo.DTO;

import lombok.Data;

import java.util.List;

@Data
public class UserRequestDTO {

    private String name;
    private int age;
    private String email;
    private String login;
    private String password;
    private List<Long> roles;

    public UserRequestDTO() {
    }

    public UserRequestDTO(String name,
                          int age,
                          String email,
                          String login,
                          String password,
                          List<Long> roles) {

        this.name = name;
        this.age = age;
        this.email = email;
        this.login = login;
        this.password = password;
        this.roles = roles;
    }
}