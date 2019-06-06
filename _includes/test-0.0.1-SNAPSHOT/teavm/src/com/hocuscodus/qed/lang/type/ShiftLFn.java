/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.QEDProcess;

public class ShiftLFn extends BinaryFn {
	public ShiftLFn() {
		super("<<", new Type[] {Type.intType, Type.intType});
	}

	public Object eval(QEDProcess process, Call call, Object value1, Object value2) {
		return ((Integer) value1) << ((Integer) value2);
	}
}
