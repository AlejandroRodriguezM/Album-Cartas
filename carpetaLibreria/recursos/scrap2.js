const puppeteer = require('puppeteer');

async function getData(url) {
	const browser = await puppeteer.launch({ headless: true });
	try {
		const page = await browser.newPage();
		await page.goto(url);

		// Esperar a que aparezca el div con clase 'w-100 ml-3 mr-0 mr-md-3'
		await page.waitForSelector('div.w-100.ml-3.mr-0.mr-md-3');

		// Obtener todos los elementos <br> dentro del div
		const brElements = await page.$$('div.w-100.ml-3.mr-0.mr-md-3 br');

		let resultText = ''; // Variable para almacenar el resultado final

		// Iterar sobre los elementos <br> y procesar el texto
		for (let i = 0; i < brElements.length; i++) {
			const br = brElements[i];
			const text = await page.evaluate(element => element.nextSibling.textContent.trim(), br);

			// Verificar si el texto contiene {3}
			if (text.includes('{3}')) {
				// Eliminar {3} y limpiar símbolos como : ( ) !
				const cleanedText = text.replace(/\{3\}/g, '')
					.replace(/[():!]/g, '')
					.trim(); // Realizar trim para eliminar espacios en blanco adicionales

				// Agregar el texto limpio al resultado final si no está vacío
				if (cleanedText.length > 0) {
					// Agregar salto de línea solo si ya hay texto en resultText
					if (resultText.length > 0) {
						resultText += '\n';
					}
					resultText += cleanedText;
				}
			}
		}

	} catch (error) {
		console.error('Error al obtener datos:', error.message);
	} finally {
		await browser.close();
	}
}

// Leer la URL desde los argumentos de la línea de comandos
const url = process.argv[2];
getData(url);
