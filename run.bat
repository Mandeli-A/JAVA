@echo off
chcp 65001 > nul

set "JAVAC_CMD=javac"
where javac >nul 2>nul
if %errorlevel% neq 0 (
    if exist "C:\Program Files\Java\jdk1.8.0_202\bin\javac.exe" (
        set "JAVAC_CMD=C:\Program Files\Java\jdk1.8.0_202\bin\javac.exe"
    )
)

set "JAVA_CMD=java"
where java >nul 2>nul
if %errorlevel% neq 0 (
    if exist "C:\Program Files\Java\jdk1.8.0_202\bin\java.exe" (
        set "JAVA_CMD=C:\Program Files\Java\jdk1.8.0_202\bin\java.exe"
    )
)

if not exist "bin" mkdir bin

"%JAVAC_CMD%" -encoding UTF-8 -cp "lib/mysql-connector-j-8.0.33.jar" -d bin src\co\com\crediya\conexion\*.java src\co\com\crediya\modelo\*.java src\co\com\crediya\fabrica\*.java src\co\com\crediya\datos\*.java src\co\com\crediya\servicio\*.java src\co\com\crediya\app\*.java

"%JAVA_CMD%" -cp "bin;lib/mysql-connector-j-8.0.33.jar" co.com.crediya.app.Principal
pause

