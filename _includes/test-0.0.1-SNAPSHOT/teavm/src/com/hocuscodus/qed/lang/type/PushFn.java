/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import com.hocuscodus.qed.lang.AmpEntry;
import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.QEDCall;
import com.hocuscodus.qed.lang.QEDProcess;

public class PushFn extends BinaryFn {
	public PushFn() {
		super(null, new Type[] {Type.intType});
	}

	public Object eval(QEDProcess process, Call call, Object value1, Object value2) {
		QEDCall qedCall = call.getQEDCall();

		qedCall.env.push();
		qedCall.ampStack.push(new AmpEntry((Object[]) value1, (Path) value2));
		return null;
	}
}
