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

public class BooleanType extends ValueType {
	protected Object clone() {
		return new BooleanType();
	}

	public Type[] getCastTypes() {
		return new Type[] {intType, shortType, voidType, byteType, longType, charType};
	}

	public Object cast(int typeIndex, Object value) {
		Boolean val = null;

		switch(typeIndex) {
		case 0:
			if (!(value instanceof Integer))
				value=value;
			val = ((Integer) value).intValue() != 0;
			break;

		case 1:
			val = ((Short) value).shortValue() != 0;
			break;

		case 2:
			val = true;
			break;

		case 3:
			val = ((Byte) value).byteValue() != 0;
			break;

		case 4:
			val = ((Long) value).longValue() != 0;
			break;

		case 5:
			val = ((Character) value).charValue() != 0;
			break;
		}

		return (val);
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		boolean value = (Boolean) ((ValueExp) exp).getValue();

		super.writeExp(exp, env, path, out);
		write(out, value ? 1 : 0);
	}

	public void eval(QEDProcess process, Call call) {
		call.stack.push(call.read(process) != 0);
	}

	public String getName() {
		return "boolean";
	}

    public Object eq(Object value1, Object value2) {
		return ((Boolean) value1).booleanValue() == ((Boolean) value2).booleanValue();
    }

    public Object neq(Object value1, Object value2) {
		return ((Boolean) value1).booleanValue() != ((Boolean) value2).booleanValue();
    }

    public Object and(Object value1, Object value2) {
		return ((Boolean) value1).booleanValue() & ((Boolean) value2).booleanValue();
    }

    public Object or(Object value1, Object value2) {
		return ((Boolean) value1).booleanValue() | ((Boolean) value2).booleanValue();
    }

    public Object xor(Object value1, Object value2) {
		return ((Boolean) value1).booleanValue() ^ ((Boolean) value2).booleanValue();
    }

	public Object newDefault() {
		return new Boolean(false);
	}
}
