@echo off
chcp 65001 > nul
echo ===================================================
echo   Compiling Online Bus Ticket Reservation System
echo ===================================================
if not exist "bin" mkdir bin

dir /s /B src\main\java\*.java > sources.txt
javac -encoding UTF-8 -d bin @sources.txt
del sources.txt

if %ERRORLEVEL% EQU 0 (
    echo.
    echo [SUCCESS] Compilation completed with 0 errors! Classes generated in /bin
) else (
    echo.
    echo [ERROR] Compilation failed!
)