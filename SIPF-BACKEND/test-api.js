const http = require('http');

const data = JSON.stringify({
  "correo": "angular@test.com",
  "contrasena": "Password123!",
  "rol": "ADMINISTRADOR",
  "nombre": "Juan",
  "apellido": "Perez",
  "documentoIdentidad": 1234567,
  "telefono": 1234567,
  "tipoContrato": "PLANTA"
});

const options = {
  hostname: 'localhost',
  port: 8080,
  path: '/api/usuarios',
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Content-Length': data.length
  }
};

const req = http.request(options, (res) => {
  console.log(`STATUS: ${res.statusCode}`);
  res.on('data', (chunk) => {
    console.log(`BODY: ${chunk}`);
  });
});

req.on('error', (e) => {
  console.error(`problem with request: ${e.message}`);
});

req.write(data);
req.end();
