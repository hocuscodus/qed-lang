/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import com.hocuscodus.qed.lang.Declaration.DecEnv;
import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.Exp;
import com.hocuscodus.qed.lang.ObjEnv;
import com.hocuscodus.qed.lang.QEDProcess;

public class ObjType extends FuncFn {
	public ObjType() {
		super(new BooleanType(), "obj", new Type[0]);
	}

	public Type getReturnType(Exp exp, DecEnv env) {
		return env.declarations[env.declarations.length - 1];
	}

	public Object eval(QEDProcess process, Call call, Object[] args) {
		ObjEnv env = call.getQEDCall().env;

		return env.objs[env.objs.length - 1];
	}
//
//	@Override
//	public boolean exec(Env env, List<Exp> args, TypeInstance typeInstance) {
//		return typeInstance.callReturn(env.getParm());
//	}
}
