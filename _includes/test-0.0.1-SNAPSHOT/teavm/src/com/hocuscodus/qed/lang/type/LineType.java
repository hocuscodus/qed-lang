/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

public class LineType extends FuncFn {
	static Type intArrayType = null;//new Exp.ArrayType(new IntType());

	public LineType() {
		super(new BooleanType(), "line", new Type[] {intArrayType, intArrayType});
	}
//
//	@Override
//	public boolean exec(Env env, List<Exp> args, TypeInstance typeInstance) {
//		UIContext uiContext = getUIContext();
//
//		if (uiContext != null) {
//			Object[] arg0 = ((ArrayTypeInstance) args.get(0).eval(env)).values;
//			Object[] arg1 = ((ArrayTypeInstance) args.get(1).eval(env)).values;
//			int[] args0 = new int[] {(Integer) arg0[0], (Integer) arg0[1]};
//			int[] args1 = new int[] {(Integer) arg1[0], (Integer) arg1[1]};
//
//			if (args.size() > 2) {
//				int stroke = (Integer) args.get(2).eval(env);
//
//				if (uiContext.ctx instanceof Form)
//					((Form) uiContext.ctx).line(LayoutContextSet.add(uiContext.pos, args0), LayoutContextSet.add(uiContext.pos, args1), stroke);
//				else
//					((UI) uiContext.ctx).line(LayoutContextSet.add(uiContext.pos, args0), LayoutContextSet.add(uiContext.pos, args1), stroke);
//			}
//			else
//				if (uiContext.ctx instanceof Form)
//					((Form) uiContext.ctx).line(LayoutContextSet.add(uiContext.pos, args0), LayoutContextSet.add(uiContext.pos, args1));
//				else
//					((UI) uiContext.ctx).line(LayoutContextSet.add(uiContext.pos, args0), LayoutContextSet.add(uiContext.pos, args1));
//		}
//
//		return typeInstance.callReturn("");
//	}
}
