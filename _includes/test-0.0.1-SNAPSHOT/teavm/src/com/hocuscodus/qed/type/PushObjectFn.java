/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.Declaration;
import com.hocuscodus.qed.lang.Declaration.FunctionDeclaration;
import com.hocuscodus.qed.lang.ObjEnv;
import com.hocuscodus.qed.lang.QEDCall;
import com.hocuscodus.qed.lang.QEDObj;
import com.hocuscodus.qed.lang.QEDProcess;

public class PushObjectFn extends UnaryFn {
	public PushObjectFn() {
		super(null, new Type[] {Type.intType});
	}

	public Object eval(QEDProcess process, Call call, Object value) {
		int functionId = (Integer) value;
		FunctionDeclaration func = (FunctionDeclaration) Declaration.funcs.get(functionId);
		Object[] info = (Object[]) call.stack.pop();
		QEDObj parentObj = (QEDObj) info[0];
		ObjEnv parentEnv = (ObjEnv) info[1];

		process.push(new QEDCall(call, parentObj, parentEnv, func, call.read(process)));
		return null;
	}
}
