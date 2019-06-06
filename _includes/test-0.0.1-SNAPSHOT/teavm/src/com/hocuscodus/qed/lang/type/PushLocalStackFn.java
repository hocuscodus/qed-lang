/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import java.util.Stack;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.QEDProcess;

public class PushLocalStackFn extends Type {
	public void eval(QEDProcess process, Call call) {
		call.getQEDCall().localStack.push(new Stack<Object>());
	}
}
