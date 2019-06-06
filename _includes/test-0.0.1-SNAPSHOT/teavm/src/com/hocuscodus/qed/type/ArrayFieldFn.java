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
import com.hocuscodus.qed.lang.Exp.BinaryExp;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.QEDProcess;

public class ArrayFieldFn extends BinaryFn {
	public ArrayFieldFn() {
		super("[]", new Type[] {Type.intType, Type.intType});
	}

	public Type getReturnType(Exp exp, DecEnv env) {
		return getExp1(exp).getReturnType(env).bracketType();
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		super.writeExp(exp, env, path, out);
		write(out, getExp1(exp).getReturnType(env).opCode);
	}

	public Object eval(QEDProcess process, Call call, Object value1, Object value2) {
		int opCode = call.read(process);
		Type type = fnList.get(opCode);

		return type.bracket((Object[]) value1, (Integer) value2);
	}
//
//	public Object assign(Env env, Exp exp, Object value) {
//		Type indexType = getExp2(exp).getReturnType();
//		int index = (int) indexType.getInteger(getExp2(exp).eval(env));
//
//		return getExp1(exp).getReturnType().assignBracket(getExp1(exp).eval(env), index, value);
//	}

	public Object assign(QEDProcess process, Call call, Object value) {
		int index = (Integer) call.stack.pop();
		Object[] array = (Object[]) call.stack.pop();

		array[index] = value; 
		return (value);
	}

	public boolean findArrayFieldExp(Exp exp, Exp indexExp) {
		boolean flag = !getExp1(exp).findArrayFieldExp(indexExp) && getExp2(exp) == null;

		if (flag)
			((BinaryExp) exp).value2 = indexExp;

		return flag;
	}

	public String toString(Exp exp) {
		return getExp1(exp) + "[" + getExp2(exp) + "]";
	}
}
