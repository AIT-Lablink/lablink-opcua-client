//
// Copyright (c) AIT Austrian Institute of Technology GmbH.
//
// This program and the accompanying materials are made
// available under the terms of the Eclipse Public License 2.0
// which is available at: https://www.eclipse.org/legal/epl-2.0/
//

package at.ac.ait.lablink.clients.opcuaclient;

import at.ac.ait.lablink.clients.opcuaclient.listeners.DataChangeListenerBoolean;
import at.ac.ait.lablink.clients.opcuaclient.listeners.DataChangeListenerDouble;
import at.ac.ait.lablink.clients.opcuaclient.listeners.DataChangeListenerLong;
import at.ac.ait.lablink.clients.opcuaclient.listeners.DataChangeListenerString;
import at.ac.ait.lablink.clients.opcuaclient.listeners.IDataChangeListener;
import at.ac.ait.lablink.clients.opcuaclient.notifiers.InputDataNotifierBoolean;
import at.ac.ait.lablink.clients.opcuaclient.notifiers.InputDataNotifierDouble;
import at.ac.ait.lablink.clients.opcuaclient.notifiers.InputDataNotifierLong;
import at.ac.ait.lablink.clients.opcuaclient.notifiers.InputDataNotifierString;
import at.ac.ait.lablink.clients.opcuaclient.services.EDataServiceType;

import at.ac.ait.lablink.core.service.IImplementedService;
import at.ac.ait.lablink.core.service.LlService;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.subscriptions.ManagedDataItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.ManagedSubscription;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

/**
 * Class BasicOpcUaClient.
 *
 * <p>This client acts as an adapter between Lablink and an OPC UA server. Data received by this
 * client will be written to the corresponding variables on the OPC UA server. Conversely, this
 * client sends data whenever the corresponding variables on the OPC UA server change. This data
 * exchange happens only on demand, i.e., whenever either a new input is sent to the client or in
 * case a value changes on the OPC UA server.
 *
 * <p>This simple adaptor only works for basic OPC UA data types: (short) byte, (unsinged) integer,
 * boolean, float, double, string. It does not support other types, complex objects, function
 * calls, etc.
 */
public class BasicOpcUaClient extends OpcUaClientBase {

  // Tags for OPC UA client configuration.
  protected static final String OPCUA_DEFAULT_SAMPLING_INTERVAL_TAG = "DefaulSamplingInterval_ms";

  // Tags for input configuration.
  protected static final String INPUT_DATATYPE_TAG = "DataType";
  protected static final String INPUT_ID_TAG = "Name";
  protected static final String INPUT_NODE_ID_NUMERIC_TAG = "NodeIdNumeric";
  protected static final String INPUT_NODE_ID_STRING_TAG = "NodeIdString";
  protected static final String INPUT_UNIT_TAG = "Unit";

  // Tags for output configuration.
  protected static final String OUTPUT_DATATYPE_TAG = "DataType";
  protected static final String OUTPUT_ID_TAG = "Name";
  protected static final String OUTPUT_NODE_ID_NUMERIC_TAG = "NodeIdNumeric";
  protected static final String OUTPUT_NODE_ID_STRING_TAG = "NodeIdString";
  protected static final String OUTPUT_UNIT_TAG = "Unit";

  /** Sampling interval for data subscriptions to OPC UA server. */
  private long defaultSamplingInterval;

  /** Manager for data subscriptions to OPC UA server. */
  private ManagedSubscription subscription;

  /** Mapping of data subscription handles to data change listeners. */
  private Map<UInteger, IDataChangeListener> dataChangeListeners;

