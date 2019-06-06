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
import com.hocuscodus.qed.lang.Declaration.InternalDeclaration;
import com.hocuscodus.qed.lang.Exp;
import com.hocuscodus.qed.lang.Exp.ValueExp;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.QEDProcess;

public class PathType extends ValueType {
	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		Path relPath = (Path) ((ValueExp) exp).value;
		Path dataPath = relPath != null ? ((InternalDeclaration) env.declarations[env.declarations.length - 1]).topDataNode.getDataIndex(relPath) : new Path();

		super.writeExp(exp, env, path, out);
		write(out, dataPath.getNumLevels());

		for (int index = 0; index < dataPath.getNumLevels(); index++)
			write(out, dataPath.get(index));
	}

	public void eval(QEDProcess process, Call call) {
		int numLevels = call.read(process);
		Path path = Path.zero(numLevels);

		for (int index = 0; index < numLevels; index++) {
			int ndx = call.read(process);

			path.set(index, ndx);// >= 0 ? ndx : 0);//call.getQEDCall().ampStack.peek().index);
		}

		call.stack.push(path);
	}

	public Type bracketType() {
		return null;
	}

	public Object bracket(Object array, int index) {
		return null;
	}
}
