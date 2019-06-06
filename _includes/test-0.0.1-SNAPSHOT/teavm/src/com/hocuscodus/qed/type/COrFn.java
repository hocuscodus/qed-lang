/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.Declaration.DecEnv;
import com.hocuscodus.qed.lang.Exp;
import com.hocuscodus.qed.lang.Exp.BinaryExp;
import com.hocuscodus.qed.lang.Exp.UnaryExp;
import com.hocuscodus.qed.lang.Exp.ValueExp;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.QEDProcess;

public class COrFn extends BinaryFn {
	public COrFn() {
		super(Type.booleanType, "||", new Type[] {Type.booleanType, Type.booleanType});
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		BinaryExp binaryExp = (BinaryExp) exp;

		if (binaryExp.value1.getReturnType(env) == Type.booleanType) {
			ByteArrayOutputStream bodyContent = new ByteArrayOutputStream();

			binaryExp.value2.write(env, path, bodyContent);

			byte[] body = bodyContent.toByteArray();

			new BinaryExp(Type.ifStackFn, new UnaryExp(Type.notFn, binaryExp.value1), new ValueExp(Type.intType, body.length + 5)).write(env, path, out);
			out.write(body);
			bodyContent.reset();
			new ValueExp(Type.booleanType, true).write(env, path, bodyContent);
			body = bodyContent.toByteArray();
			new UnaryExp(Type.jumpFn, new ValueExp(Type.intType, body.length)).write(env, path, out);
			out.write(body);
		}
		else
			super.writeExp(exp, env, path, out);
	}

	public Object eval(QEDProcess process, Call call, Object value1, Object value2) {
		return ((Boolean) value1) || ((Boolean) value2);
	}
}
