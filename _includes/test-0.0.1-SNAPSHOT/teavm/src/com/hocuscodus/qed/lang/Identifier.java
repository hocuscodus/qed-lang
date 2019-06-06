/*
 *    Copyright (C) 2018 Hocus Codus Software inc.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang;

import java.util.List;

public class Identifier {
	private List<String> strings;
	private List<Exp> args;

	public Identifier(List<String> strings) {
		this(strings, null);
	}

	public Identifier(List<String> strings, List<Exp> args) {
		this.strings = strings;
		this.args = args;
	}

	public int getNumStrings() {
		return strings.size();
	}

	public String getString(int index) {
		return strings.get(index);
	}

	public int getNumArgs() {
		return isFunction() ? args.size() : 0;
	}

	public Exp getArg(int index) {
		return args.get(index);
	}

	public boolean isFunction() {
		return args != null;
	}

	public String getType() {
		return isFunction() ? "function" : "variable";
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();

		for (int index = 0; index < strings.size(); index++) {
			if (index != 0)
				buffer.append('.');

			buffer.append(strings.get(index));
		}

		if (isFunction()) {
			buffer.append('(');

			for (int index = 0; index < args.size(); index++) {
				if (index != 0)
					buffer.append(", ");

				buffer.append(args.get(index).toString());
			}

			buffer.append(')');
		}

		return buffer.toString();
	}
}
