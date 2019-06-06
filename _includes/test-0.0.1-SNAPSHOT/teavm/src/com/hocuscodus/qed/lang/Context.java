/*
 *    Copyright (C) 2016 Hocus Codus Software inc.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.hocuscodus.qed.lang.Declaration.DecEnv;
import com.hocuscodus.qed.lang.Declaration.FieldDeclaration;
import com.hocuscodus.qed.lang.Declaration.FunctionDeclaration;
import com.hocuscodus.qed.lang.Declaration.InternalDeclaration;
import com.hocuscodus.qed.lang.Declaration.Module;
import com.hocuscodus.qed.lang.Declaration.VTableTree;
import com.hocuscodus.qed.lang.Exp.BinaryExp;
import com.hocuscodus.qed.lang.Exp.FuncExp;
import com.hocuscodus.qed.lang.Exp.RefExp;
import com.hocuscodus.qed.lang.Exp.ValueExp;
import com.hocuscodus.qed.lang.type.Type;
import com.hocuscodus.qed.node.CodeNode;
import com.hocuscodus.qed.node.Node;


public class Context {
	private static Context nextContextStorage;
	private Context nextContext;
	ArrayList<Module> modules = new ArrayList<Module>();
	private int next = 0;
	public List<Ctx> methods = new ArrayList<Ctx>();
	public QEDProcess process;
	public ByteArrayOutputStream out;

	public Context(QEDProcess process, ByteArrayOutputStream out) {
		nextContext = nextContextStorage;
		this.process = process;
		this.out = out;
	}

	public void pushInst() {
		getCtx().pushInst(next);
		next = 0;
	}

	public void popInst() {
		next = getCtx().popInst() + 1;
	}

	public void pushPath() {
		if (getCtx() != null)
		getCtx().pushPath();
	}

	public Path popPath() {
		return getCtx() != null ? getCtx().popPath() : null;
	}

	public boolean popPath(Object unit) {
		return popPath(unit, false);
	}

	public boolean popPath(Object unit, boolean treeFlag) {
		Path path = popPath();
		FunctionDeclaration function = getCtx().declaration;

		if (((Declaration) unit).hasData(treeFlag))
			function.topDataNode.retrieveNode(function, unit, null, path, null);

		boolean rc = function.topMtx.retrieveNode(function, unit, null, path, null) != null;//true;

		if (rc && ((Declaration) unit).hasData(treeFlag)) {
	        FieldDeclaration fieldDeclaration = (FieldDeclaration) unit;

			if (fieldDeclaration.name != null || fieldDeclaration.returnType != Type.voidType) {
				Exp exp = getCtx().declaration.getVarPath(getCtx().path);
	        	DecEnv env = function.buildDecEnv(getCtx().path);
	        	Type returnType = exp.getReturnType(env);

	        	if (returnType != null)
	        		fieldDeclaration.initExpr = new BinaryExp(Type.assignmentFn, exp, fieldDeclaration.initExpr != null ? (fieldDeclaration.whileFlag ? Type.booleanType : returnType).cast(fieldDeclaration.initExpr, env)/*new BinaryExp(Type.castFn, new Exp(fieldDeclaration.whileFlag ? Type.booleanType : returnType), fieldDeclaration.initExpr)*/ : new ValueExp(returnType, returnType.newDefault()));
	        }
		}
