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


public class DataChangeListenerDouble implements IDataChangeListener {

  private IImplementedService<Double> dataService;

  private String serviceName;

  private Function<Object, Double> dataTypeCaster;

  private static final Logger logger = LogManager.getLogger("DataChangeListenerDouble");

  /**
   * Constructor.
   *
   * @param name name of the associated Lablink data service
   * @param dataTypeId data type ID of the associated OPC UA variable
   */
  public DataChangeListenerDouble(String name, int dataTypeId) {
    serviceName = name;
    dataService = null;

    switch (dataTypeId) {
      case DataTypeUtil.BOOLEAN:
        dataTypeCaster = DataTypeUtil::booleanToDouble;
        break;
      case DataTypeUtil.SBYTE:
      case DataTypeUtil.BYTE:
      case DataTypeUtil.UINT16:
      case DataTypeUtil.UINT32:
      case DataTypeUtil.UINT64:
      case DataTypeUtil.INT16:
      case DataTypeUtil.INT32:
      case DataTypeUtil.INT64:
      case DataTypeUtil.FLOAT:
      case DataTypeUtil.DOUBLE:
        dataTypeCaster = DataTypeUtil::numberToDouble;
        break;
      case DataTypeUtil.STRING:
        dataTypeCaster = DataTypeUtil::stringToDouble;
        break;
      default:
        throw new RuntimeException(
          String.format(
              "Casting of Double to specified data type (%1$d) not supported.", dataTypeId
          )
        );
    }
  }

  @Override
  public String getServiceName() {
    return serviceName;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void setImplementedService(IImplementedService service) {
    dataService = (IImplementedService<Double>) service;
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
