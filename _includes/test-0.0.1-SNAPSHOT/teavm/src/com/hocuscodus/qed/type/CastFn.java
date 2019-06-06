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
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.QEDProcess;

public class CastFn extends BinaryFn {
	public CastFn() {
		super("(cast)", new Type[] {Type.intType, Type.intType});
	}

	public Type getReturnType(Exp exp, DecEnv env) {
		return getExp1(exp).type;
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		Exp srcExp = getExp2(exp);
		Type srcType = srcExp.getReturnType(env);
		Type destType = getExp1(exp).type;
		int srcOpCode = srcType.opCode;
		int destOpCode = destType.opCode;

		srcExp.write(env, path, out);

		if (srcOpCode != destOpCode && destType.canCast(srcType)) {
			write(out, opCode);
			write(out, srcOpCode);
			write(out, destOpCode);
		}
	}

	public void eval(QEDProcess process, Call call) {
		Object value1 = call.stack.pop();
		int srcOpCode = call.read(process);
		int destOpCode = call.read(process);
		Type srcType = fnList.get(srcOpCode);
		Type destType = fnList.get(destOpCode);
		Object value = destType.cast(srcType, value1);

		if (value != null)
			call.stack.push(value);
	}
}
