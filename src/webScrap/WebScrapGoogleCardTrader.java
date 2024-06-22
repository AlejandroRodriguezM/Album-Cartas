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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cartaManagement.Carta;
import ficherosFunciones.FuncionesFicheros;
import javafx.concurrent.Task;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class WebScrapGoogleCardTrader {

	public static String agregarMasAMayusculas(String cadena) {
		return cadena.toUpperCase();
	}

	public static List<String> buscarURL(String searchTerm) throws URISyntaxException {
//		if (esURL(searchTerm)) {
//			return buscarURLValida(searchTerm);
//		} else {
		return buscarEnGoogle(searchTerm);
//		}
	}

	public static String buscarURLValida(String urlString) throws URISyntaxException {
		try {
			URI uri = new URI(urlString);
			URL url = uri.toURL();
			HttpURLConnection con = (HttpURLConnection) uri.toURL().openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36");

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
		// Conectar y extraer enlaces con manejo de errores 403
		return getEnlacesFromPuppeteer(urlString);
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
		searchTerm = agregarMasAMayusculas(searchTerm).replace("(", "%28").replace(")", "%29").replace("#", "%23");

		try {
			String encodedSearchTerm = URLEncoder.encode(searchTerm, "UTF-8");
			String urlString = "https://www.google.com/search?q=cardmarke+" + encodedSearchTerm + "+versions";

			URI uri = new URI(urlString);
			URL url = uri.toURL();
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36");

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuilder content = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			con.disconnect();

			String html = content.toString();
			int startIndex = html.indexOf("www.cardmarket.com/");
			List<String> urls = new ArrayList<>(); // Use ArrayList to dynamically store URLs

			while (startIndex != -1) {
				int endIndex = html.indexOf("\"", startIndex);
				if (endIndex != -1) {
					String urlFound = html.substring(startIndex, endIndex);

					if (urlFound.endsWith("/Versions")) { // Check if the URL ends with "/Versions"
						List<String> versionLinks = extraerEnlacesDePagina("https://" + urlFound);
						if (versionLinks.isEmpty()) {
							return null; // Return null if no versions links are found
						} else {
							return versionLinks; // Return the found version links
						}
					} else {
						urls.add(urlFound); // Add the URL to the list
					}
					startIndex = html.indexOf("www.cardmarket.com/", endIndex);
				} else {
					break;
				}
			}

			if (urls.isEmpty()) {
				return null; // Return null if no URLs are found
			} else {
				return urls; // Return the list of URLs found
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null; // Return null in case of exception
		}
	}

	public static Carta extraerDatosMTG(String url) throws URISyntaxException {

		List<String> data = getCartaFromPuppeteer(url);

		String referencia = "";
		String nombre = "";
		String coleccion = "";
		String editorial = "";
		String rareza = "";
		String numero = "";
		String valor = "";
		String foil = "";
		String normas = "";
		String imagen = "";
		String estado = "Nueva";
		String gradeo = "NM (Noir Medium)";

		for (String line : data) {
			if (line.startsWith("Referencia: ")) {
				referencia = line.substring("Referencia: ".length()).trim();
			} else if (line.startsWith("Nombre: ")) {
				nombre = line.substring("Nombre: ".length()).trim();
			} else if (line.startsWith("Coleccion: ")) {
				coleccion = line.substring("Coleccion: ".length()).trim();
			} else if (line.startsWith("Editorial: ")) {
				editorial = line.substring("Editorial: ".length()).trim();
			} else if (line.startsWith("Rareza: ")) {
				rareza = line.substring("Rareza: ".length()).trim();
			} else if (line.startsWith("Numero: ")) {
				numero = line.substring("Numero: ".length()).trim();
			} else if (line.startsWith("Valor: ")) {
				String[] precioCarta = line.substring("Valor: ".length()).trim().split("€");
				valor = precioCarta[0].replace(',', '.');
			} else if (line.startsWith("Foil: ")) {
				foil = line.substring("Foil: ".length()).trim();
			} else if (line.startsWith("Normas: ")) {
				normas = line.substring("Normas: ".length()).trim();
			} else if (line.startsWith("Imagen: ")) {
				String argument = "cardtrader+" + nombre.replace(" ", "+") + "+" + numero + "+"
						+ coleccion.replace(" ", "+");
				String urlCarta = searchCardTraderUrl(argument);
				imagen = extraerDatosImagen(urlCarta);
				System.out.println(imagen);
			}
		}

		return new Carta.CartaBuilder("", nombre).numCarta(numero).editorialCarta(editorial).coleccionCarta(coleccion)
				.rarezaCarta(rareza).esFoilCarta(foil).gradeoCarta(gradeo).estadoCarta(estado).precioCarta(valor)
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
		} catch (InterruptedException | IOException e) {
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
						System.out.println(dataString);
						dataArrayList.add(dataString);
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

	public static String searchCardTraderUrl(String searchTerm) throws URISyntaxException {
		try {
			// Encode the search term for URL compatibility
			String encodedSearchTerm = URLEncoder.encode(searchTerm, "UTF-8");
			String urlString = "https://www.google.com/search?q=" + encodedSearchTerm;

			// Setup the connection
			URI uri = new URI(urlString);
			URL url = uri.toURL();
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
					+ "(KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36");

			// Read the response from Google
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuilder content = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			con.disconnect();

			// Convert response to string
			String html = content.toString();

			// Use regex to find the first URL from cardtrader.com in the search results
			Pattern pattern = Pattern.compile("href=\"(https://www.cardtrader.com/(?!versions)[^\"]+)\"");
			Matcher matcher = pattern.matcher(html);
			if (matcher.find()) {
				// Extract the URL
				String urlFound = matcher.group(1);
				return urlFound;
			} else {
				System.out.println("No se encontraron enlaces de cardtrader.com en los resultados.");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String extraerDatosImagen(String url) {

		Document doc;
		String imagen = "";
		try {
			doc = Jsoup.connect(url).get();

			Element imagenElemento = doc.selectFirst(
					"div.image-flipper.border-radius-10 img[src*='/uploads/'][src$='.jpg'], div.image-flipper.border-radius-10 img[src*='/uploads/'][src$='.png']");
			if (imagenElemento != null) {
				imagen = "https://www.cardtrader.com/" + imagenElemento.attr("src");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return imagen;
	}
}
