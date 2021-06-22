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

/**
 * Helper class with conversion routines for data types.
 */
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

  /**
   * No data type conversion, just return the object itself.
   * @param obj data object
   * @return same object
   */
  public static Object identity(Object obj) {
    return obj;
  }

  /**
   * Convert object to boolean.
   * @param obj data object
   * @return converted value 
   */
  public static Boolean numberToBoolean(Object obj) {
    return (((Number) obj).intValue() != 0);
  }

  /**
   * Convert number to boolean.
   * @param num number
   * @return converted value 
   */
  public static Boolean numberToBoolean(Number num) {
    return (num.intValue() != 0);
  }

  /**
   * Convert number to byte.
   * @param num number
   * @return converted value 
   */
  public static Byte numberToByte(Number num) {
    return num.byteValue();
  }

  /**
   * Convert number to unsigned byte.
   * @param num number
   * @return converted value 
   */
  public static UByte numberToUByte(Number num) {
    return ubyte(num.byteValue());
  }

  /**
   * Convert number to short.
   * @param num number
   * @return converted value 
   */
  public static Short numberToShort(Number num) {
    return num.shortValue();
  }

  /**
   * Convert number to unsigned short.
   * @param num number
   * @return converted value 
   */
  public static UShort numberToUShort(Number num) {
    return ushort(num.shortValue());
  }

  /**
   * Convert number to integer.
   * @param num number
   * @return converted value 
   */
  public static Integer numberToInt(Number num) {
    return num.intValue();
  }

  /**
   * Convert number to unsigned integer.
   * @param num number
   * @return converted value 
   */
  public static UInteger numberToUInt(Number num) {
    return uint(num.intValue());
  }

  /**
   * Convert object to long.
   * @param obj data object
   * @return converted value 
   */
  public static Long numberToLong(Object obj) {
    return ((Number) obj).longValue();
  }

  /**
   * Convert number to long.
   * @param num number
   * @return converted value 
   */
  public static Long numberToLong(Number num) {
    return num.longValue();
  }

  /**
   * Convert number to unsigned long.
   * @param num number
   * @return converted value 
   */
  public static ULong numberToULong(Number num) {
    return ulong(num.longValue());
  }

  /**
   * Convert number to float.
   * @param num number
   * @return converted value 
   */
  public static Float numberToFloat(Number num) {
    return num.floatValue();
  }

  /**
   * Convert object to double.
   * @param obj data object
   * @return converted value 
   */
  public static Double numberToDouble(Object obj) {
    return ((Number) obj).doubleValue();
  }

  /**
   * Convert number to double.
   * @param num number
   * @return converted value 
   */
  public static Double numberToDouble(Number num) {
    return num.doubleValue();
  }

  /**
   * Convert object to boolean.
   * @param obj data object
   * @return converted value 
   */
  public static Boolean booleanToBoolean(Object obj) {
    return (Boolean) obj;
  }

  /**
   * Convert boolean to byte.
   * @param bool boolean
   * @return converted value 
   */
  public static Byte booleanToByte(Boolean bool) {
    return (bool == true) ? (byte) 1 : (byte) 0;
  }

  /**
   * Convert boolean to unsigned byte.
   * @param bool boolean
   * @return converted value 
   */
  public static UByte booleanToUByte(Boolean bool) {
    return (bool == true) ? ubyte(1) : ubyte(0);
  }

  /**
   * Convert boolean to short.
   * @param bool boolean
   * @return converted value 
   */
  public static Short booleanToShort(Boolean bool) {
    return (bool == true) ? (short) 1 : (short) 0;
  }

  /**
   * Convert boolean to unsigned short.
   * @param bool boolean
   * @return converted value 
   */
  public static UShort booleanToUShort(Boolean bool) {
    return (bool == true) ? ushort(1) : ushort(0);
  }

  /**
   * Convert boolean to integer.
   * @param bool boolean
   * @return converted value 
   */
  public static Integer booleanToInt(Boolean bool) {
    return (bool == true) ? (int) 1 : (int) 0;
  }

  /**
   * Convert boolean to unsigned integer.
   * @param bool boolean
   * @return converted value 
   */
  public static UInteger booleanToUInt(Boolean bool) {
    return (bool == true) ? uint(1) : uint(0);
  }

  /**
   * Convert object to long.
   * @param obj data object
   * @return converted value 
   */
  public static Long booleanToLong(Object obj) {
    return ((Boolean) obj == true) ? (long) 1 : (long) 0;
  }

  /**
   * Convert boolean to long.
   * @param bool boolean
   * @return converted value 
   */
  public static Long booleanToLong(Boolean bool) {
    return (bool == true) ? (long) 1 : (long) 0;
  }

  /**
   * Convert boolean to unsigned long.
   * @param bool boolean
   * @return converted value 
   */
  public static ULong booleanToULong(Boolean bool) {
    return (bool == true) ? ulong(1) : ulong(0);
  }

  /**
   * Convert boolean to float.
   * @param bool boolean
   * @return converted value 
   */
  public static Float booleanToFloat(Boolean bool) {
    return (bool == true) ? 1.f : 0.f;
  }

  /**
   * Convert object to double.
   * @param obj data object
   * @return converted value 
   */
  public static Double booleanToDouble(Object obj) {
    return ((Boolean) obj == true) ? 1.d : 0.d;
  }

  /**
   * Convert boolean to double.
   * @param bool boolean
   * @return converted value 
   */
  public static Double booleanToDouble(Boolean bool) {
    return (bool == true) ? 1.d : 0.d;
  }

  /**
   * Convert object to boolean.
   * @param obj data object
   * @return converted value 
   */
  public static Boolean stringToBoolean(Object obj) {
    return Boolean.valueOf((String) obj);
  }

  /**
   * Convert string to boolean.
   * @param str string
   * @return converted value 
   */
  public static Boolean stringToBoolean(String str) {
    return Boolean.valueOf(str);
  }

  /**
   * Convert string to unsigned byte.
   * @param str string
   * @return converted value 
   */
  public static UByte stringToUByte(String str) {
    try {
      return ubyte(Byte.valueOf(str));
    } catch (NumberFormatException ex) {
      // The string probably contains a decimal point, try to convert to double first.
      return ubyte(Double.valueOf(str).byteValue());
    }
  }

  /**
   * Convert string to byte.
   * @param str string
   * @return converted value 
   */
  public static Byte stringToByte(String str) {
    try {
      return Byte.valueOf(str);
    } catch (NumberFormatException ex) {
      // The string probably contains a decimal point, try to convert to double first.
      return Byte.valueOf(Double.valueOf(str).byteValue());
    }
  }

  /**
   * Convert string to unsigned short.
   * @param str string
   * @return converted value 
   */
  public static UShort stringToUShort(String str) {
    try {
      return ushort(Short.valueOf(str));
    } catch (NumberFormatException ex) {
      // The string probably contains a decimal point, try to convert to double first.
      return ushort(Double.valueOf(str).shortValue());
    }
  }

  /**
   * Convert string to short.
   * @param str string
   * @return converted value 
   */
  public static Short stringToShort(String str) {
    try {
      return Short.valueOf(str);
    } catch (NumberFormatException ex) {
      // The string probably contains a decimal point, try to convert to double first.
      return Short.valueOf(Double.valueOf(str).shortValue());
    }
  }

  /**
   * Convert string to unsigned integer.
   * @param str string
   * @return converted value 
   */
  public static UInteger stringToUInt(String str) {
    try {
      return uint(Integer.valueOf(str));
    } catch (NumberFormatException ex) {
      // The string probably contains a decimal point, try to convert to double first.
      return uint(Double.valueOf(str).intValue());
    }
  }

  /**
   * Convert string to integer.
   * @param str string
   * @return converted value 
   */
  public static Integer stringToInt(String str) {
    try {
      return Integer.valueOf(str);
    } catch (NumberFormatException ex) {
      // The string probably contains a decimal point, try to convert to double first.
      return Integer.valueOf(Double.valueOf(str).intValue());
    }
  }

  /**
   * Convert string to unsigned long.
   * @param str string
   * @return converted value 
   */
  public static ULong stringToULong(String str) {
    try {
      return ulong(Long.valueOf(str));
    } catch (NumberFormatException ex) {
      // The string probably contains a decimal point, try to convert to double first.
      return ulong(Double.valueOf(str).longValue());
    }
  }

  /**
   * Convert object to long.
   * @param obj data object
   * @return converted value 
   */
  public static Long stringToLong(Object obj) {
    try {
      return Long.valueOf((String) obj);
    } catch (NumberFormatException ex) {
      // The string probably contains a decimal point, try to convert to double first.
      return Long.valueOf(Double.valueOf((String) obj).longValue());
    }
  }

  /**
   * Convert string to long.
   * @param str string
   * @return converted value 
   */
  public static Long stringToLong(String str) {
    try {
      return Long.valueOf(str);
    } catch (NumberFormatException ex) {
      // The string probably contains a decimal point, try to convert to double first.
      return Long.valueOf(Double.valueOf(str).longValue());
    }
  }

  /**
   * Convert string to float.
   * @param str string
   * @return converted value 
   */
  public static Float stringToFloat(String str) {
    try {
      return Float.valueOf(str);
    } catch (NumberFormatException ex) {
      // The string probably contains a decimal point, try to convert to double first.
      return Float.valueOf(Double.valueOf(str).floatValue());
    }
  }

  /**
   * Convert object to double.
   * @param obj data object
   * @return converted value 
   */
  public static Double stringToDouble(Object obj) {
    return Double.valueOf((String) obj);
  }

  /**
   * Convert string to double.
   * @param str string
   * @return converted value 
   */
  public static Double stringToDouble(String str) {
    return Double.valueOf(str);
  }

  /**
   * Convert object to string.
   * @param obj data object
   * @return converted value 
   */
  public static String objectToString(Object obj) {
    return obj.toString();
  }
}
