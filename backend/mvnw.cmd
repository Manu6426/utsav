@echo off
rem Minimal Maven bootstrap for the Utsav backend (Windows).
rem Downloads Apache Maven 3.9.9 once into %USERPROFILE%\.utsav-maven and reuses it.
setlocal
set MVN_VERSION=3.9.9
set INSTALL_DIR=%USERPROFILE%\.utsav-maven
set MVN_HOME=%INSTALL_DIR%\apache-maven-%MVN_VERSION%

if not exist "%MVN_HOME%\bin\mvn.cmd" (
  echo [mvnw] Downloading Apache Maven %MVN_VERSION% (one-time)...
  if not exist "%INSTALL_DIR%" mkdir "%INSTALL_DIR%"
  powershell -NoProfile -Command "Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/%MVN_VERSION%/apache-maven-%MVN_VERSION%-bin.zip' -OutFile '%TEMP%\maven.zip'"
  powershell -NoProfile -Command "Expand-Archive -Path '%TEMP%\maven.zip' -DestinationPath '%INSTALL_DIR%' -Force"
)

where java >nul 2>nul
if errorlevel 1 (
  echo [mvnw] ERROR: Java 17+ is required but 'java' was not found on PATH.
  exit /b 1
)

"%MVN_HOME%\bin\mvn.cmd" %*
