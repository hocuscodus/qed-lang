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

public class LongType extends IntegralType {
	protected Object clone() {
		return new LongType();
	}

	public String getName() {
		return "long";
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		super.writeExp(exp, env, path, out);
		writeL(out, (Long) ((ValueExp) exp).getValue());
	}

	public void eval(QEDProcess process, Call call) {
		long value = call.readL(process);

		call.stack.push(new Long(value));
	}

	public Type[] getCastTypes() {
		return new Type[] {intType, byteType, booleanType, voidType, floatType, charType, doubleType, shortType};
	}

	public Object cast(int typeIndex, Object value) {
		Long val = null;

		switch(typeIndex) {
		case 0:
			val = (long) ((Integer) value).intValue();
			break;

		case 1:
			val = (long) ((Byte) value).byteValue();
			break;

		case 2:
			val = ((Boolean) value) ? 1L : 0L;
			break;

		case 3:
			val = 1L;
			break;

		case 4:
			val = (long) ((Float) value).floatValue();
			break;

		case 5:
			val = (long) ((Character) value).charValue();
			break;

		case 6:
			val = (long) ((Double) value).doubleValue();
			break;

		case 7:
			val = (long) ((Short) value).longValue();
			break;
		}

		return (val);
	}

	public long getInteger(Object value) {
		return (Long) value;
	}

	public Object setInteger(long num) {
		return num;
	}

	public Object add(Object value1, Object value2) {
		return (Long) value1 + (Long) value2;
	}

	public Object subtract(Object value1, Object value2) {
		return (Long) value1 - (Long) value2;
	}

	public Object mult(Object value1, Object value2) {
		return (Long) value1 * (Long) value2;
	}

	public Object div(Object value1, Object value2) {
		return (Long) value1 / (Long) value2;
	}

    public Object lt(Object value1, Object value2) {
		return ((Long) value1).longValue() < ((Long) value2).longValue();
    }

    public Object gt(Object value1, Object value2) {
		return ((Long) value1).longValue() > ((Long) value2).longValue();
    }

    public Object ltEq(Object value1, Object value2) {
		return ((Long) value1).longValue() <= ((Long) value2).longValue();
    }

    public Object gtEq(Object value1, Object value2) {
		return ((Long) value1).longValue() >= ((Long) value2).longValue();
    }

    public Object eq(Object value1, Object value2) {
		return ((Long) value1).longValue() == ((Long) value2).longValue();
    }

    public Object neq(Object value1, Object value2) {
		return ((Long) value1).longValue() != ((Long) value2).longValue();
    }

    public Object and(Object value1, Object value2) {
		return (Long) value1 & (Long) value2;
    }

    public Object or(Object value1, Object value2) {
		return (Long) value1 | (Long) value2;
    }

    public Object xor(Object value1, Object value2) {
		return (Long) value1 ^ (Long) value2;
    }
}
