package funcionesManagment;

import java.util.Arrays;
import java.util.List;

import alarmas.AlarmaList;
import cartaManagement.Carta;
import dbmanager.CartaManagerDAO;
import dbmanager.DBUtilidades;
import dbmanager.ListasCartasDAO;
import funcionesAuxiliares.Ventanas;
import funcionesInterfaz.AccionControlUI;
import funcionesInterfaz.FuncionesComboBox;
import funcionesInterfaz.FuncionesTableView;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;

public class AccionEliminar {

	private static AccionFuncionesComunes accionFuncionesComunes = new AccionFuncionesComunes();
	private static AccionReferencias referenciaVentana = getReferenciaVentana();
	/**
	 * Instancia de la clase Ventanas para la navegación.
	 */
	private static Ventanas nav = new Ventanas();

	/**
	 * Instancia de la clase FuncionesComboBox para el manejo de ComboBox.
	 */
	private static FuncionesComboBox funcionesCombo = new FuncionesComboBox();

	public void mostrarElementosEliminar(List<Node> elementosAMostrarYHabilitar) {
		elementosAMostrarYHabilitar.addAll(Arrays.asList(referenciaVentana.getLabelIdMod(),
				referenciaVentana.getBotonVender(), referenciaVentana.getBotonEliminar(),
				referenciaVentana.getTablaBBDD(), referenciaVentana.getBotonbbdd(), referenciaVentana.getRootVBox(),
				referenciaVentana.getBotonParametroCarta(), referenciaVentana.getIdCartaTratarTextField(),
				referenciaVentana.getNumeroCartaCombobox()));
		getReferenciaVentana().getLabelColeccion().setVisible(false);
		getReferenciaVentana().getLabelIdMod().setLayoutX(5);
		referenciaVentana.getRootVBox().toFront();
	}

	public static void eliminarCarta() {

		String idCarta = getReferenciaVentana().getIdCartaTratarTextField().getText();
		getReferenciaVentana().getIdCartaTratarTextField().setStyle("");
		if (accionFuncionesComunes.comprobarExistenciaCarta(idCarta)) {
			if (nav.alertaAccionGeneral()) {

				CartaManagerDAO.borrarCarta(idCarta);
				ListasCartasDAO.reiniciarListaCartas();
				ListasCartasDAO.listasAutoCompletado();

				String sentenciaSQL = DBUtilidades.construirSentenciaSQL(DBUtilidades.TipoBusqueda.COMPLETA);
				List<Carta> listaCartas = CartaManagerDAO.verLibreria(sentenciaSQL);
				getReferenciaVentana().getTablaBBDD().refresh();
				FuncionesTableView.nombreColumnas();
				FuncionesTableView.tablaBBDD(listaCartas);

				List<ComboBox<String>> comboboxes = getReferenciaVentana().getListaComboboxes();

				funcionesCombo.rellenarComboBox(comboboxes);
				getReferenciaVentana().getImagenCarta().setImage(null);
				getReferenciaVentana().getImagenCarta().setVisible(true);
				getReferenciaVentana().getProntInfoTextArea().clear();
				getReferenciaVentana().getProntInfoTextArea().setOpacity(0);

			} else {
				String mensaje = "Accion cancelada";
				AlarmaList.mostrarMensajePront(mensaje, false, getReferenciaVentana().getProntInfoTextArea());
			}
		}
	}

	public static void eliminarCartaLista() {

		String idCarta = getReferenciaVentana().getIdCartaTratarTextField().getText();

		if (nav.alertaEliminar() && idCarta != null) {

			ListasCartasDAO.cartasImportados.removeIf(c -> c.getIdCarta().equals(idCarta));
			AccionControlUI.limpiarAutorellenos(false);
			FuncionesTableView.nombreColumnas();

			FuncionesTableView.tablaBBDD(ListasCartasDAO.cartasImportados);
			getReferenciaVentana().getTablaBBDD().refresh();

			if (ListasCartasDAO.cartasImportados.isEmpty()) {
				AccionFuncionesComunes.cambiarEstadoBotones(false);
			}

		}

	}

	public static AccionReferencias getReferenciaVentana() {
		return referenciaVentana;
	}

	public static void setReferenciaVentana(AccionReferencias referenciaVentana) {
		AccionEliminar.referenciaVentana = referenciaVentana;
	}

}
