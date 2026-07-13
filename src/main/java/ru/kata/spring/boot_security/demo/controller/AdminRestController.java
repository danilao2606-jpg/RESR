package ru.kata.spring.boot_security.demo.controller;


import org.mapstruct.control.MappingControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.DTO.UserResponseDTO;
import ru.kata.spring.boot_security.demo.mapper.UserMapper;
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
    private final UserMapper userMapper;

    @Autowired
    public AdminRestController(UserService userService, RoleRepository roleRepository, UserMapper userMapper) {

        this.userService = userService;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {

        return userService.findAll()
                .stream()
                .map(userMapper::toResponseDTO)
                .toList();
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @RequestBody UserRequestDTO userRequestDTO) {

        User user =  userMapper.toEntity(userRequestDTO);
        List<Role> roles = userRequestDTO.getRoles()
                .stream()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() ->
                                new RuntimeException("Роль не найдена: " + roleId)))
                .collect(Collectors.toList());
        user.setRoles(roles);
        userService.save(user);
        return ResponseEntity.ok(userMapper.toResponseDTO(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable Long id) {

        User user = userService.findById(id);
        return ResponseEntity.ok(userMapper.toResponseDTO(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDTO dto) {
        User user = userService.findById(id);
        userMapper.updateUserController(dto, user);
        List<Role> roles = dto.getRoles()
                .stream()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() ->
                                new RuntimeException("Роль не найдена")))
                .collect(Collectors.toList());
        user.getRoles().clear();
        user.getRoles().addAll(roles);
        userService.update(user);
        return ResponseEntity.ok(userMapper.toResponseDTO(user));
    }
}

