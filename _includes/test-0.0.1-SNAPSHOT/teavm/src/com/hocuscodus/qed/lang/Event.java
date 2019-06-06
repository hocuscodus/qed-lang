/*
 *    Copyright (C) 2016 Hocus Codus Software inc.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang;


public class Event {
	public Object getParm() {
		return null;
	};

	public static class MouseEvent extends Event {
		public int[] pos;

		public MouseEvent() {
			this(new int[] {0, 0});
		}

		public MouseEvent(int[] pos) {
			this.pos = pos;
		}

		public Object getParm() {
			return new Integer(pos[1]);
		};
	}

	public static class KeyEvent extends Event {
		public char chr;

		public KeyEvent() {
			this((char) 0);
		}

		public KeyEvent(char chr) {
			this.chr = chr;
		}

		public Object getParm() {
			return new Character(chr);
		};
	}

	public static class KeyCode extends Event {
		public int code;

		public KeyCode() {
			this(0);
		}

		public KeyCode(int code) {
			this.code = code;
		}

		public Object getParm() {
			return new Integer(code);
		};
	}
};
