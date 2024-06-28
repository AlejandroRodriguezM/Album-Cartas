package funcionesManagment;

import java.util.List;

import cartaManagement.Carta;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class AccionReferencias {

	private TableColumn<Carta, String> iDColumna;
	private TableColumn<Carta, String> nombreColumna;
	private TableColumn<Carta, String> numeroColumna;
	private TableColumn<Carta, String> gradeoColumna;
	private TableColumn<Carta, String> editorialColumna;
	private TableColumn<Carta, String> coleccionColumna;
	private TableColumn<Carta, String> rarezaColumna;
	private TableColumn<Carta, String> esFoilColumna;
	private TableColumn<Carta, String> estadoColumna;
	private TableColumn<Carta, String> precioColumna;
	private TableColumn<Carta, String> referenciaColumna;

	public TableView<Carta> tablaBBDD;

	private VBox rootVBox;
	private VBox vboxContenido;
	private VBox vboxImage;

	private AnchorPane rootAnchorPane;
	private AnchorPane anchoPaneInfo;

	private ImageView backgroundImage;
	private ImageView imagenCarta;
	private ImageView cargaImagen;

	private Button botonModificar;
	private Button botonIntroducir;
	private Button botonEliminar;
	private Button botonComprimirPortadas;
	private Button botonReCopiarPortadas;
	private Button botonCancelarSubida;
	private Button botonBusquedaCodigo;
	private Button botonBusquedaAvanzada;
	private Button botonLimpiar;
	private Button botonModificarCarta;
	private Button botonParametroCarta;
	private Button botonVender;
	private Button botonbbdd;
	private Button botonGuardarCarta;
	private Button botonGuardarCambioCarta;
	private Button botonEliminarImportadoCarta;
	private Button botonSubidaPortada;
	private Button botonMostrarParametro;
	private Button botonImprimir;
	private Button botonGuardarResultado;
	private Button botonMostrarGuardados;
	private Button botonActualizarDatos;
	private Button botonActualizarPortadas;
	private Button botonActualizarSoftware;
	private Button botonActualizarTodo;
	private Button botonDescargarPdf;
	private Button botonDescargarSQL;
	private Button botonGuardarListaCartas;
	private Button botonEliminarImportadoListaCarta;
	private Button botonActualizarPrecio;

	private Rectangle barraCambioAltura;

	private Label alarmaConexionInternet;
	private Label labelGradeo;
	private Label labelEditorial;
	private Label labelEstado;
	private Label labelColeccion;
	private Label labelRareza;
	private Label labelNormas;
	private Label labelIdMod;
	private Label labelEsFoil;
	private Label labelPortada;
	private Label labelPrecio;
	private Label labelReferencia;
	private Label prontInfoLabel;
	private Label alarmaConexionSql;
	private Label labelComprobar;
	private Label labelVersion;
	private Label prontInfoEspecial;
	private Label prontInfoPreviews;
	private Label prontInfoPortadas;

	private TextField busquedaCodigoTextField;
	private TextField nombreCartaTextField;
	private TextField editorialCartaTextField;
	private TextField coleccionCartaTextField;
	private TextField rarezaCartaTextField;
	private TextArea normasCartaTextArea;
	private TextField precioCartaTextField;
	private TextField idCartaTratarTextField;
	private TextField direccionImagenTextField;
	private TextField urlReferenciaTextField;

	private TextField codigoCartaTratarTextField;
	private TextField busquedaGeneralTextField;

	private ComboBox<String> nombreCartaCombobox;
	private ComboBox<String> numeroCartaCombobox;
	private ComboBox<String> nombreEditorialCombobox;
	private ComboBox<String> nombreColeccionCombobox;
	private ComboBox<String> nombreRarezaCombobox;
	private ComboBox<String> nombreEsFoilCombobox;
	private ComboBox<String> gradeoCartaCombobox;
	private ComboBox<String> estadoCartaCombobox;
	private ComboBox<String> precioCartaCombobox;
	private ComboBox<String> comboPreviewsCombobox;

	private TextArea prontInfoTextArea;

	private MenuItem menuImportarFicheroCodigoBarras;
	private MenuItem menuCartaAniadir;
	private MenuItem menuCartaEliminar;
	private MenuItem menuCartaModificar;
	private MenuItem menuEstadisticaEstadistica;
	private MenuItem menuArchivoCerrar;
	private MenuItem menuArchivoDelete;
	private MenuItem menuArchivoDesconectar;
	private MenuItem menuArchivoExcel;
	private MenuItem menuArchivoImportar;
	private MenuItem menuArchivoSobreMi;
	private MenuItem menuEstadisticaComprados;
	private MenuItem menuEstadisticaFirmados;
	private MenuItem menuEstadisticaPosesion;
	private MenuItem menuEstadisticaVendidos;
	private MenuItem menuArchivoAvanzado;

	private Menu navegacionCerrar;
	private Menu navegacionCarta;
	private Menu navegacionEstadistica;

	private MenuBar menuNavegacion;

	private ProgressIndicator progresoCarga;

	private CheckBox checkFirmas;

	private List<Control> controlAccion;

	private static List<ComboBox<String>> listaComboboxes;
	private static List<TableColumn<Carta, String>> listaColumnasTabla;
	private static ObservableList<Control> listaTextFields;
	private static ObservableList<Button> listaBotones;
	private static ObservableList<Node> listaElementosFondo;

	private Stage stageVentana;

	public TableColumn<Carta, String> getiDColumna() {
		return iDColumna;
	}

	public TableColumn<Carta, String> getNombreColumna() {
		return nombreColumna;
	}

	public TableColumn<Carta, String> getNumeroColumna() {
		return numeroColumna;
	}

	public TableColumn<Carta, String> getGradeoColumna() {
		return gradeoColumna;
	}

	public TableColumn<Carta, String> getEditorialColumna() {
		return editorialColumna;
	}

	public TableColumn<Carta, String> getColeccionColumna() {
		return coleccionColumna;
	}

	public TableColumn<Carta, String> getRarezaColumna() {
		return rarezaColumna;
	}

	public TableColumn<Carta, String> getEsFoilColumna() {
		return esFoilColumna;
	}

	public TableColumn<Carta, String> getEstadoColumna() {
		return estadoColumna;
	}

	public TableColumn<Carta, String> getPrecioColumna() {
		return precioColumna;
	}

	public TableColumn<Carta, String> getReferenciaColumna() {
		return referenciaColumna;
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

	public VBox getVboxImage() {
		return vboxImage;
	}

	public AnchorPane getRootAnchorPane() {
		return rootAnchorPane;
	}

	public AnchorPane getAnchoPaneInfo() {
		return anchoPaneInfo;
	}

	public ImageView getBackgroundImage() {
		return backgroundImage;
	}

	public ImageView getImagenCarta() {
		return imagenCarta;
	}

	public ImageView getCargaImagen() {
		return cargaImagen;
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

	public Button getBotonComprimirPortadas() {
		return botonComprimirPortadas;
	}

	public Button getBotonReCopiarPortadas() {
		return botonReCopiarPortadas;
	}

	public Button getBotonCancelarSubida() {
		return botonCancelarSubida;
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

	public Button getBotonActualizarPrecios() {
		return botonActualizarPrecio;
	}

	public Rectangle getBarraCambioAltura() {
		return barraCambioAltura;
	}

	public Label getAlarmaConexionInternet() {
		return alarmaConexionInternet;
	}

	public Label getLabelGradeo() {
		return labelGradeo;
	}

	public Label getLabelEditorial() {
		return labelEditorial;
	}

	public Label getLabelEstado() {
		return labelEstado;
	}

	public Label getLabelColeccion() {
		return labelColeccion;
	}

	public Label getLabelRareza() {
		return labelRareza;
	}

	public Label getLabelNormas() {
		return labelNormas;
	}

	public Label getLabelIdMod() {
		return labelIdMod;
	}

	public Label getLabelEsFoil() {
		return labelEsFoil;
	}

	public Label getLabelPortada() {
		return labelPortada;
	}

	public Label getLabelPrecio() {
		return labelPrecio;
	}

	public Label getLabelReferencia() {
		return labelReferencia;
	}

	public Label getProntInfoLabel() {
		return prontInfoLabel;
	}

	public Label getAlarmaConexionSql() {
		return alarmaConexionSql;
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

	public TextField getBusquedaCodigoTextField() {
		return busquedaCodigoTextField;
	}

	public TextField getNombreCartaTextField() {
		return nombreCartaTextField;
	}

	public TextField getEditorialCartaTextField() {
		return editorialCartaTextField;
	}

	public TextField getColeccionCartaTextField() {
		return coleccionCartaTextField;
	}

	public TextField getRarezaCartaTextField() {
		return rarezaCartaTextField;
	}

	public TextArea getNormasCartaTextArea() {
		return normasCartaTextArea;
	}

	public TextField getPrecioCartaTextField() {
		return precioCartaTextField;
	}

	public TextField getIdCartaTratarTextField() {
		return idCartaTratarTextField;
	}

	public TextField getDireccionImagenTextField() {
		return direccionImagenTextField;
	}

	public TextField getUrlReferenciaTextField() {
		return urlReferenciaTextField;
	}

	public TextField getCodigoCartaTratarTextField() {
		return codigoCartaTratarTextField;
	}

	public TextField getBusquedaGeneralTextField() {
		return busquedaGeneralTextField;
	}

	public ComboBox<String> getNombreCartaCombobox() {
		return nombreCartaCombobox;
	}

	public ComboBox<String> getNumeroCartaCombobox() {
		return numeroCartaCombobox;
	}

	public ComboBox<String> getNombreEditorialCombobox() {
		return nombreEditorialCombobox;
	}

	public ComboBox<String> getNombreColeccionCombobox() {
		return nombreColeccionCombobox;
	}

	public ComboBox<String> getNombreRarezaCombobox() {
		return nombreRarezaCombobox;
	}

	public ComboBox<String> getNombreEsFoilCombobox() {
		return nombreEsFoilCombobox;
	}

	public ComboBox<String> getGradeoCartaCombobox() {
		return gradeoCartaCombobox;
	}

	public ComboBox<String> getEstadoCartaCombobox() {
		return estadoCartaCombobox;
	}

	public ComboBox<String> getPrecioCartaCombobox() {
		return precioCartaCombobox;
	}

	public ComboBox<String> getComboPreviewsCombobox() {
		return comboPreviewsCombobox;
	}

	public TextArea getProntInfoTextArea() {
		return prontInfoTextArea;
	}

	public MenuItem getMenuImportarFicheroCodigoBarras() {
		return menuImportarFicheroCodigoBarras;
	}

	public MenuItem getMenuCartaAniadir() {
		return menuCartaAniadir;
	}

	public MenuItem getMenuCartaEliminar() {
		return menuCartaEliminar;
	}

	public MenuItem getMenuCartaModificar() {
		return menuCartaModificar;
	}

	public MenuItem getMenuEstadisticaEstadistica() {
		return menuEstadisticaEstadistica;
	}

	public MenuItem getMenuArchivoCerrar() {
		return menuArchivoCerrar;
	}

	public MenuItem getMenuArchivoDelete() {
		return menuArchivoDelete;
	}

	public MenuItem getMenuArchivoDesconectar() {
		return menuArchivoDesconectar;
	}

	public MenuItem getMenuArchivoExcel() {
		return menuArchivoExcel;
	}

	public MenuItem getMenuArchivoImportar() {
		return menuArchivoImportar;
	}

	public MenuItem getMenuArchivoSobreMi() {
		return menuArchivoSobreMi;
	}

	public MenuItem getMenuEstadisticaComprados() {
		return menuEstadisticaComprados;
	}

	public MenuItem getMenuEstadisticaFirmados() {
		return menuEstadisticaFirmados;
	}

	public MenuItem getMenuEstadisticaPosesion() {
		return menuEstadisticaPosesion;
	}

	public MenuItem getMenuEstadisticaVendidos() {
		return menuEstadisticaVendidos;
	}

	public MenuItem getMenuArchivoAvanzado() {
		return menuArchivoAvanzado;
	}

	public Menu getNavegacionCerrar() {
		return navegacionCerrar;
	}

	public Menu getNavegacionCarta() {
		return navegacionCarta;
	}

	public Menu getNavegacionEstadistica() {
		return navegacionEstadistica;
	}

	public MenuBar getMenuNavegacion() {
		return menuNavegacion;
	}

	public ProgressIndicator getProgresoCarga() {
		return progresoCarga;
	}

	public CheckBox getCheckFirmas() {
		return checkFirmas;
	}

	public List<ComboBox<String>> getListaComboboxes() {
		return listaComboboxes;
	}

	public List<TableColumn<Carta, String>> getListaColumnasTabla() {
		return listaColumnasTabla;
	}

	public ObservableList<Control> getListaTextFields() {
		return listaTextFields;
	}

	public static ObservableList<Button> getListaBotones() {
		return listaBotones;
	}

	public static ObservableList<Node> getListaElementosFondo() {
		return listaElementosFondo;
	}

	public Stage getStageVentana() {
		return stageVentana;
	}

	/**
	 * @return the botonGuardarListaCartas
	 */
	public Button getBotonGuardarListaCartas() {
		return botonGuardarListaCartas;
	}

	/**
	 * @return the botonEliminarImportadoListaCarta
	 */
	public Button getBotonEliminarImportadoListaCarta() {
		return botonEliminarImportadoListaCarta;
	}

	public List<Control> getControlAccion() {
		return controlAccion;
	}

	/**
	 * @param botonGuardarListaCartas the botonGuardarListaCartas to set
	 */
	public void setBotonGuardarListaCartas(Button botonGuardarListaCartas) {
		this.botonGuardarListaCartas = botonGuardarListaCartas;
	}

	/**
	 * @param botonEliminarImportadoListaCarta the botonEliminarImportadoListaCarta
	 *                                         to set
	 */
	public void setBotonEliminarImportadoListaCarta(Button botonEliminarImportadoListaCarta) {
		this.botonEliminarImportadoListaCarta = botonEliminarImportadoListaCarta;
	}

	public void setiDColumna(TableColumn<Carta, String> iDColumna) {
		this.iDColumna = iDColumna;
	}

	public void setNombreColumna(TableColumn<Carta, String> nombreColumna) {
		this.nombreColumna = nombreColumna;
	}

	public void setNumeroColumna(TableColumn<Carta, String> numeroColumna) {
		this.numeroColumna = numeroColumna;
	}

	public void setGradeoColumna(TableColumn<Carta, String> gradeoColumna) {
		this.gradeoColumna = gradeoColumna;
	}

	public void setEditorialColumna(TableColumn<Carta, String> editorialColumna) {
		this.editorialColumna = editorialColumna;
	}

	public void setColeccionColumna(TableColumn<Carta, String> coleccionColumna) {
		this.coleccionColumna = coleccionColumna;
	}

	public void setRarezaColumna(TableColumn<Carta, String> rarezaColumna) {
		this.rarezaColumna = rarezaColumna;
	}

	public void setEsFoilColumna(TableColumn<Carta, String> esFoilColumna) {
		this.esFoilColumna = esFoilColumna;
	}

	public void setEstadoColumna(TableColumn<Carta, String> estadoColumna) {
		this.estadoColumna = estadoColumna;
	}

	public void setPrecioColumna(TableColumn<Carta, String> precioColumna) {
		this.precioColumna = precioColumna;
	}

	public void setReferenciaColumna(TableColumn<Carta, String> referenciaColumna) {
		this.referenciaColumna = referenciaColumna;
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

	public void setVboxImage(VBox vboxImage) {
		this.vboxImage = vboxImage;
	}

	public void setRootAnchorPane(AnchorPane rootAnchorPane) {
		this.rootAnchorPane = rootAnchorPane;
	}

	public void setAnchoPaneInfo(AnchorPane anchoPaneInfo) {
		this.anchoPaneInfo = anchoPaneInfo;
	}

	public void setBackgroundImage(ImageView backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	public void setImagenCarta(ImageView imagenCarta) {
		this.imagenCarta = imagenCarta;
	}

	public void setCargaImagen(ImageView cargaImagen) {
		this.cargaImagen = cargaImagen;
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

	public void setBotonComprimirPortadas(Button botonComprimirPortadas) {
		this.botonComprimirPortadas = botonComprimirPortadas;
	}

	public void setBotonReCopiarPortadas(Button botonReCopiarPortadas) {
		this.botonReCopiarPortadas = botonReCopiarPortadas;
	}

	public void setBotonCancelarSubida(Button botonCancelarSubida) {
		this.botonCancelarSubida = botonCancelarSubida;
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

	public void setBotonActualizarPrecios(Button botonActualizarPrecio) {
		this.botonActualizarPrecio = botonActualizarPrecio;
	}

	public void setBarraCambioAltura(Rectangle barraCambioAltura) {
		this.barraCambioAltura = barraCambioAltura;
	}

	public void setAlarmaConexionInternet(Label alarmaConexionInternet) {
		this.alarmaConexionInternet = alarmaConexionInternet;
	}

	public void setLabelGradeo(Label labelGradeo) {
		this.labelGradeo = labelGradeo;
	}

	public void setLabelEditorial(Label labelEditorial) {
		this.labelEditorial = labelEditorial;
	}

	public void setLabelEstado(Label labelEstado) {
		this.labelEstado = labelEstado;
	}

	public void setLabelColeccion(Label labelColeccion) {
		this.labelColeccion = labelColeccion;
	}

	public void setLabelRareza(Label labelRareza) {
		this.labelRareza = labelRareza;
	}

	public void setLabelNormas(Label labelNormas) {
		this.labelNormas = labelNormas;
	}

	public void setLabelIdMod(Label labelIdMod) {
		this.labelIdMod = labelIdMod;
	}

	public void setLabelEsFoil(Label labelEsFoil) {
		this.labelEsFoil = labelEsFoil;
	}

	public void setLabelPortada(Label labelPortada) {
		this.labelPortada = labelPortada;
	}

	public void setLabelPrecio(Label labelPrecio) {
		this.labelPrecio = labelPrecio;
	}

	public void setLabelReferencia(Label labelReferencia) {
		this.labelReferencia = labelReferencia;
	}

	public void setProntInfoLabel(Label prontInfoLabel) {
		this.prontInfoLabel = prontInfoLabel;
	}

	public void setAlarmaConexionSql(Label alarmaConexionSql) {
		this.alarmaConexionSql = alarmaConexionSql;
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

	public void setBusquedaCodigoTextField(TextField busquedaCodigoTextField) {
		this.busquedaCodigoTextField = busquedaCodigoTextField;
	}

	public void setNombreCartaTextField(TextField nombreCartaTextField) {
		this.nombreCartaTextField = nombreCartaTextField;
	}

	public void setEditorialCartaTextField(TextField editorialCartaTextField) {
		this.editorialCartaTextField = editorialCartaTextField;
	}

	public void setColeccionCartaTextField(TextField coleccionCartaTextField) {
		this.coleccionCartaTextField = coleccionCartaTextField;
	}

	public void setRarezaCartaTextField(TextField rarezaCartaTextField) {
		this.rarezaCartaTextField = rarezaCartaTextField;
	}

	public void setNormasCartaTextArea(TextArea normasCartaTextArea) {
		this.normasCartaTextArea = normasCartaTextArea;
	}

	public void setPrecioCartaTextField(TextField precioCartaTextField) {
		this.precioCartaTextField = precioCartaTextField;
	}

	public void setIdCartaTratarTextField(TextField idCartaTratarTextField) {
		this.idCartaTratarTextField = idCartaTratarTextField;
	}

	public void setDireccionImagenTextField(TextField direccionImagenTextField) {
		this.direccionImagenTextField = direccionImagenTextField;
	}

	public void setUrlReferenciaTextField(TextField urlReferenciaTextField) {
		this.urlReferenciaTextField = urlReferenciaTextField;
	}

	public void setCodigoCartaTratarTextField(TextField codigoCartaTratarTextField) {
		this.codigoCartaTratarTextField = codigoCartaTratarTextField;
	}

	public void setBusquedaGeneralTextField(TextField busquedaGeneralTextField) {
		this.busquedaGeneralTextField = busquedaGeneralTextField;
	}

	public void setNombreCartaCombobox(ComboBox<String> nombreCartaCombobox) {
		this.nombreCartaCombobox = nombreCartaCombobox;
	}

	public void setNumeroCartaCombobox(ComboBox<String> numeroCartaCombobox) {
		this.numeroCartaCombobox = numeroCartaCombobox;
	}

	public void setNombreEditorialCombobox(ComboBox<String> nombreEditorialCombobox) {
		this.nombreEditorialCombobox = nombreEditorialCombobox;
	}

	public void setNombreColeccionCombobox(ComboBox<String> nombreColeccionCombobox) {
		this.nombreColeccionCombobox = nombreColeccionCombobox;
	}

	public void setNombreRarezaCombobox(ComboBox<String> nombreRarezaCombobox) {
		this.nombreRarezaCombobox = nombreRarezaCombobox;
	}

	public void setNombreEsFoilCombobox(ComboBox<String> nombreEsFoilCombobox) {
		this.nombreEsFoilCombobox = nombreEsFoilCombobox;
	}

	public void setGradeoCartaCombobox(ComboBox<String> gradeoCartaCombobox) {
		this.gradeoCartaCombobox = gradeoCartaCombobox;
	}

	public void setEstadoCartaCombobox(ComboBox<String> estadoCartaCombobox) {
		this.estadoCartaCombobox = estadoCartaCombobox;
	}

	public void setPrecioCartaCombobox(ComboBox<String> precioCartaCombobox) {
		this.precioCartaCombobox = precioCartaCombobox;
	}

	public void setComboPreviewsCombobox(ComboBox<String> comboPreviewsCombobox) {
		this.comboPreviewsCombobox = comboPreviewsCombobox;
	}

	public void setProntInfoTextArea(TextArea prontInfoTextArea) {
		this.prontInfoTextArea = prontInfoTextArea;
	}

	public void setMenuImportarFicheroCodigoBarras(MenuItem menuImportarFicheroCodigoBarras) {
		this.menuImportarFicheroCodigoBarras = menuImportarFicheroCodigoBarras;
	}

	public void setMenuCartaAniadir(MenuItem menuCartaAniadir) {
		this.menuCartaAniadir = menuCartaAniadir;
	}

	public void setMenuCartaEliminar(MenuItem menuCartaEliminar) {
		this.menuCartaEliminar = menuCartaEliminar;
	}

	public void setMenuCartaModificar(MenuItem menuCartaModificar) {
		this.menuCartaModificar = menuCartaModificar;
	}

	public void setMenuEstadisticaEstadistica(MenuItem menuEstadisticaEstadistica) {
		this.menuEstadisticaEstadistica = menuEstadisticaEstadistica;
	}

	public void setMenuArchivoCerrar(MenuItem menuArchivoCerrar) {
		this.menuArchivoCerrar = menuArchivoCerrar;
	}

	public void setMenuArchivoDelete(MenuItem menuArchivoDelete) {
		this.menuArchivoDelete = menuArchivoDelete;
	}

	public void setMenuArchivoDesconectar(MenuItem menuArchivoDesconectar) {
		this.menuArchivoDesconectar = menuArchivoDesconectar;
	}

	public void setMenuArchivoExcel(MenuItem menuArchivoExcel) {
		this.menuArchivoExcel = menuArchivoExcel;
	}

	public void setMenuArchivoImportar(MenuItem menuArchivoImportar) {
		this.menuArchivoImportar = menuArchivoImportar;
	}

	public void setMenuArchivoSobreMi(MenuItem menuArchivoSobreMi) {
		this.menuArchivoSobreMi = menuArchivoSobreMi;
	}

	public void setMenuEstadisticaComprados(MenuItem menuEstadisticaComprados) {
		this.menuEstadisticaComprados = menuEstadisticaComprados;
	}

	public void setMenuEstadisticaFirmados(MenuItem menuEstadisticaFirmados) {
		this.menuEstadisticaFirmados = menuEstadisticaFirmados;
	}

	public void setMenuEstadisticaPosesion(MenuItem menuEstadisticaPosesion) {
		this.menuEstadisticaPosesion = menuEstadisticaPosesion;
	}

	public void setMenuEstadisticaVendidos(MenuItem menuEstadisticaVendidos) {
		this.menuEstadisticaVendidos = menuEstadisticaVendidos;
	}

	public void setMenuArchivoAvanzado(MenuItem menuArchivoAvanzado) {
		this.menuArchivoAvanzado = menuArchivoAvanzado;
	}

	public void setNavegacionCerrar(Menu navegacionCerrar) {
		this.navegacionCerrar = navegacionCerrar;
	}

	public void setNavegacionCarta(Menu navegacionCarta) {
		this.navegacionCarta = navegacionCarta;
	}

	public void setNavegacionEstadistica(Menu navegacionEstadistica) {
		this.navegacionEstadistica = navegacionEstadistica;
	}

	public void setMenuNavegacion(MenuBar menuNavegacion) {
		this.menuNavegacion = menuNavegacion;
	}

	public void setProgresoCarga(ProgressIndicator progresoCarga) {
		this.progresoCarga = progresoCarga;
	}

	public void setCheckFirmas(CheckBox checkFirmas) {
		this.checkFirmas = checkFirmas;
	}

	public void setControlesCarta(List<Control> controles) {
		this.controlAccion = controles;
	}

	public void setListaComboboxes(List<ComboBox<String>> listaComboboxes) {
		AccionReferencias.listaComboboxes = listaComboboxes;
	}

	public static void setListaColumnasTabla(List<TableColumn<Carta, String>> listaColumnasTabla) {
		AccionReferencias.listaColumnasTabla = listaColumnasTabla;
	}

	public void setListaTextFields(ObservableList<Control> listaTextFields) {
		AccionReferencias.listaTextFields = listaTextFields;
	}

	public static void setListaBotones(ObservableList<Button> listaBotones) {
		AccionReferencias.listaBotones = listaBotones;
	}

	public static void setListaElementosFondo(ObservableList<Node> listaElementosFondo) {
		AccionReferencias.listaElementosFondo = listaElementosFondo;
	}

	public void setStageVentana(Stage stageVentana) {
		this.stageVentana = stageVentana;
	}
}
