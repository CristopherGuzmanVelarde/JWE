@echo off
echo ========================================
echo   Demo JWE - Inicio Rapido
echo ========================================
echo.

echo Verificando Java...
java -version
if %errorlevel% neq 0 (
    echo ERROR: Java no esta instalado o no esta en el PATH
    pause
    exit /b 1
)

echo.
echo Verificando Maven...
mvn -version
if %errorlevel% neq 0 (
    echo ERROR: Maven no esta instalado o no esta en el PATH
    pause
    exit /b 1
)

echo.
echo [1/5] Compilando Servicio 1...
cd servicio1
call mvn clean compile -DskipTests
if %errorlevel% neq 0 (
    echo ERROR: Fallo la compilacion del Servicio 1
    pause
    exit /b 1
)

echo.
echo [2/5] Generando claves RSA...
call mvn exec:java -Dexec.mainClass="com.demo.jwe.KeyGeneratorMain"
if %errorlevel% neq 0 (
    echo ERROR: Fallo la generacion de claves
    pause
    exit /b 1
)

echo.
echo [3/5] Copiando clave privada al Servicio 2...
if exist keys\private-key.pem (
    copy keys\private-key.pem ..\servicio2\keys\private-key.pem
    echo Clave copiada exitosamente
) else (
    echo ERROR: No se encontro la clave privada
    pause
    exit /b 1
)

echo.
echo [4/5] Compilando ambos servicios...
call mvn clean install -DskipTests
if %errorlevel% neq 0 (
    echo ERROR: Fallo la compilacion del Servicio 1
    pause
    exit /b 1
)

cd ..\servicio2
call mvn clean install -DskipTests
if %errorlevel% neq 0 (
    echo ERROR: Fallo la compilacion del Servicio 2
    pause
    exit /b 1
)

cd ..

echo.
echo ========================================
echo   Compilacion Completada
echo ========================================
echo.
echo Para iniciar los servicios, ejecuta:
echo   - start-servicio2.bat (en una terminal)
echo   - start-servicio1.bat (en otra terminal)
echo.
echo O ejecuta: start-all.bat
echo.
pause
