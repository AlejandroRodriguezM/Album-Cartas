const puppeteer = require('puppeteer');

async function getData(url) {
  try {
    // Configura Puppeteer y abre una nueva instancia de navegador
    const browser = await puppeteer.launch({
      headless: false, // Cambiar a false para mostrar el navegador en acción
      args: ['--window-size=10,10'] // Ajusta el tamaño de la ventana según tus necesidades
    });

    // Nueva página dentro del navegador
    const page = await browser.newPage();

    // Navega a la URL proporcionada
    await page.goto(url, { waitUntil: 'networkidle2' });

    // Espera manualmente a que el usuario resuelva el CAPTCHA en el navegador visible
    await waitForManualCaptchaSolution();

    // Obtener los datos de la página
    const data = await getPageData(page, url);

    // Imprimir los datos
    printData(data);

    // Cierra el navegador
    await browser.close();

    return data; // Devuelve los datos obtenidos
  } catch (error) {
    console.error('Error al obtener datos:', error);
    return null;
  }
}

async function waitForManualCaptchaSolution() {
  // En un escenario real, aquí esperarías a que el usuario resuelva el CAPTCHA manualmente
  // En este ejemplo, simplemente esperamos un tiempo fijo antes de continuar (20 segundos)
  await new Promise(resolve => setTimeout(resolve, 200)); // 20 segundos
}

async function getPageData(page) {
  const urlPagina = page.url(); // Obtener la URL actual del navegador

  return await page.evaluate((url) => {
    const h1Elements = document.querySelectorAll('h1');
    const images = Array.from(document.querySelectorAll('img'))
      .filter((img, index) => index === 1) // Filtrar para obtener solo la segunda imagen (index 1)
      .map((img) => img.src); // Obtener el src de la segunda imagen

    const result = {
      url: url,      // Guardar la URL de la página
      h1: [],        // Asegurarse de que h1 sea un array
      Coleccion: '',
      editorial: '',
      Rarity: '',
      Number: '',
      'Price Trend': '',
      foil: '',      // Nuevo campo para indicar si es "foil" o no
      normas: '',    // Nuevo campo para las normas
      imagen: images.length > 0 ? images[0] : ''  // Guardar la URL de la segunda imagen, o cadena vacía si no hay imagen
    };

    h1Elements.forEach(h1 => {
      let h1Text = '';
    
      // Iterar sobre todos los nodos hijos de h1
      h1.childNodes.forEach(node => {
        // Agregar solo el texto de nodos de texto (excluyendo elementos como span)
        if (node.nodeType === Node.TEXT_NODE) {
          h1Text += node.textContent.trim();
        }
      });
    
      if (h1Text) {
        const match = h1Text.match(/^(.*?[a-z])(?=[A-Z])/); // Buscar la división entre nombre y colección
        if (match) {
          result.h1.push(match[1].trim()); // Nombre principal
        } else {
          result.h1.push(h1Text); // Si no hay match, se agrega el texto completo como nombre principal
        }
      }
    });

    // Capturar las normas
    const normasContainer = document.querySelector('div.d-none.d-md-block');
    if (normasContainer) {
      const normasElements = normasContainer.querySelectorAll('p');
      if (normasElements.length > 0) {
        result.normas = Array.from(normasElements).map(p => p.textContent.trim()).join(' ');
      } else {
        result.normas = "No contiene normas o instrucciones";
      }
    } else {
      result.normas = "No contiene normas o instrucciones";
    }
    

    // Capturar editorial y otras informaciones
    const gamesDropdown = document.querySelector('div.dropdown.games-dropdown');
    if (!gamesDropdown) {
      throw new Error('No se encontró el div con clase "dropdown games-dropdown"');
    }

    const button = gamesDropdown.querySelector('button.btn.dropdown-toggle.w-100.text-start.btn-link.btn-sm');
    if (!button) {
      throw new Error('No se encontró el button con clases específicas dentro del div "games-dropdown"');
    }

    result.editorial = button.textContent.trim(); // Asignar el contenido del botón a 'editorial'

    const elements = document.querySelectorAll('.labeled.row.mx-auto.g-0');

    elements.forEach(element => {
      const dds = element.querySelectorAll('dd');
      dds.forEach(dd => {
        const key = dd.previousElementSibling.textContent.trim();
        let value = dd.textContent.trim();

        // Si la clave es "Rarity", buscamos el valor en el atributo data-bs-original-title del svg
        if (key === 'Rarity') {
          const svg = dd.querySelector('svg');
          if (svg) {
            value = svg.getAttribute('data-bs-original-title').trim();
          }
        }

        // Determinar si es "foil" o no
        if (key === 'Price Trend') {
          const esFoil = document.querySelector('div.image.card-image.is-magic.has-shadow.is-foil');
          result.foil = esFoil ? 'Si' : 'No';
        }

        // Asignar valores según la clave
        if (result.hasOwnProperty(key)) {
          result[key] = value;
        }
      });
    });

    const dtElements = document.querySelectorAll('dt');
    dtElements.forEach(dt => {
      const key = dt.textContent.trim();
      if (key === 'Printed in') {
        const dd = dt.nextElementSibling;
        if (dd && dd.tagName === 'DD') {
          result.Coleccion = dd.textContent.trim();
        }
      }
    });

    return result;
  }, urlPagina); // Pasar la URL de la página como argumento a page.evaluate
}

function printData(data) {
  console.log('Referencia:', data.url);
  console.log('Nombre:', data.h1.join(', '));
  console.log('Coleccion:', data.Coleccion);
  console.log('Editorial:', data.editorial);
  console.log('Rareza:', data.Rarity);
  console.log('Numero:', data.Number);
  console.log('Valor:', data['Price Trend']);
  console.log('Foil:', data.foil);
  console.log('Normas:', data.normas);
  console.log('Imagen:', data.imagen);
}

// Obtener la URL desde los argumentos de la línea de comandos
const url = process.argv[2];
getData(url);
