//
// Copyright (c) AIT Austrian Institute of Technology GmbH.
//
// This program and the accompanying materials are made
// available under the terms of the Eclipse Public License 2.0
// which is available at: https://www.eclipse.org/legal/epl-2.0/
//

package at.ac.ait.lablink.clients.opcuaclient.services;

import at.ac.ait.lablink.core.service.LlServiceBoolean;


/**
 * Class DataServiceBoolean.
 * 
 * <p>Data service for input/output data of type long.
 */
public class DataServiceBoolean extends LlServiceBoolean {
  /**
   * @see at.ac.ait.lablink.core.service.LlService#get()
   */
  @Override
  public Boolean get() {
    return this.getCurState();
  }

  /**
   * @see at.ac.ait.lablink.core.service.LlService#set(java.lang.Object)
   */
  @Override
  public boolean set(Boolean newVal) {
    this.setCurState(newVal);
    return true;
  }
}