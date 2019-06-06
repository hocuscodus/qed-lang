/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import com.hocuscodus.qed.lang.Env;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.QEDProcess;
import com.hocuscodus.qed.node.LayoutNode.LayoutContextSet;

public class ImageType extends PredefinedFuncFn {
	public ImageType() {
		super(new VoidType(), "image", new Type[] {Type.stringType});
	}
//
//	@Override
//	public Object exec(QEDProcess process, Call call, Object[] args) {
//		return null;
//	}
//
//	@Override
//	public boolean exec(Env env, List<Exp> args, TypeInstance typeInstance) {
//		final PredefinedTypeInstance predefinedTypeInstance = (PredefinedTypeInstance) typeInstance;
//		final String filename = (String) args.get(0).eval(env);
//
//		predefinedTypeInstance.value = env.ui.loadImage(filename);
//		return true;
//	}

	public int getWidth(QEDProcess process, Object value, int fontSize) {
		return process.getImageWidth(value, fontSize);
	}

	public int getHeight(QEDProcess process, Object value, int fontSize) {
		return process.getImageHeight(value, fontSize);
	}

	public void render(Env env, LayoutContextSet layoutContextSet, Path path, Object ctx, int[] pos, int[] size, int[] unitpos, Object value) {
		((QEDProcess) ctx).drawImage(value, pos, unitpos);
	}
}
