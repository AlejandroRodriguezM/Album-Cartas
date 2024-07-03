package UNIT_TEST;

import java.io.BufferedReader;
import java.io.FileWriter;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cartaManagement.Carta;
import javafx.concurrent.Task;

public class ScryfallScraper {

	public static CompletableFuture<List<String>> getCardLinks(String cardName) {
		CompletableFuture<List<String>> future = new CompletableFuture<>();

		Task<List<String>> task = new Task<>() {
			@Override
			protected List<String> call() throws IOException, URISyntaxException {
				String searchedCardName = buscarEnGoogle(cardName); // Usar una nueva variable aquí
				String searchUrl = String.format("https://scryfall.com/search?as=grid&order=released&q="
						+ searchedCardName.replaceAll(" ", "+") + "&unique=prints");
				System.out.println(searchUrl);

				Connection connection = Jsoup.connect(searchUrl);
				Document doc = connection.get();

				// Check if URL was redirected
				String finalUrl = connection.response().url().toString();

				List<String> cardLinks = new ArrayList<>();
				if (!finalUrl.equals(searchUrl)) {
					// URL was redirected, add the final URL to the list
					cardLinks.add(finalUrl);
				} else {
					// Check for multiple results
					Elements cardElements = doc.select("a.card-grid-item-card");
					for (Element cardElement : cardElements) {
						cardLinks.add(cardElement.attr("href"));
					}

					// If no multiple results, check for single result
					if (cardLinks.isEmpty()) {
						Element singleCardElement = doc.selectFirst("a.card-profile");
						if (singleCardElement != null) {
							cardLinks.add(singleCardElement.attr("href"));
						}
					}
				}

				if (cardLinks.isEmpty()) {
					System.err.println(
							"No se encontraron enlaces de cartas para el nombre de carta: " + searchedCardName);
				}

				return cardLinks;
			}
		};

		task.setOnSucceeded(e -> {
			List<String> urls = task.getValue();
			if (urls == null || urls.isEmpty()) {
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

	public static Carta extractCardDetails(String cardLinks) throws IOException {

		Document doc = Jsoup.connect(cardLinks).get();

		// Nombre
		String name = doc.select("span.card-text-card-name").text();

		// Número
		String number = "";
		Elements detailsElements = doc.select("span.prints-current-set-details");
		if (!detailsElements.isEmpty()) {
			String detailsText = detailsElements.text();
			number = detailsText.split(" ")[0].substring(1); // Tomar solo el número después de #
		}
		String rareza = "";
		if (!detailsElements.isEmpty()) {
			String detailsText = detailsElements.text();

			// Buscar la parte entre dos puntos
			int startIndex = detailsText.indexOf("·");
			int endIndex = detailsText.indexOf("·", startIndex + 1);

			if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
				String extractedText = detailsText.substring(startIndex + 1, endIndex).trim();
				rareza = extractedText;
			}
		}

		// Colección
		String collection = doc.select("span.prints-current-set-name").text();

		// Imagen
		String imageUrl = "";
		Element imageElement = doc.select("div.card-image-front img.card.border-black").first();
		if (imageElement != null) {
			imageUrl = imageElement.attr("src");
		}

		String normasCarta = "";
		Element normasElement = doc.select("div.card-text-box div.card-text-oracle p").first();
		if (imageElement != null) {
			normasCarta = normasElement.text();
		}

		String normalPrice = "";
		Elements priceElements = doc.select("span.currency-eur");

		for (Element priceElement : priceElements) {
			Element prevSibling = priceElement.previousElementSibling();
			if (prevSibling != null && prevSibling.tagName().equals("i")
					&& prevSibling.text().equals("Buy on Cardmarket")) {
				normalPrice = priceElement.text();
			}
		}

		String foilPrice = "";
		for (Element priceElement : priceElements) {
			Element prevSibling = priceElement.previousElementSibling();
			if (prevSibling != null && prevSibling.tagName().equals("i")
					&& prevSibling.text().equals("Buy foil on Cardmarket")) {
				foilPrice = priceElement.text();

			}
		}

		// Precios
		String normalPriceTCG = "";
		Elements priceElementsTCG = doc.select("span.currency-usd");

		for (Element priceElement : priceElementsTCG) {
			Element prevSibling = priceElement.previousElementSibling();
			if (prevSibling != null && prevSibling.tagName().equals("i")
					&& prevSibling.text().equals("Buy on TCGplayer")) {
				normalPriceTCG = priceElement.text();
			}
		}

		String foilPriceTCG = "";
		for (Element priceElement : priceElementsTCG) {
			Element prevSibling = priceElement.previousElementSibling();
			if (prevSibling != null && prevSibling.tagName().equals("i")
					&& prevSibling.text().equals("Buy foil on TCGplayer")) {
				foilPriceTCG = priceElement.text();
			}
		}

		if (normalPrice.isEmpty() && foilPrice.isEmpty() && normalPriceTCG.isEmpty() && foilPriceTCG.isEmpty()) {
			System.out.println("No capturado");
		} else {
			// Imprimir los detalles
			return new Carta.CartaBuilder("", name).numCarta(number).editorialCarta("Magic: The Gathering")
					.coleccionCarta(collection).rarezaCarta(rareza).precioCartaNormal(cleanPrice(normalPrice))
					.precioCartaFoil(cleanPrice(foilPrice)).urlReferenciaCarta(cardLinks).direccionImagenCarta(imageUrl)
					.normasCarta(normasCarta).build();
		}
		return null;

	}

	// Función para limpiar los precios
	public static String cleanPrice(String price) {
	    if (price == null || price.isEmpty()) {
	        return "0";
	    }

	    // Patrón para extraer números, punto decimal, euro (€) y dólar ($)
	    Pattern pattern = Pattern.compile("[0-9]+([,.][0-9]*)?|[€$]");
	    Matcher matcher = pattern.matcher(price);

	    StringBuilder cleanedPrice = new StringBuilder();
	    while (matcher.find()) {
	        cleanedPrice.append(matcher.group());
	    }

	    // Si no se encuentra ningún número, retornar "0"
	    if (cleanedPrice.length() == 0) {
	        return "0";
	    }

	    return cleanedPrice.toString();
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

	public static Carta devolverCartaBuscada(String urlCarta) {
		try {
			return extractCardDetails(urlCarta);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
