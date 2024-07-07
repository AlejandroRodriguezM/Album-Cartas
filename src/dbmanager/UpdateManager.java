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
		case "modificar":
			sentenciaSQL = UPDATE_CARTA;
			break;
		case "portada":
			sentenciaSQL = UPDATE_CARTA;
			break;
		default:
			// Manejar un caso no válido si es necesario
			throw new IllegalArgumentException("Operación no válida: " + operacion);
		}
		modificarComic(carta, sentenciaSQL);
	}

	public static void modificarComic(Carta datos, String sentenciaSQL) {
	    // Comprobar si el identificador de la carta es válido		
	    if (SelectManager.comprobarIdentificadorCarta(datos.getIdCarta())) {
	        // Usar try-with-resources para asegurar el cierre de recursos
	        try (Connection conn = ConectManager.conexion();
	             PreparedStatement ps = conn.prepareStatement(sentenciaSQL)) {
	             
	            // Configurar los parámetros de la consulta
	            DBUtilidades.setParameters(ps, datos, true);
	            
	            // Ejecutar la actualización
	            if (ps.executeUpdate() == 1) {
	                // Limpiar y actualizar la lista de cartas
	                ListasCartasDAO.listaCartas.clear();
	                ListasCartasDAO.listaCartas.add(datos);
	            } else {
	                System.out.println("No se actualizó ningún registro. Puede que el idCarta no exista.");
	            }
	        } catch (SQLException ex) {
	            Utilidades.manejarExcepcion(ex);
	        }
	    } else {
	        System.out.println("El identificador de la carta no es válido.");
	    }
	}


}
