package dbmanager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import cartaManagement.Carta;
import ficherosFunciones.FuncionesFicheros;
import funcionesAuxiliares.Utilidades;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Esta clase sirve para realizar diferentes operaciones que tengan que ver con
 * la base de datos.
 *
 * @author Alejandro Rodriguez
 */
public class ListasCartasDAO {

	/**
	 * Obtenemos el directorio de inicio del usuario
	 */
	private static final String USER_DIR = System.getProperty("user.home");

	/**
	 * Construimos la ruta al directorio "Documents"
	 */
	private static final String DOCUMENTS_PATH = USER_DIR + File.separator + "Documents";

	/**
	 * Construimos la ruta al directorio "libreria_comics" dentro de "Documents" y
	 * añadimos el nombre de la base de datos y la carpeta "portadas".
	 */
	private static final String SOURCE_PATH = DOCUMENTS_PATH + File.separator + "album_cartas" + File.separator
			+ Utilidades.nombreDB() + File.separator + "portadas" + File.separator;

	public static List<Carta> listaTemporalCartas = new ArrayList<>();

	/**
	 * Lista de cómics.
	 */
	public static List<Carta> listaCartas = new ArrayList<>();

	/**
	 * Lista de cómics con verificación.
	 */
	public static List<Carta> listaCartasCheck = new ArrayList<>();

	public static List<String> listaID = new ArrayList<>();

	/**
	 * Lista de nombres.
	 */
	public static List<String> listaNombre = new ArrayList<>();

	/**
	 * Lista de números de cómic.
	 */
	public static List<String> listaNumeroCarta = new ArrayList<>();

	/**
	 * Lista de formatos.
	 */
	public static List<String> listaRareza = new ArrayList<>();

	/**
	 * Lista de editoriales.
	 */
	public static List<String> listaEditorial = new ArrayList<>();

	/**
	 * Lista de procedencias.
	 */
	public static List<String> listaColeccion = new ArrayList<>();

	/**
	 * Lista de nombres de cómics.
	 */
	public static List<String> nombreCartaList = new ArrayList<>();

	/**
	 * Lista de números de cómic.
	 */
	public static List<String> numeroCartaList = new ArrayList<>();

	public static List<String> nombrePrecioNormalList = new ArrayList<>();

	public static List<String> nombrePrecioFoilList = new ArrayList<>();

	/**
	 * Lista de nombres de procedencia.
	 */
	public static List<String> nombreColeccionList = new ArrayList<>();

	/**
	 * Lista de nombres de formato.
	 */
	public static List<String> nombreRarezaList = new ArrayList<>();

	/**
	 * Lista de nombres de editorial.
	 */
	public static List<String> nombreEditorialList = new ArrayList<>();

	/**
	 * Lista de nombres de dibujantes.
	 */
	public static List<String> listaImagenes = new ArrayList<>();

	public static List<String> listaReferencia = new ArrayList<>();

	public static List<String> listaPreciosNormal = new ArrayList<>();

	public static List<String> listaPreciosFoil = new ArrayList<>();

	/**
	 * Lista de cómics limpios.
	 */
	public static List<Carta> listaLimpia = new ArrayList<>();

	/**
	 * Lista de sugerencias de autocompletado de entrada limpia.
	 */
	public static List<String> listaLimpiaAutoCompletado = new ArrayList<>();

	public static ObservableList<Carta> cartasImportados = FXCollections.observableArrayList();
	/**
	 * Lista ordenada que contiene todas las listas anteriores.
	 */
	public static List<List<String>> listaOrdenada = Arrays.asList(nombreCartaList, numeroCartaList,
			nombreEditorialList, nombreColeccionList, nombreRarezaList, nombrePrecioNormalList, nombrePrecioFoilList);

	/**
	 * Lista de listas de elementos.
	 */
	public static List<List<String>> itemsList = Arrays.asList(listaNombre, listaNumeroCarta, listaEditorial,
			listaColeccion, listaRareza, nombrePrecioNormalList, nombrePrecioFoilList);

	public static void eliminarUltimaCartaImportada() {
		ObservableList<Carta> cartasImportados = ListasCartasDAO.cartasImportados;
		if (!cartasImportados.isEmpty()) {
			cartasImportados.remove(cartasImportados.size() - 1);
		}
	}

