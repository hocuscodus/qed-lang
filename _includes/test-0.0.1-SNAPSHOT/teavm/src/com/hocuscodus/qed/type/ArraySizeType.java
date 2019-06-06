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

public class ArraySizeType extends FuncFn {
	public ArraySizeType() {
		super(Type.intType, "size", new Type[0]);
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		((FuncExp) exp).exp.write(env, path, out);
		super.writeExp(exp, env, path, out);
	}

	public Object eval(QEDProcess process, Call call, Object[] args) {
		return (Integer) ((Object[]) call.stack.pop()).length;
	}
}
