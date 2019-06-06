/*
 *    Copyright (C) 2016 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.node;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.Context;
import com.hocuscodus.qed.lang.Declaration.DecEnv;
import com.hocuscodus.qed.lang.Declaration.FunctionDeclaration;
import com.hocuscodus.qed.lang.Env;
import com.hocuscodus.qed.lang.ExecReturnHandler;
import com.hocuscodus.qed.lang.Exp;
import com.hocuscodus.qed.lang.Instruction;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.QEDCall;
import com.hocuscodus.qed.lang.type.Type;

public class ContentNode extends Node {
    public static final int MODIFcontent = 0;
    public static final int MODIFunitcol = 1;
    public static final int MODIFtextcol = 2;
    public static final int MODIFexpwidth = 3;
    public static final int MODIFexpheight = 4;
    public static final int MODIFalignx = 5;
    public static final int MODIFaligny = 6;
    public static final int MODIFtextwidth = 7;
    public static final int MODIFtextheight = 8;
    public static final int MODIFviewx = 9;
    public static final int MODIFviewy = 10;
    public static final int MODIFcweight = 11;
    public static final int MODIFonpress = 12;
    public static final int MODIFonrelease = 13;
    public static final int MODIFonclick = 14;
    public static final int MODIFondrag = 15;
    public static final int MODIFonkeypressed = 16;
    public static final int MODIFonkeyreleased = 17;
    public static final int MODIFonkeytyped = 18;
    public static final int MODIFcspace3 = 19;
    public static final int MODIFaspace3 = 20;
    public static final int MODIFfontsize = 21;
    public static final int MODIFfontstroke = 22;
    public static final int MODIFend = 23;

    public static String[] modifStrings = new String[] {
    	"out", "bgcol", "textcol", "zoomwidth", "zoomheight", "alignx", "aligny", "textwidth", "textheight", "viewx", "viewy", "cweight",
    	"onpress", "onrelease", "onclick", "ondrag", "onkeypress", "onkeyrelease", "onchar", "cspac3e", "as3pace", "size", "stroke",
    	"zoommult", "zoomdiv", "font", "style", "weight", "dep"};

//    public Exp sizeExp;
    public Path sizePath;

    public int getId() {
    	return 0;
    }

	public Node setArrayPathUnit(FunctionDeclaration function, Instruction inst, Path path, int level) {
		boolean wasNull = arrayPathUnit == null;
		ContentNode node = (ContentNode) super.setArrayPathUnit(function, inst, path, level);

		if (node != null && wasNull) {
//			FieldDeclaration dec = function.topMtx.getPathDec(path, level);
//
			sizePath = function.topDataNode.getDataIndex(function.getVarPath2(path.trim(level)));
		}

		return (node);
	}

	public StorageUnit init(Env env) {
    	return ((StorageUnit) eval((Env) null, null));
    }

    public boolean unInit(Env env, StorageUnit storageUnit) {
      return (true);
    }

	public boolean parsesubinitunits(Env env) {
        boolean rc = true;

    	for (int index = 0; rc && index < getNumNodes(); index++) {
    		ContentNode node = (ContentNode) getNode(index);

    		env.currentUnit.childStorageUnits.storageUnits[index] = (StorageUnit) node.eval(env, null);
    		rc = env.currentUnit.childStorageUnits.storageUnits[index] != null && node.parsesubinitunits(new Env(env, index));
    	}

        if (rc && getArrayFlag()) {
        	int dsize = getdsize(env);

        	env.currentUnit.arrayStorageUnits = createStorageUnitList('+');
        	rc = env.currentUnit.arrayStorageUnits != null && initArray(env.currentUnit.arrayStorageUnits, dsize);

        	for (int dindex = 0; rc && dindex < dsize; dindex++) {
        		env.currentUnit.arrayStorageUnits.storageUnits[dindex] = (StorageUnit) ((ContentNode) arrayPathUnit).eval(env, null);
        		rc = env.currentUnit.arrayStorageUnits.storageUnits[dindex] != null && ((ContentNode) arrayPathUnit).parsesubinitunits(new Env(env, -dindex - 1));
        	}
        }

        return (rc);
    }

    public boolean parsesubuninitunits(Env env) {
        boolean rc = true;

        if (rc && getArrayFlag()) {
        	int dsize = getdsize(env);

        	for (int dindex = dsize - 1; rc && dindex >= 0; dindex--)
        		rc = env.currentUnit.arrayStorageUnits.storageUnits[dindex] != null && ((ContentNode) arrayPathUnit).parsesubuninitunits(new Env(env, -dindex - 1));

//        	uninitstoragesetarray(storagesetarray, dsize);
        }

    	for (int index = getNumNodes() - 1; rc && index >= 0; index--)
    		if (env.currentUnit.childStorageUnits.storageUnits[index] != null) {
    			rc = ((ContentNode) getNode(index)).parsesubuninitunits(new Env(env, index));
//    			uninitchildstorageset(childstoragesets.storagesets[index], index);
    		}

        return (rc);
    }

	public Object eval(Env env, Exp exp) {
		StorageUnit storageUnit = createStorageUnit(env);

		if (storageUnit != null && getNumNodes() != 0) {
			storageUnit.childStorageUnits = createChildStorageUnits();

			if (storageUnit.childStorageUnits == null || !initArray(storageUnit.childStorageUnits, getNumNodes()))
				storageUnit = null;
		}

		return (storageUnit);
    }

	public StorageUnit createStorageUnit(Env env) {
    	return new StorageUnit();
    }

    public StorageUnitList createStorageUnitList(char strChar) {
    	return new StorageUnitList(strChar);
    }

    public StorageUnitList createChildStorageUnits() {
    	return new StorageUnitList('|');
    }

	public int getdsize(Env env) {
		int size = -1;
		int count = 0;
		Object value = arrayPathUnit != null ? env.obj.data : null;

		for (int index = 0; index < (arrayPathUnit != null ? sizePath.getNumLevels() : 0); index++) {
			int ndx = sizePath.get(index);

			if (ndx >= 0)
				value = ((Object[]) value)[ndx];
			else
				while(count < env.path.getNumLevels())
					if (env.path.get(count++) < 0) {
						value = ((Object[]) value)[-env.path.get(count - 1) - 1];
						break;
					}
		}

		if (value != null)
			if (value instanceof Integer)
				size = (Integer) value;
			else
				if (value instanceof Boolean)
					size = ((Boolean) value) ? 1 : 0;
				else
					// Comment the following line to make it work with TeaVM
//					if (value instanceof Object[])
					size = ((Object[]) value).length;

		return size;
	}

    public Node retrieveNode(FunctionDeclaration function, Object unit, Instruction topInst, Path path, Instruction inst) {
    	ContentNode node = (ContentNode) super.retrieveNode(function, unit, topInst, path, inst);

    	if (node != null)
			node.sizePath = function.topDataNode.getDataIndex(path);

//    	Commandsetcontent contents = node != null && node.getClass().getEnclosingClass() == null ? node.getContentNode() : null;
//
//		if (contents != null)
//			for (int index = 0; index < contents.getnumcontents(); index++)
//				if (contents.getcontent(index) != null)
//					contents.getcontent(index).makerefs(path);
//
		return node;
    }

    @Override
	public Node createNode(FunctionDeclaration function, Path path, int level, Instruction inst) {
    	return new ContentNode();
    }

	public void buildContentTree(Context context, Instruction topInst, Instruction inst, Path path, ByteArrayOutputStream out) {
		if (inst.getNumAttrs() != 0) {
			Path newPath = new Path(path);
			DecEnv env = context.getCtx().declaration.buildDecEnv(path);
			Commandsetcontent content = new Commandsetcontent(inst.getNumAttrs()/*, inst.labeldir*/);

	        for (int index = 0; index < inst.getNumAttrs(); index++) {
	        	int pointer = out != null ? out.size() : -1;

	        	if (out != null)
	        		try {
	        			inst.getAttr(index).getExp().write(env, path, out);
	        			new Exp(Type.haltFn).write(env, path, out);
	        		} catch (IOException e) {
	        		}

        		if ("fgcol".equals(inst.getAttr(index).getProp())) {
    				content.contents[2] = pointer;
        		}
        		else
	        	for (int ndx = 0; ndx < content.contents.length; ndx++)
	        		if (modifStrings[ndx].equals(inst.getAttr(index).getProp())) {
	    				content.contents[ndx] = pointer;

	        			if (ndx == 0)
	        				content.type = inst.getAttr(index).getExp().getReturnType(env);

	        			break;
	        		}
	        }

	        buildPath(newPath, inst);
	        retrieveNode(context.getCtx().declaration, content, topInst, newPath, inst);
		}

		for (int index = 0; index < inst.getNumNodes(); index++)
			buildContentTree(context, topInst, inst.getNode(index), path.concat(index), out);
	}

	public void buildContentTree(Context context, Instruction topInst, Instruction inst, ContentNode contentNode, Path path) {
		if (contentNode.getArrayFlag())
			buildContentTree(context, topInst, inst, (ContentNode) contentNode.getArrayPathUnit(), path.concat(-1));

		for (int index = 0; index < contentNode.getNumNodes(); index++) {
			ContentNode subNode = (ContentNode) contentNode.getNode(index);

			buildContentTree(context, topInst, inst.getNode(subNode.index), subNode, path.concat(subNode.index));
		}
	}

	protected boolean buildPath(Path newPath, Instruction inst) {
		boolean rc = true;

        for (int index = 0; rc && index < inst.getNumAttrs(); index++)
        	rc = buildPath(newPath, inst.getAttr(index).path);

		return (rc);
	}

	public int getPointer(int index) {
    	Commandsetcontent contentset = getContentNode();

    	return contentset != null ? contentset.getcontent(index) : 0;
	}

	public Object execcmd(Env env, Path path, int index) {
    	int pointer = getPointer(index);
    	Call call = pointer != 0 ? new QEDCall(env.obj, pointer, sizePath, path, ExecReturnHandler.obj, null) : null;

    	if (call != null)
    		env.process.evalThread(call);

    	return call != null && call.stack.size() != 0 ? call.stack.pop() : null;
    }

    public int getValueInt(Env env, Path path, int index) {
    	return getValueInt(env, path, index, -1);
    }

    public int getValueInt(Env env, Path path, int index, int defaultValue) {
    	Object val = execcmd(env, path, index);

        return (val != null && val instanceof Integer ? (Integer) val : val != null && val instanceof String ? Integer.parseInt((String) val) : defaultValue);
    }

	public Commandsetcontent getContentNode() {
		return (Commandsetcontent) unit;
	}

	public int getcontent(int index) {
		return (unit != null ? getContentNode().getcontent(index) : 0);
	}

    public StorageUnit getField(StorageUnit storageUnit, Path path) {
    	StorageUnit value = path.getNumLevels() != 0 ? null : storageUnit;

    	if (value == null && path.get(0) >= 0) {
    		int ndx = getcontentndx(path.get(0));

    		if (ndx >= 0)//arrayNode.getnumcxsets() != 0)
    			value = ((ContentNode) getNode(ndx)).getField(storageUnit.childStorageUnits.storageUnits[ndx], path.trim(0, 1));
    	}

    	if (value == null && path.get(0) < 0) {
    		int index = -path.get(0) - 1;
    		StorageUnitList storageUnitList = ((StorageUnit) storageUnit).arrayStorageUnits;

    		if (index >= 0 && index < storageUnitList.storageUnits.length)
    			value = ((ContentNode) arrayPathUnit).getField(storageUnitList.storageUnits[index], path.trim(0, 1));
    		else
    			Node.log3("Array index out of range: " + index); // error : out of range
    	}

    	return (value);
    }

    public StorageUnit getField(StorageUnit storageUnit, Path path, Path dpath) {
    	int count = 0;
    	StorageUnit value = path.getNumLevels() != 0 ? null : storageUnit;

    	if (value == null && path.get(0) >= 0) {
    		int ndx = getcontentndx(path.get(0));

    		if (ndx >= 0)//arrayNode.getnumcxsets() != 0)
    			value = ((ContentNode) getNode(ndx)).getField(storageUnit.childStorageUnits.storageUnits[ndx], path.trim(0, 1), dpath);
    	}

    	if (value == null && path.get(0) < 0) {
    		while (dpath.get(count++) >= 0);
    		int index = -dpath.get(count - 1) - 1;
    		StorageUnitList storageUnitList = ((StorageUnit) storageUnit).arrayStorageUnits;

    		if (index >= 0 && index < storageUnitList.storageUnits.length)
    			value = ((ContentNode) arrayPathUnit).getField(storageUnitList.storageUnits[index], path.trim(0, 1), dpath.trim(0, count));
    		else
    			Node.log3("Array index out of range: " + index); // error : out of range
    	}

    	return (value);
    }

    public boolean initArray(StorageUnitList arrayStorageUnits, int dsize) {
    	arrayStorageUnits.storageUnits = dsize != 0 ? new StorageUnit[dsize] : null;

    	return (dsize == 0 || arrayStorageUnits.storageUnits != null);
    }

    public static class StorageUnit {
        public StorageUnitList arrayStorageUnits;
        public StorageUnitList childStorageUnits;

        @Override
		public String toString() {
        	String str = /*childStorageUnits != null || */arrayStorageUnits != null ? level + "\\ A: " + "\n" : "";
        	String oldLevel = level;

        	level += "+";

        	if (arrayStorageUnits != null)
        		str = str + arrayStorageUnits;// + "\n";

        	level = oldLevel;
            str += /*childStorageUnits != null || */"";
//          String oldLevel = level;
//
//          level += "+";

            if (childStorageUnits != null/* || arrayStorageUnits != null*/)
            	str = str + level + "\\ S: \n" + childStorageUnits;

//          level = oldLevel + "|";
//
//          for (int index = 0; index < (childStorageUnits != null ? childStorageUnits.storageUnits.length : 0); index++)
//              str = str + childStorageUnits.storageUnits[index] + level + "\n";

//          level = oldLevel;

            return (str);
        }
    }

    public static class StorageUnitList {
        public StorageUnit[] storageUnits;
        public char strChar;

        static String level = "";

        public StorageUnitList(char strChar) {
        	this.strChar = strChar;
        }

        @Override
		public String toString() {
            String str = "";//level + "\\ M:\n";
            String oldLevel = level;

            level += strChar;

            for (int index = 0; index < (storageUnits != null ? storageUnits.length : 0); index++)
                str = str + storageUnits[index];

            level = oldLevel;

            return (str);
        }
    }

    public static class Commandsetcontent {
        public int[] contents;
        public Type type;

        public Commandsetcontent(int length) {
            contents = new int[MODIFend];
        }

        public int getnumcontents() {
            return (contents.length);
        }

        public int getcontent(int index) {
            return (index >= 0 && index < getnumcontents() ? contents[index] : 0);
        }

        public String toString() {
        	String str = "";

        	for (int index = 0; index < getnumcontents(); index++)
        		if (contents[index] != -1)
        			str = (str.length() == 0 ? "" : str + " ") + contents[index];

    		return str;
        }
    }
}
