//
// Copyright (c) AIT Austrian Institute of Technology GmbH.
//
// This program and the accompanying materials are made
// available under the terms of the Eclipse Public License 2.0
// which is available at: https://www.eclipse.org/legal/epl-2.0/
//

package at.ac.ait.lablink.clients.opcuaclient;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.api.identity.IdentityProvider;
// import org.eclipse.milo.opcua.sdk.client.api.identity.UsernameProvider;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;

import org.json.simple.JSONObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Security;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;

/**
 *  Class OpcUaClientRunner
 *
 *  <p>Basic functionality for configuring and running OPC UA clients.
 */
public abstract class OpcUaClientRunner {

  static {
    // Required for SecurityPolicy.Aes256_Sha256_RsaPss
    Security.addProvider(new BouncyCastleProvider());
  }

  // Tags for general OPC UA client setup.
  protected static final String OPCUA_ENDPOINT_URL_TAG = "EndpointURL";
  // protected static final String OPCUA_NAMESPACE_INDEX_TAG = "NamespaceIndex";
  protected static final String OPCUA_NAMESPACE_URI_TAG = "NamespaceURI";
  protected static final String OPCUA_CLIENT_URI_TAG = "ClientURI";

  /** Logger. */
  protected static final Logger logger = LogManager.getLogger("OpcUaClientRunner");

  /** OPC UA client. */
  protected OpcUaClient opcUaClient;

  /** Handle for thread synchronization. **/
  private final CompletableFuture<OpcUaClient> future = new CompletableFuture<>();

  /** Endpoint URL of OPC UA server. */
  private String endpointUrl;

  /** Namespace index. */
  private Long namespaceIndex;
  
  /** Namespace URI. */
  private String namespaceUri;

  /** Client URI. */
  private String clientUri;

  /**
   * Start the event loop of the Lablink client.
   *
   * @throws Exception exception error
   */
  protected abstract void startEventLoop() throws Exception;

  /**
   * Get the name of the Lablink client.
   *
   * @return name of Lablink client
   */
  protected abstract String getClientName();

  /**
   * Configure the OPC UA client.
   *
   * @param opcuaClientConfig OPC UA client configuration data (JSON format)
   * @throws java.io.IOException IO exception error
   */
  protected void configureOpcUaClient(JSONObject opcuaClientConfig) throws
      java.io.IOException {
    // Retrieve OPC UA server endpoint URL.
    endpointUrl = ConfigUtil.<String>getRequiredConfigParam(
        opcuaClientConfig, OPCUA_ENDPOINT_URL_TAG,
        String.format("OPC UA server endpoint URL ('%1$s') is missing", OPCUA_ENDPOINT_URL_TAG));

    // // Retrieve OPC UA namespace index.
    // namespaceIndex = ConfigUtil.<Long>getRequiredConfigParam(
    //     opcuaClientConfig, OPCUA_NAMESPACE_INDEX_TAG,
    //     String.format("OPC UA namespace index ('%1$s') is missing", OPCUA_NAMESPACE_INDEX_TAG));

    // Retrieve OPC UA namespace index.
    namespaceUri = ConfigUtil.<String>getRequiredConfigParam(
        opcuaClientConfig, OPCUA_NAMESPACE_URI_TAG,
        String.format("OPC UA namespace URI ('%1$s') is missing", OPCUA_NAMESPACE_URI_TAG));

    // Retrieve OPC UA client URI.
    clientUri = ConfigUtil.<String>getRequiredConfigParam(
        opcuaClientConfig, OPCUA_CLIENT_URI_TAG,
        String.format("OPC UC client URI ('%1$s') is missing", OPCUA_CLIENT_URI_TAG));
  }

