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

Running with Docker
===================

The image ``ait1/lablink-opcua-client:latest`` (see :doc:`installation`) starts the class named by ``OPCUA_CLASS`` with ``-c`` set to ``${LLCONFIG}${OPCUA_CONFIG}``.
The defaults are ``LLCONFIG=http://lablink-config:10101/get?id=``, ``OPCUA_CONFIG=lablink-opcuaclient.config``, and ``OPCUA_CLASS=at.ac.ait.lablink.clients.opcuaclient.BasicOpcUaClient``, so the image expects a configuration server reachable as the host ``lablink-config``.
``MEMORY_FLAG`` is the JVM heap setting and defaults to ``-Xmx512m``.
``LOGGER_ID`` is the logging configuration id and defaults to ``ait.all.log4j2``.

``--interactive`` and ``--tty`` attach the terminal.
A configuration with ``ClientShell`` set to ``true`` reads commands from that terminal.

On Windows, a configuration server on the host is reachable as ``host.docker.internal``:

.. code-block:: winbatch

   docker run --rm --interactive --tty -e LLCONFIG=http://host.docker.internal:10101/get?id= -e OPCUA_CONFIG=ait.test.opcuaclient.writer.config ait1/lablink-opcua-client:latest

On Linux, add that hostname explicitly:

.. code-block:: bash

   docker run --rm --interactive --tty --add-host=host.docker.internal:host-gateway -e LLCONFIG=http://host.docker.internal:10101/get?id= -e OPCUA_CONFIG=ait.test.opcuaclient.writer.config ait1/lablink-opcua-client:latest

Arguments after the image name are passed through to the client.
