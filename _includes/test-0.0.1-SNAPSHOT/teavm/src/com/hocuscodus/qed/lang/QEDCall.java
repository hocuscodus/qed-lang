/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang;

import java.util.Stack;

import com.hocuscodus.qed.lang.Declaration.FunctionDeclaration;
import com.hocuscodus.qed.node.ContentNode;
import com.hocuscodus.qed.node.LayoutNode.Focus;
import com.hocuscodus.qed.node.LayoutNode.LayoutObject;

public class QEDCall extends Call {
	public int instPointer;
	public ObjEnv env;
	public Stack<AmpEntry> ampStack = new Stack<AmpEntry>();
	public Stack<Stack<Object>> localStack = new Stack<Stack<Object>>();
	public LayoutObject layoutObject;
	public Focus focus;
	public Object parm;

	public QEDCall(Call parentCall, QEDObj parent, ObjEnv parentEnv, FunctionDeclaration func, int numArgs) {
		super(parentCall, new QEDObj(parent, parentEnv, func), null);
		((QEDObj) obj).handlerCall = this;
		instPointer = func.offset;
		env = new ObjEnv(((QEDObj) obj).data);

		for (int index = numArgs - 1; index >= 0; index--)
			((QEDObj) obj).data[index] = parentCall.stack.pop();

		returnHandler = (ReturnHandler) parentCall.stack.pop();
	}

	public QEDCall(Call parentCall, QEDObj obj, ReturnHandler returnHandler) {
		super(parentCall, obj, returnHandler);
		instPointer = obj.func.offset + obj.func.cleanupOffset;
		env = new ObjEnv(((QEDObj) obj).data);
	}

	public QEDCall(FunctionDeclaration func, ReturnHandler returnHandler) {
		super(null, new QEDObj(null, null, func), returnHandler);
		env = new ObjEnv(((QEDObj) obj).data);
		instPointer = func.offset;
	}

	public QEDCall(QEDObj obj, int instPointer, Path path, ReturnHandler returnHandler, Object parm) {
		super(null, obj, returnHandler);
		env = new ObjEnv(((QEDObj) obj).data);
		this.instPointer = obj.func.offset + instPointer;
		this.parm = parm;
//
//		if (path != null)
//			for (int index = 0; index < path.getNumLevels(); index++)
//				if (path.get(index) < 0) {
//					AmpEntry entry = new AmpEntry(null, null);
//
////					env.push();
//					ampStack.push(entry);
//					entry.index = -path.get(index) - 1;
//				}

		Object[] data = ((QEDObj) obj).data;

		if (path != null)
			for (int index = 0; index < path.getNumLevels() - 1; index++) {
				int ndx = path.get(index);

				if (ndx >= 0)
					data = (Object[]) data[ndx];
				else {
					AmpEntry entry = new AmpEntry(null, null);

					data = (Object[]) data[-ndx - 1];
					env.push(data);
					ampStack.push(entry);
					entry.index = -ndx - 1;
				}
			}
	}

	public QEDCall(QEDObj obj, int instPointer, Path path, Path dPath, ReturnHandler returnHandler, Object parm) {
		super(null, obj, returnHandler);
		env = new ObjEnv(((QEDObj) obj).data);
		this.instPointer = obj.func.offset + instPointer;
		this.parm = parm;

		Object[] data = ((QEDObj) obj).data;
		int count = 0;

		if (path != null) {
			for (int index = 0; index < path.getNumLevels() - 1; index++) {
				int ndx = path.get(index);

				if (ndx >= 0)
					data = (Object[]) data[ndx];
				else {
					AmpEntry entry = new AmpEntry(null, null);

					while (dPath.get(count) >= 0)
						count++;

					int dindex = -dPath.get(count++) - 1;

					data = (Object[]) data[dindex];
					env.push(data);
					ampStack.push(entry);
					entry.index = dindex;
				}
			}

			while (count < dPath.getNumLevels()) {
				int dindex = dPath.get(count++);

				if (dindex < 0) {
					AmpEntry entry = new AmpEntry(null, null);

					ampStack.push(entry);
					entry.index = -dindex - 1;
				}
			}
		}
	}

	public QEDCall(QEDObj obj, int instPointer, ObjEnv env, Path dPath, ReturnHandler returnHandler, Object parm) {
		super(null, obj, returnHandler);
		this.env = env;
		this.instPointer = obj.func.offset + instPointer;
		this.parm = parm;

		int count = 0;

		for (int index = 0; index < env.objs.length - 1; index++) {
			AmpEntry entry = new AmpEntry(null, null);

			while (dPath.get(count) >= 0)
				count++;

			ampStack.push(entry);
			entry.index = -dPath.get(count++) - 1;
		}
	}
//
//	public void process(QEDProcess process) {
////		process.println("Instruction(" + instPointer + "): " + process.byteCode[instPointer]);
//		int opCode = read(process);
//		Type type = Type.fnList.get(opCode);
//
//		type.eval(process, this);
////		process.println("Completed successfully");
//    }

	public int read(QEDProcess process) {
//		process.println("  Read: " + process.byteCode[instPointer]);
		return process.byteCode[instPointer++];
	}

	public QEDCall getQEDCall() {
		return this;
	}

	public boolean findfocus(int[] location, int modifier) {
		focus = layoutObject.findfocus(location, modifier);

		return (focus != null);
	}

	public boolean processEvent(QEDProcess process, int modifier, Event event) {
		//	        if (focus == null)
		//        		focus = new Focus(new Msg(new Path(), null), ((ContentNode) layersetui.layerlayouts[LayoutNode.numdim - 1].storageCode.method.contentMtx.childNodes.get(0)).getContentNode());

		return (layoutObject.processEvent(new Env(process, (QEDObj) obj), ((ContentNode) layoutObject.obj.func.contentMtx.getNode(0)).getContentNode()/*focus.contentset*/, modifier, event));
	}

	public boolean processEvent(QEDProcess process, int[] location, Event event, int modifier) {
		if (layoutObject == null)
			return true;
		return layoutObject.processevent(new Env(process, (QEDObj) obj), location, event, modifier);
	}
//
//	public boolean refresh(int[] pos, int[] size) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	public void reinit(Env env) {
//		//				layersetui.refresh3(env);
//		boolean rc = layoutObject.uninit();
//
//		if (rc)
//			rc = layoutObject.init(env) && layoutObject.resize(env.process);
//	}

	public boolean resize(Env env) {
		layoutObject.resize(env);
		return true;
	}
}
