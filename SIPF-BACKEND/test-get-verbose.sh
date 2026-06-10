#!/bin/bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{"correo":"sistemas@cae.com","contrasena":"password"}' | grep -o '"token":"[^"]*' | cut -d'"' -f4)
curl -v http://localhost:8080/api/usuarios -H "Authorization: Bearer $TOKEN"
