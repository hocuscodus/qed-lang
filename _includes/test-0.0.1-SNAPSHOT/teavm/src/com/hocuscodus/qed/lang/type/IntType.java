/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import java.io.IOException;
import java.io.OutputStream;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.Declaration.DecEnv;
import com.hocuscodus.qed.lang.Exp;
import com.hocuscodus.qed.lang.Exp.ValueExp;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.QEDProcess;

public class IntType extends IntegralType {
	protected Object clone() {
		return new IntType();
	}

	public String getName() {
		return "int";
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		super.writeExp(exp, env, path, out);
		writeI(out, (Integer) ((ValueExp) exp).getValue());
	}

	public void eval(QEDProcess process, Call call) {
		int value = call.readI(process);

		call.stack.push(new Integer(value));
	}

	public Type[] getCastTypes() {
		return new Type[] {longType, byteType, booleanType, voidType, floatType, charType, doubleType, shortType};
	}

	public Object cast(int typeIndex, Object value) {
		Integer val = null;

		switch(typeIndex) {
		case 0:
			val = (int) ((Long) value).longValue();
			break;

		case 1:
			val = (int) ((Byte) value).byteValue();
			break;

		case 2:
			val = ((Boolean) value) ? 1 : 0;
			break;

		case 3:
			val = 1;
			break;

		case 4:
			val = (int) ((Float) value).floatValue();
			break;

		case 5:
			val = (int) ((Character) value).charValue();
			break;

		case 6:
			val = (int) ((Double) value).doubleValue();
			break;

		case 7:
			val = (int) ((Short) value).longValue();
			break;
		}

		return (val);
	}

	public long getInteger(Object value) {
		return (Integer) value;
	}

	public Object setInteger(long num) {
		return (int) num;
	}

	public Object add(Object value1, Object value2) {
		return (Integer) value1 + (Integer) value2;
	}

	public Object subtract(Object value1, Object value2) {
		return (Integer) value1 - (Integer) value2;
	}

	public Object mult(Object value1, Object value2) {
		return (Integer) value1 * (Integer) value2;
	}

	public Object div(Object value1, Object value2) {
		return (Integer) value1 / (Integer) value2;
	}

    public Object lt(Object value1, Object value2) {
		return ((Integer) value1).intValue() < ((Integer) value2).intValue();
    }

    public Object gt(Object value1, Object value2) {
		return ((Integer) value1).intValue() > ((Integer) value2).intValue();
    }

    public Object ltEq(Object value1, Object value2) {
		return ((Integer) value1).intValue() <= ((Integer) value2).intValue();
    }

    public Object gtEq(Object value1, Object value2) {
		return ((Integer) value1).intValue() >= ((Integer) value2).intValue();
    }

    public Object eq(Object value1, Object value2) {
		return ((Integer) value1).intValue() == ((Integer) value2).intValue();
    }

    public Object neq(Object value1, Object value2) {
		return ((Integer) value1).intValue() != ((Integer) value2).intValue();
    }

    public Object and(Object value1, Object value2) {
		return (Integer) value1 & (Integer) value2;
    }

    public Object or(Object value1, Object value2) {
		return (Integer) value1 | (Integer) value2;
    }

    public Object xor(Object value1, Object value2) {
		return (Integer) value1 ^ (Integer) value2;
    }

	public String getString(Object value) {
		return "" + getInteger(value);
	}
}
