---
title: "Functions"
permalink: /tutorial/functions/
---

Again, functions are defined the same way as in C.
```
type name(type1 parm1, type2 parm2, ...) {
  <body>
}
```
Unlike C though, nested functions are allowed and closures are fully supported.

{% include sandboxframe.html qedsrc="functions1.qed" gui="0" height="230px" %}

Functions are first-class citizens in QED. Functions types can be defined with any function, leaving their body empty. Then you can use them as types if followed by a star (the semantics are very different from C here.

When used as lambdas, functions must always be embedded in a set of parentheses. You may use any function name but the preference would be `lambda` or `l` for clarity.

As a sidenote, for plain functions, the first alphabetic character must be lowercase. Uppercase-beginning "functions" do exist in QED but define something slower but much more powerful as we'll see next. When used as types (with a `*` suffix), keep the names' first letter lowercase.

## FFI

We can almost add the types in this sections but they are a bit different from 
