/*
 *    Copyright (C) 2016 Hocus Codus Software inc.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.node;

import java.util.ArrayList;
import java.util.List;

import com.hocuscodus.qed.lang.Context;
import com.hocuscodus.qed.lang.Declaration.FunctionDeclaration;
import com.hocuscodus.qed.lang.Env;
import com.hocuscodus.qed.lang.Event;
import com.hocuscodus.qed.lang.EventReturnHandler;
import com.hocuscodus.qed.lang.Instruction;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.QEDCall;
import com.hocuscodus.qed.lang.QEDObj;
import com.hocuscodus.qed.lang.QEDProcess;
import com.hocuscodus.qed.lang.type.Type;
import com.hocuscodus.qed.node.ContentObjNode.StorageUnitContent;

public abstract class LayoutNode extends SizeNode {
	public static final int numdim = 2;
	public static final int defaultSpace = 5;
	public static final int defaultASpace = 5;

	public static int modifindexsetter;
	public static int layoutIndex;

	public boolean aDirFlag;
	public boolean cDirFlag;
	public boolean aPackFlag;
	public boolean cPackFlag;
	public int cWeight;
	public int cSpace;
	public int aSpace;
	public int modifindex;
	public Path maxLevels;

    public LayoutNode(Instruction inst) {
    	super(inst.setflag);

    	int mask = 1 << layoutIndex;

    	aDirFlag = (inst.mtxdir & mask) != 0;
    	cDirFlag = (inst.childdir & mask) != 0;
//		aPackFlag = aDirFlag ? inst.apack < 0 || ((inst.apack) & mask) != 0 : inst.apack >= 0 && ((inst.apack) & mask) != 0;
		aPackFlag = inst.apack < 0 ? aDirFlag : (inst.apack & mask) != 0;
    	cPackFlag = inst.cpack < 0 ? cDirFlag : (inst.cpack & mask) != 0;
    	cWeight = inst.cweight;
    	cSpace = inst.cspace;
    	aSpace = inst.aspace;
    }
//adirflag insta.apack >= 0
//0			0			0 && ((inst.apack) & mask) != 0		0  (ok)
//0			1			1 && ((inst.apack) & mask) != 0		(inst.apack & mask) != 0 (ok)
//1			0			0 || ((inst.apack) & mask) != 0		1
//1			1			1 || ((inst.apack) & mask) != 0		(inst.apack & mask) != 0

	public static List<Instruction> postInstructions = new ArrayList<Instruction>();
	public static Path postLevels = new Path();

	public void buildContentTree(Context context, Instruction topInst, Instruction inst, ContentNode contentNode, Path path) {
		Commandsetcontent content = contentNode.getContentNode();

		if (content != null) {
			Path newPath = new Path(path);

	        buildPath(newPath, inst);
	        retrieveNode(context.getCtx().declaration, content, topInst, newPath, inst);
		}

		super.buildContentTree(context, topInst, inst, contentNode, path);
	}

    public Node retrieveNode(FunctionDeclaration function, Object unit, Instruction topInst, Path path, Instruction inst) {
    	LayoutNode node = (LayoutNode) super.retrieveNode(function, unit, topInst, path, inst);

		if (node != null) {
	    	int mask = 1 << layoutIndex;

	    	node.modifindex = modifindexsetter;
			node.cDirFlag = (inst.childdir & mask) != 0;
			node.cPackFlag = inst.cpack < 0 ? node.cDirFlag : (inst.cpack & mask) != 0;
		}

		return node;
    }

	public Node addpath(FunctionDeclaration function, Instruction inst, Path path, int level) {
    	boolean maxFlag = level < path.getNumLevels() && path.get(level) < 0 && (inst.mtxdir & (1 << layoutIndex)) == 0;

    	if (maxFlag) {
			postInstructions.add(inst);
			postLevels = postLevels.concat(level++);
    	}

    	LayoutNode layoutNode = (LayoutNode) super.addpath(function, inst, path, level);

    	if (level == path.getNumLevels()) {// || (inst.setflag && (inst.childdir & mask) == 0)) {
    		for (int index = 0; index < postLevels.getNumLevels(); index++)
    			layoutNode = (LayoutNode) layoutNode.setArrayPathUnit(function, postInstructions.get(index), path, postLevels.get(index));

    		layoutNode.maxLevels = new Path(postLevels);
    	}

    	if (maxFlag) {
			postInstructions.remove(inst);
			postLevels = postLevels.trim(postLevels.getNumLevels() - 1, 1);
    	}

    	return layoutNode;
    }

	protected boolean buildPath(Path newPath, Instruction inst) {
		String[] varAttrs = {"out"};
		String textSize = layoutIndex == 0 ? "textwidth" : "textheight";
		boolean rc = true;

		modifindexsetter = 0;

		for (int index = 0; rc && index < inst.getNumAttrs(); index++)
			if (inst.getAttr(index).getProp().equals(textSize)) {
				for (modifindexsetter = 0; modifindexsetter < MODIFend && !modifStrings[modifindexsetter].equals(textSize); modifindexsetter++);
				varAttrs[0] = textSize;
				break;
			}

    	for (int ndx = 0; ndx < varAttrs.length; ndx++)
    		for (int index = 0; rc && index < inst.getNumAttrs(); index++)
        		if (inst.getAttr(index).getProp().equals(varAttrs[ndx])) {
        			rc = buildPath(newPath, inst.getAttr(index).path);
        			break;
        		}

        for (int index = 0; rc && index < inst.getNumAttrs(); index++) {
        	int ndx;

        	for (ndx = 0; ndx < varAttrs.length && !inst.getAttr(index).getProp().equals(varAttrs[ndx]); ndx++);

        	if (ndx == varAttrs.length)
    			rc = buildPath(newPath, inst.getAttr(index).path, -2);
        }

		return (rc);
	}

    @Override
	public Node createNode(FunctionDeclaration function, Path path, int level, Instruction inst) {
        return (null);
    }

	public boolean isConstant(Env env) {
        return !aDirFlag && constantFlag;
    }

    public int getmodifier() {
    	return (modifindex);
    }

    public boolean getframeflag() {
    	return (modifindex != 0);
    }

    public boolean getchilddirflag() {
        return setflag && cDirFlag;
    }

    public boolean getchildpackflag() {
        return setflag && cPackFlag;
    }

    @Override
    public void addmtx(Env env, SizeStorageUnitList sizeStoragesetarray, int dsize, int dindex) {
		if (aDirFlag)
        	if (aPackFlag) {
        		super.addmtx(env, sizeStoragesetarray, dsize, dindex);

        		if (dindex < dsize - 1)
        			sizeStoragesetarray.mtxpanesets[dindex] += aSpace;//getValueInt(env, env.path, ContentNode.MODIFaspace, 0);
        	}
        	else {
        		StorageUnitSize storageset = (StorageUnitSize) sizeStoragesetarray.storageUnits[dindex];

        		sizeStoragesetarray.mtxpanesets[dindex] = Math.max(gettotalview(storageset), dindex != 0 ? sizeStoragesetarray.mtxpanesets[dindex - 1] : 0);
    			Node.log3("maxing for dindex " + dindex + ", for a total of " + sizeStoragesetarray.mtxpanesets[dindex] + " mtx units.");
        	}
		else {
    		StorageUnitLayout storageset = (StorageUnitLayout) sizeStoragesetarray.storageUnits[dindex];

    		if (!aPackFlag) {
				LayoutStorageUnitList layoutStoragesetarray = (LayoutStorageUnitList) sizeStoragesetarray;

				layoutStoragesetarray.headerviews[dindex] = Math.max(storageset.headerview, dindex != 0 ? layoutStoragesetarray.headerviews[dindex - 1] : 0);
			}

			int elementView = aPackFlag ? gettotalview(storageset) : getelementview(storageset);

			sizeStoragesetarray.mtxpanesets[dindex] = Math.max(elementView, dindex != 0 ? sizeStoragesetarray.mtxpanesets[dindex - 1] : 0);
    		Node.log3("maxing for dindex " + dindex + ", for a total of " + sizeStoragesetarray.mtxpanesets[dindex] + " mtx units.");
		}
    }

    @Override
    public void setmtxsize(Env env, StorageUnitSize storageUnit, int dsize, int realdsize) {
		SizeStorageUnitList sizeStorageSetArray = (SizeStorageUnitList) storageUnit.arrayStorageUnits;

		if (aDirFlag) {
    		if (!aPackFlag && !constantFlag) {
    			int space = aSpace;//getValueInt(env, env.path, ContentNode.MODIFaspace, 0);
        		int unitpaneset = dsize != 0 ? sizeStorageSetArray.mtxpanesets[dsize - 1] : 0;
                int subsize = constantFlag
    					? /*((StorageUnitSize) storageUnit.arrayStorageUnits.storageUnits[0]).*/mult(realdsize, unitpaneset + space) - space
    					: unitpaneset;

    			storageUnit.mtxpaneset = /*((StorageUnitSize) storageUnit.arrayStorageUnits.storageUnits[dsize - 1]).*/mult(dsize, subsize + space) - space;
    		}
    		else
    			super.setmtxsize(env, storageUnit, dsize, realdsize);
    	} else {
        	int unitpaneset = dsize != 0 ? sizeStorageSetArray.mtxpanesets[dsize - 1] : 0;

        	storageUnit.mtxpaneset = Math.max(storageUnit.mtxpaneset, unitpaneset);
        	Node.log3("Maxing label body size " + unitpaneset + " for a total of " + storageUnit.mtxpaneset);

        	if (!aPackFlag) {
        		int maxheaderview = dsize != 0 ? ((LayoutStorageUnitList) storageUnit.arrayStorageUnits).headerviews[dsize - 1] : 0;
        		StorageUnitLayout storageUnitLayout = (StorageUnitLayout) storageUnit;

        		storageUnitLayout.headerview = Math.max(storageUnitLayout.headerview, maxheaderview);
        		Node.log3("Maxing label header size " + maxheaderview + " for a total of " + storageUnitLayout.headerview);
        	}
    	}
    }

    @Override
	public int addchild(StorageUnitSize storageUnit, int index, int previousarea) {
		StorageUnitLayout storageset = (StorageUnitLayout) storageUnit.childStorageUnits.storageUnits[index];
    	int area = getelementview(storageset);

    	if (getchildpackflag() || getchilddirflag())
    		area += storageset.headerview;
    	else {
			LayoutStorageUnitList layoutstoragesetarray = (LayoutStorageUnitList) storageUnit.childStorageUnits;

			layoutstoragesetarray.headerviews[index] = Math.max(storageset.headerview, index != 0 ? layoutstoragesetarray.headerviews[index - 1] : 0);
		}

    	if (getchildpackflag() && getchilddirflag())
    		area += previousarea + (setflag && index < getNumNodes() - 1 ? defaultSpace : 0);
    	else
    		area = Math.max(area, previousarea);

    	return (area);
    }

    @Override
    public void setsubsize(StorageUnitSize storageUnit, int subsize) {
    	if (getchilddirflag() && !getchildpackflag()) {
            int space = setflag ? defaultSpace : 0;

            subsize = mult(getNumNodes(), subsize + space) - space;
    	}

    	storageUnit.mtxpaneset = Math.max(storageUnit.mtxpaneset, subsize);

    	if (!getchildpackflag() && !getchilddirflag()) {
            int numcxsets = getNumNodes();
    		StorageUnitLayout storageUnitLayout = (StorageUnitLayout) storageUnit;

    		storageUnitLayout.headerview = Math.max(storageUnitLayout.headerview, numcxsets != 0 ? ((LayoutStorageUnitList) storageUnit.childStorageUnits).headerviews[numcxsets - 1] : 0);
    	}
    }

    @Override
    public void addlabelsize(StorageUnitSize storageUnit, int labelsize) {
		StorageUnitLayout storageUnitLayout = (StorageUnitLayout) storageUnit;

		if (cDirFlag) {
			storageUnitLayout.headerview = Math.max(storageUnitLayout.headerview, labelsize);
        	Node.log3("Accumulating label header size " + labelsize + " for a total of " + storageUnitLayout.headerview);
    	}
    	else {
    		storageUnitLayout.mtxpaneset = Math.max(storageUnitLayout.mtxpaneset, labelsize);
    		Node.log3("Accumulating label body size " + labelsize + " for a total of " + storageUnitLayout.mtxpaneset);
    	}
    }

	public int gettotalview(StorageUnitSize storageUnit) {
    	return (super.gettotalview(storageUnit) + ((StorageUnitLayout) storageUnit).headerview);
    }

	public int getheaderview(LayoutObject layoutObject, int index) {
		return (layoutObject.topLayoutStorageUnits[index].headerview);
	}

	public Path convertPath(Path path) {
		int numLevels = maxLevels != null ? maxLevels.getNumLevels() : 0;
		int size = path.getNumLevels() - numLevels;
		Path newPath = path.trim(size);

		for (int index = 0; index < numLevels; index++)
			newPath = newPath.trim(maxLevels.get(index)).concat(path.get(size + index)).concat(newPath.trim(0, maxLevels.get(index)));

		return newPath;
    }

    @Override
	public StorageUnit createStorageUnit(Env env) {
    	return new StorageUnitLayout();
    }
