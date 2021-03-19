//
// Copyright (c) AIT Austrian Institute of Technology GmbH.
//
// This program and the accompanying materials are made
// available under the terms of the Eclipse Public License 2.0
// which is available at: https://www.eclipse.org/legal/epl-2.0/
//

package at.ac.ait.lablink.clients.opcuaclient;

import at.ac.ait.lablink.clients.opcuaclient.services.DataServiceBoolean;
import at.ac.ait.lablink.clients.opcuaclient.services.DataServiceDouble;
import at.ac.ait.lablink.clients.opcuaclient.services.DataServiceLong;
import at.ac.ait.lablink.clients.opcuaclient.services.DataServiceString;
import at.ac.ait.lablink.clients.opcuaclient.services.EDataServiceType;

import at.ac.ait.lablink.core.client.ci.mqtt.impl.MqttCommInterfaceUtility;
import at.ac.ait.lablink.core.client.ex.ClientNotReadyException;
import at.ac.ait.lablink.core.client.ex.CommInterfaceNotSupportedException;
import at.ac.ait.lablink.core.client.ex.DataTypeNotSupportedException;
import at.ac.ait.lablink.core.client.ex.InvalidCastForServiceValueException;
import at.ac.ait.lablink.core.client.ex.NoServicesInClientLogicException;
import at.ac.ait.lablink.core.client.ex.NoSuchCommInterfaceException;
import at.ac.ait.lablink.core.client.ex.ServiceIsNotRegisteredWithClientException;
import at.ac.ait.lablink.core.client.ex.ServiceTypeDoesNotMatchClientType;
import at.ac.ait.lablink.core.client.impl.LlClient;
import at.ac.ait.lablink.core.service.IImplementedService;
import at.ac.ait.lablink.core.service.LlService;
import at.ac.ait.lablink.core.utility.Utility;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.configuration.ConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// import org.eclipse.milo.opcua.sdk.client.DataTypeTreeBuilder;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
// import org.eclipse.milo.opcua.sdk.core.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * Class OpcUaClientBase.
 *
 * <p>Base class for OPC UA clients implemented in Lablink.
 */
public abstract class OpcUaClientBase extends OpcUaClientRunner {

  /** Logger. */
  protected static final Logger logger = LogManager.getLogger("OpcUaClientBase");

  // Flags for CLI setup.
  private static final String CLI_CONF_FLAG = "c";
  private static final String CLI_CONF_LONG_FLAG = "config";
  private static final String CLI_TEST_FLAG = "w";

  // Tags for client setup.
  protected static final String CLIENT_CONFIG_TAG = "Client";
  protected static final String CLIENT_DESC_TAG = "ClientDescription";
  protected static final String CLIENT_GROUP_NAME_TAG = "GroupName";
  protected static final String CLIENT_NAME_TAG = "ClientName";
  protected static final String CLIENT_SCENARIO_NAME_TAG = "ScenarioName";
  protected static final String CLIENT_SHELL_TAG = "ClientShell";
  protected static final String CLIENT_URI_LL_PROPERTIES = "labLinkPropertiesUrl";
  protected static final String CLIENT_URI_SYNC_PROPERTIES = "syncHostPropertiesUrl";

  // Tags for general OPC UA client setup.
  protected static final String OPCUA_CONFIG_TAG = "Config-OPC-UA";
  protected static final String OPCUA_INPUT_CONFIG_TAG = "Input";
  protected static final String OPCUA_OUTPUT_CONFIG_TAG = "Output";

  /** Flag for testing (write config and exit). */
  private static boolean writeConfigAndExitFlag;

