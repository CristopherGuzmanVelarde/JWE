@echo off
echo ========================================
echo   Iniciando Demo JWE
echo ========================================
echo.
echo Iniciando ambos servicios...
echo.

start "Servicio 2 - Descifrado" cmd /k "cd servicio2 && mvn spring-boot:run"
timeout /t 5 /nobreak > nul

start "Servicio 1 - Cifrado" cmd /k "cd servicio1 && mvn spring-boot:run"
timeout /t 5 /nobreak > nul

echo.
echo ========================================
echo   Servicios Iniciados
echo ========================================
echo.
echo Servicio 1 (Cifrado):   http://localhost:8081
echo Servicio 2 (Descifrado): http://localhost:8082
echo.
echo Abre tu navegador en: http://localhost:8081
echo.
echo Presiona cualquier tecla para abrir el navegador...
pause > nul

start http://localhost:8081
