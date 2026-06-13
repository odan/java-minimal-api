@echo off
setlocal

REM Check for Administrator privileges
fltmc >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Please run this script as Administrator.
    pause
    exit /b 1
)

where openssl >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: OpenSSL was not found in PATH.
    pause
    exit /b 1
)

echo Creating Root CA private key...
openssl genrsa -out rootCA.key 4096

echo Creating Root CA certificate...
openssl req -x509 -new -nodes -key rootCA.key -sha256 -days 3650 -out rootCA.crt -subj "/C=DE/O=Local Development/CN=Local Development Root CA"

echo Installing Root CA into Windows Trusted Root Certification Authorities...
certutil -addstore Root "rootCA.crt"

echo Creating localhost private key...
openssl genrsa -out localhost.key 2048

echo Creating localhost CSR...
openssl req -new -key localhost.key -out localhost.csr -subj "/C=DE/O=Local Development/CN=localhost"

echo Creating certificate extension file...
(
echo authorityKeyIdentifier=keyid,issuer
echo basicConstraints=CA:FALSE
echo keyUsage=digitalSignature,keyEncipherment
echo extendedKeyUsage=serverAuth
echo subjectAltName=@alt_names
echo.
echo [alt_names]
echo DNS.1=localhost
echo IP.1=127.0.0.1
echo IP.2=::1
) > localhost.ext

echo Signing localhost certificate...
openssl x509 -req -in localhost.csr -CA rootCA.crt -CAkey rootCA.key -CAcreateserial -out localhost.crt -days 365 -sha256 -extfile localhost.ext

echo Creating PKCS12 keystore...
openssl pkcs12 -export -in localhost.crt -inkey localhost.key -out localhost.p12 -name localhost

echo.
echo Successfully created:
echo   rootCA.crt
echo   rootCA.key
echo   localhost.crt
echo   localhost.key
echo   localhost.p12
echo.
echo The Root CA has been installed into the Windows trust store.
echo Browsers on this machine should trust localhost.crt.
echo.

pause