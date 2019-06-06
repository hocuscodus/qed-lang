/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.Declaration.DecEnv;
import com.hocuscodus.qed.lang.Exp;
import com.hocuscodus.qed.lang.Exp.FuncExp;
import com.hocuscodus.qed.lang.Exp.ValueExp;
import com.hocuscodus.qed.lang.Obj;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.PredefObj;
import com.hocuscodus.qed.lang.QEDProcess;

public class TimerType extends PredefinedFuncFn {
	public TimerType() {
		super(Type.voidType, "Timer", new Type[] {Type.intType});
	}

	@Override
	public Object exec(QEDProcess process, Call call, Object[] args) {
		Object timer = process.setTimer(((Integer) args[0]), process, (Obj) call.obj);
		((PredefObj) call.obj).value = new Object[] {args[0], timer};
		return super.exec(process, call, args);
	}

	public void writeUninitExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		ArrayList<Exp> args = new ArrayList<Exp>();

		args.add(new ValueExp(Type.intType, -1));
		new FuncExp(Type.resetTimerType, exp, args).write(env, path, out);
//		new UnaryExp(Type.cleanObjectFn, new ValueExp(Type.intType, id)).write(env, path, out);
	}

	public Exp findVarPath(Exp baseExp, String field, List<Exp> args, boolean virtualFlag) {
		Exp exp = null;

		if (field.equals("reset"))
			exp = new FuncExp(resetTimerType, baseExp, args);

		return exp;
	}
}
