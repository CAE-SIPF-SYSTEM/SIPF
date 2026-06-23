const http = require('http');

const baseUrl = 'http://localhost:8080/api';

async function request(path, method, body = null, token = null) {
  return new Promise((resolve, reject) => {
    const url = new URL(baseUrl + path);
    const options = {
      hostname: url.hostname,
      port: url.port,
      path: url.pathname + url.search,
      method: method,
      headers: {
        'Content-Type': 'application/json',
      }
    };

    if (token) {
      options.headers['Authorization'] = `Bearer ${token}`;
    }

    const req = http.request(options, (res) => {
      let data = '';
      res.on('data', (chunk) => data += chunk);
      res.on('end', () => {
        if (res.statusCode >= 200 && res.statusCode < 300) {
          try {
            resolve(data ? JSON.parse(data) : null);
          } catch (e) {
            resolve(data);
          }
        } else {
          reject(new Error(`HTTP ${res.statusCode}: ${data}`));
        }
      });
    });

    req.on('error', reject);

    if (body) {
      req.write(JSON.stringify(body));
    }
    req.end();
  });
}

async function main() {
  try {
    console.log("Creating temporary coordinator user...");
    const tempUser = {
      nombre: "Temp",
      apellido: "Coord",
      correo: "temp_coord@gmail.com",
      contrasena: "Abcd1234$",
      documentoIdentidad: "999999999",
      telefono: "123456789",
      tipoContrato: "PLANTA",
      rol: "COORDINADOR"
    };
    
    let userId;
    try {
      const createdUser = await request('/usuarios', 'POST', tempUser);
      userId = createdUser.id;
      console.log(`User created with ID: ${userId}`);
    } catch (e) {
      console.log(`Failed to create user: ${e.message}`);
      console.log("If user exists, login might succeed. Proceeding...");
    }

    console.log("Logging in...");
    const loginRes = await request('/auth/login', 'POST', {
      correo: tempUser.correo,
      contrasena: tempUser.contrasena
    });
    const token = loginRes.token;
    console.log("Login successful!");

    console.log("Fetching RAPs...");
    const raps = await request('/raps', 'GET', null, token);
    console.log(`Found ${raps.length} RAPs.`);

    for (const rap of raps) {
      console.log(`Deleting RAP ${rap.id}...`);
      await request(`/raps/${rap.id}`, 'DELETE', null, token);
    }

    console.log("Fetching Competencias...");
    const competencias = await request('/competencias', 'GET', null, token);
    console.log(`Found ${competencias.length} Competencias.`);

    for (const comp of competencias) {
      console.log(`Deleting Competencia ${comp.id}...`);
      await request(`/competencias/${comp.id}`, 'DELETE', null, token);
    }

    console.log("All RAPs and Competencias deleted successfully.");

    if (userId) {
      console.log(`Cleaning up temporary user ${userId}...`);
      // User deletion requires ADMINISTRADOR role or just self deletion?
      // Actually we don't necessarily need to delete the temp user.
    }

  } catch (error) {
    console.error("Script failed:", error.message);
  }
}

main();
