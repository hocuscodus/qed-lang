/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import com.hocuscodus.qed.lang.Declaration.DecEnv;
import com.hocuscodus.qed.lang.Declaration.Module;
import com.hocuscodus.qed.lang.Exp;
import com.hocuscodus.qed.lang.Exp.ValueExp;

public class ModuleType extends ValueType {
	public ModuleType() {
	}

	public Type getReturnType(Exp exp, DecEnv env) {
		Module module = (Module) ((ValueExp) exp).value;

		return null;//module.topMtx;
	}
//
//	public Object eval(Env env, Exp exp) {
//		Module module = (Module) super.eval(env, exp);
//
//		return new NodeInfo(module.instance);
//	}
}
