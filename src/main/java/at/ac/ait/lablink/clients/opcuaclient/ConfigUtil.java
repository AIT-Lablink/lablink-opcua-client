//
// Copyright (c) AIT Austrian Institute of Technology GmbH.
//
// This program and the accompanying materials are made
// available under the terms of the Eclipse Public License 2.0
// which is available at: https://www.eclipse.org/legal/epl-2.0/
//

package at.ac.ait.lablink.clients.opcuaclient;

import org.json.simple.JSONObject;

import java.util.NoSuchElementException;

/**
 * Collection of helper functions for client configuration.
 */
public class ConfigUtil {

  /**
   * Retrieve mandatory parameter from configuration. Throw an exception in case the
   * parameter is not found.
   *
   * @param <T> data type of parameter
   * @param config configuration data (JSON format)
   * @param tag JSON tag of the parameter
   * @param err error message to be displayed in case the parameter is not found
   * @return mandatory parameter from configuration
   * @throws NoSuchElementException specified element does not exist
   */
  public static <T> T getRequiredConfigParam(JSONObject config, String tag, String err)
      throws NoSuchElementException {

    if (false == config.containsKey(tag)) {
      throw new NoSuchElementException(err);
    }

    @SuppressWarnings("unchecked")
    T result = (T) config.get(tag);

    return result;
  }


  /**
   * Retrieve optional parameter from configuration. Return a default value in case the
   * parameter is not found.
   *
   * @param <T> data type of parameter
   * @param config configuration data (JSON format)
   * @param tag JSON tag of the parameter
   * @param defaultVal default value
   * @return optional parameter from configuration or default value
   */
  public static <T> T getOptionalConfigParam(JSONObject config, String tag, T defaultVal) {
    if (false == config.containsKey(tag)) {
      return defaultVal;
    }

    @SuppressWarnings("unchecked")
    T result = (T) config.get(tag);

    return result;
  }

}