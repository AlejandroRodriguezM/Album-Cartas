/**
 * Contiene las clases que hacen funcionar las ventanas
 *  
*/
package Controladores;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import alarmas.AlarmaList;
import cartaManagement.Carta;
import dbmanager.CartaManagerDAO;
import dbmanager.ConectManager;
import dbmanager.DBUtilidades;
import dbmanager.DBUtilidades.TipoBusqueda;
import dbmanager.ListasCartasDAO;
import dbmanager.SelectManager;
import ficherosFunciones.FuncionesExcel;
import funcionesAuxiliares.Utilidades;
import funcionesAuxiliares.Ventanas;
import funcionesInterfaz.AccionControlUI;
import funcionesInterfaz.FuncionesComboBox;
import funcionesInterfaz.FuncionesManejoFront;
import funcionesInterfaz.FuncionesTableView;
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
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Esta clase sirve viajar a las diferentes ventanas del programa, asi como
 * realizar diferentes diferentes funciones
 *
 * @author Alejandro Rodriguez
 */
public class MenuPrincipalController implements Initializable {

	@FXML
	private AnchorPane anchoPaneInfo;
	@FXML
	private AnchorPane rootAnchorPane;

	@FXML
	private VBox comboboxVbox;
	@FXML
	private VBox rootVBox;
	@FXML
	private VBox vboxContenido;
	@FXML
	private VBox vboxImage;

	@FXML
	private Label alarmaConexionInternet;

	@FXML
	private ImageView backgroundImage;
	@FXML
	private ImageView imagenCarta;

	@FXML
	private Rectangle barraCambioAltura;

	@FXML
	private Button botonCancelarSubida;
	@FXML
	private Button botonEliminar;
	@FXML
	private Button botonGuardarResultado;
	@FXML
	private Button botonImprimir;
	@FXML
	private Button botonIntroducir;
	@FXML
	private Button botonLimpiar;
	@FXML
	private Button botonModificar;
	@FXML
	private Button botonMostrarGuardados;
	@FXML
	private Button botonMostrarParametro;
	@FXML
	private Button botonbbdd;

	@FXML
	private TextField busquedaGeneral;
	@FXML
	private ComboBox<String> comboboxColeccionCarta;
	@FXML
	private ComboBox<String> comboboxEditorialCarta;
	@FXML
	private ComboBox<String> comboboxEsFoilCarta;
	@FXML
	private ComboBox<String> comboboxEstadoCarta;
	@FXML
	private ComboBox<String> comboboxGradeoCarta;
	@FXML
	private ComboBox<String> comboboxNombreCarta;
	@FXML
	private ComboBox<String> comboboxNumeroCarta;
	@FXML
	private ComboBox<String> comboboxPrecioCarta;
	@FXML
	private ComboBox<String> comboboxRarezaCarta;

	@FXML
	private TableView<Carta> tablaBBDD;

	@FXML
	private TableColumn<Carta, String> columnaColeccion;
	@FXML
	private TableColumn<Carta, String> columnaEditorial;
	@FXML
	private TableColumn<Carta, String> columnaEsFoil;
	@FXML
	private TableColumn<Carta, String> columnaEstado;
	@FXML
	private TableColumn<Carta, String> columnaGradeo;
	@FXML
	private TableColumn<Carta, String> columnaId;
	@FXML
	private TableColumn<Carta, String> columnaNombre;
	@FXML
	private TableColumn<Carta, String> columnaNumero;
	@FXML
	private TableColumn<Carta, String> columnaPrecio;
	@FXML
	private TableColumn<Carta, String> columnaRareza;
	@FXML
	private TableColumn<Carta, String> columnaReferencia;

	@FXML
	private ProgressIndicator progresoCarga;

	@FXML
	private TextArea prontInfo;

	@FXML
	private MenuItem menuArchivoAvanzado;
	@FXML
	private MenuItem menuArchivoCerrar;
	@FXML
	private MenuItem menuArchivoDelete;
	@FXML
	private MenuItem menuArchivoDesconectar;
	@FXML
	private MenuItem menuArchivoExcel;
	@FXML
	private MenuItem menuArchivoImportar;
	@FXML
	private MenuItem menuArchivoSobreMi;
	@FXML
	private MenuItem menuCartaAniadir;
	@FXML
	private MenuItem menuCartaEliminar;
	@FXML
	private MenuItem menuCartaModificar;
	@FXML
	private MenuItem menuEstadisticaComprados;
	@FXML
	private MenuItem menuEstadisticaEstadistica;
	@FXML
	private MenuItem menuEstadisticaKeyIssue;
	@FXML
	private MenuItem menuEstadisticaPosesion;
	@FXML
	private MenuItem menuEstadisticaVendidos;

	@FXML
	private MenuBar menuNavegacion;

	@FXML
	private Menu navegacionCerrar;
	@FXML
	private Menu navegacionCarta;
	@FXML
	private Menu navegacionEstadistica;

	public Carta cartaCache;

	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();

	/**
	 * Instancia de FuncionesComboBox para funciones relacionadas con ComboBox.
	 */
	private static FuncionesComboBox funcionesCombo = new FuncionesComboBox();

	public static final AccionReferencias referenciaVentana = new AccionReferencias();

	public static CompletableFuture<List<Entry<String, String>>> urlPreviews;

	public static final AlarmaList alarmaList = new AlarmaList();

	double y = 0;

