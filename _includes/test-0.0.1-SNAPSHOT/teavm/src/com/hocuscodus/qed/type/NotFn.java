/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.Exp;
import com.hocuscodus.qed.lang.QEDProcess;

public class NotFn extends UnaryFn {
	public NotFn() {
		super("!", new Type[] {Type.booleanType});
	}

	public Object eval(QEDProcess process, Call call, Object value) {
		return !((Boolean) value);
	}

	public String toString(Exp exp) {
		return "!" + super.toString(exp);
	}
}
