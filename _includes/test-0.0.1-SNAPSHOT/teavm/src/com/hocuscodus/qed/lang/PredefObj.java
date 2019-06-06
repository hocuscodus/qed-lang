/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang;

import com.hocuscodus.qed.lang.type.PredefinedFuncFn;

public class PredefObj extends Obj {
	public PredefinedFuncFn fn;
	public Object value;

	public PredefObj(PredefinedFuncFn fn) {
		this.fn = fn;
	}
}