//
//		if (rc && unit instanceof FunctionDeclaration)
//			((FunctionDeclaration) unit).declarations = function.buildDecEnv(getCtx().path).declarations;

		if (rc && path.getNumLevels() == 1 && path.get(0) >= 0 && unit instanceof FunctionDeclaration)
			getCtx().getVTableTree().add(function.topMtx, path.get(0));

		return rc;
	}

	public void pushCtx() {
		Ctx ctx = getCtx();

		methods.add(new Ctx(ctx != null ? ctx.declaration : null, ctx != null ? ctx.declaration.buildDecEnv(ctx.path).declarations : null));
	}

	public FunctionDeclaration popCtx() {
		int size = methods.size();

		return size > 0 ? methods.remove(size - 1).declaration : null;
	}

	public FunctionDeclaration getCurrentFunction() {
		Ctx ctx = getCtx();

		return ctx != null ? ctx.declaration : null;
	}

	public Ctx getCtx() {
		return getCtx(0);
	}

	public Ctx getCtx(int index) {
		int size = methods.size();

		return index >= 0 && index < size ? methods.get(size - 1 - index) : null;
	}

	public Exp getCodeNumLevels(String name) {
		Exp exp = null;

		for (int index = 0; exp == null && index < methods.size(); index++)
			exp = getCtx(index).getCodeNumLevels(index, name);

		return (exp);
	}

	public Exp getCodeNumLevels(int level) {
		Exp exp = null;
		int[] lvl = {level};

		for (int index = 0; exp == null && index < methods.size(); index++)
			exp = getCtx(index).getCodeNumLevels(index, lvl);

		return exp;
	}

	public CodeNode getCodeNode(String name) {
		CodeNode codeNode = null;

		for (int index = 0; codeNode == null && index <  methods.size(); index++)
			codeNode = getCtx(index).getCodeNode(name);

		for (int index = 0; codeNode == null && index < modules.size(); index++) {
			Module module = modules.get(index);

			codeNode = module.topMtx.findVarNode(name, null);

			if (codeNode == null)
				codeNode = module.searchVarNode(name, null);
		}

		return (codeNode);
	}

	public Exp getNameExp(List<String> names, List<Exp> args) {
		String name = names.get(0);
		Type func = names.size() == 1 ? Type.getFunc(name, args) : null;
		Exp exp = func != null ? new FuncExp(func, null, args) : null;

		if (exp == null) {
			for (int index = 0; exp == null && index < methods.size(); index++)
				exp = getCtx(index).getCodeNodePath(index, name, names.size() == 1 ? args : null);

			for (int index = 0; exp == null && index < modules.size(); index++) {
				Module module = modules.get(index);

				exp = module.findVarPath(new ValueExp(Type.moduleType, module), name, args, true);

				if (exp == null)
					exp = module.getVarPath(module.exp, name, args);
			}

			for (int index = 1; exp != null && index < names.size(); index++)
				exp = exp.findVarPath(getCtx().declaration.buildDecEnv(getCtx().path), names.get(index), index == names.size() - 1 ? args : null, false/*exp.getVirtualFlag(names.get(index - 1))*/);
		}
		else
			getCtx().addAllDeps(true);

		return (exp);
	}

	public static String buildModuleName(List<String> fileName) {
		String name = "_" + fileName.get(0);

		for (int index = 1; index < fileName.size(); index++)
			name += "." + fileName.get(index);

		return name;
	}

	public Module findModule(String name) {
		Module module = null;

		for (int index = 0; module == null && index < modules.size(); index++)
			module = modules.get(index).findModule(name);

		return module;
	}

	public Module findExtModule(String name) {
		Module module = findModule(name);

		return module != null ? module : nextContext != null ? nextContext.findExtModule(name) : null;
	}

	public FunctionDeclaration importQED(List<String> fileName) throws Exception {
		String name = buildModuleName(fileName);
		FunctionDeclaration module = findModule(name);

		if (module == null) {
			module = nextContext != null ? nextContext.findExtModule(name) : null;

			if (module == null) {
				nextContextStorage = this;
				module = process.parseQED(fileName.get(0) + ".qed", out);

				if (module != null)
					module.name = name;
			}
//
//			if (module != null)
//				modules.add(module);
		}

		return module;
	}

	public VTableTree getVTableTree() {
		return getCtx().getVTableTree();
	}
