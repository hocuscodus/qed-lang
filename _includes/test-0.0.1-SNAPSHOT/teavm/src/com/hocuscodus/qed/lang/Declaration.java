/*
 *    Copyright (C) 2016 Hocus Codus Software inc.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hocuscodus.qed.lang.Exp.BinaryExp;
import com.hocuscodus.qed.lang.Exp.FuncExp;
import com.hocuscodus.qed.lang.Exp.LValueExp;
import com.hocuscodus.qed.lang.Exp.RefExp;
import com.hocuscodus.qed.lang.Exp.UnaryExp;
import com.hocuscodus.qed.lang.Exp.ValueExp;
import com.hocuscodus.qed.lang.Exp.VirtualFuncExp;
import com.hocuscodus.qed.lang.type.PredefinedFuncFn;
import com.hocuscodus.qed.lang.type.Type;
import com.hocuscodus.qed.lang.type.UnaryFn;
import com.hocuscodus.qed.node.CodeNode;
import com.hocuscodus.qed.node.ContentNode;
import com.hocuscodus.qed.node.ContentObjNode;
import com.hocuscodus.qed.node.DataNode;
import com.hocuscodus.qed.node.LayoutNode;
import com.hocuscodus.qed.node.LayoutNode.HorizontalLayoutNode;
import com.hocuscodus.qed.node.LayoutNode.VerticalLayoutNode;
import com.hocuscodus.qed.node.Node;


public class Declaration extends UnaryFn {
	public Declaration(Type returnType, String name) {
		super(returnType, name, null);
	}

	public void write(DecEnv env, Path fullPath, DataNode topDataNode, Path path, OutputStream out) throws IOException {
	}

	public Exp getVarPath(Exp baseExp, String field, List<Exp> args) {
		return name != null && name.equals(field) ? new UnaryExp(Type.fieldFn, baseExp) : null;
	}

	public Exp getVarPath(Exp baseExp, Path basePath, String field, List<Exp> args) {
		return name != null && name.equals(field) ? new UnaryExp(Type.fieldFn, new LValueExp(Type.lValueFn, baseExp, basePath)) : null;
	}

    public boolean getcodeflag() {
        return (false);
    }

	public void uninit(Object value) {
	}

	public boolean addrref(Path path) {
		return true;
	}

	public String getdecstr() {
		return (returnType != null ? returnType + " " : "") + (name != null ? name : "");
	}

	public CodeNode searchVarNode(String field, CodeNode fnNode) {
		return name != null && name.equals(field) ? fnNode : null;
	}

	public boolean hasData(boolean treeFlag) {
		return false;
	}

	public String toString() {
		return getdecstr();
	}

	public static class FieldDeclaration extends Declaration {
		public Exp initExpr;
//		public Exp arrowExp;
		public boolean whileFlag;

		public FieldDeclaration(Type returnType, String name) {
			this(returnType, name, null);
		}

		public FieldDeclaration(Type returnType, String name, Exp initExpr) {
			this(returnType, name, initExpr, false);
		}

		public FieldDeclaration(Type returnType, String name, Exp initExpr, boolean whileFlag) {
			super(returnType, name);
			this.initExpr = initExpr;
			this.whileFlag = whileFlag;
		}

		public void endFieldBlock(Context context, boolean leafFlag) {
			context.getCtx().addAllDeps(leafFlag);

			if (initExpr == null && returnType != null)
				initExpr = new ValueExp(returnType, returnType.newDefault());
//
//				if (name != null || returnType != Type.voidType)
//					initExpr = new BinaryExp(Type.assignmentFn, new ValueExp(Type.intType, context.getCtx().topDataNode.getIndex(context.getCtx().newPath)), initExpr);
		}

	    public boolean getcodeflag() {
	        return (returnType.getcodeflag());
	    }

		public void write(DecEnv env, Path fullPath, DataNode topDataNode, Path path, OutputStream out) throws IOException {
//			if (initExpr == null || !(initExpr.getType() instanceof ArrayType)) {
//				public void eval(Process process, Call call) {
//					Call obj = (Call) call.stack.peek();
//
//					obj.handlerData = null;
//					obj.handlerInstPointer = call.instPointer + 5;
//				}
				if (initExpr != null) {
					Exp exp2 = whileFlag ? ((BinaryExp) initExpr).value2 : null;

					if (whileFlag)
						((BinaryExp) initExpr).value2 = new ValueExp(Type.intType, 0);

					initExpr.write(env, fullPath, out);

					if (whileFlag)
						((BinaryExp) initExpr).value2 = exp2;
				}

				if (returnType != null && returnType != Type.voidType)
					new Exp(Type.devNullType).write(env, fullPath, out);
		}

		public void uninit(Object value) {
//			if (returnType != null)
//				returnType.stop(value);//value.type.stop(value.value);
		}

		public boolean hasData(boolean treeFlag) {
			return name != null || returnType instanceof FunctionDeclaration || returnType instanceof PredefinedFuncFn || (treeFlag && (returnType == Type.booleanType || returnType.isIntegral()));
		}

		public String getdecstr() {
			return super.getdecstr() + (initExpr != null ? " = " + initExpr : "");
		}
	}

	public static class SubClassDeclaration extends FieldDeclaration {
		FunctionDeclaration upperClass;

		public SubClassDeclaration(Type type, String name, Exp initExpr) {
			super(type, name, initExpr);
		}
//
//		public Object eval(Env env, Exp exp) {
//			Object obj = super.eval(env, exp);
//
//			((CodeObject) obj).setSubClass((CodeObject) env.storageCode);
//			return obj;
//	    }

		public Exp getVarPath(Exp baseExp, String field, List<Exp> args) {
			Exp exp = super.getVarPath(baseExp, field, args);

//			if (exp == null)
//				exp = new UnaryExp(Type.fieldFn, baseExp).findVarPath(field, args, true);
			if (exp == null) {
				exp = new UnaryExp(Type.fieldFn, baseExp);
				exp = exp.findVarPath(null, field, args, true);
//			exp = new UnaryExp(Type.dotFn, exp);
//			exp = exp.getReturnType().findVarPath(exp, field, args);
			}

			return exp;
		}

		public Exp getVarPath(Exp baseExp, Path basePath, String field, List<Exp> args) {
			Exp exp = super.getVarPath(baseExp, basePath, field, args);

			if (exp == null) {
				Exp exp1 = new UnaryExp(Type.fieldFn, new LValueExp(Type.lValueFn, baseExp, basePath));
				Exp dotExp = new UnaryExp(Type.dotFn, exp1);

				exp = ((FunctionDeclaration) returnType).findVarPath(dotExp, field, args, false);
////	        	DecEnv env = function.buildDecEnv(getCtx().path);
//
////				if (field.equals("nextStep"))
//					exp = ((FunctionDeclaration) returnType).findVarPath(exp1, field, args, true);
////					exp = exp1.getReturnType(((FunctionDeclaration) returnType).buildDecEnv(new Path())).findVarPath(exp1, field, args, true);
////				if (exp != null)
////					exp = exp;
			}

			return exp;
		}

		public CodeNode searchVarNode(String field, CodeNode fnNode) {
			CodeNode node = super.searchVarNode(field, fnNode);

			if (node == null)
				node = ((FunctionDeclaration) returnType).topMtx.findVarNode(field, null);

			return node;
		}
	}

	public static class VTableEntry {
		public CodeNode topMtx;
		public int fnIndex;

		public VTableEntry(CodeNode topMtx, int fnIndex) {
			this.topMtx = topMtx;
			this.fnIndex = fnIndex;
		}
	}

	@SuppressWarnings("serial")
	public static class VTable extends ArrayList<VTableEntry> {
		public void add(CodeNode topMtx, int fnIndex) {
			add(new VTableEntry(topMtx, fnIndex));
		}

		VTableEntry findEntry(FunctionDeclaration func) {
			int index;

			for (index = 0; index < size(); index++) {
				VTableEntry entry = get(index);
				CodeNode node = (CodeNode) entry.topMtx.getNode(entry.fnIndex);
				FunctionDeclaration function = (FunctionDeclaration) node.getDec();

				if (function.equals(func))
					break;
			}

			return index < size() ? get(index) : null;
		}
	}

	@SuppressWarnings("serial")
	public static class VTableTree extends ArrayList<VTableTree> {
		CodeNode topMtx;
		VTable vTable = new VTable();

		public VTableTree(CodeNode topMtx) {
			this.topMtx = topMtx;
		}

		public void add(CodeNode topMtx, int fnIndex) {
			int ndx = topMtx.getcontentndx(fnIndex);
			CodeNode node = ndx != -1 ? (CodeNode) topMtx.getNode(ndx) : null;
			VTableEntry entry = node != null ? findEntry((FunctionDeclaration) node.getDec()) : null;

			if (entry != null) {
				entry.topMtx = topMtx;
				entry.fnIndex = fnIndex;
			}
			else
				getVTable().add(topMtx, fnIndex);
		}

		VTableEntry findEntry(FunctionDeclaration func) {
			VTableEntry entry = null;

			for (int index = 0; entry == null && index < size(); index++)
				entry = get(index).findEntry(func);

			if (entry == null)
				entry = getVTable().findEntry(func);

			return entry;
		}

		VTableTree findFunc(FunctionDeclaration func) {
			VTableTree tree = topMtx == func.topMtx ? this : null;

			for (int index = 0; tree == null && index < size(); index++)
				tree = get(index).findFunc(func);

			return tree;
		}

		public void addTableTree(VTableTree vTableTree) {
			VTableTree newVTable = new VTableTree(vTableTree.topMtx);

			for (int index = 0; index < vTableTree.getVTable().size(); index++) {
				VTableEntry vTableEntry = vTableTree.getVTable().get(index);

				newVTable.getVTable().add(vTableEntry.topMtx, vTableEntry.fnIndex);
			}

			for (int index = 0; index < vTableTree.size(); index++)
				newVTable.addTableTree(vTableTree.get(index));

			add(newVTable);
		}

		public VTable getVTable() {
			return vTable;
		}
	}
/*
	public static class EventHandler {
		public int offset;
		public Path path;

		EventHandler(int offset, Path path) {
			this.offset = offset;
			this.path = path;
		}
	}

	public static class EventHandlerList {
		public ArrayList<EventHandler> eventHandlers = new ArrayList<EventHandler>();

    	public void addEventHandler(int offset, Path path) {
    		eventHandlers.add(new EventHandler(offset, path));
		}

    	public int getEventHandler(Path path) {
    		EventHandler handler = null;

    		for (int index = 0; handler == null && index < eventHandlers.size(); index++) {
    			EventHandler handler2 = eventHandlers.get(index);

    			handler = handler2.path.equals(path) ? handler2 : null;
    		}

    		return handler != null ? handler.offset : -1;
    	}
	}
*/
	public static HashMap<Integer, FunctionDeclaration> funcs = new HashMap<Integer, FunctionDeclaration>();

	public static class InternalDeclaration extends Declaration {
		public CodeNode topMtx;
		public DataNode topDataNode;

		public InternalDeclaration(CodeNode topMtx, DataNode topDataNode) {
			super(Type.voidType, null);
			this.topMtx = topMtx;
			this.topDataNode = topDataNode;
		}
	}

	public static class DecEnv {
		public FunctionDeclaration parent;
		public InternalDeclaration[] declarations;

		public DecEnv(FunctionDeclaration parent) {
			this.parent = parent;
			declarations = new InternalDeclaration[] {parent};
		}

		public DecEnv(DecEnv upper, InternalDeclaration declaration) {
			this.parent = upper.parent;
			declarations = new InternalDeclaration[upper.declarations.length + 1];
			System.arraycopy(upper.declarations, 0, declarations, 0, upper.declarations.length);
			declarations[upper.declarations.length] = declaration;
		}
	}

	public static class FunctionDeclaration extends InternalDeclaration {
		public static int idAllocator = 1;
		public int id;
		public FunctionDeclaration parent;
		public InternalDeclaration[] declarations;
		public int numParams;
		public int numSuperClasses;
		public ContentNode contentMtx;
		public ContentObjNode contentMtxCx;
		public LayoutNode[] layoutMtxs = new LayoutNode[LayoutNode.numdim];
		public VTableTree vTableTree;
//		public EventHandlerList eventHandlers = new EventHandlerList();
		public int offset;
		public int cleanupOffset;

		public FunctionDeclaration(FunctionDeclaration parent) {
			super(new CodeNode(null), new DataNode());
			id = idAllocator++;
			this.parent = parent;
			funcs.put(id, this);
			vTableTree = new VTableTree(topMtx);
		}

		public FunctionDeclaration set(Type returnType, String name, int numParams, int numSuperClasses) {
			this.returnType = returnType;
			this.name = name;
			this.numParams = numParams;
			this.numSuperClasses = numSuperClasses;
			return this;
		}

		public Type getReturnType(Exp exp, DecEnv env) {
    		return this;
		}

    	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
    		FuncExp funcExp = (FuncExp) exp;
    		int numParams = funcExp.args.size();

    		for (int index = 0; index < numParams; index++)
	        	funcExp.args.get(index).write(env, path, out);

    		if (getExp(exp) != null)
    			getExp(exp).write(env, path, out);
    		else
    			out = out;

    		new UnaryExp(Type.pushObjectFn, new ValueExp(Type.intType, id)).write(env, path, out);
    		out.write(numParams);
    	}

    	public void writeUninitExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
			getExp(exp).write(env, path, out);
    		new UnaryExp(Type.cleanObjectFn, new ValueExp(Type.intType, id)).write(env, path, out);
    	}

    	public void write(DecEnv env, Path fullPath, DataNode topDataNode, Path path, OutputStream out) throws IOException {
//    		super.write(out);
//    		topMtx.write(out);
    	}
