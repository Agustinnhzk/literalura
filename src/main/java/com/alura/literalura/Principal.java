package com.alura.literalura;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.DatosAutor;
import com.alura.literalura.model.DatosLibro;
import com.alura.literalura.model.DatosResultados;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ConsumoApi;
import com.alura.literalura.service.ConvierteDatos;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private final String URL_BASE = "https://gutendex.com/books/?search=";

    // Repositorios para guardar en la base de datos
    private LibroRepository libroRepositorio;
    private AutorRepository autorRepositorio;

    public Principal(LibroRepository libroRepositorio, AutorRepository autorRepositorio) {
        this.libroRepositorio = libroRepositorio;
        this.autorRepositorio = autorRepositorio;
    }

    public void muestraElMenu() {

        var opcion = -1;
        while (opcion != 0) {
            System.out.println("Elija la opción a través de su número:");
            System.out.println("1 - Buscar libro por título");
            System.out.println("2 - Mostrar libros registrados");
            System.out.println("3 - Mostrar autores registrados");
            System.out.println("4 - Mostrar autores vivos en un determinado año");
            System.out.println("5 - Listar libros por idioma");
            System.out.println("0 - Salir");
            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(teclado.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            if (opcion == 1) {
                System.out.print("Ingrese el nombre del libro que desea buscar: ");
                var tituloLibro = teclado.nextLine();

                var json = consumoApi.obtenerDatos(URL_BASE + tituloLibro.replace(" ", "%20"));
                ConvierteDatos conversor = new ConvierteDatos();
                DatosResultados datos = conversor.obtenerDatos(json, DatosResultados.class);

                if (datos.resultados().isEmpty()) {
                    System.out.println("No se encontró ningún libro con ese nombre.");
                } else {
                    DatosLibro primerLibro = datos.resultados().get(0);


                    var libroBuscado = libroRepositorio.findByTituloContainsIgnoreCase(primerLibro.titulo());

                    if (libroBuscado.isPresent()) {
                        System.out.println("\n⚠️ ¡Epa! Ese libro ya está guardado en tu catálogo. No lo podemos duplicar.\n");
                    } else {
                        DatosAutor datosAutor = primerLibro.autor().get(0);


                        var autorBuscado = autorRepositorio.findByNombreContainsIgnoreCase(datosAutor.nombre());
                        Autor autor;

                        if (autorBuscado.isPresent()) {
                            autor = autorBuscado.get();
                        } else {
                            autor = new Autor(datosAutor);
                            autorRepositorio.save(autor);
                        }

                        Libro libro = new Libro(primerLibro);
                        libro.setAutor(autor);
                        libroRepositorio.save(libro);

                        System.out.println("\n--- 💾 ¡LIBRO GUARDADO EN LA BASE DE DATOS! ---");
                        System.out.println("Título: " + libro.getTitulo());
                        System.out.println("Autor: " + autor.getNombre());
                        System.out.println("Idioma: " + libro.getIdioma());
                        System.out.println("-----------------------------------------------\n");
                    }
                }

            } else if (opcion == 2) {
                System.out.println("\n--- 📚 LIBROS GUARDADOS EN TU CATÁLOGO ---");

                var librosGuardados = libroRepositorio.findAll();

                if (librosGuardados.isEmpty()) {
                    System.out.println("Todavía no hay libros guardados en la base de datos.");
                } else {
                    for (Libro l : librosGuardados) {
                        System.out.println("Título: " + l.getTitulo());
                        System.out.println("Autor: " + l.getAutor().getNombre());
                        System.out.println("Idioma: " + l.getIdioma());
                        System.out.println("Descargas: " + l.getNumeroDeDescargas());
                        System.out.println("------------------------------------------");
                    }
                }
            } else if (opcion == 3) {
                System.out.println("\n--- ✍️ AUTORES REGISTRADOS ---");

                var autoresGuardados = autorRepositorio.findAll();

                if (autoresGuardados.isEmpty()) {
                    System.out.println("Todavía no hay autores guardados en la base de datos.");
                } else {
                    for (Autor a : autoresGuardados) {
                        System.out.println("Nombre: " + a.getNombre());
                        System.out.println("Fecha de Nacimiento: " + a.getFechaDeNacimiento());
                        System.out.println("Fecha de Fallecimiento: " + a.getFechaDeFallecimiento());
                        System.out.println("------------------------------------------");
                    }
                }
            } else if (opcion == 4) {
                System.out.print("\nIngrese el año que desea investigar: ");
                try {
                    var anioBuscado = Integer.parseInt(teclado.nextLine());
                    var autoresVivos = autorRepositorio.autoresVivosEnUnDeterminadoAnio(anioBuscado);

                    if (autoresVivos.isEmpty()) {
                        System.out.println("No se encontraron autores vivos en nuestra base de datos para el año " + anioBuscado);
                    } else {
                        System.out.println("\n--- 🕰️ AUTORES VIVOS EN " + anioBuscado + " ---");
                        for (Autor a : autoresVivos) {
                            System.out.println("Nombre: " + a.getNombre());
                            System.out.println("Nacimiento: " + a.getFechaDeNacimiento() + " | Fallecimiento: " + a.getFechaDeFallecimiento());
                            System.out.println("------------------------------------------");
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Por favor, ingrese un año válido en formato numérico.");
                }
            } else if (opcion == 5) {
                System.out.println("\nIngrese el idioma para buscar los libros:");
                System.out.println("es - Español");
                System.out.println("en - Inglés");
                System.out.println("fr - Francés");
                System.out.println("pt - Portugués");
                System.out.print("Idioma: ");

                var idiomaBuscado = teclado.nextLine().toLowerCase();
                var librosPorIdioma = libroRepositorio.findByIdioma(idiomaBuscado);

                if (librosPorIdioma.isEmpty()) {
                    System.out.println("No se encontraron libros en ese idioma en la base de datos.");
                } else {
                    System.out.println("\n--- 📚 LIBROS EN IDIOMA '" + idiomaBuscado.toUpperCase() + "' ---");
                    for (Libro l : librosPorIdioma) {
                        System.out.println("Título: " + l.getTitulo());
                        System.out.println("Autor: " + l.getAutor().getNombre());
                        System.out.println("------------------------------------------");
                    }
                }
                System.out.println("\n");
            } else if (opcion == 0) {
                System.out.println("Cerrando la aplicación...");
            } else {
                System.out.println("Opción no válida. Intente de nuevo.\n");
            }
        }
    }
}
