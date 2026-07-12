package ru.kata.spring.boot_security.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message = "Имя пожалуйста)")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "Ты вводишь свой ник? А нужно имя)")
    @Size(min = 2, max = 30, message = "Странное имя, нужно от 2 до 30 букв в имени")
    @Column (name = "name", unique = true)
    private String name;                                           //Имя

    @NotEmpty(message = "Почту пожалуйста =)")
    @Email(message = "Email не подходит как бы...")
    @Column (name = "email")
    private String email;                                          //Почта

    @Max(value = 100, message = "Ты выглядишь моложе")
    @Min(value = 0, message = "А это точно твой возраст?")
    @Column(name = "age")
    private int age;                                               //Возраст

    @Column (name = "login", unique = true)
    private String login;                                          //Логин

    @JsonIgnore
    @Column (name = "password", unique = true)
    private String password;                                       //Пароль

    @Transient
    private String passwordConfirm;                               //Проверка пароля

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )                                                             //Роль
    private List<Role> roles = new ArrayList<>();;

    public User() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
