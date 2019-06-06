/*
 *    Copyright (C) 2016 Hocus Codus Software inc.
 *
 *    Author: Martin Savage (msavage@hocuscodus.com)
 */

package com.hocuscodus.qed.lang;

public final class Path {
    protected int[] values;

    public Path() {
        values = new int[0];
    }

    public Path(int value) {
        values = new int[] {value};
    }

    public Path(final Path source) {
    	set(source);
    }

    public static final Path zero(int pathCount) {
        Path newPath = new Path();
        newPath.values = new int[pathCount];

        return (newPath);
    }

    public final int getNumLevels() {
        return values.length;
    }

    public final int get(int index) {
        return (/*index < 0 || index >= values.length ? 0 : */values[index]);
    }

    public void set(int index, int value) {
        values[index] = value;
    }

    public void set(final Path source) {
        int numpath = source.values.length;

        values = new int[numpath];

        for (int index = 0; index < numpath; index++)
            values[index] = source.values[index];
    }

    public Path concat(Path path) {
        int index;
        Path result = zero(values.length + path.values.length);

        for (index = values.length - 1; index >= 0 ; index--)
            result.values[index] = values[index];

        for (index = path.values.length - 1; index >= 0; index--)
            result.values[index + values.length] = path.values[index];

        return (result);
    }

    public boolean equals(Path path) {
        boolean rc = path.getNumLevels() == getNumLevels();

        for (int index = 0; rc && index < getNumLevels(); index++)
            rc = path.get(index) == get(index);

        return (rc);
    }

    public Path concat(int path) {
        int length = values.length;
        Path result = zero(length + 1);

        result.values[length] = path;

        while (length-- > 0)
            result.values[length] = values[length];

        return (result);
    }

    public Path trim(int index) {
      int numpath = getNumLevels();

      return (index >= 0 && index <= numpath ? trim(index, numpath - index) : new Path(this));
    }

    public Path trim(int startIndex, int range) {
        int ndx;
        int numpath = values.length;

        if (startIndex < 0) {
            range += startIndex;
            startIndex = 0;
        }

        if (startIndex + range > numpath)
            range = numpath - startIndex;

        Path resized = zero(numpath - range);
        for (ndx = 0; ndx < startIndex; ndx++)
            resized.values[ndx] = values[ndx];

        for (ndx = startIndex + range; ndx < numpath; ndx++)
            resized.values[ndx - range] = values[ndx];

        return (resized);
    }

    @Override
	public String toString() {
        StringBuffer buffer = new StringBuffer();

        for (int index = 0; index < getNumLevels(); index++) {
            if (index > 0)
                buffer.append(" ");
            buffer.append(get(index));
        }

        return ("(" + buffer + ")");
    }
}