//
//    @Override
//    public boolean parsesubinitunits(Env env) {
//    	if (getUnitFlag() && (getframeflag() || !(getContentNode().type instanceof FunctionDeclaration)))
//    		initunit(env);
//
//        return super.parsesubinitunits(env);
//    }

    @Override
    public boolean parseResize(Env env) {
    	if (getUnitFlag() && (getframeflag() || !(getContentNode().type instanceof FunctionDeclaration)))
    		initunit(env);

        return super.parseResize(env);
    }
//
//    @Override
//    public boolean parsesubuninitunits(StorageUnitCode topNode, Path path) {
//    	LayoutNode layoutNode = (LayoutNode) arrayNode;
//    	boolean rc = true;
//
//        if (layoutNode.getMaxArrayPathUnit() != null) {
//        	for (int dindex = getdsize(topNode, path) - 1; rc && dindex >= 0; dindex--) {
//        		rc = arrayStorageUnits.storageUnits[dindex].parsesubuninitunits(topNode, path.concat(-dindex - 1));
////        		uninitmaxmtxstorageset(arrayStorageUnits.storagesets[dindex], dindex);
//        	}
//
//        	if (rc) {
////            	rc = arrayStorageUnits.uninit(this, dsize);
////            	uninit(arrayStorageUnits);
//        	}
//    	}
//
//        return (rc && super.parsesubuninitunits(topNode, path));
//    }
//
//	public int getdsize(StorageUnitCode topNode, Path path) {
//        LayoutNode layoutNode = (LayoutNode) arrayNode;
//
//        return !layoutNode.aDirFlag && layoutNode.constantFlag ? 1 : layoutNode.getdsize(new Env(null, topNode.getStorageCode(), path, null));
//    }

    public void initunit(Env env) {
    	StorageUnitLayout storageUnit = (StorageUnitLayout) env.currentUnit;

    	storageUnit.unitarea = 0;

    	int modifindex = 0;//((LayoutNode) arrayNode).getmodifier();
        Object value;

//		Commandsetcontent contentset = getContentNode();
//        int content = contentset != null ? contentset.getcontent(ContentNode.MODIFfontsize) : 0;
//		int fontSize = content != 0 ? ((Integer) execcmd(env, env.path, ContentNode.MODIFfontsize)) : -1;
        int fontSize = getValueInt(env, env.path, ContentNode.MODIFfontsize);

		if (getframeflag()) {
            value = execcmd(env, env.path, getmodifier());
            int mult = getavgcharsize(env.process, fontSize);

            storageUnit.unitarea = ((Integer) value) * mult;
        }
        else {
        	value = execcmd(env, env.path, modifindex);

        	if (value != null) {
                Type fn = getContentNode().type;
//                getReturnType(modifindex);

                storageUnit.unitarea = getSize(env.process, fn, value, fontSize);
            }
        }

        int limit = getscreensize(env.process);

        if (storageUnit.unitarea > limit)
        	storageUnit.unitarea = limit;
    }

	@Override
	public StorageUnitList createStorageUnitList(char strChar) {
    	return aDirFlag ? super.createStorageUnitList(strChar) : aPackFlag ? new SizeStorageUnitList('[') : new LayoutStorageUnitList('+');// new Sizemtxstorageset(strChar);
    }

    public StorageUnitList createChildStorageUnits() {
    	return getchildpackflag() || getchilddirflag() ? super.createChildStorageUnits() : new LayoutStorageUnitList('|');
    }

    @Override
	public int getunitsize(ContentObjNode content, StorageUnitContent topContentStorageUnit, StorageUnitSize storageUnit, Path path) {
		return (getframeflag() || !(getContentNode().type instanceof FunctionDeclaration) ? ((StorageUnitLayout) storageUnit).unitarea : super.getunitsize(content, topContentStorageUnit, storageUnit, path));
    }

    public Object execcmd(Env env, Path path, int index) {
    	return super.execcmd(env, convertPath(path), index);
    }

    public int getheadersize(ContentObjNode content, StorageUnitContent topContentStorageUnit, StorageUnitSize storageUnit, Path path) {
    	return 0;
    }

    public int getoffset(StorageUnitSize storageUnit, int index) {
        return (cDirFlag ? super.getoffset(storageUnit, index) : 0);
    }

    @Override
    public int getdpos(Env env, StorageUnitSize storageUnit, int dsize, int dindex) {
        boolean nPackFlag = dindex > 0 && aDirFlag && !constantFlag && !aPackFlag;
//		int totalSpace = defaultASpace * (dindex - 1);
//		int bodysize = layoutContext.bodysize - layoutContext.headersize;//totalSpace;
//		int totalCWeight = /*sizeContext.contentNode.*/getTotalCWeight();
//		int size = (getoffset(sizeContext.storageset, ndx2 + 1) + (ndx2 < numNodes - 1 ? 0 : defaultSpace) - (defaultSpace * (ndx2 + 1))) - (pos1 - (defaultSpace * ndx2));
//
//		if (totalCWeight != 0)
//			size += (bodysize - layerbodysize) * /*sizeContext.contentNode.*/getCWeight(ndx2) / totalCWeight;
//		else
//			layerbodysize -= totalSpace;

        return nPackFlag ? mult(dindex, ((SizeStorageUnitList) storageUnit.arrayStorageUnits).mtxpanesets[dsize - 1] + aSpace/*getValueInt(env, env.path, ContentNode.MODIFaspace, 0)*/) : aDirFlag ? super.getdpos(env, storageUnit, dsize, dindex) : 0;
    }

    public void getunitrange(Env env, StorageUnitSize storageUnit, int diffpos, int dsize, int size, int[] start, int[] end) {
    	int midarea = 0;//getlayerlayout().getmidarea(cx);
    	int unitsize = dsize != 0 ? (getdpos(env, storageUnit, dsize, dsize) + midarea) / dsize : 0;
    	int dindex = getdindex(diffpos, unitsize);

    	start[0] = Math.max(start[0], dindex);
    	dindex = size != 0 ? getdindex(diffpos + size - 1, unitsize) : start[0] - 1;
    	end[0] = Math.min(end[0], dindex);
    }

    public static int getdindex(int pos, int size) {
        return (size != 0 ? pos / size - (pos < 0 ? 1 : 0) : 0);
    }

    public SizeContext initlayercx(SizeContext sizeContext, SizeContextSet layercxset) {
    	LayoutContext sublayercx = (LayoutContext) super.initlayercx(sizeContext, layercxset);

    	if (sublayercx != null) {
    		LayoutContext layoutContext = (LayoutContext) sizeContext;

    		sublayercx.dindexes = layoutContext.dindexes;
    		sublayercx.headersize = layoutContext.headersize;
    		sublayercx.bodysize = layoutContext.bodysize;
    		sublayercx.clippos = layoutContext.clippos;
    		sublayercx.clipsize = layoutContext.clipsize;
    	}

    	return (sublayercx);
    }

	public void getsetrange(ContentObjNode content, StorageUnitContent topContentStorageUnit, LayoutContext layoutContext, int viewPos, int[] start, int[] end) {
		if (layoutContext.clipsize <= 0)
			end[0] = start[0] - 1;

		if (start[0] <= end[0] && cDirFlag) {
			boolean multipleflag = true;//layerpaths.layercommand.getNumNodes(cx) > 1;
			int posend = layoutContext.clippos + layoutContext.clipsize - 1;

			for (int index1 = end[0]; index1 >= start[0]; index1--) {
				int pos2 = layoutContext.pos - viewPos;
				int elementview = 0;

				if (multipleflag) {
					pos2 = getcpos(content, topContentStorageUnit, layoutContext, pos2, index1);
					elementview = getcsize(layoutContext, index1);//((StorageUnitSize) childstoragesets.storagesets[index1]).gettotalview();
				}

				int unitsize = elementview;//[1] + elementview[0]; // to replace by max(layercx2.subvaluesize / dsize - sublayercx2.dvaluepos, 0)

				if (layoutContext.clippos >= pos2 && layoutContext.clippos < pos2 + unitsize)
					start[0] = Math.max(start[0], index1);

				if (posend >= pos2 && posend < pos2 + unitsize)
					end[0] = Math.min(end[0], index1);
/*
				if (layoutContext.clippos >= pos2 + unitsize)
					start[0] = Math.max(start[0], index1 + 1);

				if (posend < pos2)
					end[0] = Math.min(end[0], index1 - 1);*/
			}
		}
    }

	public void initsubcx(LayoutContext layoutContext, LayoutObject layoutObject, int index, int size, int clippos, int clipsize) {
		layoutContext.dindexes = new Path();
		layoutContext.headersize = getheaderview(layoutObject, index);
		layoutContext.bodysize = size;//layer.getelementview();

		layoutContext.clipsize = Math.min(layoutContext.pos + size, clippos + clipsize);
		layoutContext.clippos = Math.max(layoutContext.pos, clippos);
		layoutContext.clipsize -= layoutContext.clippos;
    }

    @Override
	public boolean initchildlayercx(ContentObjNode content, StorageUnitContent topContentStorageUnit, SizeContext sizeContext, int ndx, SizeContext sublayercx) {
//    	StorageUnitSize oldstorageset = storageset;
//    	Path olddindexes = dindexes;
//
//     	while (dindexes.getNumLevels() != 0 && getArrayFlag() && !getmtxbodyflag()) {
//     		if (((StorageUnitLayout) storageset).storagesetmaxarray != null)
//    		storageset = (StorageUnitSize) ((StorageUnitLayout) storageset).storagesetmaxarray.storagesets[layoutNode.getconstantmtxflag() ? 0 : dindexes.get(0)];
//     		else
//    		storageset = (StorageUnitSize) storageset.storagesetarray.storagesets[getconstantmtxflag() ? 0 : dindexes.get(0)];
//    		dindexes = dindexes.trim(0, 1);
//    		setflag = layoutNode.setflag;
//        }
//
//    	if (getNumNodes() == 0) {
//        	storageset = oldstorageset;
//        	dindexes = olddindexes;
//        	setflag = layoutNode.setflag;
//    	}

    	boolean rc = super.initchildlayercx(content, topContentStorageUnit, sizeContext, ndx, sublayercx);

        if (rc && setflag) {
        	LayoutContext sublayercx2 = (LayoutContext) sublayercx;
        	int numNodes = getNumNodes();
        	LayoutContext layoutContext = (LayoutContext) sizeContext;

        	if (cPackFlag) // body
        		if (cDirFlag) { // body
        			int ndx2 = getcontentndx(ndx);
        			int pos1 = getoffset(sizeContext.storageset, ndx2);
        			int layerbodysize = getoffset(sizeContext.storageset, numNodes);

//        			sublayercx.pos +=sizeContext.headersize - pos1;
        			sublayercx2.headersize = ((StorageUnitLayout) sublayercx2.storageset).headerview;

        			if (layerbodysize != 0) {
        				int totalSpace = defaultSpace * (numNodes - 1);
        				int bodysize = layoutContext.bodysize - layoutContext.headersize;//totalSpace;
        				int totalCWeight = /*sizeContext.contentNode.*/getTotalCWeight();

//        				int spareSpace = bodysize - layerbodysize;
//        				float usedSpace = spareSpace / (numcxsets - 1);
//        				int pos = (int) (pos1 - (defaultSpace * ndx2) + usedSpace * ndx2);

//                        int space = layoutNode.setflag && ndx2 < numcxsets - 1 ? defaultSpace : 0;
//        				int pos = (pos1 - (defaultSpace * ndx2)) * bodysize / layerbodysize + defaultSpace * ndx2;
//        				int size = (storageset.getoffset(ndx2 + 1) + (ndx2 < numcxsets - 1 ? 0 : defaultSpace) - (defaultSpace * (ndx2 + 1))) * bodysize / layerbodysize + defaultSpace * (ndx2 + 1) - pos - defaultSpace;

//        				sublayercx.pos += pos;
        				int size = (getoffset(sizeContext.storageset, ndx2 + 1) + (ndx2 < numNodes - 1 ? 0 : defaultSpace) - (defaultSpace * (ndx2 + 1))) - (pos1 - (defaultSpace * ndx2));

        				if (totalCWeight != 0)
            				size += (bodysize - layerbodysize) * /*sizeContext.contentNode.*/getCWeight(ndx2) / totalCWeight;
        				else
            				layerbodysize -= totalSpace;

        				if (((SizeNode) getNode(ndx2)).getelementview(sublayercx2.storageset) != 0)
            				sublayercx2.bodysize = size - sublayercx2.headersize;
        				else {
            				sublayercx2.headersize = size;
            				sublayercx2.bodysize = 0;
        				}
        			}
        			else {
        				int pos = ndx2 * layoutContext.bodysize / numNodes;

//        				sublayercx.pos += pos;
        				sublayercx2.bodysize = (ndx2 + 1) * layoutContext.bodysize / numNodes - pos - sublayercx2.headersize;//((Layoutstorageset) sublayercx2.substorageset).getbodyview(); // to replace by max(layercx2.subvaluesize / dsize - dvaluepos, 0)
        			}
        		}
        		else {
        			sublayercx2.headersize = ((StorageUnitLayout) sublayercx2.storageset).headerview;
    				sublayercx2.bodysize = ((SizeStorageUnitList) sizeContext.storageset.childStorageUnits).mtxpanesets[numNodes - 1] - sublayercx2.headersize;
        		}
    		else // header
        		if (cDirFlag) { // body
        			int ndx2 = getcontentndx(ndx);
//        			int pos1 = storageset.getoffset(ndx2);
        			int layerbodysize = sizeContext.storageset.mtxpaneset;

//        			sublayercx.pos += layoutContext.headersize - pos1;
        			sublayercx2.headersize = ((StorageUnitLayout) sublayercx2.storageset).headerview;

        			if (layerbodysize != 0) {
            			int pos = sizeContext.storageset.mtxpaneset * ndx2 / numNodes;
            			int size = sizeContext.storageset.mtxpaneset * (ndx2 + 1) / numNodes - pos;

//        				sublayercx.pos += pos;
            			sublayercx2.bodysize = size - sublayercx2.headersize;
        			}
//        			else {
//        				int pos = ndx * bodysize / numcxsets;
//
//        				sublayercx.pos += pos;
//        				sublayercx2.bodysize = (ndx + 1) * bodysize / numcxsets - pos - sublayercx2.headersize;//((Layoutstorageset) sublayercx2.substorageset).getbodyview(); // to replace by max(layercx2.subvaluesize / dsize - dvaluepos, 0)
//        			}
        		}
        		else {
        			sublayercx2.headersize = layoutContext.headersize;
        			sublayercx2.bodysize = layoutContext.bodysize;
//    			sublayercx2.headersize = ((Layoutstorageset) sublayercx2.storageset).getheaderview();
//
//				if (((Layoutstorageset) sublayercx2.storageset).getelementview() != 0)
//    				sublayercx2.bodysize = headersize + bodysize - sublayercx2.headersize;
//				else {
//				}
        		}
        }
//
//    	storageset = oldstorageset;
//    	dindexes = olddindexes;

    	return (rc);
    }

    public int getcpos(ContentObjNode content, StorageUnitContent topContentStorageUnit, SizeContext sizeContext, int pos, int ndx) {
    	int cpos = super.getcpos(content, topContentStorageUnit, sizeContext, pos, ndx);

        if (setflag) {
        	int numNodes = getNumNodes();
        	LayoutContext layoutContext = (LayoutContext) sizeContext;

        	if (cPackFlag) {// body
        		if (cDirFlag) { // body
        			int ndx2 = ndx;//getcontentndx(ndx);
        			int pos1 = getoffset(sizeContext.storageset, ndx2);
        			int layerbodysize = getoffset(sizeContext.storageset, numNodes);

        			cpos += layoutContext.headersize - pos1;

        			if (layerbodysize != 0) {
        				int totalSpace = defaultSpace * (numNodes - 1);
        				int bodysize = layoutContext.bodysize - layoutContext.headersize;// - totalSpace;
        				int totalCWeight = /*sizeContext.contentNode.*/getTotalCWeight();

        				if (totalCWeight != 0) {
        					int weightUsed = /*sizeContext.contentNode.*/getTotalCWeight(ndx2);
            				int spareSpace = (bodysize - layerbodysize) * weightUsed / totalCWeight;
            				cpos += pos1 - (defaultSpace * ndx2) + ndx2 * defaultSpace + spareSpace;
        				} else {
            				layerbodysize -= totalSpace;
            				int spareSpace = bodysize - layerbodysize;
            				cpos += pos1 - (defaultSpace * ndx2) + ndx2 * spareSpace / (numNodes - 1);
////                            int space = layoutNode.setflag && ndx2 < numcxsets - 1 ? defaultSpace : 0;
//            				cpos += (pos1 - (defaultSpace * ndx2)) * bodysize / layerbodysize + defaultSpace * ndx2;
////            				int pos2 = pos1 * bodysize / layerbodysize;
////
////            				cpos += pos2;
        				}
        			}
        			else {
        				int pos2 = ndx2 * layoutContext.bodysize / numNodes;

        				cpos += pos2;
        			}
        		}
        	}
    		else // header
        		if (cDirFlag) { // body
        			int ndx2 = ndx;//getcontentndx(ndx);
        			int pos1 = getoffset(sizeContext.storageset, ndx2);
        			int layerbodysize = sizeContext.storageset.mtxpaneset;

        			cpos += layoutContext.headersize - pos1;

        			if (layerbodysize != 0) {
            			int pos2 = sizeContext.storageset.mtxpaneset * ndx2 / numNodes;

        				cpos += pos2;
        			}
//        			else {
//        				int pos2 = ndx * bodysize / numcxsets;
//
//        				sublayercx.pos += pos2;
//        				sublayercx2.bodysize = (ndx + 1) * bodysize / numcxsets - pos2 - sublayercx2.headersize;//((Layoutstorageset) sublayercx2.substorageset).getbodyview(); // to replace by max(layercx2.subvaluesize / dsize - dvaluepos, 0)
//        			}
        		}
        }
//
//    	storageset = oldstorageset;
//    	dindexes = olddindexes;

    	return (cpos);
    }

    public int getcsize(SizeContext sizeContext, int ndx) {
    	int cheadersize = 0;
    	int cbodysize = 0;

        if (setflag) {
        	int numNodes = getNumNodes();
        	LayoutContext layoutContext = (LayoutContext) sizeContext;
			int ndx2 = ndx;//getcontentndx(ndx);
			StorageUnitLayout substorageset = (StorageUnitLayout) sizeContext.storageset.childStorageUnits.storageUnits[ndx];

			if (cPackFlag) // body
        		if (cDirFlag) { // body
        			int pos1 = getoffset(sizeContext.storageset, ndx2);
        			int layerbodysize = getoffset(sizeContext.storageset, numNodes);

        			cheadersize = substorageset.headerview;

        			if (layerbodysize != 0) {
//        				int totalSpace = defaultSpace * (numcxsets - 1);
//        				int bodysize = this.bodysize - headersize;// - totalSpace;
//        				int totalCWeight = contentNode.getTotalCWeight();
//
//        				if (totalCWeight != 0) {
//        					int weightUsed = contentNode.getTotalCWeight(ndx2);
//            				int spareSpace = (bodysize - layerbodysize) * weightUsed / totalCWeight;
//            				cpos += pos1 - (defaultSpace * ndx2) + ndx2 * defaultSpace + spareSpace;
//        				} else {
//            				layerbodysize -= totalSpace;
//            				int spareSpace = bodysize - layerbodysize;
//            				cpos += pos1 - (defaultSpace * ndx2) + ndx2 * spareSpace / (numcxsets - 1);
                        int space = setflag && ndx2 < numNodes - 1 ? defaultSpace : 0;
        				int pos2 = pos1 * layoutContext.bodysize / layerbodysize;
        				int size = getoffset(sizeContext.storageset, ndx2 + 1) * layoutContext.bodysize / layerbodysize - pos2 - space;

        				if (((SizeNode) getNode(ndx)).getelementview(substorageset) != 0)
//        				if (((SizeNode) substorageset.arrayNode).getelementview(substorageset) != 0)
            				cbodysize = size - cheadersize;
        				else {
            				cheadersize = size;
            				cbodysize = 0;
        				}
        			}
        			else {
        				int pos2 = ndx2 * layoutContext.bodysize / numNodes;

        				cbodysize = (ndx2 + 1) * layoutContext.bodysize / numNodes - pos2 - cheadersize;//((Layoutstorageset) sublayercx2.substorageset).getbodyview(); // to replace by max(layercx2.subvaluesize / dsize - dvaluepos, 0)
        			}
        		}
        		else {
        			cheadersize = substorageset.headerview;
    				cbodysize = ((SizeStorageUnitList) sizeContext.storageset.childStorageUnits).mtxpanesets[numNodes - 1] - cheadersize;
        		}
    		else // header
        		if (cDirFlag) { // body
        			int layerbodysize = sizeContext.storageset.mtxpaneset;

        			cheadersize = substorageset.headerview;

        			if (layerbodysize != 0) {
            			int pos2 = sizeContext.storageset.mtxpaneset * ndx2 / numNodes;
            			int size = sizeContext.storageset.mtxpaneset * (ndx2 + 1) / numNodes - pos2;

            			cbodysize = size - cheadersize;
        			}
//        			else {
//        				int pos2 = ndx * bodysize / numcxsets;
//
//        				sublayercx.pos += pos2;
//        				sublayercx2.bodysize = (ndx + 1) * bodysize / numcxsets - pos2 - sublayercx2.headersize;//((Layoutstorageset) sublayercx2.substorageset).getbodyview(); // to replace by max(layercx2.subvaluesize / dsize - dvaluepos, 0)
//        			}
        		}
        		else {
        			cheadersize = layoutContext.headersize;
        			cbodysize = layoutContext.bodysize;
//    			sublayercx2.headersize = ((Layoutstorageset) sublayercx2.storageset).getheaderview();
//
//				if (((Layoutstorageset) sublayercx2.storageset).getelementview() != 0)
//    				sublayercx2.bodysize = headersize + bodysize - sublayercx2.headersize;
//				else {
//				}
        		}
        }
//
//    	storageset = oldstorageset;
//    	dindexes = olddindexes;

    	return (cheadersize + cbodysize);
    }

	public int getTotalCWeight() {
		return getTotalCWeight(getNumNodes());
	}

	public int getTotalCWeight(int size) {
		int totalCWeight = 0;

		for (int index = 0; index < size; index++)
			totalCWeight += getCWeight(index);

		return totalCWeight;
	}

	public int getCWeight(int index) {
		return ((LayoutNode) getNode(index)).cWeight;
//		int expIndex = ((ContentNode) getNode(index)).getcontent(ContentNode.MODIFcweight);
//
//		return expIndex;// 1;//expIndex != 0 ? (Integer) ((ValueExp) exp).value : 0;
	}

	@Override
	public boolean initmtxlayercx(Env env, ContentObjNode content, StorageUnitContent topContentStorageUnit, SizeContext sizeContext, int dindex, int dsize, int numNodes, SizeContext sublayercx) {
        LayoutContext sublayercx2 = (LayoutContext) sublayercx;
        boolean rc = true;

        if (aDirFlag && getArrayFlag()) {
        	rc = super.initmtxlayercx(env, content, topContentStorageUnit, sizeContext, dindex, dsize, numNodes, sublayercx);

        	if (rc) {
            	LayoutContext layoutContext = (LayoutContext) sizeContext;
    			int space = dindex < dsize - 1 ? aSpace/*getValueInt(env, env.path, ContentNode.MODIFaspace, 0)*/ : 0;

            	sublayercx2.pos += layoutContext.headersize;
        		sublayercx2.clippos = layoutContext.clippos;//Math.max(layercx.clippos, layercx.pos + layercx.headersize);
        		sublayercx2.clipsize = layoutContext.clipsize;// + layercx.clippos - sublayercx2.clippos;
        		sublayercx2.headersize = ((StorageUnitLayout) sublayercx2.storageset).headerview;

        		sublayercx2.bodysize = getdpos(env, sizeContext.storageset, dsize, dindex + 1) - getdpos(env, sizeContext.storageset, dsize, dindex) - space - sublayercx2.headersize;
        	}
        }
        else
        	sublayercx2.dindexes = sublayercx2.dindexes.concat(dindex);

		return (rc);
    }

    public boolean getpaneunitflag(SizeContext sizeContext, Env env, LayoutContextSet layoutContextSet, Path path, int index, int viewPos, int[] pos, int[] size, int[] clippos, int[] clipsize) {
        super.getpaneunitflag(sizeContext, index, pos);
        pos[index] -= viewPos;

        LayoutContext layoutContext = (LayoutContext) sizeContext;

        if (cDirFlag)
        	size[index] = layoutContext.headersize;
        else {
        	pos[index] += layoutContext.headersize;
        	size[index] = layoutContext.bodysize;
        }

        int alignment = layoutContextSet.getValueInt(env, path, index == 0 ? ContentNode.MODIFalignx : ContentNode.MODIFaligny);
		int expand = layoutContextSet.getValueInt(env, path, index == 0 ? ContentNode.MODIFexpwidth : ContentNode.MODIFexpheight);

		if (alignment != -1 || expand != -1) {
			int area = getelementview(sizeContext.storageset);
			//layoutContextSet.getValueInt(path, index == 0 ? ContentNode.MODIFviewx : ContentNode.MODIFviewy);

			if (expand != -1)
				area += (size[index] - area) * expand / 100;

			if (alignment != -1)
				pos[index] += (size[index] - area) * alignment / 100;

			size[index] = area;
		}

		clipsize[index] = Math.min(pos[index] + size[index], layoutContext.clippos + layoutContext.clipsize);
        clippos[index] = Math.max(pos[index], layoutContext.clippos);
        clipsize[index] -= clippos[index];

        return (clipsize[index] > 0);
    }

    @Override
    public boolean initArray(StorageUnitList arrayStorageUnits, int dsize) {
    	boolean rc = super.initArray(arrayStorageUnits, dsize);

    	if (rc && arrayStorageUnits instanceof LayoutStorageUnitList) {
    		LayoutStorageUnitList unitArray = (LayoutStorageUnitList) arrayStorageUnits;

    		unitArray.headerviews = unitArray.storageUnits != null ? createpanesetarray(dsize) : null;
    		rc = dsize == 0 || unitArray.headerviews != null;
    	}

    	return (rc);
    }

	public static int[] createarea() {
		return createarea(0);
	}

	public static int[] createarea(int initvalue) {
		int[] newarea = new int[numdim];

		for (int index = 0; index < numdim; index++)
			newarea[index] = initvalue;

		return (newarea);
	}

	public static int[] createarea(int[] area) {
		int[] newarea = new int[numdim];

		for (int index = 0; index < numdim; index++)
			newarea[index] = area[index];

		return (newarea);
	}

    protected abstract int getavgcharsize(QEDProcess process, int fontSize);

    protected abstract int getscreensize(QEDProcess process);

    protected abstract int getSize(QEDProcess process, Type fn, Object value, int fontSize);

	public static class StorageUnitLayout extends StorageUnitSize {
        public int headerview;
		public int unitarea;
    }

	public static class LayoutStorageUnitList extends SizeStorageUnitList {
		public int[] headerviews;

        public LayoutStorageUnitList(char strChar) {
			super(strChar);
		}
    }

    public static class LayoutContextSet extends SizeContextSet {
    	public LayoutContextSet(int size, QEDObj obj, StorageUnitContent topContentStorageUnit, ContentNode contentNode) {
			super(size, obj, topContentStorageUnit, contentNode);
		}

	    public SizeContextSet createlayercxset(QEDObj obj, StorageUnitContent topContentStorageUnit, ContentNode contentNode) {
	        return new LayoutContextSet(LayoutNode.numdim, obj, topContentStorageUnit, contentNode);
	    }

        @Override
    	public SizeContext createlayercx() {
            return new LayoutContext();
        }

        public boolean parserefresh(Env env, Object ctx, int[] viewPos) {
            boolean rc = true;

            for (int pane = (1 << LayoutNode.numdim) - 1; rc && pane >= 0; pane--) {
            	int[] subPos = new int[LayoutNode.numdim];
            	int[] oldClipPos = new int[LayoutNode.numdim];
            	int[] oldClipSize = new int[LayoutNode.numdim];

                for (int index = 0; index < LayoutNode.numdim; index++) {
                    LayoutContext layercx = (LayoutContext) layercxs[index];

                    oldClipPos[index] = layercx.clippos;
                    oldClipSize[index] = layercx.clipsize;

                    if ((pane & (1 << index)) != 0) { // header
                		subPos[index] = 0;
                		layercx.clipsize = Math.max(Math.min(layercx.clipsize + layercx.clippos, layercx.pos + layercx.headersize) - layercx.clippos, 0);
                	}
                    else { // body
                		subPos[index] = viewPos[index];
                		layercx.clippos = Math.max(layercx.clippos, layercx.pos + layercx.headersize);
                		layercx.clipsize = Math.max(layercx.clipsize + oldClipPos[index] - layercx.clippos, 0);
                    }
            	}

            	rc = parserefresh(env, ctx, subPos, new Path());

                for (int index = 0; index < LayoutNode.numdim; index++) {
                    LayoutContext layercx = (LayoutContext) layercxs[index];

                    layercx.clippos = oldClipPos[index];
                	layercx.clipsize = oldClipSize[index];
            	}
            }

        	return rc;
        }

        public boolean parserefresh(Env env, Object ctx, int[] viewPos, Path path) {
            int[] pos = createarea();
            int[] size = createarea();
            int[] clippos = createarea();
            int[] clipsize = createarea();
            int[] start = {0};
            int[] end = {0};
            boolean paneUnitFlag = getpaneunitflag(env, path, viewPos, pos, size, clippos, clipsize);
            boolean rc = true;

            if (paneUnitFlag) {
            	env.process.saveContext();
        		rc = refreshunit(env, ctx, path, pos, size, subtract(clippos, pos), clipsize);
            }

            if (rc && getsetrange(viewPos, start, end))
            	for (int index = start[0]; rc && index <= end[0]; index++)
            		rc = ((LayoutContextSet) initchildlayercxset(getcontentindex(index))).parserefresh(env, ctx, viewPos, path.concat(getcontentindex(index)));

            if (rc && getunitrange(env, path, viewPos, start, end))
                for (int dindex = start[0]; rc && dindex <= end[0]; dindex++)
                    rc = ((LayoutContextSet) initmtxlayercxset(env, path, dindex)).parserefresh(env, ctx, viewPos, path.concat(-dindex - 1));

            if (paneUnitFlag)
            	env.process.restoreContext();

            return (rc);
        }

		public boolean refreshunit(Env env, Object ctx, Path path, int[] pos, int[] size, int[] clippos, int[] clipsize) {
            int unitcol = getValueInt(env, path, ContentNode.MODIFunitcol);
            boolean rc = true;

//            setclippingarea(ctx, add(pos, clippos), clipsize);

           	setColor(ctx, unitcol);

            Type.pushUIContext(this, pos, size, ctx);
            Type fn = contentNode.getContentNode().type;
            Object value = execcmd(env, path, 0);
        	Type.popUIContext();
//
//            if (value != null && unitcol != -1)
//            	((QEDProcess) ctx).fillRectangle(pos, size);

        	boolean hasx = hasContent(ContentNode.MODIFviewx);
        	boolean hasy = hasContent(ContentNode.MODIFviewy);
            int areax = hasx ? getValueInt(env, path, ContentNode.MODIFviewx) : 0;
            int areay = hasy ? getValueInt(env, path, ContentNode.MODIFviewy) : 0;
            int viewWidth = getValueInt(env, path, ContentNode.MODIFtextwidth);
            int viewHeight = getValueInt(env, path, ContentNode.MODIFtextheight);

            if (value != null && fn.isPredefined()) {
//        		if (value instanceof String && value.toString().equals("!"))
//        			value = "\u26A0";

        		int[] unitpos = createarea();

    			for (int index = 0; index < layercxs.length; index++)
    				unitpos[index] = pos[index] - (index == 0 ? areax : areay);

//    			String text = (String) value.getValue().toString();
//    			int alignmentx = getValueInt(path, ContentNode.MODIFalignx);
//    			int alignmenty = getValueInt(path, ContentNode.MODIFaligny);
//    			int expandWidth = getValueInt(path, ContentNode.MODIFexpwidth);
//    			int expandHeight = getValueInt(path, ContentNode.MODIFexpheight);
//    			int fontSize = getValueInt(path, ContentNode.MODIFfontsize);
//				int areax = ((StorageSize) ((StorageUnitLayout) layercxs[0].storageset).layermtxstorage).gettextsize(text, fontSize);
//				int areay = ((StorageSize) ((StorageUnitLayout) layercxs[1].storageset).layermtxstorage).gettextheight(text, fontSize);
//
//    			if (expandWidth != -1)
//    				areax += (size[0] - areax) * expandWidth / 100;
//
//    			if (expandHeight != -1)
//    				areay += (size[1] - areay) * expandHeight / 100;
//
//    			if (alignmentx != -1)
//    				unitpos[0] += (size[0] - areax) * alignmentx / 100;
//
//    			if (alignmenty != -1)
//    				unitpos[1] += (size[1] - areay) * alignmenty / 100;

//    			debug("Printing \"" + value + "\" + at position (" + unitpos[0] + ", " + unitpos[1] + "), size (" + size[0] + ", " + size[1] + ")");
    			fn.render(env, this, path, ctx, pos, size, unitpos, value);
    		}
    		else {
                LayoutObject sublayerset = getsubpaneset(env.topContentStorageUnit, path);

                if (sublayerset != null) {
//        			int alignmentx = getValueInt(path, ContentNode.MODIFalignx);
//        			int alignmenty = getValueInt(path, ContentNode.MODIFaligny);
//        			int expandWidth = getValueInt(path, ContentNode.MODIFexpwidth);
//        			int expandHeight = getValueInt(path, ContentNode.MODIFexpheight);
//
//        			if (expandWidth != -1)
//        				areax += (size[0] - areax) * expandWidth / 100;
//
//        			if (expandHeight != -1)
//        				areay += (size[1] - areay) * expandHeight / 100;
//
//        			if (alignmentx != -1)
//        				pos[0] += (size[0] - areax) * alignmentx / 100;
//
//        			if (alignmenty != -1)
//        				pos[1] += (size[1] - areay) * alignmenty / 100;

        			int[] subSize = sublayerset.getTotalSize();

                    if (viewWidth != -1 && hasx)
                    	size[0] = subSize[0];

                    if (viewHeight != -1 && hasy)
                    	size[1] = subSize[1];

                    LayoutContextSet sublayercxset = (LayoutContextSet) initsublayercxs(path, sublayerset, pos, size, add(pos, clippos), clipsize);//, pane);

        			rc = sublayercxset.parserefresh(env, ctx, new int[] {areax, areay});
        		}
    		}

            return (rc);
        }

        public Focus findfocus(int modifier) {
        	return findfocus(null, new Path(), modifier);
        }

        public Focus findfocus(Env env, Path path, int modifier) {
            int[] pos = createarea();
            int[] start = {0};
            int[] end = {0};
	    	Focus focus = null;

            if (getunitrange(env, path, new int[] {0, 0}, new int[1], end))
            	focus = ((LayoutContextSet) initmtxlayercxset(env, path, end[0])).findfocus(env, path.concat(-end[0] - 1), modifier);

            if (focus == null && getsetrange(new int[] {0, 0}, start, end) && start[0] == end[0])
            	focus = ((LayoutContextSet) initchildlayercxset(getcontentindex(end[0]))).findfocus(env, path.concat(getcontentindex(end[0])), modifier);

            if (focus == null && getpaneunitflag(env, path, new int[] {0, 0}, pos, createarea(), createarea(), createarea(1)))
		        focus = findfocusunit(env, path, pos, modifier);

            return (focus);
        }

	    public Focus findfocusunit(Env env, Path path, int[] pos, int modifier) {
			LayoutObject sublayerset = getsubpaneset(env.topContentStorageUnit, path);
	    	Focus focus = null;

	        if (sublayerset != null) {
				LayoutContextSet sublayercxset = (LayoutContextSet) initsublayercxs(path, sublayerset, pos, createarea(1), pos, createarea(1));

				focus = sublayercxset.findfocus(env, new Path(), modifier);
			}
//
//	        if (focus == null && contentNode.getcontent(modifier) != null)
//        		focus = new Focus(createEvent(null, path), contentNode.getContentNode());

	        return (focus);
	    }

		public boolean processevent(Env env, Event event, int modifier) {
        	return processevent(env, new Path(), new int[] {0, 0}, event, modifier);
		}

        public boolean processevent(Env env, Path path, int[] viewPos, Event event, int modifier) {
            int[] pos = createarea();
            int[] size = createarea();
            int[] clippos = createarea();
            int[] clipsize = createarea();
            int[] start = {0};
            int[] end = {0};
	    	boolean rc = true;

            if (getunitrange(env, path, viewPos, start, end))
                for (int dindex = start[0]; rc && dindex <= end[0]; dindex++)
                	rc = ((LayoutContextSet) initmtxlayercxset(env, path, dindex)).processevent(env, path.concat(-dindex - 1), viewPos, event, modifier);

            if (rc && getsetrange(viewPos, start, end))
            	for (int index = start[0]; rc && index <= end[0]; index++) {
            		int ndx = getcontentindex(index);

            		rc = ((LayoutContextSet) initchildlayercxset(ndx)).processevent(env, path.concat(ndx), viewPos, event, modifier);
            	}

            if (rc && getpaneunitflag(env, path, viewPos, pos, size, clippos, clipsize))
		        rc = processeventunit(env, path, event, pos, size, clippos, clipsize, modifier);

            return (rc);
        }

	    public boolean processeventunit(final Env env, final Path path, final Event event, int[] pos, int[] size, int[] clippos, int[] clipsize, int modifier) {
			LayoutObject sublayerset = getsubpaneset(env.topContentStorageUnit, path);
	    	boolean rc = true;

	        if (sublayerset != null) {
				LayoutContextSet sublayercxset = (LayoutContextSet) initsublayercxs(path, sublayerset, pos, size, clippos, clipsize);
	            int areax = getValueInt(env, path, ContentNode.MODIFviewx);
	            int areay = getValueInt(env, path, ContentNode.MODIFviewy);

	            sublayercxset.obj.handlerDPath = path;
				rc = sublayercxset.processevent(env, new Path(), new int[] {areax, areay}, event, modifier);
			}

	        final int expIndex = rc ? contentNode.getcontent(modifier) : 0;

	        if (expIndex != 0) {
	        	env.process.tickThread(new QEDCall(obj, expIndex, (Path) null, path, EventReturnHandler.obj, event.getParm()));
//        		focus = new Focus(createEvent(null, path), contentNode.getContentNode());
//	        	CodeNode.addExpEvent(new ExpEvent(expIndex, new Env(env.ui, obj, path, null), null, event.getParm()));
	        }

	        return (rc);
	    }
//
//	    public Msg createEvent(Msg subMsg, Path path) {
//	    	Msg msg = new Msg(path, subMsg);
//
//	    	return (msg/*parent != null ? parent.createEvent(msg, parentPath) : msg*/);
//	    }

        public SizeContextSet initsublayercxs(Path path, LayoutObject sublayerset, int[] pos, int[] size, int[] clippos, int[] clipsize) {
            SizeContextSet sublayercxset = initsublayercxs(path, sublayerset, pos);

            for (int index = LayoutNode.numdim - 1; index >= 0; index--) {
            	LayoutContext context =((LayoutContext) sublayercxset.layercxs[index]);

            	((LayoutNode) context.node).initsubcx(context, sublayerset, index, size[index], clippos[index], clipsize[index]);
            }

            return (sublayercxset);
        }

        public LayoutObject getsubpaneset(StorageUnitContent topContentStorageUnit2, Path path) {
        	LayoutObject subpaneset = null;

            for (int ndx = 0; subpaneset == null && ndx < layercxs.length; ndx++) {
            	SizeContext layercx = layercxs[ndx];

            	if (layercx.node != null && layercx.storageset != null && topContentStorageUnit != null) {
            		StorageUnitContent content = (StorageUnitContent) obj.func.contentMtxCx.getField(topContentStorageUnit, path, path);

            		subpaneset = content != null ? content.layerset : null;
            	}
            }

    		return subpaneset;
        }

        public int[] createarea() {
            return createarea(0);
        }

        public int[] createarea(int initvalue) {
            int[] newarea = new int[layercxs.length];

            for (int index = 0; index < layercxs.length; index++)
                newarea[index] = initvalue;

            return (newarea);
        }

        public boolean getunitrange(Env env, Path path, int[] viewPos, int[] start, int[] end) {
//        	boolean arrayFlag = false;
            int dsize = getdsize(path);

            start[0] = 0;
            end[0] = dsize - 1;

            for (int index = 0; start[0] <= end[0] && index < layercxs.length; index++) {
                LayoutContext layercx = (LayoutContext) layercxs[index];

                if (((LayoutNode) layercx.node).aDirFlag && layercx.node.getArrayFlag()) {
                    int diffpos = layercx.clippos + viewPos[index] - (layercx.pos + layercx.headersize);

                    ((LayoutNode) layercx.node).getunitrange(env, layercx.storageset, diffpos, dsize, layercx.clipsize, start, end);
 //                   arrayFlag = true;
            	}
            }
/*
            if (layercx.pindex != null && getlayerlayoutpaths().layerdirs.getFlag(cx)) {
                int dindex = layercx.pindex.get(0);
                boolean outofbounds = dindex < start[0] || dindex > end[0];

                start[0] = outofbounds ? 0 : dindex;
                end[0] = outofbounds ? -1 : dindex;
            }
*/
            return (/*arrayFlag && */start[0] <= end[0]);
        }

		public boolean getpaneunitflag(Env env, Path path, int[] viewPos, int[] pos, int[] size, int[] clippos, int[] clipsize) {
            boolean rc = getpaneunitflag();

            for (int index = 0; rc && index < layercxs.length; index++) {
            	LayoutContext context = (LayoutContext) layercxs[index];

            	rc = ((LayoutNode) context.node).getpaneunitflag(context, env, this, path, index, viewPos[index], pos, size, clippos, clipsize);
            }

            return (rc);
        }

        public boolean getsetrange(int[] viewPos, int[] start, int[] end) {
            start[0] = 0;
            end[0] = getnumcontentnodes() - 1;

            for (int index = 0; start[0] <= end[0] && index < layercxs.length; index++)
            	((LayoutNode) ((LayoutContext) layercxs[index]).node).getsetrange(obj.func.contentMtxCx, topContentStorageUnit, (LayoutContext) layercxs[index], viewPos[index], start, end);

            return (start[0] <= end[0]);
        }

        public static int[] add(int[] x, int[] y) {
        	int[] num = x.length == y.length ? new int[x.length] : null;

        	if (num != null)
        		for (int index = 0; index < num.length; index++)
        			num[index] = x[index] + y[index];

        	return (num);
        }

        public static int[] subtract(int[] x, int[] y) {
        	int[] num = x.length == y.length ? new int[x.length] : null;

        	if (num != null)
        		for (int index = 0; index < num.length; index++)
        			num[index] = x[index] - y[index];

        	return (num);
        }

    	public void setclippingarea(Object ctx, int[] pos, int[] size) {
   			((QEDProcess) ctx).setclippingarea(pos, size);			
    	}

    	public void setColor(Object ctx, int col) {
   			((QEDProcess) ctx).setColor(col);			
    	}
    }

    public static class Focus {
    	int[] point;
//		public Msg msg;
	    public Commandsetcontent contentset;

	    public Focus(Object/*Msg*/ msg, Commandsetcontent contentset) {
//	    	this.msg = msg;
	    	this.contentset = contentset;
	    }
    }

    public static class LayoutContext extends SizeContext {
    	public Path dindexes;
    	public int headersize; // global
    	public int bodysize; // global
        public int clippos; // global
        public int clipsize; // global
    }
