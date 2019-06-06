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
import com.hocuscodus.qed.lang.NewReturnHandler;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.QEDProcess;

public class NewFn extends BinaryFn {
	public NewFn() {
		super("new", new Type[] {Type.voidType, Type.voidType});
	}

	public Type getReturnType(Exp exp, DecEnv env) {
		return (getExp1(exp)).getType();
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		Type type = ((BinaryExp) exp).value1.type;
		boolean isPureFunction = type instanceof FuncFn && ((FuncFn) type).isPureFunction();

		if (!isPureFunction) {
			write(out, opCode);
    		getExp1(exp).write(env, path, out);
		}
		else
			; // Compile error cannot instantiate a pure function
	}

	public void eval(QEDProcess process, Call call) {
		call.stack.push(NewReturnHandler.obj);
	}

	public String toString(Exp exp) {
		return "new " + super.toString(exp);
	}
}
