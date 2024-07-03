package UNIT_TEST;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import cartaManagement.Carta;
import ficherosFunciones.FuncionesFicheros;
import javafx.concurrent.Task;

public class TCGPlayerTest {

	public static CompletableFuture<List<String>> urlTCG(String parametro) {
		CompletableFuture<List<String>> future = new CompletableFuture<>();

		Task<List<String>> task = new Task<>() {
			@Override
			protected List<String> call() throws Exception {
				String url = "https://www.tcgplayer.com/search/all/product?q=" + parametro
						+ "&ProductTypeName=Cards&page=1";
				String scriptPath = FuncionesFicheros.rutaDestinoRecursos + File.separator + "scrap4.js";
				String command = "node " + scriptPath + " " + url;
				return executeScript(command);
			}
		};

		task.setOnSucceeded(e -> {
			List<String> result = task.getValue();
			if (result == null || result.isEmpty()) {
				future.complete(Collections.emptyList());
			} else {
				future.complete(result);
			}
		});

		task.setOnFailed(e -> {
			future.completeExceptionally(task.getException());
		});

		new Thread(task).start();

		return future;
	}

	public static List<String> datosCartas(String url) {
		String scriptPath = FuncionesFicheros.rutaDestinoRecursos + File.separator + "scrap3.js";
		String command = "node " + scriptPath + " " + url;
		return executeScript(command);
	}

	private static List<String> executeScript(String command) {
		List<String> dataArrayList = new ArrayList<>();
		int maxAttempts = 5;
		int backoff = 2000;

		for (int attempt = 1; attempt <= maxAttempts; attempt++) {
			try {
				Process process = Runtime.getRuntime().exec(command);
				BufferedReader processReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				StringBuilder output = new StringBuilder();
				String line;

				while ((line = processReader.readLine()) != null) {
					output.append(line).append("\n");
				}
				processReader.close();

				int exitCode = process.waitFor();
				if (exitCode == 0) {
					String dataString = output.toString().trim();
					if (!dataString.isEmpty()) {
						String[] keyValuePairs = dataString.split("\n");
						for (String pair : keyValuePairs) {
							dataArrayList.add(pair.trim());
						}
						return dataArrayList;
					} else {
						System.err.println("El resultado obtenido está vacío. Volviendo a intentar...");
					}
				} else {
					System.err.println("Error al ejecutar el script de Puppeteer, código de salida: " + exitCode);
				}

				Thread.sleep(backoff);
				backoff += 2000;

			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}

		return new ArrayList<>();
	}

	private static Carta processLine(String urlCarta) {
		List<String> datos = datosCartas(urlCarta);

		// Variables para almacenar los valores extraídos
		String titulo = "";
		String numero = "";
		String categoria = "";
		String coleccion = "";
		String rareza = "";
		String precioNormal = "";
		String precioFoil = "";
		String direccionImagen = "";
		String normas = "";
		String referencia = "";

		for (String line : datos) {
			// Procesamiento de la línea
			if (line.startsWith("Titulo:")) {
				titulo = line.substring("Titulo:".length()).trim();
			} else if (line.startsWith("Numero:")) {
				numero = line.substring("Numero:".length()).trim();
			} else if (line.startsWith("Categoria:")) {
				categoria = line.substring("Categoria:".length()).trim();
			} else if (line.startsWith("Coleccion:")) {
				coleccion = line.substring("Coleccion:".length()).trim();
			} else if (line.startsWith("Rareza:")) {
				rareza = line.substring("Rareza:".length()).trim();
			} else if (line.startsWith("Precio Normal:")) {
				precioNormal = line.substring("Precio Normal:".length()).trim().replace("$", "");
			} else if (line.startsWith("Precio Foil:")) {
				precioFoil = line.substring("Precio Foil:".length()).trim().replace("$", "");
			} else if (line.startsWith("Dirección de la imagen:")) {
				direccionImagen = line.substring("Dirección de la imagen:".length()).trim().replace("344x344",
						"437x437");
			} else if (line.startsWith("Normas:")) {
				normas = line.substring("Normas:".length()).trim();
			} else if (line.startsWith("Referencia:")) {
				referencia = line.substring("Referencia:".length()).trim();
			}
		}

		return new Carta.CartaBuilder("", titulo).numCarta(numero).editorialCarta(categoria).coleccionCarta(coleccion)
				.rarezaCarta(rareza).precioCartaNormal(precioNormal).precioCartaFoil(precioFoil)
				.urlReferenciaCarta(referencia).direccionImagenCarta(direccionImagen).normasCarta(normas).build();
	}

	public static Carta devolverCartaBuscada(String urlCarta) {
		return processLine(urlCarta);
	}

}