	public static boolean verificarIDExistente(String id) {
		// Verificar que el id no sea nulo ni esté vacío
		if (id == null || id.isEmpty()) {
			return false;
		}

		// Buscar en comicsImportados
		for (Carta carta : cartasImportados) {
			if (id.equalsIgnoreCase(carta.getIdCarta())) {
				return true; // Si encuentra un comic con el mismo id, devuelve true
			}
		}

		return false; // Si no encuentra ningún comic con el mismo id, devuelve false
	}

	public static Carta devolverCartaLista(String id) {
		for (Carta carta : cartasImportados) {

			if (carta.getIdCarta().equalsIgnoreCase(id)) {
				return carta;
			}
		}
		return null;
	}

	/**
	 * Realiza llamadas para inicializar listas de autocompletado.
	 *
	 * @throws SQLException si ocurre un error al acceder a la base de datos.
	 */
	public static void listasAutoCompletado() {
		listaID = DBUtilidades.obtenerValoresColumna("idCarta");
		listaNombre = DBUtilidades.obtenerValoresColumna("nomCarta");
		listaNumeroCarta = DBUtilidades.obtenerValoresColumna("numCarta");
		listaEditorial = DBUtilidades.obtenerValoresColumna("editorialCarta");
		listaColeccion = DBUtilidades.obtenerValoresColumna("coleccionCarta");
		listaRareza = DBUtilidades.obtenerValoresColumna("rarezaCarta");
		listaReferencia = DBUtilidades.obtenerValoresColumna("urlReferenciaCarta");
		listaPreciosNormal = DBUtilidades.obtenerValoresColumna("precioCartaNormal");
		listaPreciosFoil = DBUtilidades.obtenerValoresColumna("precioCartaFoil");

		listaID = ordenarLista(listaID);

		// Ordenar listaNumeroCarta como enteros
		List<Integer> numerosCarta = listaNumeroCarta.stream().map(Integer::parseInt).collect(Collectors.toList());
		Collections.sort(numerosCarta);
		listaNumeroCarta = numerosCarta.stream().map(String::valueOf).collect(Collectors.toList());

		itemsList = Arrays.asList(listaNombre, listaNumeroCarta, listaEditorial, listaColeccion, listaRareza,
				listaPreciosNormal, listaPreciosFoil);

	}

	public static void actualizarDatosAutoCompletado(String sentenciaSQL) {
		List<List<String>> listaOrdenada = new ArrayList<>(); // Cambia el tipo aquí
		try (Connection conn = ConectManager.conexion();
				PreparedStatement stmt = conn.prepareStatement(sentenciaSQL, ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				List<String> nombreCartaSet = new ArrayList<>(); // Cambia el tipo aquí
				List<Integer> numeroCartaSet = new ArrayList<>(); // Cambia el tipo aquí
				List<String> nombreEditorialSet = new ArrayList<>(); // Cambia el tipo aquí
				List<String> nombreColeccionSet = new ArrayList<>(); // Cambia el tipo aquí
				List<String> rarezaCartaSet = new ArrayList<>(); // Cambia el tipo aquí

				do {
					String nomCarta = rs.getString("nomCarta").trim();
					nombreCartaSet.add(nomCarta);

					String numCarta = rs.getString("numCarta"); // Convertir a entero
					numeroCartaSet.add(Integer.parseInt(numCarta));

					String nomEditorial = rs.getString("editorialCarta").trim();
					nombreEditorialSet.add(nomEditorial);

					String nomColeccion = rs.getString("coleccionCarta").trim();
					nombreColeccionSet.add(nomColeccion);

					String rarezaCarta = rs.getString("rarezaCarta").trim();
					rarezaCartaSet.add(rarezaCarta);

				} while (rs.next());

				procesarDatosAutocompletado(nombreColeccionSet);
				procesarDatosAutocompletado(nombreEditorialSet);

				// Eliminar elementos repetidos
				nombreCartaSet = listaArregladaAutoComplete(nombreCartaSet);
				numeroCartaSet = listaArregladaAutoComplete(numeroCartaSet);
				nombreColeccionSet = listaArregladaAutoComplete(nombreColeccionSet);
				rarezaCartaSet = listaArregladaAutoComplete(rarezaCartaSet);
				nombreEditorialSet = listaArregladaAutoComplete(nombreEditorialSet);

				Collections.sort(numeroCartaSet, Comparable::compareTo);

				listaOrdenada.add(nombreCartaSet);
				listaOrdenada.add(numeroCartaSet.stream().map(String::valueOf).toList());
				listaOrdenada.add(nombreEditorialSet);
				listaOrdenada.add(nombreColeccionSet);
				listaOrdenada.add(rarezaCartaSet);

				ListasCartasDAO.listaOrdenada = listaOrdenada;
			}
		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}
	}

