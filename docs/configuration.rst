Overview
========

The configuration has to be JSON-formatted.
It is divided into the following categories:

* **Client**: basic configuration of the Lablink client
* **Config-OPC-UA**: basic configuration related to the OPC UA client
* **Input**: configuration of the client's inputs, each associated to an OPC UA node
* **Output**: configuration of the client's outputs, each associated to an OPC UA node

Basic Lablink Client Configuration
==================================

Required parameters:

* **ClientName**: client name
* **GroupName**: group name
* **ScenarioName**: scenario name
* **labLinkPropertiesUrl**: URI to Lablink configuration
* **syncHostPropertiesUrl**: URI to sync host configuration (*currently not supported, use dummy value here*)

Optional parameters:

* **ClientDescription**: description of the client
* **ClientShell**: activate Lablink shell (default: ``false``).

OPC UA Client Configuration
===========================

Required parameters:

* **EndpointURL**: URL of OPC UA server
* **NamespaceURI**: URI of OPC UA server namespace
* **ClientURI**: URI of Lablink OPC UA client

Optional parameters for **BasicOpcUaClient**:

* **DefaulSamplingInterval_ms**: sampling interval for OPC UA server subscription

Input and Output Configuration
==============================

Configuration for each input/output.

Required parameters:

* **Name**: name of the client's input/output data service
* **DataType**: data type of the client's input/output data service; allowed values are ``double``, ``long``, ``boolean`` and ``string``
* **NodeIdString** or **NodeIdStringNumeric**: ID of associated OPC UA server node

Optional parameters:

* **Unit**: unit associated to the client's input/output data service

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
         "ClientURI": "urn:lablink:clients:opcuaclient:test",
         "DefaulSamplingInterval_ms": 3000,
         "EndpointURL": "opc.tcp://localhost:12345/lablink-test",
         "NamespaceURI": "urn:lablink:opcua-test"
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
