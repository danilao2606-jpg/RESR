package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.AdminService;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

@Controller
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostConstruct
    public void init() {
        System.out.println("Перейдите по ссылке : http://localhost:8080/login");
    }

    @GetMapping("/admin")  //Отображение данных
    public String getAdmin(Model model) {
        model.addAttribute("userList", adminService.findAll());
        model.addAttribute("user", new User());
        return "admin";
    }

    @PostMapping("/admin/add") //Сохранение нового клиента
    public String createUser(@Valid @ModelAttribute User user,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            System.out.println("=== КОНТРОЛЛЕР: login = " + user.getLogin());
            model.addAttribute("userList", adminService.findAll());
            return "admin";
        }
        adminService.save(user);
        return "redirect:/admin";
    }

    @PostMapping("admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        adminService.deleteById(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin-update/{id}")  //Редактирование человека
    public String updateUserGet(@PathVariable long id, Model model) {
        model.addAttribute("user", adminService.findById(id));
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
        adminService.update(user);
        System.out.println("=== РЕДИРЕКТ НА /admin ===");
        return "redirect:/admin";
    }
}
