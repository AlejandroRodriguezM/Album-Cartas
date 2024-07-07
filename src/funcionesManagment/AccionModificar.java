package funcionesManagment;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import alarmas.AlarmaList;
import cartaManagement.Carta;
import dbmanager.CartaManagerDAO;
import dbmanager.ConectManager;
import dbmanager.DBUtilidades;
import dbmanager.ListasCartasDAO;
import dbmanager.SelectManager;
import funcionesAuxiliares.Utilidades;
import funcionesAuxiliares.Ventanas;
import funcionesInterfaz.AccionControlUI;
import funcionesInterfaz.FuncionesComboBox;
import funcionesInterfaz.FuncionesManejoFront;
import funcionesInterfaz.FuncionesTableView;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Pair;
import webScrap.WebScrapNodeJSInstall;

public class AccionModificar {

	private static AccionFuncionesComunes accionFuncionesComunes = new AccionFuncionesComunes();

	private static AccionReferencias referenciaVentana = getReferenciaVentana();

	private static AccionReferencias referenciaVentanaPrincipal = getReferenciaVentanaPrincipal();

	private static AccionControlUI accionRellenoDatos = new AccionControlUI();

	/**
	 * Instancia de la clase FuncionesComboBox para el manejo de ComboBox.
	 */
	private static FuncionesComboBox funcionesCombo = new FuncionesComboBox();

	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();

	/**
	 * Oculta y deshabilita varios campos y elementos en la interfaz gráfica.
	 */
	public void ocultarCamposMod() {
//		List<Node> elementos = Arrays.asList(getReferenciaVentana().getUrlReferencia(),
//				getReferenciaVentana().getLabel_id_mod(), getReferenciaVentana().getIdCartaTratar(),
//				getReferenciaVentana().getPrecioCarta(), getReferenciaVentana().getDireccionImagen(),
//				getReferenciaVentana().getLabel_portada(), getReferenciaVentana().getLabel_precio(),
//				getReferenciaVentana().getLabel_referencia(), getReferenciaVentana().getBotonModificarCarta(),
//				getReferenciaVentana().getCodigoCartaTratar());
//
//		Utilidades.cambiarVisibilidad(elementos, true);
	}

	public static void venderCarta() throws SQLException {

		String idCarta = getReferenciaVentana().getIdCartaTratarTextField().getText();
		getReferenciaVentana().getIdCartaTratarTextField().setStyle("");
		Carta comicActualizar = CartaManagerDAO.cartaDatos(idCarta);
		if (accionFuncionesComunes.comprobarExistenciaCarta(idCarta)) {
			if (nav.alertaAccionGeneral()) {
				CartaManagerDAO.actualizarCartaBBDD(comicActualizar, "vender");
				ListasCartasDAO.reiniciarListaCartas();
				String mensaje = ". Has puesto a la venta el comic";
				AlarmaList.mostrarMensajePront(mensaje, false, getReferenciaVentana().getProntInfoTextArea());

				getReferenciaVentana();
				List<ComboBox<String>> comboboxes = AccionReferencias.getListaComboboxes();

				funcionesCombo.rellenarComboBox(comboboxes);
				getReferenciaVentana().getTablaBBDD().refresh();
				FuncionesTableView.nombreColumnas();
				FuncionesTableView.tablaBBDD(ListasCartasDAO.cartasImportados);

			} else {
				String mensaje = "Accion cancelada";
				AlarmaList.mostrarMensajePront(mensaje, false, getReferenciaVentana().getProntInfoTextArea());
			}

		}
	}

