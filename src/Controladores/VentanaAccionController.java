
package Controladores;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;

import alarmas.AlarmaList;
import cartaManagement.Carta;
import dbmanager.ConectManager;
import dbmanager.DBUtilidades;
import dbmanager.ListasCartasDAO;
import funcionesAuxiliares.Utilidades;
import funcionesAuxiliares.Ventanas;
import funcionesInterfaz.AccionControlUI;
import funcionesInterfaz.FuncionesComboBox;
import funcionesInterfaz.FuncionesManejoFront;
import funcionesInterfaz.FuncionesTableView;
import funcionesManagment.AccionAniadir;
import funcionesManagment.AccionEliminar;
import funcionesManagment.AccionFuncionesComunes;
import funcionesManagment.AccionModificar;
import funcionesManagment.AccionReferencias;
import funcionesManagment.AccionSeleccionar;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Clase controladora para la ventana de acciones, que gestiona la interfaz de
 * usuario y las operaciones relacionadas con los cómics.
 */
public class VentanaAccionController implements Initializable {

	@FXML
	private VBox rootVBox;
	@FXML
	private VBox vboxImage;

	@FXML
	private Label alarmaConexionInternet;
	@FXML
	private Label alarmaConexionSql;
	@FXML
	private Label labelCodigo;
	@FXML
    private Label labelEstado;
    @FXML
    private Label labelFoil;
    @FXML
    private Label labelGradeo;
    @FXML
    private Label labelId;
    @FXML
    private Label labelNormas;
    @FXML
    private Label labelPortada;
    @FXML
    private Label labelPrecio;
    @FXML
    private Label labelRareza;
    @FXML
    private Label labelReferencia;
    @FXML
    private Label labelcoleccion;

	@FXML
	private Button botonBusquedaAvanzada;
	@FXML
	private Button botonBusquedaCodigo;
	@FXML
	private Button botonCancelarSubida;
	@FXML
	private Button botonEliminar;
	@FXML
	private Button botonEliminarImportadoCarta;
	@FXML
	private Button botonGuardarCambioCarta;
	@FXML
	private Button botonGuardarCarta;
	@FXML
	private Button botonLimpiar;
	@FXML
	private Button botonModificarCarta;
	@FXML
	private Button botonParametroCarta;
	@FXML
	private Button botonSubidaPortada;
	@FXML
	private Button botonVender;
	@FXML
	private Button botonbbdd;

	@FXML
	private TextField busquedaCodigo;
	@FXML
	private TextField firmaComic;
	@FXML
	private TextField textFieldDireccionPortada;
	@FXML
	private TextField textFieldEditorialCarta;
	@FXML
	private TextField textFieldIdCarta;
	@FXML
	private TextField textFieldNombreCarta;
	@FXML
	private TextField textFieldNormasCarta;
	@FXML
	private TextField textFieldPrecioCarta;
	@FXML
	private TextField textFieldRarezaCarta;
	@FXML
	private TextField textFieldUrlCarta;

	@FXML
	private ComboBox<String> comboboxEsFoilCarta;
	@FXML
	private ComboBox<String> comboboxEstadoCarta;
	@FXML
	private ComboBox<String> comboboxGradeoCarta;
	@FXML
	private ComboBox<String> comboboxNumeroCarta;

	@FXML
	private ImageView cargaImagen;
	@FXML
	private ImageView imagencarta;

	@FXML
	private TableView<Carta> tablaBBDD;

	@FXML
	private TableColumn<Carta, String> columnaColeccion;
	@FXML
	private TableColumn<Carta, String> columnaEditorial;
	@FXML
	private TableColumn<Carta, String> columnaNombre;
	@FXML
	private TableColumn<Carta, String> columnaPrecio;
	@FXML
	private TableColumn<Carta, String> columnaRareza;
	@FXML
	private TableColumn<Carta, String> fecha;
	@FXML
	private TableColumn<Carta, String> firma;
	@FXML
	private TableColumn<Carta, String> formato;
	@FXML
	private TableColumn<Carta, String> gradeo;
	@FXML
	private TableColumn<Carta, String> id;
	@FXML
	private TableColumn<Carta, String> numero;
	@FXML
	private TableColumn<Carta, String> procedencia;
	@FXML
	private TableColumn<Carta, String> referencia;

	@FXML
	private TextArea prontInfo;

	@FXML
	private ProgressIndicator progresoCarga;

