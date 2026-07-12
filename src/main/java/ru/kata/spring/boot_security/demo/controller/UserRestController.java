package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.DTO.UserResponseDTO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;
import org.springframework.http.ResponseEntity;

import ru.kata.spring.boot_security.demo.DTO.UserRequestDTO;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userServiceImp;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserRestController(UserService userService, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {

        this.userServiceImp = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;

        System.out.println("=== UserRestController СОЗДАН ===");
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {

        return userServiceImp.findAll()
                .stream()
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getAge(),
                        user.getLogin(),
                        user.getRoles()
                                .stream()
                                .map(Role::getName)
                                .toList()
                ))
                .toList();
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @RequestBody UserRequestDTO userRequestDTO) {

        System.out.println("POST REST USER");

        User user = new User();
        user.setName(userRequestDTO.getName());
        user.setAge(userRequestDTO.getAge());
        user.setEmail(userRequestDTO.getEmail());
        user.setLogin(userRequestDTO.getLogin());
        user.setPassword(userRequestDTO.getPassword());
        List<Role> roles = userRequestDTO.getRoles()
                .stream()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() ->
                                new RuntimeException("Роль не найдена: " + roleId)))
                .collect(Collectors.toList());

        user.setRoles(roles);

        System.out.println("ПЕРЕД СОХРАНЕНИЕМ: " + user.getLogin());
        userServiceImp.save(user);
        System.out.println("ПОСЛЕ СОХРАНЕНИЯ ID: " + user.getId());

        UserResponseDTO response = new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getLogin(),
                user.getRoles()
                        .stream()
                        .map(Role::getName)
                        .toList()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable Long id) {

        User user = userServiceImp.findById(id);
        UserResponseDTO response = new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getLogin(),
                user.getRoles()
                        .stream()
                        .map(Role::getName)
                        .toList()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userServiceImp.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDTO userRequestDTO) {

        User user = userServiceImp.findById(id);

        String oldPassword = user.getPassword();
        user.setName(userRequestDTO.getName());
        user.setAge(userRequestDTO.getAge());
        user.setEmail(userRequestDTO.getEmail());
        user.setLogin(userRequestDTO.getLogin());

        List<Role> roles = userRequestDTO.getRoles()
                .stream()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() ->
                                new RuntimeException("Роль не найдена")))
                .collect(Collectors.toList());
        user.getRoles().clear();
        user.getRoles().addAll(roles);


        if (userRequestDTO.getPassword() != null &&
                !userRequestDTO.getPassword().isBlank()) {

            user.setPassword(userRequestDTO.getPassword());
        } else {
            User oldUser = userServiceImp.findById(id);
            user.setPassword(oldUser.getPassword());
        }

        userServiceImp.update(user);

        return ResponseEntity.ok(
                new UserResponseDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getAge(),
                        user.getLogin(),
                        user.getRoles()
                                .stream()
                                .map(Role::getName)
                                .toList()
                )
        );
    }
}

