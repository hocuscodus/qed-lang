/*
 *    Copyright (C) 2016 Hocus Codus Software inc.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang;

import com.hocuscodus.qed.node.ContentNode.StorageUnit;
import com.hocuscodus.qed.node.ContentObjNode.StorageUnitContent;
import com.hocuscodus.qed.node.LayoutNode.LayoutObject;
import com.hocuscodus.qed.node.Node;

public class Env {
	public QEDProcess process;
	public QEDObj obj;
	public StorageUnitContent topContentStorageUnit;
	public Path path;
	public Object parm;
	public Node currentNode;
    public StorageUnit currentUnit;

    public Env(QEDProcess process) {
		this(null, null, new Path(), null, null);
		this.process = process;
	}

    public Env(QEDProcess process, QEDObj obj) {
		this(null, null, new Path(), null, null);
		this.process = process;
		this.obj = obj;
	}

	public Env(Env env, int index) {
		this(env.process, env.topContentStorageUnit, env.obj, env.path.concat(index >= 0 ? env.currentNode.getNode(index).index : index),
			 index >= 0 ? env.currentNode.getNode(index) : env.currentNode.arrayPathUnit,
			 (StorageUnit) (index >= 0 ? env.currentUnit.childStorageUnits.storageUnits[index] : env.currentUnit.arrayStorageUnits.storageUnits[-index - 1]),
			 null);
	}

	public Env(QEDProcess process, QEDObj obj, Path path, Node currentNode, StorageUnit currentUnit) {
		this(process, null, obj, path, currentNode, currentUnit, new Event());
	}

	public Env(QEDProcess process, QEDObj obj, StorageUnitContent topContentStorageUnit) {
		this(process, obj, new Path(), obj.func.contentMtxCx, topContentStorageUnit);
		this.topContentStorageUnit = topContentStorageUnit;
	}

	public Env(QEDProcess process, LayoutObject layoutObject, int index) {
		this(process, layoutObject.obj, new Path(), layoutObject.obj.func.layoutMtxs[index], layoutObject.topLayoutStorageUnits[index]);
		this.topContentStorageUnit = layoutObject.topContentStorageUnit;
	}

	public Env(QEDProcess process, QEDObj obj, Path path, Object parm) {
		this(process, null, obj, path, obj.func.topMtx.getNode(path), null/*(StorageUnitArray) obj.func.topMtx.getField(obj.topStorageUnit, path/ *.trim(level)* /)*/, parm);
	}

	public Env(QEDProcess process, StorageUnitContent topContentStorageUnit, QEDObj obj, Path path, Node currentNode, StorageUnit currentUnit, Object parm) {
		this.topContentStorageUnit = topContentStorageUnit;
		this.obj = obj;
		this.path = path;
		this.currentNode = currentNode;
		this.currentUnit = currentUnit;
		this.parm = parm;
		this.process = process;
	}

    public Env addPath(int index) {
    	path = path.concat(index);
    	currentUnit = (StorageUnit) currentUnit.childStorageUnits.storageUnits[index];
    	return this;
	}

	public Env addArrayPath(int index) {
    	path = path.concat(-index - 1);
    	currentUnit = (StorageUnit) currentUnit.arrayStorageUnits.storageUnits[index];
    	return this;
	}

    public Object getParm() {
    	return parm;
	}
//
//	public void callRefresh() {
//		ui.refresh(this);
//	}
}
