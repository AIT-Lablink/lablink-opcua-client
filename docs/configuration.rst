Overview
========

The configuration has to be JSON-formatted.
It is divided into the following categories:

  :*Client*: basic configuration of the Lablink client (JSON object)
  :*Config-OPC-UA*: basic configuration related to the OPC UA client (JSON object)
  :*Input*: configuration of the client's inputs, each associated to an OPC UA node (JSON array of JSON objects)
  :*Output*: configuration of the client's outputs, each associated to an OPC UA node (JSON array of JSON objects)

In the following, the configuration parameters for these categories are listed.

.. seealso:: See `below <#example-configuration>`_ for an example of a complete JSON configuration.

Basic Lablink Client Configuration
==================================

.. topic:: Required parameters

  :*ClientName*: client name
  :*GroupName*: group name
  :*ScenarioName*: scenario name
  :*labLinkPropertiesUrl*: URI to Lablink configuration
  :*syncHostPropertiesUrl*: URI to sync host configuration (*currently not supported, use dummy value here*)

.. topic:: Optional parameters

  :*ClientDescription*: description of the client
  :*ClientShell*: activate Lablink shell (default: ``false``).

OPC UA Client Configuration
===========================

.. topic:: Required parameters

  :*EndpointURL*: URL of OPC UA server
  :*NamespaceURI*: URI of OPC UA server namespace
  :*ClientURI*: URI of Lablink OPC UA client

.. topic:: Optional parameters for **BasicOpcUaClient**

  :*Username*: username for accessing the OPC UA server
  :*Password*: password for accessing the OPC UA server
  :*DefaulSamplingInterval_ms*: sampling interval for OPC UA server subscription (default: ``1000``)

.. note:: In case no login credentials are provided (username *and* password), the client will attempt to connect as anonymous user.

Input and Output Configuration
==============================

.. topic:: Required configuration parameters for each input/output

  :*Name:*: name of the client's input/output data service
  :*DataType*: data type of the client's input/output data service; allowed values are ``double``, ``long``, ``boolean`` and ``string``
  :*NodeIdString* or **NodeIdStringNumeric**: ID of associated OPC UA server node
  
.. topic:: Optional configuration parameters for each input/output
  
  :*Unit*: unit associated to the client's input/output data service

Example Configuration
=====================

The following is an example configuration for a *BasicOpcUaClient* client:

.. code-block:: json

   {
      "Client": {
         "ClientDescription": "Lablink OPC UA client example.",
         "ClientName": "TestOPCUAClient",
         "ClientShell": true,
         "GroupName": "OPCUADemo",
         "ScenarioName": "OPCUAClientTest",
         "labLinkPropertiesUrl": "http://localhost:10101/get?id=ait.all.llproperties",
         "syncHostPropertiesUrl": "http://localhost:10101/get?id=ait.all.sync-host.properties"
      },
      "Config-OPC-UA": {
         "EndpointURL": "opc.tcp://localhost:12345/lablink-test",
         "NamespaceURI": "urn:lablink:opcua-test",
         "ClientURI": "urn:lablink:clients:opcuaclient:test",
         "Username": "LablinkTestUser",
         "Password": "zQC37UiH6ou",
         "DefaulSamplingInterval_ms": 3000
      },
      "Input": [
         {
            "DataType": "double",
            "Name": "x",
            "NodeIdString": "LablinkTest/ScalarTypes/LlTestDouble",
            "Unit": "none"
         }
      ],
      "Output": [
         {
            "DataType": "integer",
            "Name": "y",
            "NodeIdString": "LablinkTest/ScalarTypes/LlTestUInt16",
            "Unit": "none"
         }
      ]
   }
