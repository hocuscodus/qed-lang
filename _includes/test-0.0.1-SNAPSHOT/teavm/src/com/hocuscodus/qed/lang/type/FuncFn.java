/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.Declaration.DecEnv;
import com.hocuscodus.qed.lang.Exp;
import com.hocuscodus.qed.lang.Exp.FuncExp;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.QEDProcess;

public class FuncFn extends PredefinedType {
	public FuncFn(Type returnType, String name, Type[] params) {
		super(returnType, name, params);
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
//		if (!isPureFunction())
//			write(out, opCode);
//
		for (int index = 0; index < ((FuncExp) exp).args.size(); index++)
			((FuncExp) exp).args.get(index).write(env, path, out);

		super.writeExp(exp, env, path, out);
		write(out, ((FuncExp) exp).args.size());
	}

	public void eval(QEDProcess process, Call call) {
		Object[] args = new Object[call.read(process)];

		for (int index = 0; index < args.length; index++)
			args[index] = call.stack.pop();

		Object value = eval(process, call, args);

		if (value != null)
			call.stack.push(value);
	}

	public Object eval(QEDProcess process, Call call, Object[] args) {
		return null;
	}

	public Exp findVarPath(Exp baseExp, String field, List<Exp> args, boolean virtualFlag) {
		return null;
	}

	public boolean isPureFunction() {
		return true;
	}

	@Override
	public String toString(Exp exp) {
		List<Exp> args = ((FuncExp) exp).args;
		StringBuffer array = new StringBuffer(name);

		if (args != null) {
			array.append('(');

			for (int index = 0; index < args.size(); index++)
				array.append(index != 0 ? ", " : "").append(args.get(index));

			array.append(')');
		}

		return array.toString();
	}
}