	public AccionReferencias guardarReferencia() {
		// Labels
		referenciaVentana.setAlarmaConexionInternet(alarmaConexionInternet);

		// Buttons
		referenciaVentana.setBotonEliminar(botonEliminar);
		referenciaVentana.setBotonLimpiar(botonLimpiar);
		referenciaVentana.setBotonbbdd(botonbbdd);
		referenciaVentana.setBotonMostrarParametro(botonMostrarParametro);
		referenciaVentana.setBotonImprimir(botonImprimir);
		referenciaVentana.setBotonGuardarResultado(botonGuardarResultado);
		referenciaVentana.setBotonMostrarGuardados(botonMostrarGuardados);
		referenciaVentana.setBotonModificar(botonModificar);
		referenciaVentana.setBotonIntroducir(botonIntroducir);
		referenciaVentana.setBotonCancelarSubida(botonCancelarSubida);

		referenciaVentana.setBusquedaGeneralTextField(busquedaGeneral);

		// ImageViews
		referenciaVentana.setImagenCarta(imagenCarta);
		referenciaVentana.setBackgroundImage(backgroundImage);

		// MenuItems
		referenciaVentana.setMenuArchivoCerrar(menuArchivoCerrar);
		referenciaVentana.setMenuArchivoDelete(menuArchivoDelete);
		referenciaVentana.setMenuArchivoDesconectar(menuArchivoDesconectar);
		referenciaVentana.setMenuArchivoExcel(menuArchivoExcel);
		referenciaVentana.setMenuArchivoImportar(menuArchivoImportar);
		referenciaVentana.setMenuArchivoSobreMi(menuArchivoSobreMi);
		referenciaVentana.setMenuCartaAniadir(menuCartaAniadir);
		referenciaVentana.setMenuCartaEliminar(menuCartaEliminar);
		referenciaVentana.setMenuCartaModificar(menuCartaModificar);
		referenciaVentana.setMenuEstadisticaComprados(menuEstadisticaComprados);
		referenciaVentana.setMenuEstadisticaEstadistica(menuEstadisticaEstadistica);
		referenciaVentana.setMenuEstadisticaPosesion(menuEstadisticaPosesion);
		referenciaVentana.setMenuEstadisticaVendidos(menuEstadisticaVendidos);
		referenciaVentana.setMenuArchivoAvanzado(menuArchivoAvanzado);

		// Menus
		referenciaVentana.setMenuNavegacion(menuNavegacion);
		referenciaVentana.setNavegacionCerrar(navegacionCerrar);
		referenciaVentana.setNavegacionCarta(navegacionCarta);
		referenciaVentana.setNavegacionEstadistica(navegacionEstadistica);

		// TableColumns
		referenciaVentana.setiDColumna(columnaId);
		referenciaVentana.setNombreColumna(columnaNombre);
		referenciaVentana.setNumeroColumna(columnaNumero);
		referenciaVentana.setGradeoColumna(columnaGradeo);
		referenciaVentana.setEditorialColumna(columnaEditorial);
		referenciaVentana.setColeccionColumna(columnaColeccion);
		referenciaVentana.setRarezaColumna(columnaRareza);
		referenciaVentana.setEsFoilColumna(columnaEsFoil);
		referenciaVentana.setEstadoColumna(columnaEstado);
		referenciaVentana.setPrecioColumna(columnaPrecio);
		referenciaVentana.setReferenciaColumna(columnaReferencia);

		// ComboBoxes
		referenciaVentana.setNombreCartaCombobox(comboboxNombreCarta);
		referenciaVentana.setNumeroCartaCombobox(comboboxNumeroCarta);
		referenciaVentana.setNombreEditorialCombobox(comboboxEditorialCarta);
		referenciaVentana.setNombreColeccionCombobox(comboboxColeccionCarta);
		referenciaVentana.setNombreRarezaCombobox(comboboxRarezaCarta);
		referenciaVentana.setNombreEsFoilCombobox(comboboxEsFoilCarta);
		referenciaVentana.setGradeoCartaCombobox(comboboxGradeoCarta);
		referenciaVentana.setEstadoCartaCombobox(comboboxEstadoCarta);
		referenciaVentana.setPrecioCartaCombobox(comboboxPrecioCarta);

		// Others
		referenciaVentana.setProntInfoTextArea(prontInfo);
		referenciaVentana.setProgresoCarga(progresoCarga);
		referenciaVentana.setTablaBBDD(tablaBBDD);
		referenciaVentana.setRootVBox(rootVBox);
		referenciaVentana.setVboxContenido(vboxContenido);
		referenciaVentana.setVboxImage(vboxImage);
		referenciaVentana.setAnchoPaneInfo(anchoPaneInfo);
		referenciaVentana.setRootAnchorPane(rootAnchorPane);
		referenciaVentana.setBarraCambioAltura(barraCambioAltura);
		referenciaVentana.setStageVentana(estadoStage());

		// ComboBox List
		referenciaVentana.setListaComboboxes(Arrays.asList(comboboxNombreCarta, comboboxNumeroCarta,
				comboboxEditorialCarta, comboboxColeccionCarta, comboboxRarezaCarta, comboboxEsFoilCarta,
				comboboxGradeoCarta, comboboxEstadoCarta, comboboxPrecioCarta));
		
		

		// FXCollections Lists
		AccionReferencias.setListaElementosFondo(FXCollections.observableArrayList(backgroundImage, menuNavegacion));
		AccionReferencias.setListaBotones(FXCollections.observableArrayList(botonLimpiar, botonMostrarParametro,
				botonbbdd, botonImprimir, botonGuardarResultado, botonCancelarSubida));

		AccionReferencias.setListaColumnasTabla(
				Arrays.asList(columnaNombre, columnaNumero, columnaGradeo, columnaEditorial, columnaColeccion,
						columnaRareza, columnaEsFoil, columnaEstado, columnaId, columnaPrecio, columnaReferencia));

		return referenciaVentana;
	}

	public void enviarReferencias() {
		AccionControlUI.setReferenciaVentana(guardarReferencia());
		AccionFuncionesComunes.setReferenciaVentana(guardarReferencia());
		FuncionesTableView.setReferenciaVentana(guardarReferencia());
		FuncionesManejoFront.setReferenciaVentana(guardarReferencia());
		AccionSeleccionar.setReferenciaVentana(guardarReferencia());
		AccionEliminar.setReferenciaVentana(guardarReferencia());
		AccionModificar.setReferenciaVentana(guardarReferencia());
		Utilidades.setReferenciaVentana(guardarReferencia());
		Utilidades.setReferenciaVentanaPrincipal(guardarReferencia());
		VentanaAccionController.setReferenciaVentana(guardarReferencia());
		OpcionesAvanzadasController.setReferenciaVentanaPrincipal(guardarReferencia());
		Ventanas.setReferenciaVentanaPrincipal(guardarReferencia());
		DBUtilidades.setReferenciaVentana(guardarReferencia());
	}