  /** Lablink client instance. */
  protected LlClient client;

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
   * @throws org.eclipse.milo.opcua.stack.core.UaException
   *   exception from Eclipse Milo OPC UA stack implementation
   * @throws java.io.IOException
   *   IO exception error
   * @throws java.net.URISyntaxException
   *   URI syntax error
   * @throws java.util.NoSuchElementException
   *   no such element
   */
  public OpcUaClientBase(JSONObject jsonConfig) throws
      at.ac.ait.lablink.core.client.ex.ClientNotReadyException,
      at.ac.ait.lablink.core.client.ex.CommInterfaceNotSupportedException,
      at.ac.ait.lablink.core.client.ex.DataTypeNotSupportedException,
      at.ac.ait.lablink.core.client.ex.InvalidCastForServiceValueException,
      at.ac.ait.lablink.core.client.ex.NoServicesInClientLogicException,
      at.ac.ait.lablink.core.client.ex.NoSuchCommInterfaceException,
      at.ac.ait.lablink.core.client.ex.ServiceIsNotRegisteredWithClientException,
      at.ac.ait.lablink.core.client.ex.ServiceTypeDoesNotMatchClientType,
      org.apache.commons.configuration.ConfigurationException,
      org.eclipse.milo.opcua.stack.core.UaException,
      java.io.IOException,
      java.lang.Exception,
      java.util.NoSuchElementException {

    // Retrieve basic client configuration.
    JSONObject clientConfig = ConfigUtil.<JSONObject>getRequiredConfigParam(jsonConfig,
        CLIENT_CONFIG_TAG, String.format("Client configuration (JSON object with tag '%1$s') "
        + "is missing", CLIENT_CONFIG_TAG));

    // Basic Lablink client configuration.
    configureClient(clientConfig);

    // Retrieve config for OPC UA client.
    JSONObject opcuaClientConfig = ConfigUtil.<JSONObject>getRequiredConfigParam(jsonConfig,
        OPCUA_CONFIG_TAG, String.format("OPC UA client configuration (JSON object with tag "
        + "'%1$s') is missing", OPCUA_CONFIG_TAG));

    // Basic configuration the OPC UA client.
    configureOpcUaClient(opcuaClientConfig);

    // Create and connect the OPC UA client.
    createAndConnectOpcUaClient();

    // Initialize and customized the newly created OPC UA client (implemented by child class).
    initOpcUaClient(opcuaClientConfig);

    // Retrieve config for inputs.
    JSONArray inputConfigList = ConfigUtil.<JSONArray>getRequiredConfigParam(jsonConfig,
        OPCUA_INPUT_CONFIG_TAG, String.format("Lablink client input data service definitions "
        + "(JSON array with tag '%1$s') are missing", OPCUA_INPUT_CONFIG_TAG));

    // Add inputs to the client (implemented by child class).
    configureInputs(inputConfigList);

    // Retrieve config for outputs.
    JSONArray outputConfigList = ConfigUtil.<JSONArray>getRequiredConfigParam(jsonConfig,
        OPCUA_OUTPUT_CONFIG_TAG, String.format("Lablink client output data service definitions "
        + "(JSON array with tag '%1$s') are missing", OPCUA_OUTPUT_CONFIG_TAG));

    // Add outputs to the client (implemented by child class).
    configureOutputs(outputConfigList);

    // Create the Lablink client.
    client.create();

    // Initialize the Lablink client.
    client.init();

    // Start the Lablink client.
    client.start();
  }

  /**
   * Configure the OPC UA client.
   * @param opcuaClientConfig configuration data (JSON format)
   */
  protected abstract void initOpcUaClient(JSONObject opcuaClientConfig);

  /**
   * Configure the Lablink client data services, which serve as inputs for the OPC UA client.
   *
   * @param inputConfigList configuration data (JSON format)
   * @throws at.ac.ait.lablink.core.client.ex.ServiceTypeDoesNotMatchClientType
   *   service type does not match client type
   */
  protected abstract void configureInputs(JSONArray inputConfigList) throws
      at.ac.ait.lablink.core.client.ex.ServiceTypeDoesNotMatchClientType;

  /**
   * Configure the Lablink client data services, which serve as outputs for the OPC UA client.
   *
   * @param outputConfigList configuration data (JSON format)
   * @throws at.ac.ait.lablink.core.client.ex.ServiceTypeDoesNotMatchClientType
   *   service type does not match client type
   * @throws org.eclipse.milo.opcua.stack.core.UaException
   *   exception from Eclipse Milo OPC UA stack implementation
   */
  protected abstract void configureOutputs(JSONArray outputConfigList) throws
      at.ac.ait.lablink.core.client.ex.ServiceTypeDoesNotMatchClientType,
      org.eclipse.milo.opcua.stack.core.UaException;