	@FXML
	private MenuItem menuCodigoBarras;
	@FXML
	private MenuItem menuImportarFichero;
	@FXML
	private MenuItem navegacionMostrarEstadistica;

	@FXML
	private MenuBar menuNavegacion;

	@FXML
	private Menu navegacionEstadistica;
	@FXML
	private Menu navegacionOpciones;

	/**
	 * Referencia a la ventana (stage).
	 */
	private Stage stage;

	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();

	public static final AlarmaList alarmaList = new AlarmaList();

	public static final AccionReferencias referenciaVentana = new AccionReferencias();

	public static AccionReferencias referenciaVentanaPrincipal = new AccionReferencias();

	public AccionReferencias guardarReferencia() {
		referenciaVentana.setAlarmaConexionInternet(alarmaConexionInternet);
		referenciaVentana.setAlarmaConexionSql(alarmaConexionSql);
		referenciaVentana.setDireccionImagen(direccionImagen);
		referenciaVentana.setID(id);
		referenciaVentana.setGradeo(gradeo);
		referenciaVentana.setEditorial(editorial);
		referenciaVentana.setFormato(formato);
		referenciaVentana.setNombre(nombre);
		referenciaVentana.setNumero(numero);
		referenciaVentana.setProcedencia(procedencia);
		referenciaVentana.setReferencia(referencia);
		referenciaVentana.setBotonCancelarSubida(botonCancelarSubida);
		referenciaVentana.setBotonBorrarOpinion(botonBorrarOpinion);
		referenciaVentana.setBotonBusquedaCodigo(botonBusquedaCodigo);
		referenciaVentana.setBotonBusquedaAvanzada(botonBusquedaAvanzada);
		referenciaVentana.setBotonEliminar(botonEliminar);
		referenciaVentana.setBotonLimpiar(botonLimpiar);
		referenciaVentana.setBotonModificarCarta(botonModificarCarta);
		referenciaVentana.setBotonParametroCarta(botonParametroCarta);
		referenciaVentana.setBotonVender(botonVender);
		referenciaVentana.setBotonbbdd(botonbbdd);
		referenciaVentana.setBotonGuardarCarta(botonGuardarCarta);
		referenciaVentana.setBotonGuardarCambioCarta(botonGuardarCambioCarta);
		referenciaVentana.setBotonEliminarImportadoCarta(botonEliminarImportadoCarta);
		referenciaVentana.setBotonSubidaPortada(botonSubidaPortada);
		referenciaVentana.setBusquedaCodigo(busquedaCodigo);
		referenciaVentana.setEditorialCarta(editorialCarta);
		referenciaVentana.setNombreCarta(nombreCarta);
		referenciaVentana.setPrecioCarta(precioCarta);
		referenciaVentana.setUrlReferencia(urlReferencia);
		referenciaVentana.setEstadoCarta(estadoCarta);
		referenciaVentana.setFormatoCarta(formatoCarta);
		referenciaVentana.setGradeoCarta(gradeoCarta);
		referenciaVentana.setNumeroCarta(numeroCarta);
		referenciaVentana.setIdCartaTratar(idCartaTratar);
		referenciaVentana.setStage(estadoStage());
		referenciaVentana.setProgresoCarga(progresoCarga);

		referenciaVentana.setLabel_gradeo(labelGradeo);
		referenciaVentana.setLabel_editorial(labelEditorial);
		referenciaVentana.setLabel_estado(labelEstado);
		referenciaVentana.setLabel_fecha(labelFecha);
		referenciaVentana.setLabel_formato(labelFormato);
		referenciaVentana.setLabel_id_mod(labelId);
		referenciaVentana.setLabel_portada(labelPortada);
		referenciaVentana.setLabel_precio(labelPrecio);
		referenciaVentana.setLabel_referencia(labelReferencia);

		referenciaVentana.setTablaBBDD(tablaBBDD);
		referenciaVentana.setImagencomic(imagencomic);
		referenciaVentana.setCargaImagen(cargaImagen);
		referenciaVentana.setProntInfo(prontInfo);
		referenciaVentana.setRootVBox(rootVBox);
		referenciaVentana.setMenu_Importar_Fichero_CodigoBarras(menuImportarFichero);
		referenciaVentana.setMenu_estadistica_estadistica(navegacionMostrarEstadistica);
		referenciaVentana.setMenu_navegacion(menuNavegacion);
		referenciaVentana.setNavegacion_Opciones(navegacionOpciones);
		referenciaVentana.setNavegacion_estadistica(navegacionEstadistica);

		referenciaVentana.setComboBoxes(
				Arrays.asList(formatoCarta, gradeoCarta, numeroCarta, procedenciaCarta, estadoCarta, puntuacionMenu));

		referenciaVentana.setListaTextFields(FXCollections.observableArrayList(
				Arrays.asList(nombreCarta, numeroCarta, varianteCarta, procedenciaCarta, formatoCarta, dibujanteCarta,
						guionistaCarta, editorialCarta, firmaCarta, gradeoCarta, direccionImagen, estadoCarta,
						nombreKeyIssue, precioCarta, urlReferencia, codigoCartaTratar, idCartaTratar)));

		AccionReferencias.setColumnasTabla(Arrays.asList(nombre, gradeo, variante, editorial, guionista, dibujante));

		return referenciaVentana;
	}