	/**
	 * Inicializa el controlador cuando se carga la vista.
	 *
	 * @param location  la ubicación del archivo FXML
	 * @param resources los recursos utilizados por la vista
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		menuArchivoExcel.setGraphic(Utilidades.createIcon("/Icono/Archivo/exportar.png", 16, 16));
		menuArchivoImportar.setGraphic(Utilidades.createIcon("/Icono/Archivo/importar.png", 16, 16));
		menuArchivoDelete.setGraphic(Utilidades.createIcon("/Icono/Archivo/basura.png", 16, 16));
		menuArchivoSobreMi.setGraphic(Utilidades.createIcon("/Icono/Archivo/about.png", 16, 16));
		menuArchivoAvanzado.setGraphic(Utilidades.createIcon("/Icono/Archivo/configuraciones.png", 16, 16));
		menuArchivoDesconectar.setGraphic(Utilidades.createIcon("/Icono/Archivo/apagado.png", 16, 16));
		menuArchivoCerrar.setGraphic(Utilidades.createIcon("/Icono/Archivo/salir.png", 16, 16));

		menuCartaAniadir.setGraphic(Utilidades.createIcon("/Icono/Ventanas/add.png", 16, 16));
		menuCartaEliminar.setGraphic(Utilidades.createIcon("/Icono/Ventanas/delete.png", 16, 16));
		menuCartaModificar.setGraphic(Utilidades.createIcon("/Icono/Ventanas/modify.png", 16, 16));

		menuEstadisticaPosesion.setGraphic(Utilidades.createIcon("/Icono/Estadistica/posesion.png", 16, 16));
		menuEstadisticaComprados.setGraphic(Utilidades.createIcon("/Icono/Estadistica/comprado.png", 16, 16));
		menuEstadisticaVendidos.setGraphic(Utilidades.createIcon("/Icono/Estadistica/vendido.png", 16, 16));
		menuEstadisticaKeyIssue.setGraphic(Utilidades.createIcon("/Icono/Estadistica/keys.png", 16, 16));
		menuEstadisticaEstadistica.setGraphic(Utilidades.createIcon("/Icono/Estadistica/descarga.png", 16, 16));

		Platform.runLater(() -> {
			estadoStage().setOnCloseRequest(event -> stop());

			alarmaList.setAlarmaConexionInternet(alarmaConexionInternet);
			alarmaList.iniciarThreadChecker();

			enviarReferencias();

			establecerDinamismoAnchor();

			cambiarTamanioTable();

			FuncionesTableView.ajustarAnchoVBox();
			FuncionesTableView.seleccionarRaw();

			FuncionesTableView.modificarColumnas(true);
			AccionControlUI.controlarEventosInterfazPrincipal(guardarReferencia());
			FuncionesManejoFront.getStageVentanas().add(estadoStage());

			cargarDatosDataBase();
		});

		AccionControlUI.establecerTooltips();

		formatearTextField();

	}

	@FXML
	void ampliarImagen(MouseEvent event) {

		if (getCartaCache() != null) {
			ImagenAmpliadaController.cartaInfo = getCartaCache();

			if (guardarReferencia().getImagenCarta().getOpacity() != 0) {
				nav.verVentanaImagen();

			}
		}
	}

	@FXML
	public void cambiarTamanioTable() {

		if (!barraCambioAltura.isDisable()) {
			// Vincular el ancho de barraCambioAltura con el ancho de rootVBox
			barraCambioAltura.widthProperty().bind(rootVBox.widthProperty());

			// Configurar eventos del ratón para redimensionar el rootVBox desde la parte
			// superior
			barraCambioAltura.setOnMousePressed(event -> y = event.getScreenY());

			barraCambioAltura.setOnMouseDragged(event -> {
				double deltaY = event.getScreenY() - y;
				double newHeight = rootVBox.getPrefHeight() - deltaY;
				double maxHeight = calcularMaxHeight(); // Calcula el máximo altura permitido
				double minHeight = 250; // Límite mínimo de altura

				if (newHeight > minHeight && newHeight <= maxHeight) {
					rootVBox.setPrefHeight(newHeight);
					rootVBox.setLayoutY(tablaBBDD.getLayoutY() + deltaY);
					tablaBBDD.setPrefHeight(newHeight);
					tablaBBDD.setLayoutY(tablaBBDD.getLayoutY() + deltaY);

					y = event.getScreenY();
				}
			});

			// Cambiar el cursor cuando se pasa sobre la barra de redimensionamiento
			barraCambioAltura.setOnMouseMoved(event -> {
				if (event.getY() <= 5) {
					barraCambioAltura.setCursor(Cursor.N_RESIZE);
				} else {
					barraCambioAltura.setCursor(Cursor.DEFAULT);
				}
			});

			rootAnchorPane.heightProperty()
					.addListener((observable, oldValue, newHeightValue) -> rootVBox.setMaxHeight(calcularMaxHeight()));

			rootAnchorPane.widthProperty().addListener((observable, oldValue, newWidthValue) -> {
				double newWidth = newWidthValue.doubleValue();

				if (newWidth <= 1130) {

					botonIntroducir.setLayoutX(231);
					botonIntroducir.setLayoutY(159);

					botonModificar.setLayoutX(231);
					botonModificar.setLayoutY(197);

					botonEliminar.setLayoutX(231);
					botonEliminar.setLayoutY(237);

					botonGuardarResultado.setLayoutX(231);
					botonGuardarResultado.setLayoutY(324);

					botonImprimir.setLayoutX(290);
					botonImprimir.setLayoutY(324);

				} else if (newWidth >= 1131) {

					botonIntroducir.setLayoutX(329);
					botonIntroducir.setLayoutY(31);

					botonModificar.setLayoutX(329);
					botonModificar.setLayoutY(72);

					botonEliminar.setLayoutX(329);
					botonEliminar.setLayoutY(116);

				}
			});
		}

	}

	// Método para calcular el máximo altura permitido
	private double calcularMaxHeight() {
		// Obtener el tamaño actual de la ventana
		Stage stage = (Stage) rootVBox.getScene().getWindow();
		double windowHeight = stage.getHeight();

		// Ajustar el máximo altura permitido según la posición del AnchorPane
		// numeroCaja
		return windowHeight - comboboxGradeoCarta.getLayoutY() - 150;
	}

	/**
	 * Carga los datos de la base de datos en los ComboBox proporcionados después de
	 * un segundo de retraso. Esta función utiliza un ScheduledExecutorService para
	 * programar la tarea.
	 *
	 * @param comboboxes Una lista de ComboBox que se actualizarán con los datos de
	 *                   la base de datos.
	 */
	public void cargarDatosDataBase() {
		tablaBBDD.refresh();
		prontInfo.setOpacity(0);
		imagenCarta.setImage(null);

		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		try {
			scheduler.schedule(() -> Platform.runLater(() -> {
				ListasCartasDAO.listasAutoCompletado();

				Task<Void> task = new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						funcionesCombo.rellenarComboBox(referenciaVentana.getListaComboboxes());
						funcionesCombo.lecturaComboBox(referenciaVentana.getListaComboboxes());
						return null;
					}
				};

				// Iniciar el Task en un nuevo hilo
				Thread thread = new Thread(task);
				thread.start();

				// Manejar la cancelación
				botonCancelarSubida.setOnAction(ev -> {
					botonCancelarSubida.setVisible(false);
					task.cancel();
				});

				// Cuando la tarea haya terminado, apaga el scheduler
				task.setOnSucceeded(event -> {
					botonCancelarSubida.setVisible(false);
					scheduler.shutdown();
				});

				// Cuando la tarea haya terminado, apaga el scheduler
				task.setOnRunning(event -> botonCancelarSubida.setVisible(true));
			}), 0, TimeUnit.SECONDS);
		} catch (Exception e) {
			Utilidades.manejarExcepcion(e);
		} finally {
			scheduler.shutdown();
		}
	}

	/**
	 * Establece el dinamismo en la interfaz gráfica ajustando propiedades de
	 * elementos como tamaños, anchos y máximos.
	 */
	public void establecerDinamismoAnchor() {

		FuncionesManejoFront.establecerFondoDinamico();

		FuncionesManejoFront.establecerAnchoColumnas(13);

		FuncionesManejoFront.establecerAnchoMaximoBotones(102.0);

		FuncionesManejoFront.establecerAnchoMaximoCamposTexto(162.0);

		FuncionesManejoFront.establecerAnchoMaximoComboBoxes(162.0);

		FuncionesManejoFront.establecerTamanioMaximoImagen(252.0, 325.0);
	}

	/**
	 * Funcion que permite restringir entrada de datos de todo aquello que no sea un
	 * numero entero en los comboBox numeroCarta y caja_comic
	 */
	public void formatearTextField() {
		comboboxNumeroCarta.getEditor().setTextFormatter(FuncionesComboBox.validadorNenteros());
	}

	/////////////////////////////////
	//// METODOS LLAMADA A VENTANAS//
	/////////////////////////////////

	/**
	 * Permite el cambio de ventana a la ventana de SobreMiController
	 *
	 * @param event
	 */
	@FXML
	void verSobreMi(ActionEvent event) {
		nav.verSobreMi();
	}

	/**
	 * Metodo que mostrara los comics o comic buscados por parametro
	 *
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void mostrarPorParametro(ActionEvent event) {
		enviarReferencias();
		mostrarCartas(false);
		modificarEstadoTabla(384, 1);
	}

	/**
	 * Metodo que muestra toda la base de datos.
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	void verTodabbdd(ActionEvent event) {
		enviarReferencias();
		mostrarCartas(true);
		modificarEstadoTabla(384, 1);
	}

	public void modificarEstadoTabla(double altura, double opacidad) {
		rootVBox.setPrefHeight(altura);
		tablaBBDD.setPrefHeight(altura);
		tablaBBDD.setOpacity(opacidad);
	}

	private void mostrarCartas(boolean esCompleto) {

		if (esCompleto) {
			AccionSeleccionar.verBasedeDatos(esCompleto, false, null);
		} else {
			List<String> controls = new ArrayList<>();
			List<ComboBox<String>> listaComboboxes = referenciaVentana.getListaComboboxes();

			// Iterar sobre los ComboBox en orden
			for (ComboBox<String> comboBox : listaComboboxes) {
				controls.add(comboBox.getSelectionModel().getSelectedItem());
			}

			Carta comic = AccionControlUI.camposCarta(controls, false);

			AccionSeleccionar.verBasedeDatos(esCompleto, false, comic);
		}
	}

	/**
	 * Funcion que al pulsar el boton de 'botonPuntuacion' se muestran aquellos
	 * comics que tienen una puntuacion
	 *
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void comicsPuntuacion(ActionEvent event) {
		imprimirCartasEstado(TipoBusqueda.PUNTUACION, false);
	}

	/**
	 * Funcion que al pulsar el boton de 'botonVentas' se muestran aquellos comics
	 * que han sido vendidos
	 *
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void comicsVendidos(ActionEvent event) {
		imprimirCartasEstado(TipoBusqueda.VENDIDOS, false);
	}

	/**
	 * Funcion que al pulsar el boton de 'botonVentas' se muestran aquellos comics
	 * que han sido vendidos
	 *
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void comicsFirmados(ActionEvent event) {
		imprimirCartasEstado(TipoBusqueda.FIRMADOS, false);
	}

	/**
	 * Funcion que al pulsar el boton de 'botonVentas' se muestran aquellos comics
	 * que han sido vendidos
	 *
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void comicsComprados(ActionEvent event) {
		imprimirCartasEstado(TipoBusqueda.COMPRADOS, false);
	}

	/**
	 * Funcion que al pulsar el boton de 'botonVentas' se muestran aquellos comics
	 * que han sido vendidos
	 *
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void comicsEnPosesion(ActionEvent event) {
		imprimirCartasEstado(TipoBusqueda.POSESION, false);
	}

	@FXML
	void comicsGuardados(ActionEvent event) {
		imprimirCartasEstado(null, true);

	}

	@FXML
	void verOpcionesAvanzadas(ActionEvent event) {
		nav.verOpcionesAvanzadas();

	}

	/**
	 * Maneja la acción de mostrar los cómics considerados "Key Issue".
	 *
	 * @param event El evento que desencadenó esta acción.
	 * @throws SQLException Si ocurre un error al acceder a la base de datos.
	 */
	@FXML
	void comicsKeyIssue(ActionEvent event) throws SQLException {
		imprimirCartasEstado(TipoBusqueda.KEY_ISSUE, false);
	}

	private void imprimirCartasEstado(TipoBusqueda tipoBusqueda, boolean esGuardado) {
		limpiezaDeDatos();
		limpiarComboBox();
		ListasCartasDAO.reiniciarListaCartas();
		FuncionesTableView.nombreColumnas();
		FuncionesTableView.actualizarBusquedaRaw();
		List<Carta> listaCartas;
		if (esGuardado) {
			listaCartas = ListasCartasDAO.cartasGuardadosList;
		} else {
			String sentenciaSQL = DBUtilidades.construirSentenciaSQL(tipoBusqueda);

			System.out.println(sentenciaSQL);

			listaCartas = SelectManager.verLibreria(sentenciaSQL, false);
		}

		FuncionesTableView.tablaBBDD(listaCartas);
	}

	////////////////////////////
	/// METODOS PARA EXPORTAR///
	////////////////////////////

	/**
	 * Importa un fichero CSV compatible con el programa para copiar la informacion
	 * a la base de datos
	 *
	 * @param event
	 * @throws SQLException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@FXML
	void importCSV(ActionEvent event) {

		limpiezaDeDatos();
		limpiarComboBox();

		guardarDatosCSV();

		ListasCartasDAO.listasAutoCompletado();

		ListasCartasDAO.limpiarListaGuardados();

	}

	/**
	 * Exporta un fichero CSV compatible con el programa que copia el contenido de
	 * la base de datos en un fichero CSV
	 *
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void exportCSV(ActionEvent event) throws SQLException {

		boolean estaVacia = false;
		String mensaje = "";
		if (!ListasCartasDAO.listaNombre.isEmpty()) {
			limpiezaDeDatos();
			limpiarComboBox();
			String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);

			List<Carta> listaCartas = SelectManager.verLibreria(sentenciaSQL, false);

			cargaExportExcel(listaCartas, DBUtilidades.TipoBusqueda.COMPLETA.toString());

			ListasCartasDAO.limpiarListaGuardados();

			estaVacia = true;
		} else {
			mensaje = "La base de datos esta vacia. No hay nada que exportar";
			AlarmaList.mostrarMensajePront(mensaje, estaVacia, prontInfo);
		}

	}

	/**
	 * Limpia los campos de pantalla donde se escriben los datos.
	 *
	 * @param event
	 */
	@FXML
	void limpiarDatos(ActionEvent event) {

		enviarReferencias();
		limpiezaDeDatos();
		limpiarComboBox();
		botonImprimir.setVisible(false);
		botonImprimir.setDisable(true);
		botonGuardarResultado.setVisible(false);
		botonGuardarResultado.setDisable(true);

		int tamanioListaGuardada = ListasCartasDAO.cartasGuardadosList.size();

		if (tamanioListaGuardada > 0 && nav.borrarListaGuardada()) {

			ListasCartasDAO.limpiarListaGuardados();

			String mensaje = "Has eliminado el contenido de la lista guardada que contenia un total de: "
					+ tamanioListaGuardada + " comics guardados.\n \n \n";

			AlarmaList.mostrarMensajePront(mensaje, true, prontInfo);

			botonMostrarGuardados.setVisible(false);
		}
	}

	/**
	 * Se llama a funcion que permite ver las estadisticas de la bbdd
	 *
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void verEstadistica(ActionEvent event) {

		AlarmaList.iniciarAnimacionEstadistica(prontInfo);
		ListasCartasDAO.generar_fichero_estadisticas();
		AlarmaList.detenerAnimacionPront(prontInfo);
		String mensaje = "Fichero creado correctamente";

		AlarmaList.mostrarMensajePront(mensaje, true, prontInfo);
	}

	/////////////////////////////////
	//// FUNCIONES////////////////////
	/////////////////////////////////

	/**
	 * Funcion que permite mostrar la imagen de portada cuando clickeas en una
	 * tabla.
	 *
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	void clickRaton(MouseEvent event) {

		if (!tablaBBDD.isDisabled()) {
			enviarReferencias();
			setCartaCache(guardarReferencia().getTablaBBDD().getSelectionModel().getSelectedItem());

			AccionSeleccionar.seleccionarCartas(true);
		}
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
		if ((event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) && !tablaBBDD.isDisabled()) {
			enviarReferencias();
			setCartaCache(guardarReferencia().getTablaBBDD().getSelectionModel().getSelectedItem());
			AccionSeleccionar.seleccionarCartas(true);
		}

	}

	/////////////////////////////////
	//// FUNCIONES CREACION FICHEROS//
	/////////////////////////////////

	/**
	 * Maneja la acción de impresión del resultado. Obtiene una lista de cómics
	 * según los parámetros especificados y realiza la exportación de la información
	 * a un archivo Excel.
	 *
	 * @param event El evento de acción que desencadena la impresión del resultado.
	 * @throws SQLException Si ocurre un error al interactuar con la base de datos.
	 */
	@FXML
	void imprimirResultado(ActionEvent event) throws SQLException {

		prontInfo.clear();
		String tipoBusqueda = "Parcial";

		if (!ListasCartasDAO.cartasGuardadosList.isEmpty()) {
			cargaExportExcel(ListasCartasDAO.cartasGuardadosList, tipoBusqueda);

			String mensaje = "Lista guardada de forma correcta";
			AlarmaList.mostrarMensajePront(mensaje, true, prontInfo);

		} else {
			String mensaje = "La lista esta vacia";
			AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
		}
	}

	/**
	 * Guarda los resultados de la lista de cómics en la base de datos de la
	 * librería, asegurándose de mantener una lista única de cómics en la base de
	 * datos. Además, realiza la limpieza de datos y actualiza la visibilidad y
	 * desactiva los botones de guardar resultado e imprimir.
	 *
	 * @param event El evento que desencadenó la llamada a esta función.
	 * @throws SQLException Si ocurre un error al interactuar con la base de datos.
	 */
	@FXML
	void guardarResultado(ActionEvent event) throws SQLException {

		Carta cartaRaw = tablaBBDD.getSelectionModel().getSelectedItem();
		String mensaje = "";
		if (cartaRaw != null) {
			boolean existeCarta = ListasCartasDAO.verificarIDExistente(cartaRaw.getIdCarta(), true);
			if (existeCarta) {
				mensaje = "Este comic con dichaid: " + cartaRaw.getIdCarta() + " ya existe. No se ha guardado \n \n \n";
				AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
				return;
			}

			ListasCartasDAO.agregarElementoUnico(cartaRaw);

			mensaje = "Hay un total de: " + ListasCartasDAO.cartasGuardadosList.size()
					+ ". Cartas guardados a la espera de ser impresos \n \n \n";
			AlarmaList.mostrarMensajePront(mensaje, true, prontInfo);

		} else {
			mensaje = "Debes de clickar en el comic que quieras guardar \n \n \n";
			AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
		}

	}

	@FXML
	void borrarContenidoTabla(ActionEvent event) {
		try {
			Thread borradoTablaThread = new Thread(() -> {
				try {
					FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
					boolean confirmacionBorrado = nav.borrarContenidoTabla().get();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
					if (confirmacionBorrado) {

						AlarmaList.iniciarAnimacionCarga(referenciaVentana.getProgresoCarga());
						String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);

						List<Carta> listaCartas = SelectManager.verLibreria(sentenciaSQL, false);
						FuncionesExcel excelFuntions = new FuncionesExcel();
						// Configuración de la tarea para crear el archivo Excel

						Task<Boolean> crearExcelTask = excelFuntions.crearExcelTask(listaCartas,
								TipoBusqueda.ELIMINAR.toString(), dateFormat);
						Thread excelThread = new Thread(crearExcelTask);

						if (crearExcelTask == null) {
							botonCancelarSubida.setVisible(false);
							FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
							AlarmaList.detenerAnimacionPront(prontInfo);
							AlarmaList.detenerAnimacionCarga(progresoCarga);

							// Detener el hilo de la tarea
							excelThread.interrupt();
						} else {

							crearExcelTask.setOnRunning(e -> {

								estadoStage().setOnCloseRequest(closeEvent -> {
									crearExcelTask.cancel(true);
									excelThread.interrupt(); // Interrumpir el hilo
									Utilidades.cerrarCargaCartas();
								});

								cerradoPorOperaciones();
								botonCancelarSubida.setVisible(true);
								FuncionesManejoFront.cambiarEstadoMenuBar(true, guardarReferencia());
								limpiezaDeDatos();
							});

							crearExcelTask.setOnSucceeded(e -> {

								botonCancelarSubida.setVisible(false);
								boolean deleteCompleted;
								try {
									deleteCompleted = CartaManagerDAO.deleteTable().get();
									String mensaje = deleteCompleted
											? "Base de datos borrada y reiniciada correctamente"
											: "ERROR. No se ha podido eliminar y reiniciar la base de datos";

									if (deleteCompleted) {
										AlarmaList.detenerAnimacionCarga(referenciaVentana.getProgresoCarga());
										ListasCartasDAO.limpiarListaGuardados();
										Utilidades.eliminarContenidoCarpeta(FuncionesExcel.DEFAULT_PORTADA_IMAGE_PATH);
									}
									FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
									AlarmaList.mostrarMensajePront(mensaje, deleteCompleted, prontInfo);
									botonGuardarResultado.setVisible(false);

								} catch (InterruptedException | ExecutionException e1) {
									crearExcelTask.cancel(true);
									excelThread.interrupt();
									Utilidades.manejarExcepcion(e1);
								}
							});

							crearExcelTask.setOnFailed(e -> {
								botonCancelarSubida.setVisible(false);
								FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
							});

							crearExcelTask.setOnCancelled(e -> {
								FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
								AlarmaList.detenerAnimacionCarga(referenciaVentana.getProgresoCarga());
								String mensaje = "Has cancelado el borrado de la base de datos";
								AlarmaList.mostrarMensajePront(mensaje, true, prontInfo);

							});

							// Manejar la cancelación
							botonCancelarSubida.setOnAction(ev -> {
								botonCancelarSubida.setVisible(false);
								AlarmaList.detenerAnimacionCarga(referenciaVentana.getProgresoCarga());

								crearExcelTask.cancel(true);
								excelThread.interrupt();
							});
						}
						// Iniciar la tarea principal de creación de Excel en un hilo separado
						excelThread.start();

					} else {
						AlarmaList.detenerAnimacionCarga(referenciaVentana.getProgresoCarga());
						String mensaje = "ERROR. Has cancelado el borrado de la base de datos";
						AlarmaList.mostrarMensajePront(mensaje, false, prontInfo);
						FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
					}
				} catch (InterruptedException | ExecutionException e) {
					Utilidades.manejarExcepcion(e);
				}
			});

			borradoTablaThread.start();
		} catch (Exception e) {
			Utilidades.manejarExcepcion(e);
		}
	}

	/**
	 * Carga y ejecuta una tarea para exportar datos a un archivo Excel.
	 *
	 * @param fichero     El archivo Excel de destino.
	 * @param listaCartas La lista de cómics a exportar.
	 */
	private void cargaExportExcel(List<Carta> listaCartas, String tipoBusqueda) {

		FuncionesExcel excelFuntions = new FuncionesExcel();
		String mensajeErrorExportar = "ERROR. No se ha podido exportar correctamente.";
		String mensajeCancelarExportar = "ERROR. Se ha cancelado la exportación.";
		String mensajeValido = "Has exportado el fichero excel correctamente";

		prontInfo.setText(null);
		prontInfo.setOpacity(0);
		tablaBBDD.getItems().clear();
		imagenCarta.setImage(null);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		// Configuración de la tarea para crear el archivo Excel
		Task<Boolean> crearExcelTask = excelFuntions.crearExcelTask(listaCartas, tipoBusqueda, dateFormat);
		Thread excelThread = new Thread(crearExcelTask);

		if (crearExcelTask == null) {
			botonCancelarSubida.setVisible(false);
			FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
			AlarmaList.detenerAnimacionPront(prontInfo);
			AlarmaList.detenerAnimacionCarga(progresoCarga);

			// Detener el hilo de la tarea
			excelThread.interrupt();
			AlarmaList.mostrarMensajePront(mensajeCancelarExportar, false, prontInfo);
		} else {
			crearExcelTask.setOnRunning(e -> {

				estadoStage().setOnCloseRequest(event -> {
					crearExcelTask.cancel(true);
					Utilidades.cerrarCargaCartas();
				});

				cerradoPorOperaciones();
				botonCancelarSubida.setVisible(true);
				FuncionesManejoFront.cambiarEstadoMenuBar(true, guardarReferencia());
				AlarmaList.iniciarAnimacionCarga(progresoCarga);
				limpiezaDeDatos();

			});

			crearExcelTask.setOnSucceeded(event -> {
				botonCancelarSubida.setVisible(false);
				FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
				AlarmaList.mostrarMensajePront(mensajeValido, true, prontInfo);
				AlarmaList.detenerAnimacionCarga(progresoCarga);
			});

			// Configuración del comportamiento cuando la tarea falla
			crearExcelTask.setOnFailed(event -> {
				botonCancelarSubida.setVisible(false);
				procesarResultadoImportacion(false);
				AlarmaList.detenerAnimacionPront(prontInfo);
				AlarmaList.detenerAnimacionCarga(progresoCarga);

				// Detener el hilo de la tarea
				excelThread.interrupt();
				alarmaList.manejarFallo(mensajeErrorExportar, prontInfo);
				FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
				AlarmaList.detenerAnimacionCarga(progresoCarga);
				AlarmaList.mostrarMensajePront(mensajeCancelarExportar, false, prontInfo);
			});

			// Configuración del comportamiento cuando la tarea es cancelada
			crearExcelTask.setOnCancelled(event -> {
				alarmaList.manejarFallo(mensajeCancelarExportar, prontInfo);
				FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
				AlarmaList.detenerAnimacionCarga(progresoCarga);
				AlarmaList.mostrarMensajePront(mensajeCancelarExportar, false, prontInfo);
				// Detener el hilo de la tarea
				excelThread.interrupt();
			});
		}

		// Manejar la cancelación
		botonCancelarSubida.setOnAction(ev -> {
			botonCancelarSubida.setVisible(false);

			crearExcelTask.cancel(true);
			excelThread.interrupt();
		});
		excelThread.setDaemon(true); // Establecer como daemon
		// Iniciar la tarea principal de creación de Excel en un hilo separado
		excelThread.start();
	}

	public void guardarDatosCSV() {

		String frase = "Fichero CSV";
		String formatoFichero = "*.csv";

		File fichero = Utilidades.tratarFichero(frase, formatoFichero, false);

		// Verificar si se obtuvo un objeto FileChooser válido
		if (fichero != null) {

			String mensajeValido = "Has importado correctamente la lista de comics en la base de datos";

			Task<Boolean> lecturaTask = FuncionesExcel.procesarArchivoCSVTask(fichero);

			lecturaTask.setOnSucceeded(e -> {
				cargarDatosDataBase();
				AlarmaList.detenerAnimacion();
				AlarmaList.detenerAnimacionCarga(progresoCarga);
				botonCancelarSubida.setVisible(false);
				FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
				AlarmaList.mostrarMensajePront(mensajeValido, true, prontInfo);
			});

			lecturaTask.setOnRunning(e -> {

				estadoStage().setOnCloseRequest(event -> {
					lecturaTask.cancel(true);
					Utilidades.cerrarCargaCartas();
				});
				cerradoPorOperaciones();
				FuncionesManejoFront.cambiarEstadoMenuBar(true, guardarReferencia());
				botonCancelarSubida.setVisible(true);
				AlarmaList.iniciarAnimacionCarga(progresoCarga);
				limpiezaDeDatos();
			});

			lecturaTask.setOnFailed(e -> {
				botonCancelarSubida.setVisible(false);
				procesarResultadoImportacion(lecturaTask.getValue());
				FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
				AlarmaList.detenerAnimacionCarga(progresoCarga);
			});

			// Manejar la cancelación
			botonCancelarSubida.setOnAction(ev -> {
				lecturaTask.cancel(true); // true indica que la tarea debe ser interrumpida si ya está en ejecución
				botonCancelarSubida.setVisible(false);
				FuncionesManejoFront.cambiarEstadoMenuBar(false, guardarReferencia());
				AlarmaList.detenerAnimacionCarga(progresoCarga);

				procesarResultadoImportacion(false);
			});

			// Iniciar la tarea principal de importación en un hilo separado
			new Thread(lecturaTask).start();
		}
	}

	private void procesarResultadoImportacion(Boolean resultado) {
		String mensaje = "";
		prontInfo.clear();
		if (Boolean.TRUE.equals(resultado)) {
			mensaje = "Operacion realizada con exito";
		} else {
			mensaje = "ERROR. No se ha podido completar la operacion";
		}

		AlarmaList.detenerAnimacion();
		AlarmaList.mostrarMensajePront(mensaje, resultado, prontInfo);
	}

	/**
	 * Realiza la limpieza de datos en la interfaz gráfica.
	 */
	private void limpiezaDeDatos() {

		prontInfo.clear();
		prontInfo.setText(null);
		prontInfo.setOpacity(0);
		tablaBBDD.getItems().clear();
		tablaBBDD.refresh();
		imagenCarta.setImage(null);
		imagenCarta.setOpacity(0);


		modificarEstadoTabla(259, 0.6);
	}

	private void limpiarComboBox() {

		// Iterar sobre todos los ComboBox para realizar la limpieza
		for (ComboBox<String> comboBox : referenciaVentana.getListaComboboxes()) {
			// Limpiar el campo
			comboBox.setValue("");
			comboBox.getEditor().setText("");
		}

	}

	/**
	 * Maneja la acción del usuario en relación a los cómics, como agregar,
	 * modificar, eliminar o puntuar un cómic.
	 *
	 * @param event El evento de acción que desencadenó la llamada a esta función.
	 */
	@FXML
	void accionCarta(ActionEvent event) {
		Object fuente = event.getSource();
		tablaBBDD.getItems().clear();

		// Pasar la lista de ComboBoxes a VentanaAccionController
		referenciaVentana.setListaComboboxes(referenciaVentana.getListaComboboxes());

		if (fuente instanceof Button botonPresionado) {
			if (botonPresionado == botonIntroducir) {
				AccionFuncionesComunes.setTipoAccion("aniadir");
			} else if (botonPresionado == botonModificar) {
				AccionFuncionesComunes.setTipoAccion("modificar");
			} else if (botonPresionado == botonEliminar) {
				AccionFuncionesComunes.setTipoAccion("eliminar");
			}
		} else if (fuente instanceof MenuItem menuItemPresionado) {
			if (menuItemPresionado == menuCartaAniadir) {
				AccionFuncionesComunes.setTipoAccion("aniadir");
			} else if (menuItemPresionado == menuCartaModificar) {
				AccionFuncionesComunes.setTipoAccion("modificar");
			} else if (menuItemPresionado == menuCartaEliminar) {
				AccionFuncionesComunes.setTipoAccion("eliminar");
			}
		}
		modificarEstadoTabla(259, 0.6);
		imagenCarta.setVisible(false);
		imagenCarta.setImage(null);
		prontInfo.setOpacity(0);
		nav.verAccionCarta();
	}

	/////////////////////////////
	//// FUNCIONES PARA SALIR////
	/////////////////////////////

	public Scene miStageVentana() {
		Node rootNode = menuNavegacion;
		while (rootNode.getParent() != null) {
			rootNode = rootNode.getParent();
		}

		if (rootNode instanceof Parent parent) {
			Scene scene = parent.getScene();
			ConectManager.activeScenes.add(scene);
			return scene;
		} else {
			return null;
		}
	}

	/**
	 * Vuelve al menu inicial de conexion de la base de datos.
	 *
	 * @param event
	 */
	@FXML
	void volverMenu(ActionEvent event) {

		if (FuncionesManejoFront.getStageVentanas().contains(estadoStage())) {
			FuncionesManejoFront.getStageVentanas().remove(estadoStage());
		}

		List<Stage> stageVentanas = FuncionesManejoFront.getStageVentanas();

		// Assuming `stages` is a collection of stages you want to check against
		for (Stage stage : stageVentanas) {
			stage.close(); // Close the stage if it's not the current state
		}

		ConectManager.close();
		nav.cerrarCargaCartas();
		nav.verAccesoBBDD();
		estadoStage().close();

	}

	/**
	 * Maneja la acción de salida del programa.
	 *
	 * @param event el evento que desencadena la acción
	 */
	@FXML
	public void salirPrograma(ActionEvent event) {
		// Lógica para manejar la acción de "Salir"
		nav.cerrarCargaCartas();
		if (nav.salirPrograma(event)) {
			estadoStage().close();
		}
	}

	public void cerradoPorOperaciones() {
		List<Stage> stageVentanas = FuncionesManejoFront.getStageVentanas();

		for (Stage stage : stageVentanas) {

			if (!stage.getTitle().equalsIgnoreCase("Menu principal")) {
				stage.close();
			}
		}

		if (FuncionesManejoFront.getStageVentanas().contains(estadoStage())) {
			FuncionesManejoFront.getStageVentanas().remove(estadoStage());
		}
	}

	public Stage estadoStage() {

		return (Stage) botonLimpiar.getScene().getWindow();
	}

	/**
	 * @return the cartaCache
	 */
	public Carta getCartaCache() {
		return cartaCache;
	}

	/**
	 * @param cartaCache the cartaCache to set
	 */
	public void setCartaCache(Carta cartaCache) {
		this.cartaCache = cartaCache;
	}

	/**
	 * Al cerrar la ventana, carga la ventana del menu principal
	 *
	 */
	public void closeWindows() {
		nav.cerrarCargaCartas();
		Platform.exit();
	}

	public void stop() {

		cerradoPorOperaciones();
		alarmaList.detenerThreadChecker();
		nav.cerrarMenuOpciones();
		nav.cerrarCargaCartas();
		nav.cerrarVentanaAccion();
		Utilidades.cerrarOpcionesAvanzadas();

		Platform.exit();
	}
}
