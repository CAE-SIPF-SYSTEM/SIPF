-- ============================================================================
-- SCRIPT DE MIGRACIÓN: POBLAR MALLA CURRICULAR Y DISEÑO CURRICULAR EN H2 DB
-- ============================================================================

-- 1. ASEGURAR QUE EXISTE EL PROGRAMA DE FORMACIÓN (ID = 1, ADSO)
MERGE INTO PROGRAMA (ID, NOMBRE, NIVEL_FORMACION, JORNADA, DURACION_PRACTICAS, MUNICIPIO_ID) KEY(ID) VALUES
(1, 'ANÁLISIS Y DESARROLLO DE SOFTWARE (ADSO)', 'TECNOLOGO', 'MIXTA', 864, NULL);

-- 2. ASEGURAR QUE EXISTE LA FICHA (ID = 1, CODIGO 2996315)
MERGE INTO FICHA (ID, CODIGO_FICHA, PROGRAMA_ID, FECHA_INICIO, FECHA_FIN) KEY(ID) VALUES
(1, '2996315 - ADSO', 1, '2025-01-15', '2026-12-15');

-- 3. INSERTAR COMPETENCIAS (COMPETENCIA)
MERGE INTO COMPETENCIA (ID, CODIGO, NOMBRE, TIPO_COMPETENCIA) KEY(ID) VALUES
(1, '220501096', 'Análisis y Desarrollo de Software', 'TECNICA'),
(2, '240202501', 'Inglés Técnico y Bilingüismo', 'TRANSVERSAL'),
(3, '220501097', 'Bases de Datos y Persistencia SQL', 'TECNICA'),
(4, '220501098', 'Desarrollo Frontend y Web UI/UX', 'TECNICA'),
(5, '220501099', 'Redes y Comunicaciones de Datos', 'TECNICA'),
(6, '230101507', 'Seguridad y Salud en el Trabajo (SST)', 'TRANSVERSAL'),
(7, '220501100', 'Pruebas de Software (QA) y Calidad', 'TECNICA'),
(8, '220501101', 'DevOps y Arquitectura en la Nube', 'TECNICA');

-- 4. INSERTAR RAPS (RAP)
MERGE INTO RAP (ID, COMPETENCIA_ID, DESCRIPCION) KEY(ID) VALUES
(1, 1, 'Desarrollar componentes backend del sistema empleando patrones de diseño'),
(2, 2, 'Comprender y redactar documentación técnica y guías en idioma inglés'),
(3, 3, 'Diseñar la capa relacional y consultas SQL optimizadas para la base de datos'),
(4, 4, 'Construir interfaces interactivas en Angular, HTML5 y CSS3 responsive'),
(5, 5, 'Configurar topologías de red y servicios en sistemas operativos Linux'),
(6, 6, 'Aplicar normatividad de SST y autocuidado en entornos de desarrollo tecnológico'),
(7, 7, 'Ejecutar pruebas unitarias, de integración y control de calidad QA'),
(8, 8, 'Configurar contenedores Docker, CI/CD y despliegue automatizado en la nube');

-- 5. INSERTAR DISEÑO CURRICULAR (DISEÑO_CURRICULAR_ENTITY)
-- Asigna la malla curricular para el Programa 1 en los Trimestres 1 al 7
DELETE FROM DISEÑO_CURRICULAR_ENTITY WHERE PROGRAMAID = 1;

INSERT INTO DISEÑO_CURRICULAR_ENTITY (ID, PROGRAMAID, NUMERO_TRIMESTRE, RAP_ID, HORASPRESENCIALES) VALUES
-- Trimestre 1
(101, 1, 1, 1, 80),
(102, 1, 1, 2, 48),
(103, 1, 1, 3, 60),

-- Trimestre 2
(201, 1, 2, 4, 64),
(202, 1, 2, 2, 40),
(203, 1, 2, 5, 40),

-- Trimestre 3
(301, 1, 3, 1, 60),
(302, 1, 3, 3, 40),
(303, 1, 3, 6, 24),

-- Trimestre 4
(401, 1, 4, 4, 60),
(402, 1, 4, 7, 40),
(403, 1, 4, 2, 40),

-- Trimestre 5
(501, 1, 5, 8, 48),
(502, 1, 5, 5, 40),
(503, 1, 5, 7, 40),

-- Trimestre 6
(601, 1, 6, 1, 40),
(602, 1, 6, 4, 40),
(603, 1, 6, 8, 40),

-- Trimestre 7
(701, 1, 7, 7, 40),
(702, 1, 7, 8, 40);

-- 6. ASIGNAR ESPECIALIDADES A COMPETENCIAS (COMPETENCIA_ESPECIALIDAD)
MERGE INTO COMPETENCIA_ESPECIALIDAD (COMPETENCIA_ID, ESPECIALIDAD_ID) KEY(COMPETENCIA_ID) VALUES
(1, 1), -- Backend
(2, 4), -- Bilingüismo
(3, 2), -- Bases de Datos
(4, 3), -- Frontend
(5, 5), -- Redes
(6, 6), -- SST
(7, 7), -- QA
(8, 8); -- DevOps
