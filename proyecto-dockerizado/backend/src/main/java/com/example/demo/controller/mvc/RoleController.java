package com.example.demo.controller.mvc;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

import com.example.demo.domain.Permission;
import com.example.demo.domain.Role;
import com.example.demo.domain.RolePermission;
import com.example.demo.service.IPermissionService;
import com.example.demo.service.IUserService;
import com.example.demo.service.RolePermissionService;
import com.example.demo.service.RoleService;

@Controller
@RequestMapping("/mvc/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final IPermissionService permissionService;
    private final RolePermissionService rolePermissionService;
    private final IUserService userService;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ver-rol')")
    public String getAll(Model model) {
        model.addAttribute("roles", roleService.findAll());
        return "roles/list";
    }

    @GetMapping("/addRole")
    @PreAuthorize("hasAuthority('crear-rol')")
    public String addRoleForm(Model model) {
        Role role = new Role();
        model.addAttribute("role", role);
        return "roles/addRole";
    }

    @PostMapping("/addRole")
    @PreAuthorize("hasAuthority('crear-rol')")
    public String addRole(@ModelAttribute Role role) {
        roleService.save(role);
        System.out.println("Role created: " + role.getName());
        return "redirect:/mvc/home";
    }

    @GetMapping("/{roleId}/permissions")
    @PreAuthorize("hasAuthority('crear-rol')")
    public String assignPermissionsView(@PathVariable Long roleId, Model model) {
        Role role = roleService.findById(roleId);
        List<Permission> permissions = permissionService.findAll();
        List<RolePermission> assignedPermissions = rolePermissionService.findByRoleId(roleId);

        model.addAttribute("role", role);
        model.addAttribute("permissions", permissions);
        model.addAttribute("assignedPermissions", assignedPermissions);

        return "roles/assignPermissions";
    }

    @PostMapping("/{roleId}/permissions")
    @PreAuthorize("hasAuthority('crear-rol')")
    public String assignPermissionToRole(@PathVariable Long roleId,
                                         @RequestParam Integer permissionId) {
        Role role = roleService.findById(roleId);
        Permission permission = permissionService.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found"));

        boolean alreadyExists = rolePermissionService.existsByRoleIdAndPermissionId(roleId, permissionId);

        if (!alreadyExists) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRole(role);
            rolePermission.setPermission(permission);
            rolePermissionService.save(rolePermission);
        }

        return "redirect:/mvc/roles/" + roleId + "/permissions";
    }

    @GetMapping("/assign-user-role")
    @PreAuthorize("hasAuthority('crear-rol')")
    public String assignUserRoleView(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", roleService.findAll());
        return "roles/assignUserRole";
    }

    @PostMapping("/assign-user-role")
    @PreAuthorize("hasAuthority('crear-rol')")
    public String assignRoleToUser(@RequestParam Integer userId,
                                   @RequestParam Long roleId) {
        userService.assignRole(userId, roleId);
        return "redirect:/mvc/roles/assign-user-role";
    }

    @GetMapping("/delete")
    @PreAuthorize("hasAuthority('crear-rol')")
    public String deleteRoleView(Model model) {
        model.addAttribute("roles", roleService.findAll());
        return "roles/deleteRole";
    }
    
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('crear-rol')")
    public String deleteRole(@PathVariable Long id) {
        roleService.deleteById(id);
        return "redirect:/mvc/roles/delete";
    }

    @GetMapping("/unassign-user-role")
    @PreAuthorize("hasAuthority('crear-rol')")
    public String unassignUserRoleView(Model model) {
        model.addAttribute("users", userService.findAll());
        return "roles/unasign-user-role";
    }

    @PostMapping("/unassign-user-role")
    @PreAuthorize("hasAuthority('crear-rol')")
    public String unassignRoleFromUser(@RequestParam Integer userId) {
        userService.unassignRole(userId);
        return "redirect:/mvc/roles/unassign-user-role";
    }
}