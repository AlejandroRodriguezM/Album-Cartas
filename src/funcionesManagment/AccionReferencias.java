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
	private TableColumn<Carta, String> editorialColumna;
	private TableColumn<Carta, String> coleccionColumna;
	private TableColumn<Carta, String> rarezaColumna;
	private TableColumn<Carta, String> precioColumnaNormal;
	private TableColumn<Carta, String> precioColumnaFoil;
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

	private Button botonClonarCarta;
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
	private Label labelEditorial;
	private Label labelColeccion;
	private Label labelNombre;
	private Label labelRareza;
	private Label labelNormas;
	private Label labelIdMod;
	private Label labelPortada;
	private Label labelPrecioFoil;
	private Label labelPrecioNormal;
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
	private TextField precioCartaNormalTextField;
	private TextField precioCartaFoilTextField;
	private TextField idCartaTratarTextField;
	private TextField direccionImagenTextField;
	private TextField urlReferenciaTextField;

	private TextField codigoCartaTratarTextField;
	private TextField busquedaGeneralTextField;

	private ComboBox<String> nombreCartaCombobox;
	private ComboBox<String> numeroCartaCombobox;
	private ComboBox<String> nombreTiendaCombobox;
	private ComboBox<String> nombreEditorialCombobox;
	private ComboBox<String> nombreColeccionCombobox;
	private ComboBox<String> nombreRarezaCombobox;
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
	private MenuItem menuEstadisticaSumaTotal;
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

	/**
	 * @return the iDColumna
	 */
	public TableColumn<Carta, String> getiDColumna() {
		return iDColumna;
	}

	/**
	 * @return the nombreColumna
	 */
	public TableColumn<Carta, String> getNombreColumna() {
		return nombreColumna;
	}

	/**
	 * @return the numeroColumna
	 */
	public TableColumn<Carta, String> getNumeroColumna() {
		return numeroColumna;
	}

	/**
	 * @return the editorialColumna
	 */
	public TableColumn<Carta, String> getEditorialColumna() {
		return editorialColumna;
	}

	/**
	 * @return the coleccionColumna
	 */
	public TableColumn<Carta, String> getColeccionColumna() {
		return coleccionColumna;
	}

	/**
	 * @return the rarezaColumna
	 */
	public TableColumn<Carta, String> getRarezaColumna() {
		return rarezaColumna;
	}

	/**
	 * @return the precioColumnaNormal
	 */
	public TableColumn<Carta, String> getPrecioColumnaNormal() {
		return precioColumnaNormal;
	}

	/**
	 * @return the precioColumnaFoil
	 */
	public TableColumn<Carta, String> getPrecioColumnaFoil() {
		return precioColumnaFoil;
	}

	/**
	 * @return the referenciaColumna
	 */
	public TableColumn<Carta, String> getReferenciaColumna() {
		return referenciaColumna;
	}

	/**
	 * @return the tablaBBDD
	 */
	public TableView<Carta> getTablaBBDD() {
		return tablaBBDD;
	}

	/**
	 * @return the rootVBox
	 */
	public VBox getRootVBox() {
		return rootVBox;
	}

	/**
	 * @return the vboxContenido
	 */
	public VBox getVboxContenido() {
		return vboxContenido;
	}

	/**
	 * @return the vboxImage
	 */
	public VBox getVboxImage() {
		return vboxImage;
	}

	/**
	 * @return the rootAnchorPane
	 */
	public AnchorPane getRootAnchorPane() {
		return rootAnchorPane;
	}

	/**
	 * @return the anchoPaneInfo
	 */
	public AnchorPane getAnchoPaneInfo() {
		return anchoPaneInfo;
	}

	/**
	 * @return the backgroundImage
	 */
	public ImageView getBackgroundImage() {
		return backgroundImage;
	}

	/**
	 * @return the imagenCarta
	 */
	public ImageView getImagenCarta() {
		return imagenCarta;
	}

	/**
	 * @return the cargaImagen
	 */
	public ImageView getCargaImagen() {
		return cargaImagen;
	}

	/**
	 * @return the botonModificar
	 */
	public Button getBotonModificar() {
		return botonModificar;
	}

	/**
	 * @return the botonIntroducir
	 */
	public Button getBotonIntroducir() {
		return botonIntroducir;
	}

	/**
	 * @return the botonEliminar
	 */
	public Button getBotonEliminar() {
		return botonEliminar;
	}

	/**
	 * @return the botonComprimirPortadas
	 */
	public Button getBotonComprimirPortadas() {
		return botonComprimirPortadas;
	}

	/**
	 * @return the botonReCopiarPortadas
	 */
	public Button getBotonReCopiarPortadas() {
		return botonReCopiarPortadas;
	}

	/**
	 * @return the botonCancelarSubida
	 */
	public Button getBotonCancelarSubida() {
		return botonCancelarSubida;
	}

	/**
	 * @return the botonBusquedaCodigo
	 */
	public Button getBotonBusquedaCodigo() {
		return botonBusquedaCodigo;
	}

	/**
	 * @return the botonBusquedaAvanzada
	 */
	public Button getBotonBusquedaAvanzada() {
		return botonBusquedaAvanzada;
	}

	/**
	 * @return the botonLimpiar
	 */
	public Button getBotonLimpiar() {
		return botonLimpiar;
	}

	/**
	 * @return the botonModificarCarta
	 */
	public Button getBotonModificarCarta() {
		return botonModificarCarta;
	}

	/**
	 * @return the botonParametroCarta
	 */
	public Button getBotonParametroCarta() {
		return botonParametroCarta;
	}

	/**
	 * @return the botonVender
	 */
	public Button getBotonVender() {
		return botonVender;
	}

	/**
	 * @return the botonbbdd
	 */
	public Button getBotonbbdd() {
		return botonbbdd;
	}

	/**
	 * @return the botonGuardarCarta
	 */
	public Button getBotonGuardarCarta() {
		return botonGuardarCarta;
	}

	/**
	 * @return the botonGuardarCambioCarta
	 */
	public Button getBotonGuardarCambioCarta() {
		return botonGuardarCambioCarta;
	}

	/**
	 * @return the botonEliminarImportadoCarta
	 */
	public Button getBotonEliminarImportadoCarta() {
		return botonEliminarImportadoCarta;
	}

	/**
	 * @return the botonSubidaPortada
	 */
	public Button getBotonSubidaPortada() {
		return botonSubidaPortada;
	}

	/**
	 * @return the botonMostrarParametro
	 */
	public Button getBotonMostrarParametro() {
		return botonMostrarParametro;
	}

	/**
	 * @return the botonActualizarDatos
	 */
	public Button getBotonActualizarDatos() {
		return botonActualizarDatos;
	}

	/**
	 * @return the botonActualizarPortadas
	 */
	public Button getBotonActualizarPortadas() {
		return botonActualizarPortadas;
	}

	/**
	 * @return the botonActualizarSoftware
	 */
	public Button getBotonActualizarSoftware() {
		return botonActualizarSoftware;
	}

	/**
	 * @return the botonActualizarTodo
	 */
	public Button getBotonActualizarTodo() {
		return botonActualizarTodo;
	}

	/**
	 * @return the botonDescargarPdf
	 */
	public Button getBotonDescargarPdf() {
		return botonDescargarPdf;
	}

	/**
	 * @return the botonDescargarSQL
	 */
	public Button getBotonDescargarSQL() {
		return botonDescargarSQL;
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

	/**
	 * @return the botonActualizarPrecio
	 */
	public Button getBotonActualizarPrecio() {
		return botonActualizarPrecio;
	}

	/**
	 * @return the barraCambioAltura
	 */
	public Rectangle getBarraCambioAltura() {
		return barraCambioAltura;
	}

	/**
	 * @return the alarmaConexionInternet
	 */
	public Label getAlarmaConexionInternet() {
		return alarmaConexionInternet;
	}

	/**
	 * @return the labelEditorial
	 */
	public Label getLabelEditorial() {
		return labelEditorial;
	}

	/**
	 * @return the labelColeccion
	 */
	public Label getLabelColeccion() {
		return labelColeccion;
	}

	/**
	 * @return the labelRareza
	 */
	public Label getLabelRareza() {
		return labelRareza;
	}

	/**
	 * @return the labelNormas
	 */
	public Label getLabelNormas() {
		return labelNormas;
	}

	/**
	 * @return the labelIdMod
	 */
	public Label getLabelIdMod() {
		return labelIdMod;
	}

	/**
	 * @return the labelPortada
	 */
	public Label getLabelPortada() {
		return labelPortada;
	}

	/**
	 * @return the labelPrecioFoil
	 */
	public Label getLabelPrecioFoil() {
		return labelPrecioFoil;
	}

	/**
	 * @return the labelPrecioNormal
	 */
	public Label getLabelPrecioNormal() {
		return labelPrecioNormal;
	}

	/**
	 * @return the labelReferencia
	 */
	public Label getLabelReferencia() {
		return labelReferencia;
	}

	/**
	 * @return the prontInfoLabel
	 */
	public Label getProntInfoLabel() {
		return prontInfoLabel;
	}

	/**
	 * @return the alarmaConexionSql
	 */
	public Label getAlarmaConexionSql() {
		return alarmaConexionSql;
	}

	/**
	 * @return the labelComprobar
	 */
	public Label getLabelComprobar() {
		return labelComprobar;
	}

	/**
	 * @return the labelVersion
	 */
	public Label getLabelVersion() {
		return labelVersion;
	}

	/**
	 * @return the prontInfoEspecial
	 */
	public Label getProntInfoEspecial() {
		return prontInfoEspecial;
	}

	/**
	 * @return the prontInfoPreviews
	 */
	public Label getProntInfoPreviews() {
		return prontInfoPreviews;
	}

	/**
	 * @return the prontInfoPortadas
	 */
	public Label getProntInfoPortadas() {
		return prontInfoPortadas;
	}

	/**
	 * @return the busquedaCodigoTextField
	 */
	public TextField getBusquedaCodigoTextField() {
		return busquedaCodigoTextField;
	}

	/**
	 * @return the nombreCartaTextField
	 */
	public TextField getNombreCartaTextField() {
		return nombreCartaTextField;
	}

	/**
	 * @return the editorialCartaTextField
	 */
	public TextField getEditorialCartaTextField() {
		return editorialCartaTextField;
	}

	/**
	 * @return the coleccionCartaTextField
	 */
	public TextField getColeccionCartaTextField() {
		return coleccionCartaTextField;
	}

	/**
	 * @return the rarezaCartaTextField
	 */
	public TextField getRarezaCartaTextField() {
		return rarezaCartaTextField;
	}

	/**
	 * @return the normasCartaTextArea
	 */
	public TextArea getNormasCartaTextArea() {
		return normasCartaTextArea;
	}

	/**
	 * @return the precioCartaNormalTextField
	 */
	public TextField getPrecioCartaNormalTextField() {
		return precioCartaNormalTextField;
	}

	/**
	 * @return the precioCartaFoilTextField
	 */
	public TextField getPrecioCartaFoilTextField() {
		return precioCartaFoilTextField;
	}

	/**
	 * @return the idCartaTratarTextField
	 */
	public TextField getIdCartaTratarTextField() {
		return idCartaTratarTextField;
	}

	/**
	 * @return the direccionImagenTextField
	 */
	public TextField getDireccionImagenTextField() {
		return direccionImagenTextField;
	}

	/**
	 * @return the urlReferenciaTextField
	 */
	public TextField getUrlReferenciaTextField() {
		return urlReferenciaTextField;
	}

	/**
	 * @return the codigoCartaTratarTextField
	 */
	public TextField getCodigoCartaTratarTextField() {
		return codigoCartaTratarTextField;
	}

	/**
	 * @return the busquedaGeneralTextField
	 */
	public TextField getBusquedaGeneralTextField() {
		return busquedaGeneralTextField;
	}

	/**
	 * @return the nombreCartaCombobox
	 */
	public ComboBox<String> getNombreCartaCombobox() {
		return nombreCartaCombobox;
	}

	/**
	 * @return the numeroCartaCombobox
	 */
	public ComboBox<String> getNumeroCartaCombobox() {
		return numeroCartaCombobox;
	}

	/**
	 * @return the nombreTiendaCombobox
	 */
	public ComboBox<String> getNombreTiendaCombobox() {
		return nombreTiendaCombobox;
	}

	/**
	 * @return the nombreEditorialCombobox
	 */
	public ComboBox<String> getNombreEditorialCombobox() {
		return nombreEditorialCombobox;
	}

	/**
	 * @return the nombreColeccionCombobox
	 */
	public ComboBox<String> getNombreColeccionCombobox() {
		return nombreColeccionCombobox;
	}

	/**
	 * @return the nombreRarezaCombobox
	 */
	public ComboBox<String> getNombreRarezaCombobox() {
		return nombreRarezaCombobox;
	}

	/**
	 * @return the comboPreviewsCombobox
	 */
	public ComboBox<String> getComboPreviewsCombobox() {
		return comboPreviewsCombobox;
	}

	/**
	 * @return the prontInfoTextArea
	 */
	public TextArea getProntInfoTextArea() {
		return prontInfoTextArea;
	}

	/**
	 * @return the menuImportarFicheroCodigoBarras
	 */
	public MenuItem getMenuImportarFicheroCodigoBarras() {
		return menuImportarFicheroCodigoBarras;
	}

	/**
	 * @return the menuCartaAniadir
	 */
	public MenuItem getMenuCartaAniadir() {
		return menuCartaAniadir;
	}

	/**
	 * @return the menuCartaEliminar
	 */
	public MenuItem getMenuCartaEliminar() {
		return menuCartaEliminar;
	}

	/**
	 * @return the menuCartaModificar
	 */
	public MenuItem getMenuCartaModificar() {
		return menuCartaModificar;
	}

	/**
	 * @return the menuEstadisticaEstadistica
	 */
	public MenuItem getMenuEstadisticaEstadistica() {
		return menuEstadisticaEstadistica;
	}

	/**
	 * @return the menuArchivoCerrar
	 */
	public MenuItem getMenuArchivoCerrar() {
		return menuArchivoCerrar;
	}

	/**
	 * @return the menuArchivoDelete
	 */
	public MenuItem getMenuArchivoDelete() {
		return menuArchivoDelete;
	}

	/**
	 * @return the menuArchivoDesconectar
	 */
	public MenuItem getMenuArchivoDesconectar() {
		return menuArchivoDesconectar;
	}

	/**
	 * @return the menuArchivoExcel
	 */
	public MenuItem getMenuArchivoExcel() {
		return menuArchivoExcel;
	}

	/**
	 * @return the menuArchivoImportar
	 */
	public MenuItem getMenuArchivoImportar() {
		return menuArchivoImportar;
	}

	/**
	 * @return the menuArchivoSobreMi
	 */
	public MenuItem getMenuArchivoSobreMi() {
		return menuArchivoSobreMi;
	}

	/**
	 * @return the menuArchivoAvanzado
	 */
	public MenuItem getMenuArchivoAvanzado() {
		return menuArchivoAvanzado;
	}

	/**
	 * @return the navegacionCerrar
	 */
	public Menu getNavegacionCerrar() {
		return navegacionCerrar;
	}

	/**
	 * @return the navegacionCarta
	 */
	public Menu getNavegacionCarta() {
		return navegacionCarta;
	}

	/**
	 * @return the navegacionEstadistica
	 */
	public Menu getNavegacionEstadistica() {
		return navegacionEstadistica;
	}

	/**
	 * @return the menuNavegacion
	 */
	public MenuBar getMenuNavegacion() {
		return menuNavegacion;
	}

	/**
	 * @return the progresoCarga
	 */
	public ProgressIndicator getProgresoCarga() {
		return progresoCarga;
	}

	/**
	 * @return the checkFirmas
	 */
	public CheckBox getCheckFirmas() {
		return checkFirmas;
	}

	/**
	 * @return the controlAccion
	 */
	public List<Control> getControlAccion() {
		return controlAccion;
	}

	/**
	 * @return the listaComboboxes
	 */
	public static List<ComboBox<String>> getListaComboboxes() {
		return listaComboboxes;
	}

	/**
	 * @return the listaColumnasTabla
	 */
	public static List<TableColumn<Carta, String>> getListaColumnasTabla() {
		return listaColumnasTabla;
	}

	/**
	 * @return the listaTextFields
	 */
	public static ObservableList<Control> getListaTextFields() {
		return listaTextFields;
	}

	/**
	 * @return the listaBotones
	 */
	public static ObservableList<Button> getListaBotones() {
		return listaBotones;
	}

	/**
	 * @return the listaElementosFondo
	 */
	public static ObservableList<Node> getListaElementosFondo() {
		return listaElementosFondo;
	}

	/**
	 * @return the stageVentana
	 */
	public Stage getStageVentana() {
		return stageVentana;
	}

	/**
	 * @return the labelNombre
	 */
	public Label getLabelNombre() {
		return labelNombre;
	}

	/**
	 * @return the botonClonarCarta
	 */
	public Button getBotonClonarCarta() {
		return botonClonarCarta;
	}

	/**
	 * @return the menuEstadisticaSumaTotal
	 */
	public MenuItem getMenuEstadisticaSumaTotal() {
		return menuEstadisticaSumaTotal;
	}

	/**
	 * @param botonClonarCarta the botonClonarCarta to set
	 */
	public void setBotonClonarCarta(Button botonClonarCarta) {
		this.botonClonarCarta = botonClonarCarta;
	}

	/**
	 * @param labelNombre the labelNombre to set
	 */
	public void setLabelNombre(Label labelNombre) {
		this.labelNombre = labelNombre;
	}

	/**
	 * @param iDColumna the iDColumna to set
	 */
	public void setiDColumna(TableColumn<Carta, String> iDColumna) {
		this.iDColumna = iDColumna;
	}

	/**
	 * @param nombreColumna the nombreColumna to set
	 */
	public void setNombreColumna(TableColumn<Carta, String> nombreColumna) {
		this.nombreColumna = nombreColumna;
	}

	/**
	 * @param numeroColumna the numeroColumna to set
	 */
	public void setNumeroColumna(TableColumn<Carta, String> numeroColumna) {
		this.numeroColumna = numeroColumna;
	}

	/**
	 * @param editorialColumna the editorialColumna to set
	 */
	public void setEditorialColumna(TableColumn<Carta, String> editorialColumna) {
		this.editorialColumna = editorialColumna;
	}

	/**
	 * @param coleccionColumna the coleccionColumna to set
	 */
	public void setColeccionColumna(TableColumn<Carta, String> coleccionColumna) {
		this.coleccionColumna = coleccionColumna;
	}

	/**
	 * @param rarezaColumna the rarezaColumna to set
	 */
	public void setRarezaColumna(TableColumn<Carta, String> rarezaColumna) {
		this.rarezaColumna = rarezaColumna;
	}

	/**
	 * @param precioColumnaNormal the precioColumnaNormal to set
	 */
	public void setPrecioColumnaNormal(TableColumn<Carta, String> precioColumnaNormal) {
		this.precioColumnaNormal = precioColumnaNormal;
	}

	/**
	 * @param precioColumnaFoil the precioColumnaFoil to set
	 */
	public void setPrecioColumnaFoil(TableColumn<Carta, String> precioColumnaFoil) {
		this.precioColumnaFoil = precioColumnaFoil;
	}

	/**
	 * @param referenciaColumna the referenciaColumna to set
	 */
	public void setReferenciaColumna(TableColumn<Carta, String> referenciaColumna) {
		this.referenciaColumna = referenciaColumna;
	}

	/**
	 * @param tablaBBDD the tablaBBDD to set
	 */
	public void setTablaBBDD(TableView<Carta> tablaBBDD) {
		this.tablaBBDD = tablaBBDD;
	}

	/**
	 * @param rootVBox the rootVBox to set
	 */
	public void setRootVBox(VBox rootVBox) {
		this.rootVBox = rootVBox;
	}

	/**
	 * @param vboxContenido the vboxContenido to set
	 */
	public void setVboxContenido(VBox vboxContenido) {
		this.vboxContenido = vboxContenido;
	}

	/**
	 * @param vboxImage the vboxImage to set
	 */
	public void setVboxImage(VBox vboxImage) {
		this.vboxImage = vboxImage;
	}

	/**
	 * @param rootAnchorPane the rootAnchorPane to set
	 */
	public void setRootAnchorPane(AnchorPane rootAnchorPane) {
		this.rootAnchorPane = rootAnchorPane;
	}

	/**
	 * @param anchoPaneInfo the anchoPaneInfo to set
	 */
	public void setAnchoPaneInfo(AnchorPane anchoPaneInfo) {
		this.anchoPaneInfo = anchoPaneInfo;
	}

	/**
	 * @param backgroundImage the backgroundImage to set
	 */
	public void setBackgroundImage(ImageView backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	/**
	 * @param imagenCarta the imagenCarta to set
	 */
	public void setImagenCarta(ImageView imagenCarta) {
		this.imagenCarta = imagenCarta;
	}

	/**
	 * @param cargaImagen the cargaImagen to set
	 */
	public void setCargaImagen(ImageView cargaImagen) {
		this.cargaImagen = cargaImagen;
	}

	/**
	 * @param botonModificar the botonModificar to set
	 */
	public void setBotonModificar(Button botonModificar) {
		this.botonModificar = botonModificar;
	}

	/**
	 * @param botonIntroducir the botonIntroducir to set
	 */
	public void setBotonIntroducir(Button botonIntroducir) {
		this.botonIntroducir = botonIntroducir;
	}

	/**
	 * @param botonEliminar the botonEliminar to set
	 */
	public void setBotonEliminar(Button botonEliminar) {
		this.botonEliminar = botonEliminar;
	}

	/**
	 * @param botonComprimirPortadas the botonComprimirPortadas to set
	 */
	public void setBotonComprimirPortadas(Button botonComprimirPortadas) {
		this.botonComprimirPortadas = botonComprimirPortadas;
	}

	/**
	 * @param botonReCopiarPortadas the botonReCopiarPortadas to set
	 */
	public void setBotonReCopiarPortadas(Button botonReCopiarPortadas) {
		this.botonReCopiarPortadas = botonReCopiarPortadas;
	}

	/**
	 * @param botonCancelarSubida the botonCancelarSubida to set
	 */
	public void setBotonCancelarSubida(Button botonCancelarSubida) {
		this.botonCancelarSubida = botonCancelarSubida;
	}

	/**
	 * @param botonBusquedaCodigo the botonBusquedaCodigo to set
	 */
	public void setBotonBusquedaCodigo(Button botonBusquedaCodigo) {
		this.botonBusquedaCodigo = botonBusquedaCodigo;
	}

	/**
	 * @param botonBusquedaAvanzada the botonBusquedaAvanzada to set
	 */
	public void setBotonBusquedaAvanzada(Button botonBusquedaAvanzada) {
		this.botonBusquedaAvanzada = botonBusquedaAvanzada;
	}

	/**
	 * @param botonLimpiar the botonLimpiar to set
	 */
	public void setBotonLimpiar(Button botonLimpiar) {
		this.botonLimpiar = botonLimpiar;
	}

	/**
	 * @param botonModificarCarta the botonModificarCarta to set
	 */
	public void setBotonModificarCarta(Button botonModificarCarta) {
		this.botonModificarCarta = botonModificarCarta;
	}

	/**
	 * @param botonParametroCarta the botonParametroCarta to set
	 */
	public void setBotonParametroCarta(Button botonParametroCarta) {
		this.botonParametroCarta = botonParametroCarta;
	}

	/**
	 * @param botonVender the botonVender to set
	 */
	public void setBotonVender(Button botonVender) {
		this.botonVender = botonVender;
	}

	/**
	 * @param botonbbdd the botonbbdd to set
	 */
	public void setBotonbbdd(Button botonbbdd) {
		this.botonbbdd = botonbbdd;
	}

	/**
	 * @param botonGuardarCarta the botonGuardarCarta to set
	 */
	public void setBotonGuardarCarta(Button botonGuardarCarta) {
		this.botonGuardarCarta = botonGuardarCarta;
	}

	/**
	 * @param botonGuardarCambioCarta the botonGuardarCambioCarta to set
	 */
	public void setBotonGuardarCambioCarta(Button botonGuardarCambioCarta) {
		this.botonGuardarCambioCarta = botonGuardarCambioCarta;
	}

	/**
	 * @param botonEliminarImportadoCarta the botonEliminarImportadoCarta to set
	 */
	public void setBotonEliminarImportadoCarta(Button botonEliminarImportadoCarta) {
		this.botonEliminarImportadoCarta = botonEliminarImportadoCarta;
	}

	/**
	 * @param botonSubidaPortada the botonSubidaPortada to set
	 */
	public void setBotonSubidaPortada(Button botonSubidaPortada) {
		this.botonSubidaPortada = botonSubidaPortada;
	}

	/**
	 * @param botonMostrarParametro the botonMostrarParametro to set
	 */
	public void setBotonMostrarParametro(Button botonMostrarParametro) {
		this.botonMostrarParametro = botonMostrarParametro;
	}

	/**
	 * @param botonActualizarDatos the botonActualizarDatos to set
	 */
	public void setBotonActualizarDatos(Button botonActualizarDatos) {
		this.botonActualizarDatos = botonActualizarDatos;
	}

	/**
	 * @param botonActualizarPortadas the botonActualizarPortadas to set
	 */
	public void setBotonActualizarPortadas(Button botonActualizarPortadas) {
		this.botonActualizarPortadas = botonActualizarPortadas;
	}

	/**
	 * @param botonActualizarSoftware the botonActualizarSoftware to set
	 */
	public void setBotonActualizarSoftware(Button botonActualizarSoftware) {
		this.botonActualizarSoftware = botonActualizarSoftware;
	}

	/**
	 * @param botonActualizarTodo the botonActualizarTodo to set
	 */
	public void setBotonActualizarTodo(Button botonActualizarTodo) {
		this.botonActualizarTodo = botonActualizarTodo;
	}

	/**
	 * @param botonDescargarPdf the botonDescargarPdf to set
	 */
	public void setBotonDescargarPdf(Button botonDescargarPdf) {
		this.botonDescargarPdf = botonDescargarPdf;
	}

	/**
	 * @param botonDescargarSQL the botonDescargarSQL to set
	 */
	public void setBotonDescargarSQL(Button botonDescargarSQL) {
		this.botonDescargarSQL = botonDescargarSQL;
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

	/**
	 * @param botonActualizarPrecio the botonActualizarPrecio to set
	 */
	public void setBotonActualizarPrecio(Button botonActualizarPrecio) {
		this.botonActualizarPrecio = botonActualizarPrecio;
	}

	/**
	 * @param barraCambioAltura the barraCambioAltura to set
	 */
	public void setBarraCambioAltura(Rectangle barraCambioAltura) {
		this.barraCambioAltura = barraCambioAltura;
	}

	/**
	 * @param alarmaConexionInternet the alarmaConexionInternet to set
	 */
	public void setAlarmaConexionInternet(Label alarmaConexionInternet) {
		this.alarmaConexionInternet = alarmaConexionInternet;
	}

	/**
	 * @param labelEditorial the labelEditorial to set
	 */
	public void setLabelEditorial(Label labelEditorial) {
		this.labelEditorial = labelEditorial;
	}

	/**
	 * @param labelColeccion the labelColeccion to set
	 */
	public void setLabelColeccion(Label labelColeccion) {
		this.labelColeccion = labelColeccion;
	}

	/**
	 * @param labelRareza the labelRareza to set
	 */
	public void setLabelRareza(Label labelRareza) {
		this.labelRareza = labelRareza;
	}

	/**
	 * @param labelNormas the labelNormas to set
	 */
	public void setLabelNormas(Label labelNormas) {
		this.labelNormas = labelNormas;
	}

	/**
	 * @param labelIdMod the labelIdMod to set
	 */
	public void setLabelIdMod(Label labelIdMod) {
		this.labelIdMod = labelIdMod;
	}

	/**
	 * @param labelPortada the labelPortada to set
	 */
	public void setLabelPortada(Label labelPortada) {
		this.labelPortada = labelPortada;
	}

	/**
	 * @param labelPrecioFoil the labelPrecioFoil to set
	 */
	public void setLabelPrecioFoil(Label labelPrecioFoil) {
		this.labelPrecioFoil = labelPrecioFoil;
	}

	/**
	 * @param labelPrecioNormal the labelPrecioNormal to set
	 */
	public void setLabelPrecioNormal(Label labelPrecioNormal) {
		this.labelPrecioNormal = labelPrecioNormal;
	}

	/**
	 * @param labelReferencia the labelReferencia to set
	 */
	public void setLabelReferencia(Label labelReferencia) {
		this.labelReferencia = labelReferencia;
	}

	/**
	 * @param prontInfoLabel the prontInfoLabel to set
	 */
	public void setProntInfoLabel(Label prontInfoLabel) {
		this.prontInfoLabel = prontInfoLabel;
	}

	/**
	 * @param alarmaConexionSql the alarmaConexionSql to set
	 */
	public void setAlarmaConexionSql(Label alarmaConexionSql) {
		this.alarmaConexionSql = alarmaConexionSql;
	}

	/**
	 * @param labelComprobar the labelComprobar to set
	 */
	public void setLabelComprobar(Label labelComprobar) {
		this.labelComprobar = labelComprobar;
	}

	/**
	 * @param labelVersion the labelVersion to set
	 */
	public void setLabelVersion(Label labelVersion) {
		this.labelVersion = labelVersion;
	}

	/**
	 * @param prontInfoEspecial the prontInfoEspecial to set
	 */
	public void setProntInfoEspecial(Label prontInfoEspecial) {
		this.prontInfoEspecial = prontInfoEspecial;
	}

	/**
	 * @param prontInfoPreviews the prontInfoPreviews to set
	 */
	public void setProntInfoPreviews(Label prontInfoPreviews) {
		this.prontInfoPreviews = prontInfoPreviews;
	}

	/**
	 * @param prontInfoPortadas the prontInfoPortadas to set
	 */
	public void setProntInfoPortadas(Label prontInfoPortadas) {
		this.prontInfoPortadas = prontInfoPortadas;
	}

	/**
	 * @param busquedaCodigoTextField the busquedaCodigoTextField to set
	 */
	public void setBusquedaCodigoTextField(TextField busquedaCodigoTextField) {
		this.busquedaCodigoTextField = busquedaCodigoTextField;
	}

	/**
	 * @param nombreCartaTextField the nombreCartaTextField to set
	 */
	public void setNombreCartaTextField(TextField nombreCartaTextField) {
		this.nombreCartaTextField = nombreCartaTextField;
	}

	/**
	 * @param editorialCartaTextField the editorialCartaTextField to set
	 */
	public void setEditorialCartaTextField(TextField editorialCartaTextField) {
		this.editorialCartaTextField = editorialCartaTextField;
	}

	/**
	 * @param coleccionCartaTextField the coleccionCartaTextField to set
	 */
	public void setColeccionCartaTextField(TextField coleccionCartaTextField) {
		this.coleccionCartaTextField = coleccionCartaTextField;
	}

	/**
	 * @param rarezaCartaTextField the rarezaCartaTextField to set
	 */
	public void setRarezaCartaTextField(TextField rarezaCartaTextField) {
		this.rarezaCartaTextField = rarezaCartaTextField;
	}

	/**
	 * @param normasCartaTextArea the normasCartaTextArea to set
	 */
	public void setNormasCartaTextArea(TextArea normasCartaTextArea) {
		this.normasCartaTextArea = normasCartaTextArea;
	}

	/**
	 * @param precioCartaNormalTextField the precioCartaNormalTextField to set
	 */
	public void setPrecioCartaNormalTextField(TextField precioCartaNormalTextField) {
		this.precioCartaNormalTextField = precioCartaNormalTextField;
	}

	/**
	 * @param precioCartaFoilTextField the precioCartaFoilTextField to set
	 */
	public void setPrecioCartaFoilTextField(TextField precioCartaFoilTextField) {
		this.precioCartaFoilTextField = precioCartaFoilTextField;
	}

	/**
	 * @param idCartaTratarTextField the idCartaTratarTextField to set
	 */
	public void setIdCartaTratarTextField(TextField idCartaTratarTextField) {
		this.idCartaTratarTextField = idCartaTratarTextField;
	}

	/**
	 * @param direccionImagenTextField the direccionImagenTextField to set
	 */
	public void setDireccionImagenTextField(TextField direccionImagenTextField) {
		this.direccionImagenTextField = direccionImagenTextField;
	}

	/**
	 * @param urlReferenciaTextField the urlReferenciaTextField to set
	 */
	public void setUrlReferenciaTextField(TextField urlReferenciaTextField) {
		this.urlReferenciaTextField = urlReferenciaTextField;
	}

	/**
	 * @param codigoCartaTratarTextField the codigoCartaTratarTextField to set
	 */
	public void setCodigoCartaTratarTextField(TextField codigoCartaTratarTextField) {
		this.codigoCartaTratarTextField = codigoCartaTratarTextField;
	}

	/**
	 * @param busquedaGeneralTextField the busquedaGeneralTextField to set
	 */
	public void setBusquedaGeneralTextField(TextField busquedaGeneralTextField) {
		this.busquedaGeneralTextField = busquedaGeneralTextField;
	}

	/**
	 * @param nombreCartaCombobox the nombreCartaCombobox to set
	 */
	public void setNombreCartaCombobox(ComboBox<String> nombreCartaCombobox) {
		this.nombreCartaCombobox = nombreCartaCombobox;
	}

	/**
	 * @param numeroCartaCombobox the numeroCartaCombobox to set
	 */
	public void setNumeroCartaCombobox(ComboBox<String> numeroCartaCombobox) {
		this.numeroCartaCombobox = numeroCartaCombobox;
	}

	/**
	 * @param nombreTiendaCombobox the nombreTiendaCombobox to set
	 */
	public void setNombreTiendaCombobox(ComboBox<String> nombreTiendaCombobox) {
		this.nombreTiendaCombobox = nombreTiendaCombobox;
	}

	/**
	 * @param nombreEditorialCombobox the nombreEditorialCombobox to set
	 */
	public void setNombreEditorialCombobox(ComboBox<String> nombreEditorialCombobox) {
		this.nombreEditorialCombobox = nombreEditorialCombobox;
	}

	/**
	 * @param nombreColeccionCombobox the nombreColeccionCombobox to set
	 */
	public void setNombreColeccionCombobox(ComboBox<String> nombreColeccionCombobox) {
		this.nombreColeccionCombobox = nombreColeccionCombobox;
	}

	/**
	 * @param nombreRarezaCombobox the nombreRarezaCombobox to set
	 */
	public void setNombreRarezaCombobox(ComboBox<String> nombreRarezaCombobox) {
		this.nombreRarezaCombobox = nombreRarezaCombobox;
	}

	/**
	 * @param comboPreviewsCombobox the comboPreviewsCombobox to set
	 */
	public void setComboPreviewsCombobox(ComboBox<String> comboPreviewsCombobox) {
		this.comboPreviewsCombobox = comboPreviewsCombobox;
	}

	/**
	 * @param prontInfoTextArea the prontInfoTextArea to set
	 */
	public void setProntInfoTextArea(TextArea prontInfoTextArea) {
		this.prontInfoTextArea = prontInfoTextArea;
	}

	/**
	 * @param menuImportarFicheroCodigoBarras the menuImportarFicheroCodigoBarras to
	 *                                        set
	 */
	public void setMenuImportarFicheroCodigoBarras(MenuItem menuImportarFicheroCodigoBarras) {
		this.menuImportarFicheroCodigoBarras = menuImportarFicheroCodigoBarras;
	}

	/**
	 * @param menuCartaAniadir the menuCartaAniadir to set
	 */
	public void setMenuCartaAniadir(MenuItem menuCartaAniadir) {
		this.menuCartaAniadir = menuCartaAniadir;
	}

	/**
	 * @param menuCartaEliminar the menuCartaEliminar to set
	 */
	public void setMenuCartaEliminar(MenuItem menuCartaEliminar) {
		this.menuCartaEliminar = menuCartaEliminar;
	}

	/**
	 * @param menuCartaModificar the menuCartaModificar to set
	 */
	public void setMenuCartaModificar(MenuItem menuCartaModificar) {
		this.menuCartaModificar = menuCartaModificar;
	}

	/**
	 * @param menuEstadisticaEstadistica the menuEstadisticaEstadistica to set
	 */
	public void setMenuEstadisticaEstadistica(MenuItem menuEstadisticaEstadistica) {
		this.menuEstadisticaEstadistica = menuEstadisticaEstadistica;
	}

	/**
	 * @param menuArchivoCerrar the menuArchivoCerrar to set
	 */
	public void setMenuArchivoCerrar(MenuItem menuArchivoCerrar) {
		this.menuArchivoCerrar = menuArchivoCerrar;
	}

	/**
	 * @param menuArchivoDelete the menuArchivoDelete to set
	 */
	public void setMenuArchivoDelete(MenuItem menuArchivoDelete) {
		this.menuArchivoDelete = menuArchivoDelete;
	}

	/**
	 * @param menuArchivoDesconectar the menuArchivoDesconectar to set
	 */
	public void setMenuArchivoDesconectar(MenuItem menuArchivoDesconectar) {
		this.menuArchivoDesconectar = menuArchivoDesconectar;
	}

	/**
	 * @param menuArchivoExcel the menuArchivoExcel to set
	 */
	public void setMenuArchivoExcel(MenuItem menuArchivoExcel) {
		this.menuArchivoExcel = menuArchivoExcel;
	}

	/**
	 * @param menuArchivoImportar the menuArchivoImportar to set
	 */
	public void setMenuArchivoImportar(MenuItem menuArchivoImportar) {
		this.menuArchivoImportar = menuArchivoImportar;
	}

	/**
	 * @param menuArchivoSobreMi the menuArchivoSobreMi to set
	 */
	public void setMenuArchivoSobreMi(MenuItem menuArchivoSobreMi) {
		this.menuArchivoSobreMi = menuArchivoSobreMi;
	}

	/**
	 * @param menuEstadisticaComprados the menuEstadisticaComprados to set
	 */
	public void setMenuEstadisticaSumaTotal(MenuItem menuEstadisticaSumaTotal) {
		this.menuEstadisticaSumaTotal = menuEstadisticaSumaTotal;
	}

	/**
	 * @param menuArchivoAvanzado the menuArchivoAvanzado to set
	 */
	public void setMenuArchivoAvanzado(MenuItem menuArchivoAvanzado) {
		this.menuArchivoAvanzado = menuArchivoAvanzado;
	}

	/**
	 * @param navegacionCerrar the navegacionCerrar to set
	 */
	public void setNavegacionCerrar(Menu navegacionCerrar) {
		this.navegacionCerrar = navegacionCerrar;
	}

	/**
	 * @param navegacionCarta the navegacionCarta to set
	 */
	public void setNavegacionCarta(Menu navegacionCarta) {
		this.navegacionCarta = navegacionCarta;
	}

	/**
	 * @param navegacionEstadistica the navegacionEstadistica to set
	 */
	public void setNavegacionEstadistica(Menu navegacionEstadistica) {
		this.navegacionEstadistica = navegacionEstadistica;
	}

	/**
	 * @param menuNavegacion the menuNavegacion to set
	 */
	public void setMenuNavegacion(MenuBar menuNavegacion) {
		this.menuNavegacion = menuNavegacion;
	}

	/**
	 * @param progresoCarga the progresoCarga to set
	 */
	public void setProgresoCarga(ProgressIndicator progresoCarga) {
		this.progresoCarga = progresoCarga;
	}

	/**
	 * @param checkFirmas the checkFirmas to set
	 */
	public void setCheckFirmas(CheckBox checkFirmas) {
		this.checkFirmas = checkFirmas;
	}

	/**
	 * @param controlAccion the controlAccion to set
	 */
	public void setControlAccion(List<Control> controlAccion) {
		this.controlAccion = controlAccion;
	}

	/**
	 * @param listaComboboxes the listaComboboxes to set
	 */
	public static void setListaComboboxes(List<ComboBox<String>> listaComboboxes) {
		AccionReferencias.listaComboboxes = listaComboboxes;
	}

	/**
	 * @param listaColumnasTabla the listaColumnasTabla to set
	 */
	public static void setListaColumnasTabla(List<TableColumn<Carta, String>> listaColumnasTabla) {
		AccionReferencias.listaColumnasTabla = listaColumnasTabla;
	}

	/**
	 * @param listaTextFields the listaTextFields to set
	 */
	public static void setListaTextFields(ObservableList<Control> listaTextFields) {
		AccionReferencias.listaTextFields = listaTextFields;
	}

	/**
	 * @param listaBotones the listaBotones to set
	 */
	public static void setListaBotones(ObservableList<Button> listaBotones) {
		AccionReferencias.listaBotones = listaBotones;
	}

	/**
	 * @param listaElementosFondo the listaElementosFondo to set
	 */
	public static void setListaElementosFondo(ObservableList<Node> listaElementosFondo) {
		AccionReferencias.listaElementosFondo = listaElementosFondo;
	}

	/**
	 * @param stageVentana the stageVentana to set
	 */
	public void setStageVentana(Stage stageVentana) {
		this.stageVentana = stageVentana;
	}
}
