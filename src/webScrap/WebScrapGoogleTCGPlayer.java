package webScrap;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import cartaManagement.Carta;
import ficherosFunciones.FuncionesFicheros;
import javafx.concurrent.Task;

public class WebScrapGoogleTCGPlayer {

	public static CompletableFuture<List<String>> urlTCG(String parametro) {
		CompletableFuture<List<String>> future = new CompletableFuture<>();

		Task<List<String>> task = new Task<>() {
			@Override
			protected List<String> call() throws Exception {
				String searchedCardName = FuncionesScrapeoComunes.buscarEnGoogle(parametro); // Usar una nueva variable
																								// aquí
				String url = "https://www.tcgplayer.com/search/all/product?q=" + searchedCardName
						+ "&ProductTypeName=Cards&page=1";
				String scriptPath = FuncionesFicheros.rutaDestinoRecursos + File.separator + "scrap4.js";
				String command = "node " + scriptPath + " " + url;
				return FuncionesScrapeoComunes.executeScraping(command).get(); // Llamada a la función auxiliar
			}
		};

		task.setOnSucceeded(e -> future.complete(task.getValue()));

		task.setOnFailed(e -> future.completeExceptionally(task.getException()));

		new Thread(task).start();

		return future;
	}

	private static Carta processLine(String url) {
		String scriptPath = FuncionesFicheros.rutaDestinoRecursos + File.separator + "scrap3.js";
		List<String> data = FuncionesScrapeoComunes.getCartaFromPuppeteer(url, scriptPath);

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

		for (String line : data) {
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
				precioNormal = line.substring("Precio Normal:".length()).trim();
			} else if (line.startsWith("Precio Foil:")) {
				precioFoil = line.substring("Precio Foil:".length()).trim();
			} else if (line.startsWith("Dirección de la imagen:")) {
				direccionImagen = getUrlImagen(line);
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

	public static String getUrlImagen(String datos) {

		return datos.substring("Dirección de la imagen:".length()).trim().replace("344x344", "437x437");
	}

	public static Carta devolverCartaBuscada(String urlCarta) {
		return processLine(urlCarta);
	}

}
