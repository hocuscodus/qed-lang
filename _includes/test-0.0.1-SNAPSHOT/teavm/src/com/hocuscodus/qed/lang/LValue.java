/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang;

public class LValue {
	public Object[] data;
	public int offset;

	public LValue(Object[] data, int offset) {
		this.data = data;
		this.offset = offset;
	}
}
