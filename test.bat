@echo off
if not exist "bin\com\busticket\test\PatternVerificationTest.class" (
    echo Binaries not found, compiling first...
    call compile.bat
)
echo Running Design Patterns Verification Suite...
java -ea -cp bin com.busticket.test.PatternVerificationTest
pause
