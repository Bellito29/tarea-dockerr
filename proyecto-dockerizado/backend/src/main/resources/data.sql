-- =========================================================
-- ROLES
-- =========================================================
INSERT INTO role (id, name, description) VALUES
(1, 'ROLE_ADMIN',   'Administrador del sistema'),
(2, 'ROLE_TRAINER', 'Entrenador'),
(3, 'ROLE_STUDENT', 'Estudiante');

-- ---------------------------------------------------------
-- USERS (20 usuarios base para soportar Admin, Trainer, Student y User normal)
-- progress_id se deja NULL al inicio por la referencia circular con user_progress
-- ---------------------------------------------------------


INSERT INTO users (id, full_name, birth_date, weight, height, password, email, progress_id, role_id) VALUES
(1,  'Admin Uno',      '1985-01-10T00:00:00', 80.0, 1.80, '$2a$10$3Lo9I4DF/elX6p1jodsEo.VvnDClxPXSBYJwbqBnCwLU2lRl7/iUe', 'admin1@example.com', NULL, 1),
(2,  'Admin Dos',      '1987-03-15T00:00:00', 78.0, 1.76, '$2a$10$U1ZDfV4bYTg3TzxRf8L5EuKLMCi03sohs0i5XDs3wRT9bu/LYAVwm', 'admin2@example.com', NULL, 1),
(3,  'Admin Tres',     '1983-07-21T00:00:00', 82.0, 1.82, '$2a$10$U1ZDfV4bYTg3TzxRf8L5EuKLMCi03sohs0i5XDs3wRT9bu/LYAVwm', 'admin3@example.com', NULL, 1),
(4,  'Admin Cuatro',   '1990-09-12T00:00:00', 75.0, 1.74, '$2a$10$U1ZDfV4bYTg3TzxRf8L5EuKLMCi03sohs0i5XDs3wRT9bu/LYAVwm', 'admin4@example.com', NULL, 1),
(5,  'Admin Cinco',    '1988-11-05T00:00:00', 79.0, 1.79, '$2a$10$U1ZDfV4bYTg3TzxRf8L5EuKLMCi03sohs0i5XDs3wRT9bu/LYAVwm', 'admin5@example.com', NULL, 1),

-- 👇 Agregado NULL para progress_id
(6,  'Trainer Uno',    '1992-02-11T00:00:00', 72.0, 1.73, '$2a$10$3Lo9I4DF/elX6p1jodsEo.VvnDClxPXSBYJwbqBnCwLU2lRl7/iUe', 'trainer1@example.com', NULL, 2),
(7,  'Trainer Dos',    '1991-04-17T00:00:00', 74.0, 1.75, '$2a$10$HuyKYhwtz1qPkiAJUq1ZGOtlvXzop.GB3xLbhpiJ5LPUyGRBolbUq', 'trainer2@example.com', NULL, 2),
(8,  'Trainer Tres',   '1993-06-23T00:00:00', 76.0, 1.78, '$2a$10$HuyKYhwtz1qPkiAJUq1ZGOtlvXzop.GB3xLbhpiJ5LPUyGRBolbUq', 'trainer3@example.com', NULL, 2),
(9,  'Trainer Cuatro', '1994-08-09T00:00:00', 70.0, 1.70, '$2a$10$HuyKYhwtz1qPkiAJUq1ZGOtlvXzop.GB3xLbhpiJ5LPUyGRBolbUq', 'trainer4@example.com', NULL, 2),
(10, 'Trainer Cinco',  '1990-12-30T00:00:00', 77.0, 1.81, '$2a$10$HuyKYhwtz1qPkiAJUq1ZGOtlvXzop.GB3xLbhpiJ5LPUyGRBolbUq', 'trainer5@example.com', NULL, 2),

-- 👇 Agregado NULL para progress_id
(11, 'Student Uno',    '2000-01-01T00:00:00', 65.0, 1.68, '$2a$10$3Lo9I4DF/elX6p1jodsEo.VvnDClxPXSBYJwbqBnCwLU2lRl7/iUe', 'student1@example.com', NULL, 3),
(12, 'Student Dos',    '2001-02-02T00:00:00', 67.0, 1.70, '$2a$10$uVNUJGQC7q6nzciNZLVUbuU6/.HjH0o9NiX9Mi4uWtnW2zEKydLgS', 'student2@example.com', NULL, 3),
(13, 'Student Tres',   '1999-03-03T00:00:00', 69.0, 1.72, '$2a$10$uVNUJGQC7q6nzciNZLVUbuU6/.HjH0o9NiX9Mi4uWtnW2zEKydLgS', 'student3@example.com', NULL, 3),
(14, 'Student Cuatro', '2002-04-04T00:00:00', 64.0, 1.66, '$2a$10$uVNUJGQC7q6nzciNZLVUbuU6/.HjH0o9NiX9Mi4uWtnW2zEKydLgS', 'student4@example.com', NULL, 3),
(15, 'Student Cinco',  '2002-04-04T00:00:00', 64.0, 1.66, '$2a$10$uVNUJGQC7q6nzciNZLVUbuU6/.HjH0o9NiX9Mi4uWtnW2zEKydLgS', 'student5@example.com', NULL, 3);

