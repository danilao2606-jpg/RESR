package ru.kata.spring.boot_security.demo.DTO;

import java.util.List;

public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private int age;
    private String login;
    private List<String> roles;


    public UserResponseDTO(Long id,
                           String name,
                           String email,
                           int age,
                           String login,
                           List<String> roles) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.login = login;
        this.roles = roles;
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public String getLogin() {
        return login;
    }

    public List<String> getRoles() {
        return roles;
    }
}
