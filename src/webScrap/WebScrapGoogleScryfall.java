package webScrap;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import cartaManagement.Carta;
import ficherosFunciones.FuncionesFicheros;
import javafx.concurrent.Task;

public class WebScrapGoogleScryfall {

	public static List<String> getCardLinks(String cardName) {
	    CompletableFuture<List<String>> future = new CompletableFuture<>();

	    Task<List<String>> task = new Task<>() {
	        @Override
	        protected List<String> call() throws Exception {
	            String searchedCardName = FuncionesScrapeoComunes.buscarEnGoogle(cardName);
	            String searchUrl = String.format(
	                "https://scryfall.com/search?as=grid&order=released&q=%s&unique=prints",
	                searchedCardName.replace(" ", "+"));
	            String scriptPath = FuncionesFicheros.rutaDestinoRecursos + File.separator + "scrap7.js";
	            String command = "node " + scriptPath + " " + searchUrl;
	            return FuncionesScrapeoComunes.executeScraping(command).get(); // Llamada a la función auxiliar
	        }
	    };

	    task.setOnSucceeded(e -> {
	        List<String> result = task.getValue();
	        future.complete(result != null ? result : Collections.emptyList());
	    });

	    task.setOnFailed(e -> future.completeExceptionally(task.getException()));

	    Thread thread = new Thread(task);
	    thread.setDaemon(true); // Configurar como daemon para no bloquear la terminación de la aplicación
	    thread.start();

	    try {
	        return future.get(); // Bloquea hasta que el CompletableFuture esté completo
	    } catch (Exception e) {
	        // Manejar excepciones y retornar una lista vacía en caso de error
	        e.printStackTrace();
	        return Collections.emptyList();
	    }
	}

	public static Carta extraerDatosMTG(String url) {
		String scriptPath = FuncionesFicheros.rutaDestinoRecursos + File.separator + "scrap6.js";
		List<String> data = FuncionesScrapeoComunes.getCartaFromPuppeteer(url, scriptPath);

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
			} else if (line.startsWith("Imagen: ")) {
				imagen = line.substring("Imagen: ".length()).trim();
			}
		}

		Carta carta = new Carta.CartaBuilder("", nombre).numCarta(numero).editorialCarta(editorial)
				.coleccionCarta(coleccion).rarezaCarta(rareza).precioCartaNormal(precioNormal)
				.precioCartaFoil(precioFoil).urlReferenciaCarta(referencia).direccionImagenCarta(imagen)
				.normasCarta(normas).build();
		carta.sustituirCaracteres(carta);
		return carta;
	}

	public static Carta devolverCartaBuscada(String urlCarta) {
		return extraerDatosMTG(urlCarta);
	}
}
