Invoking the clients from the command line
==========================================

When running the clients, the use of the ``-c`` command line flag followed by the URI to the configuration (see :doc:`here <configuration>`) is mandatory.
For example, on Windows this could look something like this:

.. code-block:: winbatch

   SET LLCONFIG=http://localhost:10101/get?id=
   SET CONFIG_FILE_URI=%LLCONFIG%ait.test.opcuaclient.config
   
   SET BASIC_OPCUAC_LIENT=at.ac.ait.lablink.clients.opcuaclient.BasicOpcUaClient
   SET OPCUA_CLIENT_JAR_FILE=\path\to\lablink-opcua-client\target\assembly\opcuaclient-<VERSION>-jar-with-dependencies.jar
   
   "%JAVA_HOME%\bin\java.exe" -cp %OPCUA_CLIENT_JAR_FILE% %BASIC_OPCUAC_LIENT% -c %CONFIG_FILE_URI%
