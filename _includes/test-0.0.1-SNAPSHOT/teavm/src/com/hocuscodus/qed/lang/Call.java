/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang;

import java.util.Stack;

import com.hocuscodus.qed.lang.type.Type;

public class Call {
	public Call parent;
	public Stack<Object> stack = new Stack<Object>();
	public Object obj;
	protected ReturnHandler returnHandler;

	public Call(Call parent, Object obj, ReturnHandler returnHandler) {
		this.parent = parent;
		this.obj = obj;
		this.returnHandler = returnHandler;
	}

	public void process(QEDProcess process) {
//		int instPointer = getQEDCall().instPointer;
		int opCode = read(process);
		Type type = Type.fnList.get(opCode);

//		process.println("Instruction(" + instPointer + "): " + type.getClass().getSimpleName());
		type.eval(process, this);
//		process.println("Completed successfully");
    }

	public int read(QEDProcess process) {
//		process.println("  Read: " + process.byteCode[instPointer]);
		return -1;
	}

	public void read(QEDProcess process, byte[] array) {
		int length = array.length;

		for (int index = 0; index < length; index++)
			array[index] = (byte) read(process);
	}

	public short readS(QEDProcess process) {
		short value = 0;

		for (int index = 0; index < 2; index++) {
			value <<= 8;
			value |= read(process) & 0xFF;
		}

		return value;
	}

	public int readI(QEDProcess process) {
		int value = 0;

		for (int index = 0; index < 4; index++) {
			value <<= 8;
			value |= read(process) & 0xFF;
		}

		return value;
	}

	public long readL(QEDProcess process) {
		long value = 0;

		for (int index = 0; index < 8; index++) {
			value <<= 8;
			value |= read(process) & 0xFF;
		}

		return value;
	}

	public QEDCall getQEDCall() {
		return parent.getQEDCall();
	}

	public void onPause(QEDProcess process) {
		returnHandler.onPause(process, this);
	}

	public void onHalt(QEDProcess process) {
		returnHandler.onHalt(process, this);
	}

	public void onReturn(QEDProcess process, Object value) {
		returnHandler.onReturn(process, this, value);
	}
}
