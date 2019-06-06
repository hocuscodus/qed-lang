/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

public class IntegralType extends NumericType {
	public boolean isIntegral() {
		return true;
	}

	public Object add(Object value1, Type value2Type, Object value2) {
		return value2Type.isIntegral() ? setInteger(getInteger(value1) + value2Type.getInteger(value2)) : null;
	}

	public Object subtract(Object value1, Type value2Type, Object value2) {
		return value2Type.isIntegral() ? setInteger(getInteger(value1) - value2Type.getInteger(value2)) : null;
	}

	public Object mult(Object value1, Type value2Type, Object value2) {
		return value2Type.isIntegral() ? setInteger(getInteger(value1) * value2Type.getInteger(value2)) : null;
	}

	public Object div(Object value1, Type value2Type, Object value2) {
		return value2Type.isIntegral() ? setInteger(getInteger(value1) / value2Type.getInteger(value2)) : null;
	}

	public Object mod(Object value1, Type value2Type, Object value2) {
		return value2Type.isIntegral() ? setInteger(getInteger(value1) % value2Type.getInteger(value2)) : null;
	}

	public Object shiftL(Object value1, Type value2Type, Object value2) {
		return value2Type.isIntegral() ? setInteger(getInteger(value1) << value2Type.getInteger(value2)) : null;
	}

	public Object shiftR(Object value1, Type value2Type, Object value2) {
		return value2Type.isIntegral() ? setInteger(getInteger(value1) >> value2Type.getInteger(value2)) : null;
	}

	public Object shiftUR(Object value1, Type value2Type, Object value2) {
		return value2Type.isIntegral() ? setInteger(getInteger(value1) >>> value2Type.getInteger(value2)) : null;
	}

    public Object lt(Object value1, Type value2Type, Object value2) {
		return value2Type.isIntegral() ? getInteger(value1) < value2Type.getInteger(value2) : null;
    }

    public Object gt(Object value1, Type value2Type, Object value2) {
		return value2Type.isIntegral() ? getInteger(value1) > value2Type.getInteger(value2) : null;
    }

    public Object ltEq(Object value1, Type value2Type, Object value2) {
		return value2Type.isIntegral() ? getInteger(value1) <= value2Type.getInteger(value2) : null;
    }

    public Object gtEq(Object value1, Type value2Type, Object value2) {
		return value2Type.isIntegral() ? getInteger(value1) >= value2Type.getInteger(value2) : null;
    }

    public Object eq(Object value1, Type value2Type, Object value2) {
		return value2Type.isIntegral() ? getInteger(value1) == value2Type.getInteger(value2) : null;
    }

    public Object neq(Object value1, Type value2Type, Object value2) {
		return value2Type.isIntegral() ? getInteger(value1) != value2Type.getInteger(value2) : null;
    }

	public Object newDefault() {
		return setInteger(0);
	}
}
