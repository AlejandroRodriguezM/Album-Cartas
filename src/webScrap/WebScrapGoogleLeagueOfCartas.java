package webScrap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cartaManagement.Carta;

public class WebScrapGoogleLeagueOfCartas {

	private static final int MAX_RETRY_ATTEMPTS = 3; // Máximo número de intentos de retry
	
	public static String agregarMasAMayusculas(String cadena) {
		return cadena.toUpperCase();
	}

	public static List<String> buscarURL(String searchTerm) throws URISyntaxException {
//		if (esURL(searchTerm)) {
//			return buscarURLValida(searchTerm);
//		} else {
		return buscarEnGoogle(searchTerm);
//		}
	}

	public static String buscarURLValida(String urlString) throws URISyntaxException {
		try {
			URI uri = new URI(urlString);
			URL url = uri.toURL();
			HttpURLConnection con = (HttpURLConnection) uri.toURL().openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36");

			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				return url.toString(); // Devuelve la representación de cadena de la URL
			} else {
				System.out.println("La URL proporcionada no es válida.");
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<String> buscarEnGoogle(String searchTerm) throws URISyntaxException {

		searchTerm = "Sliver Overlord";

		searchTerm = agregarMasAMayusculas(searchTerm);
		searchTerm = searchTerm.replace("(", "%28").replace(")", "%29").replace("#", "%23");

		try {
			String encodedSearchTerm = URLEncoder.encode(searchTerm, "UTF-8");
			String urlString = "https://www.google.com/search?q=" + encodedSearchTerm + "+card+market+all+versions";

			URI uri = new URI(urlString);
			URL url = uri.toURL();
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36");

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuilder content = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			con.disconnect();

			String html = content.toString();
			int startIndex = html.indexOf("https://www.cardmarket.com/");
			String[] urls = new String[html.length()]; // Inicialización con capacidad suficiente
			int urlCount = 0;

			while (startIndex != -1) {
				int endIndex = html.indexOf("\"", startIndex);
				if (endIndex != -1) {
					String urlFound = html.substring(startIndex, endIndex);

					if (urlFound.contains("/Versions")) {
						System.out.println(urlFound);
						return extraerEnlacesDePagina(urlFound);
					} else {
						urls[urlCount++] = urlFound; // Añadir la URL a la lista
					}
					startIndex = html.indexOf("https://www.cardmarket.com/", endIndex);
				} else {
					break;
				}
			}

//			if (urlCount > 0) {
//				String[] urlsTrimmed = new String[urlCount];
//				System.arraycopy(urls, 0, urlsTrimmed, 0, urlCount); // Ajustar el tamaño del array
//				return encontrarURLRelevante(urlsTrimmed, searchTerm); // Buscar la URL más relevante
//			}

			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String encontrarURLRelevante(String[] urls, String searchTerm) {
		for (String url : urls) {
			if (url.contains("/Versions")) {
				return url; // Devuelve inmediatamente la primera URL que contenga "/Versions"
			}
		}
		return null; // Si no se encuentra ninguna URL con "/Versions", devuelve null
	}

	public static int contarCoincidencias(String url, String searchTerm) {
		int coincidencia = 0;
		for (String word : searchTerm.split("\\s+")) {
			if (url.contains(word)) {
				coincidencia += word.length();
			}
		}
		return coincidencia;
	}

    public static List<String> extraerEnlacesDePagina(String urlString) {
        List<String> enlaces = new ArrayList<>();

        try {
            // Conectar y extraer enlaces con manejo de errores 403
            enlaces = connectAndExtractLinks(urlString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return enlaces;
    }

    private static List<String> connectAndExtractLinks(String urlString) throws IOException {
        List<String> enlaces = new ArrayList<>();
        Connection.Response response = connectWithRetry(urlString);

        int attempt = 1;

        while (attempt <= MAX_RETRY_ATTEMPTS && response.statusCode() == 403) {
            System.out.println("Error 403: Acceso prohibido. Intentando sortear la restricción, intento " + attempt);
            Map<String, String> cookies = response.cookies();
            enlaces = sortearRestriccion403ConCookies(urlString, cookies);
            attempt++;
            response = connectWithRetry(urlString); // Intentar la conexión nuevamente
        }

        if (response.statusCode() == 200) {
            // Éxito en sortear la restricción
            Document doc = response.parse();
            // Seleccionar todos los elementos div con la clase especificada
            Elements divs = doc.select("div.card-column.col-6.col-md-4.col-lg-3.px-2.pb-4");

            for (Element div : divs) {
                // Dentro de cada div, seleccionar todos los enlaces (a[href])
                Elements links = div.select("a[href]");
                for (Element link : links) {
                    String enlace = link.attr("abs:href"); // Obtener el atributo href como enlace absoluto
                    enlaces.add(enlace);
                }
            }
        } else {
            System.out.println("Error al obtener la página. Código de estado: " + response.statusCode());
        }

        return enlaces;
    }

    private static Connection.Response connectWithRetry(String urlString) throws IOException {
        // Lista de user agents alternativos que podrías usar
        String[] userAgents = {
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edge/91.0.864.67 Safari/537.36",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Firefox/89.0 Safari/537.36",
                // Puedes agregar más user agents aquí según sea necesario
        };

        int maxRetries = 3;
        int retry = 0;
        IOException lastException = null;

        while (retry < maxRetries) {
            try {
                String userAgent = userAgents[retry % userAgents.length];
                return Jsoup.connect(urlString)
                        .userAgent(userAgent)
                        .referrer("https://www.google.com/")
                        .timeout(30000)
                        .followRedirects(true)
                        .ignoreHttpErrors(true)
                        .execute();
            } catch (IOException e) {
                lastException = e;
                retry++;
                // Aquí podrías agregar un breve delay entre intentos si lo deseas
            }
        }

        // Si todos los intentos fallan, lanza la última excepción capturada
        if (lastException != null) {
            throw lastException;
        }

        // Este retorno nunca debería ocurrir si todo va bien
        return null;
    }

    private static List<String> sortearRestriccion403ConCookies(String urlString, Map<String, String> cookies) {
        List<String> enlaces = new ArrayList<>();

        try {
            // Intentar la conexión con las cookies recibidas
            Connection connection = Jsoup.connect(urlString)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Safari/537.36")
                    .referrer("https://www.google.com/")
                    .timeout(30000)
                    .followRedirects(true)
                    .cookies(cookies)
                    .ignoreHttpErrors(true);

            // Ejecutar la conexión y obtener el documento
            Document doc = connection.get();
            // Seleccionar todos los elementos div con la clase especificada
            Elements divs = doc.select("div.card-column.col-6.col-md-4.col-lg-3.px-2.pb-4");

            for (Element div : divs) {
                // Dentro de cada div, seleccionar todos los enlaces (a[href])
                Elements links = div.select("a[href]");
                for (Element link : links) {
                    String enlace = link.attr("abs:href"); // Obtener el atributo href como enlace absoluto
                    enlaces.add(enlace);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return enlaces;
    }

	public static boolean esURL(String urlString) {
		try {
			// Intenta crear una instancia de URI
			URI uri = new URI(urlString);
			// Verifica si la URI tiene un esquema (protocolo) válido
			if (uri.getScheme() != null
					&& (uri.getScheme().equalsIgnoreCase("http") || uri.getScheme().equalsIgnoreCase("https"))) {
				return true; // Si la URI tiene un esquema válido, se considera una URL válida
			}
		} catch (URISyntaxException e) {
			// La cadena no es una URI válida
		}
		return false; // Si hay una excepción o la URI no tiene un esquema válido, la URL no es válida
	}

	public static Carta obtenerDatosDiv(String url) throws URISyntaxException {

//		url = buscarURL(url);
//		if (url == null) {
//			return null;
//		}
//
//		try {
//
//			Document doc = Jsoup.connect(url).get();
//			Elements divPadres = doc.select("div.d-flex.flex-column.align-self-start.mt-2, div.row,cover-artists");
//			String fechaSalida = "";
//			String distribuidora = "";
//			String valorComic = "0";
//			String artistas = "";
//			String numComic = "0";
//			String titulo = "";
//			String coverURL = "";
//			String key = "";
//			String upcValue = "";
//			String formato = "";
//			String gradeo = "NM (Noir Medium)";
//			// Buscar la sección de detalles de la página
//			Element detallesPagina = doc.selectFirst("div.page-details");
//			if (detallesPagina != null) {
//				// Extraer la fecha de salida y la distribuidora
//				Element fechaElemento = detallesPagina.selectFirst("a[style]");
//				if (fechaElemento != null) {
//					fechaSalida = fechaElemento.text();
//				}
//				Element distribuidoraElemento = detallesPagina.selectFirst("a:not([style])");
//				if (distribuidoraElemento != null) {
//					distribuidora = distribuidoraElemento.text();
//				}
//			}
//
//			// Extraer el número de cómic de la etiqueta H1
//			Element h1Elemento = doc.selectFirst("h1");
//			if (h1Elemento != null) {
//				String h1Texto = h1Elemento.text().trim();
//				int indiceNumComic = h1Texto.indexOf("#");
//				if (indiceNumComic != -1) {
//					String numComicTexto = h1Texto.substring(indiceNumComic + 1).trim();
//					try {
//						numComic = numComicTexto;
//					} catch (NumberFormatException e) {
//						e.printStackTrace();
//					}
//				}
//				titulo = h1Texto;
//				if (indiceNumComic != -1) {
//					titulo = h1Texto.substring(0, indiceNumComic).trim();
//				}
//			}
//
//			// Declara conjuntos para almacenar los nombres
//			Set<String> coverArtists = new HashSet<>();
//			Set<String> writers = new HashSet<>();
//			Set<String> artists = new HashSet<>();
//
//			for (Element divPadre : divPadres) {
//				Element divComentadoAntes = divPadre.selectFirst("div.role.color-offset.copy-really-small");
//				Element link = divPadre.selectFirst("a");
//				if (divComentadoAntes != null && link != null) {
//					String textoDiv = divComentadoAntes.text();
//					String textoEnlace = link.text();
//
//					// Solo agregar los datos para "Cover Artist", "Writer" y "Artist"
//					if (textoDiv.equalsIgnoreCase("Cover Artist") || textoDiv.equalsIgnoreCase("Cover Penciller")) {
//						coverArtists.add(textoEnlace.replace("-", ""));
//					} else if (textoDiv.equalsIgnoreCase("Writer")) {
//						writers.add(textoEnlace.replace("-", ""));
//					} else if (textoDiv.equalsIgnoreCase("Artist") || textoDiv.equalsIgnoreCase("Artist, Colorist")
//							|| textoDiv.equalsIgnoreCase("Penciller")) {
//						// Agregar artistas solo si no es un "Cover Artist" y no está en la lista
//						if (!textoDiv.equalsIgnoreCase("Cover Artist")
//								&& !textoDiv.equalsIgnoreCase("Cover Penciller")) {
//							artists.add(textoEnlace.replace("-", ""));
//						}
//					}
//				}
//			}
//
//			Element divInfoComic = doc.selectFirst("div.col.copy-small.font-italic");
//			if (divInfoComic != null) {
//				String info = divInfoComic.text();
//				// Utilizar expresión regular para encontrar el valor siguiente al símbolo '$'
//				Pattern pattern = Pattern.compile("\\$([\\d.]+)");
//				Matcher matcher = pattern.matcher(info);
//				if (matcher.find()) {
//					valorComic = matcher.group(1);
//				}
//			}
//
//			Element coverArtDiv = doc.selectFirst("div.cover-art");
//			if (coverArtDiv != null) {
//				Element img = coverArtDiv.selectFirst("img");
//				if (img != null) {
//					// Obtener la URL de la imagen
//					coverURL = img.attr("src");
//					// Eliminar el texto después de ".jpg" si está presente
//					int index = coverURL.indexOf(".jpg");
//					if (index != -1) {
//						coverURL = coverURL.substring(0, index + 4);
//					}
//				}
//			}
//
//			Element listingDescriptionDiv = doc.selectFirst("div.col-12.listing-description");
//			if (listingDescriptionDiv != null) {
//				Elements parrafos = listingDescriptionDiv.select("p");
//				for (Element parrafo : parrafos) {
//					key += parrafo.text();
//				}
//			}
//
//			Element detailsAddtlDiv = doc.selectFirst("div.row.details-addtl.copy-small.mt-3");
//			if (detailsAddtlDiv != null) {
//				Element valueDiv = detailsAddtlDiv.selectFirst("div.name:contains(UPC) + div.value");
//				if (valueDiv != null) {
//					upcValue = valueDiv.text();
//				}
//			}
//
//			String fecha = convertirFechaMySQL(fechaSalida);
//
//			// Ahora, convierte los conjuntos a cadenas
//			String cover = String.join(", ", coverArtists);
//			String guionista = String.join(", ", writers);
//			String artistasString = String.join(", ", artists);
//
//			cover = Comic.limpiarCampo(cover);
//			titulo = Comic.limpiarCampo(titulo);
//			distribuidora = Comic.limpiarCampo(distribuidora);
//			guionista = Comic.limpiarCampo(guionista);
//			artistas = Comic.limpiarCampo(artistasString);
//			key = Comic.limpiarCampo(key);
//			upcValue = Comic.limpiarCampo(upcValue);
//			formato = Utilidades.devolverPalabrasClave(titulo);
//			String procedencia = "Estados Unidos (United States)";
//			String estado = "En posesion";
//			String puntuacion = "Sin puntuacion";
//
//			return new Comic.ComicBuilder("", titulo).valorGradeo(gradeo).numero(numComic).variante(cover).firma("")
//					.editorial(distribuidora).formato(formato).procedencia(procedencia).fecha(fecha)
//					.guionista(guionista).dibujante(artistas).estado(estado).keyIssue(key).puntuacion(puntuacion)
//					.imagen(coverURL).referenciaComic(url).precioComic(valorComic).codigoComic(upcValue).build();
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public static String convertirFechaMySQL(String fechaSalida) {
//		try {
//			// Mapear nombres de los meses en inglés a números
//			String[] meses = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
//			// Parsear la fecha al formato adecuado
//			SimpleDateFormat formatoEntrada = new SimpleDateFormat("MM dd, yyyy");
//			// Remover el sufijo ordinal numérico antes de parsear
//			fechaSalida = fechaSalida.replaceAll("(?<=\\d)(st|nd|rd|th)", "");
//			// Dividir la fecha en partes: mes, día, año
//			String[] partesFecha = fechaSalida.split(" ");
//			// Convertir el nombre del mes a un número
//			int mesNum = -1;
//			for (int i = 0; i < meses.length; i++) {
//				if (meses[i].equals(partesFecha[0])) {
//					mesNum = i + 1; // Añadir 1 porque los meses en SimpleDateFormat comienzan en 0
//					break;
//				}
//			}
//			// Formatear la fecha al formato MySQL
//			SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy-MM-dd");
//			if (partesFecha.length >= 3) {
//				Date fecha = formatoEntrada
//						.parse(String.format("%02d", mesNum) + " " + partesFecha[1] + " " + partesFecha[2]);
//				return formatoSalida.format(fecha);
//			} else {
//				return "2000-01-01"; // Devuelve la fecha por defecto
//			}
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
		return null;
	}

//	public static void main(String[] args) throws URISyntaxException {
//
//		String url = "75960620168600411";
//
//		obtenerDatosDiv(url);
//
//	}
}
