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
public class AdminRestController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminRestController(UserService userService, RoleRepository roleRepository) {

        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    private UserResponseDTO mapToDto(User user) {
        return new UserResponseDTO(
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
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {

        return userService.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @RequestBody UserRequestDTO userRequestDTO) {

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
        userService.save(user);
        return ResponseEntity.ok(mapToDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable Long id) {

        User user = userService.findById(id);
        return ResponseEntity.ok(mapToDto(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDTO userRequestDTO) {
        User user = userService.findById(id);
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
        userService.update(user);
        return ResponseEntity.ok(mapToDto(user));
    }
}

