package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import javax.validation.Valid;


@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")  //Отображение данных
    public String getUser(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        User freshUser = userService.findById(user.getId());
        model.addAttribute("user", freshUser);
        return "user";
    }

    @GetMapping("/user-update/{id}")  //Редактирование человека
    public String updateUserGet(@PathVariable long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "user-update";
    }

    @PostMapping("/user-update/{id}")
    public String updateUserPost(@PathVariable long id,
                                 @Valid @ModelAttribute User user,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println("=== ОШИБКИ ВАЛИДАЦИИ ===");
            bindingResult.getAllErrors().forEach(e -> System.out.println(e.getDefaultMessage()));
            return "user-update";
        }
        user.setId(id);
        userService.update(user);
        System.out.println("=== РЕДИРЕКТ НА /user ===");
        return "redirect:/user";
    }
}
