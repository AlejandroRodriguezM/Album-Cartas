package funcionesInterfaz;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import Controladores.VentanaAccionController;
import alarmas.AlarmaList;
import cartaManagement.Carta;
import dbmanager.CartaManagerDAO;
import dbmanager.ListasCartasDAO;
import funcionesAuxiliares.Utilidades;
import funcionesManagment.AccionAniadir;
import funcionesManagment.AccionEliminar;
import funcionesManagment.AccionFuncionesComunes;
import funcionesManagment.AccionModificar;
import funcionesManagment.AccionReferencias;
import funcionesManagment.AccionSeleccionar;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class AccionControlUI {

	private static AccionReferencias referenciaVentana = getReferenciaVentana();

	private static VentanaAccionController accionController = new VentanaAccionController();

	private static AccionAniadir accionAniadir = new AccionAniadir();

	private static AccionEliminar accionEliminar = new AccionEliminar();

	private static AccionModificar accionModificar = new AccionModificar();

	public static void autoRelleno() {

		referenciaVentana.getIdCartaTratarTextField().textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.isEmpty()) {
				if (!rellenarCampos(newValue)) {
					limpiarAutorellenos(false);
					borrarDatosGraficos();
				}
			} else {
				limpiarAutorellenos(false);
				borrarDatosGraficos();
			}
		});
	}

	public static boolean rellenarCampos(String idCarta) {
		Carta cartaTempTemp = Carta.obtenerCarta(idCarta);
		if (cartaTempTemp != null) {
			rellenarDatos(cartaTempTemp);
			return true;
		}
		return false;
	}

	public static void mostrarOpcion(String opcion) {
		ocultarCampos();

		List<Node> elementosAMostrarYHabilitar = new ArrayList<>();

		switch (opcion.toLowerCase()) {
		case "eliminar":
			accionEliminar.mostrarElementosEliminar(elementosAMostrarYHabilitar);
			break;
		case "aniadir":
			accionAniadir.mostrarElementosAniadir(elementosAMostrarYHabilitar);
			break;
		case "modificar":
			accionModificar.mostrarElementosModificar(elementosAMostrarYHabilitar);
			break;
		default:
			accionController.closeWindow();
			return;
		}

		mostrarElementos(elementosAMostrarYHabilitar);
	}

	public static List<Node> modificarInterfazAccion(String opcion) {

		List<Node> elementosAMostrarYHabilitar = new ArrayList<>();

		switch (opcion.toLowerCase()) {
		case "modificar":
			elementosAMostrarYHabilitar.add(referenciaVentana.getBotonModificarCarta());
			break;
		case "aniadir":
			elementosAMostrarYHabilitar.add(referenciaVentana.getBotonGuardarCarta());
			elementosAMostrarYHabilitar.add(referenciaVentana.getBotonEliminarImportadoCarta());
			elementosAMostrarYHabilitar.add(referenciaVentana.getBotonClonarCarta());
			break;
		default:
			break;
		}

		return elementosAMostrarYHabilitar;
	}

	private static void mostrarElementos(List<Node> elementosAMostrarYHabilitar) {
		for (Node elemento : elementosAMostrarYHabilitar) {

			if (elemento != null) {
				elemento.setVisible(true);
				elemento.setDisable(false);
			}
		}

		if (!AccionFuncionesComunes.TIPO_ACCION.equals("modificar")) {
			autoRelleno();
		}

		if (!AccionFuncionesComunes.TIPO_ACCION.equals("aniadir")) {

			referenciaVentana.getNavegacionCerrar().setDisable(true);
			referenciaVentana.getNavegacionCerrar().setVisible(false);

		} else {
			referenciaVentana.getIdCartaTratarTextField().setEditable(false);
			referenciaVentana.getIdCartaTratarTextField().setOpacity(0.7);
		}
		if (AccionFuncionesComunes.TIPO_ACCION.equals("eliminar")) {
			referenciaVentana.getLabelIdMod().setLayoutX(5);
			referenciaVentana.getNumeroCartaCombobox().setVisible(false);
			referenciaVentana.getColeccionCartaTextField().setVisible(false);
			referenciaVentana.getNombreCartaTextField().setVisible(false);
			referenciaVentana.getEditorialCartaTextField().setVisible(false);
			referenciaVentana.getLabelColeccion().setVisible(false);
			referenciaVentana.getLabelNombre().setVisible(false);
//			referenciaVentana.getLabelIdMod().setLayoutY(referenciaVentana.getLabelNombre().getLayoutY());
//			referenciaVentana.getIdCartaTratarTextField()
//					.setLayoutY(referenciaVentana.getNombreCartaTextField().getLayoutY());

		}

		if (AccionFuncionesComunes.TIPO_ACCION.equals("aniadir")) {
			referenciaVentana.getBotonEliminarImportadoListaCarta().setVisible(false);
			referenciaVentana.getBotonGuardarListaCartas().setVisible(false);

			referenciaVentana.getBotonEliminarImportadoListaCarta().setDisable(true);
			referenciaVentana.getBotonGuardarListaCartas().setDisable(true);
		}
//		
		if (AccionFuncionesComunes.TIPO_ACCION.equals("modificar")) {

			referenciaVentana.getBotonModificarCarta().setVisible(false);
			referenciaVentana.getBotonModificarCarta().setDisable(true);
		}

	}

	/**
	 * Oculta y deshabilita varios campos y elementos en la interfaz gráfica.
	 */
	public static void ocultarCampos() {

		List<Node> elementosTextfield = Arrays.asList(referenciaVentana.getRarezaCartaTextField(),
				referenciaVentana.getNormasCartaTextArea(), referenciaVentana.getPrecioCartaNormalTextField(),
				referenciaVentana.getPrecioCartaFoilTextField(), referenciaVentana.getDireccionImagenTextField(),
				referenciaVentana.getUrlReferenciaTextField());

		List<Node> elementosLabel = Arrays.asList(referenciaVentana.getLabelRareza(),
				referenciaVentana.getLabelNormas(), referenciaVentana.getLabelPrecioNormal(),
				referenciaVentana.getLabelPrecioFoil(), referenciaVentana.getLabelPortada(),
				referenciaVentana.getLabelReferencia());

		List<Node> elementosCombobox = Arrays.asList(referenciaVentana.getNumeroCartaCombobox());

		List<Node> elementosBoton = Arrays.asList(referenciaVentana.getBotonSubidaPortada(),
				referenciaVentana.getBotonVender(), referenciaVentana.getBotonEliminar(),
				referenciaVentana.getBotonModificarCarta(), referenciaVentana.getBotonBusquedaCodigo(),
				referenciaVentana.getBotonbbdd());

		Utilidades.cambiarVisibilidad(elementosTextfield, true);
		Utilidades.cambiarVisibilidad(elementosLabel, true);
		Utilidades.cambiarVisibilidad(elementosCombobox, true);
		Utilidades.cambiarVisibilidad(elementosBoton, true);
	}

	/**
	 * Establece los atributos del cómic basándose en el objeto Carta proporcionado.
	 * 
	 * @param cartaTempTemp El objeto Carta que contiene los datos a establecer.
	 */
	public void setAtributosDesdeTabla(Carta cartaTemp) {

		referenciaVentana.getNombreCartaTextField().setText(cartaTemp.getNomCarta());

		String numeroNuevo = cartaTemp.getNumCarta();
		referenciaVentana.getNumeroCartaCombobox().getSelectionModel().select(numeroNuevo);

		referenciaVentana.getEditorialCartaTextField().setText(cartaTemp.getEditorialCarta());

		referenciaVentana.getColeccionCartaTextField().setText(cartaTemp.getColeccionCarta());

		referenciaVentana.getRarezaCartaTextField().setText(cartaTemp.getRarezaCarta());

		referenciaVentana.getNormasCartaTextArea().setText(cartaTemp.getNormasCarta());

		referenciaVentana.getPrecioCartaNormalTextField().setText(cartaTemp.getPrecioCartaNormal());

		referenciaVentana.getPrecioCartaFoilTextField().setText(cartaTemp.getPrecioCartaFoil());

		referenciaVentana.getIdCartaTratarTextField().setText(cartaTemp.getIdCarta());

		referenciaVentana.getUrlReferenciaTextField().setText(cartaTemp.getUrlReferenciaCarta());

		referenciaVentana.getNormasCartaTextArea().setText(cartaTemp.getNormasCarta());

		Utilidades.cargarImagenAsync(cartaTemp.getDireccionImagenCarta(), referenciaVentana.getImagenCarta());
	}

	private static void rellenarDatos(Carta cartaTemp) {

		referenciaVentana.getNumeroCartaCombobox().getSelectionModel().clearSelection();
		referenciaVentana.getNombreCartaTextField().setText(cartaTemp.getNomCarta());
		referenciaVentana.getNumeroCartaCombobox().getSelectionModel().select(cartaTemp.getNumCarta());
		referenciaVentana.getEditorialCartaTextField().setText(cartaTemp.getEditorialCarta());
		referenciaVentana.getColeccionCartaTextField().setText(cartaTemp.getColeccionCarta());
		referenciaVentana.getRarezaCartaTextField().setText(cartaTemp.getRarezaCarta());
		referenciaVentana.getPrecioCartaNormalTextField().setText(cartaTemp.getPrecioCartaNormal());
		referenciaVentana.getPrecioCartaFoilTextField().setText(cartaTemp.getPrecioCartaFoil());
		referenciaVentana.getDireccionImagenTextField().setText(cartaTemp.getDireccionImagenCarta());
		referenciaVentana.getUrlReferenciaTextField().setText(cartaTemp.getUrlReferenciaCarta());

		referenciaVentana.getProntInfoTextArea().clear();
		referenciaVentana.getProntInfoTextArea().setOpacity(1);

		Image imagenCarta = Utilidades.devolverImagenCarta(cartaTemp.getDireccionImagenCarta());
		referenciaVentana.getImagenCarta().setImage(imagenCarta);
	}

	/**
	 * Actualiza los campos únicos del objeto Carta con los valores ingresados en
	 * los campos de la interfaz gráfica.
	 * 
	 * @param cartaTemp El objeto Carta a actualizar.
	 */
	public void actualizarCamposUnicos(Carta cartaTemp) {

		// Get and process the text for NormasCarta
		String normasTexto = referenciaVentana.getNormasCartaTextArea().getText().trim();
		if (!normasTexto.isEmpty()) {
			normasTexto = Utilidades.eliminarEspacios(normasTexto);
		} else if (!Pattern.compile(".*\\w+.*").matcher(normasTexto).matches()) {
			normasTexto = "Vacio";
		}
		cartaTemp.setNormasCarta(normasTexto);

		// Get and process the text for UrlReferenciaCarta
		String urlReferenciaTexto = referenciaVentana.getUrlReferenciaTextField().getText().trim();
		if (!urlReferenciaTexto.isEmpty()) {
			urlReferenciaTexto = Utilidades.eliminarEspacios(urlReferenciaTexto);
		} else {
			urlReferenciaTexto = "Sin referencia";
		}
		cartaTemp.setUrlReferenciaCarta(urlReferenciaTexto);

		String precioTextoNormal = referenciaVentana.getPrecioCartaNormalTextField().getText();
		String precioTextoFoil = referenciaVentana.getPrecioCartaFoilTextField().getText();
		precioTextoNormal = Utilidades.eliminarEspacios(precioTextoNormal);
		precioTextoFoil = Utilidades.eliminarEspacios(precioTextoFoil);

		if (precioTextoNormal.isEmpty()) {
			precioTextoNormal = "0";
		}

		if (precioTextoFoil.isEmpty()) {
			precioTextoFoil = "0";
		}
		cartaTemp.setPrecioCartaNormal(precioTextoNormal);
		cartaTemp.setPrecioCartaFoil(precioTextoFoil);
	}

	public static void validarCamposClave(boolean esBorrado) {
		List<TextField> camposUi = Arrays.asList(referenciaVentana.getNombreCartaTextField(),
				referenciaVentana.getEditorialCartaTextField(), referenciaVentana.getColeccionCartaTextField(),
				referenciaVentana.getRarezaCartaTextField(), referenciaVentana.getPrecioCartaNormalTextField(),
				referenciaVentana.getPrecioCartaFoilTextField());

		for (TextField campoUi : camposUi) {

			if (campoUi != null) {
				String datoCarta = campoUi.getText();

				if (esBorrado) {
					if (datoCarta == null || datoCarta.isEmpty() || datoCarta.equalsIgnoreCase("vacio")) {
						campoUi.setStyle("");
					}
				} else {
					// Verificar si el campo está vacío, es nulo o tiene el valor "Vacio"
					if (datoCarta == null || datoCarta.isEmpty() || datoCarta.equalsIgnoreCase("vacio")) {
						campoUi.setStyle("-fx-background-color: red;");
					} else {
						campoUi.setStyle("");
					}
				}
			}
		}
	}

	public boolean camposCartaSonValidos() {
		List<Control> camposUi = Arrays.asList(referenciaVentana.getNombreCartaTextField(),
				referenciaVentana.getEditorialCartaTextField(), referenciaVentana.getColeccionCartaTextField(),
				referenciaVentana.getRarezaCartaTextField(), referenciaVentana.getNormasCartaTextArea(),
				referenciaVentana.getNumeroCartaCombobox(), referenciaVentana.getPrecioCartaNormalTextField(),
				referenciaVentana.getPrecioCartaFoilTextField());

		for (Control campoUi : camposUi) {
			if (campoUi instanceof TextField) {
				String datoCarta = ((TextField) campoUi).getText();

				// Verificar si el campo está vacío, es nulo o tiene el valor "Vacio"
				if (datoCarta == null || datoCarta.isEmpty() || datoCarta.equalsIgnoreCase("vacio")) {
					campoUi.setStyle("-fx-background-color: #FF0000;");
					return false; // Devolver false si al menos un campo no es válido
				} else {
					campoUi.setStyle("");
				}
			}
		}

		return true; // Devolver true si todos los campos son válidos
	}

	public static boolean comprobarListaValidacion(Carta c) {
		String numCartaStr = c.getNumCarta();
		String precioCartaNormalStr = c.getPrecioCartaNormal();
		String precioCartaFoilStr = c.getPrecioCartaFoil();

		// Validar campos requeridos y "vacio"
		if (c.getNomCarta() == null || c.getNomCarta().isEmpty() || c.getNomCarta().equalsIgnoreCase("vacio")
				|| numCartaStr == null || numCartaStr.isEmpty() || Integer.parseInt(numCartaStr) <= 0
				|| c.getEditorialCarta() == null || c.getEditorialCarta().isEmpty()
				|| c.getEditorialCarta().equalsIgnoreCase("vacio") || c.getColeccionCarta() == null
				|| c.getColeccionCarta().isEmpty() || c.getColeccionCarta().equalsIgnoreCase("vacio")
				|| c.getRarezaCarta() == null || c.getRarezaCarta().isEmpty()
				|| c.getRarezaCarta().equalsIgnoreCase("vacio") || precioCartaNormalStr == null
				|| precioCartaNormalStr.isEmpty() || !isValidPrecio(precioCartaNormalStr) || precioCartaFoilStr == null
				|| precioCartaFoilStr.isEmpty() || !isValidPrecio(precioCartaFoilStr)
				|| c.getUrlReferenciaCarta() == null || c.getUrlReferenciaCarta().isEmpty()
				|| c.getUrlReferenciaCarta().equalsIgnoreCase("vacio")) {

			String mensajePront = "Revisa la lista, algunos campos están mal rellenados.";
			AlarmaList.mostrarMensajePront(mensajePront, false, referenciaVentana.getProntInfoTextArea());
			return false;
		}

		return true;
	}

	public static boolean isValidPrecio(String precioStr) {
		// Verificar si el precio es válido (no vacío, no solo un símbolo, no solo el
		// número 0)
		if (precioStr == null || precioStr.isEmpty()) {
			return false;
		}

		// Formatear el precio
		String formattedPrecio = parsePrecio(precioStr);

		System.out.println(formattedPrecio);

		// Verificar si el precio es "€0.0", "€0", "$0.0", "$0" o solo el símbolo de la
		// moneda
		if (formattedPrecio.equalsIgnoreCase("€0.0") || formattedPrecio.equalsIgnoreCase("€0")
				|| formattedPrecio.equalsIgnoreCase("$0.0") || formattedPrecio.equalsIgnoreCase("$0")
				|| formattedPrecio.equals("€") || formattedPrecio.equals("$")) {
			return false;
		}

		// Verificar si el precio consiste solo en un símbolo
		if (formattedPrecio.length() == 1) {
			return false;
		}

		// Verificar si el precio consiste solo en el número 0
		if (formattedPrecio.equals("0.0") || formattedPrecio.equals("0,0")) {
			return false;
		}

		return true;
	}

	public static String parsePrecio(String precioStr) {
		if (precioStr == null || precioStr.isEmpty()) {
			return "€0.0";
		}

		// Crear un formateador decimal que maneje símbolos y formatos específicos
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		symbols.setGroupingSeparator(',');
		DecimalFormat format = new DecimalFormat("#,##0.00", symbols);
		format.setParseBigDecimal(true);

		// Encontrar el símbolo de moneda al inicio de la cadena
		char simbolo = precioStr.charAt(0);

		// Eliminar todos los caracteres que no son dígitos, punto o coma
		String cleanPrecioStr = precioStr.replaceAll("[^0-9.,]", "");

		// Insertar el símbolo de moneda al inicio del número limpio
		cleanPrecioStr = simbolo + cleanPrecioStr;

		// Devolver el valor parseado como double
		return cleanPrecioStr;
	}

	/**
	 * Borra los datos del cómic
	 */
	public static void limpiarAutorellenos(boolean esPrincipal) {

		if (esPrincipal) {
			return;
		}

		referenciaVentana.getNombreCartaTextField().setText("");
		referenciaVentana.getNumeroCartaCombobox().getEditor().setText("");

		referenciaVentana.getEditorialCartaTextField().setText("");
		referenciaVentana.getColeccionCartaTextField().setText("");
		referenciaVentana.getRarezaCartaTextField().setText("");

		referenciaVentana.getNormasCartaTextArea().setText("");
		referenciaVentana.getIdCartaTratarTextField().setText("");

		referenciaVentana.getDireccionImagenTextField().setText("");
		referenciaVentana.getUrlReferenciaTextField().setText("");
		referenciaVentana.getPrecioCartaNormalTextField().setText("");
		referenciaVentana.getPrecioCartaFoilTextField().setText("");

		if ("aniadir".equals(AccionFuncionesComunes.TIPO_ACCION)) {
			referenciaVentana.getIdCartaTratarTextField().setDisable(false);
			referenciaVentana.getIdCartaTratarTextField().setText("");
			referenciaVentana.getIdCartaTratarTextField().setDisable(true);
		}

		referenciaVentana.getProntInfoTextArea().setText(null);
		referenciaVentana.getProntInfoTextArea().setOpacity(0);
		referenciaVentana.getProntInfoTextArea().setStyle("");
		validarCamposClave(true);
	}

	public static void borrarDatosGraficos() {
		referenciaVentana.getProntInfoTextArea().setText(null);
		referenciaVentana.getProntInfoTextArea().setOpacity(0);
		referenciaVentana.getProntInfoTextArea().setStyle("");
	}

	/**
	 * Asigna tooltips a varios elementos en la interfaz gráfica. Estos tooltips
	 * proporcionan información adicional cuando el usuario pasa el ratón sobre los
	 * elementos.
	 */
	public static void establecerTooltips() {
		Platform.runLater(() -> {
			Map<Node, String> tooltipsMap = new HashMap<>();

			tooltipsMap.put(referenciaVentana.getNombreCartaCombobox(), "Nombre de los cómics / libros / mangas");
			tooltipsMap.put(referenciaVentana.getNumeroCartaCombobox(), "Número del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getNombreEditorialCombobox(),
					"Nombre de la variante del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getBotonLimpiar(), "Limpia la pantalla y reinicia todos los valores");
			tooltipsMap.put(referenciaVentana.getBotonbbdd(), "Botón para acceder a la base de datos");
			tooltipsMap.put(referenciaVentana.getBotonSubidaPortada(), "Botón para subir una portada");
			tooltipsMap.put(referenciaVentana.getBotonEliminar(), "Botón para eliminar un cómic");
			tooltipsMap.put(referenciaVentana.getBotonVender(), "Botón para vender un cómic");
			tooltipsMap.put(referenciaVentana.getBotonParametroCarta(),
					"Botón para buscar un cómic mediante una lista de parámetros");
			tooltipsMap.put(referenciaVentana.getBotonModificarCarta(), "Botón para modificar un cómic");

			tooltipsMap.put(referenciaVentana.getNombreColeccionCombobox(),
					"Nombre de la firma del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getNombreRarezaCombobox(),
					"Nombre del guionista del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getBotonIntroducir(),
					"Realizar una acción de introducción del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getBotonModificar(),
					"Realizar una acción de modificación del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getBotonEliminar(),
					"Realizar una acción de eliminación del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getBotonMostrarParametro(),
					"Buscar por parámetros según los datos rellenados");

			FuncionesTooltips.assignTooltips(tooltipsMap);
		});
	}

	public static void autocompletarListas() {
		FuncionesManejoFront.asignarAutocompletado(referenciaVentana.getNombreCartaTextField(),
				ListasCartasDAO.listaNombre);
		FuncionesManejoFront.asignarAutocompletado(referenciaVentana.getEditorialCartaTextField(),
				ListasCartasDAO.listaEditorial);
		FuncionesManejoFront.asignarAutocompletado(referenciaVentana.getColeccionCartaTextField(),
				ListasCartasDAO.listaColeccion);
		FuncionesManejoFront.asignarAutocompletado(referenciaVentana.getRarezaCartaTextField(),
				ListasCartasDAO.listaRareza);
		FuncionesManejoFront.asignarAutocompletado(referenciaVentana.getPrecioCartaNormalTextField(),
				ListasCartasDAO.listaNumeroCarta);
		FuncionesManejoFront.asignarAutocompletado(referenciaVentana.getPrecioCartaFoilTextField(),
				ListasCartasDAO.listaNumeroCarta);
	}

	public static void controlarEventosInterfaz() {

		referenciaVentana.getProntInfoTextArea().textProperty().addListener((observable, oldValue, newValue) -> {
			FuncionesTableView.ajustarAnchoVBox();
		});

		// Desactivar el enfoque en el VBox para evitar que reciba eventos de teclado
		referenciaVentana.getRootVBox().setFocusTraversable(false);

		// Agregar un filtro de eventos para capturar el enfoque en el TableView y
		// desactivar el enfoque en el VBox
		referenciaVentana.getTablaBBDD().addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
			referenciaVentana.getRootVBox().setFocusTraversable(false);
			referenciaVentana.getTablaBBDD().requestFocus();
		});

		referenciaVentana.getImagenCarta().imageProperty().addListener((observable, oldImage, newImage) -> {
			if (newImage != null) {
				// Cambiar la apariencia del cursor y la opacidad cuando la imagen se ha cargado
				referenciaVentana.getImagenCarta().setOnMouseEntered(e -> {
					if (referenciaVentana.getImagenCarta() != null) {
						referenciaVentana.getImagenCarta().setOpacity(0.7); // Cambiar la opacidad para indicar que es
						// clickable
						referenciaVentana.getImagenCarta().setCursor(Cursor.HAND);
					}
				});

				// Restaurar el cursor y la opacidad al salir del ImageView
				referenciaVentana.getImagenCarta().setOnMouseExited(e -> {

					if (referenciaVentana.getImagenCarta() != null) {

						referenciaVentana.getImagenCarta().setOpacity(1.0); // Restaurar la opacidad
						referenciaVentana.getImagenCarta().setCursor(Cursor.DEFAULT);
					}

				});
			} else {
				// Restaurar el cursor y la opacidad al salir del ImageView
				referenciaVentana.getImagenCarta().setOnMouseEntered(e -> {
					referenciaVentana.getImagenCarta().setCursor(Cursor.DEFAULT);
				});
			}
		});

	}

	public static void controlarEventosInterfazAccion() {
		controlarEventosInterfaz();

		// Establecemos un evento para detectar cambios en el segundo TextField
		referenciaVentana.getIdCartaTratarTextField().textProperty().addListener((observable, oldValue, newValue) -> {
			// Verificar que newValue no sea null antes de usarlo
			AccionSeleccionar.mostrarCarta(newValue, false);
		});

		List<Node> elementos = Arrays.asList(referenciaVentana.getBotonGuardarCarta(),
				referenciaVentana.getBotonEliminarImportadoCarta(),
				referenciaVentana.getBotonEliminarImportadoListaCarta(), referenciaVentana.getBotonGuardarListaCartas(),
				referenciaVentana.getBotonEliminarImportadoCarta(), referenciaVentana.getBotonGuardarCarta());

		ListasCartasDAO.cartasImportados.addListener((ListChangeListener<Carta>) change -> {
			while (change.next()) {

				if (!change.wasAdded() && ListasCartasDAO.cartasImportados.isEmpty()) {
					Utilidades.cambiarVisibilidad(elementos, true);
				}
			}
		});
	}

	public static void controlarEventosInterfazPrincipal(AccionReferencias referenciaVentana) {
		controlarEventosInterfaz();

		referenciaVentana.getTablaBBDD().getSelectionModel().selectedItemProperty()
				.addListener((obs, oldSelection, newSelection) -> {

					if (newSelection != null) {
						//Esto algo hace pero seguramente cambie de idea. Mejor no tocar
//						Carta idRow = referenciaVentana.getTablaBBDD().getSelectionModel().getSelectedItem();
					}
				});

		// Establecer un Listener para el tamaño del AnchorPane
		referenciaVentana.getRootAnchorPane().widthProperty().addListener((observable, oldValue, newValue) -> {

			FuncionesTableView.ajustarAnchoVBox();
			FuncionesTableView.seleccionarRaw();
			FuncionesTableView.modificarColumnas(true);
		});
	}

	public static Carta camposCarta(List<String> camposCarta, boolean esAccion) {
		Carta cartaTemp = new Carta();

		// Asignar los valores a las variables correspondientes
		String nomCarta = camposCarta.get(0);
		String numCarta = camposCarta.get(1);
		String editorialCarta = camposCarta.get(2);
		String coleccionCarta = camposCarta.get(3);
		String rarezaCarta = camposCarta.get(4);

		String urlReferenciaCarta = "";
		String direccionImagenCarta = "";
		String normasCarta = "";
		String idCartaTratar = "";
		String precioCartaNormal = "";
		String precioCartaFoil = "";
		if (esAccion) {

			urlReferenciaCarta = camposCarta.get(5);
			precioCartaFoil = limpiarPrecio(camposCarta.get(6));
			precioCartaNormal = limpiarPrecio(camposCarta.get(7));
			idCartaTratar = camposCarta.get(8);
			direccionImagenCarta = camposCarta.get(9);
			normasCarta = camposCarta.get(10);
		}

		cartaTemp.setNomCarta(Utilidades.defaultIfNullOrEmpty(nomCarta, ""));
		cartaTemp.setNumCarta(Utilidades.defaultIfNullOrEmpty(numCarta, ""));
		cartaTemp.setEditorialCarta(Utilidades.defaultIfNullOrEmpty(editorialCarta, ""));
		cartaTemp.setColeccionCarta(Utilidades.defaultIfNullOrEmpty(coleccionCarta, ""));
		cartaTemp.setRarezaCarta(Utilidades.defaultIfNullOrEmpty(rarezaCarta, ""));
		cartaTemp.setPrecioCartaNormal(Utilidades.defaultIfNullOrEmpty(precioCartaNormal, ""));
		cartaTemp.setPrecioCartaFoil(Utilidades.defaultIfNullOrEmpty(precioCartaFoil, ""));
		cartaTemp.setUrlReferenciaCarta(Utilidades.defaultIfNullOrEmpty(urlReferenciaCarta, ""));
		cartaTemp.setDireccionImagenCarta(Utilidades.defaultIfNullOrEmpty(direccionImagenCarta, ""));
		cartaTemp.setNormasCarta(Utilidades.defaultIfNullOrEmpty(normasCarta, ""));
		cartaTemp.setIdCarta(Utilidades.defaultIfNullOrEmpty(idCartaTratar, ""));

		return cartaTemp;
	}

	public static String limpiarPrecio(String precioStr) {
		// Formatear el precio
		String formattedPrecio = parsePrecio(precioStr);

		// Verificar si el precio es "€0.0", "€0", "$0.0", "$0", "€" o "$"
		if (formattedPrecio.equalsIgnoreCase("€0.0") || formattedPrecio.equalsIgnoreCase("€0")
				|| formattedPrecio.equalsIgnoreCase("$0.0") || formattedPrecio.equalsIgnoreCase("$0")
				|| formattedPrecio.equals("€") || formattedPrecio.equals("$")) {
			return "0";
		}

		// En todos los otros casos, el precio es considerado válido
		return precioStr;
	}

	public static List<String> comprobarYDevolverLista(List<ComboBox<String>> comboBoxes,
			ObservableList<Control> observableList) {
		List<String> valores = new ArrayList<>();
		for (ComboBox<String> comboBox : comboBoxes) {
			valores.add(comboBox.getValue() != null ? comboBox.getValue() : "");
		}
		if (contieneNulo(comboBoxes)) {
			return Arrays.asList(observableList.stream()
					.map(control -> control instanceof TextInputControl ? ((TextInputControl) control).getText() : "")
					.toArray(String[]::new));
		} else {
			return valores;
		}
	}

	private static <T> boolean contieneNulo(List<T> lista) {

		if (lista == null) {
			return false;
		}

		for (T elemento : lista) {
			if (elemento == null) {
				return true;
			}
		}
		return false;
	}

	public static Carta cartaModificado() {

		String id_cartaTemp = referenciaVentana.getIdCartaTratarTextField().getText();

		Carta cartaTempTemp = CartaManagerDAO.cartaDatos(id_cartaTemp);

		List<String> controls = new ArrayList<>();

		for (Control control : AccionReferencias.getListaTextFields()) {
			controls.add(((TextField) control).getText()); // Add the Control object itself
		}

		// Añadir valores de los ComboBoxes de getListaComboboxes() a controls
		for (ComboBox<?> comboBox : AccionReferencias.getListaComboboxes()) {
			Object selectedItem = comboBox.getSelectionModel().getSelectedItem();
			controls.add(selectedItem != null ? selectedItem.toString() : "");
		}

		Carta datos = camposCarta(controls, true);

		int numCarta = Integer.parseInt(datos.getNumCarta());
		double precioCartaNormal = Double.parseDouble(datos.getPrecioCartaNormal());
		double precioCartaFoil = Double.parseDouble(datos.getPrecioCartaFoil());

		Carta cartaTempModificado = new Carta();
		cartaTempModificado.setIdCarta(cartaTempTemp.getIdCarta());
		cartaTempModificado
				.setNomCarta(Utilidades.defaultIfNullOrEmpty(datos.getNomCarta(), cartaTempTemp.getNomCarta()));
		cartaTempModificado.setNumCarta(numCarta != 0 ? datos.getNumCarta() : cartaTempTemp.getNumCarta());
		cartaTempModificado.setEditorialCarta(
				Utilidades.defaultIfNullOrEmpty(datos.getEditorialCarta(), cartaTempTemp.getEditorialCarta()));
		cartaTempModificado.setColeccionCarta(
				Utilidades.defaultIfNullOrEmpty(datos.getColeccionCarta(), cartaTempTemp.getColeccionCarta()));
		cartaTempModificado.setRarezaCarta(
				Utilidades.defaultIfNullOrEmpty(datos.getRarezaCarta(), cartaTempTemp.getRarezaCarta()));
		cartaTempModificado.setPrecioCartaNormal(
				precioCartaNormal != 0.0 ? datos.getPrecioCartaNormal() : cartaTempTemp.getPrecioCartaNormal());
		cartaTempModificado.setPrecioCartaFoil(
				precioCartaFoil != 0.0 ? datos.getPrecioCartaFoil() : cartaTempTemp.getPrecioCartaFoil());
		cartaTempModificado.setUrlReferenciaCarta(
				Utilidades.defaultIfNullOrEmpty(datos.getUrlReferenciaCarta(), cartaTempTemp.getUrlReferenciaCarta()));
		cartaTempModificado.setDireccionImagenCarta(Utilidades.defaultIfNullOrEmpty(datos.getDireccionImagenCarta(),
				cartaTempTemp.getDireccionImagenCarta()));
		cartaTempModificado.setNormasCarta(
				Utilidades.defaultIfNullOrEmpty(datos.getNormasCarta(), cartaTempTemp.getNormasCarta()));

		// Si hay otros campos que deben ser ignorados, simplemente no los incluimos
		// A continuación, puedes agregar cualquier lógica adicional que necesites

		return cartaTempModificado;

	}

	public static AccionReferencias getReferenciaVentana() {
		return referenciaVentana;
	}

	public static void setReferenciaVentana(AccionReferencias referenciaVentana) {
		AccionControlUI.referenciaVentana = referenciaVentana;
	}

}