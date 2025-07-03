
# Payroll-MS

**Payroll-MS** es un sistema de gestión de nómina automatizado desarrollado en Java con Spring Boot. Permite administrar empleados, calcular nóminas, gestionar deducciones e impuestos, y generar recibos de pago de manera eficiente y segura.

## Características principales

- **Gestión de empleados:** Alta, consulta, actualización y baja de empleados.
- **Generación automática de nómina:** Cálculo de salarios, deducciones, impuestos y bonificaciones para todos los empleados.
- **Recibos de pago:** Generación de recibos individuales por empleado y registro histórico.
- **Seguridad:** Acceso protegido por roles (ADMIN, EMPLEADO, SUPERVISOR) usando Spring Security.
- **API RESTful:** Endpoints para todas las operaciones principales.
- **Manejo de errores:** Respuestas estructuradas y controladas ante errores y recursos no encontrados.

## Arquitectura

- **Backend:** Java 17, Spring Boot, Spring Data JPA, Spring Security
- **Base de datos:** MySQL
- **ORM:** JPA/Hibernate
- **Gestión de dependencias:** Maven

## Modelo de datos principal

- **Empleado:** nombre, apellido, email, teléfono, fecha de contratación, salario base, horas extra, deducciones, impuestos, bonificaciones, estado, tipo de contrato.
- **Nómina:** fecha de generación, lista de recibos de pago.
- **Recibo:** salario total, deducciones, impuestos, fecha, referencia a empleado y nómina.
- **Usuario y roles:** autenticación y autorización basada en roles.

## Endpoints principales

### Empleados (`/empleados`)
- `POST /crear` (ADMIN): Crear empleado
- `GET /{id}`: Consultar empleado por ID
- `GET /apellido?apellido=...`: Consultar empleado por apellido
- `GET /obtEmpleados`: Listar todos los empleados
- `PUT /actualizar/{id}`: Actualizar empleado
- `DELETE /{id}`: Eliminar empleado

### Nómina (`/nominas`)
- `POST /generar`: Generar nómina para todos los empleados

### Seguridad y roles
- Acceso a endpoints restringido según rol (ADMIN, EMPLEADO, SUPERVISOR)
- Autenticación con usuario y contraseña (encriptada con BCrypt)

## Configuración

Editar `src/main/resources/application.properties` para los datos de conexión a MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/crudemp
spring.datasource.username=root
spring.datasource.password="password"
```

## Instalación y ejecución

1. Clona el repositorio y accede al directorio del proyecto.
2. Configura la base de datos MySQL y actualiza el archivo `application.properties`.
3. Ejecuta:
   ```bash
   ./mvnw spring-boot:run
   ```
4. La API estará disponible en `http://localhost:8080`

## Pruebas

Incluye pruebas unitarias y de integración con JUnit y Spring Boot Test. Para ejecutar:

```bash
./mvnw test
```

## Dependencias principales

- spring-boot-starter-data-jpa
- spring-boot-starter-security
- spring-boot-starter-validation
- spring-boot-starter-web
- mysql-connector-j
- lombok

## Notas

- El frontend no está incluido en este repositorio.
- El sistema está preparado para integrarse con un frontend (por ejemplo, React) y servicios de correo electrónico.

---
Desarrollado por Juan Baez | 2024
