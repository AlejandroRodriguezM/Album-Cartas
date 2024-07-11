package funcionesManagment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import Controladores.CargaCartasController;
import alarmas.AlarmaList;
import cartaManagement.Carta;
import dbmanager.CartaManagerDAO;
import dbmanager.DatabaseManagerDAO;
import dbmanager.ListasCartasDAO;
import dbmanager.UpdateManager;
import ficherosFunciones.FuncionesFicheros;
import funcionesAuxiliares.Utilidades;
import funcionesAuxiliares.Ventanas;
import funcionesInterfaz.AccionControlUI;
import funcionesInterfaz.FuncionesComboBox;
import funcionesInterfaz.FuncionesManejoFront;
import funcionesInterfaz.FuncionesTableView;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import webScrap.FuncionesScrapeoComunes;
import webScrap.WebScrapGoogleCardMarket;
import webScrap.WebScrapGoogleScryfall;
import webScrap.WebScrapGoogleTCGPlayer;

public class AccionFuncionesComunes {

	private static File fichero;
	private static AtomicInteger contadorErrores;
	private static AtomicInteger cartasProcesados;
	private static AtomicInteger mensajeIdCounter;
	private static AtomicInteger numLineas;
	private static AtomicReference<CargaCartasController> cargaCartasControllerRef;
	private static StringBuilder codigoFaltante;
	private static HashSet<String> mensajesUnicos = new HashSet<>();

	/**
	 * Obtenemos el directorio de inicio del usuario
	 */
	private static final String USER_DIR = System.getProperty("user.home");

	/**
	 * Construimos la ruta al directorio "Documents"
	 */
	private static final String DOCUMENTS_PATH = USER_DIR + File.separator + "Documents";

	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();

	/**
	 * Instancia de la clase FuncionesComboBox para el manejo de ComboBox.
	 */
	private static FuncionesComboBox funcionesCombo = new FuncionesComboBox();

	public static String TIPO_ACCION = getTipoAccion();

	private static AccionReferencias referenciaVentana = getReferenciaVentana();

	private static AccionReferencias referenciaVentanaPrincipal = getReferenciaVentanaPrincipal();

	private static AccionControlUI accionRellenoDatos = new AccionControlUI();

	/**
	 * Procesa la información de un cómic, ya sea para realizar una modificación o
	 * una inserción en la base de datos.
	 *
	 * @param comic          El cómic con la información a procesar.
	 * @param esModificacion Indica si se está realizando una modificación (true) o
	 *                       una inserción (false).
	 * @throws Exception
	 */
	public void procesarCarta(Carta comic, boolean esModificacion) {
		getReferenciaVentana().getProntInfoTextArea().setOpacity(1);

		if (!accionRellenoDatos.camposCartaSonValidos()) {
			mostrarErrorDatosIncorrectos();
			return;
		}

		String codigoImagen = Utilidades.generarCodigoUnico(carpetaPortadas(Utilidades.nombreDB()) + File.separator);
		String mensaje = "";

		try {
			Utilidades.redimensionarYGuardarImagen(comic.getDireccionImagenCarta(), codigoImagen);
			comic.setDireccionImagenCarta(
					carpetaPortadas(Utilidades.nombreDB()) + File.separator + codigoImagen + ".jpg");

			if (esModificacion) {
				CartaManagerDAO.actualizarCartaBBDD(comic, "modificar");
				mensaje = "Has modificado correctamente el cómic";
			} else {
				procesarNuevaCarta(comic);
				mensaje = "Has introducido correctamente el cómic";
			}

			AlarmaList.mostrarMensajePront(mensaje, esModificacion, getReferenciaVentana().getProntInfoTextArea());
			procesarBloqueComun(comic);

		} catch (IOException | SQLException e) {
			Utilidades.manejarExcepcion(e);
		}
	}

	private void mostrarErrorDatosIncorrectos() {
		String mensaje = "Error. Debes de introducir los datos correctos";
		AlarmaList.mostrarMensajePront(mensaje, false, getReferenciaVentana().getProntInfoTextArea());
		List<Carta> cartasFinal = ListasCartasDAO.cartasImportados;
		Platform.runLater(() -> FuncionesTableView.tablaBBDD(cartasFinal));
	}

	private void procesarNuevaCarta(Carta comic) {
		CartaManagerDAO.insertarDatos(comic, true);
		Carta newSelection = getReferenciaVentana().getTablaBBDD().getSelectionModel().getSelectedItem();
		List<Carta> cartasFinal;

		if (newSelection != null) {
			String idCarta = newSelection.getIdCarta();
			ListasCartasDAO.cartasImportados.removeIf(c -> c.getIdCarta().equals(idCarta));
			getReferenciaVentana().getTablaBBDD().getItems().clear();
			cartasFinal = ListasCartasDAO.cartasImportados;
		} else {
			cartasFinal = null; // Inicializar cartasFinal en caso de que no haya ningún cómic seleccionado
		}

		Platform.runLater(() -> FuncionesTableView.tablaBBDD(cartasFinal));
		getReferenciaVentana().getTablaBBDD().refresh();
	}

	/**
	 * Procesa el bloque común utilizado en la función procesarCarta para actualizar
	 * la interfaz gráfica y realizar operaciones relacionadas con la manipulación
	 * de imágenes y la actualización de listas y combos.
	 *
	 * @param comic El objeto Carta que se está procesando.
	 * @throws SQLException Si ocurre un error al interactuar con la base de datos.
	 */
	private void procesarBloqueComun(Carta comic) {
		File file = new File(comic.getDireccionImagenCarta());
		Image imagen = new Image(file.toURI().toString(), 250, 0, true, true);
		getReferenciaVentana().getImagenCarta().setImage(imagen);

		ListasCartasDAO.listasAutoCompletado();
		FuncionesTableView.nombreColumnas();
		FuncionesTableView.actualizarBusquedaRaw();
	}

	public static boolean procesarCartaPorCodigo(Carta cartaInfo, boolean esClonar) {

		boolean alMenosUnoProcesado = false;

		if (comprobarCodigo(cartaInfo)) {
			rellenarTablaImport(cartaInfo, esClonar);
			alMenosUnoProcesado = true;
		}

		return alMenosUnoProcesado;
	}

