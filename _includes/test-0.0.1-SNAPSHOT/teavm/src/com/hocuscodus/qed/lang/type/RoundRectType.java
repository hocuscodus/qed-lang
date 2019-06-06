/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.QEDProcess;

public class RoundRectType extends FuncFn {
	public RoundRectType() {
		super(new BooleanType(), "roundrect", new Type[] {new IntType()});
	}

	public Object eval(QEDProcess process, Call call, Object[] args) {
		int arcNum = (Integer) args[0];
		UIContext uiContext = getUIContext();

		if (uiContext != null)
			process.fillRoundRectangle(uiContext.pos, uiContext.size, new int[] {arcNum, arcNum});

		return null;
	}
}