/*
    public static class LayoutObject {
		public Form form;
		public QEDObj storageCode;
		public StorageUnitContent topContentStorageUnit;
    	public StorageUnitLayout[] topLayoutStorageUnits = new StorageUnitLayout[LayoutNode.numdim];

    	public LayoutObject(Form form, QEDObj storageCode) {
			this.form = form;
    		this.storageCode = storageCode;
    	}

    	public boolean init(Env env) {
    		topContentStorageUnit = (StorageUnitContent) storageCode.func.contentMtxCx.init(storageCode);
//    		if (storageCode.topContentStorageUnit == null)
//    			storageCode.topContentStorageUnit = (StorageUnitContent) storageCode.type.contentMtxCx.init(env, storageCode);
//    		else
//    			topContentStorageUnit = topContentStorageUnit;

    		for (int index = 0; index < LayoutNode.numdim; index++)
    			topLayoutStorageUnits[index] = (StorageUnitLayout) storageCode.func.layoutMtxs[index].init(env, this, index);

    		return (true);
    	}

    	public boolean uninit() {
    		boolean rc = true;

    		for (int index = LayoutNode.numdim - 1; rc && index >= 0; index--)
    			rc = storageCode.func.layoutMtxs[index].unInit(null, this, index);

    		return (rc && storageCode.func.contentMtxCx.unInit(null, storageCode, topContentStorageUnit));
    	}

    	public void resize(Env env) {
    		storageCode.func.contentMtxCx.refresh(env, storageCode, topContentStorageUnit);

    		for (int index = 0; index < LayoutNode.numdim; index++)
    			if (topLayoutStorageUnits[index] != null)
    				storageCode.func.layoutMtxs[index].parseResize(new Env(env, this, index));
    	}

    	public LayoutObject createlayersetui(QEDObj storageCode) {
    		return (new LayoutObject(form, storageCode));
    	}

		public boolean resize(UI ui) {
			if (ui != null) {
				int[] size = getTotalSize();

				ui.resize(size);
				form.refresh(new int[2], size);//(pos[0], pos[1], size[0], size[1]);
//				refresh(null, renderer, new int[2], size);
				//        	deviceform.getui().mainFrame.canvas.repaint(pos[0], pos[1], size[0], size[1]);
			}
			return (true);
		}

    	public int[] getTotalSize() {
    		int[] size = createarea();

    		for (int index = LayoutNode.numdim - 1; index >= 0; index--)
    			size[index] = getelementview(this, index) + getheaderview(this, index);

    		return (size);
    	}

    	public Focus findfocus(int[] location, int modifier) {
    		LayoutContextSet layercxset = (LayoutContextSet) initcxs(location, createarea(1));

    		return (layercxset.findfocus(modifier));
    	}

    	public boolean processevent(final Env env, Commandsetcontent contentset, int modifier, final Event event) {
			final int expIndex = contentset.getcontent(modifier);

			if (expIndex != 0)
				CodeNode.addExpEvent(new ExpEvent(expIndex, new Env(env, storageCode, new Path(), null), null, event.getParm()));

			return (true);
//    		return layerlayouts[LayoutNode.numdim - 1].processevent(msg, contentset, modifier, event);
    	}

    	public boolean processevent(Env env, int[] location, Event event, int modifier) {
    		LayoutContextSet layercxset = (LayoutContextSet) initcxs(location, createarea(1));

    		return layercxset.processevent(env, event, modifier);
    	}

		public void refresh(Env env) {
    		if (topLayoutStorageUnits[LayoutNode.numdim - 1] != null) {
        		LayoutContextSet layercxset = (LayoutContextSet) initcxs(LayoutNode.createarea(), LayoutNode.createarea(10000));

        		layercxset.parserefresh(env, form, new int[] {0, 0});
    		}
		}

    	public SizeContextSet initcxs(int[] clippos, int[] clipsize) {
    		SizeContextSet layercxset = initlayercxs();

    		for (int index = LayoutNode.numdim - 1; index >= 0; index--) {
    			((LayoutContext) layercxset.layercxs[index]).clippos = clippos[index];
    			((LayoutContext) layercxset.layercxs[index]).clipsize = clipsize[index];
    		}

    		return (layercxset);
    	}

    	public SizeContextSet initlayercxs() {
    		SizeContextSet layercxset = createlayercxset(storageCode, topContentStorageUnit, storageCode.func.contentMtx);
    		boolean rc = true;

    		for (int index = LayoutNode.numdim - 1; index >= 0; index--) {
    			layercxset.layercxs[index] = layercxset.createlayercx();
    			layercxset.layercxs[index].contentNode = layercxset.contentNode;

    			rc = layercxset.layercxs[index] != null && initlayercx((LayoutContext) layercxset.layercxs[index], this, index);
    		}

    		return (rc ? layercxset : null);
    	}

	    public SizeContextSet createlayercxset(QEDObj storageCode, StorageUnitContent topContentStorageUnit, ContentNode contentNode) {
	        return new LayoutContextSet(LayoutNode.numdim, storageCode, topContentStorageUnit, contentNode);
	    }

    	public boolean initlayercx(LayoutContext layercx, LayoutObject layoutObject, int index) {
    		layercx.node = layoutObject.storageCode.func.layoutMtxs[index];
    		layercx.storageset = layoutObject.topLayoutStorageUnits[index];
    		layercx.pos = 0;
    		layercx.dindexes = new Path();
    		layercx.headersize = getheaderview(layoutObject, index);
    		layercx.bodysize = getelementview(layoutObject, index);
    		layercx.clippos = 0;
    		layercx.clipsize = 0;

    		return (true);
    	}

    	public int getelementview(LayoutObject layoutObject, int index) {
    		return (layoutObject.storageCode.func.layoutMtxs[index].getelementview(layoutObject.topLayoutStorageUnits[index]));
    	}

    	public int getheaderview(LayoutObject layoutObject, int index) {
    		return (layoutObject.topLayoutStorageUnits[index].headerview);
    	}
    }
*/
    public static class LayoutObject {
		public QEDObj obj;
		public StorageUnitContent topContentStorageUnit;
    	public StorageUnitLayout[] topLayoutStorageUnits = new StorageUnitLayout[LayoutNode.numdim];

    	public LayoutObject(QEDObj obj) {
    		this.obj = obj;
    	}

    	public boolean init(Env env) {
    		topContentStorageUnit = (StorageUnitContent) obj.func.contentMtxCx.init(env, obj);
//    		if (storageCode.topContentStorageUnit == null)
//    			storageCode.topContentStorageUnit = (StorageUnitContent) storageCode.type.contentMtxCx.init(env, storageCode);
//    		else
//    			topContentStorageUnit = topContentStorageUnit;

    		for (int index = 0; index < LayoutNode.numdim; index++)
    			topLayoutStorageUnits[index] = (StorageUnitLayout) obj.func.layoutMtxs[index].init(env, this, index);

    		return (true);
    	}

    	public boolean uninit(Env env) {
    		boolean rc = true;

    		for (int index = LayoutNode.numdim - 1; rc && index >= 0; index--)
    			rc = obj.func.layoutMtxs[index].unInit(env, this, index);

    		return (rc && obj.func.contentMtxCx.unInit(env, obj, topContentStorageUnit));
    	}

    	public void resize(Env env) {
    		obj.func.contentMtxCx.refresh(env, obj, topContentStorageUnit);

    		for (int index = 0; index < LayoutNode.numdim; index++)
    			if (topLayoutStorageUnits[index] != null)
    				obj.func.layoutMtxs[index].parseResize(new Env(env.process, this, index));
    	}

    	public LayoutObject createlayersetui(QEDObj obj) {
    		return (new LayoutObject(obj));
    	}

		public boolean resize(QEDProcess process) {
			if (process != null) {
				int[] size = getTotalSize();

				process.resize(size);
//				call.refresh(new int[2], size);//(pos[0], pos[1], size[0], size[1]);
//				refresh(null, renderer, new int[2], size);
				//        	deviceform.getui().mainFrame.canvas.repaint(pos[0], pos[1], size[0], size[1]);
			}
			return (true);
		}

    	public int[] getTotalSize() {
    		int[] size = createarea();

    		for (int index = LayoutNode.numdim - 1; index >= 0; index--)
    			size[index] = getelementview(this, index) + getheaderview(this, index);

    		return (size);
    	}

    	public Focus findfocus(int[] location, int modifier) {
    		LayoutContextSet layercxset = (LayoutContextSet) initcxs(location, createarea(1));

    		return (layercxset.findfocus(modifier));
    	}

    	public boolean processEvent(final Env env, Commandsetcontent contentset, int modifier, final Event event) {
			int pointer = contentset.getcontent(modifier);

			if (pointer != 0) {
				event.getParm();
	        	QEDCall call = new QEDCall(obj, pointer, null, EventReturnHandler.obj, event.getParm());

	        	env.process.tickThread(call);
//				CodeObject.addExpEvent(new ExpEvent(exp, new Env(env, obj, new Path(), null), null, event.getParm()));
			}

			return (true);
//    		return layerlayouts[LayoutNode.numdim - 1].processevent(msg, contentset, modifier, event);
    	}

    	public boolean processevent(Env env, int[] location, Event event, int modifier) {
    		LayoutContextSet layercxset = (LayoutContextSet) initcxs(location, createarea(1));

    		return layercxset.processevent(env, event, modifier);
    	}

		public void refresh(Env env) {
    		if (topLayoutStorageUnits[LayoutNode.numdim - 1] != null) {
        		LayoutContextSet layercxset = (LayoutContextSet) initcxs(LayoutNode.createarea(), LayoutNode.createarea(10000));

        		layercxset.parserefresh(env, env.process, new int[] {0, 0});
    		}
		}

    	public SizeContextSet initcxs(int[] clippos, int[] clipsize) {
    		SizeContextSet layercxset = initlayercxs();

    		for (int index = LayoutNode.numdim - 1; index >= 0; index--) {
    			((LayoutContext) layercxset.layercxs[index]).clippos = clippos[index];
    			((LayoutContext) layercxset.layercxs[index]).clipsize = clipsize[index];
    		}

    		return (layercxset);
    	}

    	public SizeContextSet initlayercxs() {
    		SizeContextSet layercxset = createlayercxset(obj, topContentStorageUnit, obj.func.contentMtx);
    		boolean rc = true;

    		for (int index = LayoutNode.numdim - 1; index >= 0; index--) {
    			layercxset.layercxs[index] = layercxset.createlayercx();
    			layercxset.layercxs[index].contentNode = layercxset.contentNode;

    			rc = layercxset.layercxs[index] != null && initlayercx((LayoutContext) layercxset.layercxs[index], this, index);
    		}

    		return (rc ? layercxset : null);
    	}

	    public SizeContextSet createlayercxset(QEDObj obj, StorageUnitContent topContentStorageUnit, ContentNode contentNode) {
	        return new LayoutContextSet(LayoutNode.numdim, obj, topContentStorageUnit, contentNode);
	    }

    	public boolean initlayercx(LayoutContext layercx, LayoutObject layoutObject, int index) {
    		layercx.node = layoutObject.obj.func.layoutMtxs[index];
    		layercx.storageset = layoutObject.topLayoutStorageUnits[index];
    		layercx.pos = 0;
    		layercx.dindexes = new Path();
    		layercx.headersize = getheaderview(layoutObject, index);
    		layercx.bodysize = getelementview(layoutObject, index);
    		layercx.clippos = 0;
    		layercx.clipsize = 0;

    		return (true);
    	}

    	public int getelementview(LayoutObject layoutObject, int index) {
    		return (layoutObject.obj.func.layoutMtxs[index].getelementview(layoutObject.topLayoutStorageUnits[index]));
    	}

    	public int getheaderview(LayoutObject layoutObject, int index) {
    		return (layoutObject.topLayoutStorageUnits[index].headerview);
    	}
    }

	public static class HorizontalLayoutNode extends LayoutNode {
		public HorizontalLayoutNode(Instruction inst) {
			super(inst);
		}

	    public int getId() {
	    	return 0;
	    }

	    @Override
		public Node createNode(FunctionDeclaration function, Path path, int level, Instruction inst) {
	        return (new HorizontalLayoutNode(inst));
	    }

	    protected int getavgcharsize(QEDProcess process, int fontSize) {
			return process.getavgcharwidth(fontSize);
		}

	    protected int getscreensize(QEDProcess process) {
			return process.getscreenarea()[0];
		}

	    protected int getSize(QEDProcess process, Type fn, Object value, int fontSize) {
			return fn.getWidth(process, value, fontSize);
		}
	}

	public static class VerticalLayoutNode extends LayoutNode {
		public VerticalLayoutNode(Instruction inst) {
			super(inst);
		}

	    public int getId() {
	    	return 1;
	    }

	    @Override
		public Node createNode(FunctionDeclaration function, Path path, int level, Instruction inst) {
	        return (new VerticalLayoutNode(inst));
	    }

	    protected int getavgcharsize(QEDProcess process, int fontSize) {
			return process.getavgcharheight(fontSize);
		}

	    protected int getscreensize(QEDProcess process) {
			return process.getscreenarea()[1];
		}

	    protected int getSize(QEDProcess process, Type fn, Object value, int fontSize) {
			return fn.getHeight(process, value, fontSize);
		}
	}
}