  /**
   * Basic configuration of the Lablink client.
   *
   * @param clientConfig configuration data (JSON format)
   * @throws at.ac.ait.lablink.core.client.ex.CommInterfaceNotSupportedException
   *   comm interface not supported
   */
  protected void configureClient(JSONObject clientConfig) throws
      at.ac.ait.lablink.core.client.ex.CommInterfaceNotSupportedException {

    logger.info("Basic client configuration ...");

    // General Lablink properties configuration.
    String llPropUri = ConfigUtil.<String>getRequiredConfigParam(clientConfig,
        CLIENT_URI_LL_PROPERTIES, String.format("Lablink client configuration URI missing "
        + "(%1$s)", CLIENT_URI_LL_PROPERTIES));

    // Sync properties configuration.
    String llSyncUri = ConfigUtil.<String>getRequiredConfigParam(clientConfig,
        CLIENT_URI_SYNC_PROPERTIES, String.format("Sync host configuration URI missing "
        + "(%1$s)", CLIENT_URI_SYNC_PROPERTIES));

    // Scenario name.
    String scenarioName = ConfigUtil.<String>getRequiredConfigParam(clientConfig,
        CLIENT_SCENARIO_NAME_TAG, String.format("Scenario name missing (%1$s)",
        CLIENT_SCENARIO_NAME_TAG));

    // Group name.
    String groupName = ConfigUtil.<String>getRequiredConfigParam(clientConfig,
        CLIENT_GROUP_NAME_TAG, String.format("Group name missing (%1$s)",
        CLIENT_GROUP_NAME_TAG));

    // Client name.
    String clientName = ConfigUtil.<String>getRequiredConfigParam(clientConfig,
        CLIENT_NAME_TAG, String.format("Client name missing (%1$s)", CLIENT_NAME_TAG));

    // Client description (optional).
    String clientDesc = ConfigUtil.getOptionalConfigParam(clientConfig,
        CLIENT_DESC_TAG, clientName);

    // Activate shell (optional, default: false).
    boolean giveShell = ConfigUtil.getOptionalConfigParam(clientConfig,
        CLIENT_SHELL_TAG, false);

    boolean isPseudo = false;

    // Declare the client with required interface.
    client = new LlClient(clientName,
        MqttCommInterfaceUtility.SP_ACCESS_NAME, giveShell, isPseudo);

    // Specify client configuration (no sync host).
    MqttCommInterfaceUtility.addClientProperties(client, clientDesc,
        scenarioName, groupName, clientName, llPropUri, llSyncUri, null);
  }

  /**
   * Create a new data service for this client (input or output).
   *
   * @param serviceId name of input signal
   * @param serviceType type of data associated to service (boolean, double, long, string)
   * @param unit unit associated to input signal
   * @return reference to the new service added to the Lablink client
   * @throws at.ac.ait.lablink.core.client.ex.ServiceTypeDoesNotMatchClientType
   *   service type does not match client type
   */
  protected LlService addDataService(String serviceId, EDataServiceType serviceType, String unit)
      throws at.ac.ait.lablink.core.client.ex.ServiceTypeDoesNotMatchClientType {

    // Create new data service.
    LlService dataService = null;

    switch (serviceType) {
      case DOUBLE:
        dataService = new DataServiceDouble();
        break;
      case LONG:
        dataService = new DataServiceLong();
        break;
      case BOOLEAN:
        dataService = new DataServiceBoolean();
        break;
      case STRING:
        dataService = new DataServiceString();
        break;
      default:
        throw new IllegalArgumentException(
            String.format("Data service type not supported: '%1$d'", serviceType)
        );
    }

    // Set data service name.
    dataService.setName(serviceId);

    // Data service description.
    String serviceDesc = String.format("OPC UA client data service %1$s (%2$s)",
        serviceId, EDataServiceType.toString(serviceType));

    // Specify data service properties.
    MqttCommInterfaceUtility.addDataPointProperties(dataService,
        serviceId, serviceDesc, serviceId, unit);

    // Add service to the client.
    client.addService(dataService);
    
    return dataService;
  }

