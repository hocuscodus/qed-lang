---
title: "Event Handlers"
permalink: /tutorial/event-handlers/
---

To handle return statements in object instantiation, the `->` operator can be used to link a class instance with a handler expression (or block). In the handler, the internal _ret parameter holds the return value and is always defined using the right type (taken from the class declaration type).

{% include sandboxframe.html qedsrc="calls4.qed" gui="0" height="200px" %}

You may have remarked that the instance line is displayed after the `Done with both!` printout. This is expected. When the application has run its code, it does not end right after. It rather enters an **event loop** to retrieve and execute incoming events. The `->` operator generates an event when `return` is called in an object code. The event is handle when the event loop starts, at the end of the code.

So with class return types used with class instantiation and `->`, QED introduces the notion of an event when calling `return`. The `return` here really acts like a hypothetical `PostMessage` function that stops object execution right after so the message will be handled by an event loop.

The event can be complex; instead of using a basic predefined type, just define the class return type as a sophisticated class with many the fields you need.
{: .notice--info}

Also, we'll see that `return` can be called multiple times so the object can emit multiple events!
{: .notice--info}

It's now time to reap the benefits that dual nature classes bring.