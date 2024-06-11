package dbmanager;

import java.io.IOException;
import java.sql.SQLException;

import cartaManagement.Carta;
import cartaManagement.Comic;

public class InsertManager {

	public static final String INSERT_SENTENCIA = "INSERT INTO albumbbdd ("
			+ "nomComic, nivel_gradeo, precio_comic, codigo_comic, numComic, nomVariante, firma, nomEditorial, "
			+ "formato, procedencia, fecha_publicacion, nomGuionista, nomDibujante, puntuacion, portada, "
			+ "key_issue, url_referencia, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	/**
	 * Inserta los datos de un cómic en la base de datos.
	 *
	 * @param comicDatos los datos del cómic a insertar
	 * @throws IOException  si ocurre un error al manejar el archivo de imagen
	 * @throws SQLException si ocurre un error al ejecutar la consulta SQL
	 */
	public static void insertarDatos(Carta datos, boolean esImportar) {

		DatabaseManagerDAO.subirCarta(datos, esImportar);
	}

}