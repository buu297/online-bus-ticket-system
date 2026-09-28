@echo off
if not exist "bin\com\busticket\Main.class" (
    echo Binaries not found, compiling first...
    call compile.bat
)
echo Starting Online Bus Ticket Reservation System...
java -cp bin com.busticket.Main
