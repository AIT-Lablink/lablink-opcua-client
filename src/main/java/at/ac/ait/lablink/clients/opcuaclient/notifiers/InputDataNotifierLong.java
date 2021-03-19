//
// Copyright (c) AIT Austrian Institute of Technology GmbH.
//
// This program and the accompanying materials are made
// available under the terms of the Eclipse Public License 2.0
// which is available at: https://www.eclipse.org/legal/epl-2.0/
//

package at.ac.ait.lablink.clients.opcuaclient.notifiers;

import com.google.common.collect.ImmutableList;

import at.ac.ait.lablink.clients.opcuaclient.DataTypeUtil;
import at.ac.ait.lablink.clients.opcuaclient.OpcUaClientBase;

import at.ac.ait.lablink.core.service.IServiceStateChangeNotifier;
import at.ac.ait.lablink.core.service.LlService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

/**
 * Class InputDataNotifierLong.
 */
public class InputDataNotifierLong implements IServiceStateChangeNotifier<LlService, Long> {

  private final OpcUaClientBase client;
  private final NodeId id;
  private Function<Long, Object> dataTypeCaster;

  private static final Logger logger = LogManager.getLogger("InputDataNotifierLong");

  /**
   * Constructor.
   *
   * @param opcuaClient Lablink OPC UA client
   * @param nodeId associated OPC UA node ID
   * @param dataTypeId data type ID of variable associated to node ID
   */
  public InputDataNotifierLong(OpcUaClientBase opcuaClient, NodeId nodeId, int dataTypeId) {
    client = opcuaClient;
    id = nodeId;

    switch (dataTypeId) {
      case DataTypeUtil.BOOLEAN:
        dataTypeCaster = DataTypeUtil::numberToBoolean;
        break;
      case DataTypeUtil.SBYTE:
        dataTypeCaster = DataTypeUtil::numberToUByte;
        break;
      case DataTypeUtil.BYTE:
        dataTypeCaster = DataTypeUtil::numberToByte;
        break;
      case DataTypeUtil.UINT16:
        dataTypeCaster = DataTypeUtil::numberToUShort;
        break;
      case DataTypeUtil.UINT32:
        dataTypeCaster = DataTypeUtil::numberToUInt;
        break;
      case DataTypeUtil.UINT64:
        dataTypeCaster = DataTypeUtil::numberToULong;
        break;
      case DataTypeUtil.INT16:
        dataTypeCaster = DataTypeUtil::numberToShort;
        break;
      case DataTypeUtil.INT32:
        dataTypeCaster = DataTypeUtil::numberToInt;
        break;
      case DataTypeUtil.INT64:
        dataTypeCaster = DataTypeUtil::identity;
        break;
      case DataTypeUtil.FLOAT:
        dataTypeCaster = DataTypeUtil::numberToFloat;
        break;
      case DataTypeUtil.DOUBLE:
        dataTypeCaster = DataTypeUtil::numberToDouble;
        break;
      case DataTypeUtil.STRING:
        dataTypeCaster = DataTypeUtil::objectToString;
        break;
      default:
        throw new RuntimeException(
          String.format(
              "Casting of Long to specified data type (%1$d) not supported.", dataTypeId
          )
        );
    }
  }

  /**
   * Whenever the state of the associated data service changes (i.e., a new
   * input arrives), write the corresponding value to the OPC UA server.
   */
  @Override
  public void stateChanged(LlService service, Long oldVal, Long newVal) {
    Variant var = new Variant(dataTypeCaster.apply(newVal));
    DataValue val = new DataValue(var);

    CompletableFuture<List<StatusCode>> cf = client.writeValue(
        ImmutableList.of(id), ImmutableList.of(val)
    );

    try {    
      List<StatusCode> statusCodes = cf.get();

      if (!statusCodes.get(0).isGood()) {
        logger.warn("Failed to write '{}' to nodeId = {}", val, id);
      }
    } catch (InterruptedException | ExecutionException ex) {
      logger.warn(ex);
    }
  }
}