//
//	public void addLateBindingEntry(List<String> names, List<Exp> args) {
//		Exp exp = getNameExp(0, names.get(0), names.size() == 1 ? args : null);
//
//		if (exp != null && names.size() > 1)
//			exp = exp.findVarPath(names, 1, args);
//	}
//
//	public static class LateBindingEntry {
//		public CodeNode topMtx;
//		public Path path = new Path();
//		public List<String> names;
//		public List<Exp> args;
//
//		LateBindingEntry(Path path, List<String> names, List<Exp> args) {
//			this.path = path;
//			this.names = names;
//			this.args = args;
//		}
//
//		public Exp getNameExp(List<String> names, List<Exp> args) {
//			Path strippedPath = new Path(path);
//			Exp exp = topMtx.getVarPath(new Context(), 0, new Exp(topMtx), names.get(0), args, strippedPath, 0);
//
//			if (exp != null) {
////				CodeNode.buildPath(newPath, strippedPath);
//				Storage.log3("Found path for variable " + exp);
//			}
//
//			return exp;
//		}
//	}

	public static class Ctx {/*
		public CodeNode topMtx;
		public DataNode topDataNode;
		public VTableTree vTableTree;*/
		public FunctionDeclaration declaration;
		public Path path = new Path();
		public Path newPath;
//
//		public Ctx() {
//			topMtx = new CodeNode();
//			topDataNode = new DataNode();
//			vTableTree = new VTableTree(topMtx);
//		}

		public Ctx(FunctionDeclaration parent, InternalDeclaration[] declarations) {
			declaration = new FunctionDeclaration(parent);
			declaration.declarations = declarations;
		}

		public void pushInst(int next) {
			path.set(path.concat(next));
		}

		public int popInst() {
			int next = -1;
			int size = path.getNumLevels() - 1;

			if (size >= 0) {
				next = path.get(size);
				path.set(path.trim(size, 1));
			}

			return next;
		}

		public void pushPath() {
			newPath = new Path(path);
		}

		public Path popPath() {
			Path path = newPath;

			newPath = null;
			return path;
		}

		public CodeNode getCodeNode(String name) {
			Path strippedPath = new Path(path);
			CodeNode node = declaration.topMtx.searchVarNode(name, strippedPath, 0);

			if (node != null)
				CodeNode.buildPath(newPath, strippedPath);

			return (node);
		}
/*
		public CodeNode getCodeNode(Context context, int index, String name) {
			Path strippedPath = new Path(path);
			CodeNode node = declaration.topMtx.searchVarNode(context, index, name, strippedPath, 0);

			if (node != null)
				CodeNode.buildPath(newPath, strippedPath);

			return (node);
		}
*/
		public Exp getCodeNumLevels(int index, String field) {
			Exp exp = null;
			Path strippedPath = new Path(path);
			int numLevels = field != null ? declaration.topDataNode.getUpperFieldPath(field, strippedPath, 0) : -1;

			if (numLevels != -1) {
				CodeNode.buildPath(newPath, strippedPath);
				exp = new BinaryExp(Type.varIndexFn2, new ValueExp(Type.intType, index), new ValueExp(Type.intType, numLevels));
				Node.log3("Found indexed path for variable " + field + ": " + numLevels);
			}

			return (exp);
		}
/*
		public int getCodeNumLevels(int level) {
			Path strippedPath = new Path(path);
			int numLevels = declaration.topMtx.getUpperFieldPath(new int[] {level}, strippedPath, 0);

			if (numLevels != -1) {
				strippedPath = strippedPath.trim(numLevels).concat(-1).concat(strippedPath.trim(0, numLevels));

				CodeNode.buildPath(newPath, strippedPath);
				Node.log3("Found indexed path for variable index " + level + ": " + numLevels);
			}

			return numLevels;
		}
*/
		public Exp getCodeNumLevels(int index, int[] level) {
			Path strippedPath = new Path(path);
			int numLevels = declaration.topDataNode.getUpperFieldPath(level, strippedPath, 0);
			Exp exp = null;

			if (numLevels != -1) {
				CodeNode.buildPath(newPath, strippedPath);
				exp = new BinaryExp(Type.varIndexFn2, new ValueExp(Type.intType, index), new ValueExp(Type.intType, numLevels));
				Node.log3("Found indexed path for variable index " + level[0] + ": " + numLevels);
			}

			return exp;
		}

		public Exp getCodeNodePath(int index, String name, List<Exp> args) {
			Path strippedPath = new Path(path);
			Exp exp = declaration.topMtx.getVarPath(index, 0, name, args, strippedPath, 0);

			if (exp == null)
				exp = declaration.findVarPath(new RefExp(index << 8), name, args, false);

			if (exp != null) {
				CodeNode.buildPath(newPath, strippedPath);
				Node.log3("Found path for variable " + exp);
			}

			return exp;
		}

		public boolean addAllDeps(boolean leafFlag) {
			return declaration.topMtx.addAllDeps(leafFlag, newPath, 0);
		}

		public VTableTree getVTableTree() {
			return declaration.vTableTree;
		}
	}
}
