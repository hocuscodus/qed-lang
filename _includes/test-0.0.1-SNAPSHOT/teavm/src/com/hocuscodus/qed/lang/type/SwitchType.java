/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.QEDProcess;

public class SwitchType extends FuncFn {
	public static class SwitchValue {
		public Object value;
		public boolean used;

		public SwitchValue(Object value) {
			this.value = value;
		}
	}

	public SwitchType() {
		super(Type.floatType, "switch", new Type[] {intType});
	}

	public Object eval(QEDProcess process, Call call, Object[] args) {
		return new SwitchValue(args[0]);
	}
//
//	@Override
//	public boolean exec(Env env, List<Exp> args, TypeInstance typeInstance) {
//		int index;
//		StorageUnitCode unit = env.storageCode.topStorageUnit;
//
//		for (index = 0; index < env.path.getNumLevels(); index++) {
//			int ndx = env.path.get(index);
//
//			unit = (StorageUnitCode) (ndx >= 0 ? unit.childStorageUnits.storageUnits[ndx] : unit.arrayStorageUnits.storageUnits[-ndx - 1]);
//		}
//
//		return typeInstance.callReturn();
//	}
}
