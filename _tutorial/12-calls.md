---
title: "Class Calls"
permalink: /tutorial/calls/
---

Ok, we're at that point now and here is what makes QED drift away from most other languages. Apart from instantiating objects, there is a ***second*** use case for QED classes. Since classes are declared as functions, they could be used as function calls as well by invoking them without the `new` operator.

{% include sandboxframe.html qedsrc="calls1.qed" gui="0" height="200px" %}

I can hear some of you asking "Why?" as functions handle calls already. Well, class calls are implemented differently and, above all, *do have unique advantages*, as we'll realize soon.
{: .notice--info}

So classes can act like functions for calls but the internal process is different. What happens under the hood is, a temporary internal object is created and executed. Upon the first `return` call, its execution ends and the return value (if the class return type is not `void`) is returned to the caller, which resumes its own execution with it.

A corollary of this rule, and a key difference between function and class calls is, if `return` is never called during the object execution, the application simply hangs.

{% include sandboxframe.html qedsrc="calls2.qed" gui="0" height="330px" %}

While this may first look as a fundamental flaw, it truly is a blessing in disguise, as we'll see soon.
{: .notice--warning}

The novelty here is not class calls by themselves, functions do it well. It's more than classes now have a dual nature and can be invoked both ways in the same code. That brings another caveat, which is how class instantiation deal with return values now present in classes?

{% include sandboxframe.html qedsrc="calls3.qed" gui="0" height="200px" %}

The return value is lost for the object version. Let's solve it right away...
