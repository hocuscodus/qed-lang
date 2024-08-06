---
title: "Private Members"
permalink: /tutorial/private-members/
---

All class members (fields and methods) are declared public. Private members can be embedded a wrapper plain builder function (which first letter is lowercase to not make it a class):

{% include sandboxframe.html qedsrc="classes3.qed" gui="0" height="280px" %}

For pure object instantiation, it is better to use the `void` return type when declaring its class, since the object will never return anything. Which leads to a class return type becoming useful... read on.
{: .notice--info}

We only scratched the surface as to what QED classes can do. We're about to expand their functionality!