	public static void modificarCarta() throws Exception {

		String idCarta = getReferenciaVentana().getIdCartaTratarTextField().getText();
		getReferenciaVentana().getIdCartaTratarTextField().setStyle("");

		if (accionFuncionesComunes.comprobarExistenciaCarta(idCarta)) {
			String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);
			List<Carta> listaCartas;
			if (nav.alertaAccionGeneral()) {

				Utilidades.convertirNombresCarpetas(AccionFuncionesComunes.carpetaPortadas(Utilidades.nombreDB()));

				Carta comicModificado = AccionControlUI.cartaModificado();

				accionFuncionesComunes.procesarCarta(comicModificado, true);

				ListasCartasDAO.listasAutoCompletado();

				listaCartas = CartaManagerDAO.verLibreria(sentenciaSQL);
				getReferenciaVentana().getTablaBBDD().refresh();
				FuncionesTableView.nombreColumnas();
				FuncionesTableView.tablaBBDD(listaCartas);
			} else {
				listaCartas = CartaManagerDAO.verLibreria(sentenciaSQL);
//				CartaManagerDAO.borrarCarta(idCarta);
				ListasCartasDAO.reiniciarListaCartas();
				ListasCartasDAO.listasAutoCompletado();
				FuncionesTableView.nombreColumnas(); // Llamada a funcion
				FuncionesTableView.actualizarBusquedaRaw();
				FuncionesTableView.tablaBBDD(listaCartas);
			}
		}
	}

	public static void actualizarCartaLista() {

		if (!accionRellenoDatos.camposCartaSonValidos()) {
			String mensaje = "Error. Debes de introducir los datos correctos";
			AlarmaList.mostrarMensajePront(mensaje, false, getReferenciaVentana().getProntInfoTextArea());
			return; // Agregar return para salir del método en este punto
		}

		List<Control> allControls = getReferenciaVentana().getControlAccion();
		List<String> valorControles = new ArrayList<>();
		for (Control control : allControls) {
			if (control instanceof TextField) {
				String value = ((TextField) control).getText();
				valorControles.add(value);
			} else if (control instanceof ComboBox<?>) {
				Object selectedItem = ((ComboBox<?>) control).getSelectionModel().getSelectedItem();
				String value = selectedItem != null ? selectedItem.toString() : "";
				valorControles.add(value);
			} else if (control instanceof TextArea) {
				String value = ((TextArea) control).getText();
				valorControles.add(value);
			}
		}

		Carta datos = AccionControlUI.camposCarta(valorControles, true);

		if (!ListasCartasDAO.cartasImportados.isEmpty()) {

			if (datos.getIdCarta() == null || datos.getIdCarta().isEmpty()) {
				datos = ListasCartasDAO.buscarCartaPorID(ListasCartasDAO.cartasImportados, datos.getIdCarta());
			}

			// Si hay elementos en la lista
			for (Carta c : ListasCartasDAO.cartasImportados) {
				if (c.getIdCarta().equals(datos.getIdCarta())) {
					// Si se encuentra un cómic con el mismo ID, reemplazarlo con los nuevos datos
					ListasCartasDAO.cartasImportados.set(ListasCartasDAO.cartasImportados.indexOf(c), datos);
					break; // Salir del bucle una vez que se actualice el cómic
				}
			}
		} else {
			String id = "A" + 0 + "" + (ListasCartasDAO.cartasImportados.size() + 1);
			datos.setIdCarta(id);
			ListasCartasDAO.cartasImportados.add(datos);
			getReferenciaVentana().getBotonGuardarListaCartas().setVisible(true);
			getReferenciaVentana().getBotonEliminarImportadoListaCarta().setVisible(true);

			getReferenciaVentana().getBotonGuardarListaCartas().setDisable(false);
			getReferenciaVentana().getBotonEliminarImportadoListaCarta().setDisable(false);
		}

		AccionFuncionesComunes.cambiarEstadoBotones(false);
		getReferenciaVentana().getBotonCancelarSubida().setVisible(false); // Oculta el botón de cancelar

		Carta.limpiarCamposCarta(datos);
		AccionControlUI.limpiarAutorellenos(false);

		FuncionesTableView.nombreColumnas();
		FuncionesTableView.tablaBBDD(ListasCartasDAO.cartasImportados);
	}

	public void mostrarElementosModificar(List<Node> elementosAMostrarYHabilitar) {

		elementosAMostrarYHabilitar.addAll(Arrays.asList(referenciaVentana.getLabelRareza(),
				referenciaVentana.getLabelNormas(), referenciaVentana.getLabelPrecioNormal(),
				referenciaVentana.getLabelPrecioFoil(), referenciaVentana.getLabelIdMod(),
				referenciaVentana.getLabelPortada(), referenciaVentana.getLabelReferencia()));

		elementosAMostrarYHabilitar
				.addAll(Arrays.asList(referenciaVentana.getNumeroCartaCombobox(), getReferenciaVentana().getRootVBox(),
						getReferenciaVentana().getBotonSubidaPortada(), getReferenciaVentana().getBotonbbdd(),
						getReferenciaVentana().getTablaBBDD(), getReferenciaVentana().getBotonParametroCarta()));

		elementosAMostrarYHabilitar.addAll(Arrays.asList(referenciaVentana.getRarezaCartaTextField(),
				referenciaVentana.getNormasCartaTextArea(), referenciaVentana.getIdCartaTratarTextField(),
				referenciaVentana.getDireccionImagenTextField(), referenciaVentana.getUrlReferenciaTextField(),
				referenciaVentana.getPrecioCartaNormalTextField(), referenciaVentana.getPrecioCartaFoilTextField()));

		elementosAMostrarYHabilitar.addAll(Arrays.asList(referenciaVentana.getBotonSubidaPortada(),
				getReferenciaVentana().getBotonModificarCarta()));

		getReferenciaVentana().getRootVBox().toFront();
	}

	public static void actualizarDatabase(String tipoUpdate, Stage ventanaOpciones) {

		if (!Utilidades.isInternetAvailable()) {
			return;
		}

		List<String> inputPortadas = DBUtilidades.obtenerValoresColumna("direccionImagenCarta");
		Utilidades.borrarArchivosNoEnLista(inputPortadas);

		boolean estaBaseLlena = ListasCartasDAO.comprobarLista();

		if (!estaBaseLlena) {
			String cadenaCancelado = "La base de datos esta vacia";
			AlarmaList.iniciarAnimacionAvanzado(getReferenciaVentana().getProntInfoEspecial(), cadenaCancelado);
			return;
		}

		String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);
		List<Carta> listaCartasDatabase = SelectManager.verLibreria(sentenciaSQL, true);

		Collections.sort(listaCartasDatabase, (comic1, comic2) -> {
			int id1 = Integer.parseInt(comic1.getIdCarta());
			int id2 = Integer.parseInt(comic2.getIdCarta());
			return Integer.compare(id1, id2);
		});

		List<Stage> stageVentanas = FuncionesManejoFront.getStageVentanas();

		for (Stage stage : stageVentanas) {
			if (stage != ventanaOpciones && !stage.getTitle().equalsIgnoreCase("Menu principal")) {
				stage.close(); // Close the stage if it's not the current state
			}
		}

		if (WebScrapNodeJSInstall.checkNodeJSVersion()) {
			AccionFuncionesComunes.busquedaPorListaDatabase(listaCartasDatabase, tipoUpdate);

			if (getReferenciaVentana().getTablaBBDD() != null) {
				getReferenciaVentana().getTablaBBDD().refresh();
				FuncionesTableView.nombreColumnas();
				FuncionesTableView.tablaBBDD(ListasCartasDAO.cartasImportados);
			}
		}
	}

	public static AccionReferencias getReferenciaVentana() {
		return referenciaVentana;
	}

	public static AccionReferencias getReferenciaVentanaPrincipal() {
		return referenciaVentanaPrincipal;
	}

	public static void setReferenciaVentana(AccionReferencias referenciaVentana) {
		AccionModificar.referenciaVentana = referenciaVentana;
	}

	public static void setReferenciaVentanaPrincipal(AccionReferencias referenciaVentana) {
		AccionModificar.referenciaVentanaPrincipal = referenciaVentana;
	}

}
