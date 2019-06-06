/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang;

import java.io.ByteArrayOutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Vector;

import com.hocuscodus.qed.lang.Declaration.FunctionDeclaration;
import com.hocuscodus.qed.lang.Exp.ArrayExp;
import com.hocuscodus.qed.lang.Exp.ValueExp;
import com.hocuscodus.qed.lang.type.Type;
import com.hocuscodus.qed.node.LayoutNode;
import com.hocuscodus.qed.node.LayoutNode.LayoutObject;

import com.hocuscodus.qed.java_cup_runtime.ComplexSymbolFactory;

@SuppressWarnings("serial")
public abstract class QEDProcess extends Vector<Call> {
	public Call current;
	public byte[] byteCode;

	public void init(FunctionDeclaration func, byte[] byteCode, ReturnHandler returnHandler) {
		this.byteCode = byteCode;
		push(new QEDCall(func, returnHandler));
	}

	public synchronized void tickThread(Call call) {
		push(call);
		tick();
	}

	public synchronized void evalThread(Call call) {
		push(call);
		eval();
	}

	public void push(Call call) {
		addElement(call);
		current = call;
	}

	public void remove(Call call, Object value) {
		removeCallSet(call);
		current = call.parent;


		if (current != null && value != null)
			current.stack.push(value);
	}

	public void removeCallSet(Call call) {
		for (int index = size() - 1; index >= 0; index--) {
			Call subCall = get(index);

			if (subCall.parent == call)
				removeCallSet(get(index));
			else
				if (subCall == call) {
					removeElement(call);
					break;
				}
		}
	}

	public void stop() {
		current = null;
	}

	public boolean eval() {
		while (current != null)
			current.process(this);

		return size() != 0;
    }

	public synchronized void tick() {
		eval();

		if (getFormFlag()) {
			int[] totalSize = new int[LayoutNode.numdim];

			init();

			for (int ndx = 0; ndx < size(); ndx++)
				if (get(ndx) instanceof QEDCall) {
					QEDCall call = (QEDCall) get(ndx);

					if (call.returnHandler instanceof CallReturnHandler) {
						if (call.layoutObject != null)
							call.layoutObject.uninit(new Env(this));

						call.layoutObject = new LayoutObject((QEDObj) call.obj);
						call.layoutObject.init(new Env(this));
						call.layoutObject.resize(this);

						int[] size = call.layoutObject.getTotalSize();

						for (int index = 0; index < LayoutNode.numdim; index++)
							totalSize[index] = Math.max(size[index], totalSize[index]);
					}

					int[] size = call.layoutObject.getTotalSize();

					for (int index = 0; index < LayoutNode.numdim; index++)
						totalSize[index] = Math.max(size[index], totalSize[index]);
				}

			init2(totalSize);

			for (int ndx = 0; ndx < size(); ndx++)
				if (get(ndx) instanceof QEDCall) {
					QEDCall call = (QEDCall) get(ndx);

					if (call.returnHandler instanceof CallReturnHandler)
						call.layoutObject.refresh(new Env(this, (QEDObj) call.obj));
				}

			uninit();
		}
	}

	public boolean getFormFlag() {
		boolean formFlag = false;

		for (int ndx = 0; !formFlag && ndx < size(); ndx++)
			if (get(ndx) instanceof QEDCall) {
				QEDObj obj = (QEDObj) get(ndx).obj;
				FunctionDeclaration func = obj.func;

				formFlag = func.contentMtx.getNumNodes() != 0 || (func.contentMtx.getContentNode() != null && func.contentMtx.getContentNode().getnumcontents() != 0);
			}

		return formFlag;
	}

	public boolean processEvent(int modifier, Event event) {
		//    	synchronized(this) {
		QEDCall call = (QEDCall) get(size() - 1);
//		QEDCall eventCall = new QEDCall();

		return (call.processEvent(this, modifier, event));
		//    	}
	}

	public boolean processEvent(int[] location, int modifier, Event event) {
		//    	synchronized(this) {
		QEDCall call = (QEDCall) get(size() - 1);
//		QEDCall eventCall = new QEDCall();

		return (call.processEvent(this, location, event, modifier));
		//    	}
	}

	public FunctionDeclaration parseQED(String fileName, ByteArrayOutputStream out) throws Exception {
		Reader reader = getReader(fileName);
		ComplexSymbolFactory csf = reader != null ? new ComplexSymbolFactory() : null;
		Scanner scanner = csf != null ? new Scanner(reader, csf) : null;
		QEDParser p = scanner != null ? new QEDParser(scanner, csf) : null;

		if (p != null) {
			p.context = new Context(this, out);
			return (FunctionDeclaration) p.parse().value;
		}
		else
			return null;
	}

	public boolean run(String argv[]) throws Exception {
		return run(argv, ProcessReturnHandler.obj);
	}

	public boolean run(String argv[], ReturnHandler returnHandler) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		FunctionDeclaration mainFunc = parseQED(argv[0], out);
		boolean rc = mainFunc != null;

		if (rc) {
			ArrayExp args = new ArrayExp(Type.stringType);
			args.addExp(new ValueExp(Type.stringType, argv[0]));

			for (int index = 1; index < argv.length; index++)
				args.addExp(new ValueExp(Type.stringType, argv[index]));

			ArrayList<Exp> argList = new ArrayList<Exp>();

			argList.add(args);

			byte[] byteCode = out.toByteArray();

			init(mainFunc, byteCode, returnHandler);

			tick();
		}

		return rc;
	}

	public abstract Reader getReader(String fileName);
	public abstract void init();
    public abstract void init2(int[] size);
    public abstract void uninit();
	public abstract int getavgcharwidth(int fontSize);
	public abstract int getavgcharheight(int fontSize);
	public abstract int gettextsize(String value, int fontSize);
	public abstract int gettextheight(String value, int fontSize);
	public abstract int[] getscreenarea();
	public abstract boolean resize(int[] size);
	public abstract boolean refresh(int[] pos, int[] size);
	public abstract void repaintNow();
	public abstract Object createImageBuffer();
	public abstract int getImageWidth(Object image, int fontSize);
	public abstract int getImageHeight(Object image, int fontSize);
	public abstract Object setTimer(long timer, QEDProcess process, Obj obj);
	public abstract void resetTimer(Object timerObjs);

	public abstract void setclippingarea(int[] pos, int[] size);
	public abstract void setColor(int col);
	public abstract void fillRectangle(int[] pos, int[] size);
	public abstract void fillRoundRectangle(int[] pos, int[] size, int[] arc);
	public abstract void fillOval(int[] pos, int[] size);
	public abstract void line(int[] pos1, int[] pos2);
	public abstract void line(int[] pos1, int[] pos2, int stroke);
	public abstract void drawText(String string, int[] pos, int[] size, int[] unitpos, int fontSize, int textcol);
	public abstract void drawImage(Object value, int[] pos, int[] unitpos);
	public abstract void print(String string);
	public abstract void println(String string);
	public abstract void exit(int value);
	public abstract Object loadImage(String name, QEDProcess process, Obj obj);
	public abstract void saveContext();
	public abstract void restoreContext();
}
