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

public class AssignmentFn extends BinaryFn {
	public AssignmentFn() {
		super("=", new Type[] {Type.voidType, Type.voidType});
	}

	public boolean makerefs(Exp exp, Path path) {
        return (getExp2(exp)).makerefs(path);
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		getExp1(exp).writeAssignment(env, path, out);
//		((UnaryExp) getExp1(exp)).exp.write(env, out);
//		if (getExp2(exp) != null)
		getExp2(exp).write(env, path, out);
		write(out, opCode);
		write(out, getExp1(exp).type.opCode);
	}

	public Type getReturnType(Exp exp, DecEnv env) {
		return returnType != null ? returnType : getExp1(exp).getReturnType(env);
	}

	public void eval(QEDProcess process, Call call) {
		int opCode = call.read(process);
		Type type = fnList.get(opCode);
		Object value2 = call.stack.pop();
		Object value = type.assign(process, call, value2);

		if (value != null)
			call.stack.push(value);
	}
}
