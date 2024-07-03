package funcionesManagment;

import java.io.File;
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
				referenciaVentana.getBotonParametroCarta(), referenciaVentana.getIdCartaTratarTextField()));

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
			// Obtener la carta a eliminar
			Carta cartaEliminar = ListasCartasDAO.cartasImportados.stream().filter(c -> c.getIdCarta().equals(idCarta))
					.findFirst().orElse(null);

			if (cartaEliminar != null) {
				// Obtener la dirección de la imagen y verificar si existe
				String direccionImagen = cartaEliminar.getDireccionImagenCarta();
				if (direccionImagen != null && !direccionImagen.isEmpty()) {
					File archivoImagen = new File(direccionImagen);
					if (archivoImagen.exists()) {
						// Borrar el archivo de la imagen
						if (archivoImagen.delete()) {
							System.out.println("Archivo de imagen eliminado: " + direccionImagen);
						} else {
							System.err.println("No se pudo eliminar el archivo de imagen: " + direccionImagen);
							// Puedes lanzar una excepción aquí si lo prefieres
						}
					}
				}

				// Eliminar la carta de la lista
				ListasCartasDAO.cartasImportados.remove(cartaEliminar);
				AccionControlUI.limpiarAutorellenos(false);
				FuncionesTableView.nombreColumnas();
				FuncionesTableView.tablaBBDD(ListasCartasDAO.cartasImportados);
				getReferenciaVentana().getTablaBBDD().refresh();

				// Verificar si la lista está vacía y cambiar el estado de los botones
				if (ListasCartasDAO.cartasImportados.isEmpty()) {
					AccionFuncionesComunes.cambiarEstadoBotones(false);
				}
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
