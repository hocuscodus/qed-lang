/*
 *    Copyright (C) 2016 Hocus Codus Software inc.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.node;

import java.util.List;

import com.hocuscodus.qed.lang.Declaration;
import com.hocuscodus.qed.lang.Exp;
import com.hocuscodus.qed.lang.Exp.RefExp;
import com.hocuscodus.qed.lang.Instruction;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.Declaration.FunctionDeclaration;

public class DataNode extends Node {
    @Override
	public Node createNode(FunctionDeclaration function, Path path, int level, Instruction inst) {
    	return new DataNode();
    }
//
//	public void buildDataTree(Context context, CodeNode codeNode, Path path) {
//		if (codeNode.hasData())
//	        retrieveNode(context.getCtx().topMtx, codeNode, null, path, null);
//
//		for (int index = 0; index < codeNode.getNumNodes(); index++) {
//			CodeNode subNode = (CodeNode) codeNode.getNode(index);
//
//			buildDataTree(context, subNode, path.concat(subNode.index));
//		}
//
//		if (codeNode.getArrayFlag())
//			buildDataTree(context, (CodeNode) codeNode.getArrayPathUnit(), path.concat(-1));
//	}

	public int getSize() {
		int size = unit != null ? 1 : 0;

		for (int index = 0; index < getNumNodes(); index++)
			size += getDataNode(index).getSize();

		return size + (getArrayDataNode() != null ? 1 : 0);
	}
//
//	public int getSize(Path path) {
//		return ((DataNode) getNode(path)).size();
//	}

	public Path getFlatPath(Path path) {
		Path outPath = new Path(0);

		getFlatIndex(path, outPath);

		return outPath;
	}

	public void getFlatIndex(Path path, Path outPath) {
		int numdim = outPath.getNumLevels() - 1;

		outPath.set(numdim, outPath.get(numdim) + (unit != null ? 1 : 0));

		if (path.getNumLevels() != 0) {
			int ndx = path.get(0);
			int size = ndx >= 0 ? getcontentndx(ndx) : getNumNodes();

			if (size >= 0) {
				for (int ndx2 = 0; ndx2 < size; ndx2++)
					outPath.set(numdim, outPath.get(numdim) + getDataNode(ndx2).getSize());

				if (path.getNumLevels() > 1)
					if (ndx >= 0)
						getDataNode(size).getFlatIndex(path.trim(0, 1), outPath);
					else {
						DataNode arrayDataNode = getArrayDataNode();

						if (arrayDataNode != null) {
							outPath.set(outPath.concat(getArrayPrefix(-1)).concat(0));
							arrayDataNode.getFlatIndex(path.trim(0, 1), outPath);
						}
					}
			}
		}
	}

	public int getFlatIndex(Path path) {
		int index = unit != null ? 1 : 0;

		if (path.getNumLevels() != 0) {
			int ndx = path.get(0);
			int size = ndx >= 0 ? getcontentndx(ndx) : getNumNodes();

			if (size >= 0) {
				for (int ndx2 = 0; ndx2 < size; ndx2++)
					index += getDataNode(ndx2).getSize();

				if (ndx >= 0 && path.getNumLevels() > 1)
					index += getDataNode(size).getFlatIndex(path.trim(0, 1));
			}
			else
				index = -1;
		}
		else
			index = index;

		return index;
	}

	public Path getFlatPath3(Path path) {
		int index = getFlatIndex(path);

		return index == -1 ? new Path() : new Path(index);
	}
//
//	public int getFlatIndexOld(Path path) {
//		int index = 0;
//
//		if (path.getNumLevels() != 0) {
//			int ndx = path.get(0);
//
//			if (ndx >= 0) {
//				ndx = getcontentndx(ndx);
//				index = unit != null ? 1 : 0;
//
//				for (int ndx2 = 0; ndx2 < ndx; ndx2++)
//					index += getDataNode(ndx2).getSize();
//
//				if (path.getNumLevels() > 1)
//					index += getDataNode(ndx).getFlatIndex(path.trim(0, 1));
//			}
//			else
//				index = getSize() - 1;
//		}
//		else
//			index = index;
//
//		return index;
//	}

	public Path getArrayPrefix(int prefix) {
		return getDec() == null || getDec().returnType.isIntegral() ? new Path(prefix) : new Path();
	}

	public Path getRemainingPath(Path path) {
		Path remainingPath;

		if (path.getNumLevels() > 1)
			if (path.get(0) < 0) {
				DataNode arrayDataNode = getArrayDataNode();

				remainingPath = arrayDataNode != null ? getArrayPrefix(-1).concat(arrayDataNode.getDataIndex(path.trim(0, 1))) : new Path();
			}
			else
				remainingPath = getDataNode(getcontentndx(path.get(0))).getRemainingPath(path.trim(0, 1));
		else
			if (path.getNumLevels() == 0)
				remainingPath = new Path(); // Shall declare error
			else
				remainingPath = new Path();

		return remainingPath;
	}

	public Path getDataIndex(Path path) {
//		Path goodPath = getDataIndex2(path);
		Path badPath = getFlatPath(path);
//
//		if (!badPath.equals(goodPath) && (!goodPath.equals(new Path(-1)) || !badPath.equals(new Path(0))))
//			badPath = getFlatPath(path);

//		return goodPath;
		return badPath;
	}

	public Path getDataIndex2(Path path) {
		return path.getNumLevels() != 0 ? new Path(getFlatIndex(path)).concat(getRemainingPath(path)) : new Path();
//		Path flatPath = getFlatPath3(path);
//
//		return flatPath.getNumLevels() != 0 ? flatPath.concat(getRemainingPath(path)) : flatPath;
	}
//
//	public Path getLoopIndex(Path path) {
//		return getDataIndex(path.concat(-1));
//	}

	private Path getPathIndex2(Path dataPath) {
		Path path = new Path();

		dataPath.set(0, dataPath.get(0) - (unit != null ? 1 : 0));

		for (int ndx = 0; dataPath.get(0) >= 0 && ndx < getNumNodes(); ndx++) {
			path = getDataNode(ndx).getPathIndex2(dataPath);

			if (dataPath.get(0) < 0)
				path = new Path(getDataNode(ndx).index).concat(path);
		}

		if (dataPath.get(0) == 0 && getArrayDataNode() != null) {
			path = getArrayDataNode().getPathIndex2(dataPath.trim(0, 1));

			if (path.getNumLevels() != 0) {
				dataPath.set(0, -1);
				path = new Path(-1).concat(path);
			}
		}

		return path;
	}

	public Path getPathIndex(Path dataPath) {
		return getPathIndex2(stripPath(dataPath));
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

	public DataNode getDataNode(int index) {
		return (DataNode) getNode(index);
	}

	public DataNode getDataNode(Path path) {
		DataNode node = this;

		if (path.getNumLevels() != 0) {
			DataNode subNode = path.get(0) < 0 ? getArrayDataNode() : getDataNode(getcontentndx(path.get(0)));

			node = subNode != null ? subNode.getDataNode(path.trim(0, 1)) : null;
		}

		return node;
	}

	public DataNode getArrayDataNode() {
		return (DataNode) getArrayPathUnit();
	}

	public Declaration getDec() {
		return (Declaration) unit;
	}

	public int getUpperFieldPath(String field, Path path, int level) {
		int numLevels = -1;

		if (level < path.getNumLevels() - 1) {
			int ndx = getcontentndx(path.get(level));
			DataNode submtx = ndx != -1 ? getDataNode(ndx) : null;

			numLevels = submtx != null ? submtx.getUpperFieldPath(field, path, level + 1) : -1;
		}

		if (numLevels != -1)
			numLevels += getDec() != null && getDec().getcodeflag() ? 1 : 0;
		else {
			numLevels = getDec() != null && getDec().getName().equals(field) ? 0 : -1;

			if (numLevels != -1)
				if (getDec().getcodeflag())
					path.set(path.trim(level).concat(-1).concat(path.trim(0, level)));
				else
					; // error, no array on element
		}

		return (numLevels);
	}

	public int getUpperFieldPath(int[] lvl, Path path, int level) {
		int numLevels = -1;

		if (level < path.getNumLevels() - 1) {
			int ndx = getcontentndx(path.get(level));
			DataNode submtx = ndx != -1 ? getDataNode(ndx) : null;

			numLevels = submtx != null ? submtx.getUpperFieldPath(lvl, path, level + 1) : -1;
		}

		if (numLevels != -1)
			numLevels += getDec() != null && getDec().getcodeflag() ? 1 : 0;
		else
			if (getDec() != null && getDec().getcodeflag() && lvl[0]-- == 0) {
				path.set(path.trim(level).concat(-1).concat(path.trim(0, level)));
				numLevels++;
			}

		return (numLevels);
	}

	public Exp findVarPath(Exp baseExp, Path basePath, String field, List<Exp> args) {
		Exp exp = null;

		if (getArrayFlag())
			exp = getArrayDataNode().findVarPath(baseExp, basePath.concat(-1), field, args);

		for (int index = getNumNodes() - 1; exp == null && index >= 0; index--) {
			DataNode dataNode = getDataNode(index);

			exp = dataNode.findVarPath(baseExp, basePath.concat(dataNode.index), field, args);
		}

		if (exp == null && getDec() != null)
			exp = getDec().getVarPath(baseExp, basePath, field, args);

		return exp;
	}

	public boolean getcodeflag() {
		return (getDec() != null && getDec().getcodeflag());
	}

	public Exp getVarPath(int index, int count, String field, List<Exp> args, Path path, int level) {
		int ndx = level < path.getNumLevels() ? getcontentndx(path.get(level)) : -1;
		DataNode subNode = ndx != -1 ? getDataNode(ndx) : null;
		Exp exp = subNode != null ? subNode.getVarPath(index, count, field, args, path, level + 1) : null;

		if (exp == null && getArrayFlag()) {
			exp = getArrayDataNode().getVarPath(index, count + 1, field, args, path, level);

			if (exp == null)
				exp = findVarPath(new RefExp((index << 8) | (count + 1)), new Path(), field, args);

			if (exp != null)
				path.set(path.trim(level).concat(-1).concat(path.trim(0, level)));
		}

		return exp;
	}
}