	public static List<String> ordenarLista(List<String> listaStrings) {
		// Creamos una lista para almacenar los enteros
		List<Integer> listaEnteros = new ArrayList<>();

		// Convertimos los strings a enteros y los almacenamos en la lista de enteros
		for (String str : listaStrings) {
			listaEnteros.add(Integer.parseInt(str));
		}

		// Ordenamos la lista de enteros de menor a mayor
		Collections.sort(listaEnteros);

		// Creamos una nueva lista para almacenar los strings ordenados
		List<String> listaOrdenada = new ArrayList<>();

		// Convertimos los enteros ordenados a strings y los almacenamos en la lista
		// ordenada
		for (int num : listaEnteros) {
			listaOrdenada.add(String.valueOf(num));
		}

		// Devolvemos la lista ordenada de strings
		return listaOrdenada;
	}

	public static void procesarDatosAutocompletado(List<String> lista) {
		List<String> nombresProcesados = new ArrayList<>();
		for (String cadena : lista) {
			String[] nombres = cadena.split("-");
			for (String nombre : nombres) {
				nombre = nombre.trim();
				if (!nombre.isEmpty()) {
					nombresProcesados.add(nombre);
				}
			}
		}
		lista.clear(); // Limpiar la lista original
		lista.addAll(nombresProcesados); // Agregar los nombres procesados a la lista original
	}

	/**
	 * Limpia todas las listas utilizadas para autocompletado.
	 */
	public static void limpiarListas() {
		nombreCartaList.clear();
		numeroCartaList.clear();
		nombreColeccionList.clear();
		nombreRarezaList.clear();
		nombreEditorialList.clear();
		nombrePrecioNormalList.clear();
		nombrePrecioFoilList.clear();
	}

	/**
	 * Limpia todas las listas principales utilizadas en el contexto actual. Esto
	 * incluye listas de nombres, números de cómic, variantes, firmas, editoriales,
	 * guionistas, dibujantes, fechas, formatos, procedencias y cajas.
	 */
	public static void limpiarListasPrincipales() {
		listaNombre.clear();
		listaNumeroCarta.clear();
		listaEditorial.clear();
		listaRareza.clear();
		listaColeccion.clear();
	}

	/**
	 * Permite reiniciar la lista de cómics.
	 */
	public static void reiniciarListaCartas() {
		listaCartas.clear(); // Limpia la lista de cómics
	}

	public static void reiniciarListas() {
		listaCartas.clear();
		listaCartasCheck.clear();
		listaID.clear();
		listaNombre.clear();
		listaNumeroCarta.clear();
		listaRareza.clear();
		listaEditorial.clear();
		listaColeccion.clear();
		nombreCartaList.clear();
		numeroCartaList.clear();
		nombrePrecioNormalList.clear();
		nombrePrecioFoilList.clear();
		nombreColeccionList.clear();
		nombreRarezaList.clear();
		nombreEditorialList.clear();
		listaReferencia.clear();
		listaImagenes.clear();
		listaLimpia.clear();
		listaLimpiaAutoCompletado.clear();
		cartasImportados.clear();
	}

	/**
	 * Comprueba si la lista de cómics contiene o no algún dato.
	 *
	 * @param listaCarta Lista de cómics a comprobar.
	 * @return true si la lista está vacía, false si contiene elementos.
	 */
	public boolean checkList(List<Carta> listaCarta) {
		if (listaCarta.isEmpty()) {
			return true; // La lista está vacía
		}
		return false; // La lista contiene elementos
	}

	/**
	 * Ordena un HashMap en orden ascendente por valor (valor de tipo Integer).
	 *
	 * @param map El HashMap a ordenar.
	 * @return Una lista de entradas ordenadas por valor ascendente.
	 */
	public static List<Map.Entry<Integer, Integer>> sortByValueInt(Map<Integer, Integer> map) {
		List<Map.Entry<Integer, Integer>> list = new LinkedList<>(map.entrySet());
		list.sort(Map.Entry.comparingByValue());
		return list;
	}

