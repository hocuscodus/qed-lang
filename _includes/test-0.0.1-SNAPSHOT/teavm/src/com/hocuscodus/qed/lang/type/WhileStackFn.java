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

public class WhileStackFn extends BinaryFn {
	public WhileStackFn() {
		super(null, new Type[] {Type.intType});
	}

	public Object eval(QEDProcess process, Call call, Object value1, Object value2) {
		QEDCall qedCall = call.getQEDCall();

		if ((Boolean) value1) {
			AmpEntry entry = qedCall.ampStack.peek();
			Object[] lNumEntries = (Object[]) entry.dataIndex;
			int numEntr = (Integer) ((Object[]) lNumEntries[0])[(Integer) lNumEntries[1]];

			((Object[]) lNumEntries[0])[(Integer) lNumEntries[1]] = ++numEntr;

			if (entry.loopIndex.getNumLevels() != 0) {
				Object[] data = qedCall.env.objs[qedCall.env.objs.length - 2];
				LValue entries = QEDObj.getLValue(data, entry.loopIndex);
				Object[][] newEntries = new Object[numEntr][];

				System.arraycopy(entries.data[entries.offset], 0, newEntries, 0, numEntr - 1);
				entries.data[entries.offset] = newEntries;
			}
		}
		else
			qedCall.instPointer += (Integer) value2;

		return null;
	}
}