	public void enviarReferencias() {
		AccionControlUI.setReferenciaVentana(guardarReferencia());
		AccionFuncionesComunes.setReferenciaVentana(guardarReferencia());
		AccionFuncionesComunes.setReferenciaVentanaPrincipal(referenciaVentanaPrincipal);
		FuncionesTableView.setReferenciaVentana(guardarReferencia());
		FuncionesManejoFront.setReferenciaVentana(guardarReferencia());

		AccionSeleccionar.setReferenciaVentana(guardarReferencia());
		AccionAniadir.setReferenciaVentana(guardarReferencia());
		AccionEliminar.setReferenciaVentana(guardarReferencia());
		AccionModificar.setReferenciaVentana(guardarReferencia());
		Utilidades.setReferenciaVentana(guardarReferencia());
		Ventanas.setReferenciaVentana(guardarReferencia());
		DBUtilidades.setReferenciaVentana(guardarReferencia());
	}

	/**
	 * Inicializa la interfaz de usuario y configura el comportamiento de los
	 * elementos al cargar la vista.
	 *
	 * @param location  La ubicación relativa del archivo FXML.
	 * @param resources Los recursos que pueden ser utilizados por el controlador.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		menuImportarFichero.setGraphic(Utilidades.createIcon("/Icono/Archivo/importar.png", 16, 16));

		navegacionMostrarEstadistica.setGraphic(Utilidades.createIcon("/Icono/Estadistica/descarga.png", 16, 16));

		alarmaList.setAlarmaConexionSql(alarmaConexionSql);
		alarmaList.setAlarmaConexionInternet(alarmaConexionInternet);
		alarmaList.iniciarThreadChecker();

		Platform.runLater(() -> {

			enviarReferencias();

			AccionControlUI.controlarEventosInterfazAccion();

			AccionControlUI.autocompletarListas();

			rellenarCombosEstaticos();

			AccionControlUI.mostrarOpcion(AccionFuncionesComunes.getTipoAccion());

			FuncionesManejoFront.getStageVentanas().add(estadoStage());

			estadoStage().setOnCloseRequest(event -> stop());

		});

		ListasCartasDAO.cartasImportados.clear();

		AccionControlUI.establecerTooltips();

		formatearTextField();

	}

	@FXML
	void ampliarImagen(MouseEvent event) {

		Carta idRow = tablaBBDD.getSelectionModel().getSelectedItem();

		if (idRow != null) {

			ImagenAmpliadaController.cartaInfo = idRow;

			nav.verVentanaImagen();
		}
	}

	/**
	 * Rellena los combos estáticos en la interfaz. Esta función llena los
	 * ComboBoxes con opciones estáticas predefinidas.
	 */
	public void rellenarCombosEstaticos() {
		FuncionesComboBox.rellenarComboBoxEstaticos(referenciaVentana.getListaComboboxes());
	}

