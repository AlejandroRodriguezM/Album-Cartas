package funcionesManagment;

import java.util.List;

import cartaManagement.Carta;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class AccionReferencias {

	/**
	 * Columna de la tabla para la procedencia.
	 */
	private TableColumn<Carta, String> procedencia;

	/**
	 * Columna de la tabla para la referencia.
	 */
	private TableColumn<Carta, String> referencia;

	/**
	 * Tabla que muestra información sobre cómics.
	 */
	public TableView<Carta> tablaBBDD;

	/**
	 * Contenedor de la interfaz gráfica.
	 */
	private VBox rootVBox;

	/**
	 * Contenedor del contenido.
	 */
	private VBox vboxContenido;

	/**
	 * Imagen de fondo.
	 */
	private ImageView backgroundImage;

	/**
	 * Panel de anclaje principal.
	 */
	private AnchorPane rootAnchorPane;

	/**
	 * Contenedor de imágenes.
	 */
	private VBox vboxImage;

	/**
	 * Panel de anclaje para información.
	 */
	private AnchorPane anchoPaneInfo;

	/**
	 * Botón para modificar información.
	 */
	private Button botonModificar;

	/**
	 * Botón para introducir información.
	 */
	private Button botonIntroducir;

	/**
	 * Botón para eliminar información.
	 */
	private Button botonEliminar;

	private Button botonComprimirPortadas;

	private Button botonReCopiarPortadas;

	private Rectangle barraCambioAltura;

	private Label alarmaConexionInternet;

	/**
	 * Campo de texto para la dirección de la imagen.
	 */
	private TextField direccionImagen;

	/**
	 * Columna de la tabla para el ID.
	 */
	private TableColumn<Carta, String> ID;

	/**
	 * Columna de la tabla para la gradeo.
	 */
	private TableColumn<Carta, String> gradeo;

	/**
	 * Columna de la tabla para el formato.
	 */
	private TableColumn<Carta, String> formato;

	/**
	 * Columna de la tabla para mostrar el nombre del cómic.
	 */
	private TableColumn<Carta, String> nombre;

	/**
	 * Columna de la tabla para mostrar el número del cómic.
	 */
	private TableColumn<Carta, String> numero;

	/**
	 * Columna de la tabla para mostrar la editorial del cómic.
	 */
	private TableColumn<Carta, String> editorial;

	/**
	 * Botón para cancelar la subida de imagenes.
	 */
	private Button botonCancelarSubida;

	/**
	 * Botón para borrar una opinión.
	 */
	private Button botonBorrarOpinion;

	/**
	 * Botón para realizar una búsqueda por código.
	 */
	private Button botonBusquedaCodigo;

	/**
	 * Botón para realizar una búsqueda avanzada.
	 */
	private Button botonBusquedaAvanzada;

	/**
	 * Botón para limpiar campos.
	 */
	private Button botonLimpiar;

	/**
	 * Botón para modificar un cómic.
	 */
	private Button botonModificarCarta;

	/**
	 * Botón para buscar mediante parametro un cómic.
	 */
	private Button botonParametroCarta;

	/**
	 * Botón para vender un cómic.
	 */
	private Button botonVender;

	/**
	 * Botón para acceder a la base de datos.
	 */
	private Button botonbbdd;

	/**
	 * Botón para guardar un comic correctamente para el importado de comics
	 * mediante fichero.
	 */
	private Button botonGuardarCarta;

	/**
	 * Boton que guarda un cambio en un comic especifico de los importados
	 */
	private Button botonGuardarCambioCarta;

	/**
	 * Boton que elimina un comic seleccionado de los comics importados mediante
	 * fichero
	 */
	private Button botonEliminarImportadoCarta;

	/**
	 * Boton que sirve para subir una imagen a un comic que escojamos
	 */
	private Button botonSubidaPortada;

	// Campos de texto (TextField)
	/**
	 * Campo de texto para la búsqueda por código.
	 */
	private TextField busquedaCodigo;

	/**
	 * Campo de texto para la editorial del cómic.
	 */
	private TextField editorialCarta;

	/**
	 * Campo de texto para el ID del cómic a tratar en modificacion.
	 */
	private TextField idCartaTratar;

	/**
	 * Campo de texto para el codigo del cómic a tratar en modificacion o para
	 * añadir.
	 */
	private TextField codigoCartaTratar;

	/**
	 * Campo de texto para el nombre del cómic.
	 */
	private TextField nombreCarta;

	/**
	 * Campo de texto para el precio del cómic.
	 */
	private TextField precioCarta;

	/**
	 * Campo de texto para la URL de referencia del cómic.
	 */
	private TextField urlReferencia;

	/**
	 * Etiqueta para mostrar la gradeo.
	 */
	private Label label_gradeo;

	/**
	 * Etiqueta para mostrar la editorial.
	 */
	private Label label_editorial;

	/**
	 * Etiqueta para mostrar el estado.
	 */
	private Label label_estado;

	/**
	 * Etiqueta para mostrar la fecha.
	 */
	private Label label_fecha;

	/**
	 * Etiqueta para mostrar el formato.
	 */
	private Label label_formato;

	/**
	 * Etiqueta para mostrar el ID en modificacion.
	 */
	private Label label_id_mod;

	/**
	 * Etiqueta para mostrar la portada.
	 */
	private Label label_portada;

	/**
	 * Etiqueta para mostrar el precio.
	 */
	private Label label_precio;

	/**
	 * Etiqueta para mostrar la referencia.
	 */
	private Label label_referencia;

	// Otros controles (ComboBox, DatePicker, TableView, etc.)
	/**
	 * ComboBox para seleccionar el estado del cómic.
	 */
	private ComboBox<String> estadoCarta;

	/**
	 * ComboBox para seleccionar el formato del cómic.
	 */
	private ComboBox<String> tituloCarta;

	/**
	 * ComboBox para seleccionar el formato del cómic.
	 */
	private ComboBox<String> formatoCarta;

	/**
	 * ComboBox para seleccionar el número de gradeo del cómic.
	 */
	private ComboBox<String> gradeoCarta;

	/**
	 * ComboBox para seleccionar el número del cómic.
	 */
	private ComboBox<String> numeroCarta;

	/**
	 * ImageView para mostrar la imagen del cómic.
	 */
	private ImageView imagencomic;

	/**
	 * ImageView para mostrar la carga de imagen del comic.
	 */
	private ImageView cargaImagen;

	/**
	 * TextArea para mostrar información de texto.
	 */
	private TextArea prontInfo;

	private Label prontInfoLabel;

	private MenuItem menu_Importar_Fichero_CodigoBarras;

	private MenuItem menuComprobarApis;

	private MenuItem menu_comic_aniadir;

	private MenuItem menu_comic_eliminar;

	private MenuItem menu_comic_modificar;

	private MenuItem menu_estadistica_estadistica;

	private MenuBar menu_navegacion;

	private Menu navegacion_cerrar;

	private Menu navegacion_comic;

	private Menu navegacion_estadistica;

	private Label alarmaConexionSql;

	/**
	 * Botón para mostrar un parámetro.
	 */

	private Button botonMostrarParametro;

	/**
	 * Botón que permite imprimir el resultado de una busqueda por parametro
	 */

	private Button botonImprimir;

	/**
	 * Botón que permite guardar el resultado de una busqueda por parametro
	 */

	private Button botonGuardarResultado;

	private Button botonMostrarGuardados;

	/**
	 * Campo de texto para realizar una búsqueda general.
	 */

	private TextField busquedaGeneral;

	/**
	 * Menú de archivo con opciones relacionadas con la base de datos.
	 */

	private MenuItem menu_archivo_cerrar, menu_archivo_delete, menu_archivo_desconectar, menu_archivo_excel,
			menu_archivo_importar, menu_archivo_sobreMi;

	/**
	 * Menú relacionado con estadísticas de cómics.
	 */

	private MenuItem menu_estadistica_comprados, menu_estadistica_firmados, menu_estadistica_key_issue,
			menu_estadistica_posesion, menu_estadistica_puntuados, menu_estadistica_vendidos;

	private MenuItem menu_archivo_avanzado;

	/**
	 * Selector para el nombre de la editorial.
	 */
	private ComboBox<String> nombreEditorial;

	/**
	 * Selector para el nombre del formato.
	 */
	private ComboBox<String> nombreFormato;

	/**
	 * Declaramos una lista de ComboBox de tipo String
	 */
	private static List<ComboBox<String>> comboboxes;

	private static ObservableList<Control> listaTextFields;
	private static ObservableList<Button> listaBotones;
	private static ObservableList<Node> listaElementosFondo;

	private static List<TableColumn<Carta, String>> columnasTabla;

	private ProgressIndicator progresoCarga;

	private Button botonActualizarDatos;

	private Button botonActualizarPortadas;

	private Button botonActualizarSoftware;

	private Button botonActualizarTodo;

	private Button botonDescargarPdf;

	private Button botonDescargarSQL;

	private Button botonNormalizarDB;

	private CheckBox checkFirmas;

	private ComboBox<String> comboPreviews;

	private Label labelComprobar;

	private Label labelVersion;

	private Label prontInfoEspecial;

	private Label prontInfoPreviews;

	private Label prontInfoPortadas;

	private Stage stage;

	public TableColumn<Carta, String> getProcedencia() {
		return procedencia;
	}

	public TableColumn<Carta, String> getReferencia() {
		return referencia;
	}

	public TableView<Carta> getTablaBBDD() {
		return tablaBBDD;
	}

	public VBox getRootVBox() {
		return rootVBox;
	}

	public VBox getVboxContenido() {
		return vboxContenido;
	}

	public ImageView getBackgroundImage() {
		return backgroundImage;
	}

	public AnchorPane getRootAnchorPane() {
		return rootAnchorPane;
	}

	public VBox getVboxImage() {
		return vboxImage;
	}

	public AnchorPane getAnchoPaneInfo() {
		return anchoPaneInfo;
	}

	public Button getBotonModificar() {
		return botonModificar;
	}

	public Button getBotonIntroducir() {
		return botonIntroducir;
	}

	public Button getBotonEliminar() {
		return botonEliminar;
	}

	public Rectangle getBarraCambioAltura() {
		return barraCambioAltura;
	}

	public Label getAlarmaConexionInternet() {
		return alarmaConexionInternet;
	}

	public TextField getDireccionImagen() {
		return direccionImagen;
	}

	public TableColumn<Carta, String> getID() {
		return ID;
	}

	public TableColumn<Carta, String> getGradeo() {
		return gradeo;
	}

	public TableColumn<Carta, String> getFormato() {
		return formato;
	}

	public TableColumn<Carta, String> getNombre() {
		return nombre;
	}

	public TableColumn<Carta, String> getNumero() {
		return numero;
	}

	public TableColumn<Carta, String> getEditorial() {
		return editorial;
	}

	public Button getBotonCancelarSubida() {
		return botonCancelarSubida;
	}

	public Button getBotonBorrarOpinion() {
		return botonBorrarOpinion;
	}

	public Button getBotonBusquedaCodigo() {
		return botonBusquedaCodigo;
	}

	public Button getBotonBusquedaAvanzada() {
		return botonBusquedaAvanzada;
	}

	public Button getBotonLimpiar() {
		return botonLimpiar;
	}

	public Button getBotonModificarCarta() {
		return botonModificarCarta;
	}

	public Button getBotonParametroCarta() {
		return botonParametroCarta;
	}

	public Button getBotonVender() {
		return botonVender;
	}

	public Button getBotonbbdd() {
		return botonbbdd;
	}

	public Button getBotonGuardarCarta() {
		return botonGuardarCarta;
	}

	public Button getBotonGuardarCambioCarta() {
		return botonGuardarCambioCarta;
	}

	public Button getBotonEliminarImportadoCarta() {
		return botonEliminarImportadoCarta;
	}

	public Button getBotonSubidaPortada() {
		return botonSubidaPortada;
	}

	public Label getLabel_gradeo() {
		return label_gradeo;
	}

	public Label getLabel_editorial() {
		return label_editorial;
	}

	public Label getLabel_estado() {
		return label_estado;
	}

	public Label getLabel_fecha() {
		return label_fecha;
	}

	public Label getLabel_formato() {
		return label_formato;
	}

	public Label getLabel_id_mod() {
		return label_id_mod;
	}

	public Label getLabel_portada() {
		return label_portada;
	}

	public Label getLabel_precio() {
		return label_precio;
	}

	public Label getLabel_referencia() {
		return label_referencia;
	}

	public ImageView getImagencomic() {
		return imagencomic;
	}

	public ImageView getCargaImagen() {
		return cargaImagen;
	}

	public TextArea getProntInfo() {
		return prontInfo;
	}

	public MenuItem getMenu_Importar_Fichero_CodigoBarras() {
		return menu_Importar_Fichero_CodigoBarras;
	}

	public MenuItem getMenu_comic_aniadir() {
		return menu_comic_aniadir;
	}

	public MenuItem getMenu_comic_eliminar() {
		return menu_comic_eliminar;
	}

	public MenuItem getMenu_comic_modificar() {
		return menu_comic_modificar;
	}

	public MenuItem getMenu_estadistica_estadistica() {
		return menu_estadistica_estadistica;
	}

	public MenuBar getMenu_navegacion() {
		return menu_navegacion;
	}

	public Menu getNavegacion_cerrar() {
		return navegacion_cerrar;
	}

	public Menu getNavegacion_comic() {
		return navegacion_comic;
	}

	public Menu getNavegacion_estadistica() {
		return navegacion_estadistica;
	}

	public Label getAlarmaConexionSql() {
		return alarmaConexionSql;
	}

	public Button getBotonMostrarParametro() {
		return botonMostrarParametro;
	}

	public Button getBotonImprimir() {
		return botonImprimir;
	}

	public Button getBotonGuardarResultado() {
		return botonGuardarResultado;
	}

	public Button getBotonMostrarGuardados() {
		return botonMostrarGuardados;
	}

	public TextField getBusquedaGeneral() {
		return busquedaGeneral;
	}

	public MenuItem getMenu_archivo_cerrar() {
		return menu_archivo_cerrar;
	}

	public MenuItem getMenu_archivo_delete() {
		return menu_archivo_delete;
	}

	public MenuItem getMenu_archivo_desconectar() {
		return menu_archivo_desconectar;
	}

	public MenuItem getMenu_archivo_excel() {
		return menu_archivo_excel;
	}

	public MenuItem getMenu_archivo_importar() {
		return menu_archivo_importar;
	}

	public MenuItem getMenu_archivo_sobreMi() {
		return menu_archivo_sobreMi;
	}

	public MenuItem getMenu_estadistica_comprados() {
		return menu_estadistica_comprados;
	}

	public MenuItem getMenu_estadistica_firmados() {
		return menu_estadistica_firmados;
	}

	public MenuItem getMenu_estadistica_key_issue() {
		return menu_estadistica_key_issue;
	}

	public MenuItem getMenu_estadistica_posesion() {
		return menu_estadistica_posesion;
	}

	public MenuItem getMenu_estadistica_puntuados() {
		return menu_estadistica_puntuados;
	}

	public MenuItem getMenu_estadistica_vendidos() {
		return menu_estadistica_vendidos;
	}

	public MenuItem getMenu_archivo_avanzado() {
		return menu_archivo_avanzado;
	}

	public ProgressIndicator getProgresoCarga() {
		return progresoCarga;
	}

	public List<ComboBox<String>> getComboboxes() {
		return comboboxes;
	}

	public static ObservableList<Button> getListaBotones() {
		return listaBotones;
	}

	public ObservableList<Node> getListaElementosFondo() {
		return listaElementosFondo;
	}

	public List<TableColumn<Carta, String>> getColumnasTabla() {
		return columnasTabla;
	}

	public ObservableList<Control> getListaTextFields() {
		return listaTextFields;
	}

	public Button getBotonActualizarDatos() {
		return botonActualizarDatos;
	}

	public Button getBotonActualizarPortadas() {
		return botonActualizarPortadas;
	}

	public Button getBotonActualizarSoftware() {
		return botonActualizarSoftware;
	}

	public Button getBotonActualizarTodo() {
		return botonActualizarTodo;
	}

	public Button getBotonDescargarPdf() {
		return botonDescargarPdf;
	}

	public Button getBotonDescargarSQL() {
		return botonDescargarSQL;
	}

	public Button getBotonNormalizarDB() {
		return botonNormalizarDB;
	}

	public CheckBox getCheckFirmas() {
		return checkFirmas;
	}

	public ComboBox<String> getComboPreviews() {
		return comboPreviews;
	}

	public Label getLabelComprobar() {
		return labelComprobar;
	}

	public Label getLabelVersion() {
		return labelVersion;
	}

	public Label getProntInfoEspecial() {
		return prontInfoEspecial;
	}

	public Label getProntInfoPreviews() {
		return prontInfoPreviews;
	}

	public Label getProntInfoPortadas() {
		return prontInfoPortadas;
	}

	public Label getProntInfoLabel() {
		return prontInfoLabel;
	}

	public Stage getStage() {
		return stage;
	}

	public Button getBotonComprimirPortadas() {
		return botonComprimirPortadas;
	}

	public Button getBotonReCopiarPortadas() {
		return botonReCopiarPortadas;
	}

	public void setBotonComprimirPortadas(Button botonComprimirPortadas) {
		this.botonComprimirPortadas = botonComprimirPortadas;
	}

	public void setBotonReCopiarPortadas(Button botonReCopiarPortadas) {
		this.botonReCopiarPortadas = botonReCopiarPortadas;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setBotonActualizarDatos(Button botonActualizarDatos) {
		this.botonActualizarDatos = botonActualizarDatos;
	}

	public void setBotonActualizarPortadas(Button botonActualizarPortadas) {
		this.botonActualizarPortadas = botonActualizarPortadas;
	}

	public void setBotonActualizarSoftware(Button botonActualizarSoftware) {
		this.botonActualizarSoftware = botonActualizarSoftware;
	}

	public void setBotonActualizarTodo(Button botonActualizarTodo) {
		this.botonActualizarTodo = botonActualizarTodo;
	}

	public void setBotonDescargarPdf(Button botonDescargarPdf) {
		this.botonDescargarPdf = botonDescargarPdf;
	}

	public void setBotonDescargarSQL(Button botonDescargarSQL) {
		this.botonDescargarSQL = botonDescargarSQL;
	}

	public void setBotonNormalizarDB(Button botonNormalizarDB) {
		this.botonNormalizarDB = botonNormalizarDB;
	}

	public void setCheckFirmas(CheckBox checkFirmas) {
		this.checkFirmas = checkFirmas;
	}

	public void setComboPreviews(ComboBox<String> comboPreviews) {
		this.comboPreviews = comboPreviews;
	}

	public void setLabelComprobar(Label labelComprobar) {
		this.labelComprobar = labelComprobar;
	}

	public void setLabelVersion(Label labelVersion) {
		this.labelVersion = labelVersion;
	}

	public void setProntInfoEspecial(Label prontInfoEspecial) {
		this.prontInfoEspecial = prontInfoEspecial;
	}

	public void setProntInfoPreviews(Label prontInfoPreviews) {
		this.prontInfoPreviews = prontInfoPreviews;
	}

	public void setProntInfoPortadas(Label prontInfoPortadas) {
		this.prontInfoPortadas = prontInfoPortadas;
	}

	public void setListaTextFields(ObservableList<Control> listaTextFields) {
		AccionReferencias.listaTextFields = listaTextFields;
	}

	public static void setColumnasTabla(List<TableColumn<Carta, String>> columnasTabla) {
		AccionReferencias.columnasTabla = columnasTabla;
	}

	public static void setComboboxes(List<ComboBox<String>> comboboxes) {
		AccionReferencias.comboboxes = comboboxes;
	}

	public static void setListaBotones(ObservableList<Button> listaBotones) {
		AccionReferencias.listaBotones = listaBotones;
	}

	public static void setListaElementosFondo(ObservableList<Node> listaElementosFondo) {
		AccionReferencias.listaElementosFondo = listaElementosFondo;
	}

	public void setComboBoxes(List<ComboBox<String>> comboBoxes) {
		comboboxes = comboBoxes;
	}

	public void setProcedencia(TableColumn<Carta, String> procedencia) {
		this.procedencia = procedencia;
	}

	public void setReferencia(TableColumn<Carta, String> referencia) {
		this.referencia = referencia;
	}

	public void setTablaBBDD(TableView<Carta> tablaBBDD) {
		this.tablaBBDD = tablaBBDD;
	}

	public void setRootVBox(VBox rootVBox) {
		this.rootVBox = rootVBox;
	}

	public void setVboxContenido(VBox vboxContenido) {
		this.vboxContenido = vboxContenido;
	}

	public void setBackgroundImage(ImageView backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	public void setRootAnchorPane(AnchorPane rootAnchorPane) {
		this.rootAnchorPane = rootAnchorPane;
	}

	public void setVboxImage(VBox vboxImage) {
		this.vboxImage = vboxImage;
	}

	public void setAnchoPaneInfo(AnchorPane anchoPaneInfo) {
		this.anchoPaneInfo = anchoPaneInfo;
	}

	public void setBotonModificar(Button botonModificar) {
		this.botonModificar = botonModificar;
	}

	public void setBotonIntroducir(Button botonIntroducir) {
		this.botonIntroducir = botonIntroducir;
	}

	public void setBotonEliminar(Button botonEliminar) {
		this.botonEliminar = botonEliminar;
	}

	public void setBarraCambioAltura(Rectangle barraCambioAltura) {
		this.barraCambioAltura = barraCambioAltura;
	}

	public void setAlarmaConexionInternet(Label alarmaConexionInternet) {
		this.alarmaConexionInternet = alarmaConexionInternet;
	}

	public void setDireccionImagen(TextField direccionImagen) {
		this.direccionImagen = direccionImagen;
	}

	public void setID(TableColumn<Carta, String> iD) {
		ID = iD;
	}

	public void setGradeo(TableColumn<Carta, String> gradeo) {
		this.gradeo = gradeo;
	}

	public void setFormato(TableColumn<Carta, String> formato) {
		this.formato = formato;
	}

	public void setNombre(TableColumn<Carta, String> nombre) {
		this.nombre = nombre;
	}

	public void setNumero(TableColumn<Carta, String> numero) {
		this.numero = numero;
	}

	public void setEditorial(TableColumn<Carta, String> editorial) {
		this.editorial = editorial;
	}

	public void setBotonCancelarSubida(Button botonCancelarSubida) {
		this.botonCancelarSubida = botonCancelarSubida;
	}

	public void setBotonBorrarOpinion(Button botonBorrarOpinion) {
		this.botonBorrarOpinion = botonBorrarOpinion;
	}

	public void setBotonBusquedaCodigo(Button botonBusquedaCodigo) {
		this.botonBusquedaCodigo = botonBusquedaCodigo;
	}

	public void setBotonBusquedaAvanzada(Button botonBusquedaAvanzada) {
		this.botonBusquedaAvanzada = botonBusquedaAvanzada;
	}

	public void setBotonLimpiar(Button botonLimpiar) {
		this.botonLimpiar = botonLimpiar;
	}

	public void setBotonModificarCarta(Button botonModificarCarta) {
		this.botonModificarCarta = botonModificarCarta;
	}

	public void setBotonParametroCarta(Button botonParametroCarta) {
		this.botonParametroCarta = botonParametroCarta;
	}

	public void setBotonVender(Button botonVender) {
		this.botonVender = botonVender;
	}

	public void setBotonbbdd(Button botonbbdd) {
		this.botonbbdd = botonbbdd;
	}

	public void setBotonGuardarCarta(Button botonGuardarCarta) {
		this.botonGuardarCarta = botonGuardarCarta;
	}

	public void setBotonGuardarCambioCarta(Button botonGuardarCambioCarta) {
		this.botonGuardarCambioCarta = botonGuardarCambioCarta;
	}

	public void setBotonEliminarImportadoCarta(Button botonEliminarImportadoCarta) {
		this.botonEliminarImportadoCarta = botonEliminarImportadoCarta;
	}

	public void setBotonSubidaPortada(Button botonSubidaPortada) {
		this.botonSubidaPortada = botonSubidaPortada;
	}

	public void setLabel_gradeo(Label label_gradeo) {
		this.label_gradeo = label_gradeo;
	}

	public void setLabel_editorial(Label label_editorial) {
		this.label_editorial = label_editorial;
	}

	public void setLabel_estado(Label label_estado) {
		this.label_estado = label_estado;
	}

	public void setLabel_fecha(Label label_fecha) {
		this.label_fecha = label_fecha;
	}

	public void setLabel_formato(Label label_formato) {
		this.label_formato = label_formato;
	}

	public void setLabel_id_mod(Label label_id_mod) {
		this.label_id_mod = label_id_mod;
	}

	public void setLabel_portada(Label label_portada) {
		this.label_portada = label_portada;
	}

	public void setLabel_precio(Label label_precio) {
		this.label_precio = label_precio;
	}

	public void setLabel_referencia(Label label_referencia) {
		this.label_referencia = label_referencia;
	}

	public void setImagencomic(ImageView imagencomic) {
		this.imagencomic = imagencomic;
	}

	public void setCargaImagen(ImageView cargaImagen) {
		this.cargaImagen = cargaImagen;
	}

	public void setProntInfo(TextArea prontInfo) {
		this.prontInfo = prontInfo;
	}

	public void setProntInfoLabel(Label prontInfoLabel) {
		this.prontInfoLabel = prontInfoLabel;
	}

	public void setMenu_Importar_Fichero_CodigoBarras(MenuItem menu_Importar_Fichero_CodigoBarras) {
		this.menu_Importar_Fichero_CodigoBarras = menu_Importar_Fichero_CodigoBarras;
	}

	public void setMenu_comprobar_apis(MenuItem menuComprobarApis) {
		this.menuComprobarApis = menuComprobarApis;
	}

	public MenuItem getMenu_comprobar_apis() {
		return menuComprobarApis;
	}

	public void setMenu_comic_aniadir(MenuItem menu_comic_aniadir) {
		this.menu_comic_aniadir = menu_comic_aniadir;
	}

	public void setMenu_comic_eliminar(MenuItem menu_comic_eliminar) {
		this.menu_comic_eliminar = menu_comic_eliminar;
	}

	public void setMenu_comic_modificar(MenuItem menu_comic_modificar) {
		this.menu_comic_modificar = menu_comic_modificar;
	}

	public void setMenu_estadistica_estadistica(MenuItem menu_estadistica_estadistica) {
		this.menu_estadistica_estadistica = menu_estadistica_estadistica;
	}

	public void setMenu_navegacion(MenuBar menu_navegacion) {
		this.menu_navegacion = menu_navegacion;
	}

	public void setNavegacion_Opciones(Menu navegacion_cerrar) {
		this.navegacion_cerrar = navegacion_cerrar;
	}

	public void setNavegacion_comic(Menu navegacion_comic) {
		this.navegacion_comic = navegacion_comic;
	}

	public void setNavegacion_estadistica(Menu navegacion_estadistica) {
		this.navegacion_estadistica = navegacion_estadistica;
	}

	public void setAlarmaConexionSql(Label alarmaConexionSql) {
		this.alarmaConexionSql = alarmaConexionSql;
	}

	public void setBotonMostrarParametro(Button botonMostrarParametro) {
		this.botonMostrarParametro = botonMostrarParametro;
	}

	public void setBotonImprimir(Button botonImprimir) {
		this.botonImprimir = botonImprimir;
	}

	public void setBotonGuardarResultado(Button botonGuardarResultado) {
		this.botonGuardarResultado = botonGuardarResultado;
	}

	public void setBotonMostrarGuardados(Button botonMostrarGuardados) {
		this.botonMostrarGuardados = botonMostrarGuardados;
	}

	public void setMenu_archivo_cerrar(MenuItem menu_archivo_cerrar) {
		this.menu_archivo_cerrar = menu_archivo_cerrar;
	}

	public void setMenu_archivo_delete(MenuItem menu_archivo_delete) {
		this.menu_archivo_delete = menu_archivo_delete;
	}

	public void setMenu_archivo_desconectar(MenuItem menu_archivo_desconectar) {
		this.menu_archivo_desconectar = menu_archivo_desconectar;
	}

	public void setMenu_archivo_excel(MenuItem menu_archivo_excel) {
		this.menu_archivo_excel = menu_archivo_excel;
	}

	public void setMenu_archivo_importar(MenuItem menu_archivo_importar) {
		this.menu_archivo_importar = menu_archivo_importar;
	}

	public void setMenu_archivo_sobreMi(MenuItem menu_archivo_sobreMi) {
		this.menu_archivo_sobreMi = menu_archivo_sobreMi;
	}

	public void setMenu_estadistica_comprados(MenuItem menu_estadistica_comprados) {
		this.menu_estadistica_comprados = menu_estadistica_comprados;
	}

	public void setMenu_estadistica_firmados(MenuItem menu_estadistica_firmados) {
		this.menu_estadistica_firmados = menu_estadistica_firmados;
	}

	public void setMenu_estadistica_key_issue(MenuItem menu_estadistica_key_issue) {
		this.menu_estadistica_key_issue = menu_estadistica_key_issue;
	}

	public void setMenu_estadistica_posesion(MenuItem menu_estadistica_posesion) {
		this.menu_estadistica_posesion = menu_estadistica_posesion;
	}

	public void setMenu_estadistica_puntuados(MenuItem menu_estadistica_puntuados) {
		this.menu_estadistica_puntuados = menu_estadistica_puntuados;
	}

	public void setMenu_estadistica_vendidos(MenuItem menu_estadistica_vendidos) {
		this.menu_estadistica_vendidos = menu_estadistica_vendidos;
	}

	public void setMenu_archivo_avanzado(MenuItem menu_archivo_avanzado) {
		this.menu_archivo_avanzado = menu_archivo_avanzado;
	}

	public void setProgresoCarga(ProgressIndicator progresoCarga) {
		this.progresoCarga = progresoCarga;
	}

	// ComboBox

	public ComboBox<String> getEstadoCarta() {
		return estadoCarta;
	}

	public ComboBox<String> getFormatoCarta() {
		return formatoCarta;
	}

	public ComboBox<String> getGradeoCarta() {
		return gradeoCarta;
	}

	public ComboBox<String> getNumeroCarta() {
		return numeroCarta;
	}

	public ComboBox<String> getNombreEditorial() {
		return nombreEditorial;
	}

	public ComboBox<String> getNombreFormato() {
		return nombreFormato;
	}

	public ComboBox<String> getTituloCarta() {
		return tituloCarta;
	}

	public void setEstadoCarta(ComboBox<String> estadoCarta) {
		this.estadoCarta = estadoCarta;
	}

	public void setTituloCarta(ComboBox<String> tituloCarta) {
		this.tituloCarta = tituloCarta;
	}

	public void setNombreEditorial(ComboBox<String> nombreEditorial) {
		this.nombreEditorial = nombreEditorial;
	}

	public void setNombreFormato(ComboBox<String> nombreFormato) {
		this.nombreFormato = nombreFormato;
	}

	public void setFormatoCarta(ComboBox<String> formatoCarta) {
		this.formatoCarta = formatoCarta;
	}

	public void setGradeoCarta(ComboBox<String> gradeoCarta) {
		this.gradeoCarta = gradeoCarta;
	}

	public void setNumeroCarta(ComboBox<String> numeroCarta) {
		this.numeroCarta = numeroCarta;
	}

	// TextField

	public void setBusquedaCodigo(TextField busquedaCodigo) {
		this.busquedaCodigo = busquedaCodigo;
	}

	public void setEditorialCarta(TextField editorialCarta) {
		this.editorialCarta = editorialCarta;
	}

	public void setIdCartaTratar(TextField idCartaTratar) {
		this.idCartaTratar = idCartaTratar;
	}

	public void setNombreCarta(TextField nombreCarta) {
		this.nombreCarta = nombreCarta;
	}

	public void setPrecioCarta(TextField precioCarta) {
		this.precioCarta = precioCarta;
	}

	public void setUrlReferencia(TextField urlReferencia) {
		this.urlReferencia = urlReferencia;
	}

	public void setBusquedaGeneral(TextField busquedaGeneral) {
		this.busquedaGeneral = busquedaGeneral;
	}

	public TextField getBusquedaCodigo() {
		return busquedaCodigo;
	}

	public TextField getEditorialCarta() {
		return editorialCarta;
	}

	public TextField getIdCartaTratar() {
		return idCartaTratar;
	}

	public TextField getCodigoCartaTratar() {
		return codigoCartaTratar;
	}

	public TextField getNombreCarta() {
		return nombreCarta;
	}

	public TextField getPrecioCarta() {
		return precioCarta;
	}

	public TextField getUrlReferencia() {
		return urlReferencia;
	}

}
