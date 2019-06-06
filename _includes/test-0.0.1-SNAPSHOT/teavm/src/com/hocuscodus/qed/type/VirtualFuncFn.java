/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import java.util.List;

import com.hocuscodus.qed.lang.Declaration.DecEnv;
import com.hocuscodus.qed.lang.Declaration.FunctionDeclaration;
import com.hocuscodus.qed.lang.Declaration.VTableEntry;
import com.hocuscodus.qed.lang.Exp;
import com.hocuscodus.qed.lang.Exp.VirtualFuncExp;
import com.hocuscodus.qed.node.CodeNode;

public class VirtualFuncFn extends UnaryFn {
	public VirtualFuncFn() {
		super("virtual", null);
	}
//
//	public Object eval(Env env, Exp exp) {
//		NodeInfo info = (NodeInfo) super.eval(env, exp);
//		VTableEntry vTableEntry = info.storageCode.vTable.get(((VirtualFuncExp) exp).fnIndex);
//		CodeNode node = (CodeNode) vTableEntry.topMtx.getNode(vTableEntry.fnIndex);
//		FunctionDeclaration func = (FunctionDeclaration) node.getDec();
//		CodeObject obj = info.storageCode.getStorageCode(vTableEntry.topMtx);
//
//		if (env.storageCode.topStorageUnit != null && env.storageCode.type.topMtx.getField(env.storageCode.topStorageUnit, env.path) != env.currentUnit)
//			env.currentUnit = env.currentUnit;
//
//		return new CodeObject(obj != null ? obj : null, func);
//	}

	public Type getReturnType(Exp exp, DecEnv env) {
		return getFunction(exp, env);
	}
//
//	public String getName(Exp exp) {
//		return getFunction(exp).getName();
//	}

	public FunctionDeclaration getFunction(Exp exp) {
		VirtualFuncExp vExp = (VirtualFuncExp) exp;
		FunctionDeclaration classObj = null;//(FunctionDeclaration) vExp.exp.getReturnType();
		VTableEntry vTableEntry = classObj.vTableTree.getVTable().get(vExp.fnIndex);
		CodeNode node = (CodeNode) vTableEntry.topMtx.getNode(vTableEntry.fnIndex);
		FunctionDeclaration func = (FunctionDeclaration) node.getDec();

		return func;//new StorageCode(info != null ? info.storageCode : null, env.currentNode, env.path, topMtx, this, args1);
	}

	public FunctionDeclaration getFunction(Exp exp, DecEnv env) {
		VirtualFuncExp vExp = (VirtualFuncExp) exp;
		FunctionDeclaration classObj = null;//(FunctionDeclaration) vExp.exp.getReturnType(env);
		VTableEntry vTableEntry = classObj.vTableTree.getVTable().get(vExp.fnIndex);
		CodeNode node = (CodeNode) vTableEntry.topMtx.getNode(vTableEntry.fnIndex);
		FunctionDeclaration func = (FunctionDeclaration) node.getDec();

		return func;//new StorageCode(info != null ? info.storageCode : null, env.currentNode, env.path, topMtx, this, args1);
	}

	@Override
	public String toString(Exp exp) {
		List<Exp> args = ((VirtualFuncExp) exp).args;
		StringBuffer array = new StringBuffer();//getName(exp));

		if (args != null) {
			array.append('(');

			for (int index = 0; index < args.size(); index++)
				array.append(index != 0 ? ", " : "").append(args.get(index));

			array.append(')');
		}

		return array.toString();
	}
}
