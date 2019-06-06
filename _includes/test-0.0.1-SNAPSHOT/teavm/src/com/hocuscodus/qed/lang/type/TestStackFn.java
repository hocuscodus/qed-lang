/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import com.hocuscodus.qed.lang.AmpEntry;
import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.QEDCall;
import com.hocuscodus.qed.lang.QEDProcess;

public class TestStackFn extends UnaryFn {
	public TestStackFn() {
		super(null, new Type[] {Type.intType});
	}

	public Object eval(QEDProcess process, Call call, Object value) {
		QEDCall qedCall = call.getQEDCall();
		AmpEntry entry = qedCall.ampStack.peek();
		Object[] lNumEntries = (Object[]) entry.dataIndex;
		int numEntries = (Integer) ((Object[]) lNumEntries[0])[(Integer) lNumEntries[1]];

		if (entry.index >= numEntries)
			qedCall.instPointer += (Integer) value;

		return null;
	}
}
