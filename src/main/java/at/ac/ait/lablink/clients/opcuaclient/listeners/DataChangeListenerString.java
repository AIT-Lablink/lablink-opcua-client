//
// Copyright (c) AIT Austrian Institute of Technology GmbH.
//
// This program and the accompanying materials are made
// available under the terms of the Eclipse Public License 2.0
// which is available at: https://www.eclipse.org/legal/epl-2.0/
//

package at.ac.ait.lablink.clients.opcuaclient.listeners;

import at.ac.ait.lablink.clients.opcuaclient.DataTypeUtil;
import at.ac.ait.lablink.core.service.IImplementedService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;

import java.util.function.Function;


public class DataChangeListenerString implements IDataChangeListener {

  private IImplementedService<String> dataService;

  private String serviceName;

  private Function<Object, String> dataTypeCaster;

  private static final Logger logger = LogManager.getLogger("DataChangeListenerString");

  /**
   * Constructor.
   *
   * @param name name of the associated Lablink data service
   * @param dataTypeId data type ID of the associated OPC UA variable
   */
  public DataChangeListenerString(String name, int dataTypeId) {
    serviceName = name;
    dataService = null;

    dataTypeCaster = DataTypeUtil::objectToString;
  }

  @Override
  public String getServiceName() {
    return serviceName;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void setImplementedService(IImplementedService service) {
    dataService = (IImplementedService<String>) service;
  }

  @Override
  public void setValue(DataValue newVal) {
    // Retrieve value as Object.
    Object objectNewVal = newVal.getValue().getValue();

    if (dataService != null) {
      dataService.setValue(dataTypeCaster.apply(objectNewVal));
    } else {
      logger.warn("Lablink data service has not been set");
    }
  }
}