  /**
   * The main method.
   *
   * @param args arguments to main method
   * @throws at.ac.ait.lablink.core.client.ex.ClientNotReadyException
   *   client not ready
   * @throws at.ac.ait.lablink.core.client.ex.CommInterfaceNotSupportedException
   *   comm interface not supported
   * @throws at.ac.ait.lablink.core.client.ex.DataTypeNotSupportedException
   *   data type not supported
   * @throws at.ac.ait.lablink.core.client.ex.InvalidCastForServiceValueException
   *   invalid cast for service value
   * @throws at.ac.ait.lablink.core.client.ex.NoServicesInClientLogicException
   *   no services in client logic
   * @throws at.ac.ait.lablink.core.client.ex.NoSuchCommInterfaceException
   *   no such comm interface
   * @throws at.ac.ait.lablink.core.client.ex.ServiceIsNotRegisteredWithClientException
   *   service is not registered with client
   * @throws at.ac.ait.lablink.core.client.ex.ServiceTypeDoesNotMatchClientType
   *   service type does not match client type
   * @throws org.apache.commons.cli.ParseException
   *   parse exception
   * @throws org.apache.commons.configuration.ConfigurationException
   *   configuration error
   * @throws org.json.simple.parser.ParseException
   *   parse error
   * @throws java.io.IOException
   *   IO error
   * @throws java.io.IOException
   *   IO exception error
   * @throws java.net.MalformedURLException
   *   malformed URL
   * @throws java.net.URISyntaxException
   *   URI syntax error
   * @throws java.util.NoSuchElementException
   *   no such element
   */
  public static void main(String[] args) throws
      at.ac.ait.lablink.core.client.ex.ClientNotReadyException,
      at.ac.ait.lablink.core.client.ex.CommInterfaceNotSupportedException,
      at.ac.ait.lablink.core.client.ex.DataTypeNotSupportedException,
      at.ac.ait.lablink.core.client.ex.InvalidCastForServiceValueException,
      at.ac.ait.lablink.core.client.ex.NoServicesInClientLogicException,
      at.ac.ait.lablink.core.client.ex.NoSuchCommInterfaceException,
      at.ac.ait.lablink.core.client.ex.ServiceIsNotRegisteredWithClientException,
      at.ac.ait.lablink.core.client.ex.ServiceTypeDoesNotMatchClientType,
      org.apache.commons.cli.ParseException,
      org.apache.commons.configuration.ConfigurationException,
      org.json.simple.parser.ParseException,
      java.io.IOException,
      java.lang.Exception,
      java.net.MalformedURLException,
      java.net.URISyntaxException,
      java.util.NoSuchElementException {

    // Retrieve configuration.
    JSONObject jsonConfig = OpcUaClientBase.getConfig(args);

    // Instantiate Lablink client.
    BasicOpcUaClient app = new BasicOpcUaClient(jsonConfig);

    if (true == OpcUaClientBase.getWriteConfigAndExitFlag()) {
      // Run a test (write client config and exit).
      TestUtil.writeConfigAndExit(app);
    } else {
      app.runClient();
    }
  }

  /**
   * Constructor.
   *
   * @param jsonConfig configuration data (JSON format)
   * @throws at.ac.ait.lablink.core.client.ex.ClientNotReadyException
   *   client not ready
   * @throws at.ac.ait.lablink.core.client.ex.CommInterfaceNotSupportedException
   *   comm interface not supported
   * @throws at.ac.ait.lablink.core.client.ex.DataTypeNotSupportedException
   *   data type not supported
   * @throws at.ac.ait.lablink.core.client.ex.InvalidCastForServiceValueException
   *   invalid cast for service value
   * @throws at.ac.ait.lablink.core.client.ex.NoServicesInClientLogicException
   *   no services in client logic
   * @throws at.ac.ait.lablink.core.client.ex.NoSuchCommInterfaceException
   *   no such comm interface
   * @throws at.ac.ait.lablink.core.client.ex.ServiceIsNotRegisteredWithClientException
   *   service is not registered with client
   * @throws at.ac.ait.lablink.core.client.ex.ServiceTypeDoesNotMatchClientType
   *   service type does not match client type
   * @throws org.apache.commons.configuration.ConfigurationException
   *   configuration error
   * @throws java.io.IOException
   *   IO exception error
   * @throws java.net.URISyntaxException
   *   URI syntax error
   * @throws java.util.NoSuchElementException
   *   no such element
   */
  public BasicOpcUaClient(JSONObject jsonConfig) throws
      at.ac.ait.lablink.core.client.ex.ClientNotReadyException,
      at.ac.ait.lablink.core.client.ex.CommInterfaceNotSupportedException,
      at.ac.ait.lablink.core.client.ex.DataTypeNotSupportedException,
      at.ac.ait.lablink.core.client.ex.InvalidCastForServiceValueException,
      at.ac.ait.lablink.core.client.ex.NoServicesInClientLogicException,
      at.ac.ait.lablink.core.client.ex.NoSuchCommInterfaceException,
      at.ac.ait.lablink.core.client.ex.ServiceIsNotRegisteredWithClientException,
      at.ac.ait.lablink.core.client.ex.ServiceTypeDoesNotMatchClientType,
      org.apache.commons.configuration.ConfigurationException,
      java.io.IOException,
      java.lang.Exception,
      java.net.URISyntaxException,
      java.util.NoSuchElementException {

    super(jsonConfig);
  }

