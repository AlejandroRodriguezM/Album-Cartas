/**
 * Contiene las clases que hacen funcionar las diferentes funciones de uso de back end y front de todo el proyecto.
 *  
*/
package cartaManagement;

import java.util.Objects;

import dbmanager.CartaManagerDAO;
import dbmanager.ListasCartasDAO;
import funcionesAuxiliares.Utilidades;

/**
 * Esta clase objeto sirve para dar forma al comic que estara dentro de la base
 * de datos.
 *
 * @author Alejandro Rodriguez
 */
public class Carta implements Cloneable {
	/**
	 * Identificador único de la carta.
	 */
	protected String idCarta;

	/**
	 * Nombre de la carta.
	 */
	protected String nomCarta;

	/**
	 * Número de la carta.
	 */
	protected String numCarta;

	/**
	 * Editorial de la carta.
	 */
	protected String editorialCarta;

	/**
	 * Coleccion de la carta.
	 */
	protected String coleccionCarta;

	/**
	 * Rareza de la carta.
	 */
	protected String rarezaCarta;

	/**
	 * Precio de la carta.
	 */
	protected String precioCartaNormal;

	/**
	 * Precio de la carta.
	 */
	protected String precioCartaFoil;

	/**
	 * URL de referencia relacionada con la carta.
	 */
	protected String urlReferenciaCarta;

	/**
	 * Dirección de la imagen de la carta.
	 */
	protected String direccionImagenCarta;

	protected String normasCarta;

	/**
	 * Constructor para crear un objeto Carta con todos los atributos.
	 */
	private Carta(CartaBuilder builder) {
		this.idCarta = builder.idCarta;
		this.nomCarta = builder.nomCarta;
		this.numCarta = builder.numCarta;
		this.editorialCarta = builder.editorialCarta;
		this.coleccionCarta = builder.coleccionCarta;
		this.rarezaCarta = builder.rarezaCarta;
		this.precioCartaNormal = builder.precioCartaNormal;
		this.precioCartaFoil = builder.precioCartaFoil;
		this.urlReferenciaCarta = builder.urlReferenciaCarta;
		this.direccionImagenCarta = builder.direccionImagenCarta;
		this.normasCarta = builder.normasCarta;
	}

	/**
	 * Constructor vacío para crear un objeto Carta sin atributos inicializados.
	 */
	public Carta() {
		this.idCarta = "";
		this.nomCarta = "";
		this.numCarta = "0";
		this.editorialCarta = "";
		this.coleccionCarta = "";
		this.rarezaCarta = "";
		this.precioCartaNormal = "";
		this.precioCartaFoil = "";
		this.urlReferenciaCarta = "";
		this.direccionImagenCarta = "";
		this.normasCarta = "";
	}

	public static class CartaBuilder {
		private String idCarta;
		private String nomCarta;
		private String numCarta;
		private String editorialCarta;
		private String coleccionCarta;
		private String rarezaCarta;
		private String precioCartaFoil;
		private String precioCartaNormal;
		private String urlReferenciaCarta;
		private String direccionImagenCarta;
		private String normasCarta;

		public CartaBuilder(String idCarta, String nomCarta) {
			this.idCarta = idCarta;
			this.nomCarta = nomCarta;
		}

		public CartaBuilder numCarta(String numCarta) {
			this.numCarta = numCarta;
			return this;
		}

		public CartaBuilder editorialCarta(String editorialCarta) {
			this.editorialCarta = editorialCarta;
			return this;
		}

		public CartaBuilder coleccionCarta(String coleccionCarta) {
			this.coleccionCarta = coleccionCarta;
			return this;
		}

		public CartaBuilder rarezaCarta(String rarezaCarta) {
			this.rarezaCarta = rarezaCarta;
			return this;
		}

		public CartaBuilder precioCartaNormal(String precioCartaNormal) {
			this.precioCartaNormal = precioCartaNormal;
			return this;
		}

		public CartaBuilder precioCartaFoil(String precioCartaFoil) {
			this.precioCartaFoil = precioCartaFoil;
			return this;
		}

		public CartaBuilder urlReferenciaCarta(String urlReferenciaCarta) {
			this.urlReferenciaCarta = urlReferenciaCarta;
			return this;
		}

		public CartaBuilder direccionImagenCarta(String direccionImagenCarta) {
			this.direccionImagenCarta = direccionImagenCarta;
			return this;
		}

		public CartaBuilder normasCarta(String normasCarta) {
			this.normasCarta = normasCarta;
			return this;
		}

		public Carta build() {
			return new Carta(this);
		}
	}

	// Métodos getters para todos los atributos
	public String getIdCarta() {
		return idCarta;
	}

