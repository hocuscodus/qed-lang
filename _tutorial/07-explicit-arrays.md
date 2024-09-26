---
title: "Explicit arrays"
permalink: /tutorial/explicit-arrays/
---

Explicit arrays are arrays where elements are defined at initialization, within a pair of brackets after the declaration type (`[]`). The same bracket operator is used to access individual elements. The size() member function, accessible with the `.` operator, returns the size of the array.

{% include sandboxframe.html qedsrc="tutorial/explicit1.qed" gui="0" height="230px" %}

QED does not support null pointers, as per [good practice recommendations](https://en.wikipedia.org/wiki/Null_pointer#:~:text=In%202009%2C,last%20forty%20years.). I might implement in QED helper keywords such as `None` and `Some` someday but in the meantime, you may use an explicit array with one or zero element to emulate the null case.

{% include sandboxframe.html qedsrc="tutorial/explicit2.qed" gui="0" height="230px" %}
