---
title: "Statements"
permalink: /tutorial/statements/
---

Statements in QED are delimited by a newline character or by a semicolon (which allows multiple statements to share one line).

{% include sandboxframe.html qedsrc="tutorial/statements1.qed" gui="0" height="100px" %}

Depending on your preference, you may add one or more semicolons at the end of a line without any adverse effect.

You can split a statement into multiple lines but be careful though; because semicolons do not mark the end of a statement, there are rules to follow. For instance, when splitting a binary expression, put the operator at the end of the first line, not at the beginning of the second line, like this:

{% include sandboxframe.html qedsrc="tutorial/statements2.qed" gui="0" height="200px" %}

Here, on the second line, if the plus sign was moved on the next line ("+ 5"), QED would split this statement into two statements, namely `println("3 + 5 = " + (3` and `+5` (also, an error would be generated for the first statement). So operators must end one line to indicate the statement continues on the next line.
