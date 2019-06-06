/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang;

import com.hocuscodus.qed.lang.Declaration.FunctionDeclaration;

public class QEDObj extends Obj {
	public QEDObj parent;
	public ObjEnv parentEnv;
	public FunctionDeclaration func;
//	public EventHandlerList eventHandlers;
	public Object[] data;

	public QEDObj(QEDObj parent, ObjEnv parentEnv, FunctionDeclaration func) {
		this.parent = parent;
		this.parentEnv = parentEnv;
		this.func = func;
//		eventHandlers = func.eventHandlers;
		data = new Object[func.topDataNode.getSize()];
	}

	public static LValue getLValue(Object[] data, Path path) {
		int level = path.getNumLevels() - 1;

		for (int index = 0; index < level; index++)
			data = (Object[]) data[path.get(index)];

		return new LValue(data, path.get(level));
	}

	public static Object getValue(Object[] data, Path path) {
		LValue lValue = getLValue(data, path);

		return lValue.data[lValue.offset];
	}
}
