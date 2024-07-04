package dbmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cartaManagement.Carta;
import funcionesAuxiliares.Utilidades;

public class SelectManager {

	public static final String TAMANIO_DATABASE = "SELECT COUNT(*) FROM albumbbdd;";
	private static final String SENTENCIA_BUSQUEDA_INDIVIDUAL = "SELECT * FROM albumbbdd WHERE idCarta = ?;";
	private static final String SENTENCIA_CONTAR_CARTAS_POR_ID = "SELECT 1 FROM albumbbdd WHERE idCarta = ? LIMIT 1;";
	private static final String SENTENCIA_BUSCAR_PORTADA = "SELECT portada FROM albumbbdd WHERE idCarta = ?;";
	public static final String SENTENCIA_BUSQUEDA_COMPLETA = "SELECT * FROM albumbbdd";
	public static final String SENTENCIA_TOTAL_BUSQUEDA = "SELECT COUNT(*) FROM albumbbdd WHERE 1=1;";

	public static final String SENTENCIA_COMPLETA = "SELECT * FROM albumbbdd ORDER BY nomCarta, numCarta;";

	/**
	 * Funcion que permite contar cuantas filas hay en la base de datos.
	 *
	 * @return
	 */
	public static int countRows() {

		try (Connection conn = ConectManager.conexion();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(TAMANIO_DATABASE)) {

			if (rs.next()) {
				return rs.getInt(1);
			}

		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}
		return -1;
	}

