/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.Declaration.DecEnv;
import com.hocuscodus.qed.lang.Exp;
import com.hocuscodus.qed.lang.Exp.ValueExp;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.QEDProcess;

public class StringType extends ValueType {
	public String getName() {
		return "string";
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		byte[] str = ((String) ((ValueExp) exp).getValue()).getBytes("UTF-8");

		super.writeExp(exp, env, path, out);
		write(out, str.length);
		write(out, str);
	}

	public void eval(QEDProcess process, Call call) {
		int length = call.read(process);
		byte[] bytes = new byte[length];

		call.read(process, bytes);
		try {
			call.stack.push(new String(bytes, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			call.stack.push("Erroneous string");
		}
	}

	public Object newDefault() {
		return new String();
	}

	public Type[] getCastTypes() {
		return new Type[] {intType, shortType, byteType, longType, charType, floatType, doubleType, booleanType};
	}

	public Object cast(int typeIndex, Object value) {
		return "" + value;
	}

	public Object add(Object value1, Type value2Type, Object value2) {
		return (String) value1 + value2;
	}

	public Object add(Object value1, Object value2) {
		if (!(value1 instanceof String) || !(value2 instanceof String))
			value1 = value1;
		return (String) value1 + (String) value2;
	}

	public Type bracketType() {
		return new CharType();
	}

	public Object bracket(Object array, int index) {
		return ((String) array).charAt(index);
	}

    public Object lt(Object value1, Type value2Type, Object value2) {
		return ((String) value1).compareTo(value2.toString()) < 0;
    }

    public Object gt(Object value1, Type value2Type, Object value2) {
		return ((String) value1).compareTo(value2.toString()) > 0;
    }

    public Object ltEq(Object value1, Type value2Type, Object value2) {
		return (Boolean) lt(value1, value2Type, value2) || (Boolean) eq(value1, value2Type, value2);
    }

    public Object gtEq(Object value1, Type value2Type, Object value2) {
		return (Boolean) gt(value1, value2Type, value2) || (Boolean) eq(value1, value2Type, value2);
    }

    public Object eq(Object value1, Type value2Type, Object value2) {
		return ((String) value1).compareTo(value2.toString()) == 0;
    }

    public Object neq(Object value1, Type value2Type, Object value2) {
		return ((String) value1).compareTo(value2.toString()) != 0;
    }

    public Object lt(Object value1, Object value2) {
		return ((String) value1).compareTo((String) value2) < 0;
    }

    public Object gt(Object value1, Object value2) {
		return ((String) value1).compareTo((String) value2) > 0;
    }

    public Object ltEq(Object value1, Object value2) {
		return (Boolean) lt(value1, value2) || (Boolean) eq(value1, value2);
    }

    public Object gtEq(Object value1, Object value2) {
		return (Boolean) gt(value1, value2) || (Boolean) eq(value1, value2);
    }

    public Object eq(Object value1, Object value2) {
		return ((String) value1).compareTo((String) value2) == 0;
    }

    public Object neq(Object value1, Object value2) {
		return ((String) value1).compareTo((String) value2) != 0;
    }

	public Object assignBracket(Object array, int index, Object value) {
		String str = (String) array;

		array = str.substring(0, index) + ((Character) value) + str.substring(index + 1);
		return str.charAt(index);
	}

	@Override
	public String toString(Exp exp) {
		return "\"" + super.toString(exp) + "\"";
	}
}

