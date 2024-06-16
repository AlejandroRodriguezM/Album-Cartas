package funcionesInterfaz;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import dbmanager.ConectManager;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class AccionControlUI {

	private static AccionReferencias referenciaVentana = getReferenciaVentana();

	private static VentanaAccionController accionController = new VentanaAccionController();

	private static AccionAniadir accionAniadir = new AccionAniadir();

	private static AccionEliminar accionEliminar = new AccionEliminar();

	private static AccionModificar accionModificar = new AccionModificar();

	public static void autoRelleno() {

		referenciaVentana.getIdCartaTratar().textProperty().addListener((observable, oldValue, newValue) -> {
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
		Carta comicTemp = Carta.obtenerCarta(idCarta);
		if (comicTemp != null) {
			rellenarDatos(comicTemp);
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
		case "puntuar":
			accionModificar.mostrarElementosPuntuar(elementosAMostrarYHabilitar);
			break;
		default:
			accionController.closeWindow();
			return;
		}

		mostrarElementos(elementosAMostrarYHabilitar);
	}

	private static void mostrarElementos(List<Node> elementosAMostrarYHabilitar) {
		for (Node elemento : elementosAMostrarYHabilitar) {
			elemento.setVisible(true);
			elemento.setDisable(false);
		}

		if (!AccionFuncionesComunes.TIPO_ACCION.equals("modificar")) {
			autoRelleno();
		}

		if (!AccionFuncionesComunes.TIPO_ACCION.equals("aniadir")) {
			referenciaVentana.getNavegacion_cerrar().setDisable(true);
			referenciaVentana.getNavegacion_cerrar().setVisible(false);

			referenciaVentana.getIdCartaTratar().setLayoutX(56);
			referenciaVentana.getIdCartaTratar().setLayoutY(104);
			referenciaVentana.getLabel_id_mod().setLayoutX(3);
			referenciaVentana.getLabel_id_mod().setLayoutY(104);
			referenciaVentana.getGradeo().setVisible(false);
		} else {
			referenciaVentana.getIdCartaTratar().setEditable(false);
			referenciaVentana.getIdCartaTratar().setOpacity(0.7);
		}
	}

	/**
	 * Oculta y deshabilita varios campos y elementos en la interfaz gráfica.
	 */
	public static void ocultarCampos() {
		List<Node> elementos = Arrays.asList(referenciaVentana.getTablaBBDD(), referenciaVentana.getDibujanteCarta(),
				referenciaVentana.getEditorialCarta(), referenciaVentana.getEstadoCarta(),
				referenciaVentana.getFechaCarta(), referenciaVentana.getFirmaCarta(),
				referenciaVentana.getFormatoCarta(), referenciaVentana.getGuionistaCarta(),
				referenciaVentana.getNombreKeyIssue(), referenciaVentana.getGradeoCarta(),
				referenciaVentana.getProcedenciaCarta(), referenciaVentana.getUrlReferencia(),
				referenciaVentana.getBotonBorrarOpinion(), referenciaVentana.getPuntuacionMenu(),
				referenciaVentana.getLabelPuntuacion(), referenciaVentana.getBotonAgregarPuntuacion(),
				referenciaVentana.getLabel_id_mod(), referenciaVentana.getBotonVender(),
				referenciaVentana.getBotonEliminar(), referenciaVentana.getBotonModificarCarta(),
				referenciaVentana.getBotonBusquedaCodigo(), referenciaVentana.getBotonbbdd(),
				referenciaVentana.getPrecioCarta(), referenciaVentana.getDireccionImagen(),
				referenciaVentana.getLabel_portada(), referenciaVentana.getLabel_precio(),
				referenciaVentana.getLabel_gradeo(), referenciaVentana.getLabel_dibujante(),
				referenciaVentana.getLabel_editorial(), referenciaVentana.getLabel_estado(),
				referenciaVentana.getLabel_fecha(), referenciaVentana.getLabel_firma(),
				referenciaVentana.getLabel_formato(), referenciaVentana.getLabel_guionista(),
				referenciaVentana.getLabel_key(), referenciaVentana.getLabel_procedencia(),
				referenciaVentana.getLabel_referencia(), referenciaVentana.getCodigoCartaTratar(),
				referenciaVentana.getLabel_codigo_comic(), referenciaVentana.getBotonSubidaPortada());

		Utilidades.cambiarVisibilidad(elementos, true);
	}

	/**
	 * Establece los atributos del cómic basándose en el objeto Carta proporcionado.
	 * 
	 * @param comicTemp El objeto Carta que contiene los datos a establecer.
	 */
	public void setAtributosDesdeTabla(Carta comicTemp) {
		referenciaVentana.getNombreCarta().setText(comicTemp.getNomCarta());

		int numeroNuevo = comicTemp.getNumCarta();
		referenciaVentana.getNumeroCarta().getSelectionModel().select(numeroNuevo);

		referenciaVentana.getVarianteCarta().setText(comicTemp.getVariante());

		referenciaVentana.getFirmaCarta().setText(comicTemp.getFirma());

		referenciaVentana.getEditorialCarta().setText(comicTemp.getEditorial());

		String formato = comicTemp.getFormato();
		referenciaVentana.getFormatoCarta().getSelectionModel().select(formato);

		String procedencia = comicTemp.getProcedencia();
		referenciaVentana.getProcedenciaCarta().getSelectionModel().select(procedencia);

		String fechaString = comicTemp.getFecha();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		LocalDate fecha = LocalDate.parse(fechaString, formatter);
		referenciaVentana.getFechaCarta().setValue(fecha);

		referenciaVentana.getGuionistaCarta().setText(comicTemp.getGuionista());

		referenciaVentana.getDibujanteCarta().setText(comicTemp.getDibujante());

		String cajaAni = comicTemp.getValorGradeo();
		referenciaVentana.getGradeoCarta().getSelectionModel().select(cajaAni);

		referenciaVentana.getNombreKeyIssue().setText(comicTemp.getkeyIssue());

		referenciaVentana.getEstadoCarta().getSelectionModel().select(comicTemp.getEstado());

		referenciaVentana.getPrecioCarta().setText(comicTemp.getprecioCarta());
		referenciaVentana.getUrlReferencia().setText(comicTemp.getUrlReferencia());

		referenciaVentana.getDireccionImagen().setText(comicTemp.getImagen());

		referenciaVentana.getCodigoCartaTratar().setText(comicTemp.getcodigoCarta());

		referenciaVentana.getIdCartaTratar().setText(comicTemp.getid());

		Utilidades.cargarImagenAsync(comicTemp.getImagen(), referenciaVentana.getImagencomic());
	}

	private static void rellenarDatos(Carta comic) {
		referenciaVentana.getNumeroCarta().getSelectionModel().clearSelection();
		referenciaVentana.getFormatoCarta().getSelectionModel().clearSelection();
		referenciaVentana.getGradeoCarta().getSelectionModel().clearSelection();

		referenciaVentana.getNombreCarta().setText(comic.getNombre());
		referenciaVentana.getNumeroCarta().getSelectionModel().select(comic.getNumero());
		referenciaVentana.getVarianteCarta().setText(comic.getVariante());
		referenciaVentana.getFirmaCarta().setText(comic.getFirma());
		referenciaVentana.getEditorialCarta().setText(comic.getEditorial());
		referenciaVentana.getFormatoCarta().getSelectionModel().select(comic.getFormato());
		referenciaVentana.getProcedenciaCarta().getSelectionModel().select(comic.getProcedencia());

		LocalDate fecha = LocalDate.parse(comic.getFecha(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		referenciaVentana.getFechaCarta().setValue(fecha);

		referenciaVentana.getGuionistaCarta().setText(comic.getGuionista());
		referenciaVentana.getDibujanteCarta().setText(comic.getDibujante());
		referenciaVentana.getGradeoCarta().getSelectionModel().select(comic.getValorGradeo());
		referenciaVentana.getNombreKeyIssue().setText(comic.getkeyIssue());
		referenciaVentana.getEstadoCarta().getSelectionModel().select(comic.getEstado());
		referenciaVentana.getPrecioCarta().setText(comic.getprecioCarta());
		referenciaVentana.getUrlReferencia().setText(comic.getUrlReferencia());
		referenciaVentana.getDireccionImagen().setText(comic.getImagen());

		referenciaVentana.getProntInfo().clear();
		referenciaVentana.getProntInfo().setOpacity(1);

		Image imagenCarta = Utilidades.devolverImagenCarta(comic.getImagen());
		referenciaVentana.getImagencomic().setImage(imagenCarta);
	}

	/**
	 * Actualiza los campos únicos del objeto Carta con los valores ingresados en
	 * los campos de la interfaz gráfica.
	 * 
	 * @param comic El objeto Carta a actualizar.
	 */
	public void actualizarCamposUnicos(Carta comic) {

		comic.setkeyIssue(!referenciaVentana.getNombreKeyIssue().getText().isEmpty()
				? Utilidades.eliminarEspacios(referenciaVentana.getNombreKeyIssue().getText())
				: (!referenciaVentana.getNombreKeyIssue().getText().trim().isEmpty() && Pattern.compile(".*\\w+.*")
						.matcher(referenciaVentana.getNombreKeyIssue().getText().trim()).matches()
								? referenciaVentana.getNombreKeyIssue().getText().trim()
								: "Vacio"));

		comic.seturlReferencia(!referenciaVentana.getUrlReferencia().getText().isEmpty()
				? Utilidades.eliminarEspacios(referenciaVentana.getUrlReferencia().getText())
				: (referenciaVentana.getUrlReferencia().getText().isEmpty() ? "Sin referencia"
						: referenciaVentana.getUrlReferencia().getText()));
		comic.setprecioCarta(!referenciaVentana.getPrecioCarta().getText().isEmpty()
				? Utilidades.eliminarEspacios(referenciaVentana.getPrecioCarta().getText())
				: (referenciaVentana.getPrecioCarta().getText().isEmpty() ? "0"
						: referenciaVentana.getPrecioCarta().getText()));
		comic.setcodigoCarta(Utilidades.eliminarEspacios(referenciaVentana.getCodigoCartaTratar().getText()));

		comic.setValorGradeo(comic.getValorGradeo().isEmpty() ? "0" : comic.getValorGradeo());
	}

	public static void validarCamposClave(boolean esBorrado) {
		List<TextField> camposUi = Arrays.asList(referenciaVentana.getNombreCarta(),
				referenciaVentana.getVarianteCarta(), referenciaVentana.getEditorialCarta(),
				referenciaVentana.getPrecioCarta(), referenciaVentana.getGuionistaCarta(),
				referenciaVentana.getDibujanteCarta());

		for (TextField campoUi : camposUi) {
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

	public boolean camposCartaSonValidos() {
		List<Control> camposUi = Arrays.asList(referenciaVentana.getNombreCarta(), referenciaVentana.getVarianteCarta(),
				referenciaVentana.getEditorialCarta(), referenciaVentana.getPrecioCarta(),
				referenciaVentana.getCodigoCartaTratar(), referenciaVentana.getGuionistaCarta(),
				referenciaVentana.getDibujanteCarta(), referenciaVentana.getFechaCarta());

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
			} else if (campoUi instanceof DatePicker) {
				LocalDate fecha = ((DatePicker) campoUi).getValue();

				// Verificar si la fecha está vacía
				if (fecha == null) {
					campoUi.setStyle("-fx-background-color: #FF0000;");
					return false; // Devolver false si al menos un campo no es válido
				} else {
					campoUi.setStyle("");
				}
			}
		}

		return true; // Devolver true si todos los campos son válidos
	}

	public static void comprobarListaValidacion(Carta c) {
		if (c.getNombre() == null || c.getNombre().isEmpty() || c.getNombre().equalsIgnoreCase("vacio")
				|| c.getNumero() == null || c.getNumero().isEmpty() || c.getNumero().equalsIgnoreCase("vacio")
				|| c.getVariante() == null || c.getVariante().isEmpty() || c.getVariante().equalsIgnoreCase("vacio")
				|| c.getEditorial() == null || c.getEditorial().isEmpty() || c.getEditorial().equalsIgnoreCase("vacio")
				|| c.getFormato() == null || c.getFormato().isEmpty() || c.getFormato().equalsIgnoreCase("vacio")
				|| c.getProcedencia() == null || c.getProcedencia().isEmpty()
				|| c.getProcedencia().equalsIgnoreCase("vacio") || c.getFecha() == null || c.getFecha().isEmpty()
				|| c.getGuionista() == null || c.getGuionista().isEmpty() || c.getGuionista().equalsIgnoreCase("vacio")
				|| c.getDibujante() == null || c.getDibujante().isEmpty() || c.getDibujante().equalsIgnoreCase("vacio")
				|| c.getEstado() == null || c.getEstado().isEmpty() || c.getEstado().equalsIgnoreCase("vacio")
				|| c.getValorGradeo() == null || c.getValorGradeo().isEmpty()
				|| c.getValorGradeo().equalsIgnoreCase("vacio") || c.getUrlReferencia() == null
				|| c.getUrlReferencia().isEmpty() || c.getUrlReferencia().equalsIgnoreCase("vacio")
				|| c.getprecioCarta() == null || c.getprecioCarta().isEmpty()
				|| c.getprecioCarta().equalsIgnoreCase("vacio") || c.getcodigoCarta() == null) {

			String mensajePront = "Revisa la lista, algunos comics estan mal rellenados.";
			AlarmaList.mostrarMensajePront(mensajePront, false, referenciaVentana.getProntInfo());

			return;
		}
	}

	/**
	 * Borra los datos del cómic
	 */
	public static void limpiarAutorellenos(boolean esPrincipal) {

		if (esPrincipal) {
			return;
		}

		referenciaVentana.getNombreCarta().setText("");
		referenciaVentana.getNumeroCarta().setValue("");
		referenciaVentana.getNumeroCarta().getEditor().setText("");

		referenciaVentana.getVarianteCarta().setText("");
		referenciaVentana.getFirmaCarta().setText("");
		referenciaVentana.getEditorialCarta().setText("");

		referenciaVentana.getFormatoCarta().setValue("");
		referenciaVentana.getFormatoCarta().getEditor().setText("");

		referenciaVentana.getProcedenciaCarta().setValue("");
		referenciaVentana.getProcedenciaCarta().getEditor().setText("");

		referenciaVentana.getFechaCarta().setValue(null);
		referenciaVentana.getGuionistaCarta().setText("");
		referenciaVentana.getDibujanteCarta().setText("");
		referenciaVentana.getNombreKeyIssue().setText("");
		referenciaVentana.getFechaCarta().setValue(null);

		referenciaVentana.getPrecioCarta().setText("");
		referenciaVentana.getBusquedaCodigo().setText("");
		referenciaVentana.getCodigoCartaTratar().setText("");
		referenciaVentana.getUrlReferencia().setText("");

		referenciaVentana.getNombreKeyIssue().setText("");
		referenciaVentana.getDireccionImagen().setText("");
		referenciaVentana.getImagencomic().setImage(null);
		referenciaVentana.getCodigoCartaTratar().setText("");
		referenciaVentana.getIdCartaTratar().setText("");

		if ("aniadir".equals(AccionFuncionesComunes.TIPO_ACCION)) {
			referenciaVentana.getIdCartaTratar().setDisable(false);
			referenciaVentana.getIdCartaTratar().setText("");
			referenciaVentana.getIdCartaTratar().setDisable(true);
		}

		referenciaVentana.getFormatoCarta().getSelectionModel().selectFirst();
		referenciaVentana.getProcedenciaCarta().getSelectionModel().selectFirst();
		referenciaVentana.getEstadoCarta().getSelectionModel().selectFirst();
		referenciaVentana.getGradeoCarta().getSelectionModel().selectFirst();

		referenciaVentana.getProntInfo().setText(null);
		referenciaVentana.getProntInfo().setOpacity(0);
		referenciaVentana.getProntInfo().setStyle("");
		validarCamposClave(true);
	}

	public static void borrarDatosGraficos() {
		referenciaVentana.getProntInfo().setText(null);
		referenciaVentana.getProntInfo().setOpacity(0);
		referenciaVentana.getProntInfo().setStyle("");
	}

	/**
	 * Asigna tooltips a varios elementos en la interfaz gráfica. Estos tooltips
	 * proporcionan información adicional cuando el usuario pasa el ratón sobre los
	 * elementos.
	 */
	public static void establecerTooltips() {
		Platform.runLater(() -> {
			Map<Node, String> tooltipsMap = new HashMap<>();

			tooltipsMap.put(referenciaVentana.getNombreCarta(), "Nombre de los cómics / libros / mangas");
			tooltipsMap.put(referenciaVentana.getNumeroCarta(), "Número del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getVarianteCarta(), "Nombre de la variante del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getBotonLimpiar(), "Limpia la pantalla y reinicia todos los valores");
			tooltipsMap.put(referenciaVentana.getBotonbbdd(), "Botón para acceder a la base de datos");
			tooltipsMap.put(referenciaVentana.getBotonSubidaPortada(), "Botón para subir una portada");
			tooltipsMap.put(referenciaVentana.getBotonEliminar(), "Botón para eliminar un cómic");
			tooltipsMap.put(referenciaVentana.getBotonVender(), "Botón para vender un cómic");
			tooltipsMap.put(referenciaVentana.getBotonParametroCarta(),
					"Botón para buscar un cómic mediante una lista de parámetros");
			tooltipsMap.put(referenciaVentana.getBotonModificarCarta(), "Botón para modificar un cómic");
			tooltipsMap.put(referenciaVentana.getBotonBorrarOpinion(), "Botón para borrar una opinión");
			tooltipsMap.put(referenciaVentana.getBotonAgregarPuntuacion(), "Botón para agregar una puntuación");
			tooltipsMap.put(referenciaVentana.getPuntuacionMenu(), "Selecciona una puntuación en el menú");

			tooltipsMap.put(referenciaVentana.getNombreFirma(), "Nombre de la firma del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getNombreGuionista(), "Nombre del guionista del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getNombreProcedencia(),
					"Nombre de la procedencia del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getNombreFormato(), "Nombre del formato del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getNombreEditorial(), "Nombre de la editorial del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getNombreDibujante(), "Nombre del dibujante del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getFechaPublicacion(), "Fecha del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getBusquedaGeneral(),
					"Puedes buscar de forma general los cómic / libro / manga / artistas / guionistas");
			tooltipsMap.put(referenciaVentana.getGradeoCarta(), "Gradeo del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getBotonIntroducir(),
					"Realizar una acción de introducción del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getBotonModificar(),
					"Realizar una acción de modificación del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getBotonEliminar(),
					"Realizar una acción de eliminación del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getBotonAgregarPuntuacion(),
					"Abrir una ventana para agregar puntuación del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getBotonMostrarParametro(),
					"Buscar por parámetros según los datos rellenados");

			FuncionesTooltips.assignTooltips(tooltipsMap);
		});
	}

	public static void listas_autocompletado() {
		FuncionesManejoFront.asignarAutocompletado(referenciaVentana.getNombreCarta(), ListasCartasDAO.listaNombre);
		FuncionesManejoFront.asignarAutocompletado(referenciaVentana.getVarianteCarta(), ListasCartasDAO.listaVariante);
		FuncionesManejoFront.asignarAutocompletado(referenciaVentana.getFirmaCarta(), ListasCartasDAO.listaFirma);
		FuncionesManejoFront.asignarAutocompletado(referenciaVentana.getEditorialCarta(),
				ListasCartasDAO.listaEditorial);
		FuncionesManejoFront.asignarAutocompletado(referenciaVentana.getGuionistaCarta(),
				ListasCartasDAO.listaGuionista);
		FuncionesManejoFront.asignarAutocompletado(referenciaVentana.getDibujanteCarta(),
				ListasCartasDAO.listaDibujante);
		FuncionesManejoFront.asignarAutocompletado(referenciaVentana.getNumeroCarta().getEditor(),
				ListasCartasDAO.listaNumeroCarta);
	}

	public static void controlarEventosInterfaz() {

		referenciaVentana.getProntInfo().textProperty().addListener((observable, oldValue, newValue) -> {
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

		referenciaVentana.getImagencomic().imageProperty().addListener((observable, oldImage, newImage) -> {
			if (newImage != null) {
				// Cambiar la apariencia del cursor y la opacidad cuando la imagen se ha cargado
				referenciaVentana.getImagencomic().setOnMouseEntered(e -> {
					referenciaVentana.getImagencomic().setOpacity(0.7); // Cambiar la opacidad para indicar que es
					// clickable
					referenciaVentana.getImagencomic().setCursor(Cursor.HAND);
				});

				// Restaurar el cursor y la opacidad al salir del ImageView
				referenciaVentana.getImagencomic().setOnMouseExited(e -> {
					referenciaVentana.getImagencomic().setOpacity(1.0); // Restaurar la opacidad
					referenciaVentana.getImagencomic().setCursor(Cursor.DEFAULT);
				});
			} else {
				// Restaurar el cursor y la opacidad al salir del ImageView
				referenciaVentana.getImagencomic().setOnMouseEntered(e -> {
					referenciaVentana.getImagencomic().setCursor(Cursor.DEFAULT);
				});
			}
		});

	}

	public static void controlarEventosInterfazAccion() {
		controlarEventosInterfaz();

		// Establecemos un evento para detectar cambios en el segundo TextField
		referenciaVentana.getIdCartaTratar().textProperty().addListener((observable, oldValue, newValue) -> {
			AccionSeleccionar.mostrarCarta(referenciaVentana.getIdCartaTratar().getText(), false);
		});
	}

	public static void controlarEventosInterfazPrincipal(AccionReferencias referenciaVentana) {
		controlarEventosInterfaz();

		referenciaVentana.getTablaBBDD().getSelectionModel().selectedItemProperty()
				.addListener((obs, oldSelection, newSelection) -> {

					if (newSelection != null) {
						Carta idRow = referenciaVentana.getTablaBBDD().getSelectionModel().getSelectedItem();

						if (idRow != null) {
							referenciaVentana.getBotonGuardarResultado().setVisible(true);
							referenciaVentana.getBotonGuardarResultado().setDisable(false);
						}
					}
				});

		ListasCartasDAO.comicsGuardadosList.addListener((ListChangeListener.Change<? extends Carta> change) -> {
			while (change.next()) {
				if (!ListasCartasDAO.comicsGuardadosList.isEmpty()) {
					referenciaVentana.getBotonImprimir().setVisible(true);
					referenciaVentana.getBotonImprimir().setDisable(false);
				}
			}
		});

		// Establecer un Listener para el tamaño del AnchorPane
		referenciaVentana.getRootAnchorPane().widthProperty().addListener((observable, oldValue, newValue) -> {

			FuncionesTableView.ajustarAnchoVBox();
			FuncionesTableView.seleccionarRaw();
			FuncionesTableView.modificarColumnas(true);
		});

		referenciaVentana.getBotonGuardarResultado().setOnMousePressed(event -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				// Si la lista está vacía, oculta el botón
				referenciaVentana.getBotonMostrarGuardados().setVisible(true);
			}
		});
	}

	public static Carta camposCarta(List<String> camposCarta, boolean esAccion) {
		Carta comic = new Carta();

		// Asignar los valores a las variables correspondientes
		String nombreCarta = camposCarta.get(0);
		String numeroCarta = camposCarta.get(1);
		String varianteCarta = camposCarta.get(2);
		String procedenciaCarta = camposCarta.get(3);
		String formatoCarta = camposCarta.get(4);
		String dibujanteCarta = camposCarta.get(5);
		String guionistaCarta = camposCarta.get(6);
		String editorialCarta = camposCarta.get(7);
		String firmaCarta = camposCarta.get(8);
		String valorGradeoCarta = camposCarta.get(9);
		LocalDate fecha = referenciaVentana.getFechaCarta().getValue();
		String fechaCarta = (fecha != null) ? fecha.toString() : "";
		String direccionImagen = "";
		String estadoCarta = "";
		String nombreKeyIssue = "";
		String precioCarta = "";
		String urlReferencia = "";
		String codigoCartaTratar = "";
		String idCartaTratar = "";

		if (esAccion) {
			direccionImagen = camposCarta.get(10);
			estadoCarta = camposCarta.get(11);
			nombreKeyIssue = camposCarta.get(12);
			precioCarta = camposCarta.get(13);
			urlReferencia = camposCarta.get(14);
			codigoCartaTratar = camposCarta.get(15);
			idCartaTratar = camposCarta.get(16);
		}

		comic.setNombre(Utilidades.defaultIfNullOrEmpty(nombreCarta, ""));

		comic.setNumero(Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(numeroCarta), ""));
		comic.setVariante(Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(varianteCarta), ""));
		comic.setFirma(Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(firmaCarta), ""));
		comic.setEditorial(Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(editorialCarta), ""));
		comic.setFormato(Utilidades.defaultIfNullOrEmpty(formatoCarta, ""));
		comic.setProcedencia(Utilidades.defaultIfNullOrEmpty(procedenciaCarta, ""));
		comic.setFecha(fechaCarta);
		comic.setGuionista(Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(guionistaCarta), ""));
		comic.setDibujante(Utilidades.defaultIfNullOrEmpty(Utilidades.comaYGuionPorEspaciado(dibujanteCarta), ""));
		comic.setValorGradeo(Utilidades.defaultIfNullOrEmpty(valorGradeoCarta, ""));
		comic.setImagen(Utilidades.defaultIfNullOrEmpty(direccionImagen, ""));
		comic.setEstado(Utilidades.defaultIfNullOrEmpty(estadoCarta, ""));
		comic.setkeyIssue(Utilidades.defaultIfNullOrEmpty(nombreKeyIssue, ""));
		comic.setprecioCarta(Utilidades.defaultIfNullOrEmpty(precioCarta, ""));
		comic.seturlReferencia(Utilidades.defaultIfNullOrEmpty(urlReferencia, ""));

		comic.setcodigoCarta(Utilidades.eliminarEspacios(codigoCartaTratar));
		comic.setID(Utilidades.defaultIfNullOrEmpty(idCartaTratar, ""));

		return comic;
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

	public static Carta comicModificado() {

		String id_comic = referenciaVentana.getIdCartaTratar().getText();

		Carta comicTemp = CartaManagerDAO.comicDatos(id_comic);

		List<String> controls = new ArrayList<>();

		for (Control control : referenciaVentana.getListaTextFields()) {
			if (control instanceof TextField) {
				controls.add(((TextField) control).getText()); // Add the Control object itself
			} else if (control instanceof ComboBox<?>) {
				Object selectedItem = ((ComboBox<?>) control).getSelectionModel().getSelectedItem();
				if (selectedItem != null) {
					controls.add(selectedItem.toString());
				} else {
					controls.add(""); // Add the Control object itself
				}
			}
		}

		Carta datos = camposCarta(controls, true);

		Carta comicModificado = new Carta();
		comicModificado.setID(comicTemp.getid());
		comicModificado.setNombre(Utilidades.defaultIfNullOrEmpty(datos.getNombre(), comicTemp.getNombre()));
		comicModificado.setNumero(Utilidades.defaultIfNullOrEmpty(datos.getNumero(), comicTemp.getNumero()));
		comicModificado.setVariante(Utilidades.defaultIfNullOrEmpty(datos.getVariante(), comicTemp.getVariante()));
		comicModificado.setFirma(Utilidades.defaultIfNullOrEmpty(datos.getFirma(), comicTemp.getFirma()));
		comicModificado.setEditorial(Utilidades.defaultIfNullOrEmpty(datos.getEditorial(), comicTemp.getEditorial()));
		comicModificado.setFormato(Utilidades.defaultIfNullOrEmpty(datos.getFormato(), comicTemp.getFormato()));
		comicModificado
				.setProcedencia(Utilidades.defaultIfNullOrEmpty(datos.getProcedencia(), comicTemp.getProcedencia()));
		comicModificado.setFecha(Utilidades.defaultIfNullOrEmpty(datos.getFecha(), comicTemp.getFecha()));
		comicModificado.setGuionista(Utilidades.defaultIfNullOrEmpty(datos.getGuionista(), comicTemp.getGuionista()));
		comicModificado.setDibujante(Utilidades.defaultIfNullOrEmpty(datos.getDibujante(), comicTemp.getDibujante()));
		comicModificado.setImagen(Utilidades.defaultIfNullOrEmpty(datos.getImagen(), comicTemp.getImagen()));
		comicModificado.setEstado(Utilidades.defaultIfNullOrEmpty(datos.getEstado(), comicTemp.getEstado()));
		comicModificado
				.setValorGradeo(Utilidades.defaultIfNullOrEmpty(datos.getValorGradeo(), comicTemp.getValorGradeo()));
		comicModificado.setPuntuacion(
				comicTemp.getPuntuacion().equals("Sin puntuar") ? "Sin puntuar" : comicTemp.getPuntuacion());

		String key_issue_sinEspacios = datos.getkeyIssue().trim();

		if (!key_issue_sinEspacios.isEmpty() && key_issue_sinEspacios.matches(".*\\w+.*")) {
			comicModificado.setkeyIssue(key_issue_sinEspacios);
		} else if (comicTemp != null && comicTemp.getkeyIssue() != null && !comicTemp.getkeyIssue().isEmpty()) {
			comicModificado.setkeyIssue(comicTemp.getkeyIssue());
		}

		String urlReferencia = Utilidades.defaultIfNullOrEmpty(datos.getUrlReferencia(), comicTemp.getUrlReferencia());
		comicModificado.seturlReferencia(urlReferencia.isEmpty() ? "Sin referencia" : urlReferencia);

		String precioCarta = Utilidades.defaultIfNullOrEmpty(datos.getprecioCarta(), comicTemp.getprecioCarta());
		comicModificado.setprecioCarta(
				String.valueOf(Utilidades.convertirMonedaADolar(comicModificado.getProcedencia(), precioCarta)));

		comicModificado
				.setcodigoCarta(Utilidades.defaultIfNullOrEmpty(datos.getcodigoCarta(), comicTemp.getcodigoCarta()));

		return comicModificado;
	}

	public static AccionReferencias getReferenciaVentana() {
		return referenciaVentana;
	}

	public static void setReferenciaVentana(AccionReferencias referenciaVentana) {
		AccionControlUI.referenciaVentana = referenciaVentana;
	}

}