INSERT INTO user_progress (id, progress_date, effort_level, notes, past_weight, current_weight, user_id) VALUES
(1, '2026-03-01T08:00:00', 'HIGH',   'Excelente progreso semanal', 70, 68, 11),
(2, '2026-03-02T08:00:00', 'MEDIUM', 'Mejorando resistencia',      76, 74, 12),
(3, '2026-03-03T08:00:00', 'LOW',    'Semana ligera',              68, 66, 13),
(4, '2026-03-04T08:00:00', 'HIGH',   'Muy buen rendimiento',       78, 75, 14),
(5, '2026-03-05T00:00:00', 'MEDIUM', 'Manteniendo constancia',     72, 70, 15);
-- ---------------------------------------------------------
-- ADMINS
-- ---------------------------------------------------------
INSERT INTO admins (user_id) VALUES
(1), (2), (3), (4), (5);

-- ---------------------------------------------------------
-- TRAINERS
-- ---------------------------------------------------------
INSERT INTO trainers (user_id) VALUES
(6), (7), (8), (9), (10);

-- ---------------------------------------------------------
-- STUDENTS
-- ---------------------------------------------------------
INSERT INTO students (user_id, trainer_id) VALUES
(11, 6),
(12, 7),
(13, 8),
(14, 9),
(15, 10);


-- ---------------------------------------------------------
-- PERMISSION
-- ---------------------------------------------------------
INSERT INTO permission (id, code, name, description) VALUES
(1,  101, 'editar-usuario',           'Permite modificar los datos de un usuario existente'),
(2,  102, 'crear-usuario',            'Permite registrar nuevos usuarios en el sistema'),
(3,  103, 'borrar-usuario',           'Permite eliminar usuarios del sistema'),
(4,  104, 'ver-usuario',              'Permite consultar la información de los usuarios'),

(5,  201, 'editar-rol',               'Permite modificar los datos de un rol existente'),
(6,  202, 'crear-rol',                'Permite crear nuevos roles en el sistema'),
(7,  203, 'borrar-rol',               'Permite eliminar roles del sistema'),
(8,  204, 'ver-rol',                  'Permite consultar los roles y sus permisos asociados'),

(9,  301, 'asignar-permiso-rol',      'Permite agregar permisos a un rol'),
(10, 302, 'asignar-rol-usuario',      'Permite asignar un rol a un usuario'),
(11, 303, 'eliminar-permiso-rol',     'Permite quitar permisos de un rol'),
(12, 304, 'eliminar-rol-usuario',     'Permite quitar el rol asignado a un usuario'),

(13, 401, 'crear-rutina',             'Permite crear nuevas rutinas de entrenamiento'),
(14, 402, 'asignar-rutina',           'Permite asignar rutinas a estudiantes'),
(15, 403, 'registrar-asistencia-evento', 'Permite registrar la asistencia a un evento');


-- ---------------------------------------------------------
-- RECOMENDATION
-- ---------------------------------------------------------
INSERT INTO recomendation (id, description) VALUES
(1, 'Aumentar consumo de agua diariamente'),
(2, 'Realizar estiramientos despues del entrenamiento'),
(3, 'Dormir al menos 7 horas por noche'),
(4, 'Incrementar progresivamente la intensidad'),
(5, 'Llevar registro semanal del peso');

-- ---------------------------------------------------------
-- EXERCISE
-- ---------------------------------------------------------
INSERT INTO exercise (id, name, type, description, duration, difficulty, video_url) VALUES
(1, 'Sentadillas',      'Strength',   'Ejercicio para piernas y gluteos',        15, 'Medium', 'https://video.com/sentadillas'),
(2, 'Flexiones',        'Strength',   'Ejercicio para pecho y brazos',           10, 'Medium', 'https://video.com/flexiones'),
(3, 'Plancha',          'Core',       'Ejercicio isometrico abdominal',           5, 'Hard',   'https://video.com/plancha'),
(4, 'Burpees',          'Cardio',     'Ejercicio de cuerpo completo',            12, 'Hard',   'https://video.com/burpees'),
(5, 'Trote Suave',      'Endurance',  'Cardio de baja intensidad',               30, 'Easy',   'https://video.com/trote');

