package com.example.demo.controller.mvc;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;

import com.example.demo.domain.User;
import com.example.demo.service.IUserService;
import com.example.demo.service.RoleService;


@Controller
@RequestMapping("/mvc/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final RoleService roleService;
    
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ver-usuario')")
    public String getAll(Model model) {
        model.addAttribute("users", userService.findAll());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auth.getAuthorities().forEach(authority -> System.out.println(authority.getAuthority()));
        return "users/list";
    }

    @GetMapping("/addUser")
    @PreAuthorize("hasAuthority('crear-usuario')")
    public String addUserForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.findAll());
        return "users/addUser";
    }

    @PostMapping("/addUser")
    @PreAuthorize("hasAuthority('crear-usuario')")
    public String addUser(@ModelAttribute User user) {
        userService.saveUser(user);  // ← era userService.save(user)
        System.out.println("User added: " + user.getFullName());
        return "redirect:/mvc/home";
    }

}
