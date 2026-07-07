package ru.kata.spring.boot_security.demo.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Slf4j
public class Init {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public Init(UserRepository userRepository,
                    RoleRepository roleRepository,
                    BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void run() {
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));

        User user = userRepository.findByLogin("user");
        if (user == null) {
            user = new User();
            user.setName("User");
            user.setLogin("user");
            user.setEmail("user@mail.ru");
            user.setAge(25);
            user.setPassword(passwordEncoder.encode("user"));
            user.setRoles(List.of(userRole));
            userRepository.save(user);
            log.info("Пользователь 'user' создан: логин -> user   пароль -> user");
        }
        User admin = userRepository.findByLogin("admin");
        if (admin == null) {
            admin = new User();
            admin.setName("Admin");
            admin.setLogin("admin");
            admin.setEmail("admin@mail.ru");
            admin.setAge(25);
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles(List.of(adminRole));
            userRepository.save(admin);
            log.info("Пользователь 'admin' создан: логин -> admin   пароль -> admin");
        }
    }
}
