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

public class LValueFn extends UnaryFn {
	public LValueFn() {
		super("_path_", new Type[] {Type.intType});
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
		if (value instanceof QEDObj)
			value = value;
		Object[] info = (Object[]) value;
		ObjEnv env = (ObjEnv) info[1];
		Object[] data = env.objs[env.objs.length - 1];

		for (int index = 0; index < numLevels - 1; index++) {
			int ndx = call.read(process);

			data = (Object[]) data[ndx >= 0 ? ndx : call.getQEDCall().ampStack.peek().index];
		}

		return(new Object[] {data, (Integer) call.read(process)});
	}

	public Type bracketType() {
		return null;
	}

	public Object bracket(Object array, int index) {
		return null;
	}
}
