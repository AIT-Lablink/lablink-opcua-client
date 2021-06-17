Maven project dependency
========================

Include the Lablink OPC UA client to your own Maven setup by including the following dependency into your *pom.xml*:

.. code-block:: xml

   <dependency>
     <groupId>at.ac.ait.lablink.clients</groupId>
     <artifactId>opcuaclient</artifactId>
     <version>0.0.2</version>
   </dependency>


Installation from source
========================

Installation from source requires a local **Java Development Kit** installation, for instance the `Oracle Java SE Development Kit 13 <https://www.oracle.com/technetwork/java/javase/downloads/index.html>`_ or the `OpenJDK <https://openjdk.java.net/>`_.

Check out the project and compile it with `Maven <https://maven.apache.org/>`__:

.. code-block:: winbatch

   git clone https://github.com/ait-lablink/lablink-opcua-client
   cd lablink-opcua-client
   mvnw clean package

This will create JAR file *opcuaclient-<VERSION>-jar-with-dependencies.jar* in subdirectory *target/assembly*.
Furthermore, all required JAR files for running the example will be copied to subdirectory *target/dependency/*.

Troubleshooting the installation
================================

Nothing yet ...
