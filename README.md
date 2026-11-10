# CrediYa S.A.S. - Sistema de Gestion de Prestamos y Cartera

Sistema de backend en Java para la gestion de solicitudes, prestamos, clientes, empleados y pagos con persistencia dual: base de datos relacional (MySQL mediante JDBC) y archivos binarios serializables (.dat y .ser), ademas de reportes financieros mediante la API de Streams y expresiones Lambda.

---

## 1. Requisitos Previos

- Java Development Kit (JDK 8 o superior).
- Servidor MySQL (MySQL Workbench, XAMPP o servicio local).
- Conector MySQL JDBC (ya incluido en la carpeta `lib/mysql-connector-j-8.0.33.jar`).

---

## 2. Configuracion de la Base de Datos

1. Abra **MySQL Workbench** (o phpMyAdmin / terminal de MySQL).
2. Abra y ejecute el archivo `script_crediya.sql` ubicado en la raiz del proyecto.
3. El script creara automaticamente la base de datos `crediya_db` y las siguientes tablas con datos de prueba:
   - `roles`: Roles del sistema (ADMINISTRADOR, ASESOR).
   - `personas`: Herencia aplicada en BD con atributos comunes (documento, nombre, correo, telefono).
   - `empleados`: Datos de usuario, clave, cargo y rol referenciando a `personas`.
   - `clientes`: Informacion de clientes referenciando a `personas`.
   - `solicitudes`: Solicitudes de credito (evaluadas como PENDIENTE, APROBADA o RECHAZADA).
   - `prestamos`: Creditos aprobados con calculo de intereses colombianos (2.1% mes vencido), cuota fija y saldo.
   - `pagos`: Historial de abonos con fecha, numero de cuota y metodo de pago.

---

## 3. Configuracion de Conexion Java

Si su MySQL local tiene una contrasena diferente, abra el archivo:
`src/co/com/crediya/conexion/ConexionBD.java`

Modifique las lineas correspondientes:
```java
public static String USUARIO = "root";
public static String CLAVE = "su_contrasena";
```
*(Por defecto intenta con "root" y si no conecta, intenta automaticamente sin contrasena).*

---

## 4. Usuarios de Prueba para Iniciar Sesion

| Rol | Usuario | Contrasena | Nombre |
| :--- | :--- | :--- | :--- |
| **Administrador** | `admin` | `admin123` | Carlos Alberto Gomez Restrepo |
| **Asesor Comercial** | `diana` | `1234` | Diana Marcela Morales Zapata |

---

## 5. Como Ejecutar el Proyecto

### Opcion A: En Windows (Doble Clic o Consola)
Haga doble clic en el archivo `run.bat` o ejecutelo desde la consola:
```cmd
run.bat
```

### Opcion B: En Mac / Linux (Terminal)
```bash
chmod +x run.sh
./run.sh
```

### Opcion C: En Apache NetBeans / IntelliJ / Eclipse / VS Code
1. Abra la carpeta del proyecto en su IDE.
2. Agregue a las librerias del proyecto el archivo JAR ubicado en: `lib/mysql-connector-j-8.0.33.jar`.
3. Ejecute la clase principal: `co.com.crediya.app.Principal`.

---

## 6. Estructura de Paquetes del Proyecto

- `co.com.crediya.conexion`: Gestion de conexion a MySQL mediante JDBC con Patron Singleton (`ConexionBD`).
- `co.com.crediya.modelo`: Entidades del modelo POO (`Persona`, `Empleado`, `Cliente`, `Rol`, `Solicitud`, `Prestamo`, `Pago`).
- `co.com.crediya.fabrica`: Creacion de objetos mediante Patron Factory (`PersonaFactory`).
- `co.com.crediya.datos`: Interfaces y clases de acceso a datos (`IRepositorio`, `IRepositorioEstado`, `RepositorioEmpleado`, etc.).
- `co.com.crediya.servicio`: Logica de reportes con Streams/Lambdas (`ServicioReportes`) y persistencia de archivos (`ServicioArchivos`).
- `co.com.crediya.app`: Interfaz de consola y navegacion de menus (`Principal`).

