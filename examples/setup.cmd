@ECHO OFF

REM =============================================================
REM Edit the following variables to comply with your local setup.
REM =============================================================

REM Connection string for configuration server.
SET LLCONFIG=http://localhost:10101/get?id=

REM Version of the OPC UA client package.
SET VERSION=0.0.3

REM Root directory of the OPC UA client package (only change this if you really know what you are doing).
SET OPCUA_CLIENT_ROOT_DIR=%~DP0..

REM Path to Java JAR file of the OPC UA client package.
SET OPCUA_CLIENT_JAR_FILE=%OPCUA_CLIENT_ROOT_DIR%\target\assembly\opcuaclient-%VERSION%-jar-with-dependencies.jar

REM Path to Java JAR file of config server.
SET CONFIG_JAR_FILE=%OPCUA_CLIENT_ROOT_DIR%\target\dependency\config-0.1.1-jar-with-dependencies.jar

REM Check if environment variable JAVA_HOME has been defined.
IF NOT DEFINED JAVA_HOME (
    ECHO WARNING: environment variable JAVA_HOME has not been defined!
    PAUSE
)
