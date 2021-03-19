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

import static org.junit.Assert.assertEquals;

import org.eclipse.milo.opcua.stack.core.BuiltinDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for class DataTypeUtil.
 */
public class DataTypeUtilTest {

  @Test
  public void numberObjectToBoolean_test() {
    Long num1 = 0L;
    assertEquals(false, DataTypeUtil.numberToBoolean((Object) num1));

    Float num2 = 1.0f;
    assertEquals(true, DataTypeUtil.numberToBoolean((Object) num2));

    Double num3 = 0.5d;
    assertEquals(false, DataTypeUtil.numberToBoolean((Object) num3));
  }

  @Test
  public void numberToBoolean_test() {
    Long num1 = 0L;
    assertEquals(false, DataTypeUtil.numberToBoolean(num1));

    Float num2 = 1.0f;
    assertEquals(true, DataTypeUtil.numberToBoolean(num2));

    Double num3 = 0.5d;
    assertEquals(false, DataTypeUtil.numberToBoolean(num3));
  }

  @Test
  public void numberToByte_test() {
    Long num1 = 0L;
    assertEquals(Byte.valueOf((byte) 0), DataTypeUtil.numberToByte(num1));

    Float num2 = -1.0f;
    assertEquals(Byte.valueOf((byte) -1), DataTypeUtil.numberToByte(num2));

    Double num3 = 0.5d;
    assertEquals(Byte.valueOf((byte) 0), DataTypeUtil.numberToByte(num3));
  }

  @Test
  public void numberToUByte_test() {
    Long num1 = 0L;
    assertEquals(ubyte(0), DataTypeUtil.numberToUByte(num1));

    Float num2 = 1.0f;
    assertEquals(ubyte(1), DataTypeUtil.numberToUByte(num2));

    Double num3 = 0.5d;
    assertEquals(ubyte(0), DataTypeUtil.numberToUByte(num3));
  }

  @Test
  public void numberToShort_test() {
    Long num1 = 0L;
    assertEquals(Short.valueOf((short) 0), DataTypeUtil.numberToShort(num1));

    Float num2 = -1.0f;
    assertEquals(Short.valueOf((short) -1), DataTypeUtil.numberToShort(num2));

    Double num3 = 0.5d;
    assertEquals(Short.valueOf((short) 0), DataTypeUtil.numberToShort(num3));
  }

  @Test
  public void numberToUShort_test() {
    Long num1 = 0L;
    assertEquals(ushort(0), DataTypeUtil.numberToUShort(num1));

    Float num2 = 1.0f;
    assertEquals(ushort(1), DataTypeUtil.numberToUShort(num2));

    Double num3 = 0.5d;
    assertEquals(ushort(0), DataTypeUtil.numberToUShort(num3));
  }

  @Test
  public void numberToInt_test() {
    Long num1 = 0L;
    assertEquals(Integer.valueOf(0), DataTypeUtil.numberToInt(num1));

    Float num2 = -1.0f;
    assertEquals(Integer.valueOf(-1), DataTypeUtil.numberToInt(num2));

    Double num3 = 0.5d;
    assertEquals(Integer.valueOf(0), DataTypeUtil.numberToInt(num3));
  }

  @Test
  public void numberToUInt_test() {
    Long num1 = 0L;
    assertEquals(uint(0), DataTypeUtil.numberToUInt(num1));

    Float num2 = -1.0f;
    assertEquals(uint(-1), DataTypeUtil.numberToUInt(num2));

    Double num3 = 0.5d;
    assertEquals(uint(0), DataTypeUtil.numberToUInt(num3));
  }

  @Test
  public void numberObjectToLong_test() {
    Long num1 = 0L;
    assertEquals(Long.valueOf(0L), DataTypeUtil.numberToLong((Object) num1));

    Float num2 = -1.0f;
    assertEquals(Long.valueOf(-1L), DataTypeUtil.numberToLong((Object) num2));

    Double num3 = 0.5d;
    assertEquals(Long.valueOf(0L), DataTypeUtil.numberToLong((Object) num3));
  }

  @Test
  public void numberToLong_test() {
    Long num1 = 0L;
    assertEquals(Long.valueOf(0L), DataTypeUtil.numberToLong(num1));

    Float num2 = -1.0f;
    assertEquals(Long.valueOf(-1L), DataTypeUtil.numberToLong(num2));

    Double num3 = 0.5d;
    assertEquals(Long.valueOf(0L), DataTypeUtil.numberToLong(num3));
  }

  @Test
  public void numberToULong_test() {
    Long num1 = 0L;
    assertEquals(ulong(0), DataTypeUtil.numberToULong(num1));

    Float num2 = -1.0f;
    assertEquals(ulong(-1), DataTypeUtil.numberToULong(num2));

    Double num3 = 0.5d;
    assertEquals(ulong(0), DataTypeUtil.numberToULong(num3));
  }

