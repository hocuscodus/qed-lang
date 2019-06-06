/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import com.hocuscodus.qed.lang.ArrayCall;
import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.QEDProcess;


public class InitArrayValueFn extends Type {
	public void eval(QEDProcess process, Call call) {
//		// error, shall never come here
//		process.println("Error, shall never come here");
		ArrayCall arrayCall = (ArrayCall) call;
		Object[] array = (Object[]) call.obj;

		array[arrayCall.index++] = call.stack.isEmpty() ? null : call.stack.pop();

		if (arrayCall.index == array.length) 
			arrayCall.onReturn(process, arrayCall.index != 0 ? array[arrayCall.index - 1] : null);
	}
}
