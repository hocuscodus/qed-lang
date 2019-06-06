/*
 *    Copyright (C) 2016 Hocus Codus Software inc.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang;

import java.util.ArrayList;
import java.util.List;

import com.hocuscodus.qed.lang.AttrSet.Attr;
import com.hocuscodus.qed.lang.Exp.ValueExp;

public class Instruction {
	public boolean setflag;
	public int childdir;
	public int mtxdir;
	public int cpack;
	public int apack;
	public int cweight;
	public int cspace;
	public int aspace;
	public AttrSet atts;
	public List<Instruction> block;
	public static int indent = 0;

	public Instruction(List<Instruction> block, AttrSet atts) {
		this.block = block != null ? block : new ArrayList<Instruction>();

		if (atts != null) {
			String[] dirlabels = {"cdir", "adir", "cpack", "apack", "cweight", "cspace", "aspace"};
			String[] dirlabels2 = {"childdir", "mtxdir"};
			int dirs[] = new int[dirlabels.length];
			boolean rc = true;

			dirs[2] = -1;
			dirs[3] = -1;

			for (int index = 0; rc && index < atts.getNumAttrs(); index++) {
				Attr attr = atts.getAttr(index);

				for (int ndx = 0; ndx < dirlabels.length; ndx++)
					if (attr.getProp().equals(dirlabels[ndx])) {
						dirs[ndx] = (Integer) ((ValueExp) attr.getExp()).value;
						rc = atts.removeAttr(index--);
						break;
					}

				for (int ndx = 0; ndx < dirlabels2.length; ndx++)
					if (attr.getProp().equals(dirlabels2[ndx])) {
						dirs[ndx] = (Integer) ((ValueExp) attr.getExp()).value;
						rc = atts.removeAttr(index--);
						break;
					}
			}

			childdir = dirs[0];
			mtxdir = dirs[1];
			cpack = dirs[2];
			apack = dirs[3];
			cweight = dirs[4];
			cspace = dirs[5];
			aspace = dirs[6];

			int count = 0;

			for (int index = 0; count <= 1 && index < getNumNodes(); index++)
				count += getNode(index).getSizeFlag() ? 1 : 0;

			setflag = count > 1;
			this.atts = atts.getNumAttrs() != 0 ? atts : null;
		}
	}

	public int findAttr(String prop) {
        return atts.findattr(prop, false);
    }

    public int getNumAttrs() {
        return atts != null ? atts.getNumAttrs() : 0;
    }

    public Attr getAttr(int index) {
        return (atts.getAttr(index));
    }

    private boolean getSizeFlag() {
    	boolean rc = atts != null;

		for (int index = 0; !rc && index < getNumNodes(); index++)
			rc = getNode(index).getSizeFlag();

		return rc;
	}

	public int getNumNodes() {
        return block != null ? block.size() : 0;
    }

    public Instruction getNode(int index){
        return block.get(index);
    }

    public String getdecstr() {
		return "";
	}

    public String toString() {
    	String header = getdecstr();
		String str = new String();

		for (int index = 0; index < indent; index++)
			str += "\t";

		str += header;

		if (header.length() != 0)
			str += " ";

		for (int index = 0; index < getNumAttrs(); index++)
			str += (index == 0 ? "" : " ") + "@" + atts.getAttr(index).getProp() + "(" + atts.getAttr(index).getExp() + ")";

		if (block.size() == 0)
			str += ";\n";
		else {
			str += "{\n";
			indent++;
			for (int index = 0; index < block.size(); index++) {
				str += block.get(index);
			}
			indent--;
			for (int index = 0; index < indent; index++)
				str += "\t";
			str += "}\n";
		}

		return str;
	}
}
