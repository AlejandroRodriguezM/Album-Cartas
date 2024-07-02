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
			String precioCartaNormal = data[6];
			String precioCartaFoil = data[7];
			String urlReferenciaCarta = data[8];
			String direccionImagenCarta = data[9];
			String normasCarta = data[10];
			String nombrePortada = Utilidades.obtenerNombrePortada(false, direccionImagenCarta);
			String imagen = FuncionesExcel.DEFAULT_PORTADA_IMAGE_PATH + File.separator + nombrePortada;

			// Verificaciones y asignaciones predeterminadas
			precioCartaNormal = (Double.parseDouble(precioCartaNormal) <= 0) ? "0.0" : precioCartaNormal;
			precioCartaFoil = (Double.parseDouble(precioCartaFoil) <= 0) ? "0.0" : precioCartaFoil;
			urlReferenciaCarta = (urlReferenciaCarta.isEmpty()) ? "Sin referencia" : urlReferenciaCarta;

			return new Carta.CartaBuilder("", nombre).numCarta(numCarta).editorialCarta(editorialCarta)
					.coleccionCarta(coleccionCarta).rarezaCarta(rarezaCarta).precioCartaNormal(precioCartaNormal)
					.precioCartaFoil(precioCartaFoil).urlReferenciaCarta(urlReferenciaCarta).direccionImagenCarta(imagen)
					.normasCarta(normasCarta).build();
		} else {
			return null;
		}
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
