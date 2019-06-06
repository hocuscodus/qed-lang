/*
 *    Copyright (C) 2016 Hocus Codus Software inc.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang;

import java.util.ArrayList;

public class AttrSet {
	protected ArrayList<Attr> attrs = new ArrayList<Attr>();

	public AttrSet() {
		super();
	}

	public boolean addAttr(Attr attr) {
		int attrindex = findattr(attr.getProp(), true);
		boolean rc = getAttr(attrindex) == null || getAttr(attrindex).getProp().compareTo(attr.getProp()) != 0;

		if (rc)
			attrs.add(attrindex, attr);

		return (rc);
	}

	public boolean removeAttr(String property) {
		int foundIndex = findattr(property, false);

		if (foundIndex == -1)
			return false;

		return (removeAttr(foundIndex));
	}

	// Deletes the attribute pointed to by its index in the list
	public boolean removeAttr(int index) {
		Attr attr = attrs.remove(index);
		boolean rc = attr != null;

		if (rc)
			;//     delete attr;

		return (rc);
	}

	public int findattr(String prop, boolean locationflag) {
		int remainder;
		int comp = 1;
		int start = 0;
		int limit = attrs.size();

		while (limit != 0 && comp != 0) {
			remainder = limit & 1;
			limit >>= 1;
			comp = prop.compareTo(getAttr(start + limit).getProp());

			if (comp > 0) {
				start += limit + 1L;

				if (remainder == 0)
					limit--;
			}
		}

		return (locationflag || comp == 0 ? start + limit : -1);
	}

	public int getNumAttrs() {
		return (attrs.size());
	}

	public Attr getAttr(int index) {
		return (index >= 0 && index < attrs.size() ? attrs.get(index) : null);
	}

	public int findAttr(String prop) {
		return findattr(prop, false);
	}

	public void clear() {
		attrs.clear();
	}

	public static final class Attr {
		public Path path;
		String property = null;
		Exp expr;

		public Attr(String property, Exp expr, Path path) {
			this.property = property;
			this.expr = expr;
			this.path = path;
		}

		public String getProp() {
			return property;
		}

		public Exp getExp() {
			return expr;
		}

		public Path getPath() {
			return path;
		}
	}
}
