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
import com.hocuscodus.qed.lang.ObjEnv;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.QEDObj;
import com.hocuscodus.qed.lang.QEDProcess;

public class DotFn extends UnaryFn {
	public DotFn() {
		super(".", new Type[] {Type.stringType});
	}

	public Type getReturnType(Exp exp, DecEnv env) {
        return super.getReturnType(exp, env);
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		getExp(exp).write(env, path, out);
		write(out, opCode);
	}

	public Object eval(QEDProcess process, Call call, Object value) {
		if (value instanceof QEDObj) {
		QEDObj obj = (QEDObj) value;

		return new Object[] {obj, new ObjEnv(obj.data)};
		}
		else
			return value;
	}

	public String toString(Exp exp) {
		return super.toString(exp) + ".";
	}
}
