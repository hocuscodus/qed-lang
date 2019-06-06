/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import java.io.IOException;
import java.io.OutputStream;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.CallReturnHandler;
import com.hocuscodus.qed.lang.Declaration.DecEnv;
import com.hocuscodus.qed.lang.Exp;
import com.hocuscodus.qed.lang.Exp.UnaryExp;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.QEDProcess;

public class CallFn extends UnaryFn {
	public CallFn() {
		super("c", new Type[] {});
	}

	public Type getReturnType(Exp exp, DecEnv env) {
		Type type = getExp(exp).type.returnType;
		Type type2 = getExp(exp).getReturnType(env);

		if (type != type2)
			type = getExp(exp).getReturnType(env);

		return getExp(exp).type.returnType;//getReturnType(env);
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		Type type = ((UnaryExp) exp).exp.type;
		boolean isPureFunction = type instanceof FuncFn && ((FuncFn) type).isPureFunction();

		if (!isPureFunction) {
			write(out, opCode);
		}

		getExp(exp).write(env, path, out);
	}

	public void eval(QEDProcess process, Call call) {
		call.stack.push(CallReturnHandler.obj);
	}
}
