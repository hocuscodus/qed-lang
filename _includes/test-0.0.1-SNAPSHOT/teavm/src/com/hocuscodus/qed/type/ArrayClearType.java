/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

public class ArrayClearType extends FuncFn {
	public ArrayClearType() {
		super(Type.intType, "remove", new Type[0]);
	}
//
//	@Override
//	public boolean exec(Env env, List<Exp> args, TypeInstance typeInstance) {
//		ArrayTypeInstance arrayTypeInstance = (ArrayTypeInstance) ((UnaryExp) ((PredefinedTypeInstance) typeInstance).baseExp).exp.eval(env);
//
//		arrayTypeInstance.remove(0, arrayTypeInstance.values.length);
//		return typeInstance.callReturn(typeInstance);
//	}
}
