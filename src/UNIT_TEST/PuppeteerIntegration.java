package UNIT_TEST;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PuppeteerIntegration {

	public static void main(String[] args) {
		String url = "https://www.cardtrader.com/en/cards/sliver-overlord-secret-lair-drop-series";

		getPriceFromPuppeteer(url);

		getNormasFromPuppeteer(url);
	}

	public static String getPriceFromPuppeteer(String url) {
		try {
			String scriptPath = "src/UNIT_TEST/scrap.js"; // Ruta relativa al directorio de trabajo

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

				// Leer la salida de error del proceso
				BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				String errorLine;
				StringBuilder errorOutput = new StringBuilder();
				while ((errorLine = errorReader.readLine()) != null) {
					errorOutput.append(errorLine).append("\n");
				}
				errorReader.close();

				// Esperar a que termine el proceso
				int exitCode = process.waitFor();
				if (exitCode == 0) {
					// Proceso terminado exitosamente, obtener el resultado (precio de la carta)
					String precio = output.toString().trim();
					if (!precio.equals("—")) {
						System.out.println(precio);
						return precio;
					} else {
						System.err.println("Intento " + attempt + ": El precio obtenido es -. Volviendo a intentar...");
						Thread.sleep(backoff); // Esperar antes de intentar nuevamente
						backoff += 10; // Aumentar el tiempo de espera (backoff exponencial)
					}
				} else {
					// Error al ejecutar el script
					System.err.println("Error al ejecutar el script de Puppeteer:\n" + errorOutput.toString());
					break; // Salir del bucle si hay un error
				}
			}
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
		return "-1";
	}

	public static String getNormasFromPuppeteer(String url) {

		try {
			// Ruta del script de Puppeteer dentro del proyecto
			String scriptPath = "src/UNIT_TEST/scrap2.js";

			// Construir el comando para ejecutar Node.js con el script y pasar la URL como
			// argumento
			String command = "node " + scriptPath + " " + url;

			while (true) { // Bucle infinito para manejar caso de respuesta "---"
				// Crear un proceso para ejecutar el comando
				Process process = Runtime.getRuntime().exec(command);

				// Leer la salida del proceso
				BufferedReader processReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String outputLine;
				StringBuilder output = new StringBuilder();
				while ((outputLine = processReader.readLine()) != null) {
					output.append(outputLine).append("\n");
				}
				processReader.close();

				// Leer la salida de error del proceso
				BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				String errorLine;
				StringBuilder errorOutput = new StringBuilder();
				while ((errorLine = errorReader.readLine()) != null) {
					errorOutput.append(errorLine).append("\n");
				}
				errorReader.close();

				// Esperar a que termine el proceso
				int exitCode = process.waitFor();
				if (exitCode == 0) {
					System.out.println(output.toString().trim());
					return output.toString().trim();
				} else {
					// Error al ejecutar el script
					System.err.println("Error al ejecutar el script de Puppeteer:\n" + errorOutput.toString());
					break; // Salir del bucle si hay un error
				}
			}
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}

		return "Sin normas";
	}

}