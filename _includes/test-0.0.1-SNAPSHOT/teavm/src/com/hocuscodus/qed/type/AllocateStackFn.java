/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import com.hocuscodus.qed.lang.AmpEntry;
import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.LValue;
import com.hocuscodus.qed.lang.QEDCall;
import com.hocuscodus.qed.lang.QEDObj;
import com.hocuscodus.qed.lang.QEDProcess;

public class AllocateStackFn extends Type {
	public void eval(QEDProcess process, Call call) {
		QEDCall qedCall = call.getQEDCall();
		AmpEntry entry = qedCall.ampStack.peek();
		Object[] lNumEntries = (Object[]) entry.dataIndex;
		int numEntries = (Integer) ((Object[]) lNumEntries[0])[(Integer) lNumEntries[1]];
		Object[] data = qedCall.env.objs[qedCall.env.objs.length - 2];
		LValue lValue = QEDObj.getLValue(data, entry.loopIndex);
		if (lValue.offset >= lValue.data.length)
			lValue=lValue;
		lValue.data[lValue.offset] = new Object[numEntries][];
	}
}