/*
    	public void addEventHandler(int offset, Path path) {
    		eventHandlers.addEventHandler(offset, path);
		}*/
//
//		public Object eval(Env env, Exp exp) {
//    		NodeInfo info = getExp(exp) != null ? (NodeInfo) super.eval(env, exp) : null;
//			List<Exp> args = ((FuncExp) exp).args;
//			Object[] args1 = new Object[args.size()];
//
//			for (int index = 0; index < args.size(); index++)
//				args1[index] = args.get(index).eval(env);
////
////			if (env.topNode != null && (StorageUnitCode) env.topNode.getField(env.path) != env.currentNode)
////				env.currentNode = env.currentNode;
//
//			return new StorageCode(info != null ? info.storageCode : null, /*env.topNode != null ? (StorageUnitCode) env.topNode.getField(env.path) : null*/env.currentNode, this, args1);
//		}
//
//		public Object evalTmp(Env env, Exp exp) {
//    		NodeInfo info = (NodeInfo) super.eval(env, exp);
////
////			if (env.topNode != null && (StorageUnitCode) env.topNode.getField(env.path) != env.currentNode)
////				env.currentNode = env.currentNode;
//
//			CodeObject storageCode = new CodeObject(info != null ? info.storageCode : null, this);
////			storageCode.topStorageUnit = (StorageUnit) topMtx.eval(new Env(env, storageCode, storageCode, null), null);
//
//			return storageCode;
//		}
//
//		public boolean init(Env env) {
//	    	env.storageNode.topStorageUnit = topMtx.eval(env, null);
//	    	boolean rc = env.storageNode.topStorageUnit && env.topNode.parseinitunits(env, method.numSuperClasses, args);
//
//	    	if (rc)
//	    		initVTables(method.vTableTree);
//
//	    	return rc;
//	    }
//
//	    public void initVTables(VTableTree vTableTree) {
//	    	vTable = vTableTree.getVTable();
//
//	    	((StorageUnitCode) topStorageUnit).initVTables(method.numParams, method.numSuperClasses, vTableTree);
//	    }

		public void endMethodBlock(Context context, Instruction inst) {
			endMethodBlock(context, inst, null);
		}

		public void endMethodBlock(Context context, Instruction inst, Exp handler) {
//			Node.log3("MTX MAP:\n" + topMtx);

			// pad parameters with empty (for now) instructions...
			for (int index = 0; index < numParams; index++)
				inst.block.add(0, new Instruction(null, null));

			for (int index = 0; index < numSuperClasses; index++) {
				// pad parameters with empty (for now) instructions...
				inst.block.add(0, new Instruction(null, null));
				// set upper class for virtual functions and build vfptr
				((SubClassDeclaration) ((CodeNode) topMtx.getNode(numParams + index)).unit).upperClass = this;

				if (handler != null) {
					BinaryExp exp = (BinaryExp) ((SubClassDeclaration) ((CodeNode) topMtx.getNode(numParams + index)).unit).initExpr;

					exp.value2 = new BinaryExp(Type.arrowFn, exp.value2, handler);
				}
			}

			contentMtx = new ContentNode();

			try {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				DecEnv env = buildDecEnv(context.getCtx().path);

				topMtx.write(this, topDataNode, out);
				new Exp(Type.haltFn).write(env, new Path(), out);
				cleanupOffset = out.size();
				topMtx.writeUninitCode(this, topDataNode, out);
				new Exp(Type.haltFn).write(env, new Path(), out);
				contentMtx.buildContentTree(context, inst, inst, new Path(), out);

				byte[] byteCode = out.toByteArray();

				offset = context.out.size();

				context.out.write(byteCode);
			} catch (IOException e) {
				e.printStackTrace();
			}

			Node.log3("CONTENT MAP:\n" + contentMtx);

			contentMtxCx = new ContentObjNode();
			contentMtxCx.buildContentTree(context, inst, inst, contentMtx, new Path());
			Node.log3("CONTENT CX MAP:\n" + contentMtxCx);

			LayoutNode.layoutIndex = 0;
			layoutMtxs[0] = new HorizontalLayoutNode(inst);
			LayoutNode.layoutIndex = 1;
			layoutMtxs[1] = new VerticalLayoutNode(inst);

			for (LayoutNode.layoutIndex = 0; LayoutNode.layoutIndex < layoutMtxs.length; LayoutNode.layoutIndex++) {
				layoutMtxs[LayoutNode.layoutIndex].buildContentTree(context, inst, inst, contentMtx, new Path());
				Node.log3("LAYOUT MAP " + LayoutNode.layoutIndex + ":\n" + layoutMtxs[LayoutNode.layoutIndex]);
			}
		}

		public Exp getVarPath(Path path) {
			Exp exp = topMtx.findVarPath(new RefExp(0), path, 0, new Path());

			return exp != null ? exp : topMtx.getVarPath(0, path, 0);
		}

		public Path getVarPath2(Path path) {
			Exp exp = getVarPath(Node.stripPath(path));

			return exp != null ? ((LValueExp) ((UnaryExp) exp).exp).path : null;
		}

		public Exp findVarPath(Exp baseExp, String field, List<Exp> args, boolean virtualFlag) {
			//Exp exp = topMtx.findVarPath(baseExp, new Path(), field, args);
			Exp exp = topMtx.findVarPath(baseExp, new Path(), field, args);

			if (virtualFlag && exp != null && exp instanceof FuncExp) {
				FuncExp funcExp = (FuncExp) exp;

				if (funcExp.type instanceof FunctionDeclaration && false/*funcExp.exp.type == Type.subFn*/) {
					BinaryExp subExp = (BinaryExp) funcExp.exp;

					if (subExp.value1 == baseExp) {
						FunctionDeclaration func = (FunctionDeclaration) funcExp.type;

						for (int index = 0; index < vTableTree.getVTable().size(); index++) {
							VTableEntry entry = vTableTree.getVTable().get(index);
							CodeNode node = (CodeNode) entry.topMtx.getNode(entry.fnIndex);
							FunctionDeclaration function = (FunctionDeclaration) node.getDec();

							if (func.equals(function)) {
								exp = new VirtualFuncExp(baseExp, args, index);
								break;
							}
						}
					}
				}
			}

			return exp;
		}

		public Exp getVarPath(Exp baseExp, String field, List<Exp> args) {
			return name != null && name.equals(field) ? new FuncExp(this, baseExp, args) : null;
//			Exp exp = super.getVarPath(baseExp, field, args);
////
////			if (exp == null)
////				exp = new UnaryExp(Type.fieldFn, baseExp).findVarPath(field, args);
//
//			return exp;
////
////			return (exp != null ? exp : topMtx.findVarPath(baseExp, field, args));
		}

		public Exp getVarPath(Exp baseExp, Path basePath, String field, List<Exp> args) {
			return name != null && name.equals(field) ? new FuncExp(this, new LValueExp(Type.thisLValueFn, baseExp, basePath), args) : null;
		}

		public String getdecstr() {
			String str = super.getdecstr() + "(";

			for (int index = 0; index < numParams; index++) {
				str += index == 0 ? "" : ", ";
				str += "";//params.get(index).type + " " + params.get(index).name;
			}

			return (str + ")");
		}

		public boolean equals(FunctionDeclaration func) {
			boolean rc = func.getName().equals(name) && func.numParams == numParams;

			for (int index = 0; rc && index < numParams; index++)
				rc = true;//

			return rc;
		}

		public boolean canCast(Type valueType) {
			FunctionDeclaration func = valueType instanceof FunctionDeclaration ? (FunctionDeclaration) valueType : null;

			return func != null && (vTableTree.findFunc(func) != null || func.vTableTree.findFunc(this) != null);
		}
