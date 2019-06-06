/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.QEDProcess;

public class RectType extends FuncFn {
	public RectType() {
		super(new BooleanType(), "rect", new Type[0]);
	}

	public Object eval(QEDProcess process, Call call, Object[] args) {
		UIContext uiContext = getUIContext();

		if (uiContext != null)
			process.fillRectangle(uiContext.pos, uiContext.size);

		return null;
	}
}
