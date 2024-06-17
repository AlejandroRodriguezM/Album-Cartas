package dbmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import cartaManagement.Carta;
import funcionesAuxiliares.Utilidades;

public class DeleteManager {

	private static final String DELETE_SENTENCIA = "DELETE FROM albumbbdd WHERE idCarta = ?";
	private static final String DELETE_SENTENCIA_COMPLETA = "DELETE FROM albumbbdd";
	private static final String SENTENCIA_REBOOT_ID = "DELETE FROM sqlite_sequence WHERE name = 'albumbbdd'";


	/**
	 * Borra y reinicia la base de datos de manera asíncrona.
	 *
	 * @return CompletableFuture que indica si la operación se realizó con éxito o
	 *         no.
	 */
	public static CompletableFuture<Boolean> deleteAndRestartDatabaseAsync() {
		return CompletableFuture.supplyAsync(() -> {
			try {
				ListasCartasDAO.limpiarListasPrincipales();
				CompletableFuture<Boolean> result = CompletableFuture.supplyAsync(() -> {
					try (Connection conn = ConectManager.conexion();
							PreparedStatement deleteStatement = conn.prepareStatement(DELETE_SENTENCIA_COMPLETA);
							PreparedStatement resetAutoIncrementStatement = conn
									.prepareStatement(SENTENCIA_REBOOT_ID)) {

						deleteStatement.executeUpdate();
						resetAutoIncrementStatement.executeUpdate();
						return true;
					} catch (Exception e) {
						throw new RuntimeException(e); // Envuelve excepción en RuntimeException para ser manejada en el
														// nivel superior
					}
				});

				return result.join(); // Espera a que la operación asíncrona se complete y devuelve el resultado
			} catch (Exception e) {
				throw new RuntimeException(e); // Envuelve excepción en RuntimeException para ser manejada en el nivel
												// superior
			}
		}, Executors.newCachedThreadPool()); // Se utiliza un Executor diferente para ejecutar el CompletableFuture en
												// un hilo separado
	}

	/**
	 * Función que comprueba si la opinión ha sido introducida correctamente.
	 *
	 * @param sentenciaSQL La sentencia SQL a ejecutar
	 * @param ID           La ID del cómic
	 * @param puntuacion   La puntuación a insertar
	 * @throws SQLException Si ocurre un error en la base de datos
	 */
	public static void borrarCarta(String idComic) {
		ListasCartasDAO.listaCartas.clear();

		try (Connection conn = ConectManager.conexion();
				PreparedStatement ps = conn.prepareStatement(DELETE_SENTENCIA);) {
			if (SelectManager.comprobarIdentificadorCarta(idComic)) { // Comprueba si la ID introducida existe en la
																		// base de datos
				Carta carta = SelectManager.cartaDatos(idComic);
				ps.setString(1, idComic);
				if (ps.executeUpdate() == 1) { // Si se ha modificado correctamente, se añade el cómic a la lista
					ListasCartasDAO.listaCartas.add(carta);
				}
			}
		} catch (SQLException ex) {
			Utilidades.manejarExcepcion(ex);
		}
	}
}
