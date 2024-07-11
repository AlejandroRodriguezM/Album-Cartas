package webScrap;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import cartaManagement.Carta;
import ficherosFunciones.FuncionesFicheros;
import javafx.concurrent.Task;

public class WebScrapGoogleCardMarket {

	public static String agregarMasAMayusculas(String cadena) {
		return cadena.toUpperCase();
	}

	public static List<String> buscarURL(String searchTerm) throws URISyntaxException, IOException {
		return buscarEnGoogle(searchTerm);
	}

	public static String buscarURLValida(String urlString) throws URISyntaxException {
		try {
			URI uri = new URI(urlString);
			URL url = uri.toURL();
			HttpURLConnection con = (HttpURLConnection) uri.toURL().openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.6478.127 Safari/537.36");

			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				return url.toString(); // Devuelve la representación de cadena de la URL
			} else {
				System.out.println("La URL proporcionada no es válida.");
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String encontrarURLRelevante(String[] urls, String searchTerm) {
		for (String url : urls) {
			if (url.contains("/Versions")) {
				return url; // Devuelve inmediatamente la primera URL que contenga "/Versions"
			}
		}
		return null; // Si no se encuentra ninguna URL con "/Versions", devuelve null
	}

	public static int contarCoincidencias(String url, String searchTerm) {
		int coincidencia = 0;
		for (String word : searchTerm.split("\\s+")) {
			if (url.contains(word)) {
				coincidencia += word.length();
			}
		}
		return coincidencia;
	}

	public static List<String> extraerEnlacesDePagina(String urlString) {
		// Conectar y extraer enlaces originales
		urlString = urlString.replaceFirst("/\\w{2}/", "/en/");
		List<String> enlaceOriginales = getEnlacesFromPuppeteer(urlString);

		// Crear un mapa para agrupar enlaces por su versión sin "?isFoil=Y"
		Map<String, List<String>> enlacesMap = new HashMap<>();

		// Agregar enlaces originales al mapa
		for (String enlace : enlaceOriginales) {
			enlace = enlace + "?language=1";
			enlacesMap.computeIfAbsent(enlace, k -> new ArrayList<>()).add(enlace);
		}

		// Crear una nueva lista para contener todos los enlaces en el orden deseado
		List<String> enlacesFinales = new ArrayList<>();

		// Agregar los enlaces al resultado final en el orden correcto
		for (List<String> enlaces : enlacesMap.values()) {
			enlacesFinales.addAll(enlaces);
		}

		return enlacesFinales;
	}

	public static CompletableFuture<List<String>> iniciarBusquedaGoogle(String valorCodigo) {
		CompletableFuture<List<String>> future = new CompletableFuture<>();

		Task<List<String>> task = createGoogleSearchTask(valorCodigo);
		task.setOnSucceeded(e -> {
			List<String> urls = task.getValue();
			if (urls == null) {
				future.complete(Collections.emptyList()); // Return an empty list if no results are found
			} else {
				future.complete(urls); // Completes the future with the results
			}
		});
		task.setOnFailed(e -> {
			future.completeExceptionally(task.getException()); // Completes the future with an exception if the task
																// fails
		});

		new Thread(task).start();

		return future;
	}

	public static Task<List<String>> createGoogleSearchTask(String searchTerm) {
		return new Task<>() {
			@Override
			protected List<String> call() throws Exception {
				return buscarEnGoogle(searchTerm);
			}
		};
	}

	public static List<String> buscarEnGoogle(String searchTerm) throws URISyntaxException {
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
			try {
				// Introduce un retardo de 1 segundo entre solicitudes
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
			List<String> urls = new ArrayList<>();

			while (startIndex != -1) {
				int endIndex = html.indexOf("\"", startIndex);
				if (endIndex != -1) {
					String urlFound = html.substring(startIndex, endIndex);

					// Verificar si la URL termina con "/Versions"
					if (urlFound.endsWith("/Versions")) {
						List<String> versionLinks = extraerEnlacesDePagina("https://" + urlFound);

						if (versionLinks.isEmpty()) {
							return null; // No se encontraron enlaces de versiones
						} else {
							return versionLinks; // Devolver los enlaces de versiones encontrados
						}
					} else {
						urls.add(urlFound); // Agregar la URL a la lista
					}

					// Buscar la siguiente ocurrencia de "www.cardmarket.com/"
					startIndex = html.indexOf("www.cardmarket.com/", endIndex);
				} else {
					break;
				}
			}

			// Devolver la lista de URLs encontradas
			if (urls.isEmpty()) {
				return null; // No se encontraron URLs
			} else {
				return urls;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null; // Devolver null en caso de excepción
		}
	}

	public static Carta extraerDatosMTG(String url) {

		List<String> data = getCartaFromPuppeteer(url); // Método para obtener datos de la carta

		String referencia = "";
		String nombre = "";
		String coleccion = "";
		String editorial = "";
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
				String nombreLimpio = line.substring("Nombre: ".length()).trim().replaceAll("\\(.*\\)", "").trim();
				nombre = nombreLimpio;
			} else if (line.startsWith("Coleccion: ")) {
				String[] coleccionLimpio = line.substring("Coleccion: ".length()).trim().split(":");
				coleccion = coleccionLimpio[0];
			} else if (line.startsWith("Editorial: ")) {
				editorial = line.substring("Editorial: ".length()).trim();
			} else if (line.startsWith("Rareza: ")) {
				rareza = line.substring("Rareza: ".length()).trim();
			} else if (line.startsWith("Numero: ")) {
				numero = line.substring("Numero: ".length()).trim();
			} else if (line.startsWith("Valor: ")) {
				String[] precioCarta = line.substring("Valor: ".length()).trim().split("€");
				String numeroFormateado = precioCarta[0].replace(".", "").replace(",", ".");
				double numFormateado = Double.parseDouble(numeroFormateado);
				precioNormal = "€" + numFormateado;
			} else if (line.startsWith("Valor (Foil)")) {
				String[] precioCarta = line.substring("Valor (Foil): ".length()).trim().split("€");
				String numeroFormateado = precioCarta[0].replace(".", "").replace(",", ".");
				double numFormateado = Double.parseDouble(numeroFormateado);
				precioFoil = "€" + numFormateado;
			} else if (line.startsWith("Normas: ")) {
				normas = line.substring("Normas: ".length()).trim();
			}

			if (precioNormal.equalsIgnoreCase(precioFoil)) {
				precioNormal = "0.0";
			}

		}
		return new Carta.CartaBuilder("", nombre).numCarta(numero).editorialCarta(editorial).coleccionCarta(coleccion)
				.rarezaCarta(rareza).precioCartaNormal(precioNormal).precioCartaFoil(precioFoil)
				.urlReferenciaCarta(referencia).direccionImagenCarta(imagen).normasCarta(normas).build();
	}

	public static String limpiarTexto(String texto) {
		// Expresión regular para eliminar caracteres no deseados
		// En este caso, estamos eliminando : ( ) ! - ' " < >
		return texto.replaceAll("[\\:\\(\\)\\!\\-\\'\\\"\\<\\>]", "");
	}

	public static List<String> getCartaFromPuppeteer(String url) {
		List<String> dataArrayList = new ArrayList<>();

		try {
			String scriptPath = FuncionesFicheros.rutaDestinoRecursos + File.separator + "scrap.js";
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
					System.err.println("Error al ejecutar el script de Puppeteer");
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
		}
		return new ArrayList<>(); // Devolver un List<String> vacío en caso de excepción
	}

	public static List<String> getEnlacesFromPuppeteer(String url) {
		List<String> dataArrayList = new ArrayList<>();

		try {
			String scriptPath = FuncionesFicheros.rutaDestinoRecursos + File.separator + "scrap2.js";
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
						// Dividir el resultado en líneas individuales
						String[] enlaces = dataString.split("\n");
						// Agregar cada enlace a la lista
						dataArrayList.addAll(Arrays.asList(enlaces));
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
					System.err.println("Error al ejecutar el script de Puppeteer");
					break; // Salir del bucle si hay un error
				}
			}
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>(); // Devolver un List<String> vacío en caso de excepción
	}

