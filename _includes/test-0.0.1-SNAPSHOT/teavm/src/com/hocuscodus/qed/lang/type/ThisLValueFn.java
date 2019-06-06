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
import com.hocuscodus.qed.lang.Exp.LValueExp;
import com.hocuscodus.qed.lang.ObjEnv;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.QEDObj;
import com.hocuscodus.qed.lang.QEDProcess;
import com.hocuscodus.qed.node.CodeNode;

public class ThisLValueFn extends UnaryFn {
	public ThisLValueFn() {
		super("_thispath_", new Type[] {Type.intType});
	}

	public Type getReturnType(Exp exp, DecEnv env) {
   		Path path = ((LValueExp) exp).path;

   		return ((CodeNode) ((InternalDeclaration) super.getReturnType(exp, env)).topMtx.getNode(path)).getDec();
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		super.writeExp(exp, env, path, out);

		Path relPath = (Path) ((LValueExp) exp).path;
		Path dataPath = ((InternalDeclaration) super.getReturnType(exp, env)).topDataNode.getDataIndex(relPath);
		int numLevels = dataPath.getNumLevels();

		write(out, numLevels);

		for (int index = 0; index < numLevels; index++)
			write(out, dataPath.get(index));
	}

	public Object eval(QEDProcess process, Call call, Object value) {
		int numLevels = call.read(process);
		Object[] info = (Object[]) value;
		QEDObj obj = (QEDObj) info[0];
		ObjEnv env = (ObjEnv) info[1];
		Object[] data = env.objs[env.objs.length - 1];

		for (int index = 0; index < numLevels - 1; index++) {
			int ndx = call.read(process);

			if (ndx >= 0)
				data = (Object[]) data[ndx];
			else {
				int dindex = call.getQEDCall().ampStack.peek().index;

				data = (Object[]) data[dindex];
				env.push(data);
			}
		}

		int index = call.read(process);

		return new Object[] {obj, env};//(ObjEnv) data[index];
	}

	public Type bracketType() {
		return null;
	}

	public Object bracket(Object array, int index) {
		return null;
	}
}
