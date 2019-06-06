/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang;

public class ExecReturnHandler implements ReturnHandler {
	public static ExecReturnHandler obj = new ExecReturnHandler();

	public void onPause(QEDProcess process, Call call) {
		// TODO illegal outcome, report it
		System.out.println("ILLEGAL PAUSE IN EXEC EXPRESSION...");
	}

	public void onHalt(QEDProcess process, Call call) {
		process.remove(call, null);
	}

	public void onReturn(QEDProcess process, Call call, Object value) {
		// TODO illegal outcome, report it
		process.remove(call, null);
	}
}
