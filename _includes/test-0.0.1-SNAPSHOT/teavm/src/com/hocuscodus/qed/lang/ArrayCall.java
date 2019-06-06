/*
 *    Copyright (C) 2018 Hocus Codus Software inc. All rights reserved.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang;

public class ArrayCall extends Call {
	public Call trueParent;
	public int index = 0;

	public ArrayCall(Call trueParent, Call parent, int size, ReturnHandler returnHandler) {
		super(parent, new Object[size], returnHandler);
		this.trueParent = trueParent;
	}
/*
	public void process(QEDProcess process) {
		Object[] array = (Object[]) obj;
		int opCode = read(process);

			if (opCode == Type.initArrayValueFn.opCode) {
				array[index++] = stack.pop();

				if (index == array.length) {
					onReturn(process, index != 0 ? array[index - 1] : null);
					process.current = trueParent;
				}
			}
			else
				if (opCode == Type.arrayType.opCode) {
					QEDCall qedCall = (QEDCall) parent;

					process.push(new ArrayCall(this, qedCall, readI(process), (ReturnHandler) stack.pop()));
				}
				else {
					Type type = Type.fnList.get(opCode);

					type.eval(process, this);
				}
    }
*/
	public int read(QEDProcess process) {
		return parent.read(process);
	}
}
