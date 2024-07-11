package cartaManagement;

import java.io.File;

import ficherosFunciones.FuncionesExcel;
import funcionesAuxiliares.Utilidades;

public class CartaFichero extends Carta {

	public static Carta datosCartaFichero(String lineText) {
		// Verificar si la línea está vacía
		if (lineText == null || lineText.trim().isEmpty()) {
			// Si la línea está vacía, devuelve null para indicar que la línea debe ser
			// ignorada
			return null;
		}

		String[] data = lineText.split(";");

		// Verificar si hay suficientes elementos en el array 'data'
		if (data.length >= 11) { // Ajusta este valor según la cantidad de campos esperados

			String nombre = data[1];
			String numCarta = data[2];
			String editorialCarta = data[3];
			String coleccionCarta = data[4];
			String rarezaCarta = data[5];
			String precioCartaNormal = limpiarPrecio(data[6]);
			String precioCartaFoil = limpiarPrecio(data[7]);
			String urlReferenciaCarta = data[8];
			String direccionImagenCarta = data[9];
			String normasCarta = data[10];
			String nombrePortada = Utilidades.obtenerNombrePortada(false, direccionImagenCarta);
			String imagen = FuncionesExcel.DEFAULT_PORTADA_IMAGE_PATH + File.separator + nombrePortada;

			urlReferenciaCarta = (urlReferenciaCarta.isEmpty()) ? "Sin referencia" : urlReferenciaCarta;

			return new Carta.CartaBuilder("", nombre).numCarta(numCarta).editorialCarta(editorialCarta)
					.coleccionCarta(coleccionCarta).rarezaCarta(rarezaCarta).precioCartaNormal(precioCartaNormal)
					.precioCartaFoil(precioCartaFoil).urlReferenciaCarta(urlReferenciaCarta)
					.direccionImagenCarta(imagen).normasCarta(normasCarta).build();
		} else {
			return null;
		}
	}

	public static String limpiarPrecio(String precioStr) {
		// Verificar si el precio es nulo o está vacío
		if (precioStr == null || precioStr.isEmpty()) {
			return "0";
		}

		// Eliminar espacios en blanco al inicio y al final
		precioStr = precioStr.trim();

		// Paso 1: Eliminar símbolos repetidos y dejar solo uno
		precioStr = precioStr.replaceAll("([€$])\\1+", "$1");

		// Paso 2: Si hay varios símbolos, mantener solo uno y eliminar el resto
		precioStr = precioStr.replaceAll("([€$])(.*)([€$])", "$1$2");

		// Extraer el símbolo monetario, si existe
		String symbol = "";
		if (precioStr.startsWith("€") || precioStr.startsWith("$")) {
			symbol = precioStr.substring(0, 1);
			precioStr = precioStr.substring(1);
		}

		// Eliminar caracteres no numéricos excepto el primer punto decimal
		precioStr = precioStr.replaceAll("[^\\d.]", "");
		int dotIndex = precioStr.indexOf('.');
		if (dotIndex != -1) {
			precioStr = precioStr.substring(0, dotIndex + 1) + precioStr.substring(dotIndex + 1).replaceAll("\\.", "");
		}

		// Verificar si el precio contiene solo un punto decimal y ningún otro número
		if (precioStr.equals(".") || precioStr.equals(".0")) {
			return "0";
		}

		// Si el precio después de limpiar es vacío, retornar "0"
		if (precioStr.isEmpty()) {
			return "0";
		}

		// Retornar el precio limpiado con el símbolo monetario
		return symbol + precioStr;
	}

	public static String comprobarGradeo(String valorGradeo) {
		String[] valores = { "NM (Noir Medium)", "SM (Standard Medium)", "LM (Light Medium)", "FL (Fine Light)",
				"VF (Very Fine)" };
		for (String gradeo : valores) {
			if (valorGradeo.equalsIgnoreCase(gradeo)) {
				return valorGradeo;
			}
		}
		return "NM (Noir Medium)";
	}

}