  /**
   * Start the event loop of this client.
   *
   * <p>This functions does not have to implement a logic for the event loop, because all
   * interactions with the OPC UA server are handled via the data change listeners and the
   * input data notifiers. However, this method has to provide the references to the client's
   * implemented data services to the data change listeners, because they are not available
   * before (the Lablink client has to be initialized and started first).
   *
   * <p>Implements {@link OpcUaClientRunner#startEventLoop()}.
   */
  protected void startEventLoop() throws
      Exception {

    logger.info("Start event loop");

    for (IDataChangeListener dcl: dataChangeListeners.values()) {
      // Get name of output data service associated to this data listener.
      String outputServiceName = dcl.getServiceName();

      // Retrieve implemented output data service from Lablink client.
      IImplementedService dataService = client.getImplementedServices().get(outputServiceName);

      // Link the data listener with the output data service.
      dcl.setImplementedService(dataService);
    }

    // Add data change listener to subscription.
    subscription.addDataChangeListener(
        (items, values) -> this.updateDataChangeListeners(items, values)
    );
  }

  /**
   * Configure the OPC UA client. Implements {@link OpcUaClientBase#initOpcUaClient(JSONObject)}.
   *
   * @param opcuaClientConfig configuration data (JSON format)
   */
  protected void initOpcUaClient(JSONObject opcuaClientConfig) {
    // Retrieve the additional config parameter that determines the sampling interval
    // of the OPC UA server data subscriptions. Will be used later, when setting up the
    // input data services and their associated data subscriptions.
    defaultSamplingInterval = ConfigUtil.getOptionalConfigParam(
        opcuaClientConfig, OPCUA_DEFAULT_SAMPLING_INTERVAL_TAG, 1000L
    );
  }

  /**
   * Configure the Lablink client data services, which serve as inputs for the OPC UA client.
   * Values received as inputs will be written to the corresponding nodes of the connected
   * OPC UA server. Implements {@link OpcUaClientBase#configureInputs(JSONArray)}.
   *
   * @param inputConfigList configuration data (JSON format)
   * @throws at.ac.ait.lablink.core.client.ex.ServiceTypeDoesNotMatchClientType
   *   service type does not match client type
   */
  protected void configureInputs(JSONArray inputConfigList) throws
      at.ac.ait.lablink.core.client.ex.ServiceTypeDoesNotMatchClientType {

    logger.info("Configuring client inputs...");

    @SuppressWarnings("rawtypes")
    Iterator inputConfigListIter = inputConfigList.iterator();

    // Create data point consumer for each input.
    while (inputConfigListIter.hasNext()) {
      JSONObject inputConfig = (JSONObject) inputConfigListIter.next();

      // Retrieve name of new input data service.
      String inputId = ConfigUtil.<String>getRequiredConfigParam(
          inputConfig, INPUT_ID_TAG,
          String.format("name for input data service is missing (%1$s)", INPUT_ID_TAG));

      // Retrieve node ID of associated OPC UA server variable (either a numeric value or a string).
      Number numericNodeId = ConfigUtil.getOptionalConfigParam(
          inputConfig, INPUT_NODE_ID_NUMERIC_TAG, (Number) null);
      String strNodeId = ConfigUtil.getOptionalConfigParam(
          inputConfig, INPUT_NODE_ID_STRING_TAG, (String) null);

      if (numericNodeId == null && strNodeId == null) {
        throw new NoSuchElementException(String.format(
            "node ID (numeric or string) for OPC UA variable is missing (%1$s or %2$s)", 
            INPUT_NODE_ID_NUMERIC_TAG, INPUT_NODE_ID_STRING_TAG
        ));
      }

      // Retrieve service type label.
      String dataType = ConfigUtil.<String>getRequiredConfigParam(
          inputConfig, INPUT_DATATYPE_TAG,
          String.format("type for input data service is missing (%1$s)", INPUT_DATATYPE_TAG));

      // Retrieve unit associated to data service.
      String unit = ConfigUtil.getOptionalConfigParam(inputConfig, INPUT_UNIT_TAG, "");

      logger.info("add new {} input: {}", dataType.toLowerCase(), inputId);

      // Determine type of data service.
      EDataServiceType serviceType = EDataServiceType.fromString(dataType);

      // Create and retrieve reference to new data service.
      LlService dataService = this.addDataService(inputId, serviceType, unit);

      // Retrieve complete node ID of associated OPC UA server variable.
      NodeId nodeId = null;
      if (numericNodeId != null) {
        nodeId = new NodeId(getNamespaceIndex(), numericNodeId.intValue());
      } else if (strNodeId != null) {
        nodeId = new NodeId(getNamespaceIndex(), strNodeId);
      }

      // Retrieve data type ID of associated OPC UA server variable.
      int dataTypeId = getDataTypeId(nodeId);

      // Add state change notifiers to data services. These notifiers will write new values to the
      // associated OPC UA server variables via method writeValue(...).
      addStateChangeNotifier(dataService, serviceType, nodeId, dataTypeId);
    }
  }

