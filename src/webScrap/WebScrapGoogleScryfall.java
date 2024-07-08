package webScrap;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import cartaManagement.Carta;
import ficherosFunciones.FuncionesFicheros;
import javafx.concurrent.Task;

public class WebScrapGoogleScryfall {

	public static CompletableFuture<List<String>> getCardLinks(String cardName) {
	    CompletableFuture<List<String>> future = new CompletableFuture<>();

	    Task<List<String>> task = new Task<>() {
	        @Override
	        protected List<String> call() throws Exception {
	            String searchedCardName = FuncionesScrapeoComunes.buscarEnGoogle(cardName); // Usar una nueva variable aquí
	            String searchUrl = String.format("https://scryfall.com/search?as=grid&order=released&q=%s&unique=prints",
	                    searchedCardName.replace(" ", "+"));
	            String scriptPath = FuncionesFicheros.rutaDestinoRecursos + File.separator + "scrap7.js";
	            String command = "node " + scriptPath + " " + searchUrl;
	            return FuncionesScrapeoComunes.executeScraping(command).get(); // Llamada a la función auxiliar
	        }
	    };

	    task.setOnSucceeded(e -> {
	        future.complete(task.getValue());
	    });

	    task.setOnFailed(e -> {
	        future.completeExceptionally(task.getException());
	    });

	    new Thread(task).start();

	    return future;
	}


	public static Carta extraerDatosMTG(String url) {
		List<String> data = getCartaFromPuppeteer(url); // Método para obtener datos de la carta

		String referencia = "";
		String nombre = "";
		String coleccion = "";
		String editorial = "Magic: The Gathering"; // Asignamos directamente la editorial
		String rareza = "";
		String numero = "";
		String normas = "";
		String imagen = "";
		String precioNormal = "0.0";
		String precioFoil = "0.0";

		for (String line : data) {
			if (line.startsWith("Referencia: ")) {
				referencia = line.substring("Referencia: ".length()).trim();
			} else if (line.startsWith("Nombre: ")) {
				nombre = line.substring("Nombre: ".length()).trim().replaceAll("\\(.*\\)", "").trim();
			} else if (line.startsWith("Coleccion: ")) {
				String[] coleccionLimpio = line.substring("Coleccion: ".length()).trim().split(":");
				coleccion = coleccionLimpio[0].trim();
			} else if (line.startsWith("Rareza: ")) {
				rareza = line.substring("Rareza: ".length()).trim();
			} else if (line.startsWith("Numero: ")) {
				numero = line.substring("Numero: ".length()).trim();
			} else if (line.startsWith("Valor: ")) {
				precioNormal = line.substring("Valor: ".length()).trim();
			} else if (line.startsWith("Foil: ")) {
				precioFoil = line.substring("Foil: ".length()).trim();
			} else if (line.startsWith("Normas: ")) {
				normas = line.substring("Normas: ".length()).trim();
			}
		}

		// Construimos y retornamos el objeto Carta
		return new Carta.CartaBuilder("", nombre).numCarta(numero).editorialCarta(editorial).coleccionCarta(coleccion)
				.rarezaCarta(rareza).precioCartaNormal(precioNormal).precioCartaFoil(precioFoil)
				.urlReferenciaCarta(referencia).direccionImagenCarta(imagen).normasCarta(normas).build();
	}

	public static List<String> getCartaFromPuppeteer(String url) {
		List<String> dataArrayList = new ArrayList<>();

		try {
			String scriptPath = FuncionesFicheros.rutaDestinoRecursos + File.separator + "scrap6.js";
			String command = "node " + scriptPath + " " + url;

			int attempt = 0;
			int backoff = 2000; // Tiempo de espera inicial en milisegundos

			while (true) {
				attempt++;
				Process process = Runtime.getRuntime().exec(command);

				// Leer la salida del proceso
				BufferedReader processReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String outputLine;
				StringBuilder output = new StringBuilder();
				while ((outputLine = processReader.readLine()) != null) {
					output.append(outputLine).append("\n");
				}
				processReader.close();

				// Esperar a que termine el proceso
				int exitCode = process.waitFor();
				if (exitCode == 0) {
					// Proceso terminado exitosamente, obtener el resultado
					String dataString = output.toString().trim();
					if (!dataString.isEmpty()) {
						// Dividir los pares clave-valor y añadirlos al List<String>
						String[] keyValuePairs = dataString.split("\n");
						for (String pair : keyValuePairs) {
							dataArrayList.add(pair.trim());
						}
						return dataArrayList;
					} else {
						System.err.println("El resultado obtenido está vacío. Volviendo a intentar...");
						Thread.sleep(backoff); // Esperar antes de intentar nuevamente
						backoff += 10; // Aumentar el tiempo de espera (backoff exponencial)
					}

					if (attempt >= 5) {
						// Si se superan los intentos, devolver un List<String> vacío
						return new ArrayList<>();
					}
				} else {
					// Error al ejecutar el script
					System.err.println("Error al ejecutar el script de Puppeteer. Código de salida: " + exitCode);
					break; // Salir del bucle si hay un error
				}
			}
		} catch (InterruptedException e) {
			// Restaurar el estado de interrupción
			Thread.currentThread().interrupt();
			System.err.println("El hilo fue interrumpido. Terminando la ejecución.");
			// Opcional: Manejar la interrupción de manera adecuada, por ejemplo, limpiando
			// recursos
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error de entrada/salida al ejecutar el script de Puppeteer.");
		}
		return new ArrayList<>(); // Devolver un List<String> vacío en caso de excepción
	}

	



	public static Carta devolverCartaBuscada(String urlCarta) {
		return extraerDatosMTG(urlCarta);
	}
}
