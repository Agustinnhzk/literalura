# 📚 Literalura - Catálogo de Libros

¡Bienvenido a **Literalura**! Este es un proyecto Backend desarrollado en Java con Spring Boot, diseñado para funcionar como un catálogo de libros interactivo por consola. 

La aplicación consume datos directamente de la API pública de **Gutendex**, procesa la información y la almacena de forma persistente en una base de datos **PostgreSQL**.

---

## 🛠️ Tecnologías Utilizadas

Este proyecto fue construido utilizando las siguientes tecnologías y herramientas:

* **Java 17** (o superior)
* **Spring Boot** (Framework principal)
* **Spring Data JPA** (Para la persistencia de datos mediante Hibernate)
* **PostgreSQL** (Motor de base de datos relacional)
* **Maven** (Gestor de dependencias)
* **Jackson** (Para la serialización y deserialización de datos JSON)
* **API Externa:** [Gutendex API](https://gutendex.com/)

---

## ⚙️ Características y Funcionalidades

La aplicación funciona completamente a través de la consola (CLI) y ofrece un menú interactivo con las siguientes opciones:

1. **Buscar libro por título:** Realiza una petición GET a la API de Gutendex, procesa el JSON recibido, extrae los datos relevantes (Título, Autor, Idioma, Descargas) y los guarda automáticamente en la base de datos (evitando duplicados).
2. **Mostrar libros registrados:** Consulta la base de datos y lista todos los libros que ya fueron guardados localmente.
3. **Mostrar autores registrados:** Lista todos los autores guardados en la base de datos, incluyendo sus fechas de nacimiento y fallecimiento.
4. **Mostrar autores vivos en un determinado año:** Permite ingresar un año específico y, mediante una consulta JPQL personalizada, devuelve los autores que estaban vivos en esa época.
5. **Listar libros por idioma:** Filtra y muestra los libros almacenados según su idioma (ej. `es` para español, `en` para inglés).

---

## 🚀 Arquitectura del Proyecto

El sistema está diseñado siguiendo buenas prácticas de separación de responsabilidades:
* **`model`**: Contiene los `Records` (DTOs) para mapear el JSON de la API y las `@Entity` (Clases `Libro` y `Autor`) que definen las tablas en PostgreSQL.
* **`repository`**: Interfaces de Spring Data JPA (`LibroRepository`, `AutorRepository`) encargadas de la comunicación con la base de datos y las consultas personalizadas.
* **`service`**: Contiene la lógica para el consumo de la API (`HttpClient`) y la conversión de los datos utilizando `ObjectMapper`.
* **`Principal`**: Gestiona el menú interactivo, la entrada del usuario y coordina el flujo de información entre los servicios y los repositorios.

---

## 🔧 Cómo ejecutar el proyecto localmente

1. Clonar este repositorio.
2. Tener instalado **PostgreSQL** y crear una base de datos local llamada `literalura`.
3. Configurar las credenciales de tu base de datos en el archivo `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
   spring.datasource.username=TU_USUARIO
   spring.datasource.password=TU_CONTRASEÑA
   spring.jpa.hibernate.ddl-auto=update

   👤 Autor

  <img src="assets/autor.jpg" alt="Agus" width="120" style="border:4px solid #4CAF50;">



Agustín Negri Hrytezuk

[GitHub](https://github.com/Agustinnhzk) <img src="assets/github.png" alt="git" width="30"/> | [LinkedIn](https://www.linkedin.com/in/agustín-negri-55b0a4281) <img src="assets/linkedin.png" alt="linkedin" width="30"/>
