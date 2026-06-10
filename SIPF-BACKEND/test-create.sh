#!/bin/bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{"correo":"sistemas@cae.com","contrasena":"password"}' | grep -o '"token":"[^"]*' | cut -d'"' -f4)
echo "TOKEN: $TOKEN"

curl -v -X POST http://localhost:8080/api/usuarios \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "correo": "testuser99@cae.com",
    "contrasena": "Password123!",
    "rol": "INSTRUCTOR",
    "nombre": "Test",
    "apellido": "User99",
    "documentoIdentidad": 99999999,
    "telefono": 99999999,
    "tipoContrato": "PLANTA"
  }'
