/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.Exp;
import com.hocuscodus.qed.lang.Exp.ValueExp;
import com.hocuscodus.qed.lang.QEDProcess;

public class VarIndexFn2 extends BinaryFn {
	public VarIndexFn2() {
		super(Type.intType, "&", new Type[] {Type.intType});
	}

	public Object eval(QEDProcess process, Call call, Object value1, Object value2) {
		if (call.getQEDCall().ampStack.isEmpty())
			call=call;
		int ndx = (Integer) value2;
		if (ndx >= call.getQEDCall().ampStack.size())
			ndx = 0;
		return call.getQEDCall().ampStack.elementAt(/*call.ampStack.size() - 1 - */ndx).index;
	}

	public String toString(Exp exp) {
		int numLevels = (int) ((ValueExp) getExp2(exp)).getInteger();

		return "&" + numLevels;
	}
}
