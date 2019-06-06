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

public class ReturnFn extends FuncFn {
	public ReturnFn() {
		super(new BooleanType(), "return", new Type[] {intType});
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		super.writeExp(exp, env, path, out);
		write(out, ((FuncExp) exp).args.size() != 0 ? 1 : 0);
	}

	public Object eval(QEDProcess process, Call call, Object[] args) {
		Object value = args.length != 0 ? args[0] : null;

		call.getQEDCall().onReturn(process, value);
		return null;
	}
}