---

## 7. Conceptos Clave para Sustentar ante el Profesor

1. **Herencia y Polimorfismo (POO y BD):**
   - En Java: La clase abstracta `Persona` encapsula los datos comunes (documento, nombre, correo, telefono) y el metodo abstracto `getTipo()`. Las clases `Empleado` y `Cliente` heredan de `Persona` y sobreescriben `getTipo()` evidenciando polimorfismo.
   - En Base de Datos: Se creo la tabla `personas` y las tablas `empleados` y `clientes` referencian su clave foranea a dicha tabla.

2. **Diferenciacion de Solicitudes y Prestamos:**
   - La tabla `solicitudes` permite auditar todas las radicaciones de credito.
   - Un prestamo solo se genera en la tabla `prestamos` cuando una solicitud es aprobada por el asesor/administrador. Si se rechaza, queda constancia en el historial.

3. **Modulo de Pagos e Historial:**
   - Cada abono registra su comprobante con numero de cuota y metodo de pago.
   - Se descuenta el saldo del prestamo en la base de datos y, cuando el saldo llega a cero, el estado cambia automaticamente a `PAGADO`.

4. **Programacion Funcional (Streams y Lambdas):**
   - En `ServicioReportes`, se filtran prestamos por estado con `.filter()`, se suman montos con `.mapToDouble().sum()`, se ordenan con `.sorted()` y se extrae el top de morosos o deudas con `.limit()`.

5. **Persistencia Hibrida (JDBC + Archivos Binarios):**
   - Base de datos relacional para la operacion transaccional.
   - Clases que implementan `Serializable` junto a `ObjectOutputStream` y `ObjectInputStream` para exportar e importar copias de seguridad de cartera (`backup_cartera.dat`) y empleados (`respaldo_empleados.ser`).
   - Generacion de informes en formato de texto plano (`informe_cartera_crediya.txt`).

---

## 8. Patrones de Diseno y Principios SOLID

1. **Patron Singleton (Creacional - Obligatorio):**
   - Implementado en `ConexionBD`.
   - Posee un constructor privado (`private ConexionBD()`), una variable estatica privada (`instancia`) y un metodo de acceso global sincronizado (`public static synchronized ConexionBD getInstancia()`).
   - Garantiza que solo exista un unico gestor de conexion en toda la aplicacion.

2. **Patron Factory Method (Creacional - Segundo Patron):**
   - Implementado en `PersonaFactory` (paquete `co.com.crediya.fabrica`).
   - Centraliza la creacion de instancias de `Cliente` y `Empleado`, desacoplando la interfaz de consola `Principal` de la instanciacion directa con `new`.

3. **Principios SOLID Aplicados:**
   - **S (Single Responsibility / Responsabilidad Unica):** Cada clase tiene un unico proposito (ej. `RepositorioCliente` solo accede a BD de clientes, `ServicioReportes` solo calcula indicadores, `ServicioArchivos` solo gestiona archivos).
   - **O (Open/Closed / Abierto/Cerrado):** La clase abstracta `Persona` esta abierta a extension (se pueden anadir nuevos tipos de personas como `Proveedor`) sin modificar su codigo original.
   - **L (Liskov Substitution / Sustitucion de Liskov):** Las subclases `Empleado` y `Cliente` pueden ser tratadas indistintamente como `Persona` sin romper el comportamiento esperado.
   - **I (Interface Segregation / Segregacion de Interfaces):** Se crearon interfaces atomicas (`IRepositorio<T>` e `IRepositorioEstado`) para que las clases no dependan de metodos que no necesitan.
   - **D (Dependency Inversion / Inversion de Dependencias):** Los repositorios implementan abstracciones (`IRepositorio<T>`), permitiendo que las capas superiores interactuen a traves de interfaces.

