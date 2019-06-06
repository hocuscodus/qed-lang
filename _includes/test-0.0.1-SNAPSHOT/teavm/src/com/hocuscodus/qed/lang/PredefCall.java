/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang;

import com.hocuscodus.qed.lang.type.PredefinedFuncFn;
import com.hocuscodus.qed.lang.type.Type;

public class PredefCall extends Call {
	public Object[] args;

	public PredefCall(Call parent, PredefinedFuncFn fn, Object[] args, ReturnHandler returnHandler) {
		super(parent, new PredefObj(fn), returnHandler);
		((PredefObj) obj).handlerCall = this;
		this.args = args;
	}

	public void process(QEDProcess process) {
		Object value = ((PredefObj) obj).fn.exec(process, this, args);

		if (value == Type.haltFn)
			Type.haltFn.eval(process, this);
		else
			onReturn(process, value);
    }
}
