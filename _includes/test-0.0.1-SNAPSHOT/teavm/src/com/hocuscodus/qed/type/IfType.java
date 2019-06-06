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
import com.hocuscodus.qed.lang.Exp.FuncExp;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.QEDProcess;

public class IfType extends FuncFn {
	public IfType() {
		super(Type.booleanType, "if", new Type[] {Type.booleanType});
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		((FuncExp) exp).args.set(0, booleanType.cast(((FuncExp) exp).args.get(0), env));
		super.writeExp(exp, env, path, out);
	}

	public Object eval(QEDProcess process, Call call, Object[] args) {
		return (Boolean) args[0];
	}
}
