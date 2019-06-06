/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.QEDProcess;

public class PrintType extends FuncFn {
	public PrintType() {
		super(Type.voidType, "print", new Type[] {Type.stringType});
	}

	public Object eval(QEDProcess process, Call call, Object[] args) {
		process.print(args[0].toString());
		return null;
	}
}
