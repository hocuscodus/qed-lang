/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

public class FloatType extends NumericType {
	public String getName() {
		return "float";
	}

	public Type[] getCastTypes() {
		return new Type[] {intType, shortType, booleanType, voidType, byteType, longType, doubleType, charType};
	}

	public Object cast(int typeIndex, Object value) {
		Float val = null;

		switch(typeIndex) {
		case 0:
			val = (float) ((Integer) value).intValue();
			break;

		case 1:
			val = (float) ((Short) value).shortValue();
			break;

		case 2:
			val = ((Boolean) value) ? (float) 1 : (float) 0;
			break;

		case 3:
			val = (float) 1;
			break;

		case 4:
			val = (float) ((Byte) value).byteValue();
			break;

		case 5:
			val = (float) ((Long) value).longValue();
			break;

		case 6:
			val = (float) ((Double) value).doubleValue();
			break;

		case 7:
			val = (float) ((Character) value).charValue();
			break;
		}

		return (val);
	}

	public Object newDefault() {
		return new Float(0);
	}

	public Object add(Object value1, Object value2) {
		return (Float) value1 + (Float) value2;
	}

	public Object subtract(Object value1, Object value2) {
		return (Float) value1 - (Float) value2;
	}

	public Object mult(Object value1, Object value2) {
		return (Float) value1 * (Float) value2;
	}

	public Object div(Object value1, Object value2) {
		return (Float) value1 / (Float) value2;
	}

    public Object lt(Object value1, Object value2) {
		return ((Float) value1).floatValue() < ((Float) value2).floatValue();
    }

    public Object gt(Object value1, Object value2) {
		return ((Float) value1).floatValue() > ((Float) value2).floatValue();
    }

    public Object ltEq(Object value1, Object value2) {
		return ((Float) value1).floatValue() <= ((Float) value2).floatValue();
    }

    public Object gtEq(Object value1, Object value2) {
		return ((Float) value1).floatValue() >= ((Float) value2).floatValue();
    }

    public Object eq(Object value1, Object value2) {
		return ((Float) value1).floatValue() == ((Float) value2).floatValue();
    }

    public Object neq(Object value1, Object value2) {
		return ((Float) value1).floatValue() != ((Float) value2).floatValue();
    }
}
