/*
 *    Copyright (C) 2016 Hocus Codus Software inc.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.node;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.hocuscodus.qed.lang.Context;
import com.hocuscodus.qed.lang.Declaration;
import com.hocuscodus.qed.lang.Declaration.DecEnv;
import com.hocuscodus.qed.lang.Declaration.FieldDeclaration;
import com.hocuscodus.qed.lang.Declaration.FunctionDeclaration;
import com.hocuscodus.qed.lang.Declaration.InternalDeclaration;
import com.hocuscodus.qed.lang.Exp;
import com.hocuscodus.qed.lang.Exp.BinaryExp;
import com.hocuscodus.qed.lang.Exp.LValueExp;
import com.hocuscodus.qed.lang.Exp.RefExp;
import com.hocuscodus.qed.lang.Exp.UnaryExp;
import com.hocuscodus.qed.lang.Exp.ValueExp;
import com.hocuscodus.qed.lang.Instruction;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.type.Type;


public class CodeNode extends Node {
	public FieldDeclaration codeDec;
	public static int idAllocator = 1;
	public int id;

	public CodeNode(FieldDeclaration codeDec) {
		id = idAllocator++;
		this.codeDec = codeDec;
	}
//
//	public Type getReturnType(Exp exp, DecEnv env) {
//		return this;
//	}
//
//    @Override
//	public StorageUnit createStorageUnit(Env env) {
//    	return new StorageUnitCode();
//    }
//
//	public boolean init(Env env, CodeObject storageCode, Object[] args) {
//		storageCode.topStorageUnit = (StorageUnitCode) eval(new Env(env, storageCode, args), null);
//    	boolean rc = storageCode.topStorageUnit != null && parsesubinitunits(new Env(env, storageCode, args));
//
//    	if (rc)
//    		storageCode.initVTables(storageCode.type.vTableTree);
//
//    	return rc;
//    }
//
//	public boolean unInit(Env env, CodeObject storageCode) {
//    	return storageCode.topStorageUnit != null && parsesubuninitunits(new Env(env, storageCode, (Object[]) null));
//    }
//
//    @Override
//	public boolean parsesubinitunits(Env env) {
//    	if (unit != null)
//    		((StorageUnitCode) env.currentUnit).value = ((Declaration) unit).eval(env, null);
//
//        return super.parsesubinitunits(env);
//    }
//
//    @Override
//	public boolean parsesubuninitunits(Env env) {
//		Declaration declaration = getDec();
//    	boolean rc = super.parsesubuninitunits(env);
//
//    	if (rc && declaration != null)
//    		declaration.uninit(((StorageUnitCode) env.currentUnit).value);
//
//        return rc;
//    }

	public Node getNode(Path path) {
		int index = path.getNumLevels() > 0 ? path.get(0) : 0;
		int ndx = index >= 0 ? getcontentndx(index) : 0;

		return index < 0
					? ((CodeNode) arrayPathUnit).getNode(path.trim(0, 1))
					: ndx >= 0 && ndx < childNodes.size()
						? ((CodeNode) childNodes.get(ndx)).getNode(path.trim(0, 1)) : this;
	}

	public void write(FunctionDeclaration functionDeclaration, DataNode topDataNode, OutputStream out) throws IOException {
		if (functionDeclaration.numParams < getNumNodes()) {
			out.write(Type.pushLocalStackFn.opCode);

			for (int index = functionDeclaration.numParams; index < getNumNodes(); index++) {
				CodeNode node = (CodeNode) getNode(index);

				node.write(functionDeclaration.buildDecEnv(new Path()), new Path(node.index), topDataNode, new Path(node.index), out);
//				((CodeNode) getNode(index)).write(functionDeclaration, new Path(index), topDataNode, new Path(index), out);
			}
//
//			for (int index = functionDeclaration.numParams; index < getNumNodes(); index++) {
//				CodeNode node = (CodeNode) getNode(index);
//
//				node.writeUninitCode(functionDeclaration.buildDecEnv(new Path()), new Path(node.index), topDataNode, new Path(node.index), out);
//			}

			out.write(Type.popLocalStackFn.opCode);
		}
	}

	public void write(DecEnv env, Path fullPath, DataNode dataNode, Path path, OutputStream out) throws IOException {
		Declaration declaration = (Declaration) unit;

		if (declaration != null)
			declaration.write(env, fullPath, dataNode, path, out);

		if (getNumNodes() != 0) {
			out.write(Type.pushLocalStackFn.opCode);

			for (int index = 0; index < getNumNodes(); index++) {
				CodeNode subNode = (CodeNode) getNode(index);

				subNode.write(env, fullPath.concat(subNode.index), dataNode, path.concat(subNode.index), out);
			}

			out.write(Type.popLocalStackFn.opCode);
		}

		if (getArrayPathUnit() != null && codeDec != null && codeDec.returnType != null) {
			if (codeDec.returnType.isIntegral()) {
				Exp exp = ((UnaryExp) ((BinaryExp) codeDec.initExpr).value1).exp;
				boolean whileFlag = codeDec == unit && codeDec.whileFlag;
				// get array data byte count
				DataNode subDataNode = dataNode != null ? dataNode.getDataNode(path).getArrayDataNode() : null;
				int size = subDataNode != null ? subDataNode.getSize() : 0;
				Path loopIndex = size != 0 ? path.concat(-1) : null;

				// push &0, numElements;
				new BinaryExp(Type.pushFn, exp, new ValueExp(Type.pathType, loopIndex)).write(env, fullPath, out);

				// allocate Object[abs(numElements)][] if size != 0
				if (size != 0)
					new Exp(Type.allocateStackFn).write(env, fullPath, out);

				int pointer = ((ByteArrayOutputStream) out).size();
				ByteArrayOutputStream loopContent = new ByteArrayOutputStream();

				// allocate Object[byte] at [&0], save data pointer and set it to [&0]
				if (size != 0)
					new UnaryExp(Type.saveStackFn, new UnaryExp(Type.allocateFn, new ValueExp(Type.intType, size))).write(env, fullPath, loopContent);

				((CodeNode) getArrayPathUnit()).write(new DecEnv(env, (InternalDeclaration) getArrayPathUnit().unit), fullPath.concat(-1), subDataNode, new Path(), loopContent);
				// inc &0
				new Exp(Type.incStackFn).write(env, fullPath, loopContent);

				byte[] loop = loopContent.toByteArray();

				if (whileFlag)
					// while cond exec else jump to afterCurrent + length
					new BinaryExp(Type.whileStackFn, ((BinaryExp) codeDec.initExpr).value2, new ValueExp(Type.intType, loop.length + 5)).write(env, fullPath, out);
				else
					// if &0 >= abs(numElements) jump to afterCurrent + length
					new UnaryExp(Type.testStackFn, new ValueExp(Type.intType, loop.length + 5)).write(env, fullPath, out);

				// write loop
				out.write(loop);
				// jump to start
				int jump = pointer - ((ByteArrayOutputStream) out).size() - 5;
				new UnaryExp(Type.jumpFn, new ValueExp(Type.intType, jump)).write(env, fullPath, out);
				// pop &0;
				new Exp(Type.popFn).write(env, fullPath, out);
			}
		}

		if (getArrayPathUnit() != null && unit != null && ((FieldDeclaration) unit).returnType == Type.booleanType) {
			// get array data byte count
			DataNode subDataNode = dataNode.getDataNode(path).getArrayDataNode();
			int size = subDataNode != null ? subDataNode.getSize() : 0;
			//				Exp dataIndexExp = new UnaryExp(Type.fieldFn, new ValueExp(Type.pathType, topDataNode.getDataIndex(path)));
			//				Exp loopIndexExp = size != 0 ? new UnaryExp(Type.fieldFn, new ValueExp(Type.pathType, topDataNode.getLoopIndex(path))) : null;
			Exp dataIndexExp = ((FunctionDeclaration) env.declarations[0]).getVarPath(stripPath(fullPath));
			ByteArrayOutputStream bodyContent = new ByteArrayOutputStream();

			// allocate Object[byte] at [&loopindex]
			if (size != 0) {
				LValueExp dataLValueExp = (LValueExp) ((UnaryExp) dataIndexExp).exp;
				Exp loopIndexExp = new UnaryExp(Type.fieldFn, new LValueExp(Type.lValueFn, dataLValueExp.exp, dataLValueExp.path.concat(-1)));

				new BinaryExp(Type.assignmentFn, loopIndexExp, new UnaryExp(Type.allocateFn, new ValueExp(Type.intType, size))).write(env, fullPath, bodyContent);
			}

			// if body
			((CodeNode) getArrayPathUnit()).write(env, fullPath.concat(-1), subDataNode, new Path(), bodyContent);

			byte[] body = bodyContent.toByteArray();

			// if cond exec body else jump to afterCurrent + length
			new BinaryExp(Type.ifStackFn, dataIndexExp, new ValueExp(Type.intType, body.length)).write(env, fullPath, out);
			// write body
			out.write(body);
		}
	}

	public void writeUninitCode(FunctionDeclaration functionDeclaration, DataNode dataNode, OutputStream out) throws IOException {
		if (functionDeclaration.numParams < getNumNodes()) {
			out.write(Type.pushLocalStackFn.opCode);

			for (int index = functionDeclaration.numParams; index < getNumNodes(); index++) {
				CodeNode node = (CodeNode) getNode(index);

				node.writeUninitCode(functionDeclaration.buildDecEnv(new Path()), new Path(node.index), dataNode, new Path(node.index), out);
			}

			out.write(Type.popLocalStackFn.opCode);
		}
	}

	public void writeUninitCode(DecEnv env, Path fullPath, DataNode topDataNode, Path path, OutputStream out) throws IOException {
//		if (getArrayPathUnit() != null && unit != null && ((FieldDeclaration) unit).returnType == Type.booleanType) {
//			// get array data byte count
//			DataNode subDataNode = topDataNode.getDataNode(path).getArrayDataNode();
//			Path dataIndex = topDataNode.getDataIndex(path);
//			ByteArrayOutputStream bodyContent = new ByteArrayOutputStream();
//
//			// if body
//			((CodeNode) getArrayPathUnit()).writeUninitCode(env, fullPath.concat(-1), subDataNode, new Path(), bodyContent);
//
//			byte[] body = bodyContent.toByteArray();
//
//			// if cond exec body else jump to afterCurrent + length
//			new BinaryExp(Type.ifStackFn, new ValueExp(Type.pathType, dataIndex), new ValueExp(Type.intType, body.length)).write(env, fullPath, out);
//			// write body
//			out.write(body);
//		}
//
		if (getArrayPathUnit() != null && codeDec != null && codeDec.returnType != null) {
			if (codeDec.returnType.isIntegral()) {
				Exp exp = ((UnaryExp) ((BinaryExp) codeDec.initExpr).value1).exp;
				// get array data byte count
				DataNode subDataNode = topDataNode != null ? topDataNode.getDataNode(path).getArrayDataNode() : null;
				int size = subDataNode != null ? subDataNode.getSize() : 0;
				Path loopIndex = size != 0 ? path.concat(-1) : new Path();

				// push &0, numElements;
				new BinaryExp(Type.pushFn, exp, new ValueExp(Type.pathType, loopIndex)).write(env, fullPath, out);

				int pointer = ((ByteArrayOutputStream) out).size();
				ByteArrayOutputStream loopContent = new ByteArrayOutputStream();

				((CodeNode) getArrayPathUnit()).writeUninitCode(new DecEnv(env, (InternalDeclaration) getArrayPathUnit().unit), fullPath.concat(-1), subDataNode, new Path(), loopContent);
				// inc &0
				new Exp(Type.incStackFn).write(env, fullPath, loopContent);

				byte[] loop = loopContent.toByteArray();

				// if &0 >= abs(numElements) jump to afterCurrent + length
				new UnaryExp(Type.testStackFn, new ValueExp(Type.intType, loop.length + 5)).write(env, fullPath, out);

				// write loop
				out.write(loop);
				// jump to start
				int jump = pointer - ((ByteArrayOutputStream) out).size() - 5;
				new UnaryExp(Type.jumpFn, new ValueExp(Type.intType, jump)).write(env, fullPath, out);
				// pop &0;
				new Exp(Type.popFn).write(env, fullPath, out);
			}
		}

		if (getNumNodes() != 0) {
			out.write(Type.pushLocalStackFn.opCode);

			for (int index = 0; index < getNumNodes(); index++) {
				CodeNode subNode = (CodeNode) getNode(index);

				subNode.writeUninitCode(env, fullPath.concat(subNode.index), topDataNode, path.concat(subNode.index), out);
			}

			out.write(Type.popLocalStackFn.opCode);
		}

		Declaration declaration = (Declaration) unit;

		if (declaration != null) {
			Exp exp = new UnaryExp(Type.fieldFn, new LValueExp(Type.lValueFn, new RefExp(0), fullPath));

			declaration.returnType.writeUninitExp(exp, env, path, out);
		}
	}

	public FieldDeclaration getPathDec(Path path, int level) {
		int ndx = level < path.getNumLevels() ? getcontentndx(path.get(level)) : -1;
		CodeNode submtx = ndx != -1 ? (CodeNode) getNode(ndx) : null;
		FieldDeclaration dec = submtx != null ? submtx.getPathDec(path, level + 1) : null;

		if (dec == null && getArrayFlag())
			dec = ((CodeNode) getArrayPathUnit()).getPathDec(path, level);

		if (dec == null && level == path.getNumLevels())
			dec = unit != null && unit instanceof FieldDeclaration ? (FieldDeclaration) getDec() : null;

		return (dec);
	}
//
//	public Object eval(Env env, Exp exp) {
//		Declaration declaration = getDec();
//
//    	if (declaration != null)
//    		env.currentNode.value = declaration.eval(env, exp);
//
//        return super.eval(env, exp);
//	}
//
//	public int getLevel(Env env, Exp exp, Path[] path) {
//		while (env != null && false)//env.storageCode.type.topMtx != this)
//			env = env.parent;
//
//		path[0] = env.path;
//		return 0;
//	}
//
//	public Exp getVarPath(Exp baseExp, String field, List<Exp> args, Path path, int level) {
//		Exp exp = super.getVarPath(baseExp, field, args, path, level);
//
//		if (exp == null && level < path.getNumLevels() && getDec() != null)
//			exp = getDec().getVarPath(baseExp, field, args);
//
//		return (exp);
//	}

	public Path getArrayPrefix(int prefix) {
		return getDec().returnType.isIntegral() ? new Path(prefix) : new Path();
	}

	public Exp findVarPath(Exp baseExp, Path basePath, String field, List<Exp> args) {
		Exp exp = null;

		if (getArrayFlag())
			exp = ((CodeNode) getArrayPathUnit()).findVarPath(baseExp, basePath.concat(-1), field, args);

		for (int index = getNumNodes() - 1; exp == null && index >= 0; index--) {
			CodeNode codeNode = (CodeNode) getNode(index);

			exp = codeNode.findVarPath(baseExp, basePath.concat(codeNode.index), field, args);
		}

		if (exp == null && getDec() != null)
			exp = getDec().getVarPath(baseExp, basePath, field, args);

		return exp;
	}

	public Node setArrayPathUnit(FunctionDeclaration function, Instruction inst, Path path, int level) {
		boolean wasNull = arrayPathUnit == null;
		Node arrayPathUnit = super.setArrayPathUnit(function, inst, path, level);

		if (wasNull)
			arrayPathUnit.unit = new InternalDeclaration((CodeNode) arrayPathUnit, function.topDataNode.getDataNode(path.trim(level + 1)));

		return (arrayPathUnit);
	}

	public DecEnv buildDecEnv(DecEnv env, Path path, int level) {
		if (level < path.getNumLevels())
			if (getArrayFlag()) {
				DecEnv subEnv = getcodeflag() ? new DecEnv(env, (InternalDeclaration) getArrayPathUnit().unit) : env;

				return ((CodeNode) getArrayPathUnit()).buildDecEnv(subEnv, path, level);
			}
			else {
				int ndx = getcontentndx(path.get(level));
				CodeNode subNode = ndx != -1 ? (CodeNode) getNode(ndx) : null;

				return subNode != null ? subNode.buildDecEnv(env, path, level + 1) : env;
			}
		else
			return env;
	}

	public Exp getVarPath(int index, int count, String field, List<Exp> args, Path path, int level) {
		int ndx = level < path.getNumLevels() ? getcontentndx(path.get(level)) : -1;
		CodeNode subNode = ndx != -1 ? (CodeNode) getNode(ndx) : null;
		Exp exp = subNode != null ? subNode.getVarPath(index, count, field, args, path, level + 1) : null;

		if (exp == null && getArrayFlag() && getcodeflag()) {
			exp = ((CodeNode) getArrayPathUnit()).getVarPath(index, count + 1, field, args, path, level);

			if (exp == null)
				exp = ((CodeNode) getArrayPathUnit()).findVarPath(new RefExp((index << 8) | (count + 1)), new Path(), field, args);

			if (exp != null)
				path.set(path.trim(level).concat(-1).concat(path.trim(0, level)));
		}

		return exp;
	}

	public Exp getVarPath(int count, Path path, int level) {
		int ndx = level < path.getNumLevels() ? getcontentndx(path.get(level)) : -1;
		CodeNode subNode = ndx != -1 ? (CodeNode) getNode(ndx) : null;
		Exp exp = subNode != null ? subNode.getVarPath(count, path, level + 1) : null;

		if (exp == null && getArrayFlag() && getcodeflag()) {
			exp = ((CodeNode) getArrayPathUnit()).findVarPath(new RefExp(count + 1), path, level, new Path());

			if (exp == null)
				exp = ((CodeNode) getArrayPathUnit()).getVarPath(count + 1, path, level);
		}

		return exp;
	}

	public Exp findVarPath(Exp baseExp, Path path, int level, Path basePath) {
		Exp exp = level == path.getNumLevels() ? new UnaryExp(Type.fieldFn, new LValueExp(Type.lValueFn, baseExp, basePath)) : null;

		if (exp == null) {
			int ndx = getcontentndx(path.get(level));
			CodeNode subNode = ndx != -1 ? (CodeNode) getNode(ndx) : null;

			if (subNode != null)
				exp = subNode.findVarPath(baseExp, path, level + 1, basePath.concat(subNode.index));
		}

		if (exp == null && getArrayFlag() && !getcodeflag())
			exp = ((CodeNode) getArrayPathUnit()).findVarPath(baseExp, path, level, basePath.concat(-1));

		return exp;
	}
//
//	public Type bracketType() {
//		return arrayPathUnit;
//	}
//
//	public Object bracket(Object array, int index) {
//		NodeInfo info = (NodeInfo) array;
//
//		info.storageUnitCode = (StorageUnitCode) info.storageUnitCode.arrayStorageUnits.storageUnits[index];
//		return info;
////		return ((StorageUnit) array).arrayStorageUnits.storageUnits[index];//.getvalue();
//	}
//
//	public Object assignBracket(Object array, int index, Object value) {
//		NodeInfo info = (NodeInfo) array;
//
//		info.storageUnitCode.arrayStorageUnits.storageUnits[index] = (StorageUnitCode) value;
//		info.storageUnitCode = (StorageUnitCode) info.storageUnitCode.arrayStorageUnits.storageUnits[index];
//		return info;
//	}

	public CodeNode searchVarNode(String var, Path path, int level) {
		int ndx = level < path.getNumLevels() ? getcontentndx(path.get(level)) : -1;
		CodeNode submtx = ndx != -1 ? (CodeNode) getNode(ndx) : null;
		CodeNode codeNode = submtx != null ? submtx.searchVarNode(var, path, level + 1) : null;

		if (codeNode == null)
			codeNode = findVarNode(var, level < path.getNumLevels() ? submtx != null ? submtx : this : null);

		if (codeNode == null && getArrayPathUnit() != null) {
			codeNode = ((CodeNode) getArrayPathUnit()).searchVarNode(var, path, level);

			if (codeNode != null && level < path.getNumLevels() && path.get(level) >= 0)
				path.set(path.trim(level).concat(-1).concat(path.trim(0, level)));
		}

		if (codeNode == null)
			codeNode = getDec() != null && getDec().name != null && getDec().name.equals(var) ? this : null;

		return (codeNode);
	}

	public CodeNode findVarNode(String field, Node exccx) {
		CodeNode codeNode = getDec() != null ? getDec().searchVarNode(field, this) : null;
//		CodeNode codeNode = getDec() != null && getDec().getName().equals(field) ? this : null;

		if (codeNode == null)
			codeNode = getArrayPathUnit() != null ? ((CodeNode) getArrayPathUnit()).findVarNode(field, null) : null;

		for (int index = 0; codeNode == null && index < getNumNodes(); index++) {
			CodeNode subcx = (CodeNode) getNode(index);

			codeNode = subcx != exccx ? subcx.findVarNode(field, null) : null;
		}

		return codeNode;
	}

	public CodeNode findVarNode2(String field, Node exccx) {
		CodeNode codeNode = null;

		if (this != exccx) {
			if (getDec() != null)
				codeNode = getDec().searchVarNode(field, this);

			if (codeNode == null)
				codeNode = getArrayPathUnit() != null ? ((CodeNode) getArrayPathUnit()).findVarNode(field, null) : null;

			for (int index = 0; codeNode == null && index < getNumNodes(); index++)
				codeNode = ((CodeNode) getNode(index)).findVarNode(field, null);
		}

		return codeNode;
	}

	public int getUpperFieldPath(String field, int level, Path path) {
		Path strippedPath = stripPath(path);
		int numLevels = field != null ? getUpperFieldPath(field, level, strippedPath, 0) : -1;

		if (numLevels != -1) {
			strippedPath = strippedPath.trim(numLevels).concat(-1).concat(strippedPath.trim(0, numLevels));

			buildPath(path, strippedPath);
			Node.log3("Found indexed path for variable " + field + ": " + numLevels);
		}

		return (numLevels);
	}

	public int getUpperFieldPath(String field, int lvl, Path path, int level) {
		int ndx = level < path.getNumLevels() ? getcontentndx(path.get(level)) : -1;
		CodeNode submtx = ndx != -1 ? (CodeNode) getNode(ndx) : null;
		int numLevels = submtx != null ? submtx.getUpperFieldPath(field, lvl, path, level + 1) : -1;

		if (numLevels != -1)
			numLevels++;
		else
			if (level < path.getNumLevels()) {
				numLevels = getDec() != null && getDec().getName().equals(field) ? 0 : -1;

				if (numLevels != -1 && getDec().getcodeflag()) {
					int numdim = getDec().returnType.getNumDim();

					if (lvl <= numdim)
						for (int arrayLevel = numdim - lvl; arrayLevel-- > 0; level++) {
							path.set(path.trim(level).concat(-1).concat(path.trim(0, level)));
							numLevels++;
						}
					else
						numLevels = -1; // error, bad format for inner array index
				}
				else
					; // error, no array on element
			}

		return (numLevels);
	}

	public int getUpperFieldPath(Context context, int index, String field, int lvl, Path path, int level) {
		int ndx = level < path.getNumLevels() ? getcontentndx(path.get(level)) : -1;
		CodeNode submtx = ndx != -1 ? (CodeNode) getNode(ndx) : null;
		int numLevels = submtx != null ? submtx.getUpperFieldPath(context, index, field, lvl, path, level + 1) : -1;

		if (numLevels != -1)
			numLevels += submtx != null ? 1 : 0;//path.getNumLevels() - level;
		else
			if (level < path.getNumLevels())
				if (getDec() != null && getDec().getName().equals(field) && getDec().getcodeflag()) {
					int numdim = getDec().returnType.getNumDim();

					if (lvl <= numdim)
						for (int arrayLevel = numdim - lvl; arrayLevel-- >= 0; level++) {
							path.set(path.trim(level).concat(-1).concat(path.trim(0, level)));
							numLevels++;
						}
					else
						; // error, bad format for inner array index
				}
				else
					; // error, no array on element

		return (numLevels);
	}
/*
	public int getUpperFieldPath(int[] lvl, Path path, int level) {
		int ndx = level < path.getNumLevels() ? getcontentndx(path.get(level)) : -1;
		CodeNode submtx = ndx != -1 ? (CodeNode) getNode(ndx) : null;
		int numLevels = submtx != null ? submtx.getUpperFieldPath(lvl, path, level + 1) : -1;

		if (numLevels != -1)
			numLevels++;
		else
			if (level < path.getNumLevels() && getDec() != null && getDec().getcodeflag()) {
				int numdim = getDec().returnType.getNumDim();

				numLevels = lvl[0] <= numdim ? 0 : -1;

				if (numLevels != -1)
					for (int arrayLevel = numdim - lvl[0]; arrayLevel-- > 0; level++) {
						path.set(path.trim(level).concat(-1).concat(path.trim(0, level)));
						numLevels++;
					}
				else
					lvl[0] -= numdim + 1;
			}

		return (numLevels);
	}
*/
	public int getUpperFieldPath(Context context, int index, int[] lvl, Path path, int level) {
		int ndx = level < path.getNumLevels() ? getcontentndx(path.get(level)) : -1;
		CodeNode submtx = ndx != -1 ? (CodeNode) getNode(ndx) : null;
		int numLevels = submtx != null ? submtx.getUpperFieldPath(context, index, lvl, path, level + 1) : -1;

		if (numLevels != -1)
			numLevels += submtx != null ? 1 : 0;
		else
			if (level < path.getNumLevels() && getDec() != null && getDec().getcodeflag()) {
				int numdim = getDec().returnType.getNumDim();

				if (lvl[0] <= numdim)
					for (int arrayLevel = numdim - lvl[0]; arrayLevel-- >= 0; level++) {
						path.set(path.trim(level).concat(-1).concat(path.trim(0, level)));
						numLevels++;
					}
				else
					lvl[0] -= numdim + 1;
			}

		return (numLevels);
	}

	public boolean addAllDeps(boolean leafFlag, Path path, int level) {
		boolean rc = true;

		if (level < path.getNumLevels())
			if (leafFlag && getArrayPathUnit() != null) {
				if (path.get(level) >= 0)
					path.set(path.trim(level).concat(-1).concat(path.trim(0, level)));

				rc = ((CodeNode) getArrayPathUnit()).addAllDeps(leafFlag, path, level + 1);
			}
			else {
				if (getDec() != null && ((leafFlag && getDec().returnType.isIntegral()) || getDec().returnType.getName().equals("boolean"))) {
					if (path.get(level) >= 0)
						path.set(path.trim(level).concat(-1).concat(path.trim(0, level)));

					level++;
				}

				int ndx = getcontentndx(path.get(level));

				rc = ndx == -1 || ((CodeNode) getNode(ndx)).addAllDeps(leafFlag, path, level + 1);
			}

		return (rc);
	}

	public Declaration getDec() {
		return (Declaration) unit;
	}

    public Node retrieveNode(FunctionDeclaration function, Object unit, Instruction topInst, Path path, Instruction inst) {
    	Node node = super.retrieveNode(function, unit, topInst, path, inst);
//
//		if (node != null && unit instanceof FieldDeclaration && ((FieldDeclaration) unit).getcodeflag())
//			((CodeNode) node).codeDec = (FieldDeclaration) unit;

		return node;
    }

	@Override
	public Node createNode(FunctionDeclaration function, Path path, int level, Instruction inst) {
		return (new CodeNode(null));
	}

	@Override
	public Node createArrayNode(FunctionDeclaration function, Path path, int level, Instruction inst) {
		FieldDeclaration dec = function.topMtx.getPathDec(stripPath(path.trim(level)), 0);

		if (codeDec != null)
			codeDec = codeDec;

		this.codeDec = dec;
		return (new CodeNode(null));
	}

	public boolean getcodeflag() {
		return (codeDec != null && codeDec.getcodeflag());
	}

	public String getheader() {
		return (id + ": " + super.getheader());
	}
