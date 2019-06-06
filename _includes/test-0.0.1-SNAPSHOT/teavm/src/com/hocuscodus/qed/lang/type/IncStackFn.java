/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import com.hocuscodus.qed.lang.AmpEntry;
import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.QEDProcess;

public class IncStackFn extends Type {
	public void eval(QEDProcess process, Call call) {
		AmpEntry entry = call.getQEDCall().ampStack.peek();

		entry.index++;
	}
}
