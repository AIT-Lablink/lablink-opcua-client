//
// Copyright (c) AIT Austrian Institute of Technology GmbH.
//
// This program and the accompanying materials are made
// available under the terms of the Eclipse Public License 2.0
// which is available at: https://www.eclipse.org/legal/epl-2.0/
//

package at.ac.ait.lablink.clients.opcuaclient.services;

import at.ac.ait.lablink.core.service.LlServiceDouble;


/**
 * Class DataServiceDouble.
 * 
 * <p>Data service for input/output data of type double.
 */
public class DataServiceDouble extends LlServiceDouble {
  /**
   * @see at.ac.ait.lablink.core.service.LlService#get()
   */
  @Override
  public Double get() {
    return this.getCurState();
  }

  /**
   * @see at.ac.ait.lablink.core.service.LlService#set(java.lang.Object)
   */
  @Override
  public boolean set(Double newVal) {
    this.setCurState(newVal);
    return true;
  }
}