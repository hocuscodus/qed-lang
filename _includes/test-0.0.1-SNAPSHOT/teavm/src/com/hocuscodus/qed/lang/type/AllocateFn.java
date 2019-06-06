/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.QEDProcess;

public class AllocateFn extends UnaryFn {
	public AllocateFn() {
		super(null, new Type[] {Type.intType});
	}

	public Object eval(QEDProcess process, Call call, Object value) {
		return new Object[(Integer) value];
	}
}
