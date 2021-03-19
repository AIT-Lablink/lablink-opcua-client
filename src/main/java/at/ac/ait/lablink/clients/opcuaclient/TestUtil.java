//
// Copyright (c) AIT Austrian Institute of Technology GmbH.
//
// This program and the accompanying materials are made
// available under the terms of the Eclipse Public License 2.0
// which is available at: https://www.eclipse.org/legal/epl-2.0/
//

package at.ac.ait.lablink.clients.opcuaclient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * Collection of helper functions for testing.
 */
public class TestUtil {

  /**
   * Run a test (write config and exit).
   *
   * @param client Lablink client
   */
  public static void writeConfigAndExit(OpcUaClientBase client) {

    String clientConfig = client.getYellowPageJson();

    try {
      Files.write(Paths.get("client_config.json"), clientConfig.getBytes());
    } catch (IOException ex) {
      System.exit(1);
    }

    System.exit(0);
  } 
  
}