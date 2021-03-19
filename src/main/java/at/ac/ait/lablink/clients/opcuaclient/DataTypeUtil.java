//
// Copyright (c) AIT Austrian Institute of Technology GmbH.
//
// This program and the accompanying materials are made
// available under the terms of the Eclipse Public License 2.0
// which is available at: https://www.eclipse.org/legal/epl-2.0/
//

package at.ac.ait.lablink.clients.opcuaclient;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import org.eclipse.milo.opcua.stack.core.BuiltinDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

public class DataTypeUtil {

  public static final int BOOLEAN = 1;
  public static final int SBYTE = 2;
  public static final int BYTE = 3;
  public static final int INT16 = 4;
  public static final int UINT16 = 5;
  public static final int INT32 = 6;
  public static final int UINT32 = 7;
  public static final int INT64 = 8;
  public static final int UINT64 = 9;
  public static final int FLOAT = 10;
  public static final int DOUBLE = 11;
  public static final int STRING = 12;

  public static Object identity(Object obj) {
    return obj;
  }

  public static Boolean numberToBoolean(Object obj) {
    return (((Number) obj).intValue() != 0);
  }

  public static Boolean numberToBoolean(Number num) {
    return (num.intValue() != 0);
  }

  public static Byte numberToByte(Number num) {
    return num.byteValue();
  }

  public static UByte numberToUByte(Number num) {
    return ubyte(num.byteValue());
  }

  public static Short numberToShort(Number num) {
    return num.shortValue();
  }

  public static UShort numberToUShort(Number num) {
    return ushort(num.shortValue());
  }

  public static Integer numberToInt(Number num) {
    return num.intValue();
  }

  public static UInteger numberToUInt(Number num) {
    return uint(num.intValue());
  }

  public static Long numberToLong(Object obj) {
    return ((Number) obj).longValue();
  }

  public static Long numberToLong(Number num) {
    return num.longValue();
  }

  public static ULong numberToULong(Number num) {
    return ulong(num.longValue());
  }

  public static Float numberToFloat(Number num) {
    return num.floatValue();
  }

  public static Double numberToDouble(Object obj) {
    return ((Number) obj).doubleValue();
  }

  public static Double numberToDouble(Number num) {
    return num.doubleValue();
  }

  public static Boolean booleanToBoolean(Object obj) {
    return (Boolean) obj;
  }

  public static Byte booleanToByte(Boolean bool) {
    return (bool == true) ? (byte) 1 : (byte) 0;
  }

  public static UByte booleanToUByte(Boolean bool) {
    return (bool == true) ? ubyte(1) : ubyte(0);
  }

  public static Short booleanToShort(Boolean bool) {
    return (bool == true) ? (short) 1 : (short) 0;
  }

  public static UShort booleanToUShort(Boolean bool) {
    return (bool == true) ? ushort(1) : ushort(0);
  }

  public static Integer booleanToInt(Boolean bool) {
    return (bool == true) ? (int) 1 : (int) 0;
  }

  public static UInteger booleanToUInt(Boolean bool) {
    return (bool == true) ? uint(1) : uint(0);
  }

  public static Long booleanToLong(Object obj) {
    return ((Boolean) obj == true) ? (long) 1 : (long) 0;
  }

  public static Long booleanToLong(Boolean bool) {
    return (bool == true) ? (long) 1 : (long) 0;
  }

  public static ULong booleanToULong(Boolean bool) {
    return (bool == true) ? ulong(1) : ulong(0);
  }

  public static Float booleanToFloat(Boolean bool) {
    return (bool == true) ? 1.f : 0.f;
  }

  public static Double booleanToDouble(Object obj) {
    return ((Boolean) obj == true) ? 1.d : 0.d;
  }

  public static Double booleanToDouble(Boolean bool) {
    return (bool == true) ? 1.d : 0.d;
  }

  public static Boolean stringToBoolean(Object obj) {
    return Boolean.valueOf((String) obj);
  }

  public static Boolean stringToBoolean(String str) {
    return Boolean.valueOf(str);
  }

  public static Double stringToDouble(Object obj) {
    return Double.valueOf((String) obj);
  }

  public static Double stringToDouble(String str) {
    return Double.valueOf(str);
  }

  public static Long stringToLong(Object obj) {
    return Long.valueOf((String) obj);
  }

  public static Long stringToLong(String str) {
    return Long.valueOf(str);
  }

  public static String objectToString(Object obj) {
    return obj.toString();
  }
}
