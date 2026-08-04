-- ============================================================================
-- SCRIPT CORREGIDO DE GENERACIÓN DE ESPECIALIDADES E INSTRUCTORES PARA H2 DB
-- (ESTADO = TRUE para tipo boolean)
-- ============================================================================

-- 1. INSERTAR ESPECIALIDADES TÉCNICAS Y TRANSVERSALES
MERGE INTO ESPECIALIDADES (ID, NOMBRE_ESPECIALIDAD) KEY(ID) VALUES
(1, 'Programación Backend y Algoritmos'),
(2, 'Bases de Datos y Persistencia SQL/NoSQL'),
(3, 'Desarrollo Frontend y Experiencia de Usuario (UI/UX)'),
(4, 'Inglés Técnico y Bilingüismo'),
(5, 'Redes, Infraestructura y Sistemas Operativos'),
(6, 'Seguridad y Salud en el Trabajo (SST)'),
(7, 'Pruebas de Software (QA) y Calidad'),
(8, 'DevOps y Arquitectura en la Nube');

-- 2. MAPEAR ESPECIALIDADES A COMPETENCIAS EXISTENTES
MERGE INTO COMPETENCIA_ESPECIALIDAD (COMPETENCIA_ID, ESPECIALIDAD_ID) KEY(COMPETENCIA_ID) VALUES
(1, 1), -- Programación Backend
(2, 4), -- Inglés Técnico
(3, 2), -- Bases de Datos
(4, 3), -- Frontend
(5, 5), -- Redes
(6, 6), -- SST
(7, 7), -- QA
(8, 8); -- DevOps

-- 3. CREAR NUEVOS USUARIOS INSTRUCTORES EN LA TABLA USUARIO (ESTADO = TRUE)
MERGE INTO USUARIO (ID, CORREO, CONTRASENA, ESTADO, ROL, TOKEN_SESSION) KEY(ID) VALUES
(6, 'carlos.mendoza@sena.edu.co', '$2a$10$4.o7w4bM6Tf8q0G1l2l3uO7q6g5f4e3d2c1b0a9f8e7d6c5b4a3', TRUE, 'INSTRUCTOR', NULL),
(7, 'maria.silva@sena.edu.co', '$2a$10$4.o7w4bM6Tf8q0G1l2l3uO7q6g5f4e3d2c1b0a9f8e7d6c5b4a3', TRUE, 'INSTRUCTOR', NULL),
(8, 'andres.gomez@sena.edu.co', '$2a$10$4.o7w4bM6Tf8q0G1l2l3uO7q6g5f4e3d2c1b0a9f8e7d6c5b4a3', TRUE, 'INSTRUCTOR', NULL),
(9, 'laura.restrepo@sena.edu.co', '$2a$10$4.o7w4bM6Tf8q0G1l2l3uO7q6g5f4e3d2c1b0a9f8e7d6c5b4a3', TRUE, 'INSTRUCTOR', NULL),
(10, 'diego.martinez@sena.edu.co', '$2a$10$4.o7w4bM6Tf8q0G1l2l3uO7q6g5f4e3d2c1b0a9f8e7d6c5b4a3', TRUE, 'INSTRUCTOR', NULL),
(11, 'valentina.ospina@sena.edu.co', '$2a$10$4.o7w4bM6Tf8q0G1l2l3uO7q6g5f4e3d2c1b0a9f8e7d6c5b4a3', TRUE, 'INSTRUCTOR', NULL),
(12, 'camilo.torres@sena.edu.co', '$2a$10$4.o7w4bM6Tf8q0G1l2l3uO7q6g5f4e3d2c1b0a9f8e7d6c5b4a3', TRUE, 'INSTRUCTOR', NULL),
(13, 'diana.vargas@sena.edu.co', '$2a$10$4.o7w4bM6Tf8q0G1l2l3uO7q6g5f4e3d2c1b0a9f8e7d6c5b4a3', TRUE, 'INSTRUCTOR', NULL);

