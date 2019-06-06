/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang;

public class ProcessReturnHandler extends CallReturnHandler {
	public static ProcessReturnHandler obj = new ProcessReturnHandler();

	public void onPause(QEDProcess process, Call call) {
	}

	public void onHalt(QEDProcess process, Call call) {
	}

	public void onReturn(QEDProcess process, Call call, Object value) {
		super.onReturn(process, call, value);
//		process.remove(call, null);
		process.exit((Integer) value);
	}
}
