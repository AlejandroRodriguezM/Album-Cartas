
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
	private Label alarmaConexionInternet;

	@FXML
	private Label alarmaConexionSql;

	/**
	 * Indicador de progreso.
	 */
	@FXML
	private ProgressIndicator progresoCarga;

	/**
	 * Campo de texto para la dirección de la imagen.
	 */
	@FXML
	private TextField direccionImagen;

	/**
	 * Columna de la tabla para el ID.
	 */
	@FXML
	private TableColumn<Carta, String> id;

	/**
	 * Columna de la tabla para la gradeo.
	 */
	@FXML
	private TableColumn<Carta, String> gradeo;

	/**
	 * Columna de la tabla para el dibujante.
	 */
	@FXML
	private TableColumn<Carta, String> dibujante;

	/**
	 * Columna de la tabla para la editorial.
	 */
	@FXML
	private TableColumn<Carta, String> editorial;

	/**
	 * Columna de la tabla para la fecha.
	 */
	@FXML
	private TableColumn<Carta, String> fecha;

	/**
	 * Columna de la tabla para la firma.
	 */
	@FXML
	private TableColumn<Carta, String> firma;

	/**
	 * Columna de la tabla para el formato.
	 */
	@FXML
	private TableColumn<Carta, String> formato;

	/**
	 * Columna de la tabla para el guionista.
	 */
	@FXML
	private TableColumn<Carta, String> guionista;

	/**
	 * Columna de la tabla para el nombre.
	 */
	@FXML
	private TableColumn<Carta, String> nombre;

	/**
	 * Columna de la tabla para el número.
	 */
	@FXML
	private TableColumn<Carta, String> numero;

	/**
	 * Columna de la tabla para la procedencia.
	 */
	@FXML
	private TableColumn<Carta, String> procedencia;

	/**
	 * Columna de la tabla para la referencia.
	 */
	@FXML
	private TableColumn<Carta, String> referencia;

	/**
	 * Columna de la tabla para la variante.
	 */
	@FXML
	private TableColumn<Carta, String> variante;

	/**
	 * Botón para cancelar la subida de imagenes.
	 */
	@FXML
	private Button botonCancelarSubida;

	/**
	 * Botón para agregar puntuación a un cómic.
	 */
	@FXML
	private Button botonAgregarPuntuacion;

	/**
	 * Botón para borrar una opinión.
	 */
	@FXML
	private Button botonBorrarOpinion;

	/**
	 * Botón para realizar una búsqueda por código.
	 */
	@FXML
	private Button botonBusquedaCodigo;

	/**
	 * Botón para realizar una búsqueda avanzada.
	 */
	@FXML
	private Button botonBusquedaAvanzada;

	/**
	 * Botón para eliminar un cómic.
	 */
	@FXML
	private Button botonEliminar;

	/**
	 * Botón para limpiar campos.
	 */
	@FXML
	private Button botonLimpiar;

	/**
	 * Botón para modificar un cómic.
	 */
	@FXML
	private Button botonModificarCarta;

	/**
	 * Botón para buscar mediante parametro un cómic.
	 */
	@FXML
	private Button botonParametroCarta;

	/**
	 * Botón para vender un cómic.
	 */
	@FXML
	private Button botonVender;

	/**
	 * Botón para acceder a la base de datos.
	 */
	@FXML
	private Button botonbbdd;

	/**
	 * Botón para guardar un comic correctamente para el importado de cartas
	 * mediante fichero.
	 */
	@FXML
	private Button botonGuardarCarta;

	/**
	 * Boton que guarda un cambio en un comic especifico de los importados
	 */
	@FXML
	private Button botonGuardarCambioCarta;

	/**
	 * Boton que elimina un comic seleccionado de los cartas importados mediante
	 * fichero
	 */
	@FXML
	private Button botonEliminarImportadoCarta;

	/**
	 * Boton que sirve para subir una imagen a un comic que escojamos
	 */
	@FXML
	private Button botonSubidaPortada;

	// Campos de texto (TextField)
	/**
	 * Campo de texto para la búsqueda por código.
	 */
	@FXML
	private TextField busquedaCodigo;

	/**
	 * Campo de texto para el dibujante del cómic.
	 */
	@FXML
	private TextField dibujanteCarta;

	/**
	 * Campo de texto para la editorial del cómic.
	 */
	@FXML
	private TextField editorialCarta;

	/**
	 * Campo de texto para la firma del cómic.
	 */
	@FXML
	private TextField firmaCarta;

	/**
	 * Campo de texto para el guionista del cómic.
	 */
	@FXML
	private TextField guionistaCarta;

	/**
	 * Campo de texto para el ID del cómic a tratar en modificacion.
	 */
	@FXML
	private TextField idCartaTratar;

	/**
	 * Campo de texto para el codigo del cómic a tratar en modificacion o para
	 * añadir.
	 */
	@FXML
	private TextField codigoCartaTratar;

	/**
	 * Campo de texto para el nombre del cómic.
	 */
	@FXML
	private TextField nombreCarta;

	/**
	 * Campo de texto para el nombre del Key Issue del cómic.
	 */
	@FXML
	private TextField nombreKeyIssue;

	/**
	 * Campo de texto para el precio del cómic.
	 */
	@FXML
	private TextField precioCarta;

	/**
	 * Campo de texto para la URL de referencia del cómic.
	 */
	@FXML
	private TextField urlReferencia;

	/**
	 * Campo de texto para la variante del cómic.
	 */
	@FXML
	private TextField varianteCarta;

	// Etiquetas (Label)
	/**
	 * Etiqueta para mostrar la puntuación.
	 */
	@FXML
	private Label labelPuntuacion;

	/**
	 * Etiqueta para mostrar la gradeo.
	 */
	@FXML
	private Label labelGradeo;

	/**
	 * Etiqueta para mostrar el dibujante.
	 */
	@FXML
	private Label labelDibujante;

	/**
	 * Etiqueta para mostrar la editorial.
	 */
	@FXML
	private Label labelEditorial;

	/**
	 * Etiqueta para mostrar el estado.
	 */
	@FXML
	private Label labelEstado;

	/**
	 * Etiqueta para mostrar la fecha.
	 */
	@FXML
	private Label labelFecha;

	/**
	 * Etiqueta para mostrar la firma.
	 */
	@FXML
	private Label labelFirma;

	/**
	 * Etiqueta para mostrar el formato.
	 */
	@FXML
	private Label labelFormato;

	/**
	 * Etiqueta para mostrar el guionista.
	 */
	@FXML
	private Label labelGuionista;

	/**
	 * Etiqueta para mostrar el ID en modificacion.
	 */
	@FXML
	private Label labelId;

	/**
	 * Etiqueta para mostrar el codigo en modificacion o aniadir.
	 */
	@FXML
	private Label labelCodigo;

	/**
	 * Etiqueta para mostrar el Key Issue.
	 */
	@FXML
	private Label labelKey;

	/**
	 * Etiqueta para mostrar la portada.
	 */
	@FXML
	private Label labelPortada;

	/**
	 * Etiqueta para mostrar el precio.
	 */
	@FXML
	private Label labelPrecio;

	/**
	 * Etiqueta para mostrar la procedencia.
	 */
	@FXML
	private Label labelProcedencia;

	/**
	 * Etiqueta para mostrar la referencia.
	 */
	@FXML
	private Label labelReferencia;

	// Otros controles (ComboBox, DatePicker, TableView, etc.)
	/**
	 * ComboBox para seleccionar el estado del cómic.
	 */
	@FXML
	private ComboBox<String> estadoCarta;

	/**
	 * DatePicker para seleccionar la fecha de publicación del cómic.
	 */
	@FXML
	private DatePicker fechaCarta;

	/**
	 * ComboBox para seleccionar el formato del cómic.
	 */
	@FXML
	private ComboBox<String> formatoCarta;

	/**
	 * ComboBox para seleccionar el número de gradeo del cómic.
	 */
	@FXML
	private ComboBox<String> gradeoCarta;

	/**
	 * ComboBox para seleccionar el número del cómic.
	 */
	@FXML
	private ComboBox<String> numeroCarta;

	/**
	 * ComboBox para seleccionar la procedencia del cómic.
	 */
	@FXML
	private ComboBox<String> procedenciaCarta;

	/**
	 * ComboBox para seleccionar la puntuación en el menú.
	 */
	@FXML
	private ComboBox<String> puntuacionMenu;

	/**
	 * TableView para mostrar la lista de cómics.
	 */
	@FXML
	private TableView<Carta> tablaBBDD;

	/**
	 * ImageView para mostrar la imagen del cómic.
	 */
	@FXML
	private ImageView imagencomic;

	/**
	 * ImageView para mostrar la carga de imagen del comic.
	 */
	@FXML
	private ImageView cargaImagen;

	/**
	 * TextArea para mostrar información de texto.
	 */
	@FXML
	private TextArea prontInfo;

	/**
	 * VBox para el diseño de la interfaz.
	 */
	@FXML
	private VBox rootVBox;

	@FXML
	private MenuBar menuNavegacion;

	@FXML
	private Menu navegacionEstadistica;

	@FXML
	private Menu navegacionOpciones;

	@FXML
	private MenuItem menuImportarFichero;

	@FXML
	private MenuItem navegacionMostrarEstadistica;

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

			AccionControlUI.listas_autocompletado();

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
		FuncionesComboBox.rellenarComboBoxEstaticos(referenciaVentana.getComboboxes());
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
		imagencomic.setImage(null);
		imagencomic.setVisible(true);
		botonCancelarSubida.setVisible(true);
		botonBusquedaCodigo.setDisable(true);
		botonSubidaPortada.setDisable(true);
		referenciaVentana.getMenu_Importar_Fichero_CodigoBarras().setDisable(true);
		AlarmaList.iniciarAnimacionCargaImagen(cargaImagen);
		menuImportarFichero.setDisable(true);
		FuncionesManejoFront.cambiarEstadoMenuBar(true, referenciaVentana);
		rellenarCombosEstaticos();
	}

	private Task<Void> createSearchTask(String valorCodigo) {
		return new Task<>() {
			@Override
			protected Void call() throws Exception {
				if (isCancelled() || !referenciaVentana.getStage().isShowing()) {
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
	 * Método asociado al evento de acción que se dispara al seleccionar la opción
	 * "Ver Menú Código de Barras". Invoca el método correspondiente en el objeto
	 * 'nav' para mostrar el menú de códigos de barras.
	 *
	 * @param event Objeto que representa el evento de acción.
	 */
	@FXML
	void verMenuCodigoBarras(ActionEvent event) {
		enviarReferencias();
		if ("aniadir".equals(AccionFuncionesComunes.TIPO_ACCION)) {
			nav.verMenuCodigosBarra();
		}
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
