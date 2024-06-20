const puppeteer = require('puppeteer');
async function getPrice(url) {
	const browser = await puppeteer.launch({ headless: true });
	try {
		const page = await browser.newPage();
		await page.goto(url);
		// await page.waitForSelector('div.price-box.mb-2.mb-sm-0');
		await page.waitForSelector('div.price-box__price');
		const precioCarta = await page.$eval('div.price-box__price', element => element.textContent.trim());
		console.log(precioCarta); // Imprimir el precio
	} catch (error) {
		console.error('Error al obtener el precio de la carta:', error.message);
	} finally {
		await browser.close();
	}
}
// Leer la URL desde los argumentos de la línea de comandos
const url = process.argv[2];
getPrice(url);