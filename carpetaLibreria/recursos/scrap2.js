const puppeteer = require('puppeteer');

async function main(url) {
  try {
    if (!url) {
      throw new Error('Debe proporcionar una URL');
    }

    // Configura Puppeteer y abre una nueva instancia de navegador
    const browser = await puppeteer.launch({
      headless: false, // Cambiar a true en producción
      args: ['--window-size=10,10'] // Ajusta el tamaño de la ventana según tus necesidades
    });
    const page = await browser.newPage();

    try {
      // Navega a la URL proporcionada
      await page.goto(url, { waitUntil: 'networkidle2' });

      // Espera manualmente a que el usuario resuelva el CAPTCHA en el navegador visible
      await waitForManualCaptchaSolution();

      // Obtener los enlaces dentro del div con clase 'card-column'
      const links = await getLinks(page);

      // Imprimir los enlaces encontrados
      printData(links);

    } catch (error) {
      console.error('Error al obtener datos:', error);
    } finally {
      // Cierra el navegador
      await browser.close();
    }

  } catch (error) {
    console.error('Error general:', error.message);
  }
}

async function waitForManualCaptchaSolution() {
  // En un escenario real, aquí esperarías a que el usuario resuelva el CAPTCHA manualmente
  // En este ejemplo, simplemente esperamos un tiempo fijo antes de continuar (20 segundos)
  await new Promise(resolve => setTimeout(resolve, 200)); // 20 segundos
}

async function getLinks(page) {
  try {
    // Esperar a que aparezcan todos los divs con clase 'card-column'
    await page.waitForSelector('div.card-column');

    // Obtener todos los enlaces <a> dentro de todos los divs con clase 'card-column'
    const links = await page.evaluate(() => {
      const linksArray = [];
      const cardColumns = document.querySelectorAll('div.card-column');
      cardColumns.forEach(column => {
        const aElements = column.querySelectorAll('a[href]');
        aElements.forEach(a => {
          linksArray.push(a.href);
        });
      });
      return linksArray;
    });

    return links;

  } catch (error) {
    throw new Error('Error al obtener enlaces:', error.message);
  }
}

function printData(data) {
  data.forEach((link, index) => {
    console.log(`${link}`);
  });
}

// Leer la URL desde los argumentos de la línea de comandos
const url = process.argv[2];
main(url);
