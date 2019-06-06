/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang;

public interface ReturnHandler {
	public void onPause(QEDProcess process, Call call);
	public void onHalt(QEDProcess process, Call call);
	public void onReturn(QEDProcess process, Call call, Object value);
}