  @Test
  public void numberToFloat_test() {
    Long num1 = 0L;
    assertEquals(Float.valueOf(0f), DataTypeUtil.numberToFloat(num1));

    Float num2 = -1.0f;
    assertEquals(Float.valueOf(-1f), DataTypeUtil.numberToFloat(num2));

    Double num3 = 0.5d;
    assertEquals(Float.valueOf(0.5f), DataTypeUtil.numberToFloat(num3));
  }

  @Test
  public void numberObjectToDouble_test() {
    Long num1 = 0L;
    assertEquals(Double.valueOf(0d), DataTypeUtil.numberToDouble((Object) num1));

    Float num2 = -1.0f;
    assertEquals(Double.valueOf(-1d), DataTypeUtil.numberToDouble((Object) num2));

    Double num3 = 0.5d;
    assertEquals(Double.valueOf(0.5d), DataTypeUtil.numberToDouble((Object) num3));
  }

  @Test
  public void numberToDouble_test() {
    Long num1 = 0L;
    assertEquals(Double.valueOf(0d), DataTypeUtil.numberToDouble(num1));

    Float num2 = -1.0f;
    assertEquals(Double.valueOf(-1d), DataTypeUtil.numberToDouble(num2));

    Double num3 = 0.5d;
    assertEquals(Double.valueOf(0.5d), DataTypeUtil.numberToDouble(num3));
  }

  @Test
  public void booleanToBoolean_test() {
    Boolean bool1 = true;
    assertEquals(true, DataTypeUtil.booleanToBoolean(bool1));

    Boolean bool2 = false;
    assertEquals(false, DataTypeUtil.booleanToBoolean(bool2));
  }

  @Test
  public void booleanToByte_test() {
    Boolean bool1 = true;
    assertEquals(Byte.valueOf((byte) 1), DataTypeUtil.booleanToByte(bool1));

    Boolean bool2 = false;
    assertEquals(Byte.valueOf((byte) 0), DataTypeUtil.booleanToByte(bool2));
  }

  @Test
  public void booleanToUByte_test() {
    Boolean bool1 = true;
    assertEquals(ubyte(1), DataTypeUtil.booleanToUByte(bool1));

    Boolean bool2 = false;
    assertEquals(ubyte(0), DataTypeUtil.booleanToUByte(bool2));
  }

  @Test
  public void booleanToShort_test() {
    Boolean bool1 = true;
    assertEquals(Short.valueOf((short) 1), DataTypeUtil.booleanToShort(bool1));

    Boolean bool2 = false;
    assertEquals(Short.valueOf((short) 0), DataTypeUtil.booleanToShort(bool2));
  }

  @Test
  public void booleanToUShort_test() {
    Boolean bool1 = true;
    assertEquals(ushort(1), DataTypeUtil.booleanToUShort(bool1));

    Boolean bool2 = false;
    assertEquals(ushort(0), DataTypeUtil.booleanToUShort(bool2));
  }

  @Test
  public void booleanToInt_test() {
    Boolean bool1 = true;
    assertEquals(Integer.valueOf(1), DataTypeUtil.booleanToInt(bool1));

    Boolean bool2 = false;
    assertEquals(Integer.valueOf(0), DataTypeUtil.booleanToInt(bool2));
  }

  @Test
  public void booleanToUInt_test() {
    Boolean bool1 = true;
    assertEquals(uint(1), DataTypeUtil.booleanToUInt(bool1));

    Boolean bool2 = false;
    assertEquals(uint(0), DataTypeUtil.booleanToUInt(bool2));
  }

  @Test
  public void booleanObjectToLong_test() {
    Boolean bool1 = true;
    assertEquals(Long.valueOf(1L), DataTypeUtil.booleanToLong((Object) bool1));

    Boolean bool2 = false;
    assertEquals(Long.valueOf(0L), DataTypeUtil.booleanToLong((Object) bool2));
  }

  @Test
  public void booleanToLong_test() {
    Boolean bool1 = true;
    assertEquals(Long.valueOf(1L), DataTypeUtil.booleanToLong(bool1));

    Boolean bool2 = false;
    assertEquals(Long.valueOf(0L), DataTypeUtil.booleanToLong(bool2));
  }

  @Test
  public void booleanToULong_test() {
    Boolean bool1 = true;
    assertEquals(ulong(1L), DataTypeUtil.booleanToULong(bool1));

    Boolean bool2 = false;
    assertEquals(ulong(0L), DataTypeUtil.booleanToULong(bool2));
  }

