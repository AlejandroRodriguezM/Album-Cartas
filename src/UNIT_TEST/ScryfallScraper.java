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
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cartaManagement.Carta;

public class ScryfallScraper {

	public static List<String> getCardLinks(String cardName) throws IOException {
		String searchUrl = String.format(
				"https://scryfall.com/search?as=grid&order=released&q=%%21%%22%s%%22+include%%3Aextras&unique=prints",
				cardName.replace(" ", "+"));
		Document doc = Jsoup.connect(searchUrl).get();
		Elements cardElements = doc.select("a.card-grid-item-card");

		List<String> cardLinks = new ArrayList<>();
		for (Element cardElement : cardElements) {
			cardLinks.add(cardElement.attr("href"));
		}
		return cardLinks;
	}

	public static void extractCardDetails(List<String> cardLinks) throws IOException {
		for (String link : cardLinks) {
			Document doc = Jsoup.connect(link).get();

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

			// Precios
			String esFoil = "No";
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

			// Referencia de compra
			String buyLink = "";
			Element buyElement = doc.select("a.button-n").first();
			if (buyElement != null) {
				buyLink = buyElement.parent().attr("href");
			}

			if (normalPrice.isEmpty() && normalPriceTCG.isEmpty()) {
				esFoil = "Si";
			}

			if (normalPrice.isEmpty() && foilPrice.isEmpty() && normalPriceTCG.isEmpty() && foilPriceTCG.isEmpty()) {
				System.out.println("No capturado");
			} else {
				// Imprimir los detalles
				System.out.println("Nombre: " + name);
				System.out.println("Número: " + number);
				System.out.println("Colección: " + collection);
				System.out.println("Edicion: Magic: The Gathering");
				System.out.println("Imagen: " + imageUrl);
				System.out.println("Es foil: " + esFoil);
				System.out.println("Rareza: " + rareza);
				System.out.println("Normas: " + normasCarta);
				System.out.println("Precio Normal CardMarket: " + normalPrice);
				System.out.println("Precio Foil CardMarket: " + foilPrice);
				System.out.println("Precio Normal TCGplayer: " + normalPriceTCG);
				System.out.println("Precio Foil TCGplayer: " + foilPriceTCG);
				System.out.println("Referencia de compra: " + link);
				System.out.println("--------");
//
//				return new Carta.CartaBuilder("", name).numCarta(number).editorialCarta("Magic: The Gathering")
//						.coleccionCarta(collection).rarezaCarta(rareza).esFoilCarta(esFoil)
//						.gradeoCarta("NM (Noir Medium)").estadoCarta("Comprado").precioCarta(normalPrice)
//						.urlReferenciaCarta(referencia).direccionImagenCarta(imageUrl).normasCarta(normasCarta).build();
			}

		}
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

    public static void comprobarScrap() {
        String url = "https://www.tcgplayer.com/product/10956/magic-scourge-sliver-overlord?Language=English";
        String filePath = "output.html"; // El archivo donde se guardará el HTML

        try {
            // Conectar a la URL y obtener el documento HTML
            Document doc = Jsoup.connect(url).get();

            // Obtener el HTML de la página
            String htmlContent = doc.html();

            // Escribir el HTML en un archivo
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(htmlContent);
            }

            System.out.println("El contenido HTML ha sido escrito en " + filePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public static void main(String[] args) {
//		try {
//
//			String busqueda = "Sliver";
//			String nombreCarta = buscarEnGoogle(busqueda);
//			List<String> cardLinks = getCardLinks(nombreCarta);
//			extractCardDetails(cardLinks);
//			
//			
//
//		} catch (IOException | URISyntaxException e) {
//			e.printStackTrace();
//		}

		comprobarScrap();
	}
}