  /**
   * Parse the command line arguments to retrieve the configuration.
   *
   * @param args arguments to main method
   * @return configuration data (JSON format)
   * @throws org.apache.commons.cli.ParseException
   *   parse exception
   * @throws org.apache.commons.configuration.ConfigurationException
   *   configuration error
   * @throws org.json.simple.parser.ParseException
   *   parse error
   * @throws java.io.IOException
   *   IO error
   * @throws java.net.MalformedURLException
   *   malformed URL
   * @throws java.util.NoSuchElementException
   *   no such element
   */
  protected static JSONObject getConfig(String[] args) throws
      org.apache.commons.cli.ParseException,
      org.apache.commons.configuration.ConfigurationException,
      org.json.simple.parser.ParseException,
      java.io.IOException,
      java.net.MalformedURLException,
      java.util.NoSuchElementException {

    // Define command line option.
    Options cliOptions = new Options();
    cliOptions.addOption(CLI_CONF_FLAG, CLI_CONF_LONG_FLAG, true,
        "client configuration URI");
    cliOptions.addOption(CLI_TEST_FLAG, false,
        "write config and exit");

    // Parse command line options.
    CommandLineParser parser = new BasicParser();
    CommandLine commandLine = parser.parse(cliOptions, args);

    // Set flag for testing (write config and exit).
    writeConfigAndExitFlag = commandLine.hasOption(CLI_TEST_FLAG);

    // Retrieve configuration URI from command line.
    String configUri = commandLine.getOptionValue(CLI_CONF_FLAG);

    // Get configuration URL, resolve environment variables if necessary.
    URL fullConfigUrl = new URL(Utility.parseWithEnvironmentVariable(configUri));

    // Read configuration, remove existing comments.
    Scanner scanner = new Scanner(fullConfigUrl.openStream());
    String rawConfig = scanner.useDelimiter("\\Z").next();
    rawConfig = rawConfig.replaceAll("#.*#", "");

    // Check if comments have been removed properly.
    int still = rawConfig.length() - rawConfig.replace("#", "").length();
    if (still > 0) {
      throw new IllegalArgumentException(
          String.format("Config file contains at least %1$d line(s) with incorrectly"
              + "started/terminated comments: %2$s", still, fullConfigUrl.toString())
       );
    }

    logger.info("Parsing configuration file...");

    // Parse configuration (JSON format).
    JSONParser jsonParser = new JSONParser();
    JSONObject jsonConfig = (JSONObject) jsonParser.parse(rawConfig);

    return jsonConfig;
  }

  /**
   * Retrieve value of flag {@code writeConfigAndExitFlag} (used for testing).
   *
   * @return value of flag {@code writeConfigAndExitFlag}
   */
  public static boolean getWriteConfigAndExitFlag() {
    return writeConfigAndExitFlag;
  }

  /**
   * Returns the yellow pages info (JSON format) of the Lablink client.
   *
   * @return yellow pages
   */
  public String getYellowPageJson() {
    return this.client.getYellowPageJson();
  }

  /**
   * Get the name of this Lablink client. Implements {@link OpcUaClientRunner#getClientName()}.
   *
   * @return the client name
   */
  protected String getClientName() {
    return client.getName();
  }

  /**
   * Write a new value for the variable associated to the given node ID to the OPC UA server.
   *
   * @param ids list of node IDs of the OPC UA server variables
   * @param dvs new data values
   * @return completable future with list of status codes
   */
  public synchronized CompletableFuture<List<StatusCode>> 
      writeValue(List<NodeId> ids, List<DataValue> dvs) {
    return opcUaClient.writeValues(ids, dvs);
  }

  /**
   * Retrieve data type of an OPC UA server variable identfied by given node ID.
   *
   * @param varNodeId node ID of the OPC UA server variable
   * @return data type ID (OPC UA data type scheme)
   */
  public int getDataTypeId(NodeId varNodeId) {
    try {
      UaVariableNode varNode = opcUaClient.getAddressSpace().getVariableNode(varNodeId);
      NodeId varDataType = varNode.getDataType();
      UInteger dataTypeId = (UInteger) varDataType.getIdentifier();

      // DataTypeTree tree = DataTypeTreeBuilder.build(opcUaClient);
      // Class<?> backingClass = tree.getBackingClass(varDataType);
      // logger.info("backingClass: {}", backingClass);

      return dataTypeId.intValue();
    } catch (UaException uaex) {
      logger.info(uaex.toString());
    }
    
    return -1;
  }

  /**
   * Helper function: iterate simultaneously over two containers and perform an action
   * on the entries.
   *
   * @param <T1> c1 data type
   * @param <T2> c2 data type
   * @param c1 iterable container
   * @param c2 iterable container
   * @param consumer instance of class BiConsumer that performs an action on the container entries
   */
  protected <T1, T2> void iterateSimultaneously(
      Iterable<T1> c1, Iterable<T2> c2, BiConsumer<T1, T2> consumer) {
    Iterator<T1> i1 = c1.iterator();
    Iterator<T2> i2 = c2.iterator();
    while (i1.hasNext() && i2.hasNext()) {
      consumer.accept(i1.next(), i2.next());
    }
  }
}
