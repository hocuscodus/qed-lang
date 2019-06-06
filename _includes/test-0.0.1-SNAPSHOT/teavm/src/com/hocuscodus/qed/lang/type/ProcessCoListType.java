/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.Declaration.DecEnv;
import com.hocuscodus.qed.lang.Exp;
import com.hocuscodus.qed.lang.Exp.FuncExp;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.PredefCall;
import com.hocuscodus.qed.lang.PredefObj;
import com.hocuscodus.qed.lang.QEDProcess;
import com.hocuscodus.qed.lang.ReturnHandler;

public class ProcessCoListType extends PredefinedFuncFn {
	public ProcessCoListType() {
		super(Type.booleanType, "process", new Type[0]);
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		((FuncExp) exp).exp.write(env, path, out);
		super.writeExp(exp, env, path, out);
	}

	public Object eval(QEDProcess process, Call call, Object[] args) {
		PredefObj obj = (PredefObj) call.stack.pop();
		process.push(new PredefCall(call, this, args, (ReturnHandler) call.stack.pop()) {
			public void process(QEDProcess process) {
				((PredefObj) obj).fn.exec(process, this, args);
			}
		});
		process.get(process.size() - 1).stack.push(obj);
		return null;
	}

	@SuppressWarnings("unchecked")
	public Object exec(QEDProcess process, Call call, Object[] args) {
		PredefObj obj = (PredefObj) call.stack.pop();
		Object[] objs = (Object[]) obj.value;
		int[] index = (int[]) objs[1];
		ArrayList<PredefObj> list = (ArrayList<PredefObj>) objs[0];

		if (index[0] == 0 && list.get(0) == null && list.size() > 1) {
			list.set(0, (PredefObj) call.obj);
			Type.haltFn.eval(process, call);
	    	list.get(++index[0]).onReturn(process, true);
		}
		else {
			list.set(0, null);
			call.onReturn(process, true);
		}

		return null;
	}
}
