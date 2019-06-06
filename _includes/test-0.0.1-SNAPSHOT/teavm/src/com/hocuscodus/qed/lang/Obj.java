/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang;

public class Obj {
	public Call handlerCall;
	public ObjEnv handlerEnv;
	public Path handlerDPath;
	public int handlerOffset = -1;

	public void onReturn(QEDProcess process, Object value) {
		if (handlerOffset != -1) {
			// get event handler from path
//			int handlerOffset = handlerObj.eventHandlers.getEventHandler(handlerPath);
//
//			// trigger event
//			if (handlerOffset != -1) {
				process.push(new QEDCall((QEDObj) handlerCall.obj, handlerOffset, handlerEnv, handlerDPath, EventReturnHandler.obj, value));
//				process.push(new QEDCall((QEDObj) handlerCall.obj, handlerOffset, new ObjEnv(handlerCall.env), handlerDPath, EventReturnHandler.obj, value));
//				process.tick();
//			}
		}
		else
			handlerCall.onReturn(process, value);
//			for (int index = 0; index < process.size(); index++) {
//				Call call = process.get(index);
//
//				if (call.obj == this) {
//					if (call != handlerCall)
//						System.out.println("not equal");
//					call.onReturn(process, value);
////					process.eval();
//					break;
//				}
//			}
	}
}
