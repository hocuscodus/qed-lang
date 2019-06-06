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
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.QEDProcess;

public class BinaryOpFn extends BinaryFn {
	public BinaryOpFn(Type type, String name) {
		super(type, name, new Type[] {Type.intType, Type.intType});
	}

	public BinaryOpFn(String name) {
		super(name, new Type[] {Type.intType, Type.intType});
	}

	public Type getReturnType(Exp exp, DecEnv env) {
		Type returnType1 = getExp1(exp).getReturnType(env);
		Type returnType2 = getExp2(exp).getReturnType(env);

		return returnType1.opCode > returnType2.opCode ? returnType1 : returnType2;
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		Type returnType = getReturnType(exp, env);

		returnType.cast(getExp1(exp), env).write(env, path, out);
		returnType.cast(getExp2(exp), env).write(env, path, out);
		write(out, opCode);
		write(out, returnType.opCode);
	}

	public Object eval(QEDProcess process, Call call, Object value1, Object value2) {
		int opCode = call.read(process);
		Type type = fnList.get(opCode);

		return eval(type, value1, value2);
	}

	public Object eval(Type type, Object value1, Object value2) {
		return null;
	}
}
