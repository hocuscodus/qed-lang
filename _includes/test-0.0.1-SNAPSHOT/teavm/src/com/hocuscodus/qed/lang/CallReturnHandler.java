/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang;

public class CallReturnHandler implements ReturnHandler {
	public static CallReturnHandler obj = new CallReturnHandler();

	public void onPause(QEDProcess process, Call call) {
		call.parent.onPause(process);
	}

	public void onHalt(QEDProcess process, Call call) {
		call.parent.onPause(process);
	}

	public void onReturn(QEDProcess process, Call call, Object value) {
		process.remove(call, value);
		if (call.obj instanceof QEDObj)
		process.push(new QEDCall(call.parent, (QEDObj) call.obj, ExecReturnHandler.obj));
		else
			call = call;
	}
}