  /**
   * Configure the Lablink client data services, which serve as outputs for the OPC UA client.
   * Selected values read from the connected OPC UA server will be set as outputs.
   * Implements {@link OpcUaClientBase#configureOutputs(JSONArray)}.
   *
   * @param outputConfigList configuration data (JSON format)
   * @throws at.ac.ait.lablink.core.client.ex.ServiceTypeDoesNotMatchClientType
   *   service type does not match client type
   */
  protected void configureOutputs(JSONArray outputConfigList) throws
      at.ac.ait.lablink.core.client.ex.ServiceTypeDoesNotMatchClientType,
      org.eclipse.milo.opcua.stack.core.UaException {

    logger.info("Configuring client outputs...");

    // Create a new managed data subscription for the OPC UA server).
    subscription = ManagedSubscription.create(opcUaClient);

    // Set the sampling interval for the subscription.
    subscription.setDefaultSamplingInterval(defaultSamplingInterval);

    // This map will associate the subscription handles to the data change listeners.
    dataChangeListeners = new HashMap<>();

    @SuppressWarnings("rawtypes")
    Iterator outputConfigListIter = outputConfigList.iterator();

    while (outputConfigListIter.hasNext()) {
      JSONObject outputConfig = (JSONObject) outputConfigListIter.next();

      // Retrieve name of new ouput data service.
      String outputId = ConfigUtil.<String>getRequiredConfigParam(
          outputConfig, OUTPUT_ID_TAG,
          String.format("name for output data serivce is missing (%1$s)", OUTPUT_ID_TAG));

      // Retrieve node ID of associated OPC UA server variable (either a numeric value or a string).
      Number numericNodeId = ConfigUtil.getOptionalConfigParam(
          outputConfig, OUTPUT_NODE_ID_NUMERIC_TAG, (Number) null);
      String strNodeId = ConfigUtil.getOptionalConfigParam(
          outputConfig, OUTPUT_NODE_ID_STRING_TAG, (String) null);

      if (numericNodeId == null && strNodeId == null) {
        throw new NoSuchElementException(String.format(
            "node ID (numeric or string) for OPC UA variable is missing (%1$s or %2$s)", 
            OUTPUT_NODE_ID_NUMERIC_TAG, OUTPUT_NODE_ID_STRING_TAG
        ));
      }

      // Retrieve service type label.
      String dataType = ConfigUtil.<String>getRequiredConfigParam(
          outputConfig, OUTPUT_DATATYPE_TAG,
          String.format("type for output data service is missing (%1$s)", OUTPUT_DATATYPE_TAG));

      // Retrieve unit associated to data service.
      String unit = ConfigUtil.getOptionalConfigParam(outputConfig, OUTPUT_UNIT_TAG, "");

      logger.info("add new {} output: {}", dataType.toLowerCase(), outputId);

      // Determine type of data service.
      EDataServiceType serviceType = EDataServiceType.fromString(dataType);

      // Create and retrieve reference to new data service.
      this.addDataService(outputId, serviceType, unit);

      // Retrieve complete node ID of associated OPC UA server variable.
      NodeId nodeId = null;
      if (numericNodeId != null) {
        nodeId = new NodeId(getNamespaceIndex(), numericNodeId.intValue());
      } else if (strNodeId != null) {
        nodeId = new NodeId(getNamespaceIndex(), strNodeId);
      }

      // Create managed data item.
      ManagedDataItem item = subscription.createDataItem(nodeId);

      // Retrieve handle for monitored item.
      UInteger handle = item.getMonitoredItem().getClientHandle();

      // Retrieve data type ID of associated OPC UA server variable.
      int dataTypeId = getDataTypeId(nodeId);

      // Add data change listeners to output services. These data changes listeners are called by
      // the subscription callback (see methods "startEventLoop" and "updateDataChangeListeners")
      // and will update their associated output data services.
      addDataChangeListener(serviceType, outputId, dataTypeId, handle);
    }
  }

