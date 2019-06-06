/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import com.hocuscodus.qed.lang.AmpEntry;
import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.QEDCall;
import com.hocuscodus.qed.lang.QEDObj;
import com.hocuscodus.qed.lang.QEDProcess;

public class SaveStackFn extends UnaryFn {
	public SaveStackFn() {
		super(null, new Type[] {Type.intType});
	}

	public Object eval(QEDProcess process, Call call, Object value) {
		QEDCall qedCall = call.getQEDCall();
		AmpEntry entry = qedCall.ampStack.peek();
		Object[] data = qedCall.env.objs[qedCall.env.objs.length - 2];
		Object[][] entries = (Object[][]) QEDObj.getValue(data, entry.loopIndex);

		entries[entry.index] = (Object[]) value;
		qedCall.env.set((Object[]) value);
		return null;
	}
}
