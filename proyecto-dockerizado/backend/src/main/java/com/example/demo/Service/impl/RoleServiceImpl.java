package com.example.demo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.domain.Role;
import com.example.demo.domain.RolePermission;
import com.example.demo.domain.User;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.controller.rest.dto.RoleRequest;
import com.example.demo.Repository.IUserRepository;
import com.example.demo.service.RoleService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import com.example.demo.Repository.RolePermissionRepository;
import com.example.demo.domain.Permission;

@Service
public class RoleServiceImpl implements RoleService {

    @PersistenceContext
    private EntityManager entityManager;
    private final RoleRepository roleRepository;
    private final IUserRepository userRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public RoleServiceImpl(RoleRepository roleRepository, IUserRepository userRepository, RolePermissionRepository rolePermissionRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.rolePermissionRepository = rolePermissionRepository;

    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role findById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + id));
    }

    @Override
    @Transactional // Importar de org.springframework.transaction.annotation.Transactional
    public Role save(Role role) {
        // 1. Validar que la lista de permisos no sea nula ni esté vacía
        if (role.getRolePermissions() != null && !role.getRolePermissions().isEmpty()) {
            
            for (RolePermission rp : role.getRolePermissions()) {
                // Enlazamos el RolePermission con el Rol padre actual
                rp.setRole(role);
                
                // Si el permiso interno solo trae el ID desde el frontend/mapper,
                // usamos el entityManager para obtener la referencia real de JPA
                if (rp.getPermission() != null && rp.getPermission().getId() != null) {
                    Permission managedPermission = entityManager.getReference(
                        Permission.class, 
                        rp.getPermission().getId()
                    );
                    rp.setPermission(managedPermission);
                }
            }
        }
        
        // 2. Guardamos el rol en cascada.
        // Gracias al CascadeType.ALL en Role.java, esto disparará:
        // - INSERT INTO role ...
        // - INSERT INTO role_permission ... (para cada permiso de la lista)
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public Role update(Long id, Role role) {
        // 1. Verificamos que el rol exista en la BD (Evita duplicados)
        Role existingRole = findById(id);
        
        // 2. Limpiamos manualmente los permisos antiguos asociados a este rol en la BD
        List<RolePermission> currentPermissions = rolePermissionRepository.findByRoleId(id);
        if (!currentPermissions.isEmpty()) {
            rolePermissionRepository.deleteAll(currentPermissions);
            rolePermissionRepository.flush(); // Forzamos el DELETE inmediato en la BD
        }

        // 3. Seteamos los datos básicos actualizados
        existingRole.setName(role.getName());
        existingRole.setDescription(role.getDescription());

        // 4. Mapeamos y enlazamos la nueva lista de permisos si es que viene del Frontend
        if (role.getRolePermissions() != null) {
            // Limpiamos la lista interna vieja de la entidad gestionada por Hibernate
            existingRole.getRolePermissions().clear(); 
            
            // Enlazamos cada nuevo elemento asegurando la clave foránea del rol
            for (RolePermission rp : role.getRolePermissions()) {
                rp.setRole(existingRole);
                existingRole.getRolePermissions().add(rp);
            }
        }

        // 5. Guardamos la entidad gestionada. 
        // Al tener CascadeType.ALL, Hibernate disparará los nuevos INSERTS en role_permission
        return roleRepository.save(existingRole);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        findById(id);

        // 1. Desasignar rol de todos los usuarios que lo tengan
        List<User> users = userRepository.findByRoleId(id);
        if (!users.isEmpty()) {
            users.forEach(user -> user.setRole(null));
            userRepository.saveAll(users);
            entityManager.flush();
            entityManager.clear();
        }

        // 2. Eliminar los RolePermission asociados al rol
        List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleId(id);
        if (!rolePermissions.isEmpty()) {
            rolePermissionRepository.deleteAll(rolePermissions);
            entityManager.flush();
            entityManager.clear();
        }

        // 3. Eliminar el rol
        roleRepository.deleteById(id);
    }
}
