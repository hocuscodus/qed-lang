/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.Declaration.DecEnv;
import com.hocuscodus.qed.lang.Exp;
import com.hocuscodus.qed.lang.Exp.FuncExp;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.PredefObj;
import com.hocuscodus.qed.lang.QEDObj;
import com.hocuscodus.qed.lang.QEDProcess;

public class AddCoListType extends FuncFn {
	public AddCoListType() {
		super(Type.voidType, "add", new Type[] {Type.intType});
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		((FuncExp) exp).exp.write(env, path, out);
		super.writeExp(exp, env, path, out);
	}

	@SuppressWarnings("unchecked")
	public Object eval(QEDProcess process, Call call, Object[] args) {
		Object obj = call.stack.pop();
		Object[] objs = (Object[]) ((PredefObj) obj).value;
		QEDObj fn = (QEDObj) args[0];
		ArrayList<PredefObj> list = (ArrayList<PredefObj>) objs[0];

//		list.add(fn);
		return null;
	}
}
