package funcionesManagment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import alarmas.AlarmaList;
import cartaManagement.Carta;
import dbmanager.CartaManagerDAO;
import dbmanager.ConectManager;
import dbmanager.ListasCartasDAO;
import funcionesAuxiliares.Utilidades;
import funcionesAuxiliares.Ventanas;
import funcionesInterfaz.AccionControlUI;
import funcionesInterfaz.FuncionesComboBox;
import funcionesInterfaz.FuncionesTableView;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;

public class AccionAniadir {

	private static AccionFuncionesComunes accionFuncionesComunes = new AccionFuncionesComunes();

	private static AccionReferencias referenciaVentana = getReferenciaVentana();

	private static AccionControlUI accionRellenoDatos = new AccionControlUI();

	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();

	/**
	 * Instancia de la clase FuncionesComboBox para el manejo de ComboBox.
	 */
	private static FuncionesComboBox funcionesCombo = new FuncionesComboBox();

	/**
	 * Permite introducir un comic en la base de datos de forma manual
	 * 
	 * @throws Exception
	 */
	public void subidaCarta() throws Exception {

		Utilidades.convertirNombresCarpetas(AccionFuncionesComunes.carpetaPortadas(ConectManager.DB_NAME));

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
		accionRellenoDatos.actualizarCamposUnicos(comic);

		referenciaVentana.getProntInfoTextArea().setOpacity(1);

		accionFuncionesComunes.procesarCarta(comic, false);
	}

	public static void guardarContenidoLista(boolean esLista, Carta carta) {

		if (!ListasCartasDAO.cartasImportados.isEmpty() && nav.alertaInsertar()) {
			Collections.sort(ListasCartasDAO.cartasImportados, Comparator.comparing(Carta::getNomCarta));
			String mensajePront = "";
			if (esLista) {
				for (Carta c : ListasCartasDAO.cartasImportados) {
					if (AccionControlUI.comprobarListaValidacion(c)) {
						CartaManagerDAO.insertarDatos(c, true);
					}
				}
				ListasCartasDAO.cartasImportados.clear();
				mensajePront = "Has introducido las cartas correctamente\n\n";
			} else {
				CartaManagerDAO.insertarDatos(carta, true);

				ListasCartasDAO.cartasImportados.removeIf(c -> c.getIdCarta().equals(carta.getIdCarta()));

				mensajePront = "Has introducido la carta correctamente\n\n";
			}

			ListasCartasDAO.listasAutoCompletado();
			getReferenciaVentana();
			List<ComboBox<String>> comboboxes = AccionReferencias.getListaComboboxes();
			funcionesCombo.rellenarComboBox(comboboxes);

			referenciaVentana.getTablaBBDD().getItems().clear();
			AccionControlUI.validarCamposClave(true);
			FuncionesTableView.tablaBBDD(ListasCartasDAO.cartasImportados);
			AccionControlUI.limpiarAutorellenos(false);

			AlarmaList.mostrarMensajePront(mensajePront, true, referenciaVentana.getProntInfoTextArea());
		}

	}

	public void mostrarElementosAniadir(List<Node> elementosAMostrarYHabilitar) {

		elementosAMostrarYHabilitar.addAll(Arrays.asList(referenciaVentana.getLabelRareza(),
				referenciaVentana.getLabelNormas(), referenciaVentana.getLabelPrecioNormal(),referenciaVentana.getLabelPrecioFoil(),
				referenciaVentana.getLabelIdMod(), referenciaVentana.getLabelPortada(), referenciaVentana.getLabelReferencia()));

		elementosAMostrarYHabilitar.addAll(
				Arrays.asList(referenciaVentana.getNumeroCartaCombobox()));

		elementosAMostrarYHabilitar.addAll(Arrays.asList(referenciaVentana.getRarezaCartaTextField(),
				referenciaVentana.getNormasCartaTextArea(), referenciaVentana.getPrecioCartaNormalTextField(),referenciaVentana.getPrecioCartaFoilTextField(),
				referenciaVentana.getIdCartaTratarTextField(), referenciaVentana.getDireccionImagenTextField(),
				referenciaVentana.getUrlReferenciaTextField()));

		elementosAMostrarYHabilitar.addAll(Arrays.asList(referenciaVentana.getBotonSubidaPortada(),
				referenciaVentana.getBotonBusquedaAvanzada(), referenciaVentana.getBotonGuardarCambioCarta(),
				referenciaVentana.getBotonEliminarImportadoListaCarta(),
				referenciaVentana.getBotonGuardarListaCartas()));
	}

	public static AccionReferencias getReferenciaVentana() {
		return referenciaVentana;
	}

	public static void setReferenciaVentana(AccionReferencias referenciaVentana) {
		AccionAniadir.referenciaVentana = referenciaVentana;
	}

}
