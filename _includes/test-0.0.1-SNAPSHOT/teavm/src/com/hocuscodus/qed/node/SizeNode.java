/*
 *    Copyright (C) 2016 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.node;

import com.hocuscodus.qed.lang.Declaration.FunctionDeclaration;
import com.hocuscodus.qed.lang.Env;
import com.hocuscodus.qed.lang.Instruction;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.QEDObj;
import com.hocuscodus.qed.node.ContentObjNode.StorageUnitContent;
import com.hocuscodus.qed.node.LayoutNode.LayoutObject;
import com.hocuscodus.qed.node.LayoutNode.StorageUnitLayout;

public class SizeNode extends ContentNode {
	public boolean constantFlag = true;
	public boolean setflag;
	public Path path;

    public SizeNode(boolean setflag) {
        this.setflag = setflag;
    }

    public Node retrieveNode(FunctionDeclaration function, Object unit, Instruction topInst, Path path, Instruction inst) {
    	SizeNode node = (SizeNode) super.retrieveNode(function, unit, topInst, path, inst);

		if (node != null)
			node.path = path;

		return node;
    }

	public Node setArrayPathUnit(FunctionDeclaration function, Instruction inst, Path path, int level) {
		SizeNode sizeNode = (SizeNode) super.setArrayPathUnit(function, inst, path, level);

		if (sizeNode != null)
			constantFlag &= level == path.getNumLevels() || path.get(level) != -1;

		return (sizeNode);
	}

    @Override
	public Node createNode(FunctionDeclaration function, Path path, int level, Instruction inst) {
        return (new SizeNode(inst.setflag));
    }

    @Override
	public int getdsize(Env env) {
        return isConstant(env) ? 1 : super.getdsize(env);
    }

	public boolean isConstant(Env env) {
        return getArrayFlag() && constantFlag;
    }

    @Override
    public String getheader() {
    	return (super.getheader() + (getArrayFlag() ? " C:" + (constantFlag ? 1 : 0) : ""));
    }

    @Override
	public StorageUnit createStorageUnit(Env env) {
    	return new StorageUnitSize();
    }

    @Override
	public StorageUnitList createStorageUnitList(char strChar) {
    	return new SizeStorageUnitList(strChar);
    }

    public StorageUnitList createChildStorageUnits() {
    	return new SizeStorageUnitList('|');
    }

	public boolean parseResize(Env env) {
        boolean rc = true;

    	if (rc) {
    		StorageUnitSize storageUnit = (StorageUnitSize) env.currentUnit;

    		storageUnit.mtxpaneset = 0;

    		if (getArrayFlag()) {
        		int dsize = getdsize(env);
        		SizeStorageUnitList sizeStorageSetArray = (SizeStorageUnitList) storageUnit.arrayStorageUnits;

            	for (int subdindex = 0; subdindex < dsize; subdindex++) {
            		((SizeNode) arrayPathUnit).parseResize(new Env(env, -subdindex - 1));
            		addmtx(env, sizeStorageSetArray, dsize, subdindex);
            	}

            	setmtxsize(env, storageUnit, dsize, super.getdsize(env));
        	}

            int numcxsets = getNumNodes();

        	for (int index = 0; index < numcxsets; index++) {
        		int[] mtxpaneset = ((SizeStorageUnitList) storageUnit.childStorageUnits).mtxpanesets;
        		int subpaneset = index != 0 ? mtxpaneset[index - 1] : 0;

       			((SizeNode) getNode(index)).parseResize(new Env(env, index));
        		mtxpaneset[index] = addchild(storageUnit, index, subpaneset);
            	Node.log3("accumulating to " + subpaneset + " units for index " + index + ", for a total of " + mtxpaneset[index] + " units.");
        	}

        	setsubsize(storageUnit, numcxsets != 0 ? ((SizeStorageUnitList) storageUnit.childStorageUnits).mtxpanesets[numcxsets - 1] : 0);

        	if (getUnitFlag())
        		addlabelsize(storageUnit, getunitsize(env.obj.func.contentMtxCx, env.topContentStorageUnit, storageUnit, convertPath(env.path)));
    	}

        return (rc);
    }

    public void addmtx(Env env, SizeStorageUnitList sizeStoragesetarray, int dsize, int dindex) {
		StorageUnitSize storageset = (StorageUnitSize) sizeStoragesetarray.storageUnits[dindex];

		sizeStoragesetarray.mtxpanesets[dindex] = gettotalview(storageset) + (dindex != 0 ? sizeStoragesetarray.mtxpanesets[dindex - 1] : 0);
		Node.log3("adding for dindex " + dindex + ", for a total of " + sizeStoragesetarray.mtxpanesets[dindex] + " mtx units.");
    }

    public void setmtxsize(Env env, StorageUnitSize storageUnit, int dsize, int realdsize) {
		SizeStorageUnitList sizeStorageSetArray = (SizeStorageUnitList) storageUnit.arrayStorageUnits;
		int unitpaneset = dsize != 0 ? sizeStorageSetArray.mtxpanesets[dsize - 1] : 0;

		storageUnit.mtxpaneset = constantFlag ? mult(realdsize, unitpaneset) : unitpaneset;
    }

    public int addchild(StorageUnitSize storageUnit, int index, int previousarea) {
		return gettotalview((StorageUnitSize) storageUnit.childStorageUnits.storageUnits[index]) + previousarea;
    }

    public void setsubsize(StorageUnitSize storageUnit, int subsize) {
    	storageUnit.mtxpaneset += subsize;
    }

    public void addlabelsize(StorageUnitSize storageUnit, int labelsize) {
    	storageUnit.mtxpaneset += labelsize;
    }

    public int mult(int dsize, int size) {
//		Storage.log3("Multiplying constant size " + size + " by " + dsize + " = " + size * dsize);
        return (size * dsize);
    }

    public int getconstantpos(StorageUnitSize storageUnit, int dindex) {
        int totalpaneset = gettotalview(storageUnit);

		return mult(dindex, totalpaneset);
    }

	public Path convertPath(Path path) {
		return path;
    }

	public int gettotalview(StorageUnitSize storageUnit) {
    	return (getelementview(storageUnit));
    }

    public int getelementview(StorageUnitSize storageUnit) {
        return (storageUnit.mtxpaneset);
    }

    public int getunitsize(ContentObjNode content, StorageUnitContent topContentStorageUnit, StorageUnitSize storageUnit, Path path) {
    	LayoutObject sublayerset = ((StorageUnitContent) content.getField(topContentStorageUnit, path, path)).layerset;

        return (sublayerset != null ? gettotalview(sublayerset, getId()) : -1);
    }

	public int gettotalview(LayoutObject layoutObject, int index) {
		return (layoutObject.obj.func.layoutMtxs[index].gettotalview(layoutObject.topLayoutStorageUnits[index]));
	}

    public int getheadersize(ContentObjNode content, StorageUnitContent topContentStorageUnit, StorageUnitSize storageUnit, Path path) {
    	return getunitsize(content, topContentStorageUnit, storageUnit, path);
    }

    public int getoffset(StorageUnitSize storageUnit, int index) {
    	return (index > 0 && index <= getNumNodes() ? ((SizeStorageUnitList) storageUnit.childStorageUnits).mtxpanesets[index - 1] : 0);
    }

    public int getdpos(Env env, StorageUnitSize storageUnit, int dsize, int dindex) {
    	int dpos = 0;//layercxs[index].storageset.mtxpaneset;

    	if (dindex > 0 && getArrayFlag()) {
    		SizeStorageUnitList sizeStorageSetArray = (SizeStorageUnitList) storageUnit.arrayStorageUnits;

    		if (constantFlag)
    			dpos += ((SizeNode) getArrayPathUnit()).getconstantpos((StorageUnitSize) sizeStorageSetArray.storageUnits[0], dindex);
    		else
    			dpos += sizeStorageSetArray.mtxpanesets[dindex - 1];
    	}

        return (dpos);
    }

    public SizeContext initlayercx(SizeContext sizeContext, SizeContextSet layercxset) {
    	SizeContext sublayercx = layercxset.createlayercx();

		sublayercx.pos = sizeContext.pos;
    	sublayercx.node = sizeContext.node;
    	sublayercx.storageset = sizeContext.storageset;

    	return (sublayercx);
    }

    public boolean initsubcx(SizeContext sizeContext, LayoutObject layoutObject, int index, int pos) {
    	sizeContext.node = layoutObject.obj.func.layoutMtxs[index];
    	sizeContext.storageset = layoutObject.topLayoutStorageUnits[index];
    	sizeContext.pos = pos;

        return (true);
    }

    public boolean initchildlayercx(ContentObjNode content, StorageUnitContent topContentStorageUnit, SizeContext sizeContext, int ndx, SizeContext sublayercx) {
    	int ndx2 = getcontentndx(ndx);

    	if (ndx2 != -1) {
        	sublayercx.node = (SizeNode) sizeContext.node.getNode(ndx2);
    		sublayercx.storageset = (StorageUnitSize) sizeContext.storageset.childStorageUnits.storageUnits[ndx2];
   			sublayercx.pos = getcpos(content, topContentStorageUnit, sizeContext, sublayercx.pos, ndx2);
    	}

        return (true);
    }

    public int getcpos(ContentObjNode content, StorageUnitContent topContentStorageUnit, SizeContext sizeContext, int pos, int ndx) {
    	int ndx2 = ndx;//getcontentndx(ndx);

        return (pos + (ndx2 != -1 ? getheadersize(content, topContentStorageUnit, sizeContext.storageset, new Path()) + getoffset(sizeContext.storageset, ndx2) : 0));
    }

    public boolean initmtxlayercx(Env env, ContentObjNode content, StorageUnitContent topContentStorageUnit, SizeContext sizeContext, int dindex, int dsize, int numNodes, SizeContext sublayercx) {
    	sublayercx.node = (SizeNode) sizeContext.node.getArrayPathUnit();
    	sublayercx.storageset = (StorageUnitSize) sizeContext.storageset.arrayStorageUnits.storageUnits[constantFlag ? 0 : dindex];
    	sublayercx.pos += getheadersize(content, topContentStorageUnit, sizeContext.storageset, new Path()) + /*storageset.getoffset(getNumNodes() + */getdpos(env, sizeContext.storageset, dsize, dindex);

        return (true);
    }

	public void getpaneunitflag(SizeContext sizeContext, int index, int[] pos) {
        pos[index] = sizeContext.pos;
	}

	public StorageUnit init(Env env, LayoutObject layoutObject, int index) {
		layoutObject.topLayoutStorageUnits[index] = (StorageUnitLayout) super.init(null);

		if (layoutObject.topLayoutStorageUnits[index] != null) {
    		parsesubinitunits(new Env(env.process, layoutObject, index));
    		parseResize(new Env(env.process, layoutObject, index));
    	}

    	Node.log3("TOTAL: " + gettotalview(layoutObject, index) + "\n");
    	Node.log3("MAP:\n" + layoutObject.topLayoutStorageUnits[index]);

    	return (layoutObject.topLayoutStorageUnits[index]);
    }

    public boolean unInit(Env env, LayoutObject layoutObject, int index) {
        return ((layoutObject.topLayoutStorageUnits[index] == null || parsesubuninitunits(new Env(env.process, layoutObject, index))) && super.unInit(env, layoutObject.topLayoutStorageUnits[index]));
    }

    @Override
    public boolean initArray(StorageUnitList arrayStorageUnits, int dsize) {
    	boolean rc = super.initArray(arrayStorageUnits, dsize);

    	if (rc) {
    		SizeStorageUnitList unitArray = (SizeStorageUnitList) arrayStorageUnits;

    		unitArray.mtxpanesets = unitArray.storageUnits != null ? createpanesetarray(dsize) : null;
    		rc = dsize == 0 || unitArray.mtxpanesets != null;
    	}

    	return (rc);
    }

    public int[] createpanesetarray(int size) {
        int[] areas = size != 0 ? new int[size] : null;

        if (areas != null)
            for (int ndx = 0; ndx < size; ndx++)
                areas[ndx] = 0;

        return areas;
    }

    public static class SizeStorageUnitList extends StorageUnitList {
		public int[] mtxpanesets;

        public SizeStorageUnitList(char strChar) {
			super(strChar);
		}
    }

    public static class StorageUnitSize extends StorageUnit {
        public int mtxpaneset;
    }

    public static class SizeContext {
    	public SizeNode node;
    	public StorageUnitSize storageset;
        public int pos;
        public ContentNode contentNode;
    }

    public static class SizeContextSet {
    	public static final boolean printFlag = true;

    	public ContentNode contentNode;
    	public QEDObj obj;
    	public StorageUnitContent topContentStorageUnit;
        public SizeContext[] layercxs;

        public SizeContextSet(int size, QEDObj obj, StorageUnitContent topContentStorageUnit, ContentNode contentNode) {
            layercxs = new SizeContext[size];
    		this.contentNode = contentNode;
    		this.obj = obj;
    		this.topContentStorageUnit = topContentStorageUnit;
        }

        public SizeContext createlayercx() {
            return new SizeContext();
        }

    	public SizeContextSet createlayercxset(QEDObj obj, StorageUnitContent topContentStorageUnit, ContentNode contentNode) {
    		return new SizeContextSet(LayoutNode.numdim, obj, topContentStorageUnit, contentNode);
    	}

        public SizeContextSet initlayercxset() {
            SizeContextSet sublayercxset = createlayercxset(obj, topContentStorageUnit, contentNode);
            boolean rc = true;

            for (int ndx = 0; rc && ndx < layercxs.length; ndx++) {
                sublayercxset.layercxs[ndx] = layercxs[ndx].node.initlayercx(layercxs[ndx], this);
            	sublayercxset.layercxs[ndx].contentNode = sublayercxset.contentNode;
                rc = sublayercxset.layercxs[ndx] != null;
            }

            return (rc ? sublayercxset : null);
        }

        public SizeContextSet initchildlayercxset(int index) {
            SizeContextSet sublayercxset = initlayercxset();
            boolean rc = true;

//            sublayercxset.initchildstorageset(index);
            sublayercxset.contentNode = null;

	    	if (contentNode != null) {
				int ndx = contentNode.getcontentndx(index);

				if (ndx != -1) {
					sublayercxset.contentNode = (ContentNode) contentNode.getNode(ndx);

					for (int ndx1 = 0; rc && ndx1 < layercxs.length; ndx1++)
						sublayercxset.layercxs[ndx1].contentNode = sublayercxset.contentNode;
				}
	    	}

            for (int ndx = 0; rc && ndx < layercxs.length; ndx++)
                rc = layercxs[ndx].node.initchildlayercx(obj.func.contentMtxCx, topContentStorageUnit, layercxs[ndx], index, sublayercxset.layercxs[ndx]);

//            debug("Going to child " + index + ": (" + sublayercxset.layercxs[0].pos + ", " + sublayercxset.layercxs[1].pos + ")");
            return (rc ? sublayercxset : null);
        }

		public SizeContextSet initmtxlayercxset(Env env, Path path, int dindex) {
            int dsize = getdsize(path);
            SizeContextSet sublayercxset = initlayercxset();
            boolean rc = true;

            sublayercxset.contentNode = null;

            if (contentNode != null) {
            	sublayercxset.contentNode = /* && dindex >= 0 ? */(ContentNode) contentNode.getArrayPathUnit();// : null;

            	for (int ndx1 = 0; rc && ndx1 < layercxs.length; ndx1++)
            		sublayercxset.layercxs[ndx1].contentNode = sublayercxset.contentNode;
            }

            for (int index = 0; rc && index < layercxs.length; index++)
                rc = layercxs[index].node.initmtxlayercx(env, obj.func.contentMtxCx, topContentStorageUnit, layercxs[index], dindex, dsize, getnumcontentnodes(), sublayercxset.layercxs[index]);

//            debug("Going to dindex " + dindex + ": (" + sublayercxset.layercxs[0].pos + ", " + sublayercxset.layercxs[1].pos + ")");
			return rc ? sublayercxset : null;
		}

        public SizeContextSet initsublayercxs(Path path, LayoutObject sublayerset, int[] pos) {
            SizeContextSet sublayercxset = createlayercxset(sublayerset.obj, sublayerset.topContentStorageUnit, sublayerset.obj.func.contentMtx);
            boolean rc = true;

            for (int index = LayoutNode.numdim - 1; index >= 0; index--) {
                sublayercxset.layercxs[index] = createlayercx();
            	sublayercxset.layercxs[index].contentNode = sublayercxset.contentNode;

                rc = sublayercxset.layercxs[index] != null && sublayerset.obj.func.layoutMtxs[index].initsubcx(sublayercxset.layercxs[index], sublayerset, index, pos[index]);
            }

            return (rc ? sublayercxset : null);
        }

    	public int getdsize(Path path) {
    		return (contentNode != null ? (int) contentNode.getdsize(new Env(null, obj, path, null, null)) : -1);
        }

	    public boolean getpaneunitflag() {
			return contentNode != null && contentNode.getUnitFlag();
		}

		public int getnumcontentnodes() {
			return contentNode.getNumNodes();
		}

		public int getcontentindex(int index) {
			return contentNode.getcontentindex(index);
		}

		public boolean hasContent(int index) {
	        return contentNode.getPointer(index) != 0;
        }

		public Object execcmd(Env env, Path path, int index) {
	        return contentNode.execcmd(new Env(env.process, obj, path, obj.func.topMtx, null), path, index);
        }

        public int getValueInt(Env env, Path path, int index) {
        	Object val = execcmd(env, path, index);

            return (val != null && val instanceof Integer ? (Integer) val : val != null && val instanceof String ? Integer.parseInt((String) val) : -1);
        }

		public void debug(String str) {
			if (printFlag)
				Node.log3(str);
		}
    }
}
