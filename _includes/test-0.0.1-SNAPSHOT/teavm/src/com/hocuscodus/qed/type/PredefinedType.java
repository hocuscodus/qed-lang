/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import com.hocuscodus.qed.lang.Declaration.DecEnv;
import com.hocuscodus.qed.lang.Exp;

public class PredefinedType extends Type {
	public PredefinedType() {
		super();
	}

	public PredefinedType(Type returnType, String name, Type[] params) {
		super(returnType, name, params);
	}

	public Type getReturnType(Exp exp, DecEnv env) {
		return this;
    }

	public boolean isPredefined() {
		return true;
	}
}
