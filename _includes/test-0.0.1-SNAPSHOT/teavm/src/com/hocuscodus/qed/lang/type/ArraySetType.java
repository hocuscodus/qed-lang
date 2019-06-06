/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

public class ArraySetType extends FuncFn {
	public ArraySetType() {
		super(Type.intType, "set", new Type[0]);
	}
//
//	@Override
//	public boolean exec(Env env, List<Exp> args, TypeInstance typeInstance) {
//		ArrayTypeInstance arrayTypeInstance = (ArrayTypeInstance) ((UnaryExp) ((PredefinedTypeInstance) typeInstance).baseExp).exp.eval(env);
//		Object value = args.get(0).eval(env);
//
//		if (arrayTypeInstance.values.length == 1)
//			arrayTypeInstance.values[0] = value;
//		else {
//			if (arrayTypeInstance.values.length != 0)
//				arrayTypeInstance.remove(0, arrayTypeInstance.values.length);
//
//			arrayTypeInstance.add(value, 0);
//		}
//
//		return typeInstance.callReturn(typeInstance);
//	}
}
