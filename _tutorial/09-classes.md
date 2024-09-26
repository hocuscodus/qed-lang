---
title: "Classes"
permalink: /tutorial/classes/
---

QED has classes like many other languages so why aren't they part of the basic stuff? First, the way they are defined is different. Also, their functionality is expanded in some nifty way, as we'll see in this the rest of this tutorial.

There is no reserved keyword such as `class` to declare a QED class. Classes in QED are defined the same way functions are, with a return type, a name and a set of parameters, using the same syntax. So how does QED differentiate them from plain functions? By looking at the first letter of their name; if it is uppercase, we define a class. If lowercase, as specified before, we declare a function.

```
int myFunction(String arg1) {
  /* code */
}

int MyClass(String arg1) {
  /* code */
}
```

As an aside, nouns are by convention used for class names whereas verbs are used for plain function names (of course, QED does not check this).
{: .notice--info}

You may have plenty of questions at this point already, such as "Why a return type for classes?" All I can tell at this point is, please bear with me, it will all fall into place as you continue reading.

All class members (fields and methods) are declared public. Private members can be embedded a wrapper plain builder function (which first letter is lowercase to not make it a class):

{% include sandboxframe.html qedsrc="tutorial/classes3.qed" gui="0" height="280px" %}

For pure class instantiation, it is better to use the `void` return type when declaring its class, since the object will never return anything. Which leads to a class return type becoming useful... read on.
{: .notice--info}

We only scratched the surface as to what QED classes can do. We're about to expand their functionality!
