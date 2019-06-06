/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import java.util.Stack;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.QEDProcess;

public class ElseType extends FuncFn {
	public ElseType() {
		super(Type.booleanType, "else", new Type[0]);
	}

	public Object eval(QEDProcess process, Call call, Object[] args) {
		Stack<Object> stack = call.getQEDCall().localStack.peek();

		return !((Boolean) stack.elementAt(stack.size() - 1));
	}
}
