@echo off

setlocal

if "%JASM_JAVA_HOME%" == "" goto error_varnotset

set JASM_JAVA_CMD=%JASM_JAVA_HOME%\bin\java.exe

if not exist "%JASM_JAVA_CMD%" goto error_javanotfound

set JASM_INSTALL_DIR=%~dp0

"%JASM_JAVA_CMD%" -classpath "%JASM_INSTALL_DIR%\jasm.jar" org.jasm.tools.Disassembler %*
goto end

:error_varnotset

echo Please set the variable JASM_JAVA_HOME to the java runtime's installation directory. You need at least JRE 1.8
goto end

:error_javanotfound

echo "Couldn't find %JASM_JAVA_CMD%! Please adjust JASM_JAVA_HOME to the correct value."

:end

endlocal

