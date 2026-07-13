package ru.kata.spring.boot_security.demo.DTO;

import lombok.Data;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

@Data
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

    public void setRoles(List<Role> roles) {
    }
}
