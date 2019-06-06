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
import com.hocuscodus.qed.lang.Exp.UnaryExp;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.QEDProcess;

public class UnaryFn extends Type {
	public UnaryFn(String name, Type[] params) {
		super(null, name, params);
	}

	public UnaryFn(Type type, String name, Type[] params) {
		super(type, name, params);
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		if (getExp(exp) != null)
			getExp(exp).write(env, path, out);

		super.writeExp(exp, env, path, out);
	}

	public void writeExpAssignment(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		getExp(exp).write(env, path, out);
	}

	public void eval(QEDProcess process, Call call) {
		if (call.stack.size() != 0) {
			Object value = eval(process, call, call.stack.pop());

			if (value != null)
				call.stack.push(value);
		}
	}

	public Object eval(QEDProcess process, Call call, Object value) {
		return null;
	}

	protected static Exp getExp(Exp exp) {
		return ((UnaryExp) exp).exp;
	}

	public Type getReturnType(Exp exp, DecEnv env) {
		return returnType != null ? returnType : getExp(exp).getReturnType(env);
	}

	public boolean makerefs(Exp exp, Path path) {
        return getExp(exp).makerefs(path);
	}

	public boolean findArrayFieldExp(Exp exp, Exp indexExp) {
		return getExp(exp).findArrayFieldExp(indexExp);
	}

	public String toString(Exp exp) {
		return getExp(exp).toString();
	}
}
