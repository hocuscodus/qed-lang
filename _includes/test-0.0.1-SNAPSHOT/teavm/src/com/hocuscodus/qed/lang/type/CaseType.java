/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import java.util.Stack;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.QEDProcess;
import com.hocuscodus.qed.lang.type.SwitchType.SwitchValue;

public class CaseType extends FuncFn {
	public CaseType() {
		super(Type.booleanType, "case", new Type[] {intType});
	}

	public Object eval(QEDProcess process, Call call, Object[] args) {
		Stack<Stack<Object>> localStack = call.getQEDCall().localStack;
		Stack<Object> stack = localStack.elementAt(localStack.size() - 2);
		SwitchValue switchValue = (SwitchValue) stack.peek();
		boolean caseTrue = !switchValue.used && switchValue.value.equals(args[0]);

		if (caseTrue)
			switchValue.used = true;

		return caseTrue;
	}
//
//	@Override
//	public boolean exec(Env env, List<Exp> args, TypeInstance typeInstance) {
//		int index;
//		StorageUnitCode unit = env.storageCode.topStorageUnit;
//
//		for (index = 0; index < env.path.getNumLevels() - 1; index++) {
//			int ndx = env.path.get(index);
//
//			if (index < env.path.getNumLevels() - 2 || ndx >= 0)
//				unit = (StorageUnitCode) (ndx >= 0 ? unit.childStorageUnits.storageUnits[ndx] : unit.arrayStorageUnits.storageUnits[-ndx - 1]);
//		}
//
//		SwitchValue switchValue = (SwitchValue) unit.value;
//		boolean caseTrue = !switchValue.used && switchValue.value.equals(args.get(0).eval(env));
//
//		if (caseTrue)
//			switchValue.used = true;
//
//		return typeInstance.callReturn(new Boolean(caseTrue));
//	}
}
