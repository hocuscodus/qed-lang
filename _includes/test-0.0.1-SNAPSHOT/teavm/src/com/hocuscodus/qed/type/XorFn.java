/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

public class XorFn extends BinaryOpFn {
	public XorFn() {
		super("^");
	}

	public Object eval(Type type, Object value1, Object value2) {
		return type.xor(value1, value2);
	}
}
