/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import java.util.ArrayList;
import java.util.List;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.Exp;
import com.hocuscodus.qed.lang.Exp.FuncExp;
import com.hocuscodus.qed.lang.PredefObj;
import com.hocuscodus.qed.lang.QEDObj;
import com.hocuscodus.qed.lang.QEDProcess;

public class CoListType extends PredefinedFuncFn {
	public CoListType() {
		super(Type.voidType, "CoList", new Type[0]);
	}

	@Override
	public Object exec(QEDProcess process, Call call, Object[] args) {
		ArrayList<PredefObj> list = new ArrayList<PredefObj>();

		list.add(null);
		((PredefObj) call.obj).value = new Object[] {list, new int[] {0}};
		return super.exec(process, call, args);
	}

	public Exp findVarPath(Exp baseExp, String field, List<Exp> args, boolean virtualFlag) {
		Exp exp = null;

		if (field.equals("yield"))
			exp = new FuncExp(yieldCoListType, baseExp, args);

		if (field.equals("process"))
			exp = new FuncExp(processCoListType, baseExp, args);

		if (field.equals("add"))
			exp = new FuncExp(addCoListType, baseExp, args);

		return exp;
	}
}
