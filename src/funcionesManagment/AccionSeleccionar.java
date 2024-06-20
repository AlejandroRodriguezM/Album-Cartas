package funcionesManagment;

import java.sql.SQLException;
import java.util.List;

import alarmas.AlarmaList;
import cartaManagement.Carta;
import dbmanager.CartaManagerDAO;
import dbmanager.DBUtilidades;
import dbmanager.DBUtilidades.TipoBusqueda;
import funcionesAuxiliares.Utilidades;
import dbmanager.ListasCartasDAO;
import dbmanager.SelectManager;
import funcionesInterfaz.AccionControlUI;
import funcionesInterfaz.FuncionesTableView;
import javafx.collections.FXCollections;

public class AccionSeleccionar {

	private static AccionReferencias referenciaVentana = getReferenciaVentana();

	private static AccionControlUI accionRellenoDatos = new AccionControlUI();

	/**
	 * Método para seleccionar y mostrar detalles de un cómic en la interfaz
	 * gráfica. Si la lista de cómics importados no está vacía, utiliza la
	 * información de la lista; de lo contrario, consulta la base de datos para
	 * obtener la información del cómic.
	 * 
	 * @throws SQLException Si se produce un error al acceder a la base de datos.
	 */
	public static void seleccionarCartas(boolean esPrincipal) {

		FuncionesTableView.nombreColumnas(); // funcion
		Utilidades.comprobacionListaCartas();

		Carta newSelection = getReferenciaVentana().getTablaBBDD().getSelectionModel().getSelectedItem();

		// Verificar si idRow es nulo antes de intentar acceder a sus métodos
		if (newSelection != null) {
			String idCarta = newSelection.getIdCarta();

			mostrarCarta(idCarta, esPrincipal);
		}
	}

	public static void mostrarCarta(String idCarta, boolean esPrincipal) {

		Carta comicTemp = null;
		AlarmaList.detenerAnimacion();
		String mensaje = "";

		if (!ListasCartasDAO.cartasImportados.isEmpty() && !esPrincipal) {
			comicTemp = ListasCartasDAO.buscarCartaPorID(ListasCartasDAO.cartasImportados, idCarta);
		} else {
			comicTemp = CartaManagerDAO.cartaDatos(idCarta);
		}

		if (idCarta == null || idCarta.isEmpty() || comicTemp == null) {
			AccionControlUI.limpiarAutorellenos(esPrincipal);
			return;
		}

		Carta.limpiarCamposCarta(comicTemp);

		if (!esPrincipal) {
			accionRellenoDatos.setAtributosDesdeTabla(comicTemp);
			AccionControlUI.validarCamposClave(false);

			if (AccionFuncionesComunes.TIPO_ACCION.equals("modificar")) {
				AccionControlUI.mostrarOpcion(AccionFuncionesComunes.TIPO_ACCION);
				getReferenciaVentana().getIdCartaTratarTextField().setText(comicTemp.getIdCarta());
			}
		} else {
			Utilidades.cargarImagenAsync(comicTemp.getDireccionImagenCarta(), getReferenciaVentana().getImagenCarta());
		}

		getReferenciaVentana().getProntInfoTextArea().setOpacity(1);

		if (!ListasCartasDAO.cartasImportados.isEmpty() && CartaManagerDAO.comprobarIdentificadorCarta(idCarta)) {
			mensaje = CartaManagerDAO.cartaDatos(idCarta).toString().replace("[", "").replace("]", "");
		} else {
			mensaje = comicTemp.toString().replace("[", "").replace("]", "");
		}
		getReferenciaVentana().getProntInfoTextArea().clear();
		getReferenciaVentana().getProntInfoTextArea().setText(mensaje);

	}

	public static void verBasedeDatos(boolean completo, boolean esAccion, Carta comic) {

		ListasCartasDAO.reiniciarListaCartas();
		getReferenciaVentana().getTablaBBDD().refresh();
		getReferenciaVentana().getProntInfoTextArea().setOpacity(0);
		getReferenciaVentana().getImagenCarta().setVisible(false);
		getReferenciaVentana().getImagenCarta().setImage(null);
		getReferenciaVentana().getProntInfoTextArea().setText(null);
		getReferenciaVentana().getProntInfoTextArea().clear();

		FuncionesTableView.nombreColumnas();
		FuncionesTableView.actualizarBusquedaRaw();

		if (CartaManagerDAO.countRows() > 0) {
			if (completo) {

				String sentenciaSQL = DBUtilidades.construirSentenciaSQL(TipoBusqueda.COMPLETA);

				List<Carta> listaCartas = CartaManagerDAO.verLibreria(sentenciaSQL);

				FuncionesTableView.tablaBBDD(listaCartas);

			} else {

				List<Carta> listaParametro = listaPorParametro(comic, esAccion);

				FuncionesTableView.tablaBBDD(listaParametro);

				if (!esAccion) {
					if (!listaParametro.isEmpty()) {
						getReferenciaVentana().getBotonImprimir().setVisible(true);
						getReferenciaVentana().getBotonGuardarResultado().setVisible(true);
					} else {
						getReferenciaVentana().getBotonImprimir().setVisible(false);
						getReferenciaVentana().getBotonGuardarResultado().setVisible(false);
					}
					getReferenciaVentana().getBusquedaGeneralTextField().setText("");
				}

			}
		} else {
			String mensaje = "ERROR. No hay datos en la base de datos";

			AlarmaList.mostrarMensajePront(mensaje, false, getReferenciaVentana().getProntInfoTextArea());
		}
	}

	/**
	 * Funcion que comprueba segun los datos escritos en los textArea, que comic
	 * estas buscando.
	 * 
	 * @throws SQLException
	 */
	public static List<Carta> listaPorParametro(Carta datos, boolean esAccion) {
		String busquedaGeneralTextField = "";

		if (!esAccion) {
			busquedaGeneralTextField = getReferenciaVentana().getBusquedaGeneralTextField().getText();
		}

		List<Carta> listCarta = FXCollections
				.observableArrayList(SelectManager.busquedaParametro(datos, busquedaGeneralTextField));

		if (!listCarta.isEmpty()) {
			getReferenciaVentana().getProntInfoTextArea().setOpacity(1);
			getReferenciaVentana().getProntInfoTextArea().setStyle("-fx-text-fill: black;"); // Reset the text color to black
			getReferenciaVentana().getProntInfoTextArea()
					.setText("El número de cómics donde aparece la búsqueda es: " + listCarta.size() + "\n \n \n");
		} else if (listCarta.isEmpty() && esAccion) {
			getReferenciaVentana().getProntInfoTextArea().setOpacity(1);
			// Show error message in red when no search fields are specified
			getReferenciaVentana().getProntInfoTextArea().setStyle("-fx-text-fill: red;");
			getReferenciaVentana().getProntInfoTextArea().setText("Error. No existen con dichos parametros.");
		}

		return listCarta;
	}

	public static AccionReferencias getReferenciaVentana() {
		return referenciaVentana;
	}

	public static void setReferenciaVentana(AccionReferencias referenciaVentana) {
		AccionSeleccionar.referenciaVentana = referenciaVentana;
	}

}
