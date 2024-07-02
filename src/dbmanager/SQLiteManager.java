package dbmanager;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteManager {

	private static final String DB_FOLDER = System.getProperty("user.home") + File.separator + "AppData"
			+ File.separator + "Roaming" + File.separator + "album" + File.separator;

	/**
	 * Comprueba si existe una base de datos con el nombre especificado para la
	 * creación.
	 *
	 * @return true si la base de datos no existe, false si ya existe o si hay un
	 *         error en la conexión
	 */
	public static boolean checkDatabaseExists(String nombreDataBase) {
		File file = new File(DB_FOLDER + nombreDataBase);
		return !file.exists();
	}

	/**
	 * Crea las tablas de la base de datos si no existen.
	 */
	public static void createTable(String dbName) {

		String url = "jdbc:sqlite:" + DB_FOLDER + dbName + ".db";

		try (Connection connection = DriverManager.getConnection(url);
				Statement statement = connection.createStatement()) {

			String createTableSQL = "CREATE TABLE IF NOT EXISTS albumbbdd ("
					+ "idCarta INTEGER PRIMARY KEY AUTOINCREMENT, " + "nomCarta TEXT NOT NULL, "
					+ "numCarta TEXT NOT NULL, " + "editorialCarta TEXT NOT NULL, " + "coleccionCarta TEXT NOT NULL, "
					+ "rarezaCarta TEXT NOT NULL, " + "precioCartaFoil TEXT NOT NULL, "
					+ "precioCartaNormal TEXT NOT NULL, " + "urlReferenciaCarta TEXT NOT NULL, "
					+ "direccionImagenCarta TEXT NOT NULL, " + "normasCarta TEXT NOT NULL)";

			statement.executeUpdate(createTableSQL);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}