/*
 *    Copyright (C) 2016 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.node;

import java.util.ArrayList;

import com.hocuscodus.qed.lang.Declaration.FunctionDeclaration;
import com.hocuscodus.qed.lang.Instruction;
import com.hocuscodus.qed.lang.Path;

public class Node {
	public Object unit;
	public int index = -1;
	public ArrayList<Node> childNodes = null;
	public Node arrayPathUnit = null;

    public Node retrieveNode(FunctionDeclaration function, Object unit, Instruction topInst, Path path, Instruction inst) {
    	Node node = addpath(function, topInst, path, 0);

		if (node != null)
			node.unit = unit;

		return node;
    }

    public Node addpath(FunctionDeclaration function, Instruction inst, Path path, int level) {
    	boolean subFlag = level < path.getNumLevels();
    	Instruction subInst = inst != null && subFlag ? path.get(level) >= 0 ? inst.getNode(path.get(level)) : inst : null;

    	return (subFlag ? (path.get(level) < 0 ? setArrayPathUnit(function, inst, path, level) : addNode(function, path, level, subInst)).addpath(function, subInst, path, level + 1) : this);
    }

	public Node addNode(FunctionDeclaration function, Path path, int level, Instruction inst) {
		int index = path.get(level);
        int ndx = find(index, true);
        Node node = ndx < getNumNodes() ? getNode(ndx) : null;

        if (node == null || node.index != index) {
			node = createNode(function, path, level, inst);

			if (node != null) {
				node.index = index;

				if (childNodes == null)
					childNodes = new ArrayList<Node>();

				node = childNodes.add(node) ? node : null;
			}
        }

        return (node);
    }

	public int getNumNodes() {
		return (childNodes != null ? childNodes.size() : 0);
	}

	public Node getNode(int index) {
		return (childNodes.get(index));
	}

    public Node createNode(FunctionDeclaration function, Path path, int level, Instruction inst) {
        return (new Node());
    }

	public boolean getUnitFlag() {
		return (unit != null);
	}

	public int getcontentindex(int index) {
		return (getNode(index).index);
	}

	public int getcontentndx(int index) {
		return find(index, false);
	}

    private int find(int index, boolean locationflag) {
        int remainder;
        int comp = 1;
        int start = 0;
        int limit = getNumNodes();

        while (limit != 0 && comp != 0) {
            remainder = limit & 1;
            limit >>= 1;
            comp = index - getcontentindex(start + limit);

            if (comp > 0) {
                start += limit + 1L;

                if (remainder == 0)
                    limit--;
            }
        }

        return (locationflag || comp == 0 ? start + limit : -1);
    }

	public Node getArrayPathUnit() {
		return (arrayPathUnit);
	}

	public Node setArrayPathUnit(FunctionDeclaration function, Instruction inst, Path path, int level) {
		if (arrayPathUnit == null)
			arrayPathUnit = createArrayNode(function, path, level, inst);

		return (arrayPathUnit);
	}

    public Node createArrayNode(FunctionDeclaration function, Path path, int level, Instruction inst) {
        return (createNode(function, path, level, inst));
    }

	public boolean getArrayFlag() {
		return (getArrayPathUnit() != null);
	}

	static String level = "";
	static String header = "M";
	@Override
	public String toString() {
		String str = level + header + ": " + getheader() + "\n";
		String oldLevel = level;

		level += "|";
		str = str + getbody();
		level = oldLevel;

		return (str);
	}

	public String getheader() {
		return (("L:" + (getUnitFlag() ? 1 : 0)) + " M:" + (getArrayFlag() ? 1 : 0) + " " + unit);
	}

	public String getbody() {
		String str = "";

		for (int index = 0; index < getNumNodes(); index++) {
			String oldHeader = pushHeader("S(" + getNode(index).index + ")");
			str = str + getNode(index);
			popHeader(oldHeader);
		}

		String oldHeader = pushHeader("V");

		if (getArrayPathUnit() != null)
			str = str + getArrayPathUnit();

		popHeader(oldHeader);

		return (str);
	}

	public String pushHeader(String newHeader) {
		String oldHeader = header;

		header = newHeader;

		return (oldHeader);
	}

	public void popHeader(String oldHeader) {
		header = oldHeader;
	}

	static int logcount = 1;

    public static void log3(String info) {
    	//System.out.println("" + logcount++ + ": " + info);
	}

	public static Path stripPath(Path newPath) {
		Path path = new Path();

		for (int index = 0; index < newPath.getNumLevels(); index++)
			if (newPath.get(index) >= 0)
				path = path.concat(newPath.get(index));

		return path;
	}

	public static boolean buildPath(Path newPath, Path expPath) {
		return buildPath(newPath, expPath, -1);
	}

	protected static boolean buildPath(Path newPath, Path expPath, int stuff) {
		int count = 0;
		boolean rc = true;

		for (int index = 0; rc && index < expPath.getNumLevels(); index++)
			if (expPath.get(index) < 0)
				if (count < newPath.getNumLevels() && newPath.get(count) < 0)
					count++;
				else
					newPath.set(newPath.trim(count).concat(stuff).concat(newPath.trim(0, count++)));
			else {
				while (count < newPath.getNumLevels() && newPath.get(count) < 0)
					count++;

				rc = count < newPath.getNumLevels() && newPath.get(count++) == expPath.get(index);
			}

		return (rc);
	}
}
