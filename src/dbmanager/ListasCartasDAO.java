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
	 * Lista de dibujantes.
	 */
	public static List<String> listaEsFoil = new ArrayList<>();

	public static List<String> listaGradeo = new ArrayList<>();

	public static List<String> listaEstado = new ArrayList<>();

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

	/**
	 * Lista de nombres de variantes.
	 */
	public static List<String> nombreGradeoList = new ArrayList<>();

	public static List<String> nombreEstadoList = new ArrayList<>();

	public static List<String> nombrePrecioList = new ArrayList<>();

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
	public static List<String> nombreEsFoilList = new ArrayList<>();

	/**
	 * Lista de nombres de dibujantes.
	 */
	public static List<String> listaImagenes = new ArrayList<>();

	public static List<String> listaPrecios = new ArrayList<>();

	/**
	 * Lista de cómics limpios.
	 */
	public static List<Carta> listaLimpia = new ArrayList<>();

	/**
	 * Lista de sugerencias de autocompletado de entrada limpia.
	 */
	public static List<String> listaLimpiaAutoCompletado = new ArrayList<>();

	public static List<Carta> cartasImportados = new ArrayList<>();

	/**
	 * Lista ordenada que contiene todas las listas anteriores.
	 */
	public static List<List<String>> listaOrdenada = Arrays.asList(nombreCartaList, numeroCartaList,
			nombreEditorialList, nombreColeccionList, nombreRarezaList, nombreEsFoilList, nombreGradeoList,
			nombreEstadoList, nombrePrecioList);

	/**
	 * Lista de listas de elementos.
	 */
	public static List<List<String>> itemsList = Arrays.asList(listaNombre, listaNumeroCarta, listaEditorial,
			listaColeccion, listaRareza, listaEsFoil, nombreEsFoilList, nombreGradeoList, nombreEstadoList,
			nombrePrecioList);

	/**
	 * Lista de comics guardados para poder ser impresos
	 */
	public static ObservableList<Carta> cartasGuardadosList = FXCollections.observableArrayList();

	public static boolean verificarIDExistente(String id, boolean esGuardado) {
		// Verificar que el id no sea nulo ni esté vacío
		if (id == null || id.isEmpty()) {
			return false;
		}

		// Buscar en comicsGuardadosList si es necesario
		if (esGuardado) {
			for (Carta carta : cartasGuardadosList) {
				if (id.equals(carta.getIdCarta())) {
					return true; // Si encuentra un comic con el mismo id, devuelve true
				}
			}
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
		listaEsFoil = DBUtilidades.obtenerValoresColumna("esFoilCarta");
		listaGradeo = DBUtilidades.obtenerValoresColumna("gradeoCarta");
		listaEstado = DBUtilidades.obtenerValoresColumna("estadoCarta");
		listaImagenes = DBUtilidades.obtenerValoresColumna("urlReferenciaCarta");
		listaPrecios = DBUtilidades.obtenerValoresColumna("precioCarta");

		listaID = ordenarLista(listaID);

		// Ordenar listaNumeroCarta como enteros
		List<Integer> numerosCarta = listaNumeroCarta.stream().map(Integer::parseInt).collect(Collectors.toList());
		Collections.sort(numerosCarta);
		listaNumeroCarta = numerosCarta.stream().map(String::valueOf).collect(Collectors.toList());

		itemsList = Arrays.asList(listaNombre, listaNumeroCarta, listaEditorial, listaColeccion, listaRareza,
				listaEsFoil, listaGradeo, listaEstado, listaPrecios);

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
				List<String> isFoilSet = new ArrayList<>(); // Cambia el tipo aquí
				List<String> nombreGradeoSet = new ArrayList<>(); // Cambia el tipo aquí
				List<String> nombreEstadoSet = new ArrayList<>(); // Cambia el tipo aquí
				List<String> precioCartaSet = new ArrayList<>(); // Cambia el tipo aquí

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

					String esFoil = rs.getString("esFoilCarta").trim();
					isFoilSet.add(esFoil);

					String gradeoCarta = rs.getString("gradeoCarta").trim();
					nombreGradeoSet.add(gradeoCarta);

					String estadoCarta = rs.getString("estadoCarta").trim();
					nombreEstadoSet.add(estadoCarta);

					String precioCarta = rs.getString("precioCarta").trim();
					precioCartaSet.add(precioCarta);

				} while (rs.next());

				procesarDatosAutocompletado(nombreColeccionSet);
				procesarDatosAutocompletado(nombreEditorialSet);

				// Eliminar elementos repetidos
				nombreCartaSet = listaArregladaAutoComplete(nombreCartaSet);
				numeroCartaSet = listaArregladaAutoComplete(numeroCartaSet);
				nombreColeccionSet = listaArregladaAutoComplete(nombreColeccionSet);
				rarezaCartaSet = listaArregladaAutoComplete(rarezaCartaSet);
				isFoilSet = listaArregladaAutoComplete(isFoilSet);
				nombreEditorialSet = listaArregladaAutoComplete(nombreEditorialSet);
				nombreGradeoSet = listaArregladaAutoComplete(nombreGradeoSet);
				nombreEstadoSet = listaArregladaAutoComplete(nombreEstadoSet);
				precioCartaSet = listaArregladaAutoComplete(precioCartaSet);

				Collections.sort(numeroCartaSet, Comparable::compareTo);

				listaOrdenada.add(nombreCartaSet);
				listaOrdenada.add(numeroCartaSet.stream().map(String::valueOf).toList());
				listaOrdenada.add(nombreColeccionSet);
				listaOrdenada.add(rarezaCartaSet);
				listaOrdenada.add(isFoilSet);
				listaOrdenada.add(nombreEditorialSet);
				listaOrdenada.add(nombreGradeoSet);
				listaOrdenada.add(nombreEstadoSet);
				listaOrdenada.add(precioCartaSet);

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
		nombreEsFoilList.clear();

		nombreEsFoilList.clear();
		nombreGradeoList.clear();
		nombreEstadoList.clear();
		nombrePrecioList.clear();
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
		listaEsFoil.clear();
		listaRareza.clear();
		listaColeccion.clear();
	}

	/**
	 * Permite reiniciar la lista de cómics.
	 */
	public static void reiniciarListaCartas() {
		listaCartas.clear(); // Limpia la lista de cómics
	}

	/**
	 * Funcion que permite limpiar de contenido la lista de comics guardados
	 */
	public static void limpiarListaGuardados() {
		cartasGuardadosList.clear();
	}

	public static void reiniciarListas() {
		listaCartas.clear();
		listaCartasCheck.clear();
		listaID.clear();
		listaNombre.clear();
		listaNumeroCarta.clear();
		listaRareza.clear();
		listaEditorial.clear();
		listaEsFoil.clear();
		listaColeccion.clear();
		nombreCartaList.clear();
		numeroCartaList.clear();
		nombreEsFoilList.clear();
		nombreGradeoList.clear();
		nombreEstadoList.clear();
		nombrePrecioList.clear();
		nombreColeccionList.clear();
		nombreRarezaList.clear();
		nombreEditorialList.clear();
		nombreEsFoilList.clear();
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
	 * Agrega un elemento único a la lista principal de cómics guardados,
	 * ordenándolos por ID en orden descendente de longitud.
	 *
	 * @param comicToAdd Carta a añadir a la lista principal.
	 */
	public static void agregarElementoUnico(Carta cartaToAdd) {
		// Usamos un Set para mantener los elementos únicos
		Set<String> idsUnicos = cartasGuardadosList.stream().map(Carta::getIdCarta).collect(Collectors.toSet());

		// Verificamos si la ID del cómic ya está en la lista principal
		if (!idsUnicos.contains(cartaToAdd.getIdCarta())) {
			// Añadimos el cómic a la lista principal
			cartasGuardadosList.add(cartaToAdd);

			// Ordenamos la lista por ID en orden descendente de longitud
			cartasGuardadosList
					.sort(Comparator.comparing(Carta::getIdCarta, Comparator.comparingInt(String::length).reversed()));
		}
	}

	/**
	 * Ordena un HashMap en orden ascendente por valor (valor de tipo Integer).
	 *
	 * @param map El HashMap a ordenar.
	 * @return Una lista de entradas ordenadas por valor ascendente.
	 */
	public static List<Map.Entry<String, Integer>> sortByValue(Map<String, Integer> map) {
		List<Map.Entry<String, Integer>> list = new LinkedList<>(map.entrySet());
		list.sort(Map.Entry.comparingByKey());
		return list;
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
				PreparedStatement stmt = conn.prepareStatement(sentenciaSQL, ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_READ_ONLY);
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

			listaAutoCompletado = listaArregladaAutoComplete(listaAutoCompletado);

		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}

		return listaAutoCompletado;
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
		System.out.println("Tamaño de listaEsFoil: " + listaEsFoil.size());
		System.out.println("Tamaño de listaColeccion: " + listaColeccion.size());
		System.out.println("Tamaño de nombreCartaList: " + nombreCartaList.size());
		System.out.println("Tamaño de numeroCartaList: " + numeroCartaList.size());
		System.out.println("Tamaño de nombreEsFoilList: " + nombreEsFoilList.size());
		System.out.println("Tamaño de nombreGradeoList: " + nombreGradeoList.size());
		System.out.println("Tamaño de nombreEstadoList: " + nombreEstadoList.size());
		System.out.println("Tamaño de nombrePrecioList: " + nombrePrecioList.size());
		System.out.println("Tamaño de nombreColeccionList: " + nombreColeccionList.size());
		System.out.println("Tamaño de nombreRarezaList: " + nombreRarezaList.size());
		System.out.println("Tamaño de nombreEditorialList: " + nombreEditorialList.size());
		System.out.println("Tamaño de nombreEsFoilList: " + nombreEsFoilList.size());
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
		Map<String, Integer> nomVarianteEstadistica = new HashMap<>();
		Map<String, Integer> firmaEstadistica = new HashMap<>();
		Map<String, Integer> nomEditorialEstadistica = new HashMap<>();
		Map<String, Integer> formatoEstadistica = new HashMap<>();
		Map<String, Integer> procedenciaEstadistica = new HashMap<>();
		Map<String, Integer> fechaPublicacionEstadistica = new HashMap<>();
		Map<String, Integer> nomGuionistaEstadistica = new HashMap<>();
		Map<String, Integer> nomDibujanteEstadistica = new HashMap<>();
		Map<String, Integer> puntuacionEstadistica = new HashMap<>();
		Map<String, Integer> estadoEstadistica = new HashMap<>();
		List<String> keyIssueDataList = new ArrayList<>();

		final String CONSULTA_SQL = "SELECT * FROM albumbbdd";
		int totalCartas = 0;
		try (Connection conn = ConectManager.conexion();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(CONSULTA_SQL)) {

			// Procesar los datos y generar la estadística
			while (rs.next()) {
				// Obtener los datos de cada campo
				String nomCarta = rs.getString("nomCarta");
				int numCarta = rs.getInt("numCarta");
				String nivelGradeo = rs.getString("nivel_gradeo");
				String nomVariante = rs.getString("nomVariante");
				String firma = rs.getString("firma");
				String nomEditorial = rs.getString("nomEditorial");
				String formato = rs.getString("formato");
				String procedencia = rs.getString("procedencia");
				String fechaPublicacion = rs.getString("fecha_publicacion");
				String nomGuionista = rs.getString("nomGuionista");
				String nomDibujante = rs.getString("nomDibujante");
				String puntuacion = rs.getString("puntuacion");
				String estado = rs.getString("estado");
				String clave_comic = rs.getString("key_issue");

				// Actualizar los HashMaps para cada campo
				nomCartaEstadistica.put(nomCarta, nomCartaEstadistica.getOrDefault(nomCarta, 0) + 1);
				nivelGradeoEstadistica.put(nivelGradeo, nivelGradeoEstadistica.getOrDefault(nivelGradeo, 0) + 1);

				firmaEstadistica.put(firma, firmaEstadistica.getOrDefault(firma, 0) + 1);
				nomEditorialEstadistica.put(nomEditorial, nomEditorialEstadistica.getOrDefault(nomEditorial, 0) + 1);
				formatoEstadistica.put(formato, formatoEstadistica.getOrDefault(formato, 0) + 1);
				procedenciaEstadistica.put(procedencia, procedenciaEstadistica.getOrDefault(procedencia, 0) + 1);
				fechaPublicacionEstadistica.put(fechaPublicacion,
						fechaPublicacionEstadistica.getOrDefault(fechaPublicacion, 0) + 1);

				// Dividir los valores separados por guiones ("-") en cada campo y contarlos
				// como entradas independientes en las estadísticas
				String[] claveList = clave_comic.split("-");
				for (String clave : claveList) {
					clave = clave.trim(); // Remove leading and trailing spaces

					// Aquí verificamos si clave_comic no es "Vacio" ni está vacío antes de agregar
					// a keyIssueDataList
					if (!clave.equalsIgnoreCase("Vacio") && !clave.isEmpty()) {
						String keyIssueData = "Nombre del comic: " + nomCarta + " - " + "Numero: " + numCarta
								+ " - Key issue:  " + clave;
						keyIssueDataList.add(keyIssueData);
					}
				}

				// Dividir los valores separados por guiones ("-") en cada campo y contarlos
				// como entradas independientes en las estadísticas
				String[] varianteList = nomVariante.split("-");
				for (String variante : varianteList) {
					variante = variante.trim(); // Remove leading and trailing spaces
					nomVarianteEstadistica.put(variante, nomVarianteEstadistica.getOrDefault(variante, 0) + 1);
				}

				// Dividir los valores separados por guiones ("-") en cada campo y contarlos
				// como entradas independientes en las estadísticas
				String[] guionistaList = nomGuionista.split("-");
				for (String guionista : guionistaList) {
					guionista = guionista.trim(); // Remove leading and trailing spaces
					nomGuionistaEstadistica.put(guionista, nomGuionistaEstadistica.getOrDefault(guionista, 0) + 1);
				}
				// Dividir los valores separados por guiones ("-") en cada campo y contarlos
				// como entradas independientes en las estadísticas

				String[] dibujanteList = nomDibujante.split("-");
				for (String dibujante : dibujanteList) {
					dibujante = dibujante.trim(); // Remove leading and trailing spaces
					nomDibujanteEstadistica.put(dibujante, nomDibujanteEstadistica.getOrDefault(dibujante, 0) + 1);
				}

				// Dividir los valores separados por guiones ("-") en cada campo y contarlos
				// como entradas independientes en las estadísticas
				String[] firmaList = firma.split("-");
				for (String firmaValor : firmaList) {
					firmaValor = firmaValor.trim(); // Remove leading and trailing spaces
					firmaEstadistica.put(firmaValor, firmaEstadistica.getOrDefault(firmaValor, 0) + 1);
				}

				// Dividir los valores separados por guiones ("-") en cada campo y contarlos
				// como entradas independientes en las estadísticas
				String[] procedenciaList = procedencia.split("-");
				for (String procedenciaValor : procedenciaList) {
					procedenciaValor = procedenciaValor.trim(); // Remove leading and trailing spaces
					procedenciaEstadistica.put(procedenciaValor,
							procedenciaEstadistica.getOrDefault(procedenciaValor, 0) + 1);
				}

				puntuacionEstadistica.put(puntuacion, puntuacionEstadistica.getOrDefault(puntuacion, 0) + 1);
				estadoEstadistica.put(estado, estadoEstadistica.getOrDefault(estado, 0) + 1);

				totalCartas++; // Incrementar el contador de cómics

			}

		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}

		// Generar la cadena de estadística
		StringBuilder estadisticaStr = new StringBuilder();
		String lineaDecorativa1 = "\n--------------------------------------------------------";
		String lineaDecorativa2 = "--------------------------------------------------------\n";

		String fechaActual = Utilidades.obtenerFechaActual();

		estadisticaStr.append("Estadisticas de comics de la base de datos: " + ConectManager.DB_NAME + ", a fecha de: "
				+ fechaActual + "\n");

		// Agregar los valores de nomCarta a la estadística
		estadisticaStr.append(lineaDecorativa1);
		estadisticaStr.append("\nEstadística de los nombres de comics:\n");
		estadisticaStr.append(lineaDecorativa2);
		List<Map.Entry<String, Integer>> nomCartaList = sortByValue(nomCartaEstadistica);
		for (Map.Entry<String, Integer> entry : nomCartaList) {
			estadisticaStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		// Agregar los valores de nivelGradeo a la estadística
		estadisticaStr.append(lineaDecorativa1);
		estadisticaStr.append("\nEstadística de las cajas:\n");
		estadisticaStr.append(lineaDecorativa2);
		List<Map.Entry<String, Integer>> nivelGradeoList = sortByValue(nivelGradeoEstadistica);
		for (Map.Entry<String, Integer> entry : nivelGradeoList) {
			estadisticaStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		// Agregar los valores de nomVariante a la estadística
		estadisticaStr.append(lineaDecorativa1);
		estadisticaStr.append("\nEstadística de los nombres de variantes:\n");
		estadisticaStr.append(lineaDecorativa2);
		List<Map.Entry<String, Integer>> nomVarianteList = sortByValue(nomVarianteEstadistica);
		for (Map.Entry<String, Integer> entry : nomVarianteList) {
			estadisticaStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		// Agregar los valores de firma a la estadística
		estadisticaStr.append(lineaDecorativa1);
		estadisticaStr.append("\nEstadística de autores firma:\n");
		estadisticaStr.append(lineaDecorativa2);
		List<Map.Entry<String, Integer>> firmaList = sortByValue(firmaEstadistica);
		for (Map.Entry<String, Integer> entry : firmaList) {
			estadisticaStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		// Agregar los valores de nomGuionista a la estadística
		estadisticaStr.append(lineaDecorativa1);
		estadisticaStr.append("\nEstadística de guionistas:\n");
		estadisticaStr.append(lineaDecorativa2);
		List<Map.Entry<String, Integer>> nomGuionistaList = sortByValue(nomGuionistaEstadistica);
		for (Map.Entry<String, Integer> entry : nomGuionistaList) {
			estadisticaStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		// Agregar los valores de nomDibujante a la estadística
		estadisticaStr.append(lineaDecorativa1);
		estadisticaStr.append("\nEstadística de dibujantes:\n");
		estadisticaStr.append(lineaDecorativa2);
		List<Map.Entry<String, Integer>> nomDibujantesList = sortByValue(nomDibujanteEstadistica);
		for (Map.Entry<String, Integer> entry : nomDibujantesList) {
			estadisticaStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		// Agregar los valores de nomEditorial a la estadística
		estadisticaStr.append(lineaDecorativa1);
		estadisticaStr.append("\nEstadística de Editoriales:\n");
		estadisticaStr.append(lineaDecorativa2);
		List<Map.Entry<String, Integer>> nomEditorialList = sortByValue(nomEditorialEstadistica);
		for (Map.Entry<String, Integer> entry : nomEditorialList) {
			estadisticaStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		// Agregar los valores de procedencia a la estadística
		estadisticaStr.append(lineaDecorativa1);
		estadisticaStr.append("\nEstadística de procedencia:\n");
		estadisticaStr.append(lineaDecorativa2);
		List<Map.Entry<String, Integer>> procedenciaList = sortByValue(procedenciaEstadistica);
		for (Map.Entry<String, Integer> entry : procedenciaList) {
			estadisticaStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		// Agregar los valores de fechaPublicacion a la estadística
		estadisticaStr.append(lineaDecorativa1);
		estadisticaStr.append("\nEstadística de fecha publicacion:\n");
		estadisticaStr.append(lineaDecorativa2);
		List<Map.Entry<String, Integer>> fechaPublicacionList = sortByValue(fechaPublicacionEstadistica);
		for (Map.Entry<String, Integer> entry : fechaPublicacionList) {
			estadisticaStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		// Agregar los valores de puntuacion a la estadística
		estadisticaStr.append(lineaDecorativa1);
		estadisticaStr.append("\nEstadística de puntuacion:\n");
		estadisticaStr.append(lineaDecorativa2);
		List<Map.Entry<String, Integer>> puntuacionList = sortByValue(puntuacionEstadistica);
		for (Map.Entry<String, Integer> entry : puntuacionList) {
			estadisticaStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		// Agregar los valores de estado a la estadística
		estadisticaStr.append(lineaDecorativa1);
		estadisticaStr.append("\nEstadística de estado:\n");
		estadisticaStr.append(lineaDecorativa2);
		List<Map.Entry<String, Integer>> estadoList = sortByValue(estadoEstadistica);
		for (Map.Entry<String, Integer> entry : estadoList) {
			estadisticaStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		// Agregar los valores de formato a la estadística
		estadisticaStr.append(lineaDecorativa1);
		estadisticaStr.append("\nEstadística de key issue:\n");
		estadisticaStr.append(lineaDecorativa2);
		for (String keyIssueData : keyIssueDataList) {
			estadisticaStr.append(keyIssueData).append("\n");
		}

		// Agregar los valores de formato a la estadística
		estadisticaStr.append(lineaDecorativa1);
		estadisticaStr.append("\nEstadística de formato:\n");
		estadisticaStr.append(lineaDecorativa2);
		estadisticaStr.append("Cartas en total: " + totalCartas).append("\n");
		List<Map.Entry<String, Integer>> formatoList = sortByValue(formatoEstadistica);
		for (Map.Entry<String, Integer> entry : formatoList) {
			estadisticaStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		estadisticaStr.append(lineaDecorativa1);

		// Crear el archivo de estadística y escribir los datos en él
		String nombreArchivo = "estadistica_" + fechaActual + ".txt";
		String userHome = System.getProperty("user.home");
		String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
		String carpetaLibreria = ubicacion + File.separator + "libreria";
		String rutaCompleta = carpetaLibreria + File.separator + nombreArchivo;

		try (PrintWriter writer = new PrintWriter(new FileWriter(rutaCompleta))) {
			writer.print(estadisticaStr);

			// Abrir el archivo con el programa asociado en el sistema
			Utilidades.abrirArchivo(rutaCompleta);

		} catch (IOException e) {
			Utilidades.manejarExcepcion(e);
		}
	}

}