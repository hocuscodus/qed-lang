/*
 *    Copyright (C) 2018 Hocus Codus Software inc.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import java.io.IOException;
import java.io.OutputStream;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.Declaration.DecEnv;
import com.hocuscodus.qed.lang.Declaration.FunctionDeclaration;
import com.hocuscodus.qed.lang.Declaration.InternalDeclaration;
import com.hocuscodus.qed.lang.Exp;
import com.hocuscodus.qed.lang.Exp.RefExp;
import com.hocuscodus.qed.lang.ObjEnv;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.QEDCall;
import com.hocuscodus.qed.lang.QEDObj;
import com.hocuscodus.qed.lang.QEDProcess;


public class RefType2 extends Type {
	public RefType2() {
		super(Type.intType, "_ref__", null);
	}

	public Type getReturnType(Exp exp, DecEnv env) {
		int var = ((RefExp) exp).level;
		int level = var >>> 8;
		FunctionDeclaration parent = env.parent;
		InternalDeclaration[] declarations = env.declarations;

		while (level-- != 0) {
			declarations = parent.declarations;
			parent = parent.parent;
		}

		return declarations[var & 0xFF];
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		super.writeExp(exp, env, path, out);
		write(out, ((RefExp) exp).level >>> 8);
		write(out, ((RefExp) exp).level & 0xFF);
	}

	public void eval(QEDProcess process, Call call) {
		QEDCall qedCall = call.getQEDCall();
		QEDObj obj = (QEDObj) qedCall.obj;
		ObjEnv env = qedCall.env;
		int level = qedCall.read(process);
		int index = qedCall.read(process);

		while (obj != null && level-- != 0) {
			env = obj.parentEnv;
			obj = obj.parent;
		}

		call.stack.push(new Object[] {obj, new ObjEnv(env, index + 1)});
	}

	public String getName() {
		return "ref";
	}

	@Override
	public String toString(Exp exp) {
		return returnType.toString();
	}
}
