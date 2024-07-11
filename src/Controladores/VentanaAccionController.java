
package Controladores;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
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
import webScrap.WebScrapGoogleCardMarket;
import webScrap.WebScrapGoogleScryfall;
import webScrap.WebScrapGoogleTCGPlayer;
import webScrap.WebScrapNodeJSInstall;

/**
 * Clase controladora para la ventana de acciones, que gestiona la interfaz de
 * usuario y las operaciones relacionadas con los cómics.
 */
public class VentanaAccionController implements Initializable {
	@FXML
	private Label alarmaConexionInternet;

	@FXML
	private Label alarmaConexionSql;

	@FXML
	private Label labelNombre;

	@FXML
	private Button botonClonarCarta;

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
	private Button botonEliminarImportadoListaCarta;

	@FXML
	private Button botonGuardarCambioCarta;

	@FXML
	private Button botonGuardarCarta;

	@FXML
	private Button botonGuardarListaCartas;

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
	private ImageView cargaImagen;

	@FXML
	private TableColumn<Carta, String> columnaColeccion;

	@FXML
	private TableColumn<Carta, String> columnaEditorial;

	@FXML
	private TableColumn<Carta, String> columnaNombre;

	@FXML
	private TableColumn<Carta, String> columnaPrecioFoil;

	@FXML
	private TableColumn<Carta, String> columnaPrecioNormal;

	@FXML
	private TableColumn<Carta, String> columnaRareza;

	@FXML
	private ComboBox<String> comboBoxTienda;

	@FXML
	private ComboBox<String> comboboxNumeroCarta;

	@FXML
	private TableColumn<Carta, String> fecha;

	@FXML
	private TableColumn<Carta, String> id;

	@FXML
	private ImageView imagencarta;

	@FXML
	private Label labelId;

	@FXML
	private Label labelNormas;

	@FXML
	private Label labelPortada;

	@FXML
	private Label labelPrecioFoil;

	@FXML
	private Label labelPrecioNormal;

	@FXML
	private Label labelRareza;

	@FXML
	private Label labelReferencia;

	@FXML
	private Label labelcoleccion;

	@FXML
	private MenuItem menuImportarFichero;

	@FXML
	private MenuBar menuNavegacion;

	@FXML
	private Menu navegacionEstadistica;

	@FXML
	private MenuItem navegacionMostrarEstadistica;

	@FXML
	private Menu navegacionOpciones;

	@FXML
	private TableColumn<Carta, String> numero;

	@FXML
	private ProgressIndicator progresoCarga;

	@FXML
	private TextArea prontInfo;

	@FXML
	private VBox rootVBox;

	@FXML
	private TableView<Carta> tablaBBDD;

	@FXML
	private TextArea textAreaNormasCarta;

	@FXML
	private TextField textFieldColeccion;

	@FXML
	private TextField textFieldDireccionPortada;

	@FXML
	private TextField textFieldEditorialCarta;

	@FXML
	private TextField textFieldIdCarta;

	@FXML
	private TextField textFieldNombreCarta;

	@FXML
	private TextField textFieldPrecioCartaFoil;

	@FXML
	private TextField textFieldPrecioCartaNormal;

	@FXML
	private TextField textFieldRarezaCarta;

	@FXML
	private TextField textFieldUrlCarta;

	@FXML
	private VBox vboxImage;

	/**
	 * Referencia a la ventana (stage).
	 */
	private Stage stage;

	public Carta cartaCache;

	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();

	public static final AlarmaList alarmaList = new AlarmaList();

	public static final AccionReferencias referenciaVentana = new AccionReferencias();

	public static AccionReferencias referenciaVentanaPrincipal = new AccionReferencias();

	private static final Logger logger = Logger.getLogger(Utilidades.class.getName());

