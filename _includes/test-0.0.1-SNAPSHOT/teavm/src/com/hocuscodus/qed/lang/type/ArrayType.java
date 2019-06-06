/*
 *    Copyright (C) 2016 Hocus Codus Software inc.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.hocuscodus.qed.lang.ArrayCall;
import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.Declaration.DecEnv;
import com.hocuscodus.qed.lang.Env;
import com.hocuscodus.qed.lang.Exp;
import com.hocuscodus.qed.lang.Exp.ArrayExp;
import com.hocuscodus.qed.lang.Exp.FuncExp;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.QEDProcess;
import com.hocuscodus.qed.lang.ReturnHandler;
import com.hocuscodus.qed.node.LayoutNode.LayoutContextSet;


public class ArrayType extends Type {
	public Type subType;

	public ArrayType() {
		super(null, "___");
	}

	public ArrayType(Type subType) {
		super(subType, "___");
		this.subType = subType;
		opCode = arrayType.opCode;
	}

	public Type getReturnType(Exp exp, DecEnv env) {
		ArrayExp arrayExp = (ArrayExp) exp;
		int numItems = arrayExp.getNumExps();

		return numItems != 0 ? arrayExp.getExp(numItems - 1).getReturnType(env) : voidType;
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		ArrayExp arrayExp = (ArrayExp) exp;
		int numItems = arrayExp.getNumExps();

		super.writeExp(exp, env, path, out);
		writeI(out, numItems);

		for (int dindex = 0; dindex < numItems; dindex++) {
			arrayExp.getExp(dindex).write(env, path, out);
			new Exp(Type.initArrayValueFn).write(env, path, out);
		}
	}

	public void eval(QEDProcess process, Call call) {
		if (call.stack.empty())
			call = call;
		process.push(new ArrayCall(call, call, call.readI(process), (ReturnHandler) call.stack.pop()));
	}

	public Type getType() {
		return subType;
	}

	public boolean isNumeric() {
		return subType.isNumeric();
	}

	public boolean isIntegral() {
		return true;//subType.isIntegral();
	}

	public boolean getcodeflag() {
		return (true);
	}

	public long getInteger(Object value) {
		return ((Object[]) value).length;
	}

	public Object setInteger(long num) {
		return subType.setInteger(num);
	}

	public String getName() {
		return "array";
	}

	public int getNumDim() {
		return 1 + getdowntype(1).getNumDim();
	}

	public Type getdowntype(int levels) {
		return levels > 0 ? subType.getdowntype(levels - 1) : this;
	}

	public void render(Env env, LayoutContextSet layoutContextSet, Path path, Object ctx, int[] pos, int[] size, int[] unitpos, Object value) {
		subType.render(env, layoutContextSet, path, ctx, pos, size, unitpos, value);
	}

	public Type bracketType() {
		return subType;
	}
//
//	public Object bracket(Object array, int index) {
//		return Declaration.byteCodeFlag ? ((Object[]) array)[index] : ((ArrayTypeInstance) array).values[index];
////		return new ExpValue(voidType, (StorageUnitCode) ((StorageUnit) array).storagesetarray.storagesets[(int) index.getInteger()]);//.getvalue();
//		//		return ((StorageUnitCode) ((Storagesetarray) array).storagesets[(int) index.getInteger()]).getvalue();
//	}
//
//	public Object assignBracket(Object array, int index, Object value) {
//		((ArrayTypeInstance) array).values[index] = value;
//
//		return ((ArrayTypeInstance) array).values[index];
//	}

	public Exp findVarPath(Exp baseExp, String field, List<Exp> args, boolean virtualFlag) {
		Exp exp = null;

		if (field.equals("size"))
			exp = new FuncExp(arraySizeType, baseExp, args);

		if (field.equals("set"))
			exp = new FuncExp(arraySetType, baseExp, args);

		if (field.equals("add"))
			exp = new FuncExp(arrayAddType, baseExp, args);

		if (field.equals("remove"))
			exp = new FuncExp(arrayRemoveType, baseExp, args);

		if (field.equals("clear"))
			exp = new FuncExp(arrayClearType, baseExp, args);

		return exp;
	}

	@Override
	public String toString(Exp exp) {
		StringBuffer array = new StringBuffer();

		if (exp instanceof ArrayExp) {
			ArrayExp arrayExp = (ArrayExp) exp;

			array.append("[");
			for (int index = 0; index < arrayExp.getNumExps(); index++)
				array.append(index != 0 ? ", " : "").append(arrayExp.getExp(index));
			array.append(']');
		}
		else
			array.append("array");

		return array.toString();
	}
}
