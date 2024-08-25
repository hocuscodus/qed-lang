---
title: "Functions"
permalink: /tutorial/functions/
---

Again, functions are defined the same way as in C.
```
type name(type parm1, type parm2, ...) {
  <body>
}
```
Unlike C though, nested functions are allowed and closures are fully supported.

{% include sandboxframe.html qedsrc="functions1.qed" gui="0" height="250px" %}

Functions are first-class citizens in QED. Functions types can be defined with any function, leaving their body empty with `{}`. Then you can use them as function types if followed by a star (the semantic of `*` is different from C in QED).

When used as lambdas, functions must always be embedded in a set of parentheses. You may use any function name but the preference would be `lambda` or `l` for clarity.

Also, and very important, the first alphabetic character must be lowercase. Uppercase-beginning "functions" do exist in QED but define something much more powerful as we'll see soon. When used as types (with a `*` suffix), keep the names' first letter lowercase.