-- ---------------------------------------------------------
-- SESION
-- ---------------------------------------------------------
INSERT INTO sesion (id, sesion_date, duration, user_id) VALUES
(1, '2026-03-10T06:00:00', 60, 10),
(2, '2026-03-11T07:00:00', 45, 11),
(3, '2026-03-12T08:00:00', 50, 12),
(4, '2026-03-13T09:00:00', 70, 13),
(5, '2026-03-14T10:00:00', 55, 14);

-- ---------------------------------------------------------
-- ROUTINE
-- ---------------------------------------------------------
INSERT INTO routine (id, routine_name, description, visibility, sesion_id, user_id, trainer_id) VALUES
(1, 'Rutina Pierna',    'Enfoque en fuerza de tren inferior',   'PUBLIC',  1, 10, 6),
(2, 'Rutina Pecho',     'Trabajo de empuje y resistencia',      'PRIVATE', 2, 11, 7),
(3, 'Rutina Core',      'Abdomen y estabilidad',                'PUBLIC',  3, 12, 8),
(4, 'Rutina HIIT',      'Cardio de alta intensidad',            'PRIVATE', 4, 13, 9),
(5, 'Rutina FullBody',  'Trabajo general del cuerpo',           'PUBLIC',  5, 14, 10);

-- ---------------------------------------------------------
-- EXERCISES_IN_ROUTINE
-- ---------------------------------------------------------
INSERT INTO exercises_in_routine (id, exercise_id, routine_id) VALUES
(1, 1, 1),
(2, 2, 2),
(3, 3, 3),
(4, 4, 4),
(5, 5, 5);

-- ---------------------------------------------------------
-- STATS
-- ---------------------------------------------------------
INSERT INTO stats (id, time, ammount_exercises, user_id) VALUES
(1, 3600, 4, 10),
(2, 2700, 3, 11),
(3, 3000, 5, 12),
(4, 4200, 6, 13),
(5, 3300, 4, 14);

-- ---------------------------------------------------------
-- EXERCISE_STATS
-- ---------------------------------------------------------
INSERT INTO exercise_stats (id, stats_id, exercise_id) VALUES
(1, 1, 1),
(2, 2, 2),
(3, 3, 3),
(4, 4, 4),
(5, 5, 5);

-- ---------------------------------------------------------
-- EVENT
-- admin_id referencia users (admins son users)
-- ---------------------------------------------------------
INSERT INTO event (id, title, place, date, admin_id) VALUES
(1, 'Clase Funcional',     'Salon A',  '2026-03-20T08:00:00', 1),
(2, 'Maraton Interna',     'Pista',    '2026-03-21T06:00:00', 2),
(3, 'Taller de Nutricion', 'Salon B',  '2026-03-22T10:00:00', 3),
(4, 'Sesion de Yoga',      'Terraza',  '2026-03-23T07:00:00', 4),
(5, 'Evaluacion Fisica',   'Consult.', '2026-03-24T09:00:00', 5);

-- ---------------------------------------------------------
-- USER_PARTICIPATION
-- Si esta tabla falla, el problema está en tu entidad, no en los inserts.
-- ---------------------------------------------------------
INSERT INTO user_participation (id, user_id, event_id) VALUES
(1, 10, 1),
(2, 11, 2),
(3, 12, 3),
(4, 13, 4),
(5, 14, 5);

-- ---------------------------------------------------------
-- USER_RECOMENDATION
-- ---------------------------------------------------------
INSERT INTO user_recomendation (id, user_id, recomendation_id) VALUES
(1, 10, 1),
(2, 11, 2),
(3, 12, 3),
(4, 13, 4),
(5, 14, 5);

-- ---------------------------------------------------------
-- NOTIFICATION
-- ---------------------------------------------------------
INSERT INTO notification (id, reference_id, type, user_id) VALUES
(1, 101, 'MESSAGE', 10),
(2, 102, 'MESSAGE', 11),
(3, 103, 'MESSAGE', 12),
(4, 104, 'MESSAGE', 13),
(5, 105, 'MESSAGE', 14);

-- ---------------------------------------------------------
-- MESSAGE
-- ---------------------------------------------------------
INSERT INTO message (id, content, sender_user_id, receiver_user_id, notification) VALUES
(1, 'Recuerda hidratarte hoy',          6, 10, NULL),
(2, 'Buen trabajo en la sesion',        7, 11, NULL),
(3, 'Debes mejorar la tecnica',         8, 12, NULL),
(4, 'Excelente progreso esta semana',   9, 13, NULL),
(5, 'No olvides tu rutina de manana',  10, 14, NULL);




-- =========================================================
-- ROLE_PERMISSION
-- ADMIN = todos
-- TRAINER = crear, editar, ver
-- STUDENT = ver
-- USER = ver
-- =========================================================
INSERT INTO role_permission (role_id, permission_id) VALUES