	public String getNomCarta() {
		return nomCarta;
	}

	public String getNumCarta() {
		return numCarta;
	}

	public String getEditorialCarta() {
		return editorialCarta;
	}

	public String getColeccionCarta() {
		return coleccionCarta;
	}

	public String getRarezaCarta() {
		return rarezaCarta;
	}

	public String getPrecioCartaNormal() {
		return precioCartaNormal;
	}

	public String getPrecioCartaFoil() {
		return precioCartaFoil;
	}

	public String getUrlReferenciaCarta() {
		return urlReferenciaCarta;
	}

	public String getDireccionImagenCarta() {
		return direccionImagenCarta;
	}

	public String getNormasCarta() {
		return normasCarta;
	}

	public void setNormasCarta(String normasCarta) {
		this.normasCarta = normasCarta;
	}

	public void setIdCarta(String idCarta) {
		this.idCarta = idCarta;
	}

	public void setNomCarta(String nomCarta) {
		this.nomCarta = nomCarta;
	}

	public void setNumCarta(String numCarta) {
		this.numCarta = numCarta;
	}

	public void setEditorialCarta(String editorialCarta) {
		this.editorialCarta = editorialCarta;
	}

	public void setColeccionCarta(String coleccionCarta) {
		this.coleccionCarta = coleccionCarta;
	}

	public void setRarezaCarta(String rarezaCarta) {
		this.rarezaCarta = rarezaCarta;
	}

	public void setPrecioCartaNormal(String precioCartaNormal) {
		this.precioCartaNormal = precioCartaNormal;
	}

	public void setPrecioCartaFoil(String precioCartaFoil) {
		this.precioCartaFoil = precioCartaFoil;
	}

	public void setUrlReferenciaCarta(String urlReferenciaCarta) {
		this.urlReferenciaCarta = urlReferenciaCarta;
	}

	public void setDireccionImagenCarta(String direccionImagenCarta) {
		this.direccionImagenCarta = direccionImagenCarta;
	}

	public static Carta obtenerCarta(String idCarta) {
		boolean existeComic = CartaManagerDAO.comprobarIdentificadorCarta(idCarta);
		if (!existeComic) {
			existeComic = ListasCartasDAO.verificarIDExistente(idCarta);
			if (existeComic) {
				return ListasCartasDAO.devolverCartaLista(idCarta);
			}
		} else {
			return CartaManagerDAO.cartaDatos(idCarta);
		}
		return null;
	}

	/**
	 * Verifica si todos los campos del objeto Carta están vacíos o nulos.
	 * 
	 * @return true si todos los campos están vacíos o nulos, false de lo contrario.
	 */
	public boolean estaVacio() {
		return isNullOrEmpty(this.nomCarta) && this.numCarta.equals("0") && isNullOrEmpty(this.coleccionCarta)
				&& isNullOrEmpty(this.rarezaCarta) && this.precioCartaNormal.equals("0.0")
				&& this.precioCartaFoil.equals("0.0") && isNullOrEmpty(this.urlReferenciaCarta)
				&& isNullOrEmpty(this.direccionImagenCarta) && isNullOrEmpty(this.editorialCarta);
	}

	private boolean isNullOrEmpty(String str) {
		return str == null || str.isEmpty();
	}

	public static void limpiarCamposCarta(Carta carta) {
		// Limpiar campo nombre
		carta.setNomCarta(limpiarCampo(carta.getNomCarta()));

		// Limpiar campo coleccionCarta
		carta.setColeccionCarta(limpiarCampo(carta.getColeccionCarta()));

		// Limpiar campo rarezaCarta
		carta.setRarezaCarta(limpiarCampo(carta.getRarezaCarta()));

		carta.setEditorialCarta(limpiarCampo(carta.getEditorialCarta()));
	}

	public static String limpiarCampo(String campo) {
		if (campo != null) {
			// Eliminar comas repetidas y otros símbolos al inicio y al final del campo
			campo = campo.replaceAll("^\\s*[,\\s-]+", ""); // Al principio
			campo = campo.replaceAll("[,\\s-]+\\s*$", ""); // Al final
			campo = campo.replaceAll(",\\s*,", ","); // Comas repetidas
			campo = campo.replaceAll(",\\s*", " - "); // Reemplazar ", " por " - "
			campo = campo.replace("'", " "); // Reemplazar ", " por " - "
		} else {
			return "";
		}
		return campo;
	}

