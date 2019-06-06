/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.QEDProcess;

public class DevNullType extends Type {
//	public DevNullType() {
//		super(Type.voidType, "", new Type[1]);
//	}
//
	public void eval(QEDProcess process, Call call) {
		if (call.stack.isEmpty())
			call = call;

		call.getQEDCall().localStack.peek().push(call.stack.pop());

		if (!call.stack.isEmpty())
			call = call;
	}
}
