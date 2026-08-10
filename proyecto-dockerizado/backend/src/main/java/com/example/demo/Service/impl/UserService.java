package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Repository.*;
import com.example.demo.controller.rest.dto.UserRequest;
import com.example.demo.domain.*;
import com.example.demo.mappers.IUserMapper;
import com.example.demo.service.IUserService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements IUserService {

    private final PasswordEncoder passwordEncoder;
    private final IUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserProgressRepository userProgressRepository;
    private final INotificationRepository notificationRepository;
    private final IMessageRepository messageRepository;
    private final IRoutineRepository routineRepository;
    private final ISesionRepository sesionRepository;
    private final IStatsRepository statsRepository;
    private final IStudentRepository studentRepository;
    private final ITrainerRepository trainerRepository;
    private final IAdminRepository adminRepository;
    private final IEventRepository eventRepository;    
    private final IUserParticipationRepository userParticipationRepository;      
    private final IUserMapper userMapper;


    @Override
    public User save(UserRequest request) {
        User user = userMapper.userRequestToUser(request);
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        if (request.getRoleId() != null) {
            Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado"));
            user.setRole(role);
        }
        return userRepository.save(user);
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public void delete(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));

        // 1. Romper Message.notification = null
        messageRepository.findAll().stream()
            .filter(m ->
                (m.getSender() != null && m.getSender().getId().equals(id)) ||
                (m.getReciever() != null && m.getReciever().getId().equals(id)))
            .forEach(m -> {
                m.setNotification(null);
                messageRepository.save(m);
            });

        // 2. Eliminar notificaciones del usuario
        notificationRepository.deleteAll(
            notificationRepository.findAll().stream()
                .filter(n -> n.getUser() != null && n.getUser().getId().equals(id))
                .toList()
        );

        // 3. Eliminar mensajes
        messageRepository.deleteAll(
            messageRepository.findAll().stream()
                .filter(m ->
                    (m.getSender() != null && m.getSender().getId().equals(id)) ||
                    (m.getReciever() != null && m.getReciever().getId().equals(id)))
                .toList()
        );

        // 4. Desasignar rutinas del trainer
        routineRepository.findAll().stream()
            .filter(r -> r.getTrainer() != null && r.getTrainer().getId().equals(id))
            .forEach(r -> {
                r.setTrainer(null);
                routineRepository.save(r);
            });

        // 5. Eliminar rutinas donde el user es dueño
        routineRepository.deleteAll(
            routineRepository.findAll().stream()
                .filter(r -> r.getUser() != null && r.getUser().getId().equals(id))
                .toList()
        );

        // 6. Eliminar sesiones del usuario
        sesionRepository.deleteAll(
            sesionRepository.findAll().stream()
                .filter(s -> s.getUser() != null && s.getUser().getId().equals(id))
                .toList()
        );

        // 7. Eliminar stats del usuario
        statsRepository.deleteAll(
            statsRepository.findAll().stream()
                .filter(s -> s.getUser() != null && s.getUser().getId().equals(id))
                .toList()
        );

        // 8. Desasociar estudiantes si es trainer
        studentRepository.findByTrainer_Id(id).forEach(s -> {
            s.setTrainer(null);
            studentRepository.save(s);
        });

        // 9. Eliminar participaciones propias del usuario
        userParticipationRepository.deleteAll(
            userParticipationRepository.findByUser_Id(id)
        );

        // 10. Eliminar participaciones y eventos si es admin
        eventRepository.findAll().stream()
            .filter(e -> e.getAdmin() != null && e.getAdmin().getId().equals(id))
            .forEach(e -> {
                userParticipationRepository.deleteAll(
                    userParticipationRepository.findAll().stream()
                        .filter(p -> p.getEvent() != null && p.getEvent().getId().equals(e.getId()))
                        .toList()
                );
                eventRepository.delete(e);
            });

        // 11. Eliminar recomendaciones del usuario
        entityManager.createNativeQuery("DELETE FROM user_recomendation WHERE user_id = :id")
            .setParameter("id", id)
            .executeUpdate();

        // 12. Romper referencia con UserProgress
        if (user.getProgress() != null) {
            UserProgress progress = user.getProgress();
            user.setProgress(null);
            userRepository.save(user);
            userProgressRepository.delete(progress);
        }

        // 13. Eliminar fila hija (admin/trainer/student) sin tocar la tabla users
        adminRepository.deleteByUserId(id);
        trainerRepository.deleteByUserId(id);
        studentRepository.deleteByUserId(id);

        // 14. Eliminar usuario
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findByFullName(String fullName) {
        return userRepository.findByFullName(fullName);
    }

    @Override
    public List<User> findByFullNameContaining(String fullName) {
        return userRepository.findByFullNameContaining(fullName);
    }

    @Override
    public User assignRole(Integer userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found: " + roleId));
        user.setRole(role);
        return userRepository.save(user);
    }

    @Override
    public void unassignRole(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
        user.setRole(null);
        userRepository.save(user);
    }

    @PersistenceContext
    private EntityManager entityManager; // <--- 1. INYECTA EL ENTITY MANAGER

    @Override
    @Transactional // <--- 2. MANTÉN EL TRANSACTIONAL
    public User update(Integer id, UserRequest request) {
        // 3. Buscamos al usuario existente
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        
        // 4. Actualizamos los campos básicos
        existingUser.setFullName(request.getFullName());
        existingUser.setEmail(request.getEmail());
        existingUser.setBirthDate(request.getBirthDate());
        existingUser.setWeight(request.getWeight());
        existingUser.setHeight(request.getHeight());
        
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        
        // 5. Actualizamos el Rol
        if (request.getRoleId() != null) {
            Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado"));
            existingUser.setRole(role);
        }
        
        // 6. Guardamos los cambios en el repositorio
        User savedUser = userRepository.save(existingUser);
        
        entityManager.flush(); // Fuerza a escribir el 'role_id' en la BD AHORA mismo
        entityManager.clear(); // Vacía la caché de Hibernate para destruir los proxies viejos
        
        // 8. Volvemos a buscar el usuario limpio desde la BD para que MapStruct devuelva el rol real
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Error al refrescar usuario"));
    }
}