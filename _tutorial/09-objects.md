---
title: "Object Instantiation"
permalink: /tutorial/objects/
---

Classes are of course used to instantiate objects comprising fields and methods.

The well-known `new` operator is used in QED to instantiate objects. The equally popular `.` operator allows access to fields and methods.

{% include sandboxframe.html qedsrc="classes1.qed" gui="0" height="230px" %}

Here the class declaration is the constructor, are fields and the nested function is a method. Upon instantating the class, are fields and is a method.

QED has no null instance, arrays, strings. Use an explicit array with one or zero element to emulate the null case.
