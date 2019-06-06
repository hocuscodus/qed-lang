/*
 *    Copyright (C) 2016 Hocus Codus Software inc.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang.type;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.Declaration.DecEnv;
import com.hocuscodus.qed.lang.Env;
import com.hocuscodus.qed.lang.Exp;
import com.hocuscodus.qed.lang.Exp.BinaryExp;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.QEDProcess;
import com.hocuscodus.qed.node.ContentNode;
import com.hocuscodus.qed.node.LayoutNode.LayoutContextSet;
import com.hocuscodus.qed.node.Node;


public class Type {
	public static ArrayList<UIContext> uiContexts = new ArrayList<UIContext>();

	public byte opCode;
	public Type returnType;
	public String name;
	public Type[] params;

	public Type() {
		this(null, null);
	}

	public Type(Type returnType, String name) {
		this(returnType, name, null);
	}

	public Type(Type returnType, String name, Type[] params) {
		this.returnType = returnType;
		this.name = name;
		this.params = params;
	}

	public Type getReturnType(Exp exp, DecEnv env) {
		return returnType;
	}

	public Node getNode(int index) {
		return (null);
	}

	public boolean getFuncFlag(String name, List<Exp> args) {
		return getName() != null && getName().equals(name) && (params != null ? args != null && params.length == args.size() : args == null);
	}

	public boolean getVirtualFlag(Exp exp, String name) {
		return true;
	}

	public static void pushUIContext(LayoutContextSet layoutContextSet, int[] pos, int[] size, Object ctx) {
		uiContexts.add(new UIContext(layoutContextSet, pos, size, ctx));
	}

	public static boolean popUIContext() {
		return uiContexts.size() != 0 && uiContexts.remove(uiContexts.size() - 1) != null;
	}

	public static UIContext getUIContext() {
		return uiContexts.size() != 0 ? uiContexts.get(uiContexts.size() - 1) : null;
	}

	public Object eval(Env env, Exp exp) {
		return null;
	}

	public void write(OutputStream out, int value) throws IOException {
		out.write(value);
	}

	public void write(OutputStream out, byte[] value) throws IOException {
		out.write(value);
	}

	public void writeExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
		if (opCode == 0)
			opCode = opCode;
		write(out, opCode);
	}

	public void writeUninitExp(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
	}

	public void writeExpAssignment(Exp exp, DecEnv env, Path path, OutputStream out) throws IOException {
	}

	public void eval(QEDProcess process, Call call) {
	}

	public boolean makerefs(Exp exp, Path path) {
		return true;
	}

	public boolean findArrayFieldExp(Exp exp, Exp indexExp) {
		return false;
	}

	public int getLevel(Env env, Exp exp, Path[] path) {
		return 0;
	}

	public boolean isPredefined() {
		return false;
	}

	public boolean isNumeric() {
		return false;
	}

	public boolean isIntegral() {
		return false;
	}

	public int getCodeNumDim() {
		return (isNumeric() ? 1 : 0) + getNumDim();
	}

	public boolean getcodeflag() {
		return isIntegral();
	}

	public long getInteger(Object value) {
		return -1L;
	}

	public Object setInteger(long num) {
		return null;
	}

	public String getName() {
		return name;
	}

	public int getNumDim() {
		return 0;
	}

	public Type getdowntype(int levels) {
		return levels > 0 ? null : this;
	}

	public Exp findVarPath(Exp baseExp, String field, List<Exp> args, boolean virtualFlag) {
		return null;
	}

	protected Type[] getCastTypes() {
		return new Type[0];
	}

	protected int getCastIndex(Type[] types, Type valueType) {
		int index;

		for (index = 0; index < types.length && types[index] != valueType; index++);

		return (index < types.length ? index : -1);
	}

	public boolean canCast(Type valueType) {
		return (getCastIndex(getCastTypes(), valueType) != -1);
	}

	public Object cast(Type valueType, Object value) {
		return cast(getCastIndex(getCastTypes(), valueType), value);
	}

	public Object cast(int typeIndex, Object value) {
		return value;
	}

	public Object add(Object value1, Type value2Type, Object value2) {
		return add(value1, value2);
	}

	public Object subtract(Object value1, Type value2Type, Object value2) {
		return subtract(value1, value2);
	}

	public Object mult(Object value1, Type value2Type, Object value2) {
		return mult(value1, value2);
	}

	public Object div(Object value1, Type value2Type, Object value2) {
		return div(value1, value2);
	}

	public Object mod(Object value1, Type value2Type, Object value2) {
		return null;
	}

	public Object shiftL(Object value1, Type value2Type, Object value2) {
		return null;
	}

	public Object shiftR(Object value1, Type value2Type, Object value2) {
		return null;
	}

	public Object shiftUR(Object value1, Type value2Type, Object value2) {
		return null;
	}

	public Type bracketType() {
		return null;
	}

	public Object bracket(Object array, int index) {
		return ((Object[]) array)[index];
	}

	public Object assignBracket(Object array, int index, Object value) {
		return null;
	}

	public Object assign(QEDProcess process, Call call, Object value) {
		return (null);
	}

    public Object lt(Object value1, Type value2Type, Object value2) {
		return null;
    }

    public Object gt(Object value1, Type value2Type, Object value2) {
		return null;
    }

    public Object ltEq(Object value1, Type value2Type, Object value2) {
		return null;
    }

    public Object gtEq(Object value1, Type value2Type, Object value2) {
		return null;
    }

    public Object eq(Object value1, Type value2Type, Object value2) {
		return null;
    }

    public Object neq(Object value1, Type value2Type, Object value2) {
		return null;
    }

	public Object add(Object value1, Object value2) {
		return null;
	}

	public Object subtract(Object value1, Object value2) {
		return null;
	}

	public Object mult(Object value1, Object value2) {
		return null;
	}

	public Object div(Object value1, Object value2) {
		return null;
	}

    public Object lt(Object value1, Object value2) {
		return null;
    }

    public Object gt(Object value1, Object value2) {
		return null;
    }

    public Object ltEq(Object value1, Object value2) {
		return null;
    }

    public Object gtEq(Object value1, Object value2) {
		return null;
    }

    public Object eq(Object value1, Object value2) {
		return null;
    }

    public Object neq(Object value1, Object value2) {
		return null;
    }

    public Object and(Object value1, Object value2) {
		return null;
    }

    public Object or(Object value1, Object value2) {
		return null;
    }

    public Object xor(Object value1, Object value2) {
		return null;
    }

	public int getWidth(QEDProcess process, Object value, int fontSize) {
		return (process.gettextsize(value.toString(), fontSize));
	}

	public int getHeight(QEDProcess process, Object value, int fontSize) {
		return (process.gettextheight(value.toString(), fontSize));
	}

	public void render(Env env, LayoutContextSet layoutContextSet, Path path, Object ctx, int[] pos, int[] size, int[] unitpos, Object value) {
		int textcol = layoutContextSet.getValueInt(env, path, ContentNode.MODIFtextcol);
		int fontSize = layoutContextSet.getValueInt(env, path, ContentNode.MODIFfontsize);

		((QEDProcess) ctx).drawText(value.toString(), pos, size, unitpos, fontSize, textcol);
	}

	public Object newDefault() {
		return null;
	}

	public Exp cast(Exp exp, DecEnv env) {
		return opCode == exp.getReturnType(env).opCode ? exp : new BinaryExp(Type.castFn, new Exp(this), exp);
	}

	public void writeS(OutputStream out, short num) throws IOException {
		write(out, (num >> 8) & 0xFF);
		write(out, num & 0xFF);
	}

	public void writeI(OutputStream out, int num) throws IOException {
		write(out, (num >> 24) & 0xFF);
		write(out, (num >> 16) & 0xFF);
		write(out, (num >> 8) & 0xFF);
		write(out, num & 0xFF);
	}

	public void writeL(OutputStream out, long num) throws IOException {
		write(out, (int) ((num >> 56) & 0xFF));
		write(out, (int) ((num >> 48) & 0xFF));
		write(out, (int) ((num >> 40) & 0xFF));
		write(out, (int) ((num >> 32) & 0xFF));
		write(out, (int) ((num >> 24) & 0xFF));
		write(out, (int) ((num >> 16) & 0xFF));
		write(out, (int) ((num >> 8) & 0xFF));
		write(out, (int) (num & 0xFF));
	}

	public String toString(Exp exp) {
		return null;
	}

    public static final IntType intType = new IntType();
    public static final ArrayType arrayType = new ArrayType();
    public static final InitArrayValueFn initArrayValueFn = new InitArrayValueFn();
    public static final StringType stringType = new StringType();
    public static final ImageType imageType = new ImageType();
    public static final BooleanType booleanType = new BooleanType();
    public static final LongType longType = new LongType();
    public static final VoidType voidType = new VoidType();
    public static final FloatType floatType = new FloatType();
    public static final CharType charType = new CharType();
    public static final ByteType byteType = new ByteType();
    public static final DoubleType doubleType = new DoubleType();
    public static final ShortType shortType = new ShortType();
    public static final ReturnFn returnFn = new ReturnFn();
//    public static final Return0Type ret0Type = new Return0Type();
    public static final ModuleType moduleType = new ModuleType();
    public static final JumpFn jumpFn = new JumpFn();
    public static final PushObjectFn pushObjectFn = new PushObjectFn();
    public static final CleanObjectFn cleanObjectFn = new CleanObjectFn();
    public static final HaltFn haltFn = new HaltFn();
    public static final PushFn pushFn = new PushFn();
    public static final IncStackFn incStackFn = new IncStackFn();
    public static final TestStackFn testStackFn = new TestStackFn();
    public static final WhileStackFn whileStackFn = new WhileStackFn();
    public static final IfStackFn ifStackFn = new IfStackFn();
    public static final SaveStackFn saveStackFn = new SaveStackFn();
    public static final AllocateStackFn allocateStackFn = new AllocateStackFn();
    public static final PopFn popFn = new PopFn();
    public static final PushLocalStackFn pushLocalStackFn = new PushLocalStackFn();
    public static final PopLocalStackFn popLocalStackFn = new PopLocalStackFn();
    public static final AllocateFn allocateFn = new AllocateFn();
    public static final NotFn notFn = new NotFn();
    public static final CompFn compFn = new CompFn();
    public static final CastFn castFn = new CastFn();
    public static final VarIndexFn varIndexFn = new VarIndexFn();
    public static final VarIndexFn2 varIndexFn2 = new VarIndexFn2();
    public static final RefType2 refFn2 = new RefType2();
    public static final DotFn dotFn = new DotFn();
    public static final FieldFn fieldFn = new FieldFn();
    public static final CallFn callFn = new CallFn();
    public static final ArrayFieldFn arrayFieldFn = new ArrayFieldFn();
    public static final VirtualFuncFn virtualFuncFn = new VirtualFuncFn();
    public static final AddFn addFn = new AddFn();
    public static final SubtractFn subtractFn = new SubtractFn();
    public static final MultFn multFn = new MultFn();
    public static final DivFn divFn = new DivFn();
    public static final ModFn modFn = new ModFn();
    public static final ShiftLFn shiftLFn = new ShiftLFn();
    public static final ShiftRFn shiftRFn = new ShiftRFn();
    public static final ShiftURFn shiftURFn = new ShiftURFn();
    public static final LtFn ltFn = new LtFn();
    public static final GtFn gtFn = new GtFn();
    public static final LtEqFn ltEqFn = new LtEqFn();
    public static final GtEqFn gtEqFn = new GtEqFn();
    public static final EqFn eqFn = new EqFn();
    public static final NEqFn neqFn = new NEqFn();
    public static final AndFn andFn = new AndFn();
    public static final OrFn orFn = new OrFn();
    public static final XorFn xorFn = new XorFn();
    public static final CAndFn cAndFn = new CAndFn();
    public static final COrFn cOrFn = new COrFn();
    public static final NewFn newFn = new NewFn();
    public static final TernaryFn ternaryFn = new TernaryFn();
    public static final ArrowFn arrowFn = new ArrowFn();
    public static final AssignmentFn assignmentFn = new AssignmentFn();
    public static final ParmType parmType = new ParmType();
    public static final ObjType objType = new ObjType();
    public static final IfType ifType = new IfType();
    public static final ElseType elseType = new ElseType();
    public static final SwitchType switchType = new SwitchType();
    public static final CaseType caseType = new CaseType();
    public static final DefaultType defaultType = new DefaultType();
    public static final PrintType printType = new PrintType();
    public static final PrintLnType printLnType = new PrintLnType();
    public static final RefreshType refreshType = new RefreshType();
    public static final RectType rectType = new RectType();
    public static final RoundRectType roundRectType = new RoundRectType();
    public static final OvalType ovalType = new OvalType();
    public static final LineType lineType = new LineType();
    public static final TimerType timerType = new TimerType();
    public static final ArraySizeType arraySizeType = new ArraySizeType();
    public static final ArraySetType arraySetType = new ArraySetType();
    public static final ArrayAddType arrayAddType = new ArrayAddType();
    public static final ArrayRemoveType arrayRemoveType = new ArrayRemoveType();
    public static final ArrayClearType arrayClearType = new ArrayClearType();
    public static final DevNullType devNullType = new DevNullType();
    public static final PathType pathType = new PathType();
    public static final LValueFn lValueFn = new LValueFn();
    public static final ThisLValueFn thisLValueFn = new ThisLValueFn();
    public static final ResetTimerType resetTimerType = new ResetTimerType();
    public static final LoadImageFn loadImageFn = new LoadImageFn();
    public static final CoListType coListType = new CoListType();
    public static final YieldCoListType yieldCoListType = new YieldCoListType();
    public static final ProcessCoListType processCoListType = new ProcessCoListType();
    public static final AddCoListType addCoListType = new AddCoListType();

    @SuppressWarnings("serial")
	public static class FnList extends ArrayList<Type> {
    	public FnList() {
    		// predefined pure types
    		add(arrayType);
    		add(imageType);
    		add(voidType);
    		add(booleanType);
    		add(byteType);
    		add(charType);
    		add(shortType);
    		add(intType);
    		add(longType);
    		add(floatType);
    		add(doubleType);
    		add(stringType);
    		add(ifType);  // predefined pure functions
    		add(elseType);
    		add(switchType);
    		add(caseType);
    		add(defaultType);
    		add(returnFn);
    		add(printType);
    		add(printLnType);
    		add(rectType);
    		add(roundRectType);
    		add(ovalType);
    		add(lineType);
    		add(arraySizeType);
    		add(arraySetType);
    		add(arrayAddType);
    		add(arrayRemoveType);
    		add(arrayClearType);
    		add(moduleType); // Internal functions
    		add(jumpFn);
    		add(pushObjectFn);
    		add(cleanObjectFn);
    		add(haltFn);
    		add(pushFn);
    		add(incStackFn);
    		add(testStackFn);
    		add(whileStackFn);
    		add(ifStackFn);
    		add(saveStackFn);
    		add(allocateStackFn);
    		add(popFn);
    		add(pushLocalStackFn);
    		add(popLocalStackFn);
    		add(allocateFn);
    		add(initArrayValueFn);
    		add(notFn);
    		add(compFn);
    		add(castFn);
    		add(varIndexFn);
    		add(varIndexFn2);
    		add(refFn2);
    		add(dotFn);
    		add(fieldFn);
    		add(callFn);
    		add(arrayFieldFn);
    		add(virtualFuncFn);
    		add(addFn);
    		add(subtractFn);
    		add(multFn);
    		add(divFn);
    		add(modFn);
    		add(shiftLFn);
    		add(shiftRFn);
    		add(shiftURFn);
    		add(ltFn);
    		add(gtFn);
    		add(ltEqFn);
    		add(gtEqFn);
    		add(eqFn);
    		add(neqFn);
    		add(andFn);
    		add(orFn);
    		add(xorFn);
    		add(cAndFn);
    		add(cOrFn);
    		add(newFn);
    		add(arrowFn);
    		add(assignmentFn);
    		add(parmType);
    		add(objType);
    		add(refreshType);
    		add(timerType);
    		add(devNullType);
    		add(pathType);
    		add(lValueFn);
    		add(thisLValueFn);
    		add(resetTimerType);
    		add(loadImageFn);
    		add(coListType);
    		add(yieldCoListType);
    		add(processCoListType);
    		add(addCoListType);
    	}

    	public boolean add(Type type) {
    		if (type != null)
    			type.opCode = (byte) size();
    		return super.add(type);
    	}
    };

    @SuppressWarnings("serial")
	public static class PredefinedTypeList extends HashMap<String, Byte> {
    	public PredefinedTypeList() {
    		put("int", intType.opCode);
    		put("string", stringType.opCode);
    		put("image", imageType.opCode);
    		put("boolean", booleanType.opCode);
    		put("long", longType.opCode);
    		put("void", voidType.opCode);
    		put("float", floatType.opCode);
    		put("char", charType.opCode);
    		put("byte", byteType.opCode);
    		put("double", doubleType.opCode);
    		put("short", shortType.opCode);
    		put("return", returnFn.opCode);
    		put("halt", haltFn.opCode);
    		put("parm", parmType.opCode);
    		put("obj", objType.opCode);
    		put("if", ifType.opCode);
    		put("else", elseType.opCode);
    		put("switch", switchType.opCode);
    		put("case", caseType.opCode);
    		put("default", defaultType.opCode);
    		put("print", printType.opCode);
    		put("println", printLnType.opCode);
    		put("refresh", refreshType.opCode);
    		put("rect", rectType.opCode);
    		put("roundrect", roundRectType.opCode);
    		put("oval", ovalType.opCode);
    		put("line", lineType.opCode);
    		put("Timer", timerType.opCode);
    		put("loadImage", loadImageFn.opCode);
    		put("CoList", coListType.opCode);
    	}
    };

    public static FnList fnList = new FnList();
    public static PredefinedTypeList predefinedTypes = new PredefinedTypeList();
//    public static byte intOpCode = intType.opCode;

	public static Type getType(String name) {
		Byte opCode = predefinedTypes.get(name);

		return opCode != null ? fnList.get(opCode) : null;
    }

	public static PredefinedType getFunc(String name, List<Exp> args) {
		Byte opCode = predefinedTypes.get(name);

		return opCode != null ? (PredefinedType) fnList.get(opCode) : null;
	}

	public static class UIContext {
		public LayoutContextSet layoutContextSet;
		public int[] pos;
		public int[] size;
		public Object ctx;

		public UIContext(LayoutContextSet layoutContextSet, int[] pos, int[] size, Object ctx) {
			this.layoutContextSet = layoutContextSet;
			this.pos = pos;
			this.size = size;
			this.ctx = ctx;
		}
    }
}