//
//		public Object cast(Type valueType, Object value) {
//			CodeObject codeObject = (CodeObject) value;
//
//			return codeObject.findFunc(this);
//		}

		public DecEnv buildDecEnv(Path path) {
			return topMtx.buildDecEnv(new DecEnv(this), path, 0);
		}

		public String toString(Exp exp) {
			List<Exp> args = ((FuncExp) exp).args;
			String str = name;

			if (args != null) {
				str += "(";

				for (int index = 0; index < args.size(); index++)
					str += (index == 0 ? "" : ", ") + args.get(index);

				str += ")";
			}

			return (str);
		}
	}

	public static class Module extends FunctionDeclaration {
		Exp exp;
//		CodeObject instance;
		ArrayList<Module> modules;

		public Module(String name, ArrayList<Module> modules) {
			super(null);//, Type.intType, name, 1, 0);
			this.modules = modules;
			exp = new BinaryExp(Type.newFn, new FuncExp(this, null, new ArrayList<Exp>()), null);
		}
//
//		public Object evalTmp(Env env, Exp exp) {
//			for (int index = 0; index < getNumModules(); index++) {
//				Module module = getModule(index);
//
//				if (module.instance == null) {
//					module.evalTmp(env, exp);
//					module.instance = (CodeObject) module.exp.eval(env);
//				}
//			}
//
//			return super.evalTmp(env, exp);
//		}

		public Exp getVarPath(Exp baseExp, String field, List<Exp> args) {
			Exp exp = null;

			for (int index = 0; exp == null && index < getNumModules(); index++) {
				Module module = getModule(index);

				exp = module.findVarPath(new ValueExp(Type.moduleType, module), name, args, true);

				if (exp == null)
					exp = module.getVarPath(module.exp, field, args);
			}

			return exp;
		}

		public Exp getVarPath(Exp baseExp, Path basePath, String field, List<Exp> args) {
			Exp exp = null;

			for (int index = 0; exp == null && index < getNumModules(); index++) {
				Module module = getModule(index);

				exp = module.topMtx.findVarPath(new ValueExp(Type.moduleType, module), basePath, name, args);

				if (exp == null)
					exp = module.getVarPath(module.exp, basePath, field, args);
			}

			return exp;
		}

		public CodeNode searchVarNode(String field, CodeNode fnNode) {
			CodeNode node = super.searchVarNode(field, fnNode);

			for (int index = 0; node == null && index < getNumModules(); index++) {
				Module module = getModule(index);

				node = module.topMtx.findVarNode(field, null);

				if (node == null)
					node = module.searchVarNode(field, fnNode);
			}

			return node;
		}

		public int getNumModules() {
			return modules.size();
		}

		public Module getModule(int index) {
			return modules.get(index);
		}

		public Module findModule(String name) {
			Module module = getName().equals(name) ? this : null;

			for (int index = 0; module == null && index < modules.size(); index++)
				module = modules.get(index).findModule(name);

			return module;
		}
	}
}