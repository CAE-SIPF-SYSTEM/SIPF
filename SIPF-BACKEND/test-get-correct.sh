#!/bin/bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{"correo":"admin2@gmail.com","contrasena":"AdminCae@2026"}' | grep -o '"token":"[^"]*' | cut -d'"' -f4)
curl -s http://localhost:8080/api/usuarios -H "Authorization: Bearer $TOKEN"
