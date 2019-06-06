/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang;

public class AmpEntry {
	public Object[] dataIndex;
	public Path loopIndex;
	public int index;

	public AmpEntry(Object[] dataIndex, Path loopIndex) {
		this.dataIndex = dataIndex;
		this.loopIndex = loopIndex;
	}
}
