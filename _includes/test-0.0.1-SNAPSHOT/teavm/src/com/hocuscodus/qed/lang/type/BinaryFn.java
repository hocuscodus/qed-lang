/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import java.io.IOException;
import java.io.OutputStream;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.Declaration.DecEnv;
import com.hocuscodus.qed.lang.Exp;
import com.hocuscodus.qed.lang.Exp.BinaryExp;
import com.hocuscodus.qed.lang.Exp.ValueExp;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.QEDProcess;

public class BinaryFn extends Type {
	public BinaryFn(String name, Type[] params) {
		super(null, name, params);
	}

	public BinaryFn(Type type, String name, Type[] params) {
		super(type, name, params);
	}

	private void fn(Object object) {}
	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		getExp1(exp).write(env, path, out);
		if (getExp2(exp) != null)
		getExp2(exp).write(env, path, out);
		else
			new ValueExp(Type.intType, 0).write(env, path, out);
		super.writeExp(exp, env, path, out);
	}

	public void writeExpAssignment(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		getExp1(exp).write(env, path, out);
		getExp2(exp).write(env, path, out);
	}

	public void eval(QEDProcess process, Call call) {
		Object value2 = call.stack.pop();
		Object value1 = call.stack.pop();
		Object val1 = value1; // This shitty code is here to make TeaVM work fine...
		fn(val1);
		Object val2 = value2;
		fn(val2);
		Object value = eval(process, call, val1, val2);

		if (value != null)
			call.stack.push(value);
	}

	public Object eval(QEDProcess process, Call call, Object value1, Object value2) {
		return null;
	}

	public Exp getExp1(Exp exp) {
		return ((BinaryExp) exp).value1;
	}

	public Exp getExp2(Exp exp) {
		return ((BinaryExp) exp).value2;
	}

	public Type getReturnType(Exp exp, DecEnv env) {
		return returnType != null ? returnType : getExp1(exp).getReturnType(env);
	}

	public boolean makerefs(Exp exp, Path path) {
        return getExp1(exp).makerefs(path) && getExp2(exp).makerefs(path);
	}

	public String toString(Exp exp) {
		Exp exp2 = getExp2(exp);

		return exp2 != null ? "(" + getExp1(exp).type.name + ") " + exp2 : getExp1(exp).toString();
	}
}
