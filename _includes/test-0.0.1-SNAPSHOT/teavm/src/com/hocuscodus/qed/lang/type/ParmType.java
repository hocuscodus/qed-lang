/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import com.hocuscodus.qed.lang.Declaration.DecEnv;
import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.Exp;
import com.hocuscodus.qed.lang.QEDProcess;

public class ParmType extends FuncFn {
	public ParmType() {
		super(new BooleanType(), "parm", new Type[0]);
	}

	public Type getReturnType(Exp exp, DecEnv env) {
		return this;
	}

	public Object eval(QEDProcess process, Call call, Object[] args) {
		return call.getQEDCall().parm;
	}
//
//	@Override
//	public boolean exec(Env env, List<Exp> args, TypeInstance typeInstance) {
//		return typeInstance.callReturn(env.getParm());
//	}
}
