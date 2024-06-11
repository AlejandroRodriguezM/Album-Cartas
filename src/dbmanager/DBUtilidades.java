package dbmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import Controladores.OpcionesAvanzadasController;
import cartaManagement.Carta;
import cartaManagement.Comic;
import funcionesAuxiliares.Utilidades;
import funcionesAuxiliares.Ventanas;
import funcionesManagment.AccionReferencias;

public class DBUtilidades {

	private static AccionReferencias referenciaVentana = getReferenciaVentana();

	public enum TipoBusqueda {
		POSESION, KEY_ISSUE, COMPLETA, VENDIDOS, COMPRADOS, PUNTUACION, FIRMADOS, GUARDADOS, ELIMINAR
	}

	public static void setParameters(PreparedStatement ps, Carta datos, boolean includeID) throws SQLException {
		ps.setString(1, datos.getNomCarta());
		ps.setInt(2, datos.getNumCarta());
		ps.setString(3, datos.getColeccionCarta());
		ps.setString(4, datos.getRarezaCarta());
		ps.setInt(5, datos.getEsFoilCarta() ? 1 : 0); // Assuming esFoilCarta is a boolean
		ps.setString(6, datos.getGradeoCarta());
		ps.setString(7, datos.getEstadoCarta());
		ps.setDouble(8, datos.getPrecioCarta());
		ps.setString(9, datos.getUrlReferenciaCarta());
		ps.setString(10, datos.getDireccionImagenCarta());

		if (includeID) {
			ps.setInt(11, datos.getIdCarta()); // Assuming getIdCarta() returns an integer
		}
	}

//	############################################
//	###########SELECT FUNCTIONS#################
//	############################################

	public static String construirSentenciaSQL(TipoBusqueda tipoBusqueda) {

		switch (tipoBusqueda) {
		case POSESION:
			return SelectManager.SENTENCIA_POSESION;
		case KEY_ISSUE:
			return SelectManager.SENTENCIA_KEY_ISSUE;
		case COMPLETA:
			return SelectManager.SENTENCIA_COMPLETA;
		case VENDIDOS:
			return SelectManager.SENTENCIA_VENDIDOS;
		case COMPRADOS:
			return SelectManager.SENTENCIA_COMPRADOS;
		case PUNTUACION:
			return SelectManager.SENTENCIA_PUNTUACION;
		case FIRMADOS:
			return SelectManager.SENTENCIA_FIRMADOS;
		default:
			throw new IllegalArgumentException("Tipo de búsqueda no válido");
		}
	}

