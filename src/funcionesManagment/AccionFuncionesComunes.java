package funcionesManagment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import Controladores.CargaCartasController;
import alarmas.AlarmaList;
import cartaManagement.Carta;
import dbmanager.CartaManagerDAO;
import dbmanager.ConectManager;
import dbmanager.DBUtilidades;
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
import webScrap.WebScraperPreviewsWorld;

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
		final List<Carta> listaCartas;
		final List<Carta> cartasFinal;

		getReferenciaVentana().getProntInfoTextArea().setOpacity(1);
		if (!accionRellenoDatos.camposCartaSonValidos()) {
			String mensaje = "Error. Debes de introducir los datos correctos";
			AlarmaList.mostrarMensajePront(mensaje, false, getReferenciaVentana().getProntInfoTextArea());
			cartasFinal = ListasCartasDAO.cartasImportados;
			Platform.runLater(() -> FuncionesTableView.tablaBBDD(cartasFinal));

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

				CartaManagerDAO.insertarDatos(comic, true);
				mensaje = " Has introducido correctamente el cómic";
				Carta newSelection = getReferenciaVentana().getTablaBBDD().getSelectionModel().getSelectedItem();

				if (newSelection != null) {
					listaCartas = ListasCartasDAO.cartasImportados;
					String idCarta = newSelection.getIdCarta();
					ListasCartasDAO.cartasImportados.removeIf(c -> c.getIdCarta().equals(idCarta));
					getReferenciaVentana().getTablaBBDD().getItems().clear();
				} else {
					listaCartas = null; // Inicializar listaCartas en caso de que no haya ningún cómic seleccionado
				}

				cartasFinal = listaCartas; // Declarar otra variable final para listaCartas

				Platform.runLater(() -> FuncionesTableView.tablaBBDD(cartasFinal));
				getReferenciaVentana().getTablaBBDD().refresh();
			}

			AlarmaList.mostrarMensajePront(mensaje, esModificacion, getReferenciaVentana().getProntInfoTextArea());
			procesarBloqueComun(comic);
		} catch (IOException | SQLException e) {
			Utilidades.manejarExcepcion(e);
		}

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
		getReferenciaVentana().getImagenCarta().setImage(imagen);

		ListasCartasDAO.listasAutoCompletado();
		FuncionesTableView.nombreColumnas();
		FuncionesTableView.actualizarBusquedaRaw();
	}

	public static boolean procesarCartaPorCodigo(String finalValorCodigo) {

		Carta comicInfo = obtenerCartaInfo(finalValorCodigo);

		if (comprobarCodigo(comicInfo)) {
			rellenarTablaImport(comicInfo);
			return true;
		} else {
			return false;
		}
	}

	public static void actualizarCartasDatabase(Carta comicOriginal, String tipoUpdate, boolean confirmarFirma) {

		if (!comprobarCodigo(comicOriginal)) {
			return;
		}

		String codigoCarta = comicOriginal.getUrlReferenciaCarta();
		Carta comicInfo = obtenerCartaInfo(codigoCarta);

		if (comicInfo == null) {
			return;
		}

		String codigo_imagen = Utilidades.generarCodigoUnico(carpetaPortadas(Utilidades.nombreDB()) + File.separator);
		String urlImagen = comicInfo.getDireccionImagenCarta();
		String urlFinal = carpetaPortadas(Utilidades.nombreDB()) + File.separator + codigo_imagen + ".jpg";
		String correctedUrl = urlImagen.replace("\\", "/").replaceFirst("^http:", "https:");
		comicOriginal.setIdCarta(comicOriginal.getIdCarta());
		if (tipoUpdate.equalsIgnoreCase("modificar") || tipoUpdate.equalsIgnoreCase("actualizar datos")) {

			String numCarta = comicOriginal.getNumCarta();
			String nombreCorregido = Utilidades.eliminarParentesis(comicOriginal.getNomCarta());
			String nombreLimpio = Utilidades.extraerNombreLimpio(nombreCorregido);
			nombreLimpio = DatabaseManagerDAO.corregirPatrones(nombreLimpio);
			String editorial = DatabaseManagerDAO.getComercializadora((comicOriginal.getEditorialCarta()));

			comicOriginal.setNomCarta(nombreLimpio);
			comicOriginal.setNumCarta(numCarta);
			comicOriginal.setEditorialCarta(editorial);

			completarInformacionFaltante(comicOriginal, comicOriginal);
			if (tipoUpdate.equalsIgnoreCase("modificar")) {
				Utilidades.deleteFile(comicOriginal.getDireccionImagenCarta());
				comicOriginal.setDireccionImagenCarta(urlFinal);
			} else {
				comicOriginal.setDireccionImagenCarta(comicOriginal.getDireccionImagenCarta());
			}

		}

		if (tipoUpdate.equalsIgnoreCase("modificar") || tipoUpdate.equalsIgnoreCase("actualizar portadas")) {
			comicOriginal.setDireccionImagenCarta(urlFinal);
			// Asynchronously download and convert image
			Platform.runLater(() -> {
				URI uri = null;
				try {
					uri = new URI(correctedUrl);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				Utilidades.descargarYConvertirImagenAsync(uri, carpetaPortadas(Utilidades.nombreDB()),
						codigo_imagen + ".jpg");
			});
		}

		if (tipoUpdate.equalsIgnoreCase("modificar") || tipoUpdate.equalsIgnoreCase("actualizar datos")
				|| tipoUpdate.equalsIgnoreCase("actualizar portadas")) {
			UpdateManager.actualizarCartaBBDD(comicOriginal, "modificar");
		}
	}

	private static void completarInformacionFaltante(Carta comicInfo, Carta comicOriginal) {
		// Completar información faltante con la información original si está vacía
		if (comicInfo.getEditorialCarta() == null || comicInfo.getEditorialCarta().isEmpty()) {
			comicInfo.setEditorialCarta(comicOriginal.getEditorialCarta());
		}
		if (comicInfo.getColeccionCarta() == null || comicInfo.getColeccionCarta().isEmpty()) {
			comicInfo.setColeccionCarta(comicOriginal.getColeccionCarta());
		}
		if (comicInfo.getRarezaCarta() == null || comicInfo.getRarezaCarta().isEmpty()) {
			comicInfo.setRarezaCarta(comicOriginal.getRarezaCarta());
		}
		if (comicInfo.getPrecioCarta().equals("0")) {
			comicInfo.setPrecioCarta(comicOriginal.getPrecioCarta());
		}
		comicInfo.setEstadoCarta(comicOriginal.getEstadoCarta());

		comicInfo.setDireccionImagenCarta(comicOriginal.getDireccionImagenCarta());

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
			
			System.out.println("Carta: " + nuevoNombreArchivo);
			
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
		}

		getReferenciaVentana().getNombreCartaTextField().setText("");
		getReferenciaVentana().getEditorialCartaTextField().setText("");
		getReferenciaVentana().getColeccionCartaTextField().setText("");
		getReferenciaVentana().getRarezaCartaTextField().setText("");
		getReferenciaVentana().getNormasCartaTextField().setText("");
		getReferenciaVentana().getPrecioCartaTextField().setText("");
		getReferenciaVentana().getIdCartaTratarTextField().setText("");
		getReferenciaVentana().getDireccionImagenTextField().setText("");
		getReferenciaVentana().getProntInfoTextArea().setOpacity(0);
		getReferenciaVentana().getUrlReferenciaTextField().setText("");
		getReferenciaVentana().getNumeroCartaCombobox().getEditor().clear(); // Limpiar el texto en el ComboBox
		getReferenciaVentana().getNombreEsFoilCombobox().getEditor().clear(); // Limpiar el texto en el ComboBox
		getReferenciaVentana().getGradeoCartaCombobox().getEditor().clear(); // Limpiar el texto en el ComboBox
		getReferenciaVentana().getEstadoCartaCombobox().getEditor().clear(); // Limpiar el texto en el ComboBox
		getReferenciaVentana().getImagenCarta().setImage(null);
		if ("modificar".equals(TIPO_ACCION)) {
			AccionControlUI.mostrarOpcion(TIPO_ACCION);
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
	private static void rellenarTablaImport(Carta comic) {
		Platform.runLater(() -> {

			String numCartaStr = comic.getNumCarta();
			String nombreCorregido = Utilidades.eliminarParentesis(comic.getNomCarta());
			// Variables relacionadas con la importación de cómics
			String id = "A" + 0 + "" + (ListasCartasDAO.cartasImportados.size() + 1);
			String titulo = Utilidades.defaultIfNullOrEmpty(DatabaseManagerDAO.corregirPatrones(nombreCorregido),
					"Vacio");

			String numero = Utilidades.defaultIfNullOrEmpty(numCartaStr, "0");
			String coleccion = Utilidades
					.defaultIfNullOrEmpty(DatabaseManagerDAO.corregirNombre(comic.getColeccionCarta()), "Vacio");
			String rareza = Utilidades.defaultIfNullOrEmpty(DatabaseManagerDAO.corregirNombre(comic.getRarezaCarta()),
					"Vacio");
			String esFoil = comic.getEsFoilCarta();
			String gradeo = "NM (Noir Medium)";
			String normas = Utilidades.defaultIfNullOrEmpty(DatabaseManagerDAO.corregirNombre(comic.getNormasCarta()),
					"Vacio");
			String precio = Utilidades.defaultIfNullOrEmpty(comic.getPrecioCarta(), "0");
			// Variables relacionadas con la imagen del cómic

			String urlImagen = comic.getDireccionImagenCarta();
			String estado = "Comprado";
			String urlReferencia = Utilidades.defaultIfNullOrEmpty(comic.getUrlReferenciaCarta(), "Vacio");
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
			String correctedUrl = urlImagen.replace("\\", "/").replace("http:", "https:").replace("https:", "https:/");
			String codigoImagen = Utilidades
					.generarCodigoUnico(carpetaPortadas(Utilidades.nombreDB()) + File.separator);
			URI uri = null;
			try {
				uri = new URI(correctedUrl);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			String imagen = carpetaPortadas(Utilidades.nombreDB()) + File.separator + codigoImagen + ".jpg";
			// Descarga y conversión asíncrona de la imagen
			Utilidades.descargarYConvertirImagenAsync(uri, carpetaPortadas(Utilidades.nombreDB()),
					codigoImagen + ".jpg");

			Carta comicImport = new Carta.CartaBuilder(id, titulo).numCarta(numero).coleccionCarta(coleccion)
					.rarezaCarta(rareza).esFoilCarta(esFoil).gradeoCarta(gradeo).estadoCarta(estado)
					.precioCarta(precio).urlReferenciaCarta(urlReferencia)
					.direccionImagenCarta(imagen).normasCarta(normas).build();

			ListasCartasDAO.cartasImportados.add(comicImport);
//
			FuncionesTableView.nombreColumnas();
			FuncionesTableView.tablaBBDD(ListasCartasDAO.cartasImportados);
		});
	}

	private static Carta obtenerCartaInfo(String finalValorCodigo) {
		try {

			// Obtener información del cómic según la longitud del código
			if (finalValorCodigo.matches("[A-Z]{3}\\d{6}")) {

				return WebScraperPreviewsWorld.displayCartaInfo(finalValorCodigo.trim(),
						getReferenciaVentana().getProntInfoTextArea());
			} else {
				// Si no, intentar obtener la información del cómic de diferentes fuentes
				Carta comicInfo = ApiMarvel.infoCartaCode(finalValorCodigo.trim(),
						getReferenciaVentana().getProntInfo());
				if (comicInfo == null) {
					comicInfo = WebScrapGoogleLeagueOfCartas.obtenerDatosDiv(finalValorCodigo.trim());
				}
				if (comicInfo == null) {
					ApiISBNGeneral isbnGeneral = new ApiISBNGeneral();
					comicInfo = isbnGeneral.getBookInfo(finalValorCodigo.trim(),
							getReferenciaVentana().getProntInfoTextArea());
				}

				if (comicInfo == null) {
					return null;
				}

				Carta.limpiarCamposCarta(comicInfo);
				return comicInfo;
			}
		} catch (URISyntaxException e) {
			// Manejar excepciones
			System.err.println("Error al obtener información del cómic: " + e.getMessage());
			return null;
		}
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
	public static void busquedaPorListaDatabase(List<Carta> listaCartasDatabase, String tipoUpdate,
			boolean actualizarFirma) {

		codigoFaltante = new StringBuilder();
		codigoFaltante.setLength(0);
		contadorErrores = new AtomicInteger(0);
		cartasProcesados = new AtomicInteger(0);
		mensajeIdCounter = new AtomicInteger(0); // Contador para generar IDs únicos
		numLineas = new AtomicInteger(listaCartasDatabase.size()); // Obtener el tamaño de la lista
		cargaCartasControllerRef = new AtomicReference<>();
		mensajesUnicos = new HashSet<>();
		mensajesUnicos.clear();

		Task<Void> tarea = createSearchTask(tipoUpdate, actualizarFirma, listaCartasDatabase);

		handleTaskEvents(tarea, tipoUpdate);

		Thread thread = new Thread(tarea);
		thread.setDaemon(true);
		thread.start();
	}

	// ES ACCION
	public static void busquedaPorCodigoImportacion(File file) {

		fichero = file;
		contadorErrores = new AtomicInteger(0);
		cartasProcesados = new AtomicInteger(0);
		mensajeIdCounter = new AtomicInteger(0);
		numLineas = new AtomicInteger(0);
		numLineas.set(Utilidades.contarLineasFichero(fichero));
		cargaCartasControllerRef = new AtomicReference<>();
		codigoFaltante = new StringBuilder();
		codigoFaltante.setLength(0);
		mensajesUnicos = new HashSet<>();
		mensajesUnicos.clear();

		Task<Void> tarea = createSearchTask("", false, null);

		handleTaskEvents(tarea, "");

		Thread thread = new Thread(tarea);
		thread.setDaemon(true);
		thread.start();
	}

	private static Task<Void> createSearchTask(String tipoUpdate, boolean actualizarFirma,
			List<Carta> listaCartasDatabase) {
		return new Task<>() {
			@Override
			protected Void call() {
				nav.verCargaCartas(cargaCartasControllerRef);

				if (tipoUpdate.isEmpty()) {
					try (BufferedReader reader = new BufferedReader(new FileReader(fichero))) {
						reader.lines().forEach(linea -> {
							if (isCancelled() || !getReferenciaVentana().getStageVentana().isShowing()) {
								return;
							}
							String finalValorCodigo = Utilidades.eliminarEspacios(linea).replace("-", "");
							Carta comicInfo = obtenerCartaInfo(finalValorCodigo);
							processCarta(comicInfo, "", false);
						});
					} catch (IOException e) {
						Utilidades.manejarExcepcion(e);
					}
				} else {
					listaCartasDatabase.forEach(codigo -> {
						String finalValorCodigo = Utilidades.eliminarEspacios(codigo.getcodigoCartaTextField())
								.replace("-", "");
						codigo.setcodigoCarta(finalValorCodigo);
						processCarta(codigo, tipoUpdate, actualizarFirma);

					});
				}
				return null;
			}
		};
	}

	private static void processCarta(Carta comic, String tipoUpdate, boolean actualizarFirma) {
		StringBuilder textoBuilder = new StringBuilder();

		if (comic.getUrlReferenciaCarta().isEmpty() || comic.getUrlReferenciaCarta().equalsIgnoreCase("vacio")) {

			if (tipoUpdate.isEmpty()) {
				codigoFaltante.append("Falta carta con código: ").append(comic.getNomCarta()).append("\n");
				textoBuilder.append("Cómic no capturado: ").append(comic.getNomCarta()).append("\n");
			} else {
				codigoFaltante.append("ID no procesado: ").append(comic.getIdCarta()).append("\n");
				textoBuilder.append("ID no procesado: ").append(comic.getIdCarta()).append("\n");
			}

			contadorErrores.getAndIncrement();

		} else {

			if (tipoUpdate.isEmpty()) {
				textoBuilder.append("Código: ").append(comic.getNomCarta()).append(" procesado.").append("\n");
				AccionFuncionesComunes.procesarCartaPorCodigo(comic.getUrlReferenciaCarta());
			} else {
				textoBuilder.append("ID: ").append(comic.getIdCarta()).append(" actualizado.").append("\n");
				AccionFuncionesComunes.actualizarCartasDatabase(comic, tipoUpdate, actualizarFirma);
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

	private static void handleTaskEvents(Task<Void> tarea, String tipoUpdate) {

		tarea.setOnRunning(ev -> {
			if (tipoUpdate.isEmpty()) {
				AccionControlUI.limpiarAutorellenos(false);

				cambiarEstadoBotones(true);
				getReferenciaVentana().getImagenCarta().setImage(null);
				getReferenciaVentana().getImagenCarta().setVisible(true);

				AlarmaList.iniciarAnimacionCargaImagen(getReferenciaVentana().getCargaImagen());
				FuncionesManejoFront.cambiarEstadoMenuBar(true, referenciaVentana);
				FuncionesManejoFront.cambiarEstadoMenuBar(true, referenciaVentanaPrincipal);

				getReferenciaVentana().getMenuImportarFicheroCodigoBarras().setDisable(true);
				getReferenciaVentana().getBotonSubidaPortada().setDisable(true);

				AlarmaList.mostrarMensajePront("Se estan cargando los datos", true,
						getReferenciaVentana().getProntInfoTextArea());
				AlarmaList.iniciarAnimacionCarga(getReferenciaVentana().getProgresoCarga());
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
			if (tipoUpdate.isEmpty()) {
				AlarmaList.detenerAnimacionCargaImagen(getReferenciaVentana().getCargaImagen());
				cambiarEstadoBotones(false);

				actualizarInterfaz(contadorErrores, carpetaRaizPortadas(Utilidades.nombreDB()), numLineas);

				getReferenciaVentana().getMenuImportarFicheroCodigoBarras().setDisable(false);

				Platform.runLater(() -> cargaCartasControllerRef.get().cargarDatosEnCargaCartas("", "100%", 100.0));
				AlarmaList.detenerAnimacionCarga(getReferenciaVentana().getProgresoCarga());
			} else {

				AlarmaList.mostrarMensajePront("Datos cargados correctamente", true,
						getReferenciaVentanaPrincipal().getProntInfoTextArea());

				Platform.runLater(() -> getReferenciaVentana().getBotonCancelarSubida().setVisible(false));
				String cadenaAfirmativo = getUpdateTypeString(tipoUpdate);
				AlarmaList.iniciarAnimacionAvanzado(getReferenciaVentana().getProntInfoEspecial(), cadenaAfirmativo);
				actualizarCombobox();

				FuncionesManejoFront.cambiarEstadoOpcionesAvanzadas(false, getReferenciaVentana());
			}

			FuncionesManejoFront.cambiarEstadoMenuBar(false, referenciaVentana);
			FuncionesManejoFront.cambiarEstadoMenuBar(false, referenciaVentanaPrincipal);
		});

		tarea.setOnCancelled(ev -> {

			if (tipoUpdate.isEmpty()) {
				Thread.currentThread().interrupt();
				cambiarEstadoBotones(false);

				Platform.runLater(() -> cargaCartasControllerRef.get().cargarDatosEnCargaCartas("", "100%", 100.0));

				AlarmaList.mostrarMensajePront("Se ha cancelado la importacion", false,
						getReferenciaVentana().getProntInfoTextArea());

				AlarmaList.detenerAnimacionCarga(getReferenciaVentana().getProgresoCarga());
				AlarmaList.detenerAnimacionCargaImagen(getReferenciaVentana().getCargaImagen());
			} else {
				String cadenaAfirmativo = "Cancelada la actualización de la base de datos.";
				AlarmaList.iniciarAnimacionAvanzado(getReferenciaVentana().getProntInfoEspecial(), cadenaAfirmativo);
				actualizarCombobox();
				FuncionesManejoFront.cambiarEstadoOpcionesAvanzadas(false, getReferenciaVentana());
				Platform.runLater(() -> getReferenciaVentana().getBotonCancelarSubida().setVisible(false));
			}
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

		List<ComboBox<String>> comboboxes = getReferenciaVentana().getListaComboboxes();

		if (comboboxes != null) {
			funcionesCombo.rellenarComboBox(comboboxes);
		}
	}

	/**
	 * Elimina cualquier resaltado de campos en rojo que indique errores.
	 */
	public static void borrarErrores() {

		getReferenciaVentana().getNombreCartaTextField().setStyle("");
		getReferenciaVentana().getNumeroCartaCombobox().setStyle("");
		getReferenciaVentana().getEditorialCartaTextField().setStyle("");
		getReferenciaVentana().getColeccionCartaTextField().setStyle("");
		getReferenciaVentana().getPrecioCartaTextField().setStyle("");
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
				getReferenciaVentana().getBusquedaCodigoTextField());

		if (getReferenciaVentana().getBotonBusquedaCodigo().isVisible()) {
			Utilidades.cambiarVisibilidad(elementos, true);
		} else {
			Utilidades.cambiarVisibilidad(elementos, false);
		}
	}

	public static void cambiarEstadoBotones(boolean esCancelado) {

		List<Node> elementos = Arrays.asList(getReferenciaVentana().getBotonEliminarImportadoCarta(),
				getReferenciaVentana().getBotonSubidaPortada(), getReferenciaVentana().getBotonGuardarCarta());

		if (!TIPO_ACCION.equals("aniadir")) {
			elementos.add(getReferenciaVentana().getBotonBusquedaCodigo());
			elementos.add(getReferenciaVentana().getBusquedaCodigoTextField());
			elementos.add(getReferenciaVentana().getBotonCancelarSubida());
			elementos.add(getReferenciaVentana().getBotonBusquedaCodigo());
			elementos.add(getReferenciaVentana().getBotonSubidaPortada());
		}

		
		getReferenciaVentana().getBotonCancelarSubida().setVisible(esCancelado);
		getReferenciaVentana().getBotonLimpiar().setDisable(esCancelado);
		getReferenciaVentana().getBotonBusquedaAvanzada().setDisable(esCancelado);
		getReferenciaVentana().getBotonGuardarCambioCarta().setDisable(esCancelado);

		Utilidades.cambiarVisibilidad(elementos, esCancelado);
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
