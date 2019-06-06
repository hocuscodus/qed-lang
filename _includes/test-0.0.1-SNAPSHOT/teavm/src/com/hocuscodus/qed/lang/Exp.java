/*
 *    Copyright (C) 2016 Hocus Codus Software inc.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.hocuscodus.qed.lang.Declaration.DecEnv;
import com.hocuscodus.qed.lang.Declaration.FunctionDeclaration;
import com.hocuscodus.qed.lang.type.ArrayType;
import com.hocuscodus.qed.lang.type.Type;


public class Exp {
	public Type type;

	public Exp(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

    public Type getReturnType(DecEnv env) {
        return type.getReturnType(this, env);
    }

	public boolean makerefs(Path path) {
		return type.makerefs(this, path);
	}

	public void write(DecEnv env, Path path, OutputStream out) throws IOException {
		type.writeExp(this, env, path, out);
	}

	public void writeAssignment(DecEnv env, Path path, OutputStream out) throws IOException {
		type.writeExpAssignment(this, env, path, out);
	}

	public Exp getArrayFieldExp(Exp indexExp) {
		return findArrayFieldExp(indexExp) ? this : new BinaryExp(Type.arrayFieldFn, this, indexExp);
	}

	public boolean findArrayFieldExp(Exp indexExp) {
		return type.findArrayFieldExp(this, indexExp);
	}

	public Exp findVarPath(DecEnv env, String field, List<Exp> args, boolean virtualFlag) {
		Exp dotExp = new UnaryExp(Type.dotFn, this);

		return dotExp.getReturnType(env).findVarPath(dotExp, field, args, virtualFlag);
	}

	public boolean getVirtualFlag(String name) {
		return type.getVirtualFlag(this, name);
	}

	public String toString() {
		return type.toString(this);
	}

    public static class ValueExp extends Exp {
    	public Object value;

    	public ValueExp(Type type, Object value) {
    		super(type);
    		this.value = value;
    	}

        public Type getReturnType() {
            return type;
        }

        public Type getReturnType(FunctionDeclaration function) {
            return type;
        }

        public Type getReturnType(DecEnv env) {
            return type;
        }

    	public boolean isIntegral() {
    		return type.isIntegral();
    	}

    	public long getInteger() {
    		return type.getInteger(value);
    	}

    	public Object getValue() {
    		return value;
    	}
    }

    public static class UnaryExp extends Exp {
    	public Exp exp;

    	public UnaryExp(Type type, Exp exp) {
    		super(type);
    		this.exp = exp;
    	}
    }

    public static class FuncExp extends UnaryExp {
		public List<Exp> args;

		public FuncExp(Type type, Exp thisExp, List<Exp> args) {
			super(type, thisExp);
			this.args = args;
		}
	}

    public static class RefExp extends Exp {
		public int level;

		public RefExp(int level) {
			super(Type.refFn2);
			this.level = level;
		}
	}

    public static class LValueExp extends UnaryExp {
		public Path path;

		public LValueExp(Type type, Exp exp, Path path) {
			super(type, exp);
			this.path = path;
		}
	}

    public static class VirtualFuncExp extends FuncExp {
    	public int fnIndex;

		public VirtualFuncExp(Exp thisExp, List<Exp> args, int fnIndex) {
			super(Type.virtualFuncFn, thisExp, args);
			this.fnIndex = fnIndex;
		}
	}

    public static class BinaryExp extends Exp {
    	public Exp value1;
    	public Exp value2;

    	public BinaryExp(Type type, Exp value1, Exp value2) {
    		super(type);
    		this.value1 = value1;
    		this.value2 = value2;
    	}
    }

    public static class TernaryExp extends Exp {
    	public Exp value1;
    	public Exp value2;
    	public Exp value3;

    	public TernaryExp(Type type, Exp value1, Exp value2, Exp value3) {
    		super(type);
    		this.value1 = value1;
    		this.value2 = value2;
    		this.value3 = value3;
    	}
    }

    public static class ListExp extends Exp {
		public List<Exp> list = new ArrayList<Exp>();

		public ListExp(Type type) {
			super(type);
		}

		public void addExp(Exp exp) {
			list.add(exp);
		}

		public int getNumExps() {
			return list.size();
		}

		public Exp getExp(int index) {
			return list.get(index);
		}
	}

    public static class ArrayExp extends ListExp {
		public ArrayExp(Type type) {
			super(new ArrayType(type));
		}

		public ArrayExp() {
			super(null);
		}

		public void addExp(Exp exp) {
			Type initType = Type.stringType;//exp.getReturnType();

			super.addExp(exp);

			if (/*type == null && */initType != null)
				type = new ArrayType(initType);
		}
	}
}