	public static String datosConcatenados(Carta carta) {
	    String connector = " WHERE ";

	    StringBuilder sql = new StringBuilder(SelectManager.SENTENCIA_BUSQUEDA_COMPLETA);

	    connector = agregarCondicion(sql, connector, "idCarta", carta.getIdCarta());
	    connector = agregarCondicion(sql, connector, "nomCarta", carta.getNomCarta());
	    connector = agregarCondicion(sql, connector, "gradeoCarta", carta.getGradeoCarta());
	    connector = agregarCondicion(sql, connector, "numCarta", Integer.toString(carta.getNumCarta()));
	    connector = agregarCondicionLike(sql, connector, "coleccionCarta", carta.getColeccionCarta());
	    connector = agregarCondicionLike(sql, connector, "rarezaCarta", carta.getRarezaCarta());
	    connector = agregarCondicion(sql, connector, "esFoilCarta", carta.getEsFoilCarta() ? "1" : "0");
	    connector = agregarCondicionLike(sql, connector, "estadoCarta", carta.getEstadoCarta());
	    connector = agregarCondicion(sql, connector, "precioCarta", Double.toString(carta.getPrecioCarta()));

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
		case "nomcomic":
			sql.append(connector).append("nomComic LIKE '%" + busquedaGeneral + "%';");
			break;
		case "nomvariante":
			sql.append(connector).append("nomVariante LIKE '%" + busquedaGeneral + "%';");
			break;
		case "firma":
			sql.append(connector).append("firma LIKE '%" + busquedaGeneral + "%';");
			break;
		case "nomguionista":
			sql.append(connector).append("nomGuionista LIKE '%" + busquedaGeneral + "%';");
			break;
		case "nomdibujante":
			sql.append(connector).append("nomDibujante LIKE '%" + busquedaGeneral + "%';");
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
		String sql3 = datosGenerales("firma", busquedaGeneral);
		String sql4 = datosGenerales("nomguionista", busquedaGeneral);
		String sql5 = datosGenerales("nomdibujante", busquedaGeneral);

		try (Connection conn = ConectManager.conexion();
				PreparedStatement ps1 = conn.prepareStatement(sql1);
				PreparedStatement ps2 = conn.prepareStatement(sql2);
				PreparedStatement ps3 = conn.prepareStatement(sql3);
				PreparedStatement ps4 = conn.prepareStatement(sql4);
				PreparedStatement ps5 = conn.prepareStatement(sql5);
				ResultSet rs1 = ps1.executeQuery();
				ResultSet rs2 = ps2.executeQuery();
				ResultSet rs3 = ps3.executeQuery();
				ResultSet rs4 = ps4.executeQuery();
				ResultSet rs5 = ps5.executeQuery()) {

			ListaComicsDAO.listaCartas.clear();

			agregarSiHayDatos(rs1);
			agregarSiHayDatos(rs2);
			agregarSiHayDatos(rs3);
			agregarSiHayDatos(rs4);
			agregarSiHayDatos(rs5);

			ListaComicsDAO.listaCartas = ListaComicsDAO.listaArreglada(ListaComicsDAO.listaCartas);
			return ListaComicsDAO.listaCartas;

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
		ListaComicsDAO.listaCartas.clear();

		// Crear la consulta SQL a partir de los datos proporcionados
		String sql = datosConcatenados(datos);

		// Verificar si la consulta SQL no está vacía
		if (!sql.isEmpty()) {
			try (Connection conn = ConectManager.conexion();
					PreparedStatement ps = conn.prepareStatement(sql);
					ResultSet rs = ps.executeQuery()) {

				// Llenar la lista de cómics con los resultados obtenidos
				while (rs.next()) {
					ListaComicsDAO.listaCartas.add(obtenerCartaDesdeResultSet(rs));
				}

				return ListaComicsDAO.listaCartas;
			} catch (SQLException ex) {
				// Manejar la excepción según tus necesidades (en este caso, mostrar una alerta)
				Utilidades.manejarExcepcion(ex);
			}
		} else {
			getReferenciaVentana().getProntInfo().setOpacity(1);
			// Show error message in red when no search fields are specified
			getReferenciaVentana().getProntInfo().setStyle("-fx-text-fill: red;");
			getReferenciaVentana().getProntInfo()
					.setText("Error No existe carta con los datos: " + busquedaGeneral + datos.toString());
		}

		if (sql.isEmpty() && busquedaGeneral.isEmpty()) {
			getReferenciaVentana().getProntInfo().setOpacity(1);
			// Show error message in red when no search fields are specified
			getReferenciaVentana().getProntInfo().setStyle("-fx-text-fill: red;");
			getReferenciaVentana().getProntInfo().setText("Todos los campos estan vacios");
		}

		return ListaComicsDAO.listaCartas;
	}

	/**
	 * Devuelve una lista de valores de una columna específica de la base de datos.
	 *
	 * @param columna Nombre de la columna de la base de datos.
	 * @return Lista de valores de la columna.
	 * @throws SQLException Si ocurre un error en la consulta SQL.
	 */
	public static List<String> obtenerValoresColumna(String columna) {
		String sentenciaSQL = "SELECT " + columna + " FROM albumbbdd ORDER BY " + columna + " ASC";
		ListaComicsDAO.listaComics.clear();
		return ListaComicsDAO.guardarDatosAutoCompletado(sentenciaSQL, columna);
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
			int numCarta = rs.getInt("numCarta");
			String coleccionCarta = rs.getString("coleccionCarta");
			String rarezaCarta = rs.getString("rarezaCarta");
			boolean esFoilCarta = rs.getInt("esFoilCarta") == 1;
			String gradeoCarta = rs.getString("gradeoCarta");
			String estadoCarta = rs.getString("estadoCarta");
			double precioCarta = rs.getDouble("precioCarta");
			String urlReferenciaCarta = rs.getString("urlReferenciaCarta");
			String direccionImagenCarta = rs.getString("direccionImagenCarta");

			// Verificaciones y asignaciones predeterminadas
			precioCarta = (precioCarta <= 0) ? 0.0 : precioCarta;

			return new Carta.CartaBuilder(id, nombre).numCarta(numCarta).coleccionCarta(coleccionCarta)
					.rarezaCarta(rarezaCarta).esFoilCarta(esFoilCarta).gradeoCarta(gradeoCarta).estadoCarta(estadoCarta)
					.precioCarta(precioCarta).urlReferenciaCarta(urlReferenciaCarta)
					.direccionImagenCarta(direccionImagenCarta).build();
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