-- 4. INSERTAR DATOS PERSONALES Y CONTRATOS EN PERFIL_BASE
MERGE INTO PERFIL_BASE (USUARIO_ID, NOMBRE, APELLIDO, DOCUMENTO_IDENTIDAD, TELEFONO, TIPO_CONTRATO) KEY(USUARIO_ID) VALUES
(6, 'CARLOS', 'MENDOZA', '1018473920', '3104829102', 'CONTRATISTA'),
(7, 'MARÍA FERNANDA', 'SILVA', '1032849102', '3158492019', 'PLANTA'),
(8, 'ANDRÉS FELIPE', 'GÓMEZ', '1098472910', '3129481029', 'CONTRATISTA'),
(9, 'LAURA PATRICIA', 'RESTREPO', '1073928174', '3184920192', 'PLANTA'),
(10, 'DIEGO ALEJANDRO', 'MARTÍNEZ', '1052938172', '3119482019', 'CONTRATISTA'),
(11, 'VALENTINA', 'OSPINA', '1084920193', '3168492010', 'PLANTA'),
(12, 'CAMILO ANDRÉS', 'TORRES', '1029481029', '3149281029', 'CONTRATISTA'),
(13, 'DIANA MARCELA', 'VARGAS', '1048291038', '3174920192', 'PLANTA');

-- 5. ASIGNAR DISPONIBILIDAD Y JORNADAS EN DISPONIBILIDAD_INSTRUCTOR_ENTITY
MERGE INTO DISPONIBILIDAD_INSTRUCTOR_ENTITY (USUARIO_ID, HORAS_MAXIMAS, HORAS_ASIGNADAS, JORNADA) KEY(USUARIO_ID) VALUES
(6, 160, 0, 'TARDE'),
(7, 160, 0, 'MAÑANA'),
(8, 160, 0, 'TARDE'),
(9, 160, 0, 'MAÑANA'),
(10, 160, 0, 'TARDE'),
(11, 160, 0, 'MAÑANA'),
(12, 160, 0, 'TARDE'),
(13, 160, 0, 'MAÑANA');

-- 6. ASIGNAR ESPECIALIDAD A CADA INSTRUCTOR (INSTRUCTOR_ESPECIALIDAD)
MERGE INTO INSTRUCTOR_ESPECIALIDAD (INSTRUCTOR_ID, ESPECIALIDAD_ID) KEY(INSTRUCTOR_ID) VALUES
(1, 1), -- JHON PRADA -> Programación Backend
(4, 2), -- vivi NA -> Bases de Datos
(5, 5), -- instructor_tic -> Redes
(6, 1), -- Carlos Mendoza -> Programación Backend
(7, 2), -- María Silva -> Bases de Datos
(8, 3), -- Andrés Gómez -> Frontend UI/UX
(9, 4), -- Laura Restrepo -> Bilingüismo
(10, 5), -- Diego Martínez -> Redes
(11, 6), -- Valentina Ospina -> SST
(12, 7), -- Camilo Torres -> QA
(13, 8); -- Diana Vargas -> DevOps

-- 7. ASIGNAR DÍAS DISPONIBLES A LOS NUEVOS INSTRUCTORES
DELETE FROM DIAS_DISPONIBLES_INSTRUCTOR WHERE USUARIO_ID IN (6, 7, 8, 9, 10, 11, 12, 13);
INSERT INTO DIAS_DISPONIBLES_INSTRUCTOR (USUARIO_ID, DIA_DISPONIBLE) VALUES
(6, 'LUNES'), (6, 'MARTES'), (6, 'MIERCOLES'), (6, 'JUEVES'), (6, 'VIERNES'),
(7, 'LUNES'), (7, 'MARTES'), (7, 'MIERCOLES'), (7, 'JUEVES'), (7, 'VIERNES'),
(8, 'LUNES'), (8, 'MARTES'), (8, 'MIERCOLES'), (8, 'JUEVES'), (8, 'VIERNES'),
(9, 'LUNES'), (9, 'MARTES'), (9, 'MIERCOLES'), (9, 'JUEVES'), (9, 'VIERNES'),
(10, 'LUNES'), (10, 'MARTES'), (10, 'MIERCOLES'), (10, 'JUEVES'), (10, 'VIERNES'),
(11, 'LUNES'), (11, 'MARTES'), (11, 'MIERCOLES'), (11, 'JUEVES'), (11, 'VIERNES'),
(12, 'LUNES'), (12, 'MARTES'), (12, 'MIERCOLES'), (12, 'JUEVES'), (12, 'VIERNES'),
(13, 'LUNES'), (13, 'MARTES'), (13, 'MIERCOLES'), (13, 'JUEVES'), (13, 'VIERNES');