  @Test
  public void booleanToFloat_test() {
    Boolean bool1 = true;
    assertEquals(Float.valueOf(1f), DataTypeUtil.booleanToFloat(bool1));

    Boolean bool2 = false;
    assertEquals(Float.valueOf(0f), DataTypeUtil.booleanToFloat(bool2));
  }

  @Test
  public void booleanObjectToDouble_test() {
    Boolean bool1 = true;
    assertEquals(Double.valueOf(1d), DataTypeUtil.booleanToDouble((Object) bool1));

    Boolean bool2 = false;
    assertEquals(Double.valueOf(0d), DataTypeUtil.booleanToDouble((Object) bool2));
  }

  @Test
  public void booleanToDouble_test() {
    Boolean bool1 = true;
    assertEquals(Double.valueOf(1d), DataTypeUtil.booleanToDouble(bool1));

    Boolean bool2 = false;
    assertEquals(Double.valueOf(0d), DataTypeUtil.booleanToDouble(bool2));
  }

  @Test
  public void stringObjectToBoolean_test() {
    String str1 = "true";
    assertEquals(true, DataTypeUtil.stringToBoolean((Object) str1));

    String str2 = "false";
    assertEquals(false, DataTypeUtil.stringToBoolean((Object) str2));

    String str3 = "1";
    assertEquals(false, DataTypeUtil.stringToBoolean((Object) str3));

    String str4 = "0";
    assertEquals(false, DataTypeUtil.stringToBoolean((Object) str4));

    String str5 = "";
    assertEquals(false, DataTypeUtil.stringToBoolean((Object) str5));
  }

  @Test
  public void stringToBoolean_test() {
    String str1 = "true";
    assertEquals(true, DataTypeUtil.stringToBoolean(str1));

    String str2 = "false";
    assertEquals(false, DataTypeUtil.stringToBoolean(str2));

    String str3 = "1";
    assertEquals(false, DataTypeUtil.stringToBoolean(str3));

    String str4 = "0";
    assertEquals(false, DataTypeUtil.stringToBoolean(str4));

    String str5 = "";
    assertEquals(false, DataTypeUtil.stringToBoolean(str5));
  }

  @Test
  public void stringObjectToDouble_test() {
    String str1 = "1.23";
    assertEquals(Double.valueOf(1.23d), DataTypeUtil.stringToDouble((Object) str1));

    String str2 = "1.23d";
    assertEquals(Double.valueOf(1.23d), DataTypeUtil.stringToDouble((Object) str2));

    String str3 = "1.23f";
    assertEquals(Double.valueOf(1.23d), DataTypeUtil.stringToDouble((Object) str3));
  }

  @Test(expected = NumberFormatException.class)
  public void stringObjectToDouble_invalidCast_test() {
    String str = "abc";
    DataTypeUtil.stringToDouble((Object) str);
  }

  @Test(expected = NullPointerException.class)
  public void stringObjectToDouble_nullPointer_test() {
    String str = null;
    DataTypeUtil.stringToDouble((Object) str);
  }

  @Test
  public void stringToDouble_test() {
    String str1 = "1.23";
    assertEquals(Double.valueOf(1.23d), DataTypeUtil.stringToDouble(str1));

    String str2 = "1.23d";
    assertEquals(Double.valueOf(1.23d), DataTypeUtil.stringToDouble(str2));

    String str3 = "1.23f";
    assertEquals(Double.valueOf(1.23d), DataTypeUtil.stringToDouble(str3));
  }

  @Test(expected = NumberFormatException.class)
  public void stringToDouble_invalidCast_test() {
    String str = "abc";
    DataTypeUtil.stringToDouble(str);
  }

  @Test(expected = NullPointerException.class)
  public void stringToDouble_nullPointer_test() {
    String str = null;
    DataTypeUtil.stringToDouble(str);
  }

  @Test
  public void stringObjectToLong_test() {
    String str1 = "123";
    assertEquals(Long.valueOf(123L), DataTypeUtil.stringToLong((Object) str1));
  }

  @Test(expected = NumberFormatException.class)
  public void stringObjectToLong_invalidCast_test() {
    String str = "abc";
    DataTypeUtil.stringToLong((Object) str);
  }

  @Test(expected = NumberFormatException.class)
  public void stringObjectToLong_nullPointer_test() {
    Object obj = null;
    DataTypeUtil.stringToLong(obj);
  }

  @Test
  public void stringToLong_test() {
    String str1 = "123";
    assertEquals(Long.valueOf(123L), DataTypeUtil.stringToLong(str1));
 }

  @Test
  public void objectToString_test() {
    String str = "string value";
    assertEquals("string value", DataTypeUtil.objectToString((Object) str));
  }

  @Test(expected = NullPointerException.class)
  public void objectToString_nullPointer_test() {
    String str = null;
    DataTypeUtil.objectToString((Object) str);
  }
}
