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
import com.hocuscodus.qed.lang.Exp.UnaryExp;
import com.hocuscodus.qed.lang.Exp.ValueExp;
import com.hocuscodus.qed.lang.Obj;
import com.hocuscodus.qed.lang.ObjEnv;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.QEDCall;
import com.hocuscodus.qed.lang.QEDObj;
import com.hocuscodus.qed.lang.QEDProcess;

public class ArrowFn extends BinaryFn {
	public ArrowFn() {
		super("->", new Type[] {Type.voidType, Type.voidType});
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		ByteArrayOutputStream bodyContent = new ByteArrayOutputStream();
		getExp2(exp).write(env, path, bodyContent);
		new Exp(Type.haltFn).write(env, path, bodyContent);

		getExp1(exp).write(env, path, out);
		write(out, opCode);
		new UnaryExp(jumpFn, new ValueExp(intType, bodyContent.size())).write(env, path, out);
//		((FunctionDeclaration) env.declarations[0]).addEventHandler(((ByteArrayOutputStream) out).size(), ((InternalDeclaration) super.getReturnType(initExpr, env)).topDataNode.getDataIndex(fullPath));
//		((FunctionDeclaration) env.declarations[0]).addEventHandler(((ByteArrayOutputStream) out).size(), topDataNode.getDataIndex(fullPath));
		write(out, bodyContent.toByteArray());
	}

	public void eval(QEDProcess process, Call call) {
		Obj value = (Obj) call.stack.pop();

		if (value != null) {
			QEDCall qedCall = call.getQEDCall();

			if (value.handlerCall.parent.getQEDCall() != qedCall)
				qedCall = qedCall;
			value.handlerCall = qedCall;
			value.handlerEnv = new ObjEnv(qedCall.env);
			value.handlerOffset = qedCall.instPointer + 5 - ((QEDObj) qedCall.obj).func.offset;
			call.stack.push(value);
		}
	}
}
