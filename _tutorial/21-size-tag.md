---
title: "The `size` tag"
permalink: /tutorial/size-tag/
---

The size of the user interface area is determined by the UI elements. It is the minimal area where all UI elements fit in. Some UI elements do not have a determined size though, the ones with the `out` tag having a function pointer as value expression such as `rect`, `oval`, `roundRect`. These elements use sibling elements to determine their size.

{% include sandboxframe.html qedsrc="tutorial/size1.qed" code="60" gui="100" height="200px" %}

The `radius: 8;` tag specifies the corner radius for round rects (only applies to `out: roundRect;` tags).
{: .notice--info}

Most of the times though, you want to specify the size of drawing elements in pixels. The `size` tag does so, with an explicit array of two numbers.

{% include sandboxframe.html qedsrc="tutorial/size2.qed" code="60" gui="100" height="200px" %}

## Clipping

It often happens that you want a square as the size of an element. In this case, you may shorten the explicit array notation by using a single number. In this example, a 100x100 oval (a circle in fact) is shown.

{% include sandboxframe.html qedsrc="tutorial/size3.qed" code="60" gui="100" height="200px" %}

This rule also applies to the other tags taking a pair of numbers as we'll see.
{: .notice--info}

The `size` tag also applies for sizeable outputs, such as text. In this case, it supercedes the sizeable output size, clipping the output if the latter is too large.

{% include sandboxframe.html qedsrc="tutorial/size4.qed" code="65" gui="100" height="200px" %}
