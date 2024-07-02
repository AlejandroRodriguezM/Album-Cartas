package UNIT_TEST;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ficherosFunciones.FuncionesFicheros;

public class TCGPlayerTest {

	public static List<String> urlTCG(String parametro) {
		String url = "https://www.tcgplayer.com/search/all/product?q=" + parametro + "&ProductTypeName=Cards&page=1";
		System.out.println(url);
		String scriptPath = FuncionesFicheros.rutaDestinoRecursos + File.separator + "scrap4.js";
		String command = "node " + scriptPath + " " + url;
		return executeScript(command);
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

	private static void processLine(String line) {
		if (line.startsWith("Título:")) {
			String titulo = line.substring("Título:".length()).trim();
			System.out.println("Título encontrado: " + titulo);
		} else if (line.startsWith("Número:")) {
			String numero = line.substring("Número:".length()).trim();
			System.out.println("Número encontrado: " + numero);
		} else if (line.startsWith("Categoria:")) {
			String categoria = line.substring("Categoria:".length()).trim();
			System.out.println("Categoría encontrada: " + categoria);
		} else if (line.startsWith("Colección:")) {
			String coleccion = line.substring("Colección:".length()).trim();
			System.out.println("Colección encontrada: " + coleccion);
		} else if (line.startsWith("Rareza:")) {
			String rareza = line.substring("Rareza:".length()).trim();
			System.out.println("Rareza encontrada: " + rareza);
		} else if (line.startsWith("Precio Normal:")) {
			String precioNormal = line.substring("Precio Normal:".length()).trim();
			System.out.println("Precio Normal encontrado: " + precioNormal.replace("$", ""));
		} else if (line.startsWith("Precio Foil:")) {
			String precioFoil = line.substring("Precio Foil:".length()).trim();
			System.out.println("Precio Foil encontrado: " + precioFoil.replace("$", ""));
		} else if (line.startsWith("Dirección de la imagen:")) {
			String direccionImagen = line.substring("Dirección de la imagen:".length()).trim();
			System.out.println("Dirección de la imagen encontrada: " + direccionImagen.replace("344x344", "437x437"));
		} else if (line.startsWith("Normas:")) {
			String normas = line.substring("Normas:".length()).trim();
			System.out.println("Normas encontradas: " + normas);
		} else if (line.startsWith("Referencia:")) {
			String referencia = line.substring("Referencia:".length()).trim();
			System.out.println("URL encontrada: " + referencia);
		} else {
			System.out.println("Línea no procesada: " + line);
		}
	}

	public static void main(String[] args) {

		String parametro = "Fragmentado";
		List<String> listaCartas = urlTCG(parametro);

		for (String string : listaCartas) {
			List<String> datos = datosCartas(string);
			for (String dato : datos) {
				processLine(dato);

			}
			System.out.println("---------------------------------");
			System.out.println();
		}

	}

}
