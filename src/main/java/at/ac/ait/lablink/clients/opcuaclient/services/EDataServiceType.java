//
// Copyright (c) AIT Austrian Institute of Technology GmbH.
//
// This program and the accompanying materials are made
// available under the terms of the Eclipse Public License 2.0
// which is available at: https://www.eclipse.org/legal/epl-2.0/
//

package at.ac.ait.lablink.clients.opcuaclient.services;

/**
 * Enumeration of data service types.
 */
public enum EDataServiceType {

  BOOLEAN,
  DOUBLE,
  LONG,
  STRING,
  UNKNOWN;

  /**
   * Map service type label (string) to data service type (enum).
   * @param strServiceType service type label (string)
   * @return data service type (enum)
   */
  public static EDataServiceType fromString(String strServiceType) {
    if (strServiceType.toLowerCase().equals("boolean")) {
      return BOOLEAN;
    } else if (strServiceType.toLowerCase().equals("double")) {
      return DOUBLE;
    } else if (strServiceType.toLowerCase().equals("long")) {
      return LONG;
    } else if (strServiceType.toLowerCase().equals("string")) {
      return STRING;
    }
    
    return UNKNOWN;
  }

  /**
   * Map data service type (enum) to service type label (string).
   * @param serviceType data service type (enum)
   * @return service type label (string)
   */
  public static String toString(EDataServiceType serviceType) {
    switch (serviceType) {
      case BOOLEAN:
        return new String("boolean");
      case DOUBLE:
        return new String("double");
      case LONG:
        return new String("long");
      case STRING:
        return new String("string");
      default:
        break;
    }
    
    return new String("unknown");
  }
}