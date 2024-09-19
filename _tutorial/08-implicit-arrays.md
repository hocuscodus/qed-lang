---
title: "Implicit arrays"
permalink: /tutorial/implicit-arrays/
---

We have explained what explicit arrays were but there is another way to declare an array. In a QED expression, the spacing separator (set of spaces/tabs) is considered an **array operator**. Consider the following:

{% include sandboxframe.html qedsrc="tutorial/implicit1.qed" gui="0" height="120px" %}

As you see in the implicit array `5 0`, the left expression `5` is the array count and the right expression `0` is the array element initializer expression. You can prefix the array count expression by an index name, followed by `::` to define an array index for this expression, then use it in the element expression.

{% include sandboxframe.html qedsrc="tutorial/implicit2.qed" gui="0" height="180px" %}

The implicit array spacing operator has the highest precedence, hence initializer expressions which are not unary must be within parentheses.
{: .notice--info}

An index expression prefixed by `::` without an index name is fine too, and may be more readable in some circumstances.
{: .notice--info}

You can also define multiple dimensions by using more than one array count expressions. The rightmost expression will always be the element expression.

{% include sandboxframe.html qedsrc="tutorial/implicit3.qed" gui="0" height="120px" %}

The return type of the array expression is an array of elements. If the element expression type is void (or the element expression is a block using braces `{}`), there is no return type, that is, it acts like a `for` loop and cannot be stored in a variable.

{% include sandboxframe.html qedsrc="tutorial/implicit4.qed" gui="0" height="230px" %}

A cool thing with implicit arrays is they support postfix `++` and `--`, which dynamically add one more element or remove the last element. This is used in the "Bouncing balls" demo from the demo page to dynamically add or remove balls on the display.

Implicit arrays will become very useful for arrays of class instances, as we'll see soon.
{: .notice--info}
