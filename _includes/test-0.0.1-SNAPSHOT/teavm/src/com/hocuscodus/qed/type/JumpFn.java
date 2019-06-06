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
import com.hocuscodus.qed.lang.Exp.ValueExp;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.QEDCall;
import com.hocuscodus.qed.lang.QEDProcess;

public class JumpFn extends UnaryFn {
	public JumpFn() {
		super(null, new Type[] {Type.intType});
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		int pointer = (Integer) ((ValueExp) getExp(exp)).value;

		write(out, opCode);
		writeI(out, pointer);
	}

	public void eval(QEDProcess process, Call call) {
		QEDCall qedCall = call.getQEDCall();
		int currentPointer = qedCall.instPointer;
		int pointer = qedCall.readI(process);

		// This is, again, to satisfy TeaVM peculiarities.... bummer...
		qedCall.instPointer = currentPointer + pointer + 4;
	}
}