	public static boolean esURL(String urlString) {
		try {
			// Intenta crear una instancia de URI
			URI uri = new URI(urlString);
			// Verifica si la URI tiene un esquema (protocolo) válido
			if (uri.getScheme() != null
					&& (uri.getScheme().equalsIgnoreCase("http") || uri.getScheme().equalsIgnoreCase("https"))) {
				return true; // Si la URI tiene un esquema válido, se considera una URL válida
			}
		} catch (URISyntaxException e) {
			// La cadena no es una URI válida
		}
		return false; // Si hay una excepción o la URI no tiene un esquema válido, la URL no es válida
	}

	public static String searchWebImagen(String query) {
		String googleSearchUrl = "https://www.google.com/search?q=";
		String charset = "UTF-8";
		String userAgent = "Mozilla/5.0";

		String url;
		try {
			url = googleSearchUrl + URLEncoder.encode(query, charset);
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestProperty("User-Agent", userAgent);

			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(connection.getInputStream(), charset))) {
				String line;
				StringBuilder response = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}

				// Buscar enlaces que comiencen con "/url?q="
				Pattern pattern = Pattern.compile("<a href=\"/url\\?q=(https://www.cardtrader.com/[^\"]+)\"");
				Matcher matcher = pattern.matcher(response.toString());

				while (matcher.find()) {
					String urlFound = matcher.group(1);
					urlFound = cleanGoogleUrl(urlFound);
					if (!urlFound.contains("/versions")) {
						return urlFound;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ""; // No se encontró un enlace adecuado
	}

	private static String cleanGoogleUrl(String url) {
		// Eliminar fragmentos adicionales de la URL de Google
		int index = url.indexOf("&");
		if (index != -1) {
			return url.substring(0, index);
		}
		return url;
	}

	public static String extraerImagen(Carta carta) {
		String argument = "cardtrader+" + carta.getNomCarta().replace(" ", "+") + "+" + carta.getNumCarta() + "+"
				+ carta.getColeccionCarta().replace(" ", "+");
		String urlCarta = searchWebImagen(argument);
		if (urlCarta.contains("/cards/")) {
			System.out.println(urlCarta);
			return extraerDatosImagen(urlCarta);
		}
		return "";
	}

	public static String extraerDatosImagen(String url) {
		Document doc;
		try {
			doc = Jsoup.connect(url).get();

			// Adjust selector according to actual HTML structure of the target page
			Element imagenElemento = doc.selectFirst(
					"div.image-flipper.border-radius-10 img[src*='/uploads/'][src$='.jpg'], div.image-flipper.border-radius-10 img[src*='/uploads/'][src$='.png']");

			if (imagenElemento != null) {
				return "https://www.cardtrader.com/" + imagenElemento.attr("src");
			}
		} catch (IOException e) {
			// Log the error or handle it appropriately
			System.err.println("Error fetching image: " + e.getMessage());
		}

		return "";
	}
}