/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.QEDProcess;

public class PrintLnType extends FuncFn {
	public PrintLnType() {
		super(Type.voidType, "println", new Type[] {Type.stringType});
	}

	public Object eval(QEDProcess process, Call call, Object[] args) {
		process.println(args[0].toString());
		return null;
	}
}
