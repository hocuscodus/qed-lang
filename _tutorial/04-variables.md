---
title: "Variables and primitive types"
permalink: /tutorial/variables/
---

Variables in QED are almost similar to their C/Java counterparts. A variable definition begins with a type, followed by a name and ending with an optional initialization expression. The primitive types are less numerous though: `bool`, `String`, `int` and `float` (the latter two using 64 bits). When not initialized, their default value is `false`, `""`, `0` and `0.0` respectively.

{% include sandboxframe.html qedsrc="variables1.qed" code="50" gui="0" height="160px" %}

There is another keyword to introduce: `var`. A variable declared as `var` will infer its type based on its initialization value type. Its type cannot change afterwards.

{% include sandboxframe.html qedsrc="variables2.qed" code="50" gui="0" height="160px" %}

However a `var` variable with no initialization value can hold any value from any type (a bit like a `void*` in C).

{% include sandboxframe.html qedsrc="variables3.qed" code="50" gui="0" height="160px" %}
