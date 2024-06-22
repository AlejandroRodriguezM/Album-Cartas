package UNIT_TEST;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TestApiImagen {

	public static void main(String[] args) throws URISyntaxException, IOException {

		String argumentos = "cardtrader+The+First+Sliver+1371+Secret+Lair+Drop+Series";

		String direccion = searchCardTraderUrl(argumentos);
		System.out.println(direccion);
		
		System.out.println(extraerDatosImagen(direccion));
	}

    public static String searchCardTraderUrl(String searchTerm) throws URISyntaxException {
        try {
            // Encode the search term for URL compatibility
            String encodedSearchTerm = URLEncoder.encode(searchTerm, "UTF-8");
            String urlString = "https://www.google.com/search?q=" + encodedSearchTerm;

            // Setup the connection
            URI uri = new URI(urlString);
            URL url = uri.toURL();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                    "(KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36");

            // Read the response from Google
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            // Convert response to string
            String html = content.toString();

            // Use regex to find the first URL from cardtrader.com in the search results
            Pattern pattern = Pattern.compile("href=\"(https://www.cardtrader.com/(?!versions)[^\"]+)\"");
            Matcher matcher = pattern.matcher(html);
            if (matcher.find()) {
                // Extract the URL
                String urlFound = matcher.group(1);
                return urlFound;
            } else {
                System.out.println("No se encontraron enlaces de cardtrader.com en los resultados.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

	public static String extraerDatosImagen(String url) throws IOException {

		Document doc = Jsoup.connect(url).get();

		String imagen = "";

		Element imagenElemento = doc.selectFirst(
				"div.image-flipper.border-radius-10 img[src*='/uploads/'][src$='.jpg'], div.image-flipper.border-radius-10 img[src*='/uploads/'][src$='.png']");
		if (imagenElemento != null) {
			imagen = "https://www.cardtrader.com/" + imagenElemento.attr("src");
		}

		return imagen;
	}
}