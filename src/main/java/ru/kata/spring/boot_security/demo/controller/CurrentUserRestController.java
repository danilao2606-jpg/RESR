package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.DTO.UserResponseDTO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

@RestController
@RequestMapping("/api")
public class CurrentUserRestController {

    private final UserService userServiceImp;

    public CurrentUserRestController(UserService userServiceImp) {
        this.userServiceImp = userServiceImp;
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponseDTO> getCurrentUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        User freshUser = userServiceImp.findById(user.getId());
        UserResponseDTO response = new UserResponseDTO(
                freshUser.getId(),
                freshUser.getName(),
                freshUser.getEmail(),
                freshUser.getAge(),
                freshUser.getLogin(),
                freshUser.getRoles()
                        .stream()
                        .map(Role::getName)
                        .toList()
        );
        return ResponseEntity.ok(response);
    }
}
