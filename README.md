#  Sistema de Gestión taller Express- Java SE

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql)
![JDBC](https://img.shields.io/badge/JDBC-Data_Access-lightgray?style=for-the-badge)
![Arquitectura](https://img.shields.io/badge/Arquitectura-Por_Capas-success?style=for-the-badge)

##  Descripción General
TallerExpress es una aplicación desarrollada en Java SE diseñada para gestionar de forma eficiente el inventario de repuestos y órdenes de servicio. Implementa buenas prácticas de arquitectura por capas (MVC/DAO), manejo de excepciones personalizadas, y uso de transacciones JDBC para garantizar la coherencia e integridad de los datos. La interfaz de usuario es simple e interactiva mediante modales de `JOptionPane`.

##  Características Principales
* **Autenticación y Roles:** Sistema de inicio de sesión con control de acceso restrictivo.
* **Gestión de Inventario Bibliográfico (CRUD):** Registro, actualización y visualización del catálogo de servicios y repuestos.
* **Ordenes de servicio y update (Transaccional):** Asignación de ordenes de servicio. Utiliza transacciones JDBC para asegurar la integridad de los datos (descontar el stock de un repuesto solo si el registro se completa exitosamente en la base de datos).
* **Manejo de Excepciones Personalizadas:** Respuestas claras y controladas (`UnauthorizedException`, `InsufficientStockException`, `IllegalArgumentException`) ante violaciones de reglas de negocio o fallos de base de datos.

---

##  Tecnologías y Patrones
* **Lenguaje:** Java 21
* **Base de Datos:** MySQL 8.0+
* **Conectividad:** JDBC (Java Database Connectivity)
* **Gestor de Dependencias:** Maven (pom.xml)
* **Interfaz de Usuario:** `javax.swing.JOptionPane`
* **Patrones de Diseño:**
  * **Singleton:** Aplicado para asegurar una única instancia de conexión a la base de datos.
  * **Repository:** Abstracción y encapsulamiento del acceso a datos.
  * **Arquitectura Multicapa:** Model, Repository, Service, Controller, View.

---

##  Guía de Instalación

### 1. Prerrequisitos
Antes de iniciar, asegúrese de contar con el siguiente entorno configurado:
* JDK 21 instalado en su sistema operativo.
* MySQL Server en ejecución.
* Un IDE compatible (recomendado: Apache NetBeans).
* Maven instalado y configurado.

### 2. Configuración de la Base de Datos
1. Inicie sesión en su gestor de base de datos (ej. MySQL Workbench).
2. Ejecute el script SQL provisto en el proyecto para crear la base de datos `taller_express_db`, todas las tablas relacionales requeridas (Clientes, Repuestos, Usuarios, Vehiculos, Ordenes_servicio) y la data de prueba inicial.
3. Asegúrese de tener un usuario con privilegios suficientes para realizar operaciones DML y DDL en dicha base de datos.

### 3. Configuración del Proyecto
1. Clone este repositorio o extraiga los archivos del proyecto.
2. Abra el proyecto en su IDE.
3. Navegue hasta la clase encargada de la conexión (`src/main/java/config/DatabaseConnection.java`).
4. Actualice las credenciales de conexión para que coincidan con su entorno local:
```java
private static final String URL = "jdbc:mysql://localhost:3306/taller_express_db";
private static final String USER = "tu_usuario_aqui";
private static final String PASSWORD = "tu_password_aqui";
```
4. Permita que Maven descargue las dependencias necesarias. Verifique que la dependencia de `mysql-connector-j` esté presente en su archivo `pom.xml`.

---

##  Guía de Mantenimiento y Arquitectura

El proyecto ha sido diseñado bajo principios de Clean Code y alta cohesión (SRP). Para mantener o escalar el sistema, es imperativo respetar la siguiente estructura de directorios:

### Estructura de Paquetes
```text
src/main/java/
├── 📂 config       # Configuración Singleton de conexión a la base de datos
├── 📂 controller   # Enlace de comunicación entre la Vista y los Servicios
├── 📂 exception    # Excepciones propias del dominio
├── 📂 model        # Entidades del dominio (Repuesto, Usuario)
├── 📂 DAO   # Interfaces e implementaciones con lógica SQL pura
├── 📂 service      # Lógica de negocio, validaciones y reglas 
└── 📂 view         # Capa de presentación (JOptionPane) y clase Main
```

### Flujo para agregar nuevas funcionalidades a futuro
Si requiere añadir un nuevo módulo por ejemplo Categoria, o una nueva funionalidad de negocio:
1. **Model:** Defina la clase `Category.java` con sus atributos.
2. **Repository:** Cree `CategoryRepositoryImpl` para manejar el INSERT y SELECT con SQL.
3. **Service:** Cree `CategoryServiceImpl` para las validaciones lógicas (ej. evitar nombres de categorías duplicados). Inyecte el repositorio a través de su constructor.
4. **Controller:** Desarrolle `CategoryController` para recibir la data "cruda" del usuario.
5. **View:** Integre la llamada al nuevo controlador en la estructura switch del `Main.java`.

### Control de Transacciones (JDBC)
Cualquier proceso que afecte más de una tabla debe ser tratado como una transacción indivisible dentro del Repositorio, esto es altamente eficiente para la memoria y el proyecto en si. Al extender el sistema (por ejemplo, para procesar devoluciones o inventario), asegúrese de utilizar este patrón:

```java
            // descontar del inventario
            String sqlUpdateStock = "UPDATE repuestos SET stock_disponible = stock_disponible - ? WHERE id = ?";
            try (PreparedStatement stmtStock = conn.prepareStatement(sqlUpdateStock)) {
                stmtStock.setInt(1, cantidadUsada);
                stmtStock.setInt(2, repuesto.getId());
                int rows = stmtStock.executeUpdate();
                if (rows == 0) throw new TallerException("Error actualizando el inventario.");
            }

            conn.commit(); // aca confirmo la transaccion usando codigos http
            System.out.println(" 201 Created (Transaction Committed)");

        } catch (SQLException | TallerException e) {
            if (conn != null) {
                conn.rollback(); 
                System.out.println(" 500 Error (Transaction Rolled Back)");
            }
```

---
## Pasos de Configuración y Ejecución
1. Crear la base de datos ejecutando el script `schema.sql` (ubicado en la carpeta `database`) en tu servidor MySQL.
2. Configurar el usuario y contraseña de la base de datos en la clase `DatabaseConnection.java` ubicada en `src/main/java/com/riwi/taller/config/`.
3. Abrir el proyecto como un proyecto Maven en tu IDE para descargar las dependencias del archivo `pom.xml`.
4. Ejecutar el método `main` ubicado en la clase `Main.java` (paquete raíz).

## Datos del Coder
- **Nombre:** Leonardo David Jiménez Dager
- **Clan / Empresa:** Riwi / SENA
- **Ubicación:** Barranquilla, Colombia
