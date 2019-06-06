/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

public class RefreshType extends FuncFn {
	public RefreshType() {
		super(new BooleanType(), "refresh", new Type[] {});
	}
//
//	@Override
//	public boolean exec(Env env, List<Exp> args, TypeInstance typeInstance) {
//		env.callRefresh();
//		return typeInstance.callReturn(true);
//	}
}
