/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.Declaration;
import com.hocuscodus.qed.lang.Declaration.DecEnv;
import com.hocuscodus.qed.lang.Exp;
import com.hocuscodus.qed.lang.QEDProcess;
import com.hocuscodus.qed.node.CodeNode;

public class FieldFn extends UnaryFn {
	public FieldFn() {
		super("F", new Type[] {});
	}

	public Type getReturnType(Exp exp, DecEnv env) {
		return ((Declaration) super.getReturnType(exp, env)).returnType;
	}

	public Object eval(QEDProcess process, Call call, Object value) {
		Object[] values = (Object[]) value;

		return ((Object[]) values[0])[(Integer) values[1]];
	}
//
//	public Object assign(Env env, Exp exp, Object value) {
//		NodeInfo info = exp != null ? (NodeInfo) super.eval(env, exp) : null;
//		boolean rc = info.storageUnitCode != null;
//
//		if (rc)
//			info.storageUnitCode.value = value;
//
//		return rc ? info.storageUnitCode.value : null;
//	}

	public Object assign(QEDProcess process, Call call, Object value) {
		Object[] lValue = (Object[]) call.stack.pop();
		Object[] data = (Object[]) lValue[0];
		int index = (Integer) lValue[1];

		data[index] = value; 
		return (value);
	}

	public boolean getVirtualFlag(Exp exp, String name) {
		Type type = null;//exp.getReturnType();

		return !type.getName().equals(name);
	}

	public String toString(Exp exp) {
//		Type returnType = super.getReturnType(exp, null);
        CodeNode node = null;//returnType instanceof CodeNode ? (CodeNode) returnType : null;

        return node != null ? node.getDec().name : "";
	}
}
