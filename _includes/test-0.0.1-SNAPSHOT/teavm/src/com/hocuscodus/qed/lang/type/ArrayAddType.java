/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

public class ArrayAddType extends FuncFn {
	public ArrayAddType() {
		super(Type.intType, "add", new Type[0]);
	}
//
//	@Override
//	public boolean exec(Env env, List<Exp> args, TypeInstance typeInstance) {
//		ArrayTypeInstance arrayTypeInstance = (ArrayTypeInstance) ((UnaryExp) ((PredefinedTypeInstance) typeInstance).baseExp).exp.eval(env);
//		Object value = args.get(0).eval(env);
//		int index = args.size() > 1 ? (Integer) args.get(1).eval(env) : arrayTypeInstance.values.length;
//
//		arrayTypeInstance.add(value, index);
//		return typeInstance.callReturn(typeInstance);
//	}
}
