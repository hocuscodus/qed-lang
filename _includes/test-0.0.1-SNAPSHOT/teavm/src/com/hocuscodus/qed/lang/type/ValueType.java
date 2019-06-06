/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import com.hocuscodus.qed.lang.Env;
import com.hocuscodus.qed.lang.Exp;
import com.hocuscodus.qed.lang.Exp.ValueExp;

public class ValueType extends PredefinedType {
	public ValueType() {
		super();
	}

	public Object eval(Env env, Exp exp) {
		return ((ValueExp) exp).value;
	}

	@Override
	public String toString(Exp exp) {
		return exp instanceof ValueExp ? ((ValueExp) exp).value.toString() : "int";
	}
}
