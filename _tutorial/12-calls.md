---
title: "Class Calls"
permalink: /tutorial/calls/
---

On the other hand, since classes are declared as functions, they could be used as function calls as well. Just invoke them without the new operator.

{% include sandboxframe.html qedsrc="classes10.qed" gui="0" height="230px" %}

So classes can act like functions for calls but the internal process is different. What happens under the hood is, a temporary object is created and executed. Upon the first return call, the temporary object is dismissed and the call ends, providing the return value (if non void).

If return() is never called during the object execution, the application simply hangs. While this may look as a curse, it truly is a blessing in disguise, as we'll see later.

Classes can be invoked both ways in the same code.

There is a problem here though. When used as an instance, the return value is lost.