	/**
	 * Función que guarda los datos para autocompletado en una lista.
	 * 
	 * @param sentenciaSQL La sentencia SQL para obtener los datos.
	 * @param columna      El nombre de la columna que contiene los datos para
	 *                     autocompletado.
	 * @return Una lista de cadenas con los datos para autocompletado.
	 * @throws SQLException Si ocurre algún error al ejecutar la consulta SQL.
	 */
	public static List<String> guardarDatosAutoCompletado(String sentenciaSQL, String columna) {
		List<String> listaAutoCompletado = new ArrayList<>();
		try (Connection conn = ConectManager.conexion();
				PreparedStatement stmt = conn.prepareStatement(sentenciaSQL);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				String datosAutocompletado = rs.getString(columna);
				if (columna.equals("nomCarta")) {
					listaAutoCompletado.add(datosAutocompletado.trim());
				} else if (columna.equals("direccionImagenCarta")) {
					listaAutoCompletado.add(SOURCE_PATH + Utilidades.obtenerUltimoSegmentoRuta(datosAutocompletado));
				} else {
					String[] nombres = datosAutocompletado.split("-");
					for (String nombre : nombres) {
						nombre = nombre.trim();
						if (!nombre.isEmpty()) {
							listaAutoCompletado.add(nombre);
						}
					}
				}
			}

			if (columna.contains("precio")) {
				return listaAutoCompletado;
			}

			return listaArregladaAutoComplete(listaAutoCompletado);

		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e); // Manejar la excepción adecuadamente, por ejemplo, registrándola o
											// notificándola
			// En caso de excepción, podrías decidir retornar una lista parcial o marcar que
			// la operación fue interrumpida
			return listaAutoCompletado; // Retorna lo que se haya procesado hasta el momento
		}
	}

	/**
	 * Funcion que devuelve una lista en la que solamente se guardan aquellos datos
	 * que no se repiten
	 *
	 * @param listaCartas
	 * @return
	 */
	public static List<Carta> listaArreglada(List<Carta> listaCartas) {

		// Forma número 1 (Uso de Maps).
		Map<String, Carta> mapCartas = new HashMap<>(listaCartas.size());

		// Aquí está la magia
		for (Carta c : listaCartas) {
			mapCartas.put(c.getIdCarta(), c);
		}

		// Agrego cada elemento del map a una nueva lista y muestro cada elemento.

		for (Entry<String, Carta> c : mapCartas.entrySet()) {

			listaLimpia.add(c.getValue());

		}
		return listaLimpia;
	}

	/**
	 * Funcion que devuelve una lista en la que solamente se guardan aquellos datos
	 * que no se repiten
	 *
	 * @param listaCartas
	 * @return
	 */
	public static <T extends Comparable<? super T>> List<T> listaArregladaAutoComplete(List<T> listaCartas) {
		Set<T> uniqueSet = new HashSet<>();
		List<T> result = new ArrayList<>();

		for (T item : listaCartas) {
			if (uniqueSet.add(item)) {
				result.add(item);
			}
		}

		// Ordenar la lista resultante de forma ascendente
		Collections.sort(result);

		return result;
	}

	/**
	 * Busca un cómic por su ID en una lista de cómics.
	 *
	 * @param comics  La lista de cómics en la que se realizará la búsqueda.
	 * @param idCarta La ID del cómic que se está buscando.
	 * @return El cómic encontrado por la ID, o null si no se encuentra ninguno.
	 */
	public static Carta buscarCartaPorID(List<Carta> cartas, String idCarta) {
		for (Carta c : cartas) {
			if (c.getIdCarta().equals(idCarta)) {
				return c; // Devuelve el cómic si encuentra la coincidencia por ID
			}
		}
		return null; // Retorna null si no se encuentra ningún cómic con la ID especificada
	}

	/**
	 * Busca un cómic por su ID en una lista de cómics.
	 *
	 * @param comics  La lista de cómics en la que se realizará la búsqueda.
	 * @param idCarta La ID del cómic que se está buscando.
	 * @return El cómic encontrado por la ID, o null si no se encuentra ninguno.
	 */
	public static boolean verificarCartaPorID(List<Carta> cartas, String idCarta) {
		for (Carta c : cartas) {
			if (c.getIdCarta().equals(idCarta)) {
				return true; // Devuelve el cómic si encuentra la coincidencia por ID
			}
		}
		return false; // Retorna null si no se encuentra ningún cómic con la ID especificada
	}

	public static boolean comprobarLista() {
		if (listaID.isEmpty()) {
			return false;
		}
		return true;
	}

	public static void mostrarTamanioListas() {
		System.out.println("Tamaño de listaCartas: " + listaCartas.size());
		System.out.println("Tamaño de listaCartasCheck: " + listaCartasCheck.size());
		System.out.println("Tamaño de listaID: " + listaID.size());
		System.out.println("Tamaño de listaNombre: " + listaNombre.size());
		System.out.println("Tamaño de listaNumeroCarta: " + listaNumeroCarta.size());
		System.out.println("Tamaño de listaRareza: " + listaRareza.size());
		System.out.println("Tamaño de listaEditorial: " + listaEditorial.size());
		System.out.println("Tamaño de listaColeccion: " + listaColeccion.size());
		System.out.println("Tamaño de nombreCartaList: " + nombreCartaList.size());
		System.out.println("Tamaño de numeroCartaList: " + numeroCartaList.size());
		System.out.println("Tamaño de nombrePrecioListNormal: " + nombrePrecioNormalList.size());
		System.out.println("Tamaño de nombrePrecioListFoil: " + nombrePrecioFoilList.size());
		System.out.println("Tamaño de nombreColeccionList: " + nombreColeccionList.size());
		System.out.println("Tamaño de nombreRarezaList: " + nombreRarezaList.size());
		System.out.println("Tamaño de nombreEditorialList: " + nombreEditorialList.size());
		System.out.println("Tamaño de listaImagenes: " + listaImagenes.size());
		System.out.println("Tamaño de listaLimpia: " + listaLimpia.size());
		System.out.println("Tamaño de listaLimpiaAutoCompletado: " + listaLimpiaAutoCompletado.size());
		System.out.println("Tamaño de comicsImportados: " + cartasImportados.size());
	}

	/**
	 * Genera un archivo de estadísticas basado en los datos de la base de datos de
	 * cómics. Los datos se organizan en diferentes categorías y se cuentan para su
	 * análisis. Luego, se genera un archivo de texto con las estadísticas y se abre
	 * con el programa asociado.
	 */
	public static void generar_fichero_estadisticas() {
		// Crear HashMaps para almacenar los datos de cada campo sin repetición y sus
		// conteos
		Map<String, Integer> nomCartaEstadistica = new HashMap<>();
		Map<String, Integer> nivelGradeoEstadistica = new HashMap<>();
		Map<String, Integer> nomEditorialEstadistica = new HashMap<>();
		Map<String, Double> precioCartaEstadistica = new HashMap<>();
		Map<String, Integer> coleccionCartaEstadistica = new HashMap<>();
		Map<String, Integer> rarezaCartaEstadistica = new HashMap<>();
		Map<String, Integer> normasCartaEstadistica = new HashMap<>();

		final String CONSULTA_SQL = "SELECT * FROM albumbbdd";

		try (Connection conn = ConectManager.conexion();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(CONSULTA_SQL)) {

			// Procesar los datos y generar la estadística
			while (rs.next()) {
				// Obtener los datos de cada campo
				String nomCarta = rs.getString("nomCarta");
				String numCarta = rs.getString("numCarta");
				String nivelGradeo = rs.getString("gradeoCarta");
				String nomEditorial = rs.getString("editorialCarta");
				String coleccion = rs.getString("coleccionCarta");
				String precioCartaStr = rs.getString("precioCarta");
				String rarezaCarta = rs.getString("rarezaCarta");
				String esFoilCarta = rs.getString("esFoilCarta");
				String estadoCarta = rs.getString("estadoCarta");
				String normasCarta = rs.getString("normasCarta");

				// Actualizar los HashMaps para cada campo
				nomCartaEstadistica.put(nomCarta, nomCartaEstadistica.getOrDefault(nomCarta, 0) + 1);
				nivelGradeoEstadistica.put(nivelGradeo, nivelGradeoEstadistica.getOrDefault(nivelGradeo, 0) + 1);
				nomEditorialEstadistica.put(nomEditorial, nomEditorialEstadistica.getOrDefault(nomEditorial, 0) + 1);
				coleccionCartaEstadistica.put(coleccion, coleccionCartaEstadistica.getOrDefault(coleccion, 0) + 1);
				rarezaCartaEstadistica.put(rarezaCarta, rarezaCartaEstadistica.getOrDefault(rarezaCarta, 0) + 1);
				normasCartaEstadistica.put(normasCarta, normasCartaEstadistica.getOrDefault(normasCarta, 0) + 1);

				// Convertir precioCarta de String a Double y añadir a la estadística de precios
				try {
					double precioCarta = Double.parseDouble(precioCartaStr);
					if (precioCarta > 0) {
						precioCartaEstadistica.put(nomCarta, precioCarta);
					}
				} catch (NumberFormatException e) {
					// Manejar excepción si no se puede convertir a double
					System.err.println("No se pudo convertir el precio de la carta a número: " + precioCartaStr);
				}
			}

		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}

		// Generar la cadena de estadística
		StringBuilder estadisticaStr = new StringBuilder();
		String lineaDecorativa = "\n--------------------------------------------------------\n";
		String fechaActual = Utilidades.obtenerFechaActual();
		String datosFichero = FuncionesFicheros.datosEnvioFichero();
		// Encabezado
		estadisticaStr.append(
				"Estadisticas de cartas de la base de datos: " + datosFichero + ", a fecha de: " + fechaActual + "\n");

		// Generar estadísticas para cada tipo de dato
		generarEstadistica(estadisticaStr, "Estadística de nombres de cartas:\n", nomCartaEstadistica);
		generarEstadistica(estadisticaStr, "Estadística de niveles de gradeo:\n", nivelGradeoEstadistica);
		generarEstadistica(estadisticaStr, "Estadística de editoriales:\n", nomEditorialEstadistica);
		generarEstadistica(estadisticaStr, "Estadística de colecciones:\n", coleccionCartaEstadistica);
		generarEstadistica(estadisticaStr, "Estadística de rarezas:\n", rarezaCartaEstadistica);
		generarEstadistica(estadisticaStr, "Estadística de normas:\n", normasCartaEstadistica);

		// Agregar estadística de precios
		estadisticaStr.append(lineaDecorativa);
		estadisticaStr.append("\nEstadística de precios de cartas:\n");
		estadisticaStr.append(lineaDecorativa);
		for (Map.Entry<String, Double> entry : precioCartaEstadistica.entrySet()) {
			estadisticaStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		estadisticaStr.append(lineaDecorativa);

		// Crear el archivo de estadística y escribir los datos en él
		String nombreArchivo = "estadistica_" + fechaActual + ".txt";
		String rutaCompleta = obtenerRutaArchivo(nombreArchivo);

		try (PrintWriter writer = new PrintWriter(new FileWriter(rutaCompleta))) {
			writer.print(estadisticaStr);

			// Abrir el archivo con el programa asociado en el sistema
			Utilidades.abrirArchivo(rutaCompleta);

		} catch (IOException e) {
			Utilidades.manejarExcepcion(e);
		}
	}

	// Método para generar estadística de un HashMap específico
	private static void generarEstadistica(StringBuilder sb, String titulo, Map<String, Integer> estadistica) {
		sb.append("\n--------------------------------------------------------\n");
		sb.append(titulo);
		sb.append("\n--------------------------------------------------------\n");

		List<Map.Entry<String, Integer>> listaOrdenada = sortByValue(estadistica);
		for (Map.Entry<String, Integer> entry : listaOrdenada) {
			sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}
	}

	// Método para obtener la ruta completa del archivo de estadísticas
	private static String obtenerRutaArchivo(String nombreArchivo) {
		String userHome = System.getProperty("user.home");
		String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
		String carpetaLibreria = ubicacion + File.separator + "album";
		return carpetaLibreria + File.separator + nombreArchivo;
	}

	// Método para ordenar un Map por los valores (conteos en este caso)
	private static List<Map.Entry<String, Integer>> sortByValue(Map<String, Integer> estadistica) {
		List<Map.Entry<String, Integer>> lista = new LinkedList<>(estadistica.entrySet());
		lista.sort(Map.Entry.comparingByValue());
		Collections.reverse(lista); // Para orden descendente
		return lista;
	}

}