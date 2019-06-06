/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

public class DoubleType extends NumericType {
	public String getName() {
		return "double";
	}

	public Type[] getCastTypes() {
		return new Type[] {intType, shortType, booleanType, voidType, byteType, longType, floatType, charType};
	}

	public Object cast(int typeIndex, Object value) {
		Double val = null;

		switch(typeIndex) {
		case 0:
			val = (double) ((Integer) value).intValue();
			break;

		case 1:
			val = (double) ((Short) value).shortValue();
			break;

		case 2:
			val = ((Boolean) value) ? (double) 1 : (double) 0;
			break;

		case 3:
			val = (double) 1;
			break;

		case 4:
			val = (double) ((Byte) value).byteValue();
			break;

		case 5:
			val = (double) ((Long) value).longValue();
			break;

		case 6:
			val = (double) ((Float) value).floatValue();
			break;

		case 7:
			val = (double) ((Character) value).charValue();
			break;
		}

		return (val);
	}

	public Object newDefault() {
		return new Double(0);
	}

	public Object add(Object value1, Object value2) {
		return (Double) value1 + (Double) value2;
	}

	public Object subtract(Object value1, Object value2) {
		return (Double) value1 - (Double) value2;
	}

	public Object mult(Object value1, Object value2) {
		return (Double) value1 * (Double) value2;
	}

	public Object div(Object value1, Object value2) {
		return (Double) value1 / (Double) value2;
	}

    public Object lt(Object value1, Object value2) {
		return ((Double) value1).doubleValue() < ((Double) value2).doubleValue();
    }

    public Object gt(Object value1, Object value2) {
		return ((Double) value1).doubleValue() > ((Double) value2).doubleValue();
    }

    public Object ltEq(Object value1, Object value2) {
		return ((Double) value1).doubleValue() <= ((Double) value2).doubleValue();
    }

    public Object gtEq(Object value1, Object value2) {
		return ((Double) value1).doubleValue() >= ((Double) value2).doubleValue();
    }

    public Object eq(Object value1, Object value2) {
		return ((Double) value1).doubleValue() == ((Double) value2).doubleValue();
    }

    public Object neq(Object value1, Object value2) {
		return ((Double) value1).doubleValue() != ((Double) value2).doubleValue();
    }
}