-- ROLE_ADMIN
(1,1),
(1,2),
(1,3),
(1,4),
(1,5),
(1,6),
(1,7),
(1,8),
(1,9),
(1,10),
(1,11),
(1,12),

-- ROLE_TRAINER
(2,13),
(2,14),
(2,15);

-- ROLE_STUDENT
-- Todavia no hay permisos para estudiantes, pero si los hubiera se asignarian aqui


-- =========================================================
-- USER_ROLE
-- usuarios 1-5 admin
-- usuarios 6-10 trainer
-- usuarios 11-15 student
-- =========================================================

-- ---------------------------------------------------------
-- UPDATES para cerrar referencias circulares
-- ---------------------------------------------------------
UPDATE users SET progress_id = 1 WHERE id = 11;
UPDATE users SET progress_id = 2 WHERE id = 12;
UPDATE users SET progress_id = 3 WHERE id = 13;
UPDATE users SET progress_id = 4 WHERE id = 14;
UPDATE users SET progress_id = 5 WHERE id = 15;


-- =====================================================
-- REINICIAR IDS AUTOGENERADOS (H2)
-- Agregar al FINAL de data.sql
-- =====================================================

ALTER TABLE role ALTER COLUMN id RESTART WITH 4;

ALTER TABLE users ALTER COLUMN id RESTART WITH 16;

ALTER TABLE user_progress ALTER COLUMN id RESTART WITH 6;

ALTER TABLE permission ALTER COLUMN id RESTART WITH 16;

ALTER TABLE recomendation ALTER COLUMN id RESTART WITH 6;

ALTER TABLE exercise ALTER COLUMN id RESTART WITH 6;

ALTER TABLE sesion ALTER COLUMN id RESTART WITH 6;

ALTER TABLE routine ALTER COLUMN id RESTART WITH 6;

ALTER TABLE exercises_in_routine ALTER COLUMN id RESTART WITH 6;

ALTER TABLE stats ALTER COLUMN id RESTART WITH 6;

ALTER TABLE exercise_stats ALTER COLUMN id RESTART WITH 6;

ALTER TABLE event ALTER COLUMN id RESTART WITH 6;

ALTER TABLE user_participation ALTER COLUMN id RESTART WITH 6;

ALTER TABLE user_recomendation ALTER COLUMN id RESTART WITH 6;

ALTER TABLE message ALTER COLUMN id RESTART WITH 6;

ALTER TABLE notification ALTER COLUMN id RESTART WITH 6;
-- =========================================================
-- AJUSTE DE SECUENCIAS PARA POSTGRESQL
-- Los datos semilla insertan IDs explícitos. Estas sentencias sincronizan
-- las secuencias de las columnas IDENTITY para que futuras inserciones no
-- intenten reutilizar IDs existentes.
-- =========================================================
SELECT setval(pg_get_serial_sequence('role', 'id'), (SELECT MAX(id) FROM role));
SELECT setval(pg_get_serial_sequence('users', 'id'), (SELECT MAX(id) FROM users));
SELECT setval(pg_get_serial_sequence('permission', 'id'), (SELECT MAX(id) FROM permission));
SELECT setval(pg_get_serial_sequence('role_permission', 'id'), (SELECT MAX(id) FROM role_permission));
SELECT setval(pg_get_serial_sequence('exercise', 'id'), (SELECT MAX(id) FROM exercise));
SELECT setval(pg_get_serial_sequence('routine', 'id'), (SELECT MAX(id) FROM routine));
SELECT setval(pg_get_serial_sequence('exercises_in_routine', 'id'), (SELECT MAX(id) FROM exercises_in_routine));
SELECT setval(pg_get_serial_sequence('sesion', 'id'), (SELECT MAX(id) FROM sesion));
SELECT setval(pg_get_serial_sequence('stats', 'id'), (SELECT MAX(id) FROM stats));
SELECT setval(pg_get_serial_sequence('exercise_stats', 'id'), (SELECT MAX(id) FROM exercise_stats));
SELECT setval(pg_get_serial_sequence('event', 'id'), (SELECT MAX(id) FROM event));
SELECT setval(pg_get_serial_sequence('message', 'id'), (SELECT MAX(id) FROM message));
SELECT setval(pg_get_serial_sequence('notification', 'id'), (SELECT MAX(id) FROM notification));
SELECT setval(pg_get_serial_sequence('recomendation', 'id'), (SELECT MAX(id) FROM recomendation));
SELECT setval(pg_get_serial_sequence('user_progress', 'id'), (SELECT MAX(id) FROM user_progress));
SELECT setval(pg_get_serial_sequence('user_participation', 'id'), (SELECT MAX(id) FROM user_participation));
SELECT setval(pg_get_serial_sequence('user_recomendation', 'id'), (SELECT MAX(id) FROM user_recomendation));