  /**
   * Create the OPC UA client.
   *
   * @throws java.io.IOException
   *   IO exception error
   */
  protected void createAndConnectOpcUaClient() throws
      Exception {
    Path securityTempDir = Paths.get(System.getProperty("java.io.tmpdir"), "security");
    Files.createDirectories(securityTempDir);
    if (!Files.exists(securityTempDir)) {
      throw new RuntimeException("unable to create security dir: " + securityTempDir);
    }

    KeyStoreLoader loader = new KeyStoreLoader().load(securityTempDir);

    opcUaClient = OpcUaClient.create(
      this.getEndpointUrl(),
      endpoints ->
          endpoints.stream()
              .filter(this.endpointFilter())
              .findFirst(),
      configBuilder ->
          configBuilder
              .setApplicationName(LocalizedText.english(getClientName()))
              .setApplicationUri(getClientUri())
              .setCertificate(loader.getClientCertificate())
              .setKeyPair(loader.getClientKeyPair())
              .setIdentityProvider(this.getIdentityProvider())
              .setRequestTimeout(uint(10000))
              .build()
    );
    
    opcUaClient.connect().get();
    
    retrieveNamespaceIndexFromServer();
  }

  /**
   * Retrieve the namespace index from the OPC UA server.
   *
   * @throws ExecutionException execution exception
   * @throws InterruptedException interrupted exception
   * @throws RuntimeException specified namespace URI was not found on the server
   */
  private void retrieveNamespaceIndexFromServer() throws 
      ExecutionException, InterruptedException, RuntimeException {
    CompletableFuture<DataValue> future = opcUaClient.readValue(0, TimestampsToReturn.Neither,
        Identifiers.Server_NamespaceArray);

    DataValue value = future.get();

    final String[] namespaces = (String[]) value.getValue().getValue();

    namespaceIndex = Long.valueOf(-1);

    for (int i = 0; i < namespaces.length; i++) {
      if (namespaces[i].equals(namespaceUri)) {
        namespaceIndex = Long.valueOf(i); 
      }
    }

    if (namespaceIndex == -1) {
      throw new RuntimeException(
        String.format("Namespace URI not found: %1$s", namespaceUri)
      );
    }
  }

  /**
   * Run the OPC UA client.
   */
  protected void runClient() {
    try {
      // opcUaClient = createClient();

      future.whenCompleteAsync((cc, ex1) -> {
        if (ex1 != null) {
          logger.error("Error running client: {}", ex1.getMessage(), ex1);
        }

        try {
          opcUaClient.disconnect().get();
          Stack.releaseSharedResources();
        } catch (InterruptedException | ExecutionException ex2) {
          logger.error("Error disconnecting: {}", ex2.getMessage(), ex2);
        }

        try {
          Thread.sleep(1000);
          System.exit(0);
        } catch (InterruptedException ex3) {
          ex3.printStackTrace();
        }
      });

      try {
        this.startEventLoop();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
          shutdownHook();
          future.complete(opcUaClient);
        }));

        future.get();

      } catch (Throwable th) {
        logger.error("Error running client: {}", th.getMessage(), th);
        future.completeExceptionally(th);
      }
    } catch (Throwable th) {
      logger.error("Error getting client: {}", th.getMessage(), th);
      future.completeExceptionally(th);
    }
  }

  protected String getClientUri() {
    return clientUri;
  }

  protected String getEndpointUrl() {
    return endpointUrl;
  }

  protected int getNamespaceIndex() {
    return namespaceIndex.intValue();
  }

  protected Predicate<EndpointDescription> endpointFilter() {
    return ef -> true;  // FIXME: make configurable via configureOpcUaClient(...)
  }

  protected SecurityPolicy getSecurityPolicy() {
    return SecurityPolicy.None; // FIXME: make configurable via configureOpcUaClient(...)
  }

  protected IdentityProvider getIdentityProvider() {
    return new AnonymousProvider(); // FIXME: make configurable via configureOpcUaClient(...)
    //return new UsernameProvider("user", "password");
  }

  protected void shutdownHook() {
    logger.info("execute default shutdown hook");
  }
}