	public static void actualizarPortadaCartas(String codigoImagen, String correctedUrl) {

		// Asynchronously download and convert image
		Platform.runLater(() -> {
			URI uri = null;
			try {
				uri = new URI(correctedUrl);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			Utilidades.descargarYConvertirImagenAsync(uri, carpetaPortadas(Utilidades.nombreDB()),
					codigoImagen + ".jpg");
		});
	}

	public static void actualizarPrecioCartasNormal(Carta cartaOriginal, String nuevoPrecio) {

		cartaOriginal.setPrecioCartaNormal(nuevoPrecio);
	}

	public static void actualizarPrecioCartasFoil(Carta cartaOriginal, String nuevoPrecio) {

		cartaOriginal.setPrecioCartaNormal(nuevoPrecio);
	}

	public static void actualizarInformacionCartas(Carta cartaOriginal) {

		String numCarta = cartaOriginal.getNumCarta();
		String nombreCorregido = Utilidades.eliminarParentesis(cartaOriginal.getNomCarta());
		String nombreLimpio = Utilidades.extraerNombreLimpio(nombreCorregido);
		nombreLimpio = DatabaseManagerDAO.corregirPatrones(nombreLimpio);
		String editorial = DatabaseManagerDAO.getComercializadora((cartaOriginal.getEditorialCarta()));

		cartaOriginal.setNomCarta(nombreLimpio);
		cartaOriginal.setNumCarta(numCarta);
		cartaOriginal.setEditorialCarta(editorial);
		cartaOriginal.setDireccionImagenCarta(cartaOriginal.getDireccionImagenCarta());

	}

	public static void actualizarCompletoCartas(Carta cartaOriginal) {

		String numCarta = cartaOriginal.getNumCarta();
		String nombreCorregido = Utilidades.eliminarParentesis(cartaOriginal.getNomCarta());
		String nombreLimpio = Utilidades.extraerNombreLimpio(nombreCorregido);
		nombreLimpio = DatabaseManagerDAO.corregirPatrones(nombreLimpio);
		String editorial = DatabaseManagerDAO.getComercializadora((cartaOriginal.getEditorialCarta()));

		cartaOriginal.setNomCarta(nombreLimpio);
		cartaOriginal.setNumCarta(numCarta);
		cartaOriginal.setEditorialCarta(editorial);

	}

	public static String codigoNuevaImagen() {
		return Utilidades.generarCodigoUnico(carpetaPortadas(Utilidades.nombreDB()) + File.separator);
	}

	public static String urlFinalImagen(String direccionImagen) {

		String urlImagen = direccionImagen;

		return urlImagen.replace("\\", "/").replaceFirst("^http:", "https:");
	}

	public static void actualizarCartasDatabase(Carta cartaOriginal, String tipoUpdate) {

		String codigoCarta = cartaOriginal.getUrlReferenciaCarta();

		String tipoTienda = determinarTipoTienda(codigoCarta);

		List<Carta> cartaColeccion = obtenerCartaInfo(codigoCarta, true, tipoTienda);

		for (Carta cartaInfo : cartaColeccion) {
			cartaInfo.setIdCarta(cartaOriginal.getIdCarta());

			if (tipoUpdate.equalsIgnoreCase("actualizar datos")) {
				cartaInfo.setDireccionImagenCarta(cartaOriginal.getDireccionImagenCarta());
				cartaInfo.setUrlReferenciaCarta(cartaOriginal.getUrlReferenciaCarta());
			} else if (tipoUpdate.equalsIgnoreCase("actualizar precio")) {
				cartaInfo.setNomCarta(cartaOriginal.getNomCarta());
				cartaInfo.setNumCarta(cartaOriginal.getNumCarta());
				cartaInfo.setEditorialCarta(cartaOriginal.getEditorialCarta());
				cartaInfo.setColeccionCarta(cartaOriginal.getColeccionCarta());
				cartaInfo.setRarezaCarta(cartaOriginal.getRarezaCarta());
				cartaInfo.setUrlReferenciaCarta(cartaOriginal.getUrlReferenciaCarta());
				cartaInfo.setNormasCarta(cartaOriginal.getNormasCarta());
				cartaInfo.setDireccionImagenCarta(cartaOriginal.getDireccionImagenCarta());
			} else {
				cartaInfo.setUrlReferenciaCarta(cartaOriginal.getUrlReferenciaCarta());
				AccionFuncionesComunes.actualizarValorPortadaCarta(cartaInfo, tipoTienda);
			}
			UpdateManager.actualizarCartaBBDD(cartaInfo, "modificar");
		}
	}

	public static String determinarTipoTienda(String url) {
		if (url.contains("cardmarket.com")) {
			return "Card Market";
		} else if (url.contains("scryfall.com")) {
			return "ScryFall";
		} else if (url.contains("tcgplayer.com")) {
			return "TCGPlayer";
		} else {
			return "Desconocido";
		}
	}

	public static Carta copiarCartaClon(Carta cartaOriginal) {
		try {
			// Clonar la carta original
			Carta cartaClon = (Carta) cartaOriginal.clone();

			// Obtener la dirección actual de la imagen
			String direccionActual = cartaOriginal.getDireccionImagenCarta();

			// Verificar si la dirección actual es válida
			if (direccionActual != null && !direccionActual.isEmpty()) {
				File imagenActual = new File(direccionActual);

				// Verificar si el archivo existe antes de proceder
				if (imagenActual.exists()) {
					// Generar un nuevo nombre de archivo y ruta destino
					String codigoNuevoCarta = codigoNuevaImagen();
					String nombreArchivoNuevo = codigoNuevoCarta + ".jpg";
					String carpetaPortadas = carpetaPortadas(Utilidades.nombreDB());
					String urlFinal = carpetaPortadas + File.separator + nombreArchivoNuevo;

					// Crear la ruta destino para el archivo nuevo
					Path rutaDestino = new File(urlFinal).toPath();

					// Copiar y renombrar el archivo de imagen
					Files.copy(imagenActual.toPath(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);

					// Actualizar la dirección de la imagen en la carta clonada
					cartaClon.setDireccionImagenCarta(urlFinal);

					// Devolver la carta clonada con la nueva dirección de imagen
					return cartaClon;
				}
			}

			// Si la dirección actual no es válida o el archivo no existe, devolver la carta
			// original
			return cartaOriginal;
		} catch (CloneNotSupportedException | IOException e) {
			// Manejar la excepción de clonación o de operaciones de archivos
			e.printStackTrace();
			return null;
		}
	}

	public static void actualizarCartasPortadas(Carta cartaOriginal, String tipoTienda) {

		actualizarValorPortadaCarta(cartaOriginal, tipoTienda);
		UpdateManager.actualizarCartaBBDD(cartaOriginal, "portada");
	}

	public static void actualizarValorPortadaCarta(Carta cartaOriginal, String tipoTienda) {
		String codigoNuevoCarta = codigoNuevaImagen();
		String urlFinal = carpetaPortadas(Utilidades.nombreDB()) + File.separator + codigoNuevoCarta + ".jpg";
		String imagen = "";
		if (tipoTienda.equalsIgnoreCase("CardMarket")) {
			imagen = imagenCarta(cartaOriginal);

		} else if (tipoTienda.equalsIgnoreCase("scryfall")) {
			String scriptPath = FuncionesFicheros.rutaDestinoRecursos + File.separator + "scrapImagenScryFall.js";
			String urlReferencia = cartaOriginal.getUrlReferenciaCarta();
			imagen = FuncionesScrapeoComunes.getImagenFromPuppeteer(urlReferencia, scriptPath);
		} else if (tipoTienda.equalsIgnoreCase("tcgplayer")) {
			String scriptPath = FuncionesFicheros.rutaDestinoRecursos + File.separator + "scrapImagenTCG.js";
			String urlReferencia = cartaOriginal.getUrlReferenciaCarta();

			imagen = FuncionesScrapeoComunes.getImagenFromPuppeteer(urlReferencia, scriptPath);
		}
		actualizarPortadaCartas(codigoNuevoCarta, imagen);
		cartaOriginal.setDireccionImagenCarta(urlFinal);
	}

	public static String imagenCarta(Carta cartaOriginal) {
		String argument = "cardtrader+" + cartaOriginal.getNomCarta().replace(" ", "+") + "+"
				+ cartaOriginal.getNumCarta() + "+" + cartaOriginal.getColeccionCarta().replace(" ", "+");
		String urlCarta = WebScrapGoogleCardMarket.searchWebImagen(argument);
		if (urlCarta.contains("/cards/")) {
			return WebScrapGoogleCardMarket.extraerDatosImagen(urlCarta);
		}
		return "";
	}

	/**
	 * Funcion que escribe en el TextField de "Direccion de imagen" la dirrecion de
	 * la imagen
	 */
	public static void subirPortada() {

		String frase = "Fichero JPG";

		String formato = "*.jpg";

		File fichero = Utilidades.tratarFichero(frase, formato, false);

		// Verificar si se obtuvo un objeto FileChooser válido
		if (fichero != null) {
			String nuevoNombreArchivo = Utilidades.generarCodigoUnico(carpetaRaizPortadas(Utilidades.nombreDB()));

			try {
				Utilidades.redimensionarYGuardarImagen(fichero.getAbsolutePath(), nuevoNombreArchivo);
			} catch (IOException e) {
				e.printStackTrace();
			}

			String portada = carpetaRaizPortadas(Utilidades.nombreDB()) + "portadas" + File.separator
					+ nuevoNombreArchivo + ".jpg";

			getReferenciaVentana().getDireccionImagenTextField().setText(portada);

			String mensaje = "Portada subida correctamente.";

			AlarmaList.mostrarMensajePront(mensaje, true, getReferenciaVentana().getProntInfoTextArea());

			Utilidades.cargarImagenAsync(portada, getReferenciaVentana().getImagenCarta());

		} else {
			String mensaje = "Has cancelado la subida de portada.";

			AlarmaList.mostrarMensajePront(mensaje, false, getReferenciaVentana().getProntInfoTextArea());
		}
	}

	/**
	 * Limpia y restablece todos los campos de datos en la sección de animaciones a
	 * sus valores predeterminados. Además, restablece la imagen de fondo y oculta
	 * cualquier mensaje de error o información.
	 */
	public static void limpiarDatosPantallaAccion() {
		// Restablecer los campos de datos

		if (!ListasCartasDAO.cartasImportados.isEmpty() && nav.alertaBorradoLista()) {
			getReferenciaVentana().getBotonGuardarCarta().setVisible(false);
			getReferenciaVentana().getBotonEliminarImportadoCarta().setVisible(false);

			ListasCartasDAO.cartasImportados.clear();
			getReferenciaVentana().getTablaBBDD().getItems().clear();
			getReferenciaVentana().getTablaBBDD().refresh();
		}
		getReferenciaVentana().getBotonClonarCarta().setVisible(false);
		getReferenciaVentana().getProntInfoTextArea().setOpacity(0);
		getReferenciaVentana().getTablaBBDD().refresh();
		getReferenciaVentana().getBotonEliminarImportadoListaCarta().setVisible(false);
		getReferenciaVentana().getBotonGuardarListaCartas().setVisible(false);

		getReferenciaVentana().getBotonEliminarImportadoListaCarta().setDisable(true);
		getReferenciaVentana().getBotonGuardarListaCartas().setDisable(true);
		limpiarDatosCarta();
	}

	public static void limpiarDatosCarta() {
		getReferenciaVentana().getNombreCartaTextField().setText("");
		getReferenciaVentana().getEditorialCartaTextField().setText("");
		getReferenciaVentana().getColeccionCartaTextField().setText("");
		getReferenciaVentana().getRarezaCartaTextField().setText("");
		getReferenciaVentana().getNormasCartaTextArea().setText("");
		getReferenciaVentana().getPrecioCartaNormalTextField().setText("");
		getReferenciaVentana().getPrecioCartaFoilTextField().setText("");
		getReferenciaVentana().getIdCartaTratarTextField().setText("");
		getReferenciaVentana().getDireccionImagenTextField().setText("");
		getReferenciaVentana().getBusquedaCodigoTextField().setText("");
		getReferenciaVentana().getUrlReferenciaTextField().setText("");
		getReferenciaVentana().getNumeroCartaCombobox().getEditor().clear(); // Limpiar el texto en el ComboBox
		getReferenciaVentana().getImagenCarta().setImage(null);

		if ("modificar".equals(TIPO_ACCION)) {
			AccionControlUI.mostrarOpcion(TIPO_ACCION);
		}

		if ("aniadir".equals(TIPO_ACCION)) {

			if (getReferenciaVentana().getNombreTiendaCombobox().isVisible()) {
				AccionFuncionesComunes.cambiarVisibilidadAvanzada();
			}

		}

		// Borrar cualquier mensaje de error presente
		borrarErrores();
		AccionControlUI.validarCamposClave(true);
		AccionControlUI.borrarDatosGraficos();
	}

	/**
	 * Comprueba la existencia de un cómic en la base de datos y realiza acciones
	 * dependiendo del resultado.
	 *
	 * @param ID El identificador del cómic a verificar.
	 * @return true si el cómic existe en la base de datos y se realizan las
	 *         acciones correspondientes, false de lo contrario.
	 * @throws SQLException Si ocurre un error al interactuar con la base de datos.
	 */
	public boolean comprobarExistenciaCarta(String idCarta) {

		// Si el cómic existe en la base de datos
		if (CartaManagerDAO.comprobarIdentificadorCarta(idCarta)) {
			FuncionesTableView.actualizarBusquedaRaw();
			return true;
		} else { // Si el cómic no existe en la base de datos
			String mensaje = "ERROR. ID desconocido.";
			AlarmaList.mostrarMensajePront(mensaje, false, getReferenciaVentana().getProntInfoTextArea());
			return false;
		}
	}

	/**
	 * Verifica si se ha encontrado información válida para el cómic.
	 *
	 * @param comicInfo Un objeto Carta con la información del cómic.
	 * @return True si la información es válida y existe; de lo contrario, False.
	 */
	private static boolean comprobarCodigo(Carta comicInfo) {
		return comicInfo != null;
	}

	/**
	 * Rellena los campos de la interfaz gráfica con la información del cómic
	 * proporcionada.
	 *
	 * @param comicInfo Un arreglo de strings con información del cómic.
	 * @throws IOException
	 */
	private static void rellenarTablaImport(Carta comic, boolean esClonar) {
		Platform.runLater(() -> {

			String numCartaStr = comic.getNumCarta();
			String nombreCorregido = Utilidades.eliminarParentesis(comic.getNomCarta());
			// Variables relacionadas con la importación de cómics
			String id = "A" + 0 + "" + (ListasCartasDAO.cartasImportados.size() + 1);
			String titulo = Utilidades.defaultIfNullOrEmpty(DatabaseManagerDAO.corregirPatrones(nombreCorregido),
					"Vacio");

			String editorial = Utilidades.defaultIfNullOrEmpty(comic.getEditorialCarta(), "0");
			String numero = Utilidades.defaultIfNullOrEmpty(numCartaStr, "0");
			String coleccion = Utilidades
					.defaultIfNullOrEmpty(DatabaseManagerDAO.corregirNombre(comic.getColeccionCarta()), "Vacio");
			String rareza = Utilidades.defaultIfNullOrEmpty(DatabaseManagerDAO.corregirNombre(comic.getRarezaCarta()),
					"Vacio");
			String normas = Utilidades.defaultIfNullOrEmpty(DatabaseManagerDAO.corregirNombre(comic.getNormasCarta()),
					"Vacio");
			String precioNormal = Utilidades.defaultIfNullOrEmpty(comic.getPrecioCartaNormal(), "0");
			String precioFoil = Utilidades.defaultIfNullOrEmpty(comic.getPrecioCartaFoil(), "0");
			String urlReferencia = Utilidades.defaultIfNullOrEmpty(comic.getUrlReferenciaCarta(), "Vacio");
			// Variables relacionadas con la imagen del cómic
			String imagen = "";
			if (!esClonar) {
				String urlImagen = comic.getDireccionImagenCarta();

				File file = new File(urlImagen);
				// Manejo de la ruta de la imagen
				if (urlImagen == null || urlImagen.isEmpty()) {
					String rutaImagen = "/imagenes/sinPortada.jpg";
					URL url = Utilidades.class.getClass().getResource(rutaImagen);
					if (url != null) {
						urlImagen = url.toExternalForm();
					}
				} else {
					file = new File(urlImagen);
					urlImagen = file.toString();
				}

				// Corrección y generación de la URL final de la imagen
				String correctedUrl = urlImagen.replace("\\", "/").replace("http:", "https:").replace("https:",
						"https:/");
				String codigoImagen = Utilidades
						.generarCodigoUnico(carpetaPortadas(Utilidades.nombreDB()) + File.separator);
				URI uri = null;
				try {
					uri = new URI(correctedUrl);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}

				imagen = carpetaPortadas(Utilidades.nombreDB()) + File.separator + codigoImagen + ".jpg";
				// Descarga y conversión asíncrona de la imagen
				Utilidades.descargarYConvertirImagenAsync(uri, carpetaPortadas(Utilidades.nombreDB()),
						codigoImagen + ".jpg");
			} else {
				imagen = comic.getDireccionImagenCarta();
			}

			Carta comicImport = new Carta.CartaBuilder(id, titulo).numCarta(numero).editorialCarta(editorial)
					.coleccionCarta(coleccion).rarezaCarta(rareza).precioCartaNormal(precioNormal)
					.precioCartaFoil(precioFoil).urlReferenciaCarta(urlReferencia).direccionImagenCarta(imagen)
					.normasCarta(normas).build();

			ListasCartasDAO.cartasImportados.add(comicImport);
			FuncionesTableView.nombreColumnas();
			FuncionesTableView.tablaBBDD(ListasCartasDAO.cartasImportados);
		});
	}

	public static List<Carta> obtenerCartaInfo(String finalValorCodigo, boolean esImport, String tipoTienda) {
		try {

			List<Carta> cartaInfo = new ArrayList<>();
			if (esImport) {

				if (tipoTienda.equalsIgnoreCase("CardMarket")) {
					cartaInfo.add(WebScrapGoogleCardMarket.extraerDatosMTG(finalValorCodigo));
				} else if (tipoTienda.equalsIgnoreCase("ScryFall")) {
					cartaInfo.add(WebScrapGoogleScryfall.devolverCartaBuscada(finalValorCodigo));
				} else if (tipoTienda.equalsIgnoreCase("TCGPlayer")) {
					cartaInfo.add(WebScrapGoogleTCGPlayer.devolverCartaBuscada(finalValorCodigo));
				}
			} else {
				List<String> enlaces = WebScrapGoogleCardMarket.buscarEnGoogle(finalValorCodigo);
				controlCargaCartas(enlaces.size());
				nav.verCargaCartas(cargaCartasControllerRef);
				for (String string : enlaces) {
					cartaInfo.add(WebScrapGoogleCardMarket.extraerDatosMTG(string));
				}
			}

			// Convertir la lista a un Set para eliminar duplicados
			Set<Carta> cartaSet = new HashSet<>(cartaInfo);
			cartaInfo.clear(); // Limpiar la lista original
			cartaInfo.addAll(cartaSet); // Agregar los elementos únicos de vuelta a la lista
			procesarCartas(cartaInfo);
			return cartaInfo;

		} catch (URISyntaxException e) {
			// Manejar excepciones
			System.err.println("Error al obtener información del cómic: " + e.getMessage());
			return null;
		}
	}

	public static String obtenerNombreTienda(String tipoTienda) {
		if (tipoTienda.toLowerCase().contains("cardmarket")) {
			return "cardmarket";
		} else if (tipoTienda.toLowerCase().contains("scryfall")) {
			return "scryfall";
		} else if (tipoTienda.toLowerCase().contains("tcgplayer")) {
			return "tcgplayer";
		} else {
			return "Tienda no reconocida";
		}
	}

	private static void procesarCartas(List<Carta> cartaInfo) {
		List<Carta> cartasProcesadas = new ArrayList<>();

		for (Carta cartaNueva : cartaInfo) {
			String referenciaNueva = obtenerReferenciaBase(cartaNueva.getUrlReferenciaCarta());
			boolean cartaDuplicada = false;

			for (Carta cartaExistente : cartasProcesadas) {
				String referenciaExistente = obtenerReferenciaBase(cartaExistente.getUrlReferenciaCarta());

				if (referenciaExistente.equals(referenciaNueva)) {
					procesarCartaExistente(cartaNueva, cartaExistente);
					cartaDuplicada = true;
					break;
				}
			}

			if (!cartaDuplicada) {
				cartasProcesadas.add(cartaNueva);
			}
		}

		cartaInfo.clear();
		cartaInfo.addAll(cartasProcesadas);
	}

	private static void procesarCartaExistente(Carta cartaNueva, Carta cartaExistente) {
		if (cartaNueva.getUrlReferenciaCarta().endsWith("?isFoil=Y")) {
			procesarCartaNuevaFoil(cartaNueva, cartaExistente);
		} else {
			procesarCartaNuevaNormal(cartaNueva, cartaExistente);
		}
	}

	private static void procesarCartaNuevaFoil(Carta cartaNueva, Carta cartaExistente) {
		if (cartaExistente.getPrecioCartaFoil().equals("0.0")) {
			cartaExistente.setPrecioCartaFoil(cartaNueva.getPrecioCartaFoil());
		}

		fusionarPrecios(cartaNueva, cartaExistente);
	}

	private static void procesarCartaNuevaNormal(Carta cartaNueva, Carta cartaExistente) {
		if (cartaExistente.getPrecioCartaNormal().equals("0.0")) {
			cartaExistente.setPrecioCartaNormal(cartaNueva.getPrecioCartaNormal());
		}

		fusionarPrecios(cartaNueva, cartaExistente);
	}

	private static void fusionarPrecios(Carta cartaNueva, Carta cartaExistente) {
		if (!cartaExistente.getPrecioCartaNormal().equals("0.0")
				&& !cartaExistente.getPrecioCartaFoil().equals("0.0")) {
			cartaNueva.setPrecioCartaNormal(cartaExistente.getPrecioCartaNormal());
			cartaNueva.setPrecioCartaFoil(cartaExistente.getPrecioCartaFoil());
		}
	}

	// Método auxiliar para obtener la referencia base sin el sufijo "?isFoil=Y"
	private static String obtenerReferenciaBase(String urlReferencia) {
		if (urlReferencia.endsWith("?isFoil=Y")) {
			return urlReferencia.substring(0, urlReferencia.length() - "?isFoil=Y".length());
		}
		return urlReferencia;
	}

	private static void actualizarInterfaz(AtomicInteger contadorErrores, String carpetaDatabase,
			AtomicInteger contadorTotal) {
		Platform.runLater(() -> {
			String mensaje = "";

			if (!carpetaDatabase.isEmpty()) {
				mensaje = "Se han procesado: " + (contadorTotal.get() - contadorErrores.get()) + " de "
						+ contadorTotal.get();
			} else {
				mensaje = "Se han procesado: " + (contadorErrores.get()) + " de " + contadorTotal.get();

			}

			AlarmaList.mostrarMensajePront(mensaje, true, getReferenciaVentana().getProntInfoTextArea());
		});
	}

	// ES OPCIONES
	public static void busquedaPorListaDatabase(List<Carta> listaCartasDatabase, String tipoUpdate) {

		controlCargaCartas(listaCartasDatabase.size());

		Task<Void> tarea = createSearchTask(tipoUpdate, listaCartasDatabase);

		handleTaskEvents(tarea, tipoUpdate);

		Thread thread = new Thread(tarea);
		thread.setDaemon(true);
		thread.start();
	}

	// ES ACCION
	public static void busquedaPorCodigoImportacion(File file) {

		fichero = file;

		int numCargas = Utilidades.contarLineasFichero(fichero);

		controlCargaCartas(numCargas);

		Task<Void> tarea = createSearchTask("", null);

		handleTaskEvents(tarea, "");

		Thread thread = new Thread(tarea);
		thread.setDaemon(true);
		thread.start();
	}

	public static void controlCargaCartas(int numCargas) {
		codigoFaltante = new StringBuilder();
		codigoFaltante.setLength(0);
		contadorErrores = new AtomicInteger(0);
		cartasProcesados = new AtomicInteger(0);
		mensajeIdCounter = new AtomicInteger(0);
		numLineas = new AtomicInteger(0);
		numLineas.set(numCargas);
		cargaCartasControllerRef = new AtomicReference<>();
		mensajesUnicos = new HashSet<>();
		mensajesUnicos.clear();
	}

	private static Task<Void> createSearchTask(String tipoUpdate, List<Carta> listaCartasDatabase) {
		return new Task<>() {
			@Override
			protected Void call() {
				nav.verCargaCartas(cargaCartasControllerRef);

				if (tipoUpdate.isEmpty()) {
					try (BufferedReader reader = new BufferedReader(new FileReader(fichero))) {
						List<Carta> listaSinDuplicados = new ArrayList<>();

						reader.lines().forEach(linea -> {

							String tipoTienda = obtenerNombreTienda(linea);

							if (isCancelled() || !getReferenciaVentana().getStageVentana().isShowing()) {
								Platform.runLater(() -> cargaCartasControllerRef.get().cargarDatosEnCargaCartas("",
										"100%", 100.0));
								return;
							}
							List<Carta> listaOriginal = obtenerCartaInfo(linea, true, tipoTienda);

							// Procesar y fusionar duplicados
							for (Carta carta : listaOriginal) {
								agregarCartaConDuplicados(listaSinDuplicados, carta);
								mensajesCargaCartas(carta, tipoUpdate);

								processCarta(carta, "");
							}

						});

					} catch (IOException e) {
						Utilidades.manejarExcepcion(e);
					}
				} else {
					listaCartasDatabase.forEach(carta -> {
						if (isCancelled() || !getReferenciaVentana().getStageVentana().isShowing()) {
							ListasCartasDAO.eliminarUltimaCartaImportada(); // Eliminar la última carta importada
							return; // Salir del forEach si el Task está cancelado
						}
						mensajesCargaCartas(carta, tipoUpdate);
						processCarta(carta, tipoUpdate);
					});
				}
				return null;
			}
		};
	}

	// Método para agregar carta y fusionar duplicados
	private static void agregarCartaConDuplicados(List<Carta> cartaInfo, Carta cartaNueva) {
		String referenciaNueva = obtenerReferenciaBase(cartaNueva.getUrlReferenciaCarta());
		boolean cartaDuplicada = false;

		for (Carta cartaExistente : cartaInfo) {
			String tipoTienda = obtenerNombreTienda(cartaExistente.getUrlReferenciaCarta());

			if (tipoTienda.equalsIgnoreCase("cardMarket")) {
				String referenciaExistente = obtenerReferenciaBase(cartaExistente.getUrlReferenciaCarta());

				if (referenciaExistente.equals(referenciaNueva)) {
					if (cartaNueva.getUrlReferenciaCarta().endsWith("?isFoil=Y")) {
						if (cartaExistente.getPrecioCartaFoil().equals("0.0")) {
							cartaExistente.setPrecioCartaFoil(cartaNueva.getPrecioCartaFoil());
						}
					} else {
						if (cartaExistente.getPrecioCartaNormal().equals("0.0")) {
							cartaExistente.setPrecioCartaNormal(cartaNueva.getPrecioCartaNormal());
						}
					}
					cartaDuplicada = true;
					break;
				}
			}
		}

		if (!cartaDuplicada) {
			cartaInfo.add(cartaNueva);
		}
	}

	private static void processCarta(Carta carta, String tipoUpdate) {

		if (tipoUpdate.isEmpty()) {

			String tipoTienda = obtenerNombreTienda(carta.getUrlReferenciaCarta());
			if (tipoTienda.equalsIgnoreCase("CardMarket")) {
				String urlImagen = WebScrapGoogleCardMarket.extraerImagen(carta);
				carta.setDireccionImagenCarta(urlImagen);
			}

			if (!carta.getNomCarta().isEmpty()) {
				AccionFuncionesComunes.procesarCartaPorCodigo(carta, false);
			}

		} else if (tipoUpdate.equalsIgnoreCase("actualizar portadas")) {
			String tipoTienda = determinarTipoTienda(carta.getUrlReferenciaCarta());
			AccionFuncionesComunes.actualizarCartasPortadas(carta, tipoTienda);
		} else {
			AccionFuncionesComunes.actualizarCartasDatabase(carta, tipoUpdate);
		}
	}

	private static void mensajesCargaCartas(Carta carta, String tipoUpdate) {
		StringBuilder textoBuilder = new StringBuilder();

		if (carta.getUrlReferenciaCarta().isEmpty() || carta.getUrlReferenciaCarta().equalsIgnoreCase("vacio")) {

			if (tipoUpdate.isEmpty()) {
				codigoFaltante.append("Falta carta con código: ").append(carta.getNomCarta()).append("\n");
				textoBuilder.append("Carta no capturado: ").append(carta.getNomCarta()).append("\n");
			} else {
				codigoFaltante.append("ID no procesado: ").append(carta.getIdCarta()).append("\n");
				textoBuilder.append("ID no procesado: ").append(carta.getIdCarta()).append("\n");
			}

			contadorErrores.getAndIncrement();

		} else {

			if (tipoUpdate.isEmpty()) {
				textoBuilder.append("Codigo: ").append(carta.getNomCarta()).append(" procesado.").append("\n");
			} else if (tipoUpdate.equalsIgnoreCase("actualizar portadas")) {
				textoBuilder.append("Portada Carta ID: ").append(carta.getIdCarta()).append(" actualizado.")
						.append("\n");
			} else {
				textoBuilder.append("ID: ").append(carta.getIdCarta()).append(" actualizado.").append("\n");
			}
		}
		updateGUI(textoBuilder);
	}

	private static void updateGUI(StringBuilder textoBuilder) {
		String mensajeId = String.valueOf(mensajeIdCounter.getAndIncrement());

		String mensaje = mensajeId + ": " + textoBuilder.toString();
		mensajesUnicos.add(mensaje);
		cartasProcesados.getAndIncrement();

		long finalProcessedItems = cartasProcesados.get();
		double progress = (double) finalProcessedItems / (numLineas.get());
		String porcentaje = String.format("%.2f%%", progress * 100);

		if (nav.isVentanaCerrada()) {

			nav.verCargaCartas(cargaCartasControllerRef);

			StringBuilder textoFiltrado = new StringBuilder();
			List<String> mensajesOrdenados = new ArrayList<>(mensajesUnicos);

			Collections.sort(mensajesOrdenados, Comparator.comparingInt(m -> Integer.parseInt(m.split(":")[0])));
			for (String mensajeUnico : mensajesOrdenados) {
				if (!mensajeUnico.equalsIgnoreCase(mensaje)) {
					textoFiltrado.append(mensajeUnico.substring(mensajeUnico.indexOf(":") + 2));
				}
			}

			Platform.runLater(() -> cargaCartasControllerRef.get().cargarDatosEnCargaCartas(textoFiltrado.toString(),
					porcentaje, progress));
		}
		Platform.runLater(() -> cargaCartasControllerRef.get().cargarDatosEnCargaCartas(textoBuilder.toString(),
				porcentaje, progress));

	}

	public static void cargarRuning() {
		AccionControlUI.limpiarAutorellenos(false);

		cambiarEstadoBotones(true);
		getReferenciaVentana().getImagenCarta().setImage(null);
		getReferenciaVentana().getImagenCarta().setVisible(true);

		AlarmaList.iniciarAnimacionCargaImagen(getReferenciaVentana().getCargaImagen());
		FuncionesManejoFront.cambiarEstadoMenuBar(true, referenciaVentana);
		FuncionesManejoFront.cambiarEstadoMenuBar(true, referenciaVentanaPrincipal);

		getReferenciaVentana().getMenuImportarFicheroCodigoBarras().setDisable(true);
		getReferenciaVentana().getBotonSubidaPortada().setDisable(true);
		getReferenciaVentana().getBotonEliminarImportadoListaCarta().setVisible(false);
		getReferenciaVentana().getBotonGuardarListaCartas().setVisible(false);

		getReferenciaVentana().getBotonClonarCarta().setVisible(false);
		getReferenciaVentana().getBotonGuardarCarta().setVisible(false);
		getReferenciaVentana().getBotonEliminarImportadoCarta().setVisible(false);
		getReferenciaVentana().getTablaBBDD().setDisable(true);

		AlarmaList.mostrarMensajePront("Se estan cargando los datos", true,
				getReferenciaVentana().getProntInfoTextArea());
		AlarmaList.iniciarAnimacionCarga(getReferenciaVentana().getProgresoCarga());
	}

	public static void cargarCompletado() {
		AlarmaList.detenerAnimacionCargaImagen(getReferenciaVentana().getCargaImagen());
		cambiarEstadoBotones(false);

		getReferenciaVentana().getMenuImportarFicheroCodigoBarras().setDisable(false);
		AlarmaList.detenerAnimacionCarga(getReferenciaVentana().getProgresoCarga());

		if (cargaCartasControllerRef != null) {
			actualizarInterfaz(contadorErrores, carpetaRaizPortadas(Utilidades.nombreDB()), numLineas);
			Platform.runLater(() -> cargaCartasControllerRef.get().cargarDatosEnCargaCartas("", "100%", 100.0));
		}

	}

	public static void handleTaskEvents(Task<Void> tarea, String tipoUpdate) {

		tarea.setOnRunning(ev -> {
			if (tipoUpdate.isEmpty()) {
				cargarRuning();
			} else {
				String cadenaAfirmativo = getAffirmativeMessage(tipoUpdate);
				List<Stage> stageVentanas = FuncionesManejoFront.getStageVentanas();

				for (Stage stage : stageVentanas) {
					if (stage.getTitle().equalsIgnoreCase("Menu principal")
							|| stage.getTitle().equalsIgnoreCase("Opciones avanzadas")) {
						stage.setOnCloseRequest(closeEvent -> {

							tarea.cancel(true);
							Utilidades.cerrarCargaCartas();
							FuncionesManejoFront.cambiarEstadoMenuBar(false, referenciaVentana);
							FuncionesManejoFront.cambiarEstadoMenuBar(false, referenciaVentanaPrincipal);
							FuncionesManejoFront.cambiarEstadoOpcionesAvanzadas(false, referenciaVentana);
							nav.cerrarMenuOpciones();
							nav.cerrarVentanaAccion();

							Utilidades.cerrarOpcionesAvanzadas();
						});
					}
				}

				getReferenciaVentanaPrincipal().getProntInfoTextArea().setDisable(false);
				getReferenciaVentanaPrincipal().getProntInfoTextArea().setOpacity(1);

				AlarmaList.mostrarMensajePront("Se estan cargando los datos", true,
						getReferenciaVentanaPrincipal().getProntInfoTextArea());

				AlarmaList.iniciarAnimacionAvanzado(getReferenciaVentana().getProntInfoEspecial(), cadenaAfirmativo);
				getReferenciaVentana().getBotonCancelarSubida().setVisible(true);
				actualizarCombobox();
				FuncionesManejoFront.cambiarEstadoMenuBar(true, referenciaVentana);
				FuncionesManejoFront.cambiarEstadoMenuBar(true, referenciaVentanaPrincipal);
				FuncionesManejoFront.cambiarEstadoOpcionesAvanzadas(true, getReferenciaVentana());
			}

		});

		tarea.setOnSucceeded(ev -> {

			if (ListasCartasDAO.cartasImportados.isEmpty() && tipoUpdate.isEmpty()) {

				List<Node> elementos = Arrays.asList(referenciaVentana.getBotonEliminarImportadoListaCarta(),
						referenciaVentana.getBotonGuardarListaCartas(), referenciaVentana.getBotonGuardarCarta(),
						referenciaVentana.getBotonEliminarImportadoCarta());

				Utilidades.cambiarVisibilidad(elementos, true);

				String cadenaAfirmativo = "No se han encontrado Cartas en los datos proporcionados";
				AlarmaList.mostrarMensajePront(cadenaAfirmativo, false, getReferenciaVentana().getProntInfoTextArea());
				nav.cerrarCargaCartas();
				getReferenciaVentana().getBotonCancelarSubida().setVisible(false);
			} else {
				if (tipoUpdate.isEmpty()) {
					getReferenciaVentana().getTablaBBDD().setDisable(false);
					referenciaVentana.getBotonEliminarImportadoListaCarta().setVisible(true);
					referenciaVentana.getBotonGuardarListaCartas().setVisible(true);

					cargarCompletado();
				} else {

					AlarmaList.mostrarMensajePront("Datos cargados correctamente", true,
							getReferenciaVentanaPrincipal().getProntInfoTextArea());

					Platform.runLater(() -> getReferenciaVentana().getBotonCancelarSubida().setVisible(false));
					String cadenaAfirmativo = getUpdateTypeString(tipoUpdate);
					AlarmaList.iniciarAnimacionAvanzado(getReferenciaVentana().getProntInfoEspecial(),
							cadenaAfirmativo);

					actualizarCombobox();

					FuncionesManejoFront.cambiarEstadoOpcionesAvanzadas(false, getReferenciaVentana());

				}

			}

			FuncionesManejoFront.cambiarEstadoMenuBar(false, referenciaVentana);
			FuncionesManejoFront.cambiarEstadoMenuBar(false, referenciaVentanaPrincipal);
			AlarmaList.detenerAnimacionCargaImagen(getReferenciaVentana().getCargaImagen());
			AlarmaList.detenerAnimacionCarga(getReferenciaVentana().getProgresoCarga());

			cambiarEstadoBotones(false);
		});

		tarea.setOnCancelled(ev -> {
			if (ListasCartasDAO.cartasImportados.isEmpty() && tipoUpdate.isEmpty()) {

				List<Node> elementos = Arrays.asList(referenciaVentana.getBotonEliminarImportadoListaCarta(),
						referenciaVentana.getBotonGuardarListaCartas(), referenciaVentana.getBotonGuardarCarta(),
						referenciaVentana.getBotonEliminarImportadoCarta());

				Utilidades.cambiarVisibilidad(elementos, true);

				String cadenaAfirmativo = "No se han encontrado Cartas en los datos proporcionados";
				AlarmaList.mostrarMensajePront(cadenaAfirmativo, false, getReferenciaVentana().getProntInfoTextArea());
				nav.cerrarCargaCartas();
			} else {
				if (tipoUpdate.isEmpty()) {

					getReferenciaVentana().getTablaBBDD().setDisable(false);
					cambiarEstadoBotones(false);

					Platform.runLater(() -> cargaCartasControllerRef.get().cargarDatosEnCargaCartas("", "100%", 100.0));

					AlarmaList.mostrarMensajePront("Se ha cancelado la importacion", false,
							getReferenciaVentana().getProntInfoTextArea());

					AlarmaList.detenerAnimacionCarga(getReferenciaVentana().getProgresoCarga());
					AlarmaList.detenerAnimacionCargaImagen(getReferenciaVentana().getCargaImagen());
					Thread.currentThread().interrupt();
				} else {
					String cadenaAfirmativo = "Cancelada la actualización de la base de datos.";
					AlarmaList.iniciarAnimacionAvanzado(getReferenciaVentana().getProntInfoEspecial(),
							cadenaAfirmativo);
//					actualizarCombobox();
					FuncionesManejoFront.cambiarEstadoOpcionesAvanzadas(false, getReferenciaVentana());
					Platform.runLater(() -> getReferenciaVentana().getBotonCancelarSubida().setVisible(false));
				}

			}

			if (!ListasCartasDAO.cartasImportados.isEmpty()) {
				referenciaVentana.getBotonEliminarImportadoListaCarta().setVisible(true);
				referenciaVentana.getBotonGuardarListaCartas().setVisible(true);
			}

			AlarmaList.detenerAnimacionCargaImagen(getReferenciaVentana().getCargaImagen());
			AlarmaList.detenerAnimacionCarga(getReferenciaVentana().getProgresoCarga());
			FuncionesManejoFront.cambiarEstadoMenuBar(false, referenciaVentana);
			FuncionesManejoFront.cambiarEstadoMenuBar(false, referenciaVentanaPrincipal);
		});

		// Configurar el evento para cancelar la tarea cuando se presiona el botón de
		// cancelar subida
		getReferenciaVentana().getBotonCancelarSubida().setOnAction(ev -> {
			if (tipoUpdate.isEmpty()) {
				actualizarInterfaz(cartasProcesados, "", numLineas);
				cambiarEstadoBotones(false);
				getReferenciaVentana().getMenuImportarFicheroCodigoBarras().setDisable(false);
			} else {
				referenciaVentanaPrincipal.getProntInfoTextArea().clear();
				referenciaVentanaPrincipal.getProntInfoTextArea().setText(null);
				referenciaVentanaPrincipal.getProntInfoTextArea().setOpacity(0);
				Platform.runLater(() -> cargaCartasControllerRef.get().cargarDatosEnCargaCartas("", "100%", 100.0));
				String cadenaAfirmativo = "Cancelada la actualización de la base de datos.";
				AlarmaList.iniciarAnimacionAvanzado(getReferenciaVentana().getProntInfoEspecial(), cadenaAfirmativo);
				actualizarCombobox();
				FuncionesManejoFront.cambiarEstadoMenuBar(false, referenciaVentana);
				FuncionesManejoFront.cambiarEstadoMenuBar(false, referenciaVentanaPrincipal);
				FuncionesManejoFront.cambiarEstadoOpcionesAvanzadas(false, getReferenciaVentana());
				Platform.runLater(() -> getReferenciaVentana().getBotonCancelarSubida().setVisible(false));
				getReferenciaVentana().getBotonCancelarSubida().setVisible(false);

			}
			tarea.cancel(true);
		});
	}

	private static String getUpdateTypeString(String tipoUpdate) {
		if (tipoUpdate.equalsIgnoreCase("actualizar datos")) {
			return "Datos actualizados";
		} else if (tipoUpdate.equalsIgnoreCase("actualizar portadas")) {
			return "Portadas actualizadas";
		} else {
			return "Base de datos actualizada";
		}
	}

	private static String getAffirmativeMessage(String tipoUpdate) {
		if (tipoUpdate.equalsIgnoreCase("actualizar datos")) {
			return "Actualizando datos";
		} else if (tipoUpdate.equalsIgnoreCase("actualizar portadas")) {
			return "Actualizando portadas";
		} else {
			return "Actualizando base de datos";
		}
	}

	public static void actualizarCombobox() {
		ListasCartasDAO.reiniciarListaCartas();
		ListasCartasDAO.listasAutoCompletado();

		getReferenciaVentana();
		List<ComboBox<String>> comboboxes = AccionReferencias.getListaComboboxes();

		if (comboboxes != null) {
			funcionesCombo.rellenarComboBox(comboboxes);
		}
	}

	/**
	 * Elimina cualquier resaltado de campos en rojo que indique errores.
	 */
	public static void borrarErrores() {

		if (getReferenciaVentana() != null) {
			setStyleIfNotNull(getReferenciaVentana().getNombreCartaTextField());
			setStyleIfNotNull(getReferenciaVentana().getNumeroCartaCombobox());
			setStyleIfNotNull(getReferenciaVentana().getEditorialCartaTextField());
			setStyleIfNotNull(getReferenciaVentana().getColeccionCartaTextField());
			setStyleIfNotNull(getReferenciaVentana().getPrecioCartaNormalTextField());
			setStyleIfNotNull(getReferenciaVentana().getPrecioCartaFoilTextField());
		}
	}

	public static void setStyleIfNotNull(Node element) {
		if (element != null) {
			element.setStyle("");
		}
	}

	/**
	 * Modifica la visibilidad y el estado de los elementos de búsqueda en la
	 * interfaz de usuario.
	 *
	 * @param mostrar True para mostrar los elementos de búsqueda, False para
	 *                ocultarlos.
	 */
	public static void cambiarVisibilidadAvanzada() {

		List<Node> elementos = Arrays.asList(getReferenciaVentana().getBotonBusquedaCodigo(),
				getReferenciaVentana().getBusquedaCodigoTextField(), getReferenciaVentana().getNombreTiendaCombobox());

		if (getReferenciaVentana().getBotonBusquedaCodigo().isVisible()) {
			Utilidades.cambiarVisibilidad(elementos, true);
		} else {
			getReferenciaVentana().getBusquedaCodigoTextField().setText("");
			Utilidades.cambiarVisibilidad(elementos, false);

		}
	}

	public static void cambiarEstadoBotones(boolean esCancelado) {

		if (TIPO_ACCION != null) {
			List<Node> elementos = Arrays.asList(getReferenciaVentana().getBotonSubidaPortada());

			if (!TIPO_ACCION.equals("aniadir")) {
				elementos.add(getReferenciaVentana().getBotonBusquedaCodigo());
				elementos.add(getReferenciaVentana().getBusquedaCodigoTextField());
				elementos.add(getReferenciaVentana().getBotonCancelarSubida());
				elementos.add(getReferenciaVentana().getBotonBusquedaCodigo());
				elementos.add(getReferenciaVentana().getBotonSubidaPortada());
				elementos.add(getReferenciaVentana().getNombreTiendaCombobox());
			}

			getReferenciaVentana().getBotonCancelarSubida().setVisible(esCancelado);

			if (getReferenciaVentana().getBotonLimpiar() != null) {
				getReferenciaVentana().getBotonLimpiar().setDisable(esCancelado);
				getReferenciaVentana().getBotonBusquedaAvanzada().setDisable(esCancelado);
			}

			Utilidades.cambiarVisibilidad(elementos, esCancelado);
		}

	}

	public static String carpetaRaizPortadas(String nombreDatabase) {
		return DOCUMENTS_PATH + File.separator + "album_cartas" + File.separator + nombreDatabase + File.separator;
	}

	public static String carpetaPortadas(String nombreDatabase) {
		return DOCUMENTS_PATH + File.separator + "album_cartas" + File.separator + nombreDatabase + File.separator
				+ "portadas";
	}

	public static void setTipoAccion(String tipoAccion) {
		TIPO_ACCION = tipoAccion;
	}

	public static String getTipoAccion() {
		return TIPO_ACCION;
	}

	public static AccionReferencias getReferenciaVentana() {
		return referenciaVentana;
	}

	public static AccionReferencias getReferenciaVentanaPrincipal() {
		return referenciaVentanaPrincipal;
	}

	public static void setReferenciaVentana(AccionReferencias referenciaVentana) {
		AccionFuncionesComunes.referenciaVentana = referenciaVentana;
	}

	public static void setReferenciaVentanaPrincipal(AccionReferencias referenciaVentana) {
		AccionFuncionesComunes.referenciaVentanaPrincipal = referenciaVentana;
	}

}
