---
title: "Class Calls"
permalink: /tutorial/calls/
---

Ok, we're at that point now where QED is parting ways with most other languages. Apart from instantiating objects, there is a ***second*** use case for QED classes. Since classes are declared as functions, *they could be used like function calls* by invoking them without the `new` operator.

{% include sandboxframe.html qedsrc="tutorial/calls1.qed" gui="0" height="200px" %}

I can hear some of you asking "Why?" as plain lowercase-first-letter functions handle calls already. Well, class calls are implemented differently and, above all, *do have unique advantages*, as we'll realize soon.
{: .notice--info}

So classes can act like functions for calls but the internal process is different. What happens under the hood is:

- a temporary `Double` object is created and executed
- upon reaching the `return` call, the execution is stopped
- the return value (if the class return type is not `void`) is returned to the caller
- the caller resumes its own execution using the return value if provided
- the temporary `Double` object, no longer in use, is silently dismissed.

A corollary of this rule, and a key difference between function and class calls is, if `return` is never called during the object execution, the application simply hangs.

{% include sandboxframe.html qedsrc="tutorial/calls2.qed" gui="0" height="350px" %}

While this may first look as a fundamental flaw, it truly is a blessing in disguise, as we'll see later.
{: .notice--warning}

The novelty here is not class calls by themselves, functions do it well. It's more about classes now having *a dual nature*, which means the possibility to be invoked with or without `new` in the same code. Let's show it and also present another caveat in the process, which is how class **instances** (not calls) deal with return values now present in classes?

{% include sandboxframe.html qedsrc="tutorial/calls3.qed" gui="0" height="200px" %}

The return value is used in the class call but lost in the instantiation. Let's solve it right away...