	public AccionReferencias guardarReferencia() {

		referenciaVentana.setNombreCartaTextField(textFieldNombreCarta);
		referenciaVentana.setNumeroCartaCombobox(comboboxNumeroCarta);
		referenciaVentana.setEditorialCartaTextField(textFieldEditorialCarta);
		referenciaVentana.setColeccionCartaTextField(textFieldColeccion);
		referenciaVentana.setRarezaCartaTextField(textFieldRarezaCarta);
		referenciaVentana.setNormasCartaTextArea(textAreaNormasCarta);
		referenciaVentana.setPrecioCartaNormalTextField(textFieldPrecioCartaNormal);
		referenciaVentana.setPrecioCartaFoilTextField(textFieldPrecioCartaFoil);
		referenciaVentana.setDireccionImagenTextField(textFieldDireccionPortada);
		referenciaVentana.setNombreTiendaCombobox(comboBoxTienda);
		referenciaVentana.setUrlReferenciaTextField(textFieldUrlCarta);
		referenciaVentana.setIdCartaTratarTextField(textFieldIdCarta);
		referenciaVentana.setBusquedaCodigoTextField(busquedaCodigo);

		referenciaVentana.setBotonClonarCarta(botonClonarCarta);
		referenciaVentana.setBotonCancelarSubida(botonCancelarSubida);
		referenciaVentana.setBotonBusquedaCodigo(botonBusquedaCodigo);
		referenciaVentana.setBotonBusquedaAvanzada(botonBusquedaAvanzada);
		referenciaVentana.setBotonEliminar(botonEliminar);
		referenciaVentana.setBotonLimpiar(botonLimpiar);
		referenciaVentana.setBotonModificarCarta(botonModificarCarta);
		referenciaVentana.setBotonParametroCarta(botonParametroCarta);
		referenciaVentana.setBotonVender(botonVender);
		referenciaVentana.setBotonbbdd(botonbbdd);
		referenciaVentana.setBotonGuardarCambioCarta(botonGuardarCambioCarta);

		referenciaVentana.setBotonGuardarCarta(botonGuardarCarta);
		referenciaVentana.setBotonEliminarImportadoCarta(botonEliminarImportadoCarta);

		referenciaVentana.setBotonEliminarImportadoListaCarta(botonEliminarImportadoListaCarta);
		referenciaVentana.setBotonGuardarListaCartas(botonGuardarListaCartas);

		referenciaVentana.setBotonSubidaPortada(botonSubidaPortada);
		referenciaVentana.setBusquedaCodigoTextField(busquedaCodigo);
		referenciaVentana.setStageVentana(estadoStage());
		referenciaVentana.setProgresoCarga(progresoCarga);

		referenciaVentana.setLabelColeccion(labelcoleccion);
		referenciaVentana.setLabelNombre(labelNombre);
		referenciaVentana.setLabelRareza(labelRareza);
		referenciaVentana.setLabelNormas(labelNormas);
		referenciaVentana.setLabelPrecioNormal(labelPrecioNormal);
		referenciaVentana.setLabelPrecioFoil(labelPrecioFoil);
		referenciaVentana.setLabelIdMod(labelId);
		referenciaVentana.setLabelPortada(labelPortada);
		referenciaVentana.setLabelReferencia(labelReferencia);
		referenciaVentana.setAlarmaConexionInternet(alarmaConexionInternet);
		referenciaVentana.setAlarmaConexionSql(alarmaConexionSql);

		referenciaVentana.setTablaBBDD(tablaBBDD);
		referenciaVentana.setImagenCarta(imagencarta);
		referenciaVentana.setCargaImagen(cargaImagen);
		referenciaVentana.setProntInfoTextArea(prontInfo);
		referenciaVentana.setRootVBox(rootVBox);
		referenciaVentana.setMenuImportarFicheroCodigoBarras(menuImportarFichero);
		referenciaVentana.setMenuEstadisticaEstadistica(navegacionMostrarEstadistica);
		referenciaVentana.setMenuNavegacion(menuNavegacion);
		referenciaVentana.setNavegacionCerrar(navegacionOpciones);
		referenciaVentana.setNavegacionEstadistica(navegacionEstadistica);

		AccionReferencias.setListaComboboxes(Arrays.asList(comboboxNumeroCarta));

		AccionReferencias.setListaTextFields(
				FXCollections.observableArrayList(Arrays.asList(textFieldNombreCarta, textFieldEditorialCarta,
						textFieldColeccion, textFieldRarezaCarta, textAreaNormasCarta, textFieldPrecioCartaFoil,
						textFieldPrecioCartaNormal, textFieldIdCarta, textFieldDireccionPortada, textFieldUrlCarta)));

		referenciaVentana
				.setControlAccion(Arrays.asList(textFieldNombreCarta, comboboxNumeroCarta, textFieldEditorialCarta,
						textFieldColeccion, textFieldRarezaCarta, textFieldUrlCarta, textFieldPrecioCartaNormal,
						textFieldPrecioCartaFoil, textFieldIdCarta, textFieldDireccionPortada, textAreaNormasCarta));

		AccionReferencias.setListaColumnasTabla(Arrays.asList(columnaNombre, columnaEditorial, columnaColeccion,
				columnaRareza, columnaPrecioNormal, columnaPrecioFoil));

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
			AccionSeleccionar.actualizarRefrenciaClick(guardarReferencia());
		});

		ListasCartasDAO.cartasImportados.clear();

		AccionControlUI.establecerTooltips();

		formatearTextField();

	}

	@FXML
	void ampliarImagen(MouseEvent event) {
		enviarReferencias();
		if (getCartaCache() != null) {
			ImagenAmpliadaController.cartaInfo = getCartaCache();
			if (guardarReferencia().getImagenCarta().getOpacity() != 0) {
				nav.verVentanaImagen();
			}
		}
	}

	/**
	 * Rellena los combos estáticos en la interfaz. Esta función llena los
	 * ComboBoxes con opciones estáticas predefinidas.
	 */
	public void rellenarCombosEstaticos() {
		List<ComboBox<String>> listaComboboxes = new ArrayList<>();
		listaComboboxes.add(comboBoxTienda);
		FuncionesComboBox.rellenarComboBoxEstaticos(listaComboboxes);
	}

	public void formatearTextField() {
		FuncionesManejoFront.eliminarEspacioInicialYFinal(textFieldNombreCarta);
		FuncionesManejoFront.eliminarSimbolosEspeciales(textFieldNombreCarta);
		FuncionesManejoFront.restringirSimbolos(textFieldEditorialCarta);
		FuncionesManejoFront.restringirSimbolos(textFieldColeccion);
		FuncionesManejoFront.restringirSimbolos(textFieldRarezaCarta);
//		FuncionesManejoFront.restringirSimbolos(textFieldNormasCarta);

		FuncionesManejoFront.reemplazarEspaciosMultiples(textFieldNombreCarta);
		FuncionesManejoFront.reemplazarEspaciosMultiples(textFieldEditorialCarta);
		FuncionesManejoFront.reemplazarEspaciosMultiples(textFieldColeccion);
		FuncionesManejoFront.reemplazarEspaciosMultiples(textAreaNormasCarta);
		FuncionesManejoFront.reemplazarEspaciosMultiples(textFieldRarezaCarta);

		FuncionesManejoFront.permitirUnSimbolo(textFieldNombreCarta);
		FuncionesManejoFront.permitirUnSimbolo(textFieldEditorialCarta);
		FuncionesManejoFront.permitirUnSimbolo(textFieldColeccion);
		FuncionesManejoFront.permitirUnSimbolo(textFieldRarezaCarta);
		FuncionesManejoFront.permitirSimbolosEspecificos(textFieldPrecioCartaNormal);
		FuncionesManejoFront.permitirSimbolosEspecificos(textFieldPrecioCartaFoil);
		FuncionesManejoFront.permitirUnSimbolo(busquedaCodigo);

		comboboxNumeroCarta.getEditor().setTextFormatter(FuncionesComboBox.validadorNenteros());
		textFieldIdCarta.setTextFormatter(FuncionesComboBox.validadorNenteros());
//		textFieldPrecioCartaNormal.setTextFormatter(FuncionesComboBox.validadorNdecimales());
//		textFieldPrecioCartaFoil.setTextFormatter(FuncionesComboBox.validadorNdecimales());

		if (AccionFuncionesComunes.TIPO_ACCION.equalsIgnoreCase("aniadir")) {
			textFieldIdCarta.setTextFormatter(FuncionesComboBox.desactivarValidadorNenteros());
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

		// Iterar sobre los TextField y ComboBox en referenciaVentana
		for (Control control : AccionReferencias.getListaTextFields()) {
			if (control instanceof TextField) {
				controls.add(((TextField) control).getText());
			} else if (control instanceof ComboBox<?>) {
				Object selectedItem = ((ComboBox<?>) control).getSelectionModel().getSelectedItem();
				controls.add(selectedItem != null ? selectedItem.toString() : "");
			}
		}

		// Añadir valores de los ComboBoxes de getListaComboboxes() a controls
		for (ComboBox<?> comboBox : AccionReferencias.getListaComboboxes()) {
			Object selectedItem = comboBox.getSelectionModel().getSelectedItem();
			controls.add(selectedItem != null ? selectedItem.toString() : "");
		}

		// Crear y procesar la Carta con los controles recogidos
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
		imagencarta.setImage(null);
		setCartaCache(null);
		ocultarBotonesCartas();
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
	void guardarCartaImportados(ActionEvent event) throws IOException, SQLException {
		enviarReferencias();
		nav.cerrarMenuOpciones();
		AccionAniadir.guardarContenidoLista(false, getCartaCache());
		rellenarCombosEstaticos();
		imagencarta.setImage(null);
		setCartaCache(null);
		ocultarBotonesCartas();
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
		AccionAniadir.guardarContenidoLista(true, null);
		rellenarCombosEstaticos();
		imagencarta.setImage(null);
		setCartaCache(null);
		ocultarBotonesCartas();
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
		imagencarta.setImage(null);
		setCartaCache(null);
	}

	@FXML
	void clonarCartaSeleccionada(ActionEvent event) {

		int num = Ventanas.verVentanaNumero();
		Carta cartaCopiar = getCartaCache();

		for (int i = 0; i < num; i++) {
			Carta cartaModificada = AccionFuncionesComunes.copiarCartaClon(cartaCopiar);
			AccionFuncionesComunes.procesarCartaPorCodigo(cartaModificada, true);
		}
	}

	@FXML
	void eliminarCartaSeleccionado(ActionEvent event) {
		enviarReferencias();
		nav.cerrarMenuOpciones();
		AccionEliminar.eliminarCartaLista();
		rellenarCombosEstaticos();
		imagencarta.setImage(null);
		setCartaCache(null);
		ocultarBotonesCartas();
	}

	@FXML
	void eliminarListaCartas(ActionEvent event) {
		enviarReferencias();
		nav.cerrarMenuOpciones();

		if (!ListasCartasDAO.cartasImportados.isEmpty() && nav.alertaBorradoLista()) {
			// Ocultar botones relacionados con cartas
			ocultarBotonesCartas();

			// Eliminar cada carta de la lista
			for (Carta carta : ListasCartasDAO.cartasImportados) {
				// Eliminar archivo de imagen asociado a la carta
				eliminarArchivoImagen(carta.getDireccionImagenCarta());
			}

			// Limpiar la lista de cartas y la tabla de la interfaz
			ListasCartasDAO.cartasImportados.clear();
			guardarReferencia().getTablaBBDD().getItems().clear();

			// Reiniciar la imagen de la carta y limpiar la ventana
			imagencarta.setImage(null);
			limpiarVentana();
		}

		// Rellenar combos estáticos después de la operación
		rellenarCombosEstaticos();
	}

	// Función para eliminar archivo de imagen
	private void eliminarArchivoImagen(String direccionImagen) {
		if (direccionImagen != null && !direccionImagen.isEmpty()) {
			File archivoImagen = new File(direccionImagen);
			if (archivoImagen.exists()) {
				// Intentar borrar el archivo de la imagen
				if (archivoImagen.delete()) {
					System.out.println("Archivo de imagen eliminado: " + direccionImagen);
				} else {
					System.err.println("No se pudo eliminar el archivo de imagen: " + direccionImagen);
					// Puedes lanzar una excepción aquí si lo prefieres
				}
			}
		}
	}

	// Función para ocultar botones relacionados con cartas
	private void ocultarBotonesCartas() {
		guardarReferencia().getBotonClonarCarta().setVisible(false);
		guardarReferencia().getBotonGuardarCarta().setVisible(false);
		guardarReferencia().getBotonEliminarImportadoCarta().setVisible(false);
	}

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
		enviarReferencias();
		if (!tablaBBDD.isDisabled()) {
			setCartaCache(guardarReferencia().getTablaBBDD().getSelectionModel().getSelectedItem());
			AccionSeleccionar.seleccionarCartas(false);
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
		enviarReferencias();
		if ((event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) && !tablaBBDD.isDisabled()) {
			setCartaCache(guardarReferencia().getTablaBBDD().getSelectionModel().getSelectedItem());
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
				if (WebScrapNodeJSInstall.checkNodeJSVersion()) {
					AccionFuncionesComunes.busquedaPorCodigoImportacion(fichero);
				}

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
	public void busquedaPorCodigo(ActionEvent event) throws IOException, URISyntaxException {
		enviarReferencias();
		if (Utilidades.isInternetAvailable()) {
			String valorCodigo = busquedaCodigo.getText();
			String tipoTienda = comboBoxTienda.getValue();
			if (valorCodigo.isEmpty() || tipoTienda.isEmpty()) {
				return;
			}
			nav.cerrarMenuOpciones();
			AccionControlUI.borrarDatosGraficos();

			AccionFuncionesComunes.cargarRuning();

			CompletableFuture<List<String>> future;

			if (tipoTienda.equalsIgnoreCase("Card Market")) {
				future = WebScrapGoogleCardMarket.iniciarBusquedaGoogle(valorCodigo);
			} else if (tipoTienda.equalsIgnoreCase("ScryFall")) {
				future = WebScrapGoogleScryfall.getCardLinks(valorCodigo);
			} else if (tipoTienda.equals("TCGPlayer")) {
				future = WebScrapGoogleTCGPlayer.urlTCG(valorCodigo);
			} else {
				future = CompletableFuture.completedFuture(new ArrayList<>());
			}

			future.thenAccept(enlaces -> {

				File fichero;
				try {
					fichero = createTempFile(enlaces);

					if (fichero != null) {
						enviarReferencias();
						rellenarCombosEstaticos();
						if (WebScrapNodeJSInstall.checkNodeJSVersion()) {
							AccionFuncionesComunes.busquedaPorCodigoImportacion(fichero);
						}
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			});

			future.exceptionally(ex -> {
				ex.printStackTrace();
				return null; // Manejar errores aquí según sea necesario
			});
		}
	}

	public File createTempFile(List<String> data) throws IOException {

		String tempDirectory = System.getProperty("java.io.tmpdir");

		// Create a temporary file in the system temporary directory
		Path tempFilePath = Files.createTempFile(Paths.get(tempDirectory), "tempFile", ".txt");
		logger.log(Level.INFO, "Temporary file created at: " + tempFilePath.toString());

		// Write data to the temporary file
		Files.write(tempFilePath, data, StandardOpenOption.WRITE);

		// Convert the Path to a File and return it
		return tempFilePath.toFile();
	}

	public void deleteFile(Path filePath) throws IOException {
		Files.delete(filePath);
	}

	/**
	 * Limpia los datos de la pantalla al hacer clic en el botón "Limpiar".
	 */
	@FXML
	void limpiarDatos(ActionEvent event) {
		limpiarVentana();
	}

	public void limpiarVentana() {
		enviarReferencias();
		AccionFuncionesComunes.limpiarDatosPantallaAccion();
		rellenarCombosEstaticos();
		setCartaCache(null);
	}

	/**
	 * Funcion que permite la subida de una
	 *
	 * @param event
	 */
	@FXML
	void nuevaPortada(ActionEvent event) {
		enviarReferencias();
		nav.cerrarMenuOpciones();
		AccionFuncionesComunes.subirPortada();
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
	 * Cierra la ventana asociada a este controlador, si está disponible. Si no se
	 * ha establecido una instancia de ventana (Stage), este método no realiza
	 * ninguna acción.
	 */
	public void closeWindow() {

		stage = estadoStage();
		setCartaCache(null);
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
