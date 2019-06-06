/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang;

public class EventReturnHandler implements ReturnHandler {
	public static EventReturnHandler obj = new EventReturnHandler();

	public void onPause(QEDProcess process, Call call) {
	}

	public void onHalt(QEDProcess process, Call call) {
		process.remove(call, null);
	}

	public void onReturn(QEDProcess process, Call call, Object value) {
		process.remove(call, null);
		((Obj) call.obj).onReturn(process, value);
	}
}
