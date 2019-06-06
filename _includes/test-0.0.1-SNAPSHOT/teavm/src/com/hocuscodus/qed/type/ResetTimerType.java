/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Timer;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.Declaration.DecEnv;
import com.hocuscodus.qed.lang.Exp;
import com.hocuscodus.qed.lang.Exp.FuncExp;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.PredefObj;
import com.hocuscodus.qed.lang.QEDProcess;

public class ResetTimerType extends FuncFn {
	public ResetTimerType() {
		super(Type.intType, "reset", new Type[0]);
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		((FuncExp) exp).exp.write(env, path, out);
		super.writeExp(exp, env, path, out);
	}

	public Object eval(QEDProcess process, Call call, Object[] args) {
		Object obj = call.stack.pop();
		Object[] objs = (Object[]) ((PredefObj) obj).value;
		Integer delay = args.length != 0 ? (Integer) args[0] : (Integer) objs[0];
		Object timer = (Timer) objs[1];
		Object[] timerArgs = new Object[] {timer, obj, process, delay};

		process.resetTimer(timerArgs);
		objs[1] = timerArgs[0];
		return null;
	}
}
