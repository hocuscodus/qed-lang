---
title: "Class Calls"
permalink: /tutorial/calls/
---

On the other hand, since classes are declared as functions, they could be used as function calls as well. Just invoke them without the new operator.

{% include sandboxframe.html qedsrc="classes2.qed" gui="0" height="230px" %}

Classes can be invoked both ways in the same code.

There is a problem here though. When used as an instance, the return value is lost.