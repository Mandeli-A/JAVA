DROP DATABASE IF EXISTS crediya_db;
CREATE DATABASE crediya_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE crediya_db;

CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE personas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    documento VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NOT NULL
);

CREATE TABLE empleados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    persona_id INT NOT NULL,
    rol_id INT NOT NULL,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    clave VARCHAR(100) NOT NULL,
    cargo VARCHAR(50) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (persona_id) REFERENCES personas(id),
    FOREIGN KEY (rol_id) REFERENCES roles(id)
);

CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    persona_id INT NOT NULL,
    direccion VARCHAR(150) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (persona_id) REFERENCES personas(id)
);

CREATE TABLE solicitudes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    empleado_id INT NOT NULL,
    monto DECIMAL(12,2) NOT NULL,
    plazo_meses INT NOT NULL,
    motivo VARCHAR(255) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    fecha DATE NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    FOREIGN KEY (empleado_id) REFERENCES empleados(id)
);

-- Tabla prestamos con compatibilidad completa para el Examen Java:
-- Campos requeridos por el enunciado: id, cliente_id, monto, interes, fecha_prestamo, fecha_vencimiento, estado
CREATE TABLE prestamos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    solicitud_id INT NULL,
    cliente_id INT NOT NULL,
    empleado_id INT NULL,
    monto DECIMAL(12,2) NOT NULL,
    tasa_interes DECIMAL(5,2) NOT NULL DEFAULT 2.10,
    interes DECIMAL(5,2) NULL DEFAULT 2.10,
    monto_total DECIMAL(12,2) NOT NULL,
    plazo_meses INT NOT NULL DEFAULT 12,
    cuota_mensual DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    saldo_pendiente DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    fecha_inicio DATE NOT NULL,
    fecha_prestamo DATE NULL,
    fecha_vencimiento DATE NULL,
    FOREIGN KEY (solicitud_id) REFERENCES solicitudes(id),
    FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    FOREIGN KEY (empleado_id) REFERENCES empleados(id)
);

CREATE TABLE pagos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    prestamo_id INT NOT NULL,
    monto DECIMAL(12,2) NOT NULL,
    metodo_pago VARCHAR(50) NOT NULL,
    fecha_pago DATE NOT NULL,
    numero_cuota INT NOT NULL,
    FOREIGN KEY (prestamo_id) REFERENCES prestamos(id)
);

-- ==========================================
-- DATOS INICIALES DE PRUEBA
-- ==========================================

INSERT INTO roles (nombre) VALUES 
('ADMINISTRADOR'),
('ASESOR');

INSERT INTO personas (documento, nombre, correo, telefono) VALUES 
('1017123456', 'Carlos Alberto Gomez Restrepo', 'carlos.gomez@crediya.com.co', '3104567890'),
('1020456789', 'Diana Marcela Morales Zapata', 'diana.morales@crediya.com.co', '3123456781'),
('71234567', 'Andres Felipe Herrera Rios', 'andres.herrera@gmail.com', '3001234567'),
('1037654321', 'Valentina Osorio Duque', 'valentina.osorio@hotmail.com', '3145678901'),
('98765432', 'Jorge Eliecer Castro Pena', 'jorge.castro@yahoo.es', '3156789012');

INSERT INTO empleados (persona_id, rol_id, usuario, clave, cargo, activo) VALUES 
(1, 1, 'admin', 'admin123', 'Administrador General', TRUE),
(2, 2, 'diana', '1234', 'Asesora Comercial', TRUE);

INSERT INTO clientes (persona_id, direccion, activo) VALUES 
(3, 'Cra 45 # 12-34 Medellin', TRUE),
(4, 'Cl 10 # 32-15 Envigado', TRUE),
(5, 'Av 33 # 65-20 Medellin', TRUE);

INSERT INTO solicitudes (cliente_id, empleado_id, monto, plazo_meses, motivo, estado, fecha) VALUES 
(1, 2, 5000000.00, 12, 'Compra de mercancia para negocio', 'APROBADA', '2026-03-01'),
(2, 2, 8000000.00, 24, 'Remodelacion de local comercial', 'APROBADA', '2026-10-15'),
(3, 2, 3500000.00, 6, 'Capital de trabajo inmediato', 'APROBADA', '2026-01-10'),
(1, 2, 10000000.00, 12, 'Expansion de sucursal', 'APROBADA', '2026-11-01'),
(3, 2, 15000000.00, 36, 'Libre inversion y vehiculo', 'RECHAZADA', '2026-10-05');

-- Prestamos de prueba para el Examen Java:
-- Prestamo 1: ACTIVO, VENCIDO (Fecha vencimiento 2026-09-01 < Hoy)
-- Prestamo 2: ACTIVO, AL DIA (Fecha vencimiento 2028-10-15 > Hoy)
-- Prestamo 3: ACTIVO, VENCIDO (Fecha vencimiento 2026-07-10 < Hoy)
-- Prestamo 4: ACTIVO, AL DIA (Fecha vencimiento 2027-11-01 > Hoy)
-- Prestamo 5: PAGADO (Paz y salvo, saldo 0.00)
INSERT INTO prestamos (
    solicitud_id, cliente_id, empleado_id, monto, tasa_interes, interes,
    monto_total, plazo_meses, cuota_mensual, saldo_pendiente, estado,
    fecha_inicio, fecha_prestamo, fecha_vencimiento
) VALUES 
(1, 1, 2, 5000000.00, 2.10, 2.10, 6260000.00, 6, 1043333.33, 4173333.32, 'ACTIVO', '2026-03-01', '2026-03-01', '2026-09-01'),
(2, 2, 2, 8000000.00, 2.10, 2.10, 12032000.00, 24, 501333.33, 8000000.00, 'ACTIVO', '2026-10-15', '2026-10-15', '2028-10-15'),
(3, 3, 2, 3500000.00, 2.10, 2.10, 4235000.00, 6, 705833.33, 2823333.33, 'ACTIVO', '2026-01-10', '2026-01-10', '2026-07-10'),
(4, 1, 2, 10000000.00, 2.10, 2.10, 12520000.00, 12, 1043333.33, 10433333.33, 'ACTIVO', '2026-11-01', '2026-11-01', '2027-11-01'),
(NULL, 2, 2, 2000000.00, 2.10, 2.10, 2252000.00, 6, 375333.33, 0.00, 'PAGADO', '2025-06-01', '2025-06-01', '2026-06-01');

INSERT INTO pagos (prestamo_id, monto, metodo_pago, fecha_pago, numero_cuota) VALUES 
(1, 1043333.34, 'Transferencia Bancaria', '2026-04-01', 1),
(1, 1043333.34, 'Efectivo', '2026-05-01', 2),
(3, 705833.33, 'Efectivo', '2026-02-10', 1),
(3, 705833.34, 'Tarjeta Debito', '2026-03-10', 2);
