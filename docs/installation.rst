Installation
============

Maven project dependency
------------------------

The Lablink OPC UA client's compiled Java package is available on the |MCR|_.
Use it in your local Maven_ setup by including the following dependency into your *pom.xml*:

.. code-block:: xml

   <dependency>
     <groupId>at.ac.ait.lablink.clients</groupId>
     <artifactId>opcuaclient</artifactId>
     <version>0.0.3</version>
   </dependency>

.. note:: You may have to adapt this snippet to use the latest version, please check the |MCR|_.

Installation from source
------------------------

Installation from source requires a local **Java Development Kit** installation, for instance the `Oracle Java SE Development Kit 13 <https://www.oracle.com/technetwork/java/javase/downloads/index.html>`_ or the `OpenJDK <https://openjdk.java.net/>`_.

Check out the project and compile it with Maven_:

.. code-block:: winbatch

   git clone https://github.com/ait-lablink/lablink-opcua-client
   cd lablink-opcua-client
   mvnw clean package

This will create JAR file *opcuaclient-<VERSION>-jar-with-dependencies.jar* in subdirectory *target/assembly*.

Docker image
------------

A ready-to-run image is published on `Docker Hub <https://hub.docker.com/r/ait1/lablink-opcua-client>`_ as ``ait1/lablink-opcua-client:latest``:

.. code-block:: winbatch

   docker pull ait1/lablink-opcua-client:latest

The image starts ``at.ac.ait.lablink.clients.opcuaclient.BasicOpcUaClient``.
A client shell in the configuration needs an interactive terminal.
See :doc:`here <running>` for how to start the container.
Building the image locally with :github_blob:`docker/build.cmd <docker/build.cmd>` tags the result with the same name.

Troubleshooting the installation
--------------------------------

Nothing yet ...


.. |MCR| replace:: Maven Central Repository
.. _MCR: https://search.maven.org/artifact/at.ac.ait.lablink.clients/opcuaclient
.. _Maven: https://maven.apache.org