	public void formatearTextField() {
		FuncionesManejoFront.eliminarEspacioInicialYFinal(nombreCarta);
		FuncionesManejoFront.eliminarSimbolosEspeciales(nombreCarta);
		FuncionesManejoFront.restringirSimbolos(editorialCarta);
		FuncionesManejoFront.restringirSimbolos(guionistaCarta);
		FuncionesManejoFront.restringirSimbolos(dibujanteCarta);
		FuncionesManejoFront.restringirSimbolos(varianteCarta);

		FuncionesManejoFront.reemplazarEspaciosMultiples(nombreCarta);
		FuncionesManejoFront.reemplazarEspaciosMultiples(editorialCarta);
		FuncionesManejoFront.reemplazarEspaciosMultiples(guionistaCarta);
		FuncionesManejoFront.reemplazarEspaciosMultiples(dibujanteCarta);
		FuncionesManejoFront.reemplazarEspaciosMultiples(varianteCarta);

		FuncionesManejoFront.permitirUnSimbolo(nombreCarta);
		FuncionesManejoFront.permitirUnSimbolo(editorialCarta);
		FuncionesManejoFront.permitirUnSimbolo(guionistaCarta);
		FuncionesManejoFront.permitirUnSimbolo(dibujanteCarta);
		FuncionesManejoFront.permitirUnSimbolo(varianteCarta);
		FuncionesManejoFront.permitirUnSimbolo(busquedaCodigo);

		numeroCarta.getEditor().setTextFormatter(FuncionesComboBox.validador_Nenteros());
		idCartaTratar.setTextFormatter(FuncionesComboBox.validador_Nenteros());
		precioCarta.setTextFormatter(FuncionesComboBox.validador_Ndecimales());

		if (AccionFuncionesComunes.TIPO_ACCION.equalsIgnoreCase("aniadir")) {
			idCartaTratar.setTextFormatter(FuncionesComboBox.desactivarValidadorNenteros());
		}
	}

	/**
	 * Metodo que mostrara los cartas o comic buscados por parametro
	 *
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void mostrarPorParametro(ActionEvent event) {
		enviarReferencias();

		List<String> controls = new ArrayList<>();

		for (Control control : AccionReferencias.getListaTextFields()) {
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

		Carta comic = AccionControlUI.camposCarta(controls, true);

		AccionSeleccionar.verBasedeDatos(false, true, comic);

	}

	/**
	 * Metodo que muestra toda la base de datos.
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	void verTodabbdd(ActionEvent event) throws IOException, SQLException {
		enviarReferencias();
		AccionControlUI.limpiarAutorellenos(false);

		AccionSeleccionar.verBasedeDatos(true, true, null);
	}

	/**
	 * Llamada a funcion que modifica los datos de 1 comic en la base de datos.
	 *
	 * @param event
	 * @throws Exception
	 * @throws SQLException
	 * @throws NumberFormatException
	 * @throws IOException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@FXML
	void modificarDatos(ActionEvent event) throws Exception {
		enviarReferencias();
		nav.cerrarMenuOpciones();
		AccionModificar.modificarCarta();
		rellenarCombosEstaticos();
	}

	@FXML
	void eliminarCartaSeleccionado(ActionEvent event) {
		enviarReferencias();
		nav.cerrarMenuOpciones();
		AccionEliminar.eliminarCartaLista();
		rellenarCombosEstaticos();
	}

	/**
	 * Funcion que permite mostrar la imagen de portada cuando clickeas en una
	 * tabla.
	 *
	 * @param event
	 */
	@FXML
	void clickRaton(MouseEvent event) {
		enviarReferencias();
		AccionSeleccionar.seleccionarCartas(false);
	}

	/**
	 * Funcion que permite mostrar la imagen de portada cuando usas las teclas de
	 * direccion en una tabla.
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	void teclasDireccion(KeyEvent event) {
		if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
			enviarReferencias();
			AccionSeleccionar.seleccionarCartas(false);
		}
	}

	/**
	 * Maneja la acción de búsqueda avanzada, verifica las claves API de Marvel y
	 * Carta Vine.
	 *
	 * @param event El evento de acción.
	 */
	@FXML
	void busquedaAvanzada(ActionEvent event) {
		enviarReferencias();

		// Verificar si las claves API están ausentes o vacías
		if (!Utilidades.isInternetAvailable()) {
			nav.alertaException("Revisa las APIS de Marvel y Vine, estan incorrectas o no funcionan");
		} else {
			// Continuar con la lógica cuando ambas claves están presente
			AccionFuncionesComunes.cambiarVisibilidadAvanzada();
		}
	}

	/**
	 * Método asociado al evento de acción que se dispara al seleccionar la opción
	 * "Importar Fichero Código de Barras". Este método aún no tiene implementación.
	 *
	 * @param evento Objeto que representa el evento de acción.
	 */
	@FXML
	void importarFicheroCodigoBarras(ActionEvent evento) {
		enviarReferencias();
		if (Utilidades.isInternetAvailable()) {
			nav.cerrarMenuOpciones();
			AccionControlUI.limpiarAutorellenos(false);
			AccionControlUI.borrarDatosGraficos();
			String frase = "Fichero txt";

			String formato = "*.txt";
			File fichero = Utilidades.tratarFichero(frase, formato, false);

			// Verificar si se obtuvo un objeto FileChooser válido
			if (fichero != null) {
				enviarReferencias();
				rellenarCombosEstaticos();
				AccionFuncionesComunes.busquedaPorCodigoImportacion(fichero);
			}
		}

	}

