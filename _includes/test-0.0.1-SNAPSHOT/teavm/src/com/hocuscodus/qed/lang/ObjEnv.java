/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang;

public class ObjEnv {
	public Object[][] objs;

	public ObjEnv(Object[] obj) {
		objs = new Object[][] {obj};
	}

	public ObjEnv(ObjEnv parent) {
		this(parent, parent.objs.length);
	}

	public ObjEnv(ObjEnv parent, int size) {
		objs = new Object[size][];
		try {
			System.arraycopy(parent.objs, 0, objs, 0, size);
		} catch (Exception e) {
			e = e;
		}
	}

	public void push() {
		push(null);
	}

	public void push(Object[] data) {
		Object[][] newObjs = new Object[objs.length + 1][];

		System.arraycopy(objs, 0, newObjs, 0, objs.length);
		newObjs[objs.length] = data;
		objs = newObjs;
	}

	public void pop() {
		Object[][] newObjs = new Object[objs.length - 1][];

		System.arraycopy(objs, 0, newObjs, 0, objs.length - 1);
		objs = newObjs;
	}

	public void set(Object[] obj) {
		objs[objs.length - 1] = obj;
	}
}
