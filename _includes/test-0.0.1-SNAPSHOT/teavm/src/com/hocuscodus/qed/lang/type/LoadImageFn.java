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
import com.hocuscodus.qed.lang.Obj;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.QEDProcess;

public class LoadImageFn extends PredefinedFuncFn {
	public LoadImageFn() {
		super(Type.voidType, "loadImage", new Type[] {Type.stringType});
	}

	@Override
	public Object exec(QEDProcess process, Call call, Object[] args) {
		Object image = process.loadImage(((String) args[0]), process, (Obj) call.obj);

		return image != null ? image : super.exec(process, call, args);
	}

	public void writeUninitExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
//		ArrayList<Exp> args = new ArrayList<Exp>();
//
//		args.add(new ValueExp(Type.intType, -1));
//		new FuncExp(Type.resetTimerType, exp, args).write(env, path, out);
////		new UnaryExp(Type.cleanObjectFn, new ValueExp(Type.intType, id)).write(env, path, out);
	}
}
