//
// Copyright (c) AIT Austrian Institute of Technology GmbH.
//
// This program and the accompanying materials are made
// available under the terms of the Eclipse Public License 2.0
// which is available at: https://www.eclipse.org/legal/epl-2.0/
//

package at.ac.ait.lablink.clients.opcuaclient.listeners;

import at.ac.ait.lablink.core.service.IImplementedService;

import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;

/**
 * Implementations of this interface are supposed to provide a link between data subscriptions to
 * an OPC UA server and the Lablink data services. Whenever a subscription callback provides a new
 * value (via an instance of class DataValue), the implementation of this interface is responsible
 * for casting the associated data to the correct type and update the client's data service (via 
 * method {@link #setValue(DataValue)}).
 */
public interface IDataChangeListener {
  
  /**
   * Return the name of the associated data service.
   *
   * @return name of service
   */
  public abstract String getServiceName();

  /**
   * Provide the reference to the associated data service.
   *
   * @param service the associated data service of the Lablink client
   */
  public abstract void setImplementedService(IImplementedService service);

  /**
   * OPC UA data subscription callbacks should call this method whenever a new value is available.
   * Implementations of this method are responsible for casting the associated data to the correct 
   * type and update the client's data service (set via method 
   * {@link #setImplementedService(IImplementedService)}.
   *
   * @param newVal new data value
   */
  public abstract void setValue(DataValue newVal);
}
