---
title: "Integration with Javascript"
permalink: /tutorial/js-integration/
---

QED alone would not do much if it could not tap on the huge Javascript libraries. Therefore, it must fully integrate with JS.

In any QED code, whatever is under the JS delimiters `/$` and `$/` is interpreted as embedded JS code and is left untouched in the compiled JS code.

{% include sandboxframe.html qedsrc="tutorial/embedded-js1.qed" code="50" gui="0" height="100" %}

That may of course lead to errors when running the JS code, undetected by the QED compiler. You will have to debug the full JS code from the integrated browser debugger to find out and correct the problems.
{: .notice--info}

Integrating JS code is mostly useful in QED wrapper functions. The `println` function that we've used a lot in this tutorial, along with the most popular basic math functions are already integrated in the QED library for convenience.

You are free to implement your own wrappers for non integrated JS functions. For instance, if you want the hypothenuse `c` of a right-angled triangle having sides `a` and `b`, you may use the JS `Math.hypot` function. Wrap it under a QED hypothenuse function like this.

{% include sandboxframe.html qedsrc="tutorial/embedded-js2.qed" code="53" gui="0" height="220" %}

Of course, if you have developed your own JS libraries, you can create QED wrappers for their calls using that same method and use them in QED afterwards.

It is best to encode embedded JS into global QED wrappers. It might not work under nested QED functions, as the QED compiler may do name mangling on their names when generating JS code and you would have to check the compiled code to retrieve the mangled names.
{: .notice--warning}

There are some extra details I have to give for JS integration, when used in QED classes and to wrap asynchronous JS functions. Read on if you're curious.
{: .notice--info}
