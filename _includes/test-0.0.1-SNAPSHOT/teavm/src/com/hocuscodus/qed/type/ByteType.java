/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

public class ByteType extends IntegralType {
	protected Object clone() {
		return new ByteType();
	}

	public String getName() {
		return "byte";
	}

	public Type[] getCastTypes() {
		return new Type[] {intType, shortType, booleanType, voidType, floatType, longType, doubleType, charType};
	}

	public Object cast(int typeIndex, Object value) {
		Byte val = null;

		switch(typeIndex) {
		case 0:
			val = (byte) ((Integer) value).intValue();
			break;

		case 1:
			val = (byte) ((Short) value).shortValue();
			break;

		case 2:
			val = ((Boolean) value) ? (byte) 1 : (byte) 0;
			break;

		case 3:
			val = 1;
			break;

		case 4:
			val = (byte) ((Float) value).floatValue();
			break;

		case 5:
			val = (byte) ((Long) value).longValue();
			break;

		case 6:
			val = (byte) ((Double) value).doubleValue();
			break;

		case 7:
			val = (byte) ((Character) value).charValue();
			break;
		}

		return (val);
	}

	public long getInteger(Object value) {
		return (Byte) value;
	}

	public Object setInteger(long num) {
		return (byte) num;
	}

	public Object add(Object value1, Object value2) {
		return (Byte) value1 + (Byte) value2;
	}

	public Object subtract(Object value1, Object value2) {
		return (Byte) value1 - (Byte) value2;
	}

	public Object mult(Object value1, Object value2) {
		return (Byte) value1 * (Byte) value2;
	}

	public Object div(Object value1, Object value2) {
		return (Byte) value1 / (Byte) value2;
	}

    public Object lt(Object value1, Object value2) {
		return ((Byte) value1).byteValue() < ((Byte) value2).byteValue();
    }

    public Object gt(Object value1, Object value2) {
		return ((Byte) value1).byteValue() > ((Byte) value2).byteValue();
    }

    public Object ltEq(Object value1, Object value2) {
		return ((Byte) value1).byteValue() <= ((Byte) value2).byteValue();
    }

    public Object gtEq(Object value1, Object value2) {
		return ((Byte) value1).byteValue() >= ((Byte) value2).byteValue();
    }

    public Object eq(Object value1, Object value2) {
		return ((Byte) value1).byteValue() == ((Byte) value2).byteValue();
    }

    public Object neq(Object value1, Object value2) {
		return ((Byte) value1).byteValue() != ((Byte) value2).byteValue();
    }

    public Object and(Object value1, Object value2) {
		return (Byte) value1 & (Byte) value2;
    }

    public Object or(Object value1, Object value2) {
		return (Byte) value1 | (Byte) value2;
    }

    public Object xor(Object value1, Object value2) {
		return (Byte) value1 ^ (Byte) value2;
    }
}
