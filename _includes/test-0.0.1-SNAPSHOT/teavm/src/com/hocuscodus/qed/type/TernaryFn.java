/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.hocuscodus.qed.lang.Declaration.DecEnv;
import com.hocuscodus.qed.lang.Exp;
import com.hocuscodus.qed.lang.Exp.BinaryExp;
import com.hocuscodus.qed.lang.Exp.TernaryExp;
import com.hocuscodus.qed.lang.Exp.UnaryExp;
import com.hocuscodus.qed.lang.Exp.ValueExp;
import com.hocuscodus.qed.lang.Path;

public class TernaryFn extends Type {
	public TernaryFn() {
		super(Type.voidType, "?:", new Type[] {Type.voidType, Type.voidType, Type.voidType});
	}

	public Type getReturnType(Exp exp, DecEnv env) {
		return (((TernaryExp) exp).value2).getReturnType(env);
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		TernaryExp ternaryExp = (TernaryExp) exp;
		ByteArrayOutputStream bodyContent = new ByteArrayOutputStream();
		byte[] body;

		ternaryExp.value2.write(env, path, bodyContent);
		body = bodyContent.toByteArray();
		new BinaryExp(Type.ifStackFn, ternaryExp.value1, new ValueExp(Type.intType, body.length + 5)).write(env, path, out);
		out.write(body);
		bodyContent.reset();
		ternaryExp.value3.write(env, path, bodyContent);
		body = bodyContent.toByteArray();
		new UnaryExp(Type.jumpFn, new ValueExp(Type.intType, body.length)).write(env, path, out);
		out.write(body);
	}

	public String toString(Exp exp) {
		Exp value1 = ((TernaryExp) exp).value1;
		Exp value2 = ((TernaryExp) exp).value2;
		Exp value3 = ((TernaryExp) exp).value3;

		return "" + value1 + " ? " + value2 + " : " + value3;
	}
}
