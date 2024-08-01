---
title: "Private Members"
permalink: /tutorial/private-members/
---

All class members (fields and methods) are declared public. Private members can be embedded a wrapper plain builder function (which first letter is lowercase):

{% include sandboxframe.html qedsrc="classes3.qed" gui="0" height="280px" %}

For pure object instantiation, it is better to use the `void` return type, since the object will never return anything. Which leads to an object returning something... read on.
