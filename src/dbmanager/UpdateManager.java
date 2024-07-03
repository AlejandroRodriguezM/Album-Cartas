package dbmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import cartaManagement.Carta;
import funcionesAuxiliares.Utilidades;

public class UpdateManager {

	private static final String UPDATE_CARTA = "UPDATE albumbbdd SET "
			+ "nomCarta = ?, numCarta = ?, editorialCarta = ?, coleccionCarta = ?, rarezaCarta = ?, "
			+ "precioCartaNormal = ?, precioCartaFoil = ?, urlReferenciaCarta = ?, "
			+ "direccionImagenCarta = ?, normasCarta = ? " + "WHERE idCarta = ?";

	private static final String UPDATE_ESTADO_VENDIDO = "UPDATE albumbbdd SET estado = 'Vendido' WHERE idCarta = ?";

	private static final String UPDATE_ESTADO_VENTA = "UPDATE albumbbdd SET estado = 'En venta' WHERE idCarta = ?";

	/**
	 * Realiza acciones específicas en la base de datos para un comic según la
	 * operación indicada.
	 *
	 * @param id        El ID del comic a modificar.
	 * @param operacion La operación a realizar: "Vender", "En venta" o "Eliminar".
	 * @throws SQLException Si ocurre un error en la consulta SQL.
	 */
	public static void actualizarCartaBBDD(Carta carta, String operacion) {
		String sentenciaSQL = null;

		switch (operacion.toLowerCase()) {
		case "vender":
			sentenciaSQL = UPDATE_ESTADO_VENDIDO;
			break;
		case "en venta":
			sentenciaSQL = UPDATE_ESTADO_VENTA;
			break;
		case "modificar":
			sentenciaSQL = UPDATE_CARTA;
			break;
		default:
			// Manejar un caso no válido si es necesario
			throw new IllegalArgumentException("Operación no válida: " + operacion);
		}

		modificarComic(carta, sentenciaSQL);
	}

	// Método que fusiona las dos funciones originales
	public static void modificarComic(Carta datos, String sentenciaSQL) {
		if (SelectManager.comprobarIdentificadorCarta(datos.getIdCarta())) {
			try (Connection conn = ConectManager.conexion();
					PreparedStatement ps = conn.prepareStatement(sentenciaSQL)) {
				DBUtilidades.setParameters(ps, datos, true); // Configurar los parámetros de la consulta

				if (ps.executeUpdate() == 1) {
					ListasCartasDAO.listaCartas.clear();
					ListasCartasDAO.listaCartas.add(datos);
				}
			} catch (SQLException ex) {
				Utilidades.manejarExcepcion(ex);
			}
		}
	}

}
