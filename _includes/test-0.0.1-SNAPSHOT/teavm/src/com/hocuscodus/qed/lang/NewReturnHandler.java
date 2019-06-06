/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang;

public class NewReturnHandler implements ReturnHandler {
	public static NewReturnHandler obj = new NewReturnHandler();

	public void onPause(QEDProcess process, Call call) {
		onNew(process, call);
//		call.parent.onPause(process);
	}

	public void onHalt(QEDProcess process, Call call) {
		onNew(process, call);
//		process.remove(call, call.obj);
	}

	public void onReturn(QEDProcess process, Call call, Object value) {
		onNew(process, call);
//		process.remove(call, call.obj);
	}

	public void onNew(QEDProcess process, Call call) {
		process.remove(call, call.obj);
		call.returnHandler = AfterNewReturnHandler.obj;
	}

	public static class AfterNewReturnHandler implements ReturnHandler {
		public static AfterNewReturnHandler obj = new AfterNewReturnHandler();

		public void onPause(QEDProcess process, Call call) {
		}

		public void onHalt(QEDProcess process, Call call) {
		}

		public void onReturn(QEDProcess process, Call call, Object value) {
		}
	}
}
