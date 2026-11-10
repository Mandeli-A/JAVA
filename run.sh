#!/bin/bash
mkdir -p bin
javac -encoding UTF-8 -cp "lib/mysql-connector-j-8.0.33.jar" -d bin src/co/com/crediya/conexion/*.java src/co/com/crediya/modelo/*.java src/co/com/crediya/fabrica/*.java src/co/com/crediya/datos/*.java src/co/com/crediya/servicio/*.java src/co/com/crediya/app/*.java
java -cp "bin:lib/mysql-connector-j-8.0.33.jar" co.com.crediya.app.Principal

