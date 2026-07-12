package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImp(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    @Override
    public void save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User is null");
        }
        user.setPassword(
                bCryptPasswordEncoder.encode(user.getPassword())
        );
        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public User findById(long id) {
        return userRepository.findByIdWithRoles(id)
                .orElseThrow(() -> new RuntimeException("Человек не найден"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return userRepository.findAllWithRoles();
    }

    @Override
    @Transactional
    public void update(User user) {
        User oldUser = findById(user.getId());
        oldUser.setName(user.getName());
        oldUser.setAge(user.getAge());
        oldUser.setEmail(user.getEmail());
        oldUser.setLogin(user.getLogin());
        oldUser.setRoles(user.getRoles());
        if (user.getPassword() != null &&
                !user.getPassword().startsWith("$2a$")) {
            oldUser.setPassword(
                    bCryptPasswordEncoder.encode(user.getPassword())
            );
        } else {
            oldUser.setPassword(user.getPassword());
        }
        userRepository.save(oldUser);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }
}
