/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.PredefCall;
import com.hocuscodus.qed.lang.QEDProcess;
import com.hocuscodus.qed.lang.ReturnHandler;

public class PredefinedFuncFn extends FuncFn {
	public PredefinedFuncFn(Type returnType, String name, Type[] params) {
		super(returnType, name, params);
	}

	public boolean isPureFunction() {
		return false;
	}

	public Object eval(final QEDProcess process, final Call call, Object[] args) {
		process.push(new PredefCall(call, this, args, (ReturnHandler) call.stack.pop()));
		return null;
	}

	public Object exec(QEDProcess process, Call call, Object[] args) {
		return haltFn;
	}
}
