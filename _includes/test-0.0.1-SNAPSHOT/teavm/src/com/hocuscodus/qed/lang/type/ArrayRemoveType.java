/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

public class ArrayRemoveType extends FuncFn {
	public ArrayRemoveType() {
		super(Type.intType, "remove", new Type[0]);
	}
//
//	@Override
//	public boolean exec(Env env, List<Exp> args, TypeInstance typeInstance) {
//		ArrayTypeInstance arrayTypeInstance = (ArrayTypeInstance) ((UnaryExp) ((PredefinedTypeInstance) typeInstance).baseExp).exp.eval(env);
//		int index = (Integer) args.get(0).eval(env);
//		int size = args.size() > 1 ? (Integer) args.get(1).eval(env) : arrayTypeInstance.values.length;
//
//		arrayTypeInstance.remove(index, size);
//		return typeInstance.callReturn(typeInstance);
//	}
}
