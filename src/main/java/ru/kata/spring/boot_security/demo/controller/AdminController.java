package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserServiceImp userServiceImp;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminController(UserServiceImp userServiceImp, RoleRepository roleRepository) {
        this.userServiceImp = userServiceImp;
        this.roleRepository = roleRepository;
    }


    @GetMapping
    public String getAdmin(Model model, Authentication authentication) {
        User admin = (User) authentication.getPrincipal();

        model.addAttribute("admin", admin);
        model.addAttribute("userList", userServiceImp.findAll());
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleRepository.findAll());
        return "admin";
    }

    @PostMapping("/add")
    public String createUser(@Valid @ModelAttribute User user,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userList", userServiceImp.findAll());
            return "admin";
        }
        userServiceImp.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/api/user")
    @ResponseBody
    public Map<String, Object> getCurrentUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Map<String, Object> result = new HashMap<>();

        result.put("id", user.getId());
        result.put("name", user.getName());
        result.put("age", user.getAge());
        result.put("email", user.getEmail());
        result.put("roles",
                user.getRoles()
                        .stream()
                        .map(role -> role.getName())
                        .toList());
        return result;
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userServiceImp.deleteById(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin-update/{id}")  //Редактирование человека
    public String updateUserGet(@PathVariable long id, Model model) {
        model.addAttribute("user", userServiceImp.findById(id));
        model.addAttribute("allRoles", roleRepository.findAll());
        return "admin-update";
    }

    @PostMapping("/admin-update/{id}")
    public String updateUserPost(@PathVariable long id,
                                 @Valid @ModelAttribute User user,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin-update";
        }
        user.setId(id);
        userServiceImp.update(user);
        return "redirect:/admin";
    }
}
