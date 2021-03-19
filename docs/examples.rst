Prerequisites
=============

Required Lablink resources
--------------------------

The following Lablink resources are required for running the examples:

* `Configuration Server <https://ait-lablink.readthedocs.io/projects/lablink-config-server>`__: *config-0.1.0-jar-with-dependencies.jar*

When :doc:`building from source <installation>`, the corresponding JAR files will be copied to directory *target/dependency*.


Starting the configuration server
---------------------------------

Start the configuration server by executing script :github_blob:`run_config.cmd <examples/0_config/lablink/run_config.cmd>` in subdirectory :github_tree:`examples/0_config/lablink`.
This will make the content of database file *example-lablink-config.db* available via http://localhost:10101.

**NOTE**:
Once the server is running, you can view the available configurations in a web browser via http://localhost:10101.

**TIP**:
A convenient tool for viewing the content of the database file (and editing it for experimenting with the examples) is `DB Browser for SQLite <https://sqlitebrowser.org/>`_.


Required OPC UA resources
-------------------------

The example uses a very basic OPC UA server, which is implemented with the help of the `Python OPC-UA client and server library <https://github.com/FreeOpcUa/python-opcua>`_.
Use ``pip`` to install all required Python packages:

.. code-block::

   pip install -r requirements.txt

**NOTE**:
This setup has been tested with Python 3.8.5, you may have to adapt the package versions in file ``requirements.txt`` for other versions of Python.


Starting the OPC UA test server
-------------------------------

Start the OPC UA test server by executing script :github_blob:`example-opcua-server.py <examples/0_config/opcua/example-opcua-server.py>` in subdirectory :github_tree:`examples/0_config/opcua`.

.. code-block::

   python example-opcua-server.py


Example: Reading from and writing to an OPC UA server
=====================================================

In this example, two instances of class *BasicOpcUaClient* are used to write/read data from/to an OPC UA server:

* One client is configured to only write values to the server.
  Via the console, the user can set new values to the client's data services, which will be written to the associated variables on the OPC UA server.
  (In a realistic setup, the data services would receive data from other Lablink clients.)
* The other client is configured to only read values from the server.
  In the console, new values are displayed every time on of the associated variables changes on the OPC UA server.
  (In a realistic setup, the data services would be sent to other Lablink clients.)

.. image:: img/lablink-opcua-example.png
   :align: center
   :alt: Lablink OPC UA example.

All relevant scripts can be found in subdirectory :github_tree:`examples/1_read_write`.
To run the example, execute all scripts either in separate command prompt windows or by double-clicking:

* :github_blob:`writer.cmd <examples/1_read_write/writer.cmd>`: runs the client that writes values to the OPC UA server
* :github_blob:`reader.cmd <examples/1_read_write/reader.cmd>`: runs the client that reads values from the OPC UA server

The order in which the scripts are started is arbitrary.

Once the write-only client client starts up, the client shell can be used to interact with the OPC UA server.
To start with, you can type ``ls`` to list all available data services:

.. code-block:: doscon

   llclient> ls
   Name            DataType                State
   xds     Double  0.0
   xluis   Long    0
   xlis    Long    0
   xbs     Boolean false
   xluil   Long    0
   xlil    Long    0
   xlui    Long    0
   xld     Long    0
   xli     Long    0
   xdb     Double  0.0
   xdd     Double  0.0
   xbb     Boolean false
   xdf     Double  0.0
   xbd     Boolean false
   xdi     Double  0.0

You can use the console to change the values of these data services, which will cause the associated variable on the OPC UA server to be updated accordingly.
For instance, data service ``xdf`` expect an input of type ``Double`` and will write this value to the OPC UA server variable with node ID ``LablinkTest/ScalarTypes/LlTestFloat``.
To update the value of this data service, use command ``svd``:

.. code-block:: doscon

   llclient> svd xdf 12.34
   Success

After a short delay, all the read-only client's data services subscribed to OPC UA server variable ``LablinkTest/ScalarTypes/LlTestFloat`` will receive the corresponding value.
When this happens, you should see log outputs in the client's console similar to the following:

.. code-block:: doscon

   19:01:52.459 [milo-shared-thread-pool-0] INFO  OpcUaClientBase - subscription value received: item=NodeId{ns=2, id=LablinkTest/ScalarTypes/LlTestFloat}, value=Variant{value=12.34}, handle=1
   19:01:52.464 [milo-shared-thread-pool-0] INFO  OpcUaClientBase - subscription value received: item=NodeId{ns=2, id=LablinkTest/ScalarTypes/LlTestFloat}, value=Variant{value=12.34}, handle=10
   19:01:52.469 [milo-shared-thread-pool-0] INFO  OpcUaClientBase - subscription value received: item=NodeId{ns=2, id=LablinkTest/ScalarTypes/LlTestFloat}, value=Variant{value=12.34}, handle=14
   
To check the actual value of the data services, you can again type ``ls``.
You will see that the value of ``12.34`` has been received by several data services, with the value cast accordingly to the service's data type:

.. code-block:: doscon

   ysf     String  12.34
   ylf     Long    12
   ydf     Double  12.34000015258789

Note that the casting of the original value to data type ``Long`` and ``Double`` causes rounding errors!
Hence, using the appropriate data type is always advisable ...