	public static boolean validarCarta(Carta carta) {
		// Verificar si al menos un campo tiene datos utilizando los campos del objeto
		// Carta pasado como parámetro
		if (carta.getNomCarta() != null && !carta.getNomCarta().isEmpty()) {
			return true;
		}
		if (!carta.getNumCarta().equals("0")) {
			return true;
		}
		if (carta.getColeccionCarta() != null && !carta.getColeccionCarta().isEmpty()) {
			return true;
		}
		if (carta.getRarezaCarta() != null && !carta.getRarezaCarta().isEmpty()) {
			return true;
		}
		if (carta.getEditorialCarta() != null && !carta.getEditorialCarta().isEmpty()) {
			return true;
		}
		// Si ninguno de los campos tiene datos, devuelve false
		return false;
	}

	/**
	 * Genera una representación en forma de cadena de texto del cómic, incluyendo
	 * sus atributos no nulos.
	 *
	 * @return Una cadena de texto que representa el cómic.
	 */
	public String devolverNormas() {
		StringBuilder contenidoCarta = new StringBuilder();

		if (!normasCarta.equalsIgnoreCase("Vacio")) {
			Utilidades.appendIfNotEmpty(contenidoCarta, "Normas", normasCarta);
			return contenidoCarta.toString();
		}
		return "";

	}

	/**
	 * Genera una representación en forma de cadena de texto de la carta, incluyendo
	 * sus atributos no nulos.
	 *
	 * @return Una cadena de texto que representa la carta.
	 */
	@Override
	public String toString() {
		StringBuilder contenidoCarta = new StringBuilder();

		Utilidades.appendIfNotEmpty(contenidoCarta, "Nombre", nomCarta);
		Utilidades.appendIfNotEmpty(contenidoCarta, "Número", String.valueOf(numCarta));
		Utilidades.appendIfNotEmpty(contenidoCarta, "Editorial", String.valueOf(editorialCarta));
		Utilidades.appendIfNotEmpty(contenidoCarta, "Colección", coleccionCarta);
		Utilidades.appendIfNotEmpty(contenidoCarta, "Rareza", rarezaCarta);
		Utilidades.appendIfNotEmpty(contenidoCarta, "Precio Normal", String.valueOf(precioCartaNormal));
		Utilidades.appendIfNotEmpty(contenidoCarta, "Precio Foil", String.valueOf(precioCartaFoil));
		Utilidades.appendIfNotEmpty(contenidoCarta, "URL Referencia", urlReferenciaCarta);
		Utilidades.appendIfNotEmpty(contenidoCarta, "Dirección Imagen", direccionImagenCarta);

		return contenidoCarta.toString();
	}

	/**
	 * Genera una representación en forma de cadena de texto del cómic, incluyendo
	 * sus atributos no nulos.
	 *
	 * @return Una cadena de texto que representa el cómic.
	 */
	public String infoCarta() {

		StringBuilder contenidoCarta = new StringBuilder();

		Utilidades.appendIfNotEmpty(contenidoCarta, "Identificador", idCarta);
		Utilidades.appendIfNotEmpty(contenidoCarta, "Nombre", nomCarta);
		Utilidades.appendIfNotEmpty(contenidoCarta, "Número", String.valueOf(numCarta));
		Utilidades.appendIfNotEmpty(contenidoCarta, "Editorial", editorialCarta);
		Utilidades.appendIfNotEmpty(contenidoCarta, "Colección", coleccionCarta);
		Utilidades.appendIfNotEmpty(contenidoCarta, "Rareza", rarezaCarta);
		Utilidades.appendIfNotEmpty(contenidoCarta, "Precio Normal", String.valueOf(precioCartaNormal));
		Utilidades.appendIfNotEmpty(contenidoCarta, "Precio Foil", String.valueOf(precioCartaFoil));
		Utilidades.appendIfNotEmpty(contenidoCarta, "URL Referencia", urlReferenciaCarta);
		Utilidades.appendIfNotEmpty(contenidoCarta, "Dirección Imagen", direccionImagenCarta);
		Utilidades.appendIfNotEmpty(contenidoCarta, "Normas", normasCarta);

		return contenidoCarta.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Carta carta = (Carta) o;
		return Objects.equals(getPrecioCartaNormal(), carta.getPrecioCartaNormal())
				&& Objects.equals(getPrecioCartaFoil(), carta.getPrecioCartaFoil())
				&& Objects.equals(getNomCarta(), carta.getNomCarta())
				&& Objects.equals(getNumCarta(), carta.getNumCarta())
				&& Objects.equals(getColeccionCarta(), carta.getColeccionCarta());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getPrecioCartaNormal(), getPrecioCartaFoil(), getNomCarta(), getNumCarta(),
				getColeccionCarta());
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone(); // Realiza una copia superficial
	}

}
