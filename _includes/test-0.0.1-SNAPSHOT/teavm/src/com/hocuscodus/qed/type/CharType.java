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

public class CharType extends IntegralType {
	protected Object clone() {
		return new CharType();
	}

	public String getName() {
		return "char";
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		super.writeExp(exp, env, path, out);
		int numValue = Character.getNumericValue(((Character) ((ValueExp) exp).getValue()).charValue());

		writeS(out, (short) numValue);
	}

	public void eval(QEDProcess process, Call call) {
		int value = call.readS(process) & 0xFFFF;

		call.stack.push(new Character((char) value));
	}

	public Type[] getCastTypes() {
		return new Type[] {intType, byteType, booleanType, voidType, floatType, longType, doubleType, shortType};
	}

	public Object cast(int typeIndex, Object value) {
		Character val = null;

		switch(typeIndex) {
		case 0:
			val = (char) ((Integer) value).intValue();
			break;

		case 1:
			val = (char) ((Byte) value).byteValue();
			break;

		case 2:
			val = ((Boolean) value) ? (char) 1 : (char) 0;
			break;

		case 3:
			val = 1;
			break;

		case 4:
			val = (char) ((Float) value).floatValue();
			break;

		case 5:
			val = (char) ((Long) value).longValue();
			break;

		case 6:
			val = (char) ((Double) value).doubleValue();
			break;

		case 7:
			val = (char) ((Short) value).shortValue();
			break;
		}

		return (val);
	}

	public long getInteger(Object value) {
		return (Character) value;
	}

	public Object setInteger(long num) {
		return (char) num;
	}

	public Object add(Object value1, Object value2) {
		return (Character) value1 + (Character) value2;
	}

	public Object subtract(Object value1, Object value2) {
		return (Character) value1 - (Character) value2;
	}

	public Object mult(Object value1, Object value2) {
		return (Character) value1 * (Character) value2;
	}

	public Object div(Object value1, Object value2) {
		return (Character) value1 / (Character) value2;
	}

    public Object lt(Object value1, Object value2) {
		return ((Character) value1).charValue() < ((Character) value2).charValue();
    }

    public Object gt(Object value1, Object value2) {
		return ((Character) value1).charValue() > ((Character) value2).charValue();
    }

    public Object ltEq(Object value1, Object value2) {
		return ((Character) value1).charValue() <= ((Character) value2).charValue();
    }

    public Object gtEq(Object value1, Object value2) {
		return ((Character) value1).charValue() >= ((Character) value2).charValue();
    }

    public Object eq(Object value1, Object value2) {
		return ((Character) value1).charValue() == ((Character) value2).charValue();
    }

    public Object neq(Object value1, Object value2) {
		return ((Character) value1).charValue() != ((Character) value2).charValue();
    }

    public Object and(Object value1, Object value2) {
		return (Character) value1 & (Character) value2;
    }

    public Object or(Object value1, Object value2) {
		return (Character) value1 | (Character) value2;
    }

    public Object xor(Object value1, Object value2) {
		return (Character) value1 ^ (Character) value2;
    }
}
