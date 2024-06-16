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
		elementosAMostrarYHabilitar.addAll(Arrays.asList(referenciaVentana.getLabel_id_mod(),
				referenciaVentana.getBotonVender(), referenciaVentana.getBotonEliminar(),
				referenciaVentana.getTablaBBDD(), referenciaVentana.getBotonbbdd(), referenciaVentana.getRootVBox(),
				referenciaVentana.getBotonParametroCarta(), referenciaVentana.getIdCartaTratar()));
		referenciaVentana.getRootVBox().toFront();
	}

	public static void eliminarCarta() {
		
		String idCarta = getReferenciaVentana().getIdCartaTratar().getText();
		getReferenciaVentana().getIdCartaTratar().setStyle("");
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

				List<ComboBox<String>> comboboxes = getReferenciaVentana().getComboboxes();

				funcionesCombo.rellenarComboBox(comboboxes);
				getReferenciaVentana().getImagencomic().setImage(null);
				getReferenciaVentana().getImagencomic().setVisible(true);
				getReferenciaVentana().getProntInfo().clear();
				getReferenciaVentana().getProntInfo().setOpacity(0);

			} else {
				String mensaje = "Accion cancelada";
				AlarmaList.mostrarMensajePront(mensaje, false, getReferenciaVentana().getProntInfo());
			}
		}
	}

	public static void eliminarCartaLista() {
		
		String idCarta = getReferenciaVentana().getIdCartaTratar().getText();

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