	public static int getCount(String sql) {

		int count = 0;
		try (Connection conn = ConectManager.conexion();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * Devuelve un objeto Comic cuya ID coincida con el parámetro de búsqueda.
	 *
	 * @param identificador el ID del cómic a buscar
	 * @return el objeto Comic encontrado, o null si no se encontró ningún cómic con
	 *         ese ID
	 * @throws SQLException si ocurre algún error al ejecutar la consulta SQL
	 */
	public static Carta cartaDatos(String identificador) {

		Carta carta = null;

		try (Connection conn = ConectManager.conexion();
				PreparedStatement statement = conn.prepareStatement(SENTENCIA_BUSQUEDA_INDIVIDUAL)) {
			statement.setString(1, identificador);

			try (ResultSet rs = statement.executeQuery()) {
				if (rs.next()) {
					carta = DBUtilidades.obtenerCartaDesdeResultSet(rs);
				}
			} catch (SQLException e) {
				Utilidades.manejarExcepcion(e);
			}
		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}

		return carta;
	}

    /**
     * Comprueba si un identificador de carta existe en la base de datos.
     *
     * @param identificador El identificador de la carta a comprobar.
     * @return true si el identificador existe, false en caso contrario.
     */
    public static boolean comprobarIdentificadorCarta(String identificador) {
    	
        if (identificador == null || identificador.trim().isEmpty()) {
            return false; // Si el identificador es nulo o está vacío, se considera que no existe
        }

		try (Connection conn = ConectManager.conexion();
				PreparedStatement preparedStatement = conn.prepareStatement(SENTENCIA_CONTAR_CARTAS_POR_ID)) {

            preparedStatement.setString(1, identificador.trim());

            try (ResultSet rs = preparedStatement.executeQuery()) {
            	            	
                return rs.next(); // Si hay una fila, el identificador existe
            }
        } catch (SQLException e) {
            Utilidades.manejarExcepcion(e);
            // Manejar error de sintaxis SQL de manera específica
        } catch (Exception e) {
            Utilidades.manejarExcepcion(e);
            // Manejar otros errores genéricos
        }

        return false;
    }

	/**
	 * Obtiene la dirección de la portada de un cómic.
	 *
	 * @param idComic ID del cómic
	 * @return Dirección de la portada del cómic
	 * @throws SQLException Si ocurre algún error de SQL
	 */
	public static String obtenerDireccionPortada(String idComic) {

		try (Connection conn = ConectManager.conexion();
				PreparedStatement ps = conn.prepareStatement(SENTENCIA_BUSCAR_PORTADA)) {
			ps.setString(1, idComic);
			try (ResultSet resultado = ps.executeQuery()) {
				if (resultado.next()) {
					String portada = resultado.getString("portada");
					if (portada != null && !portada.isEmpty()) {
						return portada;
					}
				}
			}
		} catch (SQLException ex) {
			Utilidades.manejarExcepcion(ex);
		}

		return null;
	}

	/**
	 * Función que busca en el ArrayList el o los cómics que tengan coincidencia con
	 * los datos introducidos en el TextField.
	 *
	 * @param comic           el cómic con los parámetros de búsqueda
	 * @param busquedaGeneral el texto de búsqueda general
	 * @return una lista de cómics que coinciden con los criterios de búsqueda
	 * @throws SQLException si ocurre un error al acceder a la base de datos
	 */
	public static List<Carta> busquedaParametro(Carta carta, String busquedaGeneral) {

		List<Carta> listComic = new ArrayList<>();
		if (CartaManagerDAO.countRows() > 0) {
			if (!busquedaGeneral.isEmpty()) {

				listComic = SelectManager.libreriaSeleccionado(busquedaGeneral);

				if (listComic.isEmpty() && Carta.validarCarta(carta)) {
					return DBUtilidades.filtroBBDD(carta, busquedaGeneral);
				}

				return listComic;
			} else {
				if (Carta.validarCarta(carta)) {
					return DBUtilidades.filtroBBDD(carta, busquedaGeneral);
				}
			}

		}
		return listComic;
	}

	public static boolean hayDatosEnLibreria(String sentenciaSQL) {
		if (sentenciaSQL.isEmpty()) {
			sentenciaSQL = SENTENCIA_BUSQUEDA_COMPLETA;
		}

		try (Connection conn = ConectManager.conexion();
				PreparedStatement stmt = conn.prepareStatement(sentenciaSQL, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				ResultSet rs = stmt.executeQuery()) {

			// Si hay resultados, devolver true
			return rs.first();

		} catch (SQLException ex) {
			Utilidades.manejarExcepcion(ex);
		}
		return false;
	}

	/**
	 * Devuelve una lista con todos los comics de la base de datos que se encuentran
	 * "En posesion"
	 *
	 * @return una lista de cómics que coinciden con los criterios de búsqueda
	 * @throws SQLException
	 */
	public static List<Carta> libreriaSeleccionado(String datoSeleccionado) {
		String sentenciaSQL = "SELECT * FROM albumbbdd WHERE "
		        + "numCarta = '" + datoSeleccionado + "' OR "
		        + "coleccionCarta LIKE '%" + datoSeleccionado + "%' OR "
		        + "rarezaCarta LIKE '%" + datoSeleccionado + "%' OR "
		        + "esFoilCarta = '" + datoSeleccionado + "' OR "
		        + "editorialCarta = '" + datoSeleccionado + "' OR "
		        + "gradeoCarta LIKE '%" + datoSeleccionado + "%' OR "
		        + "estadoCarta LIKE '%" + datoSeleccionado + "%' OR "
		        + "precioCarta = '" + datoSeleccionado + "' OR "
		        + "urlReferenciaCarta LIKE '%" + datoSeleccionado + "%' OR "
		        + "direccionImagenCarta LIKE '%" + datoSeleccionado + "%' "
		        + "ORDER BY nomCarta ASC, numCarta ASC, esFoilCarta ASC";

		return verLibreria(sentenciaSQL, false);
	}

	/**
	 * Método que muestra los cómics de la librería según la sentencia SQL
	 * proporcionada.
	 * 
	 * @param sentenciaSQL La sentencia SQL para obtener los cómics de la librería.
	 * @return Una lista de objetos Comic que representan los cómics de la librería.
	 */
	public static List<Carta> verLibreria(String sentenciaSQL, boolean esActualizacion) {
		ListasCartasDAO.listaCartas.clear(); // Limpiar la lista existente de cómics
		List<Carta> listaCartas = new ArrayList<>();

		try (Connection conn = ConectManager.conexion();
				PreparedStatement stmt = conn.prepareStatement(sentenciaSQL, ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery()) {

			if (esActualizacion) {
				while (rs.next()) {
					Carta carta = DBUtilidades.obtenerCartaDesdeResultSet(rs);
					if (!carta.getUrlReferenciaCarta().isEmpty()) {
						listaCartas.add(carta);
					}
				}
			} else {
				while (rs.next()) {
					listaCartas.add(DBUtilidades.obtenerCartaDesdeResultSet(rs));
				}
			}

		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}

		return listaCartas;
	}

}
