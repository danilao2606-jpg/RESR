package ru.kata.spring.boot_security.demo.DTO;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Long> getRoles() {
        return roles;
    }

    public void setRoles(List<Long> roles) {
        this.roles = roles;
    }
}