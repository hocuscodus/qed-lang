/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import java.io.IOException;
import java.io.OutputStream;

import com.hocuscodus.qed.lang.Exp;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.Declaration.DecEnv;
import com.hocuscodus.qed.lang.Exp.ValueExp;

public class BinaryBooleanOpFn extends BinaryOpFn {
	public BinaryBooleanOpFn(String name) {
		super(booleanType, name);
	}

	public Type getReturnType(Exp exp, DecEnv env) {
		return booleanType;
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		Type returnType = super.getReturnType(exp, env);

		getExp1(exp).write(env, path, out);
		getExp2(exp).write(env, path, out);
		write(out, opCode);
		write(out, returnType.opCode);
	}
}
