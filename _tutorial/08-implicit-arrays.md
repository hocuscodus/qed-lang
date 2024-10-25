---
title: "Implicit arrays"
permalink: /tutorial/implicit-arrays/
---

We have explained what explicit arrays were but there is another way to declare an array. In a QED expression, the spacing separator (set of spaces/tabs) is considered an **array operator**. Consider the following:

{% include sandboxframe.html qedsrc="tutorial/implicit1.qed" gui="0" height="145px" %}

As you see in the example, the implicit array `5 0` literally means "five zeroes" and the `3 "Hello"` means "three Hello strings". The left expression is the array size and the right expression is the element initializer.

You can prefix the first expression by the symbol `::` to better specify it is an array size and potentially improve readability. If you want, prior to the `::` symbol, you may define an array index for this expression. Use it in the element expression to modify the initialization value for each element.

{% include sandboxframe.html qedsrc="tutorial/implicit2.qed" gui="0" height="180px" %}

The spacing operator that separates expressions in the implicit array has the highest precedence, hence all its expressions (size and element initializer), if not unary, must be within parentheses.
{: .notice--info}

You can also define multiple dimensions by using more than one array count expressions. The rightmost expression will always be the element expression.

{% include sandboxframe.html qedsrc="tutorial/implicit3.qed" gui="0" height="120px" %}

The return type of the array expression is an array of element types (int, float, ...). If the element type is void (that includes a block of expressions delimited by braces `{}`), there is no return type, that is, it acts like a `for` loop and cannot be stored in a variable.

{% include sandboxframe.html qedsrc="tutorial/implicit4.qed" gui="0" height="230px" %}

A cool thing with implicit arrays is they support postfix `++` and `--`, which dynamically add one more element or remove the last element.

{% include sandboxframe.html qedsrc="tutorial/implicit5.qed" gui="0" height="150px" %}

This is used in the "Bouncing balls" app from the demo page to dynamically add or remove balls on the display.
{: .notice--info}

Implicit arrays will become very useful for arrays of class instances, as we'll see soon. From now on, we will use them in many examples so it is good to get familiar with them.
{: .notice--info}
