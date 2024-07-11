package dbmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import cartaManagement.Carta;
import funcionesAuxiliares.Utilidades;
import funcionesManagment.AccionReferencias;

public class DBUtilidades {

	private static AccionReferencias referenciaVentana = getReferenciaVentana();

	public enum TipoBusqueda {
		COMPLETA, ELIMINAR
	}

	public static void setParameters(PreparedStatement ps, Carta datos, boolean includeID) throws SQLException {
		ps.setString(1, datos.getNomCarta());
		ps.setString(2, datos.getNumCarta());
		ps.setString(3, datos.getEditorialCarta());
		ps.setString(4, datos.getColeccionCarta());
		ps.setString(5, datos.getRarezaCarta());
		ps.setString(6, datos.getPrecioCartaNormal());
		ps.setString(7, datos.getPrecioCartaFoil());
		ps.setString(8, datos.getUrlReferenciaCarta());
		ps.setString(9, datos.getDireccionImagenCarta());
		ps.setString(10, datos.getNormasCarta());

		if (includeID) {
			ps.setString(11, datos.getIdCarta()); // Assuming getIdCarta() returns an integer
		}
	}

//	############################################
//	###########SELECT FUNCTIONS#################
//	############################################

	public static String construirSentenciaSQL(TipoBusqueda tipoBusqueda) {

		switch (tipoBusqueda) {
		case COMPLETA:
			return SelectManager.SENTENCIA_COMPLETA;
		default:
			throw new IllegalArgumentException("Tipo de búsqueda no válido");
		}
	}

	public static String datosConcatenados(Carta carta) {
		String connector = " WHERE ";

		StringBuilder sql = new StringBuilder(SelectManager.SENTENCIA_BUSQUEDA_COMPLETA);

		connector = agregarCondicion(sql, connector, "idCarta", carta.getIdCarta());
		connector = agregarCondicion(sql, connector, "nomCarta", carta.getNomCarta());
		connector = agregarCondicion(sql, connector, "numCarta", carta.getNumCarta());
		connector = agregarCondicion(sql, connector, "editorialCarta", carta.getEditorialCarta());
		connector = agregarCondicionLike(sql, connector, "coleccionCarta", carta.getColeccionCarta());
		connector = agregarCondicionLike(sql, connector, "rarezaCarta", carta.getRarezaCarta());
		if (connector.trim().equalsIgnoreCase("WHERE")) {
			return "";
		}

		return (connector.length() > 0) ? sql.toString() : "";
	}

	public static String agregarCondicion(StringBuilder sql, String connector, String columna, String valor) {
		if (!valor.isEmpty()) {
			sql.append(connector).append(columna).append(" = '").append(valor).append("'");
			return " AND ";
		}
		return connector;
	}

	public static String agregarCondicionLike(StringBuilder sql, String connector, String columna, String valor) {
		if (!valor.isEmpty()) {
			sql.append(connector).append(columna).append(" LIKE '%").append(valor).append("%'");
			return " AND ";
		}
		return connector;
	}

	/**
	 * Función que construye una consulta SQL para buscar un identificador en la
	 * tabla utilizando diferentes criterios de búsqueda.
	 *
	 * @param tipoBusqueda    Tipo de búsqueda (nomComic, nomVariante, firma,
	 *                        nomGuionista, nomDibujante).
	 * @param busquedaGeneral Término de búsqueda.
	 * @return Consulta SQL generada.
	 */
	public static String datosGenerales(String tipoBusqueda, String busquedaGeneral) {
		String connector = " WHERE ";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM albumbbdd");

		switch (tipoBusqueda.toLowerCase()) {
		case "nomCarta":
			sql.append(connector).append("nomCarta LIKE '%" + busquedaGeneral + "%';");
			break;
		case "coleccionCarta":
			sql.append(connector).append("coleccionCarta LIKE '%" + busquedaGeneral + "%';");
			break;
		case "editorialCarta":
			sql.append(connector).append("editorialCarta LIKE '%" + busquedaGeneral + "%';");
			break;
		default:
			// Tipo de búsqueda no válido, puedes manejarlo según tus necesidades
			break;
		}

		return sql.toString();
	}

	/**
	 * Funcion que permite hacer una busqueda general mediante 1 sola palabra, hace
	 * una busqueda en ciertos identificadores de la tabla.
	 *
	 * @param sentencia
	 * @return
	 * @throws SQLException
	 */
	public static List<Carta> verBusquedaGeneral(String busquedaGeneral) {
		String sql1 = datosGenerales("nomCarta", busquedaGeneral);
		String sql2 = datosGenerales("coleccionCarta", busquedaGeneral);
		String sql3 = datosGenerales("editorialCarta", busquedaGeneral);
		try (Connection conn = ConectManager.conexion();
				PreparedStatement ps1 = conn.prepareStatement(sql1);
				PreparedStatement ps2 = conn.prepareStatement(sql2);
				PreparedStatement ps3 = conn.prepareStatement(sql3);

				ResultSet rs1 = ps1.executeQuery();
				ResultSet rs2 = ps2.executeQuery();
				ResultSet rs3 = ps3.executeQuery()) {

			ListasCartasDAO.listaCartas.clear();

			agregarSiHayDatos(rs1);
			agregarSiHayDatos(rs2);
			agregarSiHayDatos(rs3);

			ListasCartasDAO.listaCartas = ListasCartasDAO.listaArreglada(ListasCartasDAO.listaCartas);
			return ListasCartasDAO.listaCartas;

		} catch (SQLException ex) {
			Utilidades.manejarExcepcion(ex);
		}

		return Collections.emptyList();
	}