  /**
   * Custom shutdown hook for this client, which cancels all data subscription on the OPC UA
   * server. Overwrites default implementation from {@link OpcUaClientRunner#shutdownHook()}.
   */
  protected void shutdownHook() {
    List<ManagedDataItem> dataItems = subscription.getDataItems();
    CompletableFuture<?> futureDeleteMonitoredItems = subscription.deleteDataItemsAsync(dataItems);
    try {
      futureDeleteMonitoredItems.get();
    } catch (Exception ex) {
      logger.warn(ex.toString());
    }
  }

  /**
   * Callback function for the OPC UA server data subscription. Calls the data change listeners
   * associated to each monitored item, which in turn updates the corresponding output data
   * service.
   *
   * @param items list of changed data items
   * @param values list of corresponding new values
   */
  private void updateDataChangeListeners(
      List<ManagedDataItem> items, List<DataValue> values
  ) {
    iterateSimultaneously(items, values,
        (ManagedDataItem item, DataValue value) -> {

          logger.info(
              "subscription value received: item={}, value={}, handle={}",
              item.getNodeId(), value.getValue(), item.getMonitoredItem().getClientHandle()
          );

          // Retrieve handle of associated monitored item.
          UInteger handle = item.getMonitoredItem().getClientHandle();

          // Use the handle to retrieve the corresponding data change listener.
          IDataChangeListener dcl = dataChangeListeners.get(handle);

          // Set new value to data change listener, which will in turn set the new value to the
          // corresponding output data service.
          dcl.setValue(value);
      }
    );
  }

  /**
   * Add data change listeners to output services. These data changes listeners are called by
   * the subscription callback (see methods {@link #startEventLoop()} and
   * {@link #updateDataChangeListeners(List, List)}) and will update
   * their associated output data services.
   *
   * @param serviceType type of output service
   * @param outputId name of output service
   * @param dataTypeId data type of associated OPC UA server variable
   * @param handle handle of asscociated monitored item
   */
  private void addDataChangeListener(EDataServiceType serviceType, String outputId, 
      int dataTypeId, UInteger handle) {
    switch (serviceType) {
      case DOUBLE:
        dataChangeListeners.put(handle, new DataChangeListenerDouble(outputId, dataTypeId));
        break;
      case LONG:
        dataChangeListeners.put(handle, new DataChangeListenerLong(outputId, dataTypeId));
        break;
      case BOOLEAN:
        dataChangeListeners.put(handle, new DataChangeListenerBoolean(outputId, dataTypeId));
        break;
      case STRING:
        dataChangeListeners.put(handle, new DataChangeListenerString(outputId, dataTypeId));
        break;
      default:
        // This case cannot happen, method EDataServiceType.fromString(...) would have thrown
        // an exception.
        break;
    }
  }

  /**
   * Add state change notifiers to data services. These notifiers will write new values to the
   * associated OPC UA server variables via method writeValue(...).
   *
   * @param dataService input data service
   * @param serviceType data type of input data service
   * @param nodeId node ID of associated OPC UA server variable
   * @param dataTypeId data type ID of associated OPC UA server variable
   */
  @SuppressWarnings("unchecked")
  private void addStateChangeNotifier(LlService dataService, EDataServiceType serviceType,
      NodeId nodeId, int dataTypeId) {
    switch (serviceType) {
      case DOUBLE:
        dataService.addStateChangeNotifier(
            new InputDataNotifierDouble(this, nodeId, dataTypeId)
        );
        break;
      case LONG:
        dataService.addStateChangeNotifier(
            new InputDataNotifierLong(this, nodeId, dataTypeId)
        );
        break;
      case BOOLEAN:
        dataService.addStateChangeNotifier(
            new InputDataNotifierBoolean(this, nodeId, dataTypeId)
        );
        break;
      case STRING:
        dataService.addStateChangeNotifier(
            new InputDataNotifierString(this, nodeId, dataTypeId)
        );
        break;
      default:
        // This case cannot happen, method EDataServiceType.fromString(...) would have thrown
        // an exception.
        break;
    }
  }
}
