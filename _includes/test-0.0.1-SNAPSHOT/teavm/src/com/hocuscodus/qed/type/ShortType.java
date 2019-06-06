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

public class ShortType extends IntegralType {
	protected Object clone() {
		return new ShortType();
	}

	public String getName() {
		return "short";
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		super.writeExp(exp, env, path, out);
		writeS(out, (Short) ((ValueExp) exp).getValue());
	}

	public void eval(QEDProcess process, Call call) {
		short value = call.readS(process);

		call.stack.push(new Short(value));
	}

	public Type[] getCastTypes() {
		return new Type[] {intType, byteType, booleanType, voidType, floatType, longType, doubleType, charType};
	}

	public Object cast(int typeIndex, Object value) {
		Short val = null;

		switch(typeIndex) {
		case 0:
			val = (short) ((Integer) value).intValue();
			break;

		case 1:
			val = (short) ((Byte) value).byteValue();
			break;

		case 2:
			val = ((Boolean) value) ? (short) 1 : (short) 0;
			break;

		case 3:
			val = 1;
			break;

		case 4:
			val = (short) ((Float) value).floatValue();
			break;

		case 5:
			val = (short) ((Long) value).longValue();
			break;

		case 6:
			val = (short) ((Double) value).doubleValue();
			break;

		case 7:
			val = (short) ((Character) value).charValue();
			break;
		}

		return (val);
	}

	public long getInteger(Object value) {
		return (Short) value;
	}

	public Object setInteger(long num) {
		return (short) num;
	}

	public Object add(Object value1, Object value2) {
		return (Short) value1 + (Short) value2;
	}

	public Object subtract(Object value1, Object value2) {
		return (Short) value1 - (Short) value2;
	}

	public Object mult(Object value1, Object value2) {
		return (Short) value1 * (Short) value2;
	}

	public Object div(Object value1, Object value2) {
		return (Short) value1 / (Short) value2;
	}

    public Object lt(Object value1, Object value2) {
		return ((Short) value1).shortValue() < ((Short) value2).shortValue();
    }

    public Object gt(Object value1, Object value2) {
		return ((Short) value1).shortValue() > ((Short) value2).shortValue();
    }

    public Object ltEq(Object value1, Object value2) {
		return ((Short) value1).shortValue() <= ((Short) value2).shortValue();
    }

    public Object gtEq(Object value1, Object value2) {
		return ((Short) value1).shortValue() >= ((Short) value2).shortValue();
    }

    public Object eq(Object value1, Object value2) {
		return ((Short) value1).shortValue() == ((Short) value2).shortValue();
    }

    public Object neq(Object value1, Object value2) {
		return ((Short) value1).shortValue() != ((Short) value2).shortValue();
    }

    public Object and(Object value1, Object value2) {
		return (Short) value1 & (Short) value2;
    }

    public Object or(Object value1, Object value2) {
		return (Short) value1 | (Short) value2;
    }

    public Object xor(Object value1, Object value2) {
		return (Short) value1 ^ (Short) value2;
    }
}
