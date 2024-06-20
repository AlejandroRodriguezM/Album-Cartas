package UNIT_TEST;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class testScrap {
	
	public static void main(String[] args) throws IOException {
		
		String url = "https://www.cardtrader.com/cards/sliver-overlord-scourge";
		
		Document doc = Jsoup.connect(url).get();
		
		// Seleccionar el elemento div con la clase blueprint-info
		Element blueprintInfoDiv = doc.select("div.w-100.ml-3.mr-0.mr-md-3").first();

		if (blueprintInfoDiv != null) {
			// Obtener todos los elementos <br> dentro de blueprint-info
			Elements brElements = blueprintInfoDiv.select("br");

			// Buscar el texto que empieza con {3}
			for (Element br : brElements) {
				String text = br.text().trim();
				System.out.println(text);
				if (text.startsWith(" {3}")) {
					System.out.println(text);
				}
			}
		}
		
	}

}
