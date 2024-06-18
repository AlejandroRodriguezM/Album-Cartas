package webScrap;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CloudflareCookieManager {

    public static void main(String[] args) {
        String url = "https://www.ejemplo.com";
        Map<String, String> cookies = obtenerCookies(url);

        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }

    public static Map<String, String> obtenerCookies(String url) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true); // Para ejecutar en modo headless (sin interfaz gráfica)

        WebDriver driver = new ChromeDriver(options);
        driver.get(url);

        // Obtener todas las cookies del navegador
        Set<Cookie> cookiesSet = driver.manage().getCookies();

        // Convertir Set<Cookie> a Map<String, String> de nombre-valor
        Map<String, String> cookies = new HashMap<>();
        for (Cookie cookie : cookiesSet) {
            cookies.put(cookie.getName(), cookie.getValue());
        }

        // Cerrar el navegador
        driver.quit();

        return cookies;
    }
}