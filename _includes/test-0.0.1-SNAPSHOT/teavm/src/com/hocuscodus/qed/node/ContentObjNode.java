/*
 *    Copyright (C) 2016 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.node;

import com.hocuscodus.qed.lang.Context;
import com.hocuscodus.qed.lang.Declaration.FunctionDeclaration;
import com.hocuscodus.qed.lang.Env;
import com.hocuscodus.qed.lang.Instruction;
import com.hocuscodus.qed.lang.Path;
import com.hocuscodus.qed.lang.QEDObj;
import com.hocuscodus.qed.node.LayoutNode.LayoutObject;

public class ContentObjNode extends ContentNode {
    @Override
	public Node createNode(FunctionDeclaration function, Path path, int level, Instruction inst) {
    	return new ContentObjNode();
    }

	public void buildContentTree(Context context, Instruction topInst, Instruction inst, ContentNode contentNode, Path path) {
		Commandsetcontent content = contentNode.getContentNode();

		if (content != null && content.type != null && content.type instanceof FunctionDeclaration)
	        retrieveNode(context.getCtx().declaration, content, topInst, path, inst);

		super.buildContentTree(context, topInst, inst, contentNode, path);
	}

	@Override
	public boolean parsesubinitunits(Env env) {
        boolean rc = super.parsesubinitunits(env);

		if (rc && getUnitFlag()) {
	        QEDObj obj = (QEDObj) execcmd(env, env.path, 0);
//	        LayoutObject layerset = env.call.layoutObject;
	        LayoutObject sublayerset = new LayoutObject(obj);//layerset.createlayersetui(storageCode);

	        rc = sublayerset != null && sublayerset.init(env);

	        if (rc)
	        	((StorageUnitContent) env.currentUnit).layerset = sublayerset;
		}

        return (rc);
    }

	@Override
	public boolean parsesubuninitunits(Env env) {
        boolean rc = true;

		if (rc && getUnitFlag())
	        rc = ((StorageUnitContent) env.currentUnit).layerset.uninit(env);

        return (rc && super.parsesubuninitunits(env));
    }

	public boolean parsesubrefreshunits(Env env) {
        boolean rc = true;

    	for (int index = 0; rc && index < getNumNodes(); index++) {
    		Node node = getNode(index);

    		rc = ((ContentObjNode) node).parsesubrefreshunits(new Env(env, index));
    	}

        if (rc && getArrayFlag()) {
        	int dsize = getdsize(env);

        	for (int dindex = 0; rc && dindex < dsize; dindex++)
        		rc = ((ContentObjNode) arrayPathUnit).parsesubrefreshunits(new Env(env, -dindex - 1));
        }

		if (rc && getUnitFlag())
	        ((StorageUnitContent) env.currentUnit).layerset.resize(env);

        return (rc);
    }

    @Override
	public StorageUnit createStorageUnit(Env env) {
    	return new StorageUnitContent();
    }

	public StorageUnit init(Env env, QEDObj obj) {
		StorageUnitContent storageUnit = (StorageUnitContent) super.init(null);

		if (storageUnit != null)
			parsesubinitunits(new Env(env.process, obj, storageUnit));

		return storageUnit;
    }

    public boolean unInit(Env env, QEDObj obj, StorageUnit storageUnit) {
        return ((storageUnit == null || parsesubuninitunits(new Env(env.process, obj, (StorageUnitContent) storageUnit))) && super.unInit(env, storageUnit));
    }

	public void refresh(Env env, QEDObj obj, StorageUnit storageUnit) {
		if (storageUnit != null)
			parsesubrefreshunits(new Env(env.process, obj, (StorageUnitContent) storageUnit));
    }

    public static class StorageUnitContent extends StorageUnit {
		public LayoutObject layerset;
    }
}
