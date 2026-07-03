package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Primary
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(new User());
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login);
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return user;
    }

    @Transactional
    public void update(User user) {
        if (user == null) {
            throw new NullPointerException("Exception: человек для изменения не найден!");
        } else {
            System.out.println("=== ОБНОВЛЕНИЕ: " + user.getName() + ", " + user.getEmail());
            user.setRoles(Collections.singletonList(new Role(1L, "ROLE_USER")));
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            System.out.println("=== ПОЛЬЗОВАТЕЛЬ СОХРАНЁН");
        }
    }
}
