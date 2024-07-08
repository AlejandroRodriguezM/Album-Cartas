package webScrap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javafx.concurrent.Task;

public class FuncionesScrapeoComunes {

	public static CompletableFuture<List<String>> executeScraping(String command) {
		CompletableFuture<List<String>> future = new CompletableFuture<>();

		Task<List<String>> task = new Task<>() {
			@Override
			protected List<String> call() throws Exception {
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

	public static List<String> executeScript(String command) {
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

	public static String buscarEnGoogle(String searchTerm) throws URISyntaxException {
		try {
			// Codificar el término de búsqueda
			String encodedSearchTerm = URLEncoder.encode(searchTerm, "UTF-8");

			// Construir la URL de búsqueda en Google
			String urlString = "https://www.google.com/search?q=cardmarket+" + encodedSearchTerm + "+versions";

			// Crear objeto URI y URL
			URI uri = new URI(urlString);
			URL url = uri.toURL();

			// Establecer la conexión HTTP
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			// Establecer el User-Agent para simular una solicitud desde el navegador Chrome
			con.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.6478.127 Safari/537.36");

			// Leer la respuesta
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuilder content = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			con.disconnect();

			// Convertir la respuesta a String
			String html = content.toString();

			// Buscar la URL de Cardmarket
			int startIndex = html.indexOf("www.cardmarket.com/");
			while (startIndex != -1) {
				int endIndex = html.indexOf("\"", startIndex);
				if (endIndex != -1) {
					String urlFound = html.substring(startIndex, endIndex);
					if (urlFound.endsWith("/Versions")) {
						System.out.println("URL: " + urlFound);
						urlFound = urlFound.replaceFirst("/\\w{2}/", "/en/");
						return extractCardName(urlFound);
					}
					startIndex = html.indexOf("www.cardmarket.com/", endIndex);
				}
			}
			return null; // No se encontró ninguna URL
		} catch (IOException e) {
			e.printStackTrace();
			return null; // Devolver null en caso de excepción
		}
	}
	
	private static String extractCardName(String url) {
		// Encontrar el índice de "/Cards/" en la URL
		int cardsIndex = url.indexOf("/Cards/");
		if (cardsIndex != -1) {
			// Extraer el nombre de la carta entre "/Cards/" y "/Versions"
			int startIndex = cardsIndex + "/Cards/".length();
			int endIndex = url.indexOf("/Versions", startIndex);
			if (endIndex != -1) {
				String cardName = url.substring(startIndex, endIndex);
				// Reemplazar guiones por espacios
				cardName = cardName.replace("-", " ");
				return cardName;
			}
		}
		return null; // No se encontró el nombre de la carta
	}

}
