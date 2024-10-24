---
title: "Event Handlers"
permalink: /tutorial/event-handlers/
---

To handle return statements in class instantiation, the `->` operator can be used to link a class instance with a handler expression (or block). In the handler, the internal `_ret` parameter holds the return value and is always defined using the right type (taken from the class declaration type).

{% include sandboxframe.html qedsrc="tutorial/calls4.qed" gui="0" height="200px" %}

If the class return type is `void`, `_ret` is not declared. Referring to it generates a compiler error.
{: .notice--info}

You may have remarked that the instance line `double of 7 = 14` is displayed *after* the `Done with both!` printout, even if it is before in the source code. This is expected. When the application has run its code, it does not end right after. It rather enters an **event loop** to retrieve and execute incoming events. The `->` operator generates an event when `return` is called. This event is handled when the event loop starts, at the end of the script.

So with class return types used with class instantiation and `->`, QED introduces the notion of an event when calling `return`. The `return` here really acts like a hypothetical `{PostMessage(_ret); return;}` code block that ends its execution right after posting the message, which will be handled by the event loop.

The event can be more complex than a basic predefined type; just define the class return type as another sophisticated class with many the fields (say `ReturnTypeClass`) you need and use `return new ReturnTypeClass(field1, field2, ...)`. The `_ret` parameter will be of type `ReturnTypeClass`.
{: .notice--info}

Also, we'll see that `return` can be called multiple times so the object can emit multiple events!
{: .notice--info}

It's now time to reap the benefits that dual nature classes bring.