//
//    public static class StorageUnitCode extends StorageUnitArray {
//    	public Object value;
//
//        public void initVTables(int numParams, int numSubClasses, VTableTree vTableTree) {
//        	for (int index = 0; index < numSubClasses; index++) {
//        		StorageUnitCode storageUnitCode = (StorageUnitCode) childStorageUnits.storageUnits[numParams + index];
//        		CodeObject storageCode = (CodeObject) storageUnitCode.value;
//
//        		storageCode.initVTables(vTableTree.get(index));
//        	}
//        }
//    }
//
//    public static class CodeObject extends TypeInstance {
//    	public FunctionDeclaration type;
//    	public CodeObject parent;
//    	public VTable vTable;
//    	public ArrayList<CodeObject> subObjects = new ArrayList<CodeObject>();
//    	public StorageUnitCode topStorageUnit;
//
//    	public CodeObject(CodeObject parent, FunctionDeclaration method) {
//    		super(null);
//    		this.type = method;
//    		this.parent = parent;
//    		this.vTable = method.vTableTree.getVTable();
//    	}
//
//    	@Override
//    	public String toString() {
//    		return getClass().getSimpleName();
//    	}
//
//    	public Type getReturnType() {
//    		return type.returnType;
//    	}
//
//    	public boolean run(Env env, Exp exp) {
//    		List<Exp> args = ((FuncExp) exp).args;
//			Object[] args1 = new Object[args.size()];
//
//			for (int index = 0; index < args.size(); index++)
//				args1[index] = args.get(index).eval(env);
//
//			return type.topMtx.init(env, this, args1);
//    	}
//
//    	public void initVTables(VTableTree vTableTree) {
//    		vTable = vTableTree.getVTable();
//
//    		topStorageUnit.initVTables(type.numParams, type.numSuperClasses, vTableTree);
//    	}
//
//    	public boolean waitReturnValue(Env env) {
//    		boolean formFlag = env.ui != null || (type.contentMtx.getNumNodes() != 0 || (type.contentMtx.getContentNode() != null && type.contentMtx.getContentNode().getnumcontents() != 0));
//
//    		if (formFlag)
//    			env.ui.init();
//
//    		Form form = formFlag ? env.ui.createForm(this, env) : null;
//    		CallReturnHandler returnHandler = (CallReturnHandler) this.returnHandler;
//    		boolean rc = !formFlag || (form.init() && form.processRefresh(env.ui, env.ui.getnumforms() - 1));
//
//    		while (rc && !returnHandler.returnCalled) {
//    			ExpEvent exp = readqueue(env);
//
//    			if (exp != null) {
//    	    		exp.env.parm = exp.parm;
//    	    		Type.parmType.returnType = exp.parmType;
//    	    		exp.exp.eval(exp.env);
//
//    	    		if (!returnHandler.returnCalled && formFlag)
//    	    			rc = form.resize(env) && form.processRefresh(env.ui, env.ui.getnumforms() - 1);
//    			}
//    		}
//
//    		return rc && (!formFlag || form.uninit());
//    	}
//
//    	public boolean uninit(Env env, Object value) {
//    		return type.topMtx.unInit(env, this);
//    	}
//
//    	public static boolean addExpEvent(ExpEvent expEvent) {
//    		boolean rc = true;
//
//			synchronized(expEvent.env.ui.mutexHandlers) {
//    			synchronized(expEvent.env.ui.nextEvent) {
//    				while (expEvent.env.ui.nextEvent[0] != null)
//    					try {
//    						expEvent.env.ui.nextEvent.wait();
//    					} catch (InterruptedException e) {
//    					}
//
//    				expEvent.env.ui.nextEvent[0] = expEvent;
//    				System.out.println("Event: " + expEvent);
//    				expEvent.env.ui.nextEvent.notify();
//    			}
//    		}
//
//    		return (rc);
//    	}
//
//    	public ExpEvent readqueue(Env env) {
//    		ExpEvent nextEvent = null;
//
//    		synchronized(env.ui.nextEvent) {
//    			while (env.ui.nextEvent[0] == null)
//    				try {
//    					env.ui.nextEvent.wait();
//    				} catch (InterruptedException e) {
//    				}
//
//    			nextEvent = env.ui.nextEvent[0];
//    			env.ui.nextEvent[0] = null;
//    			env.ui.nextEvent.notify();
//    		}
//
//    		return nextEvent;
//    	}
//
//    	public void setSubClass(CodeObject storageCode) {
//    		subObjects.add(storageCode);
//    	}
//
//    	public CodeObject getStorageCode(CodeNode topMtx) {
//    		CodeObject storageCode = topMtx == type.topMtx ? this : null;
//
//    		for (int index = 0; storageCode == null && index < subObjects.size(); index++)
//    			storageCode = subObjects.get(index).getStorageCode(topMtx);
//
//    		return storageCode;
//    	}
//
//    	public CodeObject getTopObject() {
//    		return subObjects.size() != 0 ? subObjects.get(0).getTopObject() : this;
//    	}
//
//    	public CodeObject getSuperCode(CodeNode topMtx) {
//    		CodeObject storageCode = topMtx == type.topMtx ? this : null;
//
//    		for (int index = 0; storageCode == null && index < type.numSuperClasses; index++)
//    			storageCode = getSuperObject(index).getSuperCode(topMtx);
//
//    		return storageCode;
//    	}
//
//    	public CodeObject getSuperObject(int index) {
//    		StorageUnitCode storageUnit = (StorageUnitCode) topStorageUnit.childStorageUnits.storageUnits[type.numParams + index];
//
//    		return (CodeObject) storageUnit.value;
//    	}
//
//		public CodeObject findFunc(FunctionDeclaration func) {
//			CodeObject object = getStorageCode(func.topMtx);
//
//			return object != null ? object : getSuperCode(func.topMtx);
//		}
//    }
/*
    public static class DataObject extends TypeInstance {
    	public FunctionDeclaration type;
    	public CodeObject parent;
    	public VTable vTable;
    	public ArrayList<CodeObject> subObjects = new ArrayList<CodeObject>();
    	public Object[] values;
    	public StorageUnitCode topStorageUnit;

    	public DataObject(CodeObject parent, FunctionDeclaration method) {
    		super(null);
    		this.type = method;
    		this.parent = parent;
    		this.vTable = method.vTableTree.getVTable();
    	}

    	@Override
    	public String toString() {
    		return getClass().getSimpleName();
    	}

    	public Type getReturnType() {
    		return type.returnType;
    	}

    	public boolean run(Env env, Exp exp) {
    		List<Exp> args = ((FuncExp) exp).args;
			Object[] args1 = new Object[args.size()];

			for (int index = 0; index < args.size(); index++)
				args1[index] = args.get(index).eval(env);

			for (int index = 0; index < 1; index++) {
			Process process = new Process(type.topDataNode.getSize(), type.byteCode);
			process.eval();

			if (!env2.stack.isEmpty())
				env2.stack = env2.stack;
    		}

			return true;//type.topMtx.init(env, null, args1);
    	}

    	public void initVTables(VTableTree vTableTree) {
    		vTable = vTableTree.getVTable();

    		topStorageUnit.initVTables(type.numParams, type.numSuperClasses, vTableTree);
    	}

    	public boolean waitReturnValue(Env env) {
    		boolean formFlag = env.ui != null || (type.contentMtx.getNumNodes() != 0 || (type.contentMtx.getContentNode() != null && type.contentMtx.getContentNode().getnumcontents() != 0));

    		if (formFlag)
    			env.ui.init();

    		Form form = formFlag ? env.ui.createForm(this, env) : null;
    		CallReturnHandler returnHandler = (CallReturnHandler) this.returnHandler;
    		boolean rc = !formFlag || (form.init() && form.processRefresh(env.ui, env.ui.getnumforms() - 1));

    		while (rc && !returnHandler.returnCalled) {
    			ExpEvent exp = readqueue(env);

    			if (exp != null) {
    	    		exp.env.parm = exp.parm;
    	    		Type.parmType.returnType = exp.parmType;
    	    		exp.exp.eval(exp.env);

    	    		if (!returnHandler.returnCalled && formFlag)
    	    			rc = form.resize(env) && form.processRefresh(env.ui, env.ui.getnumforms() - 1);
    			}
    		}

    		return rc && (!formFlag || form.uninit());
    	}

    	public boolean uninit(Env env, Object value) {
    		return type.topMtx.unInit(env, this);
    	}

    	public boolean addExpEvent(ExpEvent expEvent) {
    		boolean rc = true;

			synchronized(expEvent.env.ui.mutexHandlers) {
    			synchronized(expEvent.env.ui.nextEvent) {
    				while (expEvent.env.ui.nextEvent[0] != null)
    					try {
    						expEvent.env.ui.nextEvent.wait();
    					} catch (InterruptedException e) {
    					}

    				expEvent.env.ui.nextEvent[0] = expEvent;
    				System.out.println("Event: " + expEvent);
    				expEvent.env.ui.nextEvent.notify();
    			}
    		}

    		return (rc);
    	}

    	public ExpEvent readqueue(Env env) {
    		ExpEvent nextEvent = null;

    		synchronized(env.ui.nextEvent) {
    			while (env.ui.nextEvent[0] == null)
    				try {
    					env.ui.nextEvent.wait();
    				} catch (InterruptedException e) {
    				}

    			nextEvent = env.ui.nextEvent[0];
    			env.ui.nextEvent[0] = null;
    			env.ui.nextEvent.notify();
    		}

    		return nextEvent;
    	}

    	public void setSubClass(CodeObject storageCode) {
    		subObjects.add(storageCode);
    	}

    	public CodeObject getStorageCode(CodeNode topMtx) {
    		CodeObject storageCode = topMtx == type.topMtx ? this : null;

    		for (int index = 0; storageCode == null && index < subObjects.size(); index++)
    			storageCode = subObjects.get(index).getStorageCode(topMtx);

    		return storageCode;
    	}

    	public CodeObject getTopObject() {
    		return subObjects.size() != 0 ? subObjects.get(0).getTopObject() : this;
    	}

    	public CodeObject getSuperCode(CodeNode topMtx) {
    		CodeObject storageCode = topMtx == type.topMtx ? this : null;

    		for (int index = 0; storageCode == null && index < type.numSuperClasses; index++)
    			storageCode = getSuperObject(index).getSuperCode(topMtx);

    		return storageCode;
    	}

    	public CodeObject getSuperObject(int index) {
    		StorageUnitCode storageUnit = (StorageUnitCode) topStorageUnit.childStorageUnits.storageUnits[type.numParams + index];

    		return (CodeObject) storageUnit.value;
    	}

		public CodeObject findFunc(FunctionDeclaration func) {
			CodeObject object = getStorageCode(func.topMtx);

			return object != null ? object : getSuperCode(func.topMtx);
		}
    }
*/
//    public static class ExpEvent {
//    	public int expIndex;
//    	public Env env;
//    	public Type parmType;
//    	public Object parm;
//
//    	public ExpEvent(int expIndex, Env env, Type parmType, Object parm) {
//    		this.expIndex = expIndex;
//    		this.env = env;
//    		this.parmType = parmType;
//    		this.parm = parm;
//    	}
//
//    	public String toString() {
//    		return "" + expIndex;
//    	}
//	}
}