	/**
	 * Realiza una búsqueda por código y muestra información del cómic
	 * correspondiente en la interfaz gráfica.
	 *
	 * @param event El evento que desencadena la acción.
	 * @throws IOException        Si ocurre un error de entrada/salida.
	 * @throws JSONException      Si ocurre un error al procesar datos JSON.
	 * @throws URISyntaxException Si ocurre un error de sintaxis de URI.
	 */
	@FXML
	void busquedaPorCodigo(ActionEvent event) {
		enviarReferencias();
		nav.cerrarMenuOpciones();
		Platform.runLater(() -> {
			try {
				if (!Utilidades.isInternetAvailable()) {
					return;
				}

				String valorCodigo = Utilidades.eliminarEspacios(busquedaCodigo.getText());

				if (valorCodigo.isEmpty()) {
					return;
				}

				limpiarUIBeforeSearch();

				Task<Void> tarea = createSearchTask(valorCodigo);

				configureTaskListeners(tarea);

				Thread thread = new Thread(tarea);
				thread.setDaemon(true);
				thread.start();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}

	private void limpiarUIBeforeSearch() {
		AccionControlUI.limpiarAutorellenos(false);
		imagencarta.setImage(null);
		imagencarta.setVisible(true);
		botonCancelarSubida.setVisible(true);
		botonBusquedaCodigo.setDisable(true);
		botonSubidaPortada.setDisable(true);
		referenciaVentana.getMenuImportarFicheroCodigoBarras().setDisable(true);
		AlarmaList.iniciarAnimacionCargaImagen(cargaImagen);
		menuImportarFichero.setDisable(true);
		FuncionesManejoFront.cambiarEstadoMenuBar(true, referenciaVentana);
		rellenarCombosEstaticos();
	}

	private Task<Void> createSearchTask(String valorCodigo) {
		return new Task<>() {
			@Override
			protected Void call() throws Exception {
				if (isCancelled() || !referenciaVentana.getStageVentana().isShowing()) {
					return null;
				}

				if (AccionFuncionesComunes.procesarCartaPorCodigo(valorCodigo)) {
					String mensaje = "Carta encontrado correctamente";
					AlarmaList.mostrarMensajePront(mensaje, true, prontInfo);
				} else {
					String mensaje = "La búsqueda del cómic ha salido mal. Revisa el código";
					AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
					AlarmaList.detenerAnimacionCargaImagen(cargaImagen);
				}
				return null;
			}
		};
	}

	private void configureTaskListeners(Task<Void> tarea) {
		tarea.setOnRunning(ev -> {
			limpiarUIBeforeSearch();
			AccionControlUI.limpiarAutorellenos(false);
			AccionFuncionesComunes.cambiarEstadoBotones(true);
			FuncionesManejoFront.cambiarEstadoMenuBar(true, referenciaVentana);
			FuncionesManejoFront.cambiarEstadoMenuBar(true, referenciaVentanaPrincipal);
			AlarmaList.iniciarAnimacionCarga(progresoCarga);
		});

		tarea.setOnSucceeded(ev -> {
			AlarmaList.detenerAnimacionCargaImagen(cargaImagen);
			AccionFuncionesComunes.cambiarEstadoBotones(false);
			FuncionesManejoFront.cambiarEstadoMenuBar(false, referenciaVentana);
			FuncionesManejoFront.cambiarEstadoMenuBar(false, referenciaVentanaPrincipal);
			menuImportarFichero.setDisable(false);
			updateButtonsVisibility();
			AlarmaList.detenerAnimacionCarga(progresoCarga);
		});

		tarea.setOnCancelled(ev -> {
			String mensaje = "Ha cancelado la búsqueda del cómic";
			AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
			AlarmaList.detenerAnimacionCargaImagen(cargaImagen);
			AccionFuncionesComunes.cambiarEstadoBotones(false);
			FuncionesManejoFront.cambiarEstadoMenuBar(false, referenciaVentana);
			FuncionesManejoFront.cambiarEstadoMenuBar(false, referenciaVentanaPrincipal);

			AlarmaList.detenerAnimacionCarga(progresoCarga);
		});
	}

	private void updateButtonsVisibility() {
		if (!ListasCartasDAO.cartasImportados.isEmpty()) {
			botonEliminarImportadoCarta.setVisible(true);
			botonGuardarCambioCarta.setVisible(true);
			botonGuardarCarta.setVisible(true);
		} else {
			botonEliminarImportadoCarta.setVisible(false);
			botonGuardarCambioCarta.setVisible(false);
			botonGuardarCarta.setVisible(false);
		}
	}

	/**
	 * Limpia los datos de la pantalla al hacer clic en el botón "Limpiar".
	 */
	@FXML
	void limpiarDatos(ActionEvent event) {
		enviarReferencias();
		AccionFuncionesComunes.limpiarDatosPantallaAccion();
		rellenarCombosEstaticos();
	}

	/**
	 * Funcion que permite la subida de una
	 *
	 * @param event
	 */
	@FXML
	void nuevaPortada(ActionEvent event) {
		nav.cerrarMenuOpciones();
		AccionFuncionesComunes.subirPortada();
	}

	/**
	 * Método que maneja el evento de guardar los datos de un cómic.
	 * 
	 * @param event El evento de acción que desencadena la llamada al método.
	 */
	@FXML
	void guardarDatos(ActionEvent event) {
		enviarReferencias();
		rellenarCombosEstaticos();
		nav.cerrarMenuOpciones();
		AccionModificar.actualizarCartaLista();

	}

	/**
	 * Método que maneja el evento de guardar la lista de cómics importados.
	 * 
	 * @param event El evento de acción que desencadena la llamada al método.
	 * @throws IOException        Si ocurre un error de entrada/salida.
	 * @throws SQLException       Si ocurre un error de base de datos.
	 * @throws URISyntaxException
	 */
	@FXML
	void guardarListaImportados(ActionEvent event) throws IOException, SQLException {
		enviarReferencias();
		nav.cerrarMenuOpciones();
		AccionAniadir.guardarContenidoLista();
		rellenarCombosEstaticos();
	}

	/**
	 * Metodo que permite cambiar de estado un comic, para que se deje de mostrar en
	 * el programa, pero este sigue estando dentro de la bbdd
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	void ventaCarta(ActionEvent event) throws SQLException {
		enviarReferencias();
		nav.cerrarMenuOpciones();
		AccionModificar.venderCarta();
		rellenarCombosEstaticos();
	}

	/**
	 * Funcion que elimina un comic de la base de datos.
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@FXML
	void eliminarDatos(ActionEvent event) {
		enviarReferencias();
		nav.cerrarMenuOpciones();
		AccionEliminar.eliminarCarta();
		rellenarCombosEstaticos();

	}

	/**
	 * Establece la instancia de la ventana (Stage) asociada a este controlador.
	 *
	 * @param stage La instancia de la ventana (Stage) que se asocia con este
	 *              controlador.
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Scene miStageVentana() {
		Node rootNode = botonLimpiar;
		while (rootNode.getParent() != null) {
			rootNode = rootNode.getParent();
		}

		if (rootNode instanceof Parent) {
			Scene scene = ((Parent) rootNode).getScene();
			ConectManager.activeScenes.add(scene);
			return scene;
		} else {
			// Manejar el caso en el que no se pueda encontrar un nodo raíz adecuado
			return null;
		}
	}

	public Stage estadoStage() {

		return (Stage) botonLimpiar.getScene().getWindow();
	}

	/**
	 * Cierra la ventana asociada a este controlador, si está disponible. Si no se
	 * ha establecido una instancia de ventana (Stage), este método no realiza
	 * ninguna acción.
	 */
	public void closeWindow() {

		stage = estadoStage();

		if (stage != null) {

			if (FuncionesManejoFront.getStageVentanas().contains(estadoStage())) {
				FuncionesManejoFront.getStageVentanas().remove(estadoStage());
			}
			nav.cerrarCargaCartas();
			stage.close();
		}
	}

	public void stop() {

		if (FuncionesManejoFront.getStageVentanas().contains(estadoStage())) {
			FuncionesManejoFront.getStageVentanas().remove(estadoStage());
		}

		Utilidades.cerrarCargaCartas();
	}

	public static void setReferenciaVentana(AccionReferencias referenciaVentana) {
		VentanaAccionController.referenciaVentanaPrincipal = referenciaVentana;
	}
}
