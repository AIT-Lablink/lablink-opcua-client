@ECHO OFF

SETLOCAL

REM Load the setup for the examples.
CALL %~DP0\..\setup.cmd

REM Path to class implementing the main routine.
SET BASIC_OPCUAC_LIENT=at.ac.ait.lablink.clients.opcuaclient.BasicOpcUaClient

REM Logger configuration.
SET LOGGER_CONFIG=-Dlog4j.configurationFile=%LLCONFIG%ait.all.log4j2

REM Data point bridge configuration.
SET CONFIG_FILE_URI=%LLCONFIG%ait.test.opcuaclient.reader.config

REM Run the example.
"%JAVA_HOME%\bin\java.exe" %LOGGER_CONFIG% -cp %OPCUA_CLIENT_JAR_FILE% %BASIC_OPCUAC_LIENT% -c %CONFIG_FILE_URI%

PAUSE
