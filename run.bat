@echo off
setlocal
where javac >nul 2>nul
if errorlevel 1 (
    echo JDK 17 or later is required. Install a JDK and add its bin folder to PATH.
    pause
    exit /b 1
)
if not exist out mkdir out
javac -encoding UTF-8 -d out src\main\java\com\college\result\*.java
if errorlevel 1 (
    echo Compilation failed.
    pause
    exit /b 1
)
java -cp out com.college.result.Main
endlocal