	/**
	 * Agrega el primer conjunto de resultados a la lista de cómics si hay datos en
	 * el ResultSet.
	 * 
	 * @param rs El ResultSet que contiene los resultados de la consulta.
	 * @throws SQLException Si se produce un error al acceder a los datos del
	 *                      ResultSet.
	 */
	public static void agregarSiHayDatos(ResultSet rs) throws SQLException {
		if (rs.next()) {
			obtenerCartaDesdeResultSet(rs);
		}
	}

	/**
	 * Filtra y devuelve una lista de cómics de la base de datos según los datos
	 * proporcionados.
	 *
	 * @param datos Objeto Comic con los datos para filtrar.
	 * @return Lista de cómics filtrados.
	 */
	public static List<Carta> filtroBBDD(Carta datos, String busquedaGeneral) {

		// Reiniciar la lista de cómics antes de realizar el filtrado
		ListasCartasDAO.listaCartas.clear();

		// Crear la consulta SQL a partir de los datos proporcionados
		String sql = datosConcatenados(datos);

		// Verificar si la consulta SQL no está vacía
		if (!sql.isEmpty()) {
			try (Connection conn = ConectManager.conexion();
					PreparedStatement ps = conn.prepareStatement(sql);
					ResultSet rs = ps.executeQuery()) {

				// Llenar la lista de cómics con los resultados obtenidos
				while (rs.next()) {
					ListasCartasDAO.listaCartas.add(obtenerCartaDesdeResultSet(rs));
				}

				return ListasCartasDAO.listaCartas;
			} catch (SQLException ex) {
				// Manejar la excepción según tus necesidades (en este caso, mostrar una alerta)
				Utilidades.manejarExcepcion(ex);
			}
		} else {
			getReferenciaVentana().getProntInfoTextArea().setOpacity(1);
			// Show error message in red when no search fields are specified
			getReferenciaVentana().getProntInfoTextArea().setStyle("-fx-text-fill: red;");
			getReferenciaVentana().getProntInfoTextArea()
					.setText("Error No existe carta con los datos: " + busquedaGeneral + datos.toString());
		}

		if (sql.isEmpty() && busquedaGeneral.isEmpty()) {
			getReferenciaVentana().getProntInfoTextArea().setOpacity(1);
			// Show error message in red when no search fields are specified
			getReferenciaVentana().getProntInfoTextArea().setStyle("-fx-text-fill: red;");
			getReferenciaVentana().getProntInfoTextArea().setText("Todos los campos estan vacios");
		}

		return ListasCartasDAO.listaCartas;
	}

	/**
	 * Devuelve una lista de valores de una columna específica de la base de datos.
	 *
	 * @param columna Nombre de la columna de la base de datos.
	 * @return Lista de valores de la columna.
	 * @throws SQLException Si ocurre un error en la consulta SQL.
	 */
	public static List<String> obtenerValoresColumna(String columna) {
		String sentenciaSQL = "SELECT " + columna + " FROM albumbbdd ORDER BY " + columna + " ASC;";

		ListasCartasDAO.listaCartas.clear();
		return ListasCartasDAO.guardarDatosAutoCompletado(sentenciaSQL, columna);
	}

	/**
	 * Crea y devuelve un objeto Comic a partir de los datos del ResultSet.
	 * 
	 * @param rs El ResultSet que contiene los datos del cómic.
	 * @return Un objeto Comic con los datos obtenidos del ResultSet.
	 * @throws SQLException Si se produce un error al acceder a los datos del
	 *                      ResultSet.
	 */
	public static Carta obtenerCartaDesdeResultSet(ResultSet rs) {
		try {
			String id = rs.getString("idCarta");
			String nombre = rs.getString("nomCarta");
			String numCarta = rs.getString("numCarta");
			String editorialCarta = rs.getString("editorialCarta");
			String coleccionCarta = rs.getString("coleccionCarta");
			String rarezaCarta = rs.getString("rarezaCarta");
			String precioCartaNormal = rs.getString("precioCartaNormal");
			String precioCartaFoil = rs.getString("precioCartaFoil");
			String urlReferenciaCarta = rs.getString("urlReferenciaCarta");
			String direccionImagenCarta = rs.getString("direccionImagenCarta");
			String normasCarta = rs.getString("normasCarta");

			// Verificaciones y asignaciones predeterminadas
//			precioCartaNormal = (Double.parseDouble(precioCartaNormal) <= 0) ? "0.0" : precioCartaNormal;
//			precioCartaFoil = (Double.parseDouble(precioCartaFoil) <= 0) ? "0.0" : precioCartaFoil;

			return new Carta.CartaBuilder(id, nombre).numCarta(numCarta).editorialCarta(editorialCarta)
					.coleccionCarta(coleccionCarta).rarezaCarta(rarezaCarta).precioCartaNormal(precioCartaNormal)
					.precioCartaFoil(precioCartaFoil).urlReferenciaCarta(urlReferenciaCarta)
					.direccionImagenCarta(direccionImagenCarta).normasCarta(normasCarta).build();
		} catch (SQLException e) {
			// Manejar la excepción según tus necesidades
			Utilidades.manejarExcepcion(e);
			return null; // O lanza una excepción personalizada, según el caso
		}
	}

	public static AccionReferencias getReferenciaVentana() {
		return referenciaVentana;
	}

	public static void setReferenciaVentana(AccionReferencias referenciaVentana) {
		DBUtilidades.referenciaVentana = referenciaVentana;
	}